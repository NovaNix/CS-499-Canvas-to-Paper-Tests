package io.github.csgroup.quizmaker.data.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.utils.RandomBag;
import io.github.csgroup.quizmaker.utils.stores.writable.DefaultWritableStore;
import io.github.csgroup.quizmaker.utils.stores.writable.WritableStore;

/**
 * Represents {@link Question Questions} to be pulled from a {@link QuestionBank}.
 * 
 * @author Michael Nix
 */
public class BankSelection 
{

	/** The bank to pull questions from */
	private WritableStore<QuestionBank> bank;
	
	/** The number of questions to take from the bank */
	private WritableStore<Integer> questionCount;
	
	/** The number of points each question should be */
	private WritableStore<Float> pointsPerQuestion;
	
	/** A list of Question ids to remove from the selection */
	private List<String> blockedQuestions = new ArrayList<String>();
	
	public BankSelection(QuestionBank bank, int questionCount, float pointsPerQuestion)
	{
		this.bank = new DefaultWritableStore<QuestionBank>(bank);
		
		this.questionCount = new DefaultWritableStore<Integer>(questionCount);
		this.pointsPerQuestion = new DefaultWritableStore<Float>(pointsPerQuestion);
		
		setQuestionCount(questionCount);
		setPointsPerQuestion(pointsPerQuestion);
	}
	
	public RandomBag<Question> asRandomBag()
	{
		return asRandomBag(new Random());
	}
	
	public RandomBag<Question> asRandomBag(Random random)
	{
		List<Question> contents = getBank().getQuestions();
		
		// Remove blocked questions from the list
		List<Question> allowedQuestions = contents.stream()
			.filter((question) -> !blockedQuestions.contains(question.getId()))
			.toList();
		
		return new RandomBag<Question>(allowedQuestions);
	}
	
	public void setBank(QuestionBank bank)
	{
		this.bank.set(bank);
	}
	
	/**
	 * Sets the number of questions that should be pulled from the QuestionBank.<br>
	 * <br>
	 * If the question count is outside the bounds of the bank, it will be automatically limited. 
	 * @param count the number of questions to select, between 0 and the number of questions in the bank
	 */
	public void setQuestionCount(int count)
	{
		// TODO the max question count should be limited by the number of removed questions as well
		
		// Limit the question count
		if (count > getBank().getQuestionCount())
			count = getBank().getQuestionCount();
		else if (count < 0)
			count = 0;
		
		this.questionCount.set(count);
	}
	
	public void setPointsPerQuestion(float points)
	{
		// Require points to be at least 0
		if (points < 0)
			points = 0;
		
		this.pointsPerQuestion.set(points);
	}
	
	public void addBlockedQuestion(Question q)
	{
		addBlockedQuestion(q.getId());
	}
	
	public void addBlockedQuestion(String id)
	{
		blockedQuestions.add(id);
	}
	
	public void removeBlockedQuestion(String id)
	{
		blockedQuestions.remove(id);
	}
	
	public void removeBlockedQuestion(Question q)
	{
		removeBlockedQuestion(q.getId());
	}
	
	public void clearBlockedQuestions()
	{
		blockedQuestions.clear();
	}
	
	public QuestionBank getBank()
	{
		return bank.get();
	}
	
	public int getQuestionCount()
	{
		return questionCount.get();
	}
	
	public float getPointsPerQuestion()
	{
		return pointsPerQuestion.get();
	}
	
	public WritableStore<Integer> getQuestionCountStore()
	{
		return questionCount;
	}
	
	public WritableStore<Float> getPointsPerQuestionStore()
	{
		return pointsPerQuestion;
	}
	
	public List<Question> getBlockedQuestions()
	{
		return getBank().getQuestions().stream()
				.filter((q) -> blockedQuestions.contains(q.getId()))
				.toList();
	}
	
	public List<String> getBlockedQuestionIds(boolean includeMissing)
	{
		if (includeMissing)
		{
			return new ArrayList<String>(blockedQuestions);
		}
		
		else
		{
			return getBlockedQuestions().stream()
					.map((question) -> question.getId())
					.toList();
		}
	}
	
}
