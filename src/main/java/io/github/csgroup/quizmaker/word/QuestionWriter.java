package io.github.csgroup.quizmaker.word;

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
	// You dont have to adhere too carefully to this skeleton code, this is just a rough outline and some ideas
	// Do what works, and keep things clean and reusable if possible
		
	public QuestionWriter()
	{
		// TODO take in the docx file to write to, along with any other persistent information you might need
	}
	
	/**
	 * Writes a question to the docx file
	 * @param q the question to write
	 * @param isKey whether or not an answer key is being written
	 */
	public void write(Question q, boolean isKey)
	{
		
	}
	
	{
		// Here are some example questions to test the code with
		// These should be removed or moved to a test file eventually
		
		var writtenResponse = new WrittenResponseQuestion("Written Test", 0);
		writtenResponse.setLabel(new Label("This is a written response"));
		writtenResponse.setAnswer("And this should be the answer!");
		
		// TODO add the rest of the question types
	}
}
