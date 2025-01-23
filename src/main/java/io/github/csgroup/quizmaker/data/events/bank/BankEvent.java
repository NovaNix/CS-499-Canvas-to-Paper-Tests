package io.github.csgroup.quizmaker.data.events.bank;

import io.github.csgroup.quizmaker.data.QuestionBank;

/**
 * An event related to {@link Bank Banks}
 * 
 * @author Michael Nix
 */
public abstract class BankEvent 
{

	private final QuestionBank bank;
	
	public BankEvent(QuestionBank bank)
	{
		this.bank = bank;
	}
	
	public QuestionBank getBank()
	{
		return bank;
	}
	
}
