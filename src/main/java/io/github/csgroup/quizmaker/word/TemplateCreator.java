package io.github.csgroup.quizmaker.word;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.List;

import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblLayoutType;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;
import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;

/**
 * Utility class for creating a default Word document template when no external .docx template is provided.
 * <p>
 * The generated template includes:
 * <ul>
 *   <li>A custom header with class, professor, and date information</li>
 *   <li>A footer with page numbering</li>
 *   <li>Instructional text and a formatted numbered list</li>
 *   <li>A page break followed by a name and score section</li>
 * </ul>
 * This class is typically used as a fallback when the user does not supply a template path.
 * 
 * @author Samuel Garcia
 */
public class TemplateCreator {

	/**
	 * Creates a new Word document (.docx) with a predefined layout, header, footer, instructions, and metadata.
	 * <p>
	 * This method serves as a fallback when no template is supplied during export. It populates the document
	 * with metadata, test instructions, a name field, and page numbering.
	 *
	 * @param document   The {@link XWPFDocument} to populate.
	 * @param outputPath The path where the generated document will be saved.
	 * @param metadata   Metadata to embed into the file.
	 * @throws IOException If writing the file fails.
	 */
	public static void createDocument(XWPFDocument document, Path outputPath, GeneratedQuiz generatedQuiz) throws IOException
	{

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
	    setCellText(headerTable.getRow(0).getCell(0), "CS" + generatedQuiz.getQuizMetadata().getValue(QuizMetadata.MetadataType.ClassNum) + "-0" + generatedQuiz.getQuizMetadata().getValue(QuizMetadata.MetadataType.SectionNum) + "\n" + generatedQuiz.getQuizMetadata().getValue(QuizMetadata.MetadataType.Professor), ParagraphAlignment.LEFT);
	    setCellText(headerTable.getRow(0).getCell(1), "Test-" + generatedQuiz.getQuizMetadata().getValue(QuizMetadata.MetadataType.TestNum), ParagraphAlignment.CENTER);
	    setCellText(headerTable.getRow(0).getCell(2), generatedQuiz.getQuizMetadata().getValue(QuizMetadata.MetadataType.Date), ParagraphAlignment.RIGHT);
	    if(generatedQuiz.getTitle() != null)
	    {
	    	XWPFParagraph headerPara = header.createParagraph();
	    	headerPara.setAlignment(ParagraphAlignment.CENTER);
	    	XWPFRun headerRun = headerPara.createRun();
	    	headerRun.setFontFamily("Times New Roman");
	    	headerRun.setText(generatedQuiz.getTitle());
	    }
	    
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
	    XWPFRun nameRun = nameField.createRun();
	    nameRun.setFontFamily("Times New Roman"); 
	    nameRun.setFontSize(12);
	    nameRun.setText("Name: _____________________________________");
	    
	    LabelWriter labelWriter = new LabelWriter(document);
	    if (generatedQuiz.getDescription() != null && !generatedQuiz.getDescription().asText().isBlank())
	    {
	    	XWPFParagraph fallbackParagraph = document.createParagraph();
	    	XWPFRun descRun = fallbackParagraph.createRun();
    		descRun.addBreak();
    	    descRun.setText("Quiz Description:");
    		descRun.addBreak();
	    	labelWriter.writeInline(new Label(generatedQuiz.getDescription().asText(), Label.Type.html), fallbackParagraph);
	    	
	    }
	    
	    // Create a break between name field and numbered list
        XWPFParagraph testInstruction = document.createParagraph();
        testInstruction.setSpacingAfter(0);
        testInstruction.setSpacingBetween(1, LineSpacingRule.AUTO);
        XWPFRun preListRun = testInstruction.createRun();
        preListRun.setFontFamily("Times New Roman");
        preListRun.setFontSize(12);
        preListRun.addBreak();
        preListRun.setText("Test Instructions:");
	    
	    // Setup for creating numbered list
        XWPFNumbering numbering = document.createNumbering();
        String numId = createNumberingStyle(numbering); // Define numbering style

        // Add numbered list items matching document format
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
        addNumberedParagraph(document, "The base time limit on this exam is (Time) minutes.", numId, 0);
        
	    // Create space between numbered list and the section list
	    XWPFParagraph sectionPar = document.createParagraph();
	    sectionPar.setSpacingAfter(0);
	    sectionPar.setSpacingBetween(1, LineSpacingRule.AUTO);
	    XWPFRun sectionRun = sectionPar.createRun();
	    sectionRun.setFontFamily("Times New Roman");
	    sectionRun.setFontSize(12);
	    sectionRun.addBreak();
	    sectionRun.addBreak();
	    sectionRun.addBreak();
	    sectionRun.setText("This test covers sections: (SectionList)");
	    sectionRun.addBreak();
	    sectionRun.setText("(KeyText)");
	    
	    
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
	    
	    try (FileOutputStream out = new FileOutputStream(outputPath.toFile())) {
			document.write(out);
		}
	}

	/**
	 * Sets the text content of a {@link XWPFTableCell} with proper paragraph alignment and formatting.
	 * Line breaks within the input text are preserved.
	 *
	 * @param cell       The cell to populate.
	 * @param text       The text content, with optional newline characters (`\n`).
	 * @param alignment  Paragraph alignment (e.g., LEFT, CENTER, RIGHT).
	 */
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

