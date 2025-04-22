package io.github.csgroup.quizmaker.word;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.text.DecimalFormat;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.questions.*;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion.ResponseLength;

/**
 * Responsible for writing {@link Question Questions} to a .docx file <br>
 * Currently only supports basic question writing of a written response question
 * 
 * @author Samuel Garcia 
 */
public class QuestionWriter
{
	public static final Logger logger = LoggerFactory.getLogger(QuestionWriter.class);
	private static final DecimalFormat POINT_FORMAT = new DecimalFormat("#.##");
	
	private final XWPFDocument document;
	private final boolean isKey;
	private final Set<Class<?>> questionTypesWritten = new HashSet<>();

	
	/**
	 * Constructs a QuestionWriter for the given document.
	 * @param document The XWPFDocument to which questions will be written.
	 * @param isKey whether or not an answer key is being written
	 */
	public QuestionWriter(XWPFDocument document, boolean isKey) 
	{
		this.document = document;
		this.isKey = isKey;
	}
	
	/**
	 * Creates a page blank before the next paragraph,
	 * which is used to separate the questions from the front pages.
	 */
	public void insertPageBreak()
	{
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setPageBreak(true);
	}
	
	/**
	 * Determines the type of question to be written to the quiz and calls the appropriate writer
	 * 
	 * @param q The question to be written
	 * @param questionNumber Number of the question to write
	 * @throws IOException If an error occurs during writing the label
	 */
	public void writeQuestion(Question q, int questionNumber) throws IOException 
	{
		Class<?> questionClass = q.getClass();
		if (!questionTypesWritten.contains(questionClass)) 
		{
			writeInstructionsForQuestion(q);
			questionTypesWritten.add(questionClass);
		}
		
		switch (q) {
		case WrittenResponseQuestion wr -> writeWrittenResponse(wr, questionNumber);
		case FillInTheBlankQuestion fitb -> writeFillBlank(fitb, questionNumber);
		case MatchingQuestion match -> writeMatching(match, questionNumber);
		case MultipleChoiceQuestion mc -> writeMultipleChoice(mc, questionNumber);
		default -> logger.error("Unsupported question type: {}", q.getClass().getSimpleName());
		}
	}
	
	/**
	 * Writes out instructions for a question if it is the first time that question type has been displayed
	 * 
	 * @param q The question that will be used to determine type
	 * @throws IOException If writing fails
	 */
	private void writeInstructionsForQuestion(Question q) throws IOException {
	    XWPFParagraph instructionParagraph = document.createParagraph();
	    XWPFRun run = instructionParagraph.createRun();
	    run.setBold(true);

	    String instructionText = switch (q) {
	        case MultipleChoiceQuestion __ -> "Select the best answer for each of the following multiple choice questions.";
	        case MatchingQuestion __ -> "Match each item in the left column with the correct item in the right column.";
	        case FillInTheBlankQuestion __ -> "Fill in the blanks with the correct words or phrases.";
	        case WrittenResponseQuestion __ -> "Provide a written response to the following questions.";
	        default -> null;
	    };

	    if (instructionText != null) {
	        run.setText(instructionText);
	    }
	}


	/**
	 * Writes a {@link WrittenResponseQuestion} to the Word Document. Depending on the {@link ResponseLength},
	 * the amount of page available to answer will change.
	 * 
	 * @param q The written response question to write
	 * @throws IOException If an error occurs during label writing
	 */
	public void writeWrittenResponse(WrittenResponseQuestion q, int questionNumber) throws IOException
	{
		LabelWriter labelWriter = new LabelWriter(document);
		if(q.getResponseLength() == ResponseLength.Essay)
		{
			XWPFParagraph paragraph = document.createParagraph();
			paragraph.setPageBreak(true);
		}
		XWPFParagraph paragraph = document.createParagraph();
		labelWriter.writeInline(buildQuestionLabel(q.getLabel(), questionNumber, q.getPoints(), q.isAbet()), paragraph);
		switch (q.getResponseLength()) {
			case Line -> {
				if(isKey)
				{
					labelWriter.write(redLabel(q.getAnswer()));
				}
				else
				{
					addBlankLines(2);
				}
			}
			case Paragraph -> {
				if(isKey)
				{
					labelWriter.write(redLabel(q.getAnswer()));
					addBlankLines(4); //May not be necessary, would require a lot of code to dynamically allocate lines
					//Can come back here if needed
				}
				else
				{
					addBlankLines(8);
				}
			}
			case Essay -> {
				if(isKey)
				{
					labelWriter.write(redLabel(q.getAnswer()));
				}
				document.createParagraph().setPageBreak(true);
			}
		}
	}
	
	/**
	 * Creates blank lines to render onto a Word Document.
	 * 
	 * @param count Amount of blank lines to print
	 */
	private void addBlankLines(int count)
	{
		for(int i = 0; i < count; i++)
		{
			XWPFParagraph paragraph = document.createParagraph();
			XWPFRun run = paragraph.createRun();
			run.setText(" "); //Can be changed to a label write if needed
		}
	}

	/**
	 * Writes a {@link FillInTheBlankQuestion} to the Word Document. This changes the tags to blanks,
	 * and if {@code isKey} is true, will print out the answers in order of blanks.
	 * 
	 * @param q The fill in the blank question to write
	 * @throws IOException If an error occurs during label writing
	 */
	public void writeFillBlank(FillInTheBlankQuestion q, int questionNumber) throws IOException
	{
		LabelWriter labelWriter = new LabelWriter(document);
		String tagBlank = q.getLabel().asText();
		tagBlank = tagBlank.replaceAll("\\[(.+?)\\]", "__________");
		Label tagBlankLabel = new Label(tagBlank, q.getLabel().getType());
		XWPFParagraph paragraph = document.createParagraph();
		labelWriter.writeInline(buildQuestionLabel(tagBlankLabel, questionNumber, q.getPoints(), q.isAbet()),  paragraph);
		
		if(isKey)
		{
			int index = 1;
			for(String tag : q.getTags())
			{
				BlankAnswer answer = q.getAnswer(tag);
				if (answer != null)
				{
					String addIndex =  "	Blank " +  index + ": "; 
					labelWriter.write(redLabel(new Label(addIndex + answer.asText())));
				}
				index++;
			}
		}
	}
	
	/**
	 * Writes a {@link MultipleChoiceQuestion} to the Word document. If {@code isKey} is true,
	 * answers will be made red.
	 * 
	 * @param q The multiple choice question to be written
	 * @throws IOException If an error occurs during label writing
	 */
	public void writeMultipleChoice(MultipleChoiceQuestion q, int questionNumber) throws IOException
	{
		LabelWriter labelWriter = new LabelWriter(document);
		XWPFParagraph questionParagraph = document.createParagraph();
		labelWriter.writeInline(buildQuestionLabel(q.getLabel(), questionNumber, q.getPoints(), q.isAbet()), questionParagraph);
		
		List <SimpleAnswer> answerLabels = q.getAnswers();
		Collections.shuffle(answerLabels); 
		char optionLetter = 'a';
		for (SimpleAnswer answer : answerLabels)
		{
			String prefix = "\t";
			XWPFParagraph paragraph = document.createParagraph();
			labelWriter.writeInline(new Label(prefix), paragraph);
			if(isKey && q.isCorrect(answer))
			{
				var ansLabel = new Label(optionLetter + ". " + answer.asText()); //Change this to lettered list format and grid if small
				labelWriter.writeInline(redLabel(ansLabel), paragraph);
				continue;
			}
			
			if(answer.getLabel().getType() == Label.Type.html)
			{
				var ansLabel = new Label(optionLetter + ". " + answer.asText(), Label.Type.html); 
				labelWriter.writeInline(ansLabel, paragraph);
			}
			else
			{
				var ansLabel = new Label(optionLetter + ". " + answer.asText());
				labelWriter.writeInline(ansLabel, paragraph);
			}
			optionLetter++;
		}
		
		
	}
	
	/**
	 * Writes a {@link MatchingQuestion} to the Word document, using a 3-column table layout.
	 * Actual rendering of this table is from {@link #writeMatchingTable(MatchingQuestion, LabelWriter).
	 * 
	 * @param q The matching question to write
	 * @throws IOException If an error occurs during writing
	 */
	public void writeMatching(MatchingQuestion q, int questionNumber) throws IOException
	{
		LabelWriter labelWriter = new LabelWriter(document);
		XWPFParagraph paragraph = document.createParagraph();
		labelWriter.writeInline(buildQuestionLabel(q.getLabel(), questionNumber, q.getPoints(), q.isAbet()), paragraph);
		writeMatchingTable(q, labelWriter);
		
	}
	/**
	 * Writes a matching question to the Word document using a 3-column borderless table layout,
	 * with column 1 being the left label, column 2 being a spacer symbol, and column 3 being the right label.<p>
	 * If {@code isKey} is false, the right-side is shuffled to randomize the question.
	 * 
	 * @param q The {@link MatchingQuestion} to write.
	 * @param labelWriter The {@link LabelWriter} used to render question text and labels.
	 * @throws IOException If label rendering fails due to I/O or formatting issues.
	 */
	private void writeMatchingTable(MatchingQuestion q, LabelWriter labelWriter) throws IOException
	{	
		List<MatchingAnswer> answers = q.getAnswers();
		List<Label> leftLabels = new ArrayList<>();
		List<Label> rightLabels = new ArrayList<>();
		
		for (MatchingAnswer answer : answers)
		{
			leftLabels.add(answer.getLeft());
			rightLabels.add(answer.getRight());
		}
		
		if(!isKey)
		{
			//This can be changed to another randomizer method
			Collections.shuffle(rightLabels);
		}
		
		XWPFTable table = document.createTable(answers.size(), 3);
		removeTableBorders(table);
		
		CTTblGrid grid = table.getCTTbl().addNewTblGrid();
		grid.addNewGridCol().setW(BigInteger.valueOf(2000));
		grid.addNewGridCol().setW(BigInteger.valueOf(4000));
		grid.addNewGridCol().setW(BigInteger.valueOf(3000));
		
		for(int i = 0; i < answers.size(); i++)
		{
			XWPFTableRow row = table.getRow(i);
			
			XWPFTableCell leftCell = row.getCell(0);
			leftCell.setWidth("2000");
			XWPFParagraph leftPara = leftCell.getParagraphs().get(0);
			new LabelWriter(document).writeInline(leftLabels.get(i), leftPara);
			
			XWPFTableCell spacerCell = row.getCell(1);
			spacerCell.setWidth("4000");
			spacerCell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(4000));
			spacerCell.getCTTc().getTcPr().getTcW().setType(STTblWidth.DXA);
			XWPFParagraph spacerPara = spacerCell.getParagraphs().get(0);
			spacerPara.setAlignment(ParagraphAlignment.CENTER);
			Label spacerSymbol = isKey ? redLabel(new Label("→")) : new Label(" "); //Change to dots that span screen
			new LabelWriter(document).writeInline(spacerSymbol, spacerPara);

			XWPFTableCell rightCell = row.getCell(2);
			rightCell.setWidth("3000");
			XWPFParagraph rightPara = rightCell.getParagraphs().get(0);
			if (isKey)
			{
				new LabelWriter(document).writeInline(redLabel(rightLabels.get(i)), rightPara);
			}
			else
			{
				new LabelWriter(document).writeInline(rightLabels.get(i), rightPara);
			}
		}
	}
	
	/**
	 * Removes all visible borders from the specified Apache POI table.
	 * @param table The {@link XWPFTable} from which borders should be removed.
	 */
	private void removeTableBorders(XWPFTable table)
	{
		CTTblBorders borders = table.getCTTbl().getTblPr().addNewTblBorders();
		
		borders.addNewTop().setVal(STBorder.NONE);
		borders.addNewBottom().setVal(STBorder.NONE);
		borders.addNewLeft().setVal(STBorder.NONE);
		borders.addNewRight().setVal(STBorder.NONE);
		borders.addNewInsideH().setVal(STBorder.NONE);
		borders.addNewInsideV().setVal(STBorder.NONE);
	}
	
	/**
	 * Turns a label red to signify an answer for the answer key.
	 *
	 * @param original The original label to turn red
	 * @return A label that is coded in HTML to be red
	 */
	private Label redLabel(Label original)
	{
		return new Label("<span style='color:#FF0000'>" + original.asText() + "</span>", Label.Type.html);
	}

	/**
	 * Builds a new Label containing the question number, the original label, and point value.
	 * Preserves the label's formatting (plain or HTML).
	 *
	 * @param label The original question label.
	 * @param number The question number.
	 * @param points The number of points for this question.
	 * @param isAbet If the question is ABET or not.
	 * @return A new Label with numbering and points included.
	 */
	private Label buildQuestionLabel(Label label, int number, float points, boolean isAbet) {
		String prefix = number + ". ";
		String formattedPoints = POINT_FORMAT.format(points);
		String abet = isKey && isAbet 
				? "(ABET)"
				: "";
	    String html = label.asText(); 
	    String updated = prefix + html + " (Points: " + formattedPoints + ") " + abet;
	    return new Label(updated, Label.Type.html);
	}
}
