package cs499.group7.data;

import java.util.List;

public class Question 
{

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
