package io.github.csgroup.quizmaker.data.events;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.events.question.QuestionEvent;

/**
 * An object that runs code when {@link QuestionEvent QuestionEvents} are fired
 * 
 * @author Michael Nix
 */
public interface QuestionListener 
{

	/**
	 * Called when a QuestionEvent is fired
	 * @param source The source of the event
	 * @param e The fired event
	 */
	public void onQuestionEvent(Question source, QuestionEvent e);
	
}
