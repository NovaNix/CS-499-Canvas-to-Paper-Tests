package io.github.csgroup.quizmaker.word;

import java.io.IOException;

import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.word.LabelWriter;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.questions.*;

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
		
		// TODO: Accommodate for different size written response questions
	}
	
	public void writeFillBlank(FillInTheBlankQuestion q) throws IOException
	{
		LabelWriter labelWriter = new LabelWriter(document);
		labelWriter.write(q.getLabel()); //Need to translate tags to blanks
		
		if(isKey)
		{
			int index = 0;
			for(String tag : q.getTags())
			{
				BlankAnswer answer = q.getAnswer(tag);
				if (answer != null)
				{
					String addIndex = "[" + index + "] ";
					if(answer.getAnswer().getType() == Label.Type.html)
					{
						labelWriter.write(new Label(addIndex + answer.asText(), Label.Type.html));
					}
					else
					{
						labelWriter.write(new Label(addIndex + answer.asText()));
					}
				}
				index++;
			}
		}
	}
	
	public void writeMultipleChoice(MultipleChoiceQuestion q) throws IOException
	{
		LabelWriter labelWriter = new LabelWriter(document);
		labelWriter.write(q.getLabel());
		
		for (SimpleAnswer answer : q.getAnswers())
		{
			String prefix = "- ";
			if(isKey && q.isCorrect(answer))
			{
				prefix += "[Correct] ";
			}
			
			if(answer.getLabel().getType() == Label.Type.html)
			{
				var ansLabel = new Label(prefix + answer.asText(), Label.Type.html);
				labelWriter.write(ansLabel);
			}
			else
			{
				var ansLabel = new Label(prefix + answer.asText());
				labelWriter.write(ansLabel);
			}
		}
		
		
	}
	
	public void writeMatching(MatchingQuestion q) throws IOException
	{
		LabelWriter labelWriter = new LabelWriter(document);
		// Before writing the non-key matching, I need to know how the answers are randomized, or if I need to do that myself
		if(isKey)
		{
			for(MatchingAnswer answer : q.getAnswers())
			{
				String matchText = answer.getLeft().asText() + " → " + answer.getRight().asText();
				if(answer.getLeft().getType() == Label.Type.html || answer.getRight().getType() == Label.Type.html)
				{
					labelWriter.write(new Label(matchText, Label.Type.html));
				}
				else
				{
					labelWriter.write(new Label(matchText));
				}
			}
		}
		
	}
	
	{
		//TODO: Make each question type work with its respective question
	}
}
