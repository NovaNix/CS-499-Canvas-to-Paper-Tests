package io.github.csgroup.quizmaker.data;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.events.QuizListener;
import io.github.csgroup.quizmaker.data.events.quiz.QuizEvent;
import io.github.csgroup.quizmaker.data.events.quiz.QuizRenameEvent;
import io.github.csgroup.quizmaker.data.quiz.BankSelection;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;
import io.github.csgroup.quizmaker.data.utils.DataUtils;
import io.github.csgroup.quizmaker.data.utils.QuestionContainer;
import io.github.csgroup.quizmaker.events.ListUpdateListener;

/**
 * A dynamically generated quiz that pulls {@link Question Questions} from {@link QuestionBank QuestionBanks}.<br>
 * 
 * @author Michael Nix
 */
public class Quiz implements QuestionContainer
{

	private final String id;
	private String title;

	private Label description;

	private boolean shuffleAnswers = true;

	private final QuestionBank internalQuestions;

	private List<BankSelection> banks = new ArrayList<BankSelection>();

	public Quiz(String title)
	{
		this(DataUtils.generateId(), title);
	}
	
	public Quiz(String id, String title)
	{
		this(id, title, new Label("", Label.Type.html));
	}
	
	public Quiz(String id, String title, Label desc)
	{
		this.id = id;
		this.title = title;

		this.internalQuestions = new QuestionBank(title + "-Questions");
		
		this.description = desc;
	}

	public GeneratedQuiz generate()
	{
		return null;
	}

	public void addBank(BankSelection selection)
	{
		banks.add(selection);
	}
	
	public void removeBank(BankSelection selection)
	{
		banks.remove(selection);
	}

	public List<BankSelection> getBankSelections()
	{
		return new ArrayList<BankSelection>(banks);
	}
	
	public void addQuestion(Question q)
	{
		internalQuestions.add(q);
	}
	
	public void removeQuestion(Question q)
	{
		internalQuestions.remove(q);
	}
	
	public QuestionBank getInternalQuestions()
	{
		return internalQuestions;
	}
	
	public void setTitle(String title)
	{
		String oldName = this.title;
		
		this.title = title;

		this.internalQuestions.setTitle(title + "-Questions");
		
		fireEvent(new QuizRenameEvent(this, oldName, title));
	}

	public String getTitle()
	{
		return title;
	}

	public String getId()
	{
		return id;
	}
	
	public void setDescription(Label desc)
	{
		this.description = desc;
	}
	
	public Label getDescription()
	{
		return description;
	}
	
	public void setShuffleAnswers(boolean shouldShuffle)
	{
		this.shuffleAnswers = shouldShuffle;
	}
	
	public boolean shouldShuffleAnswers()
	{
		return shuffleAnswers;
	}

	@Override
	public String toString()
	{
		return title;
	}

	// Event Processing

	private static List<QuizListener> globalListeners = new ArrayList<QuizListener>();
	private List<QuizListener> localListeners = new ArrayList<QuizListener>();

	public void addListener(QuizListener listener)
	{
		localListeners.add(listener);
	}

	public void removeListener(QuizListener listener)
	{
		localListeners.remove(listener);
	}

	public static void addGlobalListener(QuizListener listener)
	{
		globalListeners.add(listener);
	}

	public static void removeGlobalListener(QuizListener listener)
	{
		globalListeners.remove(listener);
	}

	protected void fireEvent(QuizEvent event)
	{
		for (var listener : globalListeners)
		{
			listener.onQuizEvent(this, event);
		}

		for (var listener : localListeners)
		{
			listener.onQuizEvent(this, event);
		}
	}

	// Forward all of the QuestionList functions to the internalQuestions
	
	@Override
	public void addListListener(ListUpdateListener<Question> listener) 
	{
		internalQuestions.addListListener(listener);
	}

	@Override
	public void removeListListener(ListUpdateListener<Question> listener) 
	{
		internalQuestions.removeListListener(listener);	
	}

	@Override
	public Question getQuestion(int index) 
	{
		return internalQuestions.getQuestion(index);
	}

	@Override
	public int getQuestionIndex(Question q) 
	{
		return internalQuestions.getQuestionIndex(q);
	}

	@Override
	public int getQuestionCount() 
	{
		return internalQuestions.getQuestionCount();
	}
}
