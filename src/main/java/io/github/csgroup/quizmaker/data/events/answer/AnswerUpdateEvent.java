package io.github.csgroup.quizmaker.data.events.answer;

import io.github.csgroup.quizmaker.data.Answer;

public class AnswerUpdateEvent extends AnswerEvent
{

	public AnswerUpdateEvent(Answer source) 
	{
		super(source);
	}

}
