package io.github.csgroup.quizmaker.word;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.*;

import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;
import io.github.csgroup.quizmaker.data.quiz.QuizMetadata.MetadataType;

/**
 * Utility class for applying metadata placeholders to Word documents.
 *
 * <p>This class uses {@link TemplateReplacements} to define which placeholders
 * to look for in the document (e.g., "(Class)") and {@link QuizMetadata}
 * to provide the actual values (e.g., "350"). It supports run-aware
 * replacement while preserving formatting.</p>
 *
 * <p>Supported targets include paragraphs, tables, headers, and footers.</p>
 */
public class TemplateWriter {

	/**
	 * Applies metadata replacements from a {@link QuizMetadata} instance
	 * to a Word document template, using placeholder strings defined in
	 * a {@link TemplateReplacements} instance.
	 * 
	 * @param inputPath The path to the input .docx template
	 * @param outputPath The destination path for the filled document
	 * @param replacements A {@link TemplateReplacements} object defining which string each metadata type replaces (e.g., "(Class)")
	 * @param metadata A {@link QuizMetadata} object containing the values to insert (e.g., "350")
	 * @throws IOException If reading or writing the document fails
	 */
	public static void applyMetadata(Path inputPath, Path outputPath, TemplateReplacements replacements, QuizMetadata metadata) throws IOException {
		Map<String, String> tokenMap = new HashMap<>();
		for (MetadataType type : MetadataType.values()) {
			String placeholder = replacements.getReplacementText(type);
			String value = metadata.getValue(type);
			if (placeholder != null && !placeholder.isBlank() && value != null && !value.isBlank()) {
				tokenMap.put(placeholder, value);
			}
		}

		try (FileInputStream fis = new FileInputStream(inputPath.toFile());
				XWPFDocument document = new XWPFDocument(fis)) {

			for (XWPFHeader header : document.getHeaderList()) {
				for (XWPFParagraph p : header.getParagraphs()) {
					applyToParagraph(p, tokenMap);
				}
				for (XWPFTable t : header.getTables()) {
					applyToTable(t, tokenMap);
				}
			}

			for (XWPFFooter footer : document.getFooterList()) {
				for (XWPFParagraph p : footer.getParagraphs()) {
					applyToParagraph(p, tokenMap);
				}
				for (XWPFTable t : footer.getTables()) {
					applyToTable(t, tokenMap);
				}
			}

			for (XWPFParagraph paragraph : document.getParagraphs()) {
				applyToParagraph(paragraph, tokenMap);
			}

			for (XWPFTable table : document.getTables()) {
				applyToTable(table, tokenMap);
			}

			try (FileOutputStream out = new FileOutputStream(outputPath.toFile())) {
				document.write(out);
			}
		}
	}

	/**
	 * Recursively applies all replacements to every paragraph and nested table
	 * within a given Word table.
	 * 
	 * @param table The table to apply replacements to
	 * @param replacements A map of placeholder strings to their replacement values
	 */
	private static void applyToTable(XWPFTable table, Map<String, String> replacements) {
		for (XWPFTableRow row : table.getRows()) {
			for (XWPFTableCell cell : row.getTableCells()) {
				for (XWPFParagraph paragraph : cell.getParagraphs()) {
					applyToParagraph(paragraph, replacements);
				}
				for (XWPFTable nested : cell.getTables()) {
					applyToTable(nested, replacements);
				}
			}
		}
	}

	/**
	 * Replaces placeholder tokens inside a paragraph, even if they span
	 * multiple runs, while preserving the formatting of the original text.
	 * 
	 * @param paragraph The paragraph to apply replacements in
	 * @param replacements A map of placeholder strings to their replacement values
	 */
	private static void applyToParagraph(XWPFParagraph paragraph, Map<String, String> replacements) {
		boolean replaced;

		do {
			replaced = false;

			List<XWPFRun> runs = paragraph.getRuns();
			if (runs == null || runs.isEmpty()) return;

			StringBuilder fullText = new StringBuilder();
			List<Integer> runCharMap = new ArrayList<>();

			for (int i = 0; i < runs.size(); i++) {
				String text = runs.get(i).getText(0);
				if (text != null) {
					fullText.append(text);
					for (int j = 0; j < text.length(); j++) {
						runCharMap.add(i);
					}
				}
			}

			String combined = fullText.toString();

			for (var entry : replacements.entrySet()) {
				String token = entry.getKey();
				String replacement = entry.getValue();

				int start = combined.indexOf(token);
				if (start == -1) continue;

				int end = start + token.length() - 1;
				int startRun = runCharMap.get(start);
				int endRun = runCharMap.get(end);

				// Get style from the first run
				XWPFRun original = runs.get(startRun);
				RunStyle style = new RunStyle(original);

				// Remove all affected runs
				for (int i = endRun; i >= startRun; i--) {
					paragraph.removeRun(i);
				}

				// Insert a styled replacement run
				XWPFRun newRun = paragraph.insertNewRun(startRun);
				style.applyTo(newRun);
				newRun.setText(replacement);

				replaced = true;
				break; // break token loop and reprocess from scratch
			}

		} while (replaced);
	}

	/**
	 * Captures the style (bold, italic, underline, font, size, color) of a run
	 * so that it can be applied to a newly created run later.
	 */
	static class RunStyle {
		boolean bold;
		boolean italic;
		UnderlinePatterns underline;
		String font;
		int fontSize;
		String color;

		RunStyle(XWPFRun run) {
			bold = run.isBold();
			italic = run.isItalic();
			underline = run.getUnderline();
			font = getFont(run);
			fontSize = (run.getFontSizeAsDouble() != null) ? run.getFontSizeAsDouble().intValue() : 12;
			color = run.getColor();
		}

		void applyTo(XWPFRun run) {
			run.setBold(bold);
			run.setItalic(italic);
			run.setUnderline(underline);
			run.setFontFamily(font);
			run.setFontSize(fontSize);
			if (color != null) run.setColor(color);
		}

		/**
		 * Retrieves the font family of a run safely, falling back to "Times New Roman"
		 * if no font is explicitly set.
		 * 
		 * @param run The run from which to get the font family
		 * @return The font family name
		 */
		private static String getFont(XWPFRun run) {
			try {
				String font = run.getFontFamily();
				if (font == null || font.isEmpty()) {
					font = run.getFontFamily(XWPFRun.FontCharRange.ascii);
				}
				return (font != null && !font.isEmpty()) ? font : "Times New Roman";
			} catch (Exception e) {
				return "Times New Roman";
			}
		}
	}
}
