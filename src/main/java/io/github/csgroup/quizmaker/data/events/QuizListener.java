package io.github.csgroup.quizmaker.data.events;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.events.quiz.QuizEvent;

/**
 * An object that runs code when {@link QuizEvent QuizEvents} are fired
 * 
 * @author Michael Nix
 */
public interface QuizListener 
{
	/**
	 * Called when a QuizEvent is fired
	 * @param source The source of the event
	 * @param e The fired event
	 */
	public void onQuizEvent(Quiz source, QuizEvent e);
}
