package io.github.csgroup.quizmaker.data.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.utils.RandomBag;

/**
 * Represents {@link Question Questions} to be pulled from a {@link QuestionBank}.
 * 
 * @author Michael Nix
 */
public class BankSelection 
{

	/** The bank to pull questions from */
	private QuestionBank bank;
	
	/** The number of questions to take from the bank */
	private int questionCount;
	
	/** The number of points each question should be */
	private float pointsPerQuestion;
	
	/** A list of Question ids to remove from the selection */
	private List<String> blockedQuestions = new ArrayList<String>();
	
	public BankSelection(QuestionBank bank, int questionCount, float pointsPerQuestion)
	{
		this.bank = bank;
		
		setQuestionCount(questionCount);
		setPointsPerQuestion(pointsPerQuestion);
	}
	
	public RandomBag<Question> asRandomBag()
	{
		return asRandomBag(new Random());
	}
	
	public RandomBag<Question> asRandomBag(Random random)
	{
		List<Question> contents = bank.getQuestions();
		
		// Remove blocked questions from the list
		List<Question> allowedQuestions = contents.stream()
			.filter((question) -> !blockedQuestions.contains(question.getId()))
			.toList();
		
		return new RandomBag<Question>(allowedQuestions);
	}
	
	public void setBank(QuestionBank bank)
	{
		this.bank = bank;
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
		if (count > bank.getQuestionCount())
			count = bank.getQuestionCount();
		else if (count < 0)
			count = 0;
		
		this.questionCount = count;
	}
	
	public void setPointsPerQuestion(float points)
	{
		// Require points to be at least 0
		if (points < 0)
			points = 0;
		
		this.pointsPerQuestion = points;
	}
	
	public QuestionBank getBank()
	{
		return bank;
	}
	
	public int getQuestionCount()
	{
		return questionCount;
	}
	
	public float getPointsPerQuestion()
	{
		return pointsPerQuestion;
	}
	
	public List<Question> getBlockedQuestions()
	{
		return bank.getQuestions().stream()
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
