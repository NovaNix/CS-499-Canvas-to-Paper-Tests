package io.github.csgroup.quizmaker.data;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.events.QuizListener;
import io.github.csgroup.quizmaker.data.events.quiz.QuizEvent;
import io.github.csgroup.quizmaker.data.events.quiz.QuizRenameEvent;
import io.github.csgroup.quizmaker.data.quiz.BankSelection;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;
import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;
import io.github.csgroup.quizmaker.data.utils.DataUtils;
import io.github.csgroup.quizmaker.data.utils.QuestionContainer;
import io.github.csgroup.quizmaker.events.ListUpdateListener;
import io.github.csgroup.quizmaker.utils.stores.writable.DefaultWritableStore;
import io.github.csgroup.quizmaker.utils.stores.writable.WritableStore;

/**
 * A dynamically generated quiz that pulls {@link Question Questions} from {@link QuestionBank QuestionBanks}.<br>
 * <br>
 * A Quiz is only the information needed to generate a quiz. 
 * 
 * @author Michael Nix
 */
public class Quiz implements QuestionContainer
{

	private final String id;
	private WritableStore<String> title;

	private Label description;

	private WritableStore<Boolean> shuffleAnswers;

	private final QuestionBank internalQuestions;

	private List<BankSelection> banks = new ArrayList<BankSelection>();

	private final QuizMetadata metadata = new QuizMetadata();
	
	// Generated Quiz Cache
	private GeneratedQuiz generated;
	
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
		this.title = new DefaultWritableStore<String>(title);

		this.internalQuestions = new QuestionBank(title + "-Questions");
		
		this.description = desc;
		
		shuffleAnswers = new DefaultWritableStore<Boolean>(true);
	}

	/**
	 * Takes the information stored within this Quiz and created a new GeneratedQuiz based on it
	 * @return the generated quiz
	 */
	public GeneratedQuiz regenerate()
	{
		this.generated = new GeneratedQuiz(this);
		
		return generated;
	}

	/**
	 * @return the generated version of this quiz. May be null if the quiz has not been generated before.
	 */
	public GeneratedQuiz getGenerated()
	{
		return generated;
	}
	
	/**
	 * @return whether this quiz has been generated
	 */
	public boolean isGenerated()
	{
		return generated != null;
	}
	
//	public boolean isGeneratedOutdated()
//	{
//		return isGeneratedDirty;
//	}
	
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
	
	/**
	 * @return the questions stored internally on this quiz. this does not include questions pulled from question banks
	 */
	public List<Question> getQuestions()
	{
		return internalQuestions.getQuestions();
	}
	
	public QuestionBank getInternalQuestions()
	{
		return internalQuestions;
	}
	
	public void setTitle(String title)
	{
		String oldName = getTitle();
		
		this.title.set(title);

		this.internalQuestions.setTitle(title + "-Questions");
		
		fireEvent(new QuizRenameEvent(this, oldName, title));
	}

	public String getTitle()
	{
		return title.get();
	}

	public WritableStore<String> getTitleStore()
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
	
	public QuizMetadata getMetadata()
	{
		return metadata;
	}
	
	public void setShuffleAnswers(boolean shouldShuffle)
	{
		this.shuffleAnswers.set(shouldShuffle);
	}
	
	public boolean shouldShuffleAnswers()
	{
		return shuffleAnswers.get();
	}
	
	public WritableStore<Boolean> getShouldShuffleStore()
	{
		return shuffleAnswers;
	}

	@Override
	public String toString()
	{
		return getTitle();
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
