package io.github.csgroup.quizmaker.data.events.question;

import io.github.csgroup.quizmaker.data.Question;

/**
 * An event fired when the contents of a question change. 
 * 
 * @author Michael Nix
 */
public class QuestionUpdateEvent extends QuestionEvent
{

	// TODO consider adding information about what was changed
	
	public QuestionUpdateEvent(Question question) 
	{
		super(question);
	}

}
