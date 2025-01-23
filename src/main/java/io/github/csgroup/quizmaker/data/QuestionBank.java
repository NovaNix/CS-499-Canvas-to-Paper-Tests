package io.github.csgroup.quizmaker.data;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.events.BankListener;
import io.github.csgroup.quizmaker.data.events.bank.BankEvent;
import io.github.csgroup.quizmaker.data.events.bank.BankRenameEvent;
import io.github.csgroup.quizmaker.data.events.bank.BankUpdateEvent;
import io.github.csgroup.quizmaker.data.utils.DataUtils;
import io.github.csgroup.quizmaker.utils.ListUpdateType;

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
		boolean added = questions.add(q);
		
		if (added)
		{
			// If this question was added to the list, we need to send an addition event
			fireEvent(new BankUpdateEvent(this, ListUpdateType.Deletion, q));
		}
		
		return added;
	}
	
	public boolean remove(Question q)
	{
		boolean included = questions.remove(q); 
		
		if (included)
		{
			// If this question was included in the list, we need to send a deletion event
			fireEvent(new BankUpdateEvent(this, ListUpdateType.Deletion, q));
		}
		
		return included;
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
		String oldName = this.title;
		
		this.title = title;
		
		fireEvent(new BankRenameEvent(this, oldName, title));
	}
	
	public String getId()
	{
		return id;
	}
	
	// Event Processing
	
	private static List<BankListener> globalListeners = new ArrayList<BankListener>();
	private List<BankListener> localListeners = new ArrayList<BankListener>();
	
	public void addListener(BankListener listener)
	{
		localListeners.add(listener);
	}
	
	public void removeListener(BankListener listener)
	{
		localListeners.remove(listener);
	}
	
	public static void addGlobalListener(BankListener listener)
	{
		globalListeners.add(listener);
	}
	
	public static void removeGlobalListener(BankListener listener)
	{
		globalListeners.remove(listener);
	}
	
	protected void fireEvent(BankEvent event)
	{
		for (var listener : globalListeners)
		{
			listener.onBankEvent(this, event);
		}
		
		for (var listener : localListeners)
		{
			listener.onBankEvent(this, event);
		}
	}
}
