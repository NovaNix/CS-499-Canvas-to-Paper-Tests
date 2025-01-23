package io.github.csgroup.quizmaker.data;

import java.util.List;

/**
 * A question to be used on a quiz.<br>
 * 
 * This is not currently fully implemented. Do not use yet
 * 
 * @author Michael Nix
 */
public class Question 
{
	// TODO finish implementation

	private final String id = "";
	private String title;
	
	
	private Label label;
	
	private float points;
	
	private Type type;
	
	private List<Answer> answers;
	private List<Integer> correctAnswers;
	
	public String getId()
	{
		return id;
	}
	
	public enum Type
	{
		TrueFalse("True or False")
		;
		
		String displayName;
		
		private Type(String displayName)
		{
			this.displayName = displayName;
		}
		
		public String getDisplayName()
		{
			return displayName;
		}
	}
	
}
