package io.github.csgroup.quizmaker.word;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;

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

	// TODO add more template files, add correct function parameters
	
	private Path testTemplate;
	private Path keyTemplate;
	
	public WordExporter() //throws Exception
	{
		// TODO add check for if a template is being used or if a new document is being created
		// Necessary for document creating
        XWPFDocument document = new XWPFDocument();

        // Create a header (Header and footer creation might move to function calls in the future for organization)
        XWPFHeader header = document.createHeader(HeaderFooterType.DEFAULT);

        // Create a table in the header to allow for justification (1 row and 3 columns for left, center, and right)
        XWPFTable headerTable = header.createTable(1, 3);

        // Set table width to 100% (full page width) and use fixed layout
        headerTable.setWidth("100%");
        headerTable.setTableAlignment(TableRowAlign.CENTER);
        headerTable.getCTTbl().getTblPr().addNewTblW().setW(BigInteger.valueOf(9100));
        headerTable.setWidthType(TableWidthType.PCT);
        headerTable.getCTTbl().addNewTblPr().addNewTblLayout().setType(STTblLayoutType.FIXED); 

        // Remove table borders
        headerTable.getCTTbl().getTblPr().unsetTblBorders();

        // Set each column to have an equal distribution
        headerTable.getRow(0).getCell(0).setWidth("33.3%");
        headerTable.getRow(0).getCell(1).setWidth("33.3%");
        headerTable.getRow(0).getCell(2).setWidth("33.3%");

        // Function calls to correctly set the text in each cell
        setCellText(headerTable.getRow(0).getCell(0), "CS(Class)-0(Section)\n(Professor)", ParagraphAlignment.LEFT);
        setCellText(headerTable.getRow(0).getCell(1), "Test (Test)", ParagraphAlignment.CENTER);
        setCellText(headerTable.getRow(0).getCell(2), "(Date)", ParagraphAlignment.RIGHT);
        
        // Create footer with correct properties
        XWPFFooter footer = document.createFooter(HeaderFooterType.DEFAULT);
        XWPFParagraph footerParagraph = footer.createParagraph();
        footerParagraph.setAlignment(ParagraphAlignment.LEFT);

        // Create static text for the footer
        XWPFRun footerRun = footerParagraph.createRun();
        footerRun.setFontFamily("Times New Roman");
        footerRun.setFontSize(10);
        footerRun.setText("Page | ");
        
        // Set up for creating dynamic page numbers in the footer
        XWPFRun pageNumberRun = footerParagraph.createRun();
        pageNumberRun.setFontFamily("Times New Roman");
        pageNumberRun.setFontSize(10);

        // Begin Field
        CTFldChar fldCharBegin = pageNumberRun.getCTR().addNewFldChar();
        fldCharBegin.setFldCharType(STFldCharType.BEGIN);

        // Field Instruction ("PAGE")
        XWPFRun pageNumInstr = footerParagraph.createRun();
        pageNumInstr.setFontFamily("Times New Roman");
        pageNumInstr.setFontSize(10);
        CTText instrText = pageNumInstr.getCTR().addNewInstrText();
        instrText.setStringValue("PAGE \\* MERGEFORMAT"); // Proper format expected to insert page number
        instrText.setSpace(SpaceAttribute.Space.PRESERVE);

        // End Field
        XWPFRun fldCharEndRun = footerParagraph.createRun();
        fldCharEndRun.setFontFamily("Times New Roman");
        fldCharEndRun.setFontSize(10);
        CTFldChar fldCharEnd = fldCharEndRun.getCTR().addNewFldChar();
        fldCharEnd.setFldCharType(STFldCharType.END);

        // Create name field text
        XWPFParagraph nameField = document.createParagraph();
        nameField.setSpacingAfter(0);
        nameField.setSpacingBetween(1, LineSpacingRule.AUTO);
        nameField.setAlignment(ParagraphAlignment.RIGHT); 
        XWPFRun run1 = nameField.createRun();
        run1.setFontFamily("Times New Roman"); 
        run1.setFontSize(12);
        run1.setText("Name: _____________________________________");
        
        // Create a break between name field and numbered list
        XWPFParagraph paragraph2 = document.createParagraph();
        paragraph2.setSpacingAfter(0);
        paragraph2.setSpacingBetween(1, LineSpacingRule.AUTO);
        XWPFRun run2 = paragraph2.createRun();
        run2.setFontFamily("Times New Roman");
        run2.setFontSize(12);
        run2.addBreak();
        run2.setText("Test Instructions:");

        // Setup for creating numbered list
        XWPFNumbering numbering = document.createNumbering();
        String numId = createNumberingStyle(numbering); // Define numbering style

        // Add numbered list items matching document format 
        // TODO change this to a loop for dynamic numbered list
        addNumberedParagraph(document, "Make sure your test has all the pages.", numId, 0);
        addNumberedParagraph(document, "Do your own work.", numId, 0);
        addNumberedParagraph(document, "Show your work. It is impossible to give partial credit if you do not show your work.", numId, 0);
        addNumberedParagraph(document, "When you finish the exam turn it in at the front desk and immediately leave the room. Please do not hang around outside the classroom.", numId, 0);
        addNumberedParagraph(document, "Do not use any paper other than what is provided. Use the back of these pages if needed.", numId, 0);
        addNumberedParagraph(document, "No books, notes or other materials are allowed.", numId, 0);
        addNumberedParagraph(document, "Only simple calculators are allowed. The calculator cannot store text.", numId, 0);
        addNumberedParagraph(document, "Put your name on this page and the top of the next page.", numId, 0);
        addNumberedParagraph(document, "Do NOT fold this exam and leave all the pages stapled together.", numId, 0);
        addNumberedParagraph(document, "If there are provided reference information those sheets may be removed. Those sheets do NOT need to be returned with the test.", numId, 0);
        addNumberedParagraph(document, "If you do not understand what the problem is asking for raise your hand or come to the front of the class.", numId, 0);
        addNumberedParagraph(document, "Do not communicate with other students. Talk only to the proctor.", numId, 0);
        addNumberedParagraph(document, "If you need a break during the exam, ask the proctor first. You must leave the exam and your cell phone in the classroom.", numId, 0);
        addNumberedParagraph(document, "This is a ", "(Point)", " point exam.", numId, 0);
        addNumberedParagraph(document, "The time limit on this exam is (Time) minutes.", numId, 0);
        
        // Create space between numbered list and the section list
        XWPFParagraph paragraph3 = document.createParagraph();
        paragraph3.setSpacingAfter(0);
        paragraph3.setSpacingBetween(1, LineSpacingRule.AUTO);
        XWPFRun run3 = paragraph3.createRun();
        run3.setFontFamily("Times New Roman");
        run3.setFontSize(12);
        run3.addBreak();
        run3.addBreak();
        run3.addBreak();
        run3.setText("This test covers sections: (SectionList)");
        
        
        // Setup to create the name and score table
        XWPFTable nameTable = document.createTable(1, 2);
        nameTable.setWidth("100%");
        nameTable.getCTTbl().getTblPr().addNewTblInd().setW(BigInteger.ZERO);
        nameTable.setTableAlignment(TableRowAlign.CENTER);
        nameTable.getCTTbl().getTblPr().addNewTblW().setW(BigInteger.valueOf(9200));
        nameTable.setTableAlignment(TableRowAlign.CENTER);
        nameTable.setWidthType(TableWidthType.PCT);
        nameTable.getCTTbl().addNewTblPr().addNewTblLayout().setType(STTblLayoutType.FIXED);
        
        nameTable.getRow(0).getCell(0).setWidth("66.7%");
        nameTable.getRow(0).getCell(1).setWidth("33.3%");
        
        // The first cell needs to have a page break to ensure that the table is on the top of the next page.
        XWPFTableCell cell1 = nameTable.getRow(0).getCell(0);
        XWPFParagraph cellParagraph = cell1.getParagraphs().get(0);
        cellParagraph.setPageBreak(true);
        setCellText(nameTable.getRow(0).getCell(0), "  Name: \n ", ParagraphAlignment.LEFT);
        setCellText(nameTable.getRow(0).getCell(1), "  Score:", ParagraphAlignment.LEFT);
        
        
        
        // Save document
        try {
        	File f = File.createTempFile("TestTemplate", ".docx");
        	FileOutputStream fo = new FileOutputStream(f);
        	document.write(fo);
        	Desktop.getDesktop().open(f);
        }
        catch (Exception e) {
	        e.printStackTrace();
	    }
        
        // This will be for actually saving the document
        //FileOutputStream out = new FileOutputStream(new File("TestTemplate.docx"));
        //document.write(out);
        //out.close();
	}

 
	
	public void exportTest(Quiz quiz, Path destination)
	{
		
	}
	
	public void exportAnswerKey(Quiz quiz, Path destination)
	{
		
	}
	
	// Function to correctly set text inside of a table cell
    private static void setCellText(XWPFTableCell cell, String text, ParagraphAlignment alignment) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0); // Get the first paragraph in the cell
        paragraph.setAlignment(alignment);
        paragraph.setSpacingAfter(0);
        paragraph.setSpacingBetween(1, LineSpacingRule.AUTO);

        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        // Insert new line within the same cell
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            run.setText(lines[i]); // Set the text
            if (i < lines.length - 1) {
                run.addBreak(); // Add a new line
            }
        }
    }
    
    // Function to create a correctly set numbered list
    private static String createNumberingStyle(XWPFNumbering numbering) {
        CTAbstractNum abstractNum = CTAbstractNum.Factory.newInstance();
        abstractNum.setAbstractNumId(BigInteger.ZERO);

        // Level 0 (Main Numbering: 1., 2., 3.)
        CTLvl lvl0 = abstractNum.addNewLvl();
        lvl0.setIlvl(BigInteger.ZERO);
        lvl0.addNewNumFmt().setVal(STNumberFormat.DECIMAL);
        lvl0.addNewLvlText().setVal("%1.");
        lvl0.addNewStart().setVal(BigInteger.ONE);
        lvl0.addNewPPr().addNewInd().setLeft(BigInteger.valueOf(360)); // Indentation for level 0
        
        CTRPr rPr = lvl0.addNewRPr(); // Create a run properties section
        CTFonts fonts = rPr.addNewRFonts();
        fonts.setAscii("Times New Roman");
        fonts.setHAnsi("Times New Roman");
        rPr.addNewSz().setVal(BigInteger.valueOf(24)); // Font size uses half-points

        // Level 1 (Sub-numbering: a., b., c.)
        CTLvl lvl1 = abstractNum.addNewLvl();
        lvl1.setIlvl(BigInteger.ONE);
        lvl1.addNewNumFmt().setVal(STNumberFormat.LOWER_LETTER);
        lvl1.addNewLvlText().setVal("%2.");
        lvl1.addNewStart().setVal(BigInteger.ONE);
        lvl1.addNewPPr().addNewInd().setLeft(BigInteger.valueOf(720));
        
        CTRPr rPr1 = lvl1.addNewRPr();
        CTFonts fonts1 = rPr1.addNewRFonts();
        fonts1.setAscii("Times New Roman");
        fonts1.setHAnsi("Times New Roman");
        rPr1.addNewSz().setVal(BigInteger.valueOf(24));

        XWPFAbstractNum xwpfAbstractNum = new XWPFAbstractNum(abstractNum);
        BigInteger abstractNumID = numbering.addAbstractNum(xwpfAbstractNum);
        return numbering.addNum(abstractNumID).toString(); // Returns the numId
    }

    // Function to add a numbered list paragraph with proper indentation
    private static void addNumberedParagraph(XWPFDocument document, String beforeBold, String boldText, String afterBold, String numId, int level) {
    	XWPFParagraph paragraph = document.createParagraph();
        paragraph.setNumID(new BigInteger(numId)); // Apply numbering
        paragraph.setNumILvl(BigInteger.valueOf(level)); // Set level (0 = main, 1 = sub-item)
        paragraph.setSpacingAfter(0);
        paragraph.setSpacingBetween(1, LineSpacingRule.AUTO); 
        
        // Text before bolding
        XWPFRun run1 = paragraph.createRun();
        run1.setFontFamily("Times New Roman");
        run1.setFontSize(12);
        run1.setText(beforeBold);

        // Bold text
        XWPFRun boldRun = paragraph.createRun();
        boldRun.setFontFamily("Times New Roman");
        boldRun.setFontSize(12);
        boldRun.setBold(true); // Make bold
        boldRun.setText(boldText);

        // Normal text after bold
        XWPFRun run2 = paragraph.createRun();
        run2.setFontFamily("Times New Roman");
        run2.setFontSize(12);
        run2.setText(afterBold);       
    }
    
    // Overloaded function for cases without bold text
    private static void addNumberedParagraph(XWPFDocument document, String noBold, String numId, int level) {
        addNumberedParagraph(document, noBold, "", "", numId, level);
    }

}