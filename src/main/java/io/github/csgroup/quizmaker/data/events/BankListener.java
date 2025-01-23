package io.github.csgroup.quizmaker.data.events;

import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.events.bank.BankEvent;

public interface BankListener 
{

	public void onBankEvent(QuestionBank source, BankEvent e);
	
}
