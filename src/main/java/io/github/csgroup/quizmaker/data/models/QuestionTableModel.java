package io.github.csgroup.quizmaker.data.models;

import javax.swing.table.AbstractTableModel;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.events.QuestionListener;
import io.github.csgroup.quizmaker.data.events.question.QuestionEvent;
import io.github.csgroup.quizmaker.data.utils.QuestionContainer;
import io.github.csgroup.quizmaker.events.ListUpdateEvent;
import io.github.csgroup.quizmaker.events.ListUpdateListener;

/**
 * A <a href="https://docs.oracle.com/javase/tutorial/uiswing/components/model.html">Swing model</a> for connecting a list of {@link Question Questions} to a {@link TableModel}
 * 
 * @author Michael Nix
 */
public class QuestionTableModel extends AbstractTableModel implements ListUpdateListener<Question>, QuestionListener
{

	private static final long serialVersionUID = 3982949506133938225L;

	public static final int LABEL_COLUMN = 0;
	public static final int ANSWER_COLUMN = 1;
	
	/** The source of the questions */
	private QuestionContainer questions;
	
	public QuestionTableModel(QuestionContainer questions)
	{
		this.questions = questions;
		
		questions.addListListener(this);
	}
	
	@Override
	public int getRowCount() 
	{
		return questions.getQuestionCount();
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
		Question q = questions.getQuestion(rowIndex);
		
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
	public void onListUpdate(ListUpdateEvent<Question> update)
	{
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

	@Override
	public void onQuestionEvent(Question source, QuestionEvent e) 
	{
		int index = questions.getQuestionIndex(source);
		
		if (index == -1)
			return; // TODO this could throw an error later, or autoremove it from the question
		
		this.fireTableRowsUpdated(index, index);
	}
	
	

}
