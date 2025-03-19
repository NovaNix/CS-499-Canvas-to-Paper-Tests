package io.github.csgroup.quizmaker.data.events.quiz;

import io.github.csgroup.quizmaker.data.Quiz;

/**
 * An event fired when a {@link Quiz} is renamed
 * 
 * @author Michael Nix
 */
public class QuizRenameEvent extends QuizEvent
{

	private final String oldName;
	private final String newName;
	
	public QuizRenameEvent(Quiz quiz, String oldName, String newName) 
	{
		super(quiz);
		
		this.oldName = oldName;
		this.newName = newName;
	}
	
	/**
	 * @return The name of the Quiz before it was renamed
	 */
	public String getOldName()
	{
		return oldName;
	}
	
	/**
	 * @return The name of the Quiz after it was renamed
	 */
	public String getNewName()
	{
		return newName;
	}

}
