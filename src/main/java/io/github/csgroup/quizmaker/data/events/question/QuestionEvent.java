package io.github.csgroup.quizmaker.data.events.question;

import io.github.csgroup.quizmaker.data.Question;

/**
 * An event related to {@link Question Question}
 * 
 * @author Michael Nix
 */
public abstract class QuestionEvent 
{

	private final Question question;
	
	public QuestionEvent(Question question)
	{
		this.question = question;
	}
	
	public Question getQuestion()
	{
		return question;
	}
	
}
