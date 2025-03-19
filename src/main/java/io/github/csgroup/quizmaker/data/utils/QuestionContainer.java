package io.github.csgroup.quizmaker.data.utils;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.events.ListListenable;

/**
 * Represents an object that contains a list of {@link Question Questions}
 * 
 * @author Michael Nix
 */
public interface QuestionContainer extends ListListenable<Question>
{

	public Question getQuestion(int index);
	
	public int getQuestionIndex(Question q);
	
	public int getQuestionCount();
}
