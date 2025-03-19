package io.github.csgroup.quizmaker.data.events.answer;

import io.github.csgroup.quizmaker.data.Answer;

public abstract class AnswerEvent 
{

	private final Answer source;
	
	public AnswerEvent(Answer source)
	{
		this.source = source;
	}
	
	public Answer getSource()
	{
		return source;
	}
	
}
