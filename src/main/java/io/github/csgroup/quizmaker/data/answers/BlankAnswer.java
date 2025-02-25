package io.github.csgroup.quizmaker.data.answers;

import java.util.List;

import io.github.csgroup.quizmaker.data.Answer;
import io.github.csgroup.quizmaker.data.Label;

public class BlankAnswer extends Answer
{
	//String tag;
	
	private Label correctAnswer;
	private List<Label> possibleAnswers;
	
	public BlankAnswer(int id) 
	{
		super(id);
	}
	
	public Label getCorrect()
	{
		return correctAnswer;
	}

	@Override
	public String asText() 
	{
		return correctAnswer.asText();
	}
}
