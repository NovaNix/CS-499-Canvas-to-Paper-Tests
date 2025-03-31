package io.github.csgroup.quizmaker.word;

import java.io.IOException;

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
	
	private final boolean isKey;
	
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
	 * Writes a question to the docx file
	 * @param q the written response question to write
	 * @throws IOException if LabelWriter is null 
	 */
	public void writeWrittenResponse(WrittenResponseQuestion q) throws IOException
	{
		LabelWriter labelWriter = new LabelWriter(document);
		labelWriter.write(q.getLabel());

		if (isKey) 
		{
			labelWriter.write(q.getAnswer());
		}
	}
	
	{
		
		// TODO add the rest of the question types
	}
}
