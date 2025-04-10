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

public class TemplateWriter {

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
     * Replaces tokens inside a paragraph even when split across multiple runs,
     * preserving the style of the first matched run.
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


