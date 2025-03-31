package io.github.csgroup.quizmaker.data;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.events.AnswerListener;
import io.github.csgroup.quizmaker.data.events.answer.AnswerEvent;

/**
 * An answer to a {@link Question Question}
 * 
 * @author Michael Nix
 */
public abstract class Answer 
{
	private final int id;
	
	// TODO add default constructor with randomly generating id
	
	public Answer(int id)
	{
		this.id = id;
	}
	
	public abstract String asText();
	
	@Override
	public abstract Answer clone();
	
	public int getId()
	{
		return id;
	}
	
	@Override
	public String toString()
	{
		return asText();
	}
	
	// Event Processing
	
	private static List<AnswerListener> globalListeners = new ArrayList<AnswerListener>();
	private List<AnswerListener> localListeners = new ArrayList<AnswerListener>();

	public void addListener(AnswerListener listener)
	{
		localListeners.add(listener);
	}

	public void removeListener(AnswerListener listener)
	{
		localListeners.remove(listener);
	}

	public static void addGlobalListener(AnswerListener listener)
	{
		globalListeners.add(listener);
	}

	public static void removeGlobalListener(AnswerListener listener)
	{
		globalListeners.remove(listener);
	}

	protected void fireEvent(AnswerEvent event)
	{
		for (var listener : globalListeners)
		{
			listener.onAnswerEvent(this, event);
		}

		for (var listener : localListeners)
		{
			listener.onAnswerEvent(this, event);
		}
	}
}
