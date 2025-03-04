package io.github.csgroup.quizmaker.data.quiz;

import io.github.csgroup.quizmaker.data.QuestionBank;

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
	private int pointsPerQuestion;
	
	public BankSelection(QuestionBank bank, int questionCount, int pointsPerQuestion)
	{
		this.bank = bank;
		
		setQuestionCount(questionCount);
		setPointsPerQuestion(pointsPerQuestion);
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
		// Limit the question count
		if (count > bank.getQuestionCount())
			count = bank.getQuestionCount();
		else if (count < 0)
			count = 0;
		
		this.questionCount = count;
	}
	
	public void setPointsPerQuestion(int points)
	{
		// Require points to be at least 0
		if (points < 0)
			points = 0;
		
		this.pointsPerQuestion = 0;
	}
	
	public QuestionBank getBank()
	{
		return bank;
	}
	
	public int getQuestionCount()
	{
		return questionCount;
	}
	
	public int getPointsPerQuestion()
	{
		return pointsPerQuestion;
	}
	
}
