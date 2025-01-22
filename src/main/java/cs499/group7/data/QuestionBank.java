package cs499.group7.data;

import java.util.ArrayList;
import java.util.List;
import cs499.group7.data.utils.DataUtils;

/**
 * A collection of {@link Question Questions} that can be used when generating quizzes 
 * 
 * @author Michael Nix
 */
public class QuestionBank 
{

	private final String id;
	private String title;
	
	
	private List<Question> questions = new ArrayList<Question>();

	
	public QuestionBank(String title)
	{
		this.id = DataUtils.generateId();
		this.title = title;
	}
	
	public QuestionBank(String id, String title)
	{
		this.id = id;
		this.title = title;
	}
	
	// TODO add helper methods for generating quizzes
	
	public boolean add(Question q)
	{
		return questions.add(q);
	}
	
	public boolean remove(Question q)
	{
		return questions.remove(q);
	}
	
	public Question get(int index)
	{
		return questions.get(index);
	}
	
	public Question getFromId(String id)
	{
		for (var question : questions)
		{
			if (question.getId().equals(id))
				return question;
		}
		
		return null;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getId()
	{
		return id;
	}
}
