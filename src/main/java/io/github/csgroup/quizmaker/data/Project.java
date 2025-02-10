package io.github.csgroup.quizmaker.data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;

import io.github.csgroup.quizmaker.data.events.ProjectListener;
import io.github.csgroup.quizmaker.data.events.project.ProjectBankUpdateEvent;
import io.github.csgroup.quizmaker.data.events.project.ProjectEvent;
import io.github.csgroup.quizmaker.data.events.project.ProjectQuizUpdateEvent;
import io.github.csgroup.quizmaker.utils.ListUpdateType;
import io.github.csgroup.quizmaker.data.models.ProjectBankListModel;

/**
 * A container for a set of {@link QuestionBank QuestionBanks} and {@link Quiz Quizzes}. 
 * 
 * @author Michael Nix
 */
public class Project
{	
	private final List<QuestionBank> banks = new ArrayList<QuestionBank>();
	private final List<Quiz> quizzes = new ArrayList<Quiz>();

	public Project()
	{
		
	}
	
	public boolean addBank(QuestionBank bank)
	{
		boolean added = banks.add(bank);
		
		if (added)
		{
			int index = quizzes.size() - 1;
			
			// If this QuestionBank was added to the list, we need to send an addition event
			fireEvent(new ProjectBankUpdateEvent(this, ListUpdateType.Addition, bank, index));
		}
		
		return added;
	}
	
	public boolean removeBank(QuestionBank bank)
	{
		int index = banks.indexOf(bank);
		boolean included = banks.remove(bank); 
		
		if (included)
		{
			// If this QuestionBank was included in the list, we need to send a deletion event
			fireEvent(new ProjectBankUpdateEvent(this, ListUpdateType.Deletion, bank, index));
		}
		
		return included;
	}
	
	public int getBankCount()
	{
		return banks.size();
	}
	
	public QuestionBank getBank(int index)
	{
		return banks.get(index);
	}
	
	public int getBankIndex(QuestionBank bank)
	{
		return banks.indexOf(bank);
	}
	
	/**
	 * @return A shallow copy of the list of QuestionBanks included in the Project
	 */
	public List<QuestionBank> getQuestionBanks()
	{
		return new ArrayList<QuestionBank>(banks);
	}
	
	public boolean addQuiz(Quiz quiz)
	{
		boolean added = quizzes.add(quiz);
		
		if (added)
		{
			int index = quizzes.size() - 1;
			
			// If this Quiz was added to the list, we need to send an addition event
			fireEvent(new ProjectQuizUpdateEvent(this, ListUpdateType.Addition, quiz, index));
		}
		
		return added;
	}
	
	public boolean removeQuiz(Quiz quiz)
	{
		int index = quizzes.indexOf(quiz);
		boolean included = quizzes.remove(quiz); 
		
		if (included)
		{	
			// If this Quiz was included in the list, we need to send a deletion event
			fireEvent(new ProjectQuizUpdateEvent(this, ListUpdateType.Deletion, quiz, index));
		}
		
		return included;
	}
	
	/**
	 * @return A shallow copy of the list of Quizzes included in the Project
	 */
	public List<Quiz> getQuizzes()
	{
		return new ArrayList<Quiz>(quizzes);
	}
	
	public int getQuizCount()
	{
		return quizzes.size();
	}
	
	public Quiz getQuiz(int index)
	{
		return quizzes.get(index);
	}
	
	public int getQuizIndex(Quiz quiz)
	{
		return quizzes.indexOf(quiz);
	}
	
	// Event Processing
	
	private static List<ProjectListener> globalListeners = new ArrayList<ProjectListener>();
	private List<ProjectListener> localListeners = new ArrayList<ProjectListener>();
		
	public void addListener(ProjectListener listener)
	{
		localListeners.add(listener);
	}
		
	public void removeListener(ProjectListener listener)
	{
		localListeners.remove(listener);
	}
		
	public static void addGlobalListener(ProjectListener listener)
	{
		globalListeners.add(listener);
	}
		
	public static void removeGlobalListener(ProjectListener listener)
	{
		globalListeners.remove(listener);
	}

	protected void fireEvent(ProjectEvent event)
	{
		for (var listener : globalListeners)
		{
			listener.onProjectEvent(this, event);
		}

		for (var listener : localListeners)
		{
			listener.onProjectEvent(this, event);
		}
	}
	
	// List Model Functions
	
	private ListModel<QuestionBank> bankModel = new ProjectBankListModel(this);
	
	public ListModel<QuestionBank> getBankModel()
	{
		return bankModel;
	}
	
	public ListModel<Quiz> getQuizModel()
	{
		return null;
	}
	
}
