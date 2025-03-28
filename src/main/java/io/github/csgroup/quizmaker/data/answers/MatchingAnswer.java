package io.github.csgroup.quizmaker.data.answers;

import io.github.csgroup.quizmaker.data.Answer;
import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.events.answer.AnswerUpdateEvent;

public class MatchingAnswer extends Answer
{
	private static final String MATCHING_ARROW = "→";
	
	private Label left;
	private Label right;
	
	public MatchingAnswer(int id)
	{
		super(id);
		
		this.left =  new Label();
		this.right = new Label();
	}
	
	public MatchingAnswer(int id, Label left, Label right)
	{
		super(id);
		
		this.left = left;
		this.right = right;
	}
	
	public MatchingAnswer(int id, String left, String right)
	{
		this(id, new Label(left), new Label(right));
	}
	
	public void setLeft(Label left)
	{
		this.left = left;
		
		fireEvent(new AnswerUpdateEvent(this));
	}
	
	public void setRight(Label right)
	{
		this.right = right;
		
		fireEvent(new AnswerUpdateEvent(this));
	}
	
	public void set(Label left, Label right)
	{
		this.left = left;
		this.right = right;
		
		fireEvent(new AnswerUpdateEvent(this));
	}
	
	public Label getLeft()
	{
		return left;
	}
	
	public Label getRight()
	{
		return right;
	}
	
	@Override
	public String asText()
	{
		return left.asText() + MATCHING_ARROW + right.asText();
	}

	@Override
	public Answer clone() 
	{
		return new MatchingAnswer(getId(), left.clone(), right.clone());
	}
	
}
