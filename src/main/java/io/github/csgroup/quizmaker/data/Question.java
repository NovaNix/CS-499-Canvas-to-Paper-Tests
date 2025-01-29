package io.github.csgroup.quizmaker.data;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.events.QuestionListener;
import io.github.csgroup.quizmaker.data.events.question.QuestionEvent;
import io.github.csgroup.quizmaker.data.events.question.QuestionUpdateEvent;

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

	private final String id;
	private String title;
	
	private Label label;
	
	private float points;
	
	private Type type;
	
	private List<Answer> answers = new ArrayList<Answer>();
	private List<Integer> correctAnswers = new ArrayList<Integer>();
	
	public Question(String id, String title, Type type, float points)
	{
		this.id = id;
		
		this.title = title;
		this.label = Label.text(title);
		
		this.type = type;
		this.points = points;
	}
	
	public void addAnswer(Answer answer, boolean correct)
	{
		answers.add(answer);
		
		if (correct)
		{
			correctAnswers.add(answer.getId());
		}
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public void removeAnswer(Answer answer)
	{
		boolean removed = answers.remove(answer);
		
		if (removed)
		{
			correctAnswers.remove(answer.getId());
		}
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public void clearAnswers()
	{
		this.answers.clear();
		this.correctAnswers.clear();
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public List<Answer> getAnswers()
	{
		return new ArrayList<Answer>(answers);
	}
	
	public List<Answer> getCorrectAnswers()
	{
		ArrayList<Answer> correct = new ArrayList<Answer>();
		
		// TODO this could be optimized further. If we experience speed issues, address this
		
		for (var answer : answers)
		{
			if (correctAnswers.contains(answer.getId()))
			{
				correct.add(answer);
			}
		}
		
		return correct;
	}
	
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setLabel(Label label)
	{
		this.label = label;
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public Label getLabel()
	{
		return label;
	}
	
	public void setPoints(float points)
	{
		this.points = points;
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public float getPoints()
	{
		return points;
	}
	
	public void setType(Type type)
	{
		this.type = type;
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public Type getType()
	{
		return type;
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
	
	// Event Processing
	
	private static List<QuestionListener> globalListeners = new ArrayList<QuestionListener>();
	private List<QuestionListener> localListeners = new ArrayList<QuestionListener>();

	public void addListener(QuestionListener listener)
	{
		localListeners.add(listener);
	}

	public void removeListener(QuestionListener listener)
	{
		localListeners.remove(listener);
	}

	public static void addGlobalListener(QuestionListener listener)
	{
		globalListeners.add(listener);
	}

	public static void removeGlobalListener(QuestionListener listener)
	{
		globalListeners.remove(listener);
	}

	protected void fireEvent(QuestionEvent event)
	{
		for (var listener : globalListeners)
		{
			listener.onQuestionEvent(this, event);
		}

		for (var listener : localListeners)
		{
			listener.onQuestionEvent(this, event);
		}
	}
	
}
