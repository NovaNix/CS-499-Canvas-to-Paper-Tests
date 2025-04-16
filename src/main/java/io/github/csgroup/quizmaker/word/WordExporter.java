package io.github.csgroup.quizmaker.word;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.questions.*;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion.ResponseLength;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;
import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;

/**
 * Responsible for taking a {@link Quiz} and turning it into a .docx file <br>
 * Currently supports writing to and editing a template file, along with template creation
 * 
 * @author Samuel Garcia 
 */
public class WordExporter 
{
	public static final Logger logger = LoggerFactory.getLogger(WordExporter.class);
	
	//Make Constructor with paths, verify paths as entered.
	/**
	 * Exports a quiz as a Word document (.docx), using a provided template if available.
	 * <p>
	 * If a valid template is not provided, a blank document is created. Metadata placeholders
	 * may be replaced in the template if replacements are defined. When exporting an answer key,
	 * special styling (like red "KEY" indicators and red answer text) is applied.
	 * </p>
	 *
	 * @param quiz          The quiz to export.
	 * @param templatePath  The path to a .docx file to use as a template. If null or blank, a blank document is created.
	 * @param destinationPath The output path where the .docx will be written. Must be non-null and writable.
	 * @param isKey         Whether this export represents an answer key (applies special styling).
	 * @throws IOException  If any file path is invalid, unreadable, unwritable, or export fails.
	 */
	public void exportTest(GeneratedQuiz quiz, Path template, Path destination, boolean isKey) throws IOException
	{
		// Here are some example questions and metadata to test the code with
		// These should be removed or moved to a test file eventually
		Quiz testQuiz = new Quiz("test");
		// Written Response Question
		var writtenResponse = new WrittenResponseQuestion("Written Test", 1);
		writtenResponse.setLabel(new Label("This is a written response"));
		writtenResponse.setAnswer("And this should be the answer!");
		writtenResponse.setResponseLength(ResponseLength.Line);
		
		// Fill in the Blank Question
		FillInTheBlankQuestion fitb = new FillInTheBlankQuestion("Java was created by [0].", 5);
		fitb.setAnswer("0", new BlankAnswer(1, "James Gosling"));
		        
		// Matching Question
		MatchingQuestion match = new MatchingQuestion("Q4", "Match Concepts", 4);
		match.setLabel(new Label("Match the programming concepts to their definitions:"));
		match.addAnswer(new MatchingAnswer(1, "Encapsulation", "Bundling data with methods"));
		match.addAnswer(new MatchingAnswer(2, "Inheritance", "Acquiring properties from a parent class"));
		match.addAnswer(new MatchingAnswer(3, "Abstraction", "Hiding implementation details"));
		        
		// Multiple Choice Question
		MultipleChoiceQuestion mc = new MultipleChoiceQuestion("Q3", "Java Collection Types", 4);
		mc.setLabel(new Label("Which of the following are part of the Java Collections Framework?"));
		mc.addAnswer(new SimpleAnswer(1, "HashMap"), true);
		mc.addAnswer(new SimpleAnswer(2, "ArrayList"), true);
		mc.addAnswer(new SimpleAnswer(3, "Thread"), false);
		mc.addAnswer(new SimpleAnswer(4, "File"), false);
		        
		testQuiz.addQuestion(writtenResponse);
		testQuiz.addQuestion(fitb);
		testQuiz.addQuestion(match);
		testQuiz.addQuestion(mc);
		testQuiz.regenerate();
		quiz = testQuiz.getGenerated();
		QuizMetadata metadata = buildTestMetadata(quiz);
		TemplateReplacements test  = new TemplateReplacements();
		test.setReplacementString(QuizMetadata.MetadataType.ClassNum, "(Class)");
		test.setReplacementString(QuizMetadata.MetadataType.SectionNum, "(Section)");
		test.setReplacementString(QuizMetadata.MetadataType.Points, "(Points)");
		test.setReplacementString(QuizMetadata.MetadataType.Minutes, "(Minutes)");
		test.setReplacementString(QuizMetadata.MetadataType.Professor, "(Professor)");
		test.setReplacementString(QuizMetadata.MetadataType.Date, "(Date)");
		test.setReplacementString(QuizMetadata.MetadataType.TestNum, "(Test)");
		    	

		String filename = "Output Template";
		if (isKey)
		{
			filename += "_KEY";
		}
		destination = Paths.get(filename + ".docx");
		if(destination == null || destination.toString().isBlank())
		{
			logger.error("Destination path cannot be null or blank.");
			throw new IOException("Null or blank destination");
		}
		
		XWPFDocument document;

		if (template != null && !template.toString().isBlank()) {
		    if (!Files.exists(template) || !template.toString().toLowerCase().endsWith(".docx")) {
		    	logger.error("invalid template file: {}", template);
		        throw new IOException("Invalid template file: " + template);
		    }
		    document = TemplateWriter.applyMetadata(template, test, metadata);
		} else {
		    document = new XWPFDocument();
		    TemplateCreator.createDocument(document, destination, metadata);
		}

		QuestionWriter questionWriter = new QuestionWriter(document, isKey);
		if(isKey)
		{
			insertRedKey(document);
			XWPFParagraph titleParagraph = document.createParagraph();
			titleParagraph.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun titleRun = titleParagraph.createRun();
			titleRun.setText("ANSWER KEY");
			titleRun.setColor("FF0000");
			titleRun.setBold(true);
			titleRun.setFontSize(20);	
		}
			
		//questionWriter.insertPageBreak(); //Deciding whether this is necessary or not.
			
		if(quiz != null) //Maybe move upwards in export
		{
			List<Question> generatedQuiz = quiz.getQuestions();
			for(int i = 0; i < quiz.getQuestions().size(); i++ ) {
				questionWriter.writeQuestion(generatedQuiz.get(i), i+1);
			}
		}
	        
		try (FileOutputStream out = new FileOutputStream(destination.toFile())) {
			document.write(out);
		} catch (IOException e) {
			logger.error("Failed to export document to: {}", e.getMessage());
			throw e; //This should throw up to the UI
		}
		logger.info("Export completed: {}", destination.toAbsolutePath());
	}

	/**
	 * Inserts a red "KEY" label into the first available spot in the header of a Word document.
	 * <p>
	 * It attempts to insert the label into the first empty table cell in any header. If no such
	 * space exists, it will reuse the first empty paragraph. If no empty spot is found, a new
	 * right-aligned paragraph is created at the end of the header.
	 * </p>
	 *
	 * @param document The XWPFDocument whose header will be modified.
	 */
	private void insertRedKey(XWPFDocument document)
	{
		List<XWPFHeader> headers = document.getHeaderList();
		if(headers == null || headers.isEmpty())
		{
			XWPFHeader header = document.createHeader(HeaderFooterType.DEFAULT);
			XWPFParagraph para = header.createParagraph();
			XWPFRun run = para.createRun();
			run.setText("KEY");
			run.setColor("FF0000");
			run.setBold(true);
			run.setFontSize(14);
			para.setAlignment(ParagraphAlignment.CENTER);
			return;
		}
		
		for (XWPFHeader header : headers)
		{
			boolean inserted = false;
			for(XWPFTable table : header.getTables())
			{
				for(XWPFTableRow row : table.getRows())
				{
					for(XWPFTableCell cell : row.getTableCells())
					{
						String text = cell.getText();
						if(text == null || text.isBlank()) {
							XWPFParagraph cellPara = cell.getParagraphs().isEmpty() 
									? cell.addParagraph()
									: cell.getParagraphs().get(0);
							XWPFRun run = cellPara.createRun();
							run.setText("KEY");
							run.setColor("FF0000");
							run.setBold(true);
							run.setFontSize(14);
							inserted = true;
							break;
						}
					}
					if(inserted) break;
				}
				if(inserted) break;
			}
			
			if(!inserted)
			{
				XWPFParagraph emptyPara = getFirstEmptyParagraph(header.getParagraphs());
				if(emptyPara != null)
				{
					XWPFRun run = emptyPara.createRun();
					run.setText("KEY");
					run.setColor("FF0000");
					run.setBold(true);
					run.setFontSize(14);
					emptyPara.setAlignment(ParagraphAlignment.CENTER);
					inserted = true;
				}
			}
			//All else fails case
			if(!inserted) {
				XWPFParagraph para = header.createParagraph();
				XWPFRun run = para.createRun();
				run.setText("KEY");
				run.setColor("FF0000");
				run.setBold(true);
				run.setFontSize(14);
				para.setAlignment(ParagraphAlignment.CENTER);
			}
		}
	}
	
	/**
	 * Finds the first visually empty paragraph in the given list.
	 * A paragraph is considered empty if its text is null, empty, or only whitespace.
	 *
	 * @param paragraphs A list of paragraphs to search.
	 * @return The first empty paragraph found, or null if none exist.
	 */
	private XWPFParagraph getFirstEmptyParagraph(List<XWPFParagraph> paragraphs)
	{
		for (XWPFParagraph para : paragraphs)
		{
			String text = para.getText();
			if (text == null || text.isBlank()) return para;
		}
		return null;
	}
	
	/**
	 * Builds a mock QuizMetadata instance for testing purposes.
	 */
	private QuizMetadata buildTestMetadata(GeneratedQuiz quiz) {
		QuizMetadata metadata = new QuizMetadata();

		metadata.setValue(QuizMetadata.MetadataType.ClassNum, "499");
		metadata.setValue(QuizMetadata.MetadataType.SectionNum, "01");
		metadata.setValue(QuizMetadata.MetadataType.TestNum, "2");
		metadata.setValue(QuizMetadata.MetadataType.Date, "April 17, 2025");
		metadata.setValue(QuizMetadata.MetadataType.Professor, "Mr. Example");
		metadata.setValue(QuizMetadata.MetadataType.Minutes, "75");
		metadata.setDynamicValues(quiz);

		return metadata;
	}
}
