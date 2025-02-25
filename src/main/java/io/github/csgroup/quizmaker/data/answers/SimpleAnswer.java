package io.github.csgroup.quizmaker.data.answers;

import io.github.csgroup.quizmaker.data.Answer;
import io.github.csgroup.quizmaker.data.Label;

public class SimpleAnswer extends Answer
{

	private Label label;
	
	public SimpleAnswer(int id, String text)
	{
		super(id);
		
		this.label = Label.text(text);
	}
	
	public SimpleAnswer(int id, Label label)
	{
		super(id);

		this.label = label;
	}
	
	public void setLabel(Label label)
	{
		this.label = label;
	}
	
	public void setLabel(String contents)
	{
		this.label = Label.text(contents);
	}
	
	public Label getLabel()
	{
		return label;
	}

	@Override
	public String asText() 
	{
		return label.asText();
	}
	
}
