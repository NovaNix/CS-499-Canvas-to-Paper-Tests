package io.github.csgroup.quizmaker.data.answers;

import io.github.csgroup.quizmaker.data.Answer;
import io.github.csgroup.quizmaker.data.Label;

public class MatchingAnswer extends Answer
{
	private static final String MATCHING_ARROW = "→";
	
	private Label left;
	private Label right;
	
	public MatchingAnswer(int id, Label left, Label right)
	{
		super(id);
		
		this.left = left;
		this.right = right;
	}
	
	public void setLeft(Label left)
	{
		this.left = left;
	}
	
	public void setRight(Label right)
	{
		this.right = right;
	}
	
	public void set(Label left, Label right)
	{
		this.left = left;
		this.right = right;
		
		
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
	
}
