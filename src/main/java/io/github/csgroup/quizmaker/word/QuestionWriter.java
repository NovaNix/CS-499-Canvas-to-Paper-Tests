package io.github.csgroup.quizmaker.word;

import org.apache.poi.xwpf.usermodel.*;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion;

/**
 * Responsible for writing {@link Question Questions} to a docx file
 * 
 * @author 
 */
public class QuestionWriter
{
	private final XWPFDocument document;
	// You dont have to adhere too carefully to this skeleton code, this is just a rough outline and some ideas
	// Do what works, and keep things clean and reusable if possible
		
	public QuestionWriter(XWPFDocument document) 
	{
        this.document = document;// TODO take in the docx file to write to, along with any other persistent information you might need
	}
	
	/**
	 * Writes a question to the docx file
	 * @param q the question to write
	 * @param isKey whether or not an answer key is being written
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
