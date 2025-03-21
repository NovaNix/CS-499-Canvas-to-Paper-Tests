package io.github.csgroup.quizmaker.word;

import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.word.LabelWriter;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion;

/**
 * Responsible for writing {@link Question Questions} to a .docx file <br>
 * Currently only supports basic question writing of a written response question
 * 
 * @author Samuel Garcia 
 */
public class QuestionWriter
{
	public static final Logger logger = LoggerFactory.getLogger(QuestionWriter.class);
	
	private final XWPFDocument document;
	
	/**
	 * Constructs a QuestionWriter for the given document.
	 * @param document The XWPFDocument to which questions will be written.
	 */
	public QuestionWriter(XWPFDocument document) 
	{
		this.document = document;
	}
	
	/**
	 * Writes a question to the docx file
	 * @param q the question to write
	 * @param isKey whether or not an answer key is being written (will be changed to constructor later)
	 */
	public void write(Question q, boolean isKey)
	{
		XWPFParagraph questionParagraph = document.createParagraph();
		XWPFRun questionRun = questionParagraph.createRun();
		//questionRun.setBold(true); //Used this to signify questions, will adhere to a template later
		questionRun.setText(q.getLabel().asText());

		if (isKey && q instanceof WrittenResponseQuestion wrq) 
		{
			XWPFParagraph answerParagraph = document.createParagraph();
			XWPFRun answerRun = answerParagraph.createRun();
			answerRun.setItalic(true); //Currently just using this to signify answers, will adhere to a template later
			answerRun.setText(wrq.getAnswer().asText());
		}
	}
	
	{
		
		// TODO add the rest of the question types
	}
}
