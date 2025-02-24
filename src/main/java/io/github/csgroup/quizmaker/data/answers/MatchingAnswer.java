package io.github.csgroup.quizmaker.data.answers;

import io.github.csgroup.quizmaker.data.Label;

public class MatchingAnswer 
{
	private static final String MATCHING_ARROW = "→";
	
	private final int id;
	
	private Label left;
	private Label right;
	
	public MatchingAnswer(int id, Label left, Label right)
	{
		this.id = id;
		
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
	
	public String asText()
	{
		return left.asText() + MATCHING_ARROW + right.asText();
	}
	
	public int getId()
	{
		return id;
	}
	
}
