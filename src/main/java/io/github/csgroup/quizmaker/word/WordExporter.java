package io.github.csgroup.quizmaker.word;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import io.github.csgroup.quizmaker.data.Quiz;

/**
 * Responsible for taking a {@link Quiz} and turning it into a .docx file
 * For now, the values and tables are hard coded, but a function call into said class 
 * should give the values necessary to create from any user created or template file. 
 * 
 * @author Samuel Garcia
 */
public class WordExporter 
{
	public static final Logger logger = LoggerFactory.getLogger(WordExporter.class);

	// TODO add correct function parameters, change hardcoded values
	
	private Path testTemplate;
	private Path keyTemplate;
	
	public WordExporter() //throws Exception
	{
		String inputFilePath = "Test Template.docx";
        String outputFilePath = "Modified Template.docx";
        String searchText = "(Section)";
        String replaceText = "88";

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            for (XWPFHeader header : document.getHeaderList()) {
                replaceTextInHeaderFooter(header, searchText, replaceText);
            }
            for (XWPFFooter footer : document.getFooterList()) {
                replaceTextInHeaderFooter(footer, searchText, replaceText);
            }

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                replaceTextPreservingFormatting(paragraph, searchText, replaceText);
            }

            for (XWPFTable table : document.getTables()) {
                replaceTextInTable(table, searchText, replaceText);
            }

            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                document.write(fos);
            }

            // System.out.println("Replacement done! Output saved to: " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void exportTest(Quiz quiz, Path destination)
	{
		
	}
	
	public void exportAnswerKey(Quiz quiz, Path destination)
	{
		
	}
	
	/**
     * Replaces occurrences of a specific text within a paragraph while preserving the formatting.
     */
	private static void replaceTextPreservingFormatting(XWPFParagraph paragraph, String searchText, String replaceText) {
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs == null || runs.isEmpty()) return;

        StringBuilder fullText = new StringBuilder();
        List<RunStyle> runStyles = new ArrayList<>();
        Map<Integer, XWPFRun> runMap = new LinkedHashMap<>();

        int index = 0;
        for (XWPFRun run : runs) {
            if (run.getText(0) != null) {
                String text = run.getText(0);
                fullText.append(text);
                runStyles.add(new RunStyle(run));
                runMap.put(index, run);
                index += text.length();
            }
        }

        String fullTextStr = fullText.toString();
        int startIdx = fullTextStr.indexOf(searchText);
        if (startIdx == -1) return;

        int endIdx = startIdx + searchText.length();

        XWPFRun firstRun = runMap.get(startIdx);
        RunStyle firstRunStyle = new RunStyle(firstRun);

        for (int i = runs.size() - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }

        String before = fullTextStr.substring(0, startIdx);
        String after = fullTextStr.substring(endIdx);

        if (!before.isEmpty()) {
            XWPFRun beforeRun = paragraph.createRun();
            runStyles.get(0).applyTo(beforeRun);
            beforeRun.setText(before);
        }

        XWPFRun newRun = paragraph.createRun();
        firstRunStyle.applyTo(newRun);
        newRun.setText(replaceText);

        if (!after.isEmpty()) {
            XWPFRun afterRun = paragraph.createRun();
            runStyles.get(runStyles.size() - 1).applyTo(afterRun);
            afterRun.setText(after);
        }
    }

	/**
     * Stores and applies formatting styles of an XWPFRun object.
     */
    static class RunStyle {
        boolean isBold;
        boolean isItalic;
        UnderlinePatterns underline;
        String fontFamily;
        int fontSize;
        String color;

        RunStyle(XWPFRun run) {
            this.isBold = run.isBold();
            this.isItalic = run.isItalic();
            this.underline = run.getUnderline();
            this.fontFamily = getFontFamilySafely(run);
            this.fontSize = (run.getFontSizeAsDouble() != null) ? run.getFontSizeAsDouble().intValue() : 12;
            this.color = run.getColor();
        }

        void applyTo(XWPFRun run) {
            run.setBold(isBold);
            run.setItalic(isItalic);
            run.setUnderline(underline);
            run.setFontFamily(fontFamily);
            run.setFontSize(fontSize);
            if (color != null) run.setColor(color);
        }
    }

    /**
     * Safely retrieves the font family of an XWPFRun object, with a fallback to "Times New Roman".
     */
    private static String getFontFamilySafely(XWPFRun run) {
        try {
            String fontFamily = run.getFontFamily();
            if (fontFamily == null || fontFamily.isEmpty()) {
                fontFamily = run.getFontFamily(XWPFRun.FontCharRange.ascii);
            }
            return (fontFamily != null && !fontFamily.isEmpty()) ? fontFamily : "Times New Roman";
        } catch (Exception e) {
            return "Times New Roman";
        }
    }

    /**
     * Recursively replaces text in tables while preserving formatting.
     */
    private static void replaceTextInTable(XWPFTable table, String searchText, String replaceText) {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    replaceTextPreservingFormatting(paragraph, searchText, replaceText);
                }
                for (XWPFTable nestedTable : cell.getTables()) {
                    replaceTextInTable(nestedTable, searchText, replaceText);
                }
            }
        }
    }

    /**
     * Replaces text within headers and footers while preserving formatting.
     */
    private static void replaceTextInHeaderFooter(XWPFHeaderFooter headerFooter, String searchText, String replaceText) {
        try { //Old error dont know if this is necessary anymore
            for (XWPFParagraph paragraph : headerFooter.getParagraphs()) {
                replaceTextPreservingFormatting(paragraph, searchText, replaceText);
            }
            for (XWPFTable table : headerFooter.getTables()) {
                replaceTextInTable(table, searchText, replaceText);
            }
        } catch (Exception e) {
            // System.err.println("Skipping a header/footer due to XmlValueDisconnectedException: " + e.getMessage());
        }
    }
}


	