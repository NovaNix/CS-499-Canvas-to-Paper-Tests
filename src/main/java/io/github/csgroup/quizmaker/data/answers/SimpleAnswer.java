package io.github.csgroup.quizmaker.data.answers;

import io.github.csgroup.quizmaker.data.Answer;
import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.events.answer.AnswerUpdateEvent;

public class SimpleAnswer extends Answer
{

	private Label label;
	
	public SimpleAnswer(int id)
	{
		super(id);
		
		this.label = new Label();
	}
	
	public SimpleAnswer(int id, String text)
	{
		super(id);
		
		this.label = new Label(text);
	}
	
	public SimpleAnswer(int id, Label label)
	{
		super(id);

		this.label = label;
	}
	
	public void setLabel(Label label)
	{
		this.label = label;
		
		fireEvent(new AnswerUpdateEvent(this));
	}
	
	public void setLabel(String contents)
	{
		setLabel(new Label(contents));
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

	@Override
	public Answer clone() 
	{
		return new SimpleAnswer(getId(), label.clone());
	}
	
}
