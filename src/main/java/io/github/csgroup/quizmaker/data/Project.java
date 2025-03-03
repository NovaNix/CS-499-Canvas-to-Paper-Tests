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
import io.github.csgroup.quizmaker.data.models.ProjectQuizListModel;
import io.github.csgroup.quizmaker.qti.QTIContents;

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
	
	/**
	 * Adds the contents of a QTI file to the Project
	 * @param qti The contents of the QTI file
	 */
	public void addQTI(QTIContents qti)
	{
		addBanks(qti.banks);
		addQuizzes(qti.quizzes);
	}
	
	/**
	 * Adds a single {@link QuestionBank} to this Project.<br>
	 * Fires an event when the bank is added.
	 * @param bank the bank to add to the Project
	 * @return true (to match Collection.add)
	 */
	public boolean addBank(QuestionBank bank)
	{
		boolean added = banks.add(bank);
		
		if (added)
		{
			int index = banks.size() - 1;
			
			// If this QuestionBank was added to the list, we need to send an addition event
			fireEvent(new ProjectBankUpdateEvent(this, ListUpdateType.Addition, bank, index));
		}
		
		return added;
	}
	
	/**
	 * Adds multiple {@link QuestionBank QuestionBanks} to this Project.<br>
	 * Fires an event for each bank added
	 * @param banks the banks to add
	 */
	public void addBanks(List<QuestionBank> banks)
	{
		for (var bank : banks)
		{
			// TODO this could be optimized further by changing how the events work to support multiple added items
			addBank(bank);
		}
	}
	
	/**
	 * Adds multiple {@link QuestionBank QuestionBanks} to this Project.<br>
	 * Fires an event for each bank added
	 * @param banks the banks to add
	 */
	public void addBanks(QuestionBank... banks)
	{
		for (var bank : banks)
		{
			// TODO this could be optimized further by changing how the events work to support multiple added items
			addBank(bank);
		}
	}
	
	/**
	 * Removes a {@link QuestionBank} from this Project.<br>
	 * Fires an event when the bank is removed
	 * @param bank the bank to remove
	 * @return whether the bank was included in the Project
	 */
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
	
	/**
	 * @return the number of {@link QuestionBank QuestionBanks} in this Project
	 */
	public int getBankCount()
	{
		return banks.size();
	}
	
	public QuestionBank getBank(int index)
	{
		return banks.get(index);
	}
	
	/**
	 * Finds the index of a {@link QuestionBank} included in this Project
	 * @param bank the bank to search for
	 * @return the index of the bank, or -1 if the bank is not included in this Project
	 */
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
	
	/**
	 * Adds a single {@link Quiz} to this Project.<br>
	 * Fires an event when the quiz is added.
	 * @param quiz the quiz to add to the Project
	 * @return true (to match Collection.add)
	 */
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
	
	/**
	 * Adds multiple {@link Quiz Quizzes} to this Project.<br>
	 * Fires an event for each quiz added
	 * @param quizzes the quizzes to add
	 */
	public void addQuizzes(List<Quiz> quizzes)
	{
		for (var quiz : quizzes)
		{
			// TODO this could be optimized further by changing how the events work to support multiple added items
			addQuiz(quiz);
		}
	}
	
	/**
	 * Adds multiple {@link Quiz Quizzes} to this Project.<br>
	 * Fires an event for each quiz added
	 * @param quizzes the quizzes to add
	 */
	public void addQuizzes(Quiz... quizzes)
	{
		for (var quiz : quizzes)
		{
			// TODO this could be optimized further by changing how the events work to support multiple added items
			addQuiz(quiz);
		}
	}
	
	/**
	 * Removes a {@link Quiz} from this Project.<br>
	 * Fires an event when the quiz is removed
	 * @param quiz the quiz to remove
	 * @return whether the quiz was included in the Project
	 */
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
	
	/**
	 * @return the number of {@link Quiz Quizzes} in this Project
	 */
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
	private ListModel<Quiz> quizModel = new ProjectQuizListModel(this);
	
	public ListModel<QuestionBank> getBankModel()
	{
		return bankModel;
	}
	
	public ListModel<Quiz> getQuizModel()
	{
		return quizModel;
	}
	
}
