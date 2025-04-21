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
	public void exportTest(Quiz quiz, GeneratedQuiz generatedQuiz, Path template, Path destination, TemplateReplacements replacements, Path referenceAppendPath, boolean isKey) throws IOException
	{
		
		String filename = destination.toString();
		if(quiz == null)
		{
			logger.error("Quiz has not been generated!");
			throw new IOException("Quiz has not been generated!");
		}
		if (isKey)
		{
			int dotIndex = filename.lastIndexOf(".");
			if (dotIndex != -1) 
			{
				filename = filename.substring(0, dotIndex) + "_KEY" + filename.substring(dotIndex);
			}
			else
			{
				throw new IOException("How did we get here?");
			}
		}
		destination = Paths.get(filename);
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
		    document = TemplateWriter.applyMetadata(template, replacements, generatedQuiz.getQuizMetadata(), quiz);
		} else {
		    document = new XWPFDocument();
		    TemplateCreator.createDocument(document, destination, generatedQuiz.getQuizMetadata(), quiz);
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
			List<Question> quizQuestions = generatedQuiz.getQuestions();
			for(int i = 0; i < generatedQuiz.getQuestions().size(); i++ ) {
				questionWriter.writeQuestion(quizQuestions.get(i), i+1);
			}
		}
		
		if (referenceAppendPath != null && Files.exists(referenceAppendPath)) {
		    try (FileInputStream refIn = new FileInputStream(referenceAppendPath.toFile());
		         XWPFDocument referenceDoc = new XWPFDocument(refIn)) {

		        logger.info("Appending reference material from: {}", referenceAppendPath.getFileName());

		        XWPFParagraph breakPara = document.createParagraph();
		        breakPara.setPageBreak(true);
		        // Append each paragraph
		        for (IBodyElement element : referenceDoc.getBodyElements()) {
		            if (element instanceof XWPFParagraph refPara) {
		                XWPFParagraph newPara = document.createParagraph();
		                newPara.getCTP().set(refPara.getCTP().copy());

		            } else if (element instanceof XWPFTable refTable) {
		                XWPFTable newTable = document.createTable();
		                newTable.getCTTbl().set(refTable.getCTTbl().copy());
		            }
		        }

		    } catch (IOException e) {
		        logger.error("Failed to append reference document: {}", e.getMessage());
		        throw new IOException("Failed to append reference material.", e);
		    }
		}

	        
		try (FileOutputStream out = new FileOutputStream(destination.toFile())) {
			document.write(out);
		} catch (IOException e) {
			logger.error("Failed to export document to: {}", e.getMessage());
			throw new IOException("Failed to export document.");
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
}
