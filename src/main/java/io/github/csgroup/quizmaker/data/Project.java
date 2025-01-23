package io.github.csgroup.quizmaker.data;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.events.ProjectListener;
import io.github.csgroup.quizmaker.data.events.project.ProjectBankUpdateEvent;
import io.github.csgroup.quizmaker.data.events.project.ProjectEvent;
import io.github.csgroup.quizmaker.utils.ListUpdateType;

/**
 * A container for a set of {@link QuestionBank QuestionBanks} and {@link Quiz Quizzes}. 
 * 
 * Quizzes are not currently supported, this will be addressed soon.
 * 
 * @author Michael Nix
 */
public class Project 
{
	// TODO add quiz support
	
	private List<QuestionBank> banks = new ArrayList<QuestionBank>();
	//private List<Quiz> quizzes = new ArrayList<Quiz>();

	public Project()
	{
		
	}
	
	public boolean addBank(QuestionBank bank)
	{
		boolean added = banks.add(bank);
		
		if (added)
		{
			// If this QuestionBank was added to the list, we need to send an addition event
			fireEvent(new ProjectBankUpdateEvent(this, ListUpdateType.Addition, bank));
		}
		
		return added;
	}
	
	public boolean removeBank(QuestionBank bank)
	{
		boolean included = banks.remove(bank); 
		
		if (included)
		{
			// If this QuestionBank was included in the list, we need to send a deletion event
			fireEvent(new ProjectBankUpdateEvent(this, ListUpdateType.Deletion, bank));
		}
		
		return included;
	}
	
	/**
	 * @return A shallow copy of the list of QuestionBanks included in the Project
	 */
	public List<QuestionBank> getQuestionBanks()
	{
		return new ArrayList<QuestionBank>(banks);
	}
	
//	public boolean addQuiz(Quiz quiz)
//	{
//		return quizzes.add(quiz);
//	}
	
	
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
	
}
