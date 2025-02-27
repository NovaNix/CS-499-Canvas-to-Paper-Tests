package io.github.csgroup.quizmaker.data.answers;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.Answer;
import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.events.answer.AnswerUpdateEvent;

/**
 * An answer for a {@link FillInTheBlankQuestion}.<br>
 * <br>
 * 
 * This answer type stores both a single correct answer, along with several incorrect "distractor" answers.
 * 
 * @author Michael Nix
 */
public class BlankAnswer extends Answer
{	
	private Label correctAnswer;
	
	/** A list of incorrect answers to hide the correct answer in */
	private List<Label> distractors = new ArrayList<Label>();
	
	/**
	 * Creates a new BlankAnswer with an empty correct answer
	 * @param id
	 */
	public BlankAnswer(int id) 
	{
		super(id);
		
		this.correctAnswer = Label.blank();
	}
	
	/**
	 * Creates a new BlankAnswer with a specified id and answer
	 * @param id
	 * @param answer
	 */
	public BlankAnswer(int id, Label answer)
	{
		super(id);
		
		this.correctAnswer = answer;
	}
	
	/**
	 * Sets the correct answer's label
	 * @param label
	 */
	public void setAnswer(Label label)
	{
		this.correctAnswer = label;
		
		fireEvent(new AnswerUpdateEvent(this));
	}
	
	/**
	 * Adds a new incorrect distractor answer
	 * @param label
	 */
	public void addDistractor(Label label)
	{
		distractors.add(label);
		
		fireEvent(new AnswerUpdateEvent(this));
	}
	
	/**
	 * Removes a distractor from this answer
	 * @param label The distractor to remove
	 * @return whether or not the distractor was included in this answer
	 */
	public boolean removeDistractor(Label label)
	{
		boolean included = distractors.remove(label);
		
		if (included)
		{
			fireEvent(new AnswerUpdateEvent(this));
		}
		
		return included;
	}
	
	public Label getDistractor(int index)
	{
		return distractors.get(index);
	}
	
	/**
	 * @return a list of all of the answers, including both the correct and incorrect answers
	 */
	public List<Label> getAllOptions()
	{
		List<Label> options = new ArrayList<Label>();
		
		options.add(correctAnswer);
		options.addAll(distractors);
		
		return options;
	}
	
	/**
	 * @return a list of all of the distractors used in this answer
	 */
	public List<Label> getDistractors()
	{
		return new ArrayList<Label>(distractors);
	}
	
	public Label getAnswer()
	{
		return correctAnswer;
	}

	@Override
	public String asText() 
	{
		return correctAnswer.asText();
	}
}
