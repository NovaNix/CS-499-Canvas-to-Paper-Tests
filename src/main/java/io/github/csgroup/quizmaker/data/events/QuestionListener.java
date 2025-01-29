package io.github.csgroup.quizmaker.data.events;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.events.question.QuestionEvent;

public interface QuestionListener 
{

	/**
	 * Called when a QuestionEvent is fired
	 * @param source The source of the event
	 * @param e The fired event
	 */
	public void onQuestionEvent(Question source, QuestionEvent e);
	
}
