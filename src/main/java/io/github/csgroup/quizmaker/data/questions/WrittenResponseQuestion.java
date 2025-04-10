package io.github.csgroup.quizmaker.data.questions;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.events.question.QuestionUpdateEvent;

/**
 * A {@link Question} where the user writes out the correct answer.<br>
 * <br>
 * This question type should be used for the following QTI question types:
 * <ul>
 *   <li>Numerical Answer</li>
 *   <li>Essay Question</li>
 * </ul>
 */
public class WrittenResponseQuestion extends Question
{

	private Label answer;
	
	/**
	 * How much space should be given for writing the response. 
	 */
	private ResponseLength length = ResponseLength.Paragraph;
	
	public WrittenResponseQuestion(String title)
	{
		super(title);
	}
	
	public WrittenResponseQuestion(String title, float points)
	{
		super(title, points);
	}
	
	public WrittenResponseQuestion(String id, String title, float points) 
	{
		super(id, title, points);
	}

	public void setAnswer(String answer)
	{
		this.answer = new Label(answer);
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public void setAnswer(Label answer)
	{
		this.answer = answer;
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	/**
	 * Sets how much space should be given to answer this question.
	 * @param length the amount of space to be given
	 */
	public void setResponseLength(ResponseLength length)
	{
		this.length = length;
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	/**
	 * @return how much space should be given to answer this question.
	 */
	public ResponseLength getResponseLength()
	{
		return length;
	}
	
	public Label getAnswer()
	{
		return answer;
	}
	
	@Override
	public String getAnswerString() 
	{
		return answer.asText();
	}
	
	public static enum ResponseLength
	{
		// TODO maybe replace essay with page and multipage
		Line, Paragraph, Essay;
		
		public String getDisplayName()
		{
			// TODO replace this with something more manual, should we add ResponseLengths that are more than one word
			return this.toString();
		}
	}

	@Override
	public Question clone()
	{
		var c = new WrittenResponseQuestion(getId(), getTitle(), getPoints());
		
		c.setLabel(getLabel().clone());
		c.setAbet(isAbet());
		
		c.setResponseLength(length);
		c.setAnswer(getLabel().clone());
		
		return c;
	}
	
}
