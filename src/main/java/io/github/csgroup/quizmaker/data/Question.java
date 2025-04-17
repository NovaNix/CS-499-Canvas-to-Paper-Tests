package io.github.csgroup.quizmaker.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.csgroup.quizmaker.data.events.QuestionListener;
import io.github.csgroup.quizmaker.data.events.question.QuestionEvent;
import io.github.csgroup.quizmaker.data.events.question.QuestionUpdateEvent;
import io.github.csgroup.quizmaker.data.utils.DataUtils;
import io.github.csgroup.quizmaker.utils.stores.writable.DefaultWritableStore;
import io.github.csgroup.quizmaker.utils.stores.writable.WritableStore;

/**
 * A question to be used on a quiz.
 * 
 * @author Michael Nix
 */
public abstract class Question 
{
	private final String id;
	private WritableStore<String> title;
	
	private Label label;
	
	private WritableStore<Float> points;
	
	private WritableStore<Boolean> abet;
	
	public Question(String title)
	{
		this(title, 0f);
	}
	
	public Question(String title, float points)
	{
		this(DataUtils.generateId(), title, points);
	}
	
	public Question(String id, String title, float points)
	{
		this.id = id;
		
		this.title = new DefaultWritableStore<String>(title);
		this.label = new Label(title);
		
		this.points = new DefaultWritableStore<Float>(points);
		
		this.abet = new DefaultWritableStore<Boolean>(false);
	}
	
	public abstract String getAnswerString();
	
	@Override
	public abstract Question clone();
	
	public String getTitle()
	{
		return title.get();
	}
	
	public void setTitle(String title)
	{
		this.title.set(title);
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	// TODO consider shortening these names
	
	public WritableStore<String> getTitleStore()
	{
		return title;
	}
	
	public WritableStore<Float> getPointStore()
	{
		return points;
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
		this.points.set(points);
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public float getPoints()
	{
		return points.get();
	}
	
	public void setAbet(boolean abet)
	{
		this.abet.set(abet);
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public boolean isAbet()
	{
		return abet.get();
	}
	
	public WritableStore<Boolean> getAbetStore()
	{
		return abet;
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

	@Override
	public int hashCode() 
	{
		return Objects.hash(id); // TODO make this an actual hash function
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		return other.id.equals(this.id); // TODO make this an actual equality function
	}
	
}
