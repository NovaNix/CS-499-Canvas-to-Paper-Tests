package io.github.csgroup.quizmaker.data.events.bank;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.utils.ListUpdateType;

/**
 * An event fired when the contents of a {@link QuestionBank} change
 * 
 * @author Michael Nix
 */
public class BankUpdateEvent extends BankEvent
{

	private final ListUpdateType action;
	private final Question modified;
	private final int index;
	
	public BankUpdateEvent(QuestionBank source, ListUpdateType action, Question modified, int index) 
	{
		super(source);
		
		this.action = action;
		this.modified = modified;
		this.index = index;
	}
	
	/**
	 * @return What action was performed on the QuestionBank
	 */
	public ListUpdateType getAction()
	{
		return action;
	}
	
	/**
	 * @return What item was added or removed from the QuestionBank
	 */
	public Question getModified()
	{
		return modified;
	}
	
	/**
	 * @return The index of the modified item
	 */
	public int getIndex()
	{
		return index;
	}

}
