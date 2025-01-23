package io.github.csgroup.quizmaker.data.events;

import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.events.bank.BankEvent;

/**
 * An object that runs code when {@link BankEvent BankEvents} are fired
 * 
 * @author Michael Nix
 */
public interface BankListener 
{

	/**
	 * Called when a BankEvent is fired
	 * @param source The source of the event
	 * @param e The fired event
	 */
	public void onBankEvent(QuestionBank source, BankEvent e);
	
}
