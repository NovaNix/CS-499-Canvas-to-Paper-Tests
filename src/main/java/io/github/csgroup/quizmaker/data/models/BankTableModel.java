package io.github.csgroup.quizmaker.data.models;

import javax.swing.table.AbstractTableModel;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.events.BankListener;
import io.github.csgroup.quizmaker.data.events.QuestionListener;
import io.github.csgroup.quizmaker.data.events.bank.BankEvent;
import io.github.csgroup.quizmaker.data.events.bank.BankUpdateEvent;
import io.github.csgroup.quizmaker.data.events.question.QuestionEvent;

public class BankTableModel extends AbstractTableModel implements BankListener, QuestionListener
{

	private static final long serialVersionUID = 3982949506133938225L;

	public static final int LABEL_COLUMN = 0;
	public static final int ANSWER_COLUMN = 1;
	
	private QuestionBank bank;
	
	public BankTableModel(QuestionBank bank)
	{
		this.bank = bank;
	}
	
	@Override
	public int getRowCount() 
	{
		return bank.getQuestionCount();
	}

	@Override
	public int getColumnCount() 
	{
		// TODO make dynamic
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) 
	{
		Question q = bank.get(rowIndex);
		
		switch (columnIndex)
		{
			case LABEL_COLUMN:
				return q.toString();
				
			case ANSWER_COLUMN:
				return q.getAnswerString();
		}
		
		return null;
	}

	@Override
	public void onBankEvent(QuestionBank source, BankEvent e) 
	{
		if (e instanceof BankUpdateEvent)
		{
			BankUpdateEvent update = (BankUpdateEvent) e;
			
			switch (update.getAction())
			{
			case Addition:
				this.fireTableRowsInserted(update.getIndex(), update.getIndex());
				update.getModified().addListener(this);
				break;
			case Deletion:
				this.fireTableRowsDeleted(update.getIndex(), update.getIndex());
				update.getModified().removeListener(this);
				break;
			default:
				break;
			}
		}
		
	}

	@Override
	public void onQuestionEvent(Question source, QuestionEvent e) 
	{
		int index = bank.indexOf(source);
		
		if (index == -1)
			return; // TODO this could throw an error later, or autoremove it from the question
		
		this.fireTableRowsUpdated(index, index);
	}
	
	

}
