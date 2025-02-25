package io.github.csgroup.quizmaker.data.answers;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.Answer;
import io.github.csgroup.quizmaker.data.Label;

public class BlankAnswer extends Answer
{
	//String tag;
	
	private Label correctAnswer;
	private List<Label> possibleAnswers = new ArrayList<Label>();
	
	public BlankAnswer(int id) 
	{
		super(id);
		
		this.correctAnswer = Label.blank();
	}
	
	public BlankAnswer(int id, Label answer)
	{
		super(id);
		
		this.correctAnswer = answer;
	}
	
	public void setCorrect(Label label)
	{
		this.correctAnswer = label;
	}
	
	public void addPossible(Label label)
	{
		possibleAnswers.add(label);
	}
	
	public void removePossible(Label label)
	{
		possibleAnswers.remove(label);
	}
	
	public Label getPossible(int index)
	{
		return possibleAnswers.get(index);
	}
	
	/**
	 * @return a list of all of the answers, including both the correct and incorrect answers
	 */
	public List<Label> getAllOptions()
	{
		List<Label> options = new ArrayList<Label>();
		
		options.add(correctAnswer);
		options.addAll(possibleAnswers);
		
		return options;
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
