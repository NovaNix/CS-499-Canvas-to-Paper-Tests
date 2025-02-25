package io.github.csgroup.quizmaker.data;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.events.QuestionListener;
import io.github.csgroup.quizmaker.data.events.question.QuestionEvent;
import io.github.csgroup.quizmaker.data.events.question.QuestionUpdateEvent;
import io.github.csgroup.quizmaker.data.utils.DataUtils;

/**
 * A question to be used on a quiz.<br>
 * 
 * This is not currently fully implemented. Do not use yet
 * 
 * @author Michael Nix
 */
public abstract class Question 
{
	// TODO finish implementation

	private final String id;
	private String title;
	
	private Label label;
	
	private float points;
	
	public Question(String title, float points)
	{
		this.id = DataUtils.generateId();
		
		this.title = title;
		this.label = Label.text(title);
		
		this.points = points;
	}
	
	public Question(String id, String title, float points)
	{
		this.id = id;
		
		this.title = title;
		this.label = Label.text(title);
		
		this.points = points;
	}
	
	public abstract String getAnswerString();
	
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
	
	public enum Type
	{
		MultipleChoice("Multiple Choice"), // Multiple Choice
		TrueFalse("True or False"), // Multiple Choice?
		FillInTheBlank("Fill in the Blank"), // FillInTheBlank
		FillInMultipleBlanks("Fill in Multiple Blanks"), // FillInTheBlank
		MultipleAnswers("Multiple Answers"), // Multiple Choice
		MultipleDropdowns("Multiple Dropdowns"), 
		Matching("Matching"), // Matching
		NumericalAnswer("Numerical Answer"), // Written Response?
		FormulaQuestion("Formula Question"), // delete
		EssayQuestion("Essay Question"), // Written Response
		// File Upload Question
		// Text
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
	
	@Override
	public String toString()
	{
		return label.asText();
	}
	
}
