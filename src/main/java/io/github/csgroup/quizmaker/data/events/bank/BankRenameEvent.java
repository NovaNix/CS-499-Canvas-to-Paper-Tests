package io.github.csgroup.quizmaker.data.events.bank;

import io.github.csgroup.quizmaker.data.QuestionBank;

/**
 * An event fired when a {@link QuestionBank} is renamed
 * 
 * @author Michael Nix
 */
public class BankRenameEvent extends BankEvent
{

	private final String oldName;
	private final String newName;
	
	public BankRenameEvent(QuestionBank bank, String oldName, String newName) 
	{
		super(bank);
		
		this.oldName = oldName;
		this.newName = newName;
	}
	
	/**
	 * @return The name of the QuestionBank before it was renamed
	 */
	public String getOldName()
	{
		return oldName;
	}
	
	/**
	 * @return The name of the QuestionBank after it was renamed
	 */
	public String getNewName()
	{
		return newName;
	}

}
