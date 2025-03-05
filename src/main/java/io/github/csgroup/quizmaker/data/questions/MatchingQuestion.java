package io.github.csgroup.quizmaker.data.questions;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.events.question.QuestionUpdateEvent;

/**
 * A {@link Question} where the user connects two labels together<br>
 * <br>
 * 
 * This question type should be used for the following QTI question types:
 * <ul>
 *   <li>Matching</li>
 * </ul>
 * 
 * @author Michael Nix
 */
public class MatchingQuestion extends Question
{
	
	private List<MatchingAnswer> answers = new ArrayList<MatchingAnswer>();
	
	public MatchingQuestion(String title)
	{
		super(title);
	}
	
	/**
	 * Creates a new MatchingQuestion with a generated Id 
	 * @param title
	 * @param points
	 */
	public MatchingQuestion(String title, float points)
	{
		super(title, points);
	}
	
	public MatchingQuestion(String id, String title, float points) 
	{
		super(id, title, points);
	}

	public void addAnswer(MatchingAnswer answer)
	{
		answers.add(answer);
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public boolean removeAnswer(MatchingAnswer answer)
	{
		boolean included = answers.remove(answer);
		
		if (included)
		{
			fireEvent(new QuestionUpdateEvent(this));
		}
		
		return included;
	}
	
	public MatchingAnswer getAnswer(int index)
	{
		return answers.get(index);
	}
	
	/**
	 * @return a shallow copy of the list of answers included in this question
	 */
	public List<MatchingAnswer> getAnswers()
	{
		return new ArrayList<MatchingAnswer>(answers);
	}
	
	public int getAnswerCount()
	{
		return answers.size();
	}
	
	@Override
	public String getAnswerString() 
	{
		StringBuilder a = new StringBuilder();
		
		boolean added = false; // Whether or not an answer has been added yet 
		
		for (var answer : answers)
		{
			if (added)
				a.append(", ");
			
			a.append(answer.asText());
			
			added = true;
		}
		
		return a.toString();
	}

	
}
