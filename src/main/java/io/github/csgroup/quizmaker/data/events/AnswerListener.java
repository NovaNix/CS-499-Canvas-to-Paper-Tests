package io.github.csgroup.quizmaker.data.events;

import io.github.csgroup.quizmaker.data.Answer;
import io.github.csgroup.quizmaker.data.events.answer.AnswerEvent;

/**
 * An object that runs code when {@link AnswerEvent AnswerEvents} are fired
 * 
 * @author Michael Nix
 */
public interface AnswerListener 
{
	/**
	 * Called when an AnswerEvent is fired
	 * @param source The source of the event
	 * @param e The fired event
	 */
	public void onAnswerEvent(Answer source, AnswerEvent event);
}
