package io.github.csgroup.quizmaker.data.events.quiz;

import io.github.csgroup.quizmaker.data.Quiz;

/**
 * An event related to {@link Quiz Quizzes}
 * 
 * @author Michael Nix
 */
public abstract class QuizEvent 
{
	private Quiz source;
	
	public QuizEvent(Quiz source)
	{
		this.source = source;
	}
	
	public Quiz getSource()
	{
		return source;
	}
}
