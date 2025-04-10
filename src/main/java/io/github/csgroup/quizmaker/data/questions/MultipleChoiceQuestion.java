package io.github.csgroup.quizmaker.data.questions;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.events.question.QuestionUpdateEvent;

/**
 * A {@link Question} where the user selects one or more possible answers.<br>
 * <br>
 * This question type should be used for the following QTI question types:
 * <ul>
 *   <li>Multiple Choice</li>
 *   <li>True or False</li>
 *   <li>Multiple Answers</li>
 * </ul>
 */
public class MultipleChoiceQuestion extends Question
{
	// TODO consider making true or false its own question type, for simplicity 
	
	// TODO this system should be refactored. I'm not happy with how answers and correct answers are stored right now. 
	
	private final List<SimpleAnswer> answers = new ArrayList<SimpleAnswer>();
	private final List<Integer> correctAnswers = new ArrayList<Integer>();
	
	public MultipleChoiceQuestion(String title)
	{
		super(title);
	}
	
	public MultipleChoiceQuestion(String title, float points)
	{
		super(title, points);
	}
	
	public MultipleChoiceQuestion(String id, String title, float points) 
	{
		super(id, title, points);
	}
	
	public void addAnswer(SimpleAnswer answer, boolean correct)
	{
		answers.add(answer);
		
		if (correct)
		{
			correctAnswers.add(answer.getId());
		}
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public void removeAnswer(SimpleAnswer answer)
	{
		boolean removed = answers.remove(answer);
		
		if (removed)
		{
			correctAnswers.remove(answer.getId());
		}
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public void clearAnswers()
	{
		this.answers.clear();
		this.correctAnswers.clear();
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public List<SimpleAnswer> getAnswers()
	{
		return new ArrayList<SimpleAnswer>(answers);
	}
	
	public List<SimpleAnswer> getCorrectAnswers()
	{
		List<SimpleAnswer> correct = new ArrayList<SimpleAnswer>();
		
		// TODO this could be optimized further. If we experience speed issues, address this
		
		for (var answer : answers)
		{
			if (isCorrect(answer))
			{
				correct.add(answer);
			}
		}
		
		return correct;
	}
	
	/**
	 * @param answer the answer to check the correctness of
	 * @return whether the answer is correct or not
	 */
	public boolean isCorrect(SimpleAnswer answer)
	{
		return correctAnswers.contains(answer.getId());
	}

	@Override
	public String getAnswerString()
	{
		List<SimpleAnswer> correctAnswers = getCorrectAnswers();
		List<SimpleAnswer> incorrectAnswers = getAnswers();
		incorrectAnswers.removeAll(correctAnswers);
		
		StringBuilder s = new StringBuilder("<html>");
		
		boolean added = false;
		
		for (var answer : correctAnswers)
		{
			if (added)
			{
				s.append(", ");
			}
			
			s.append("<b>");
			s.append(answer.asText());
			s.append("</b>");
		}
		
		for (var answer : incorrectAnswers)
		{
			if (added)
			{
				s.append(", ");
			}
			
			s.append(answer.asText());
		}
		
		s.append("</html>");
		
		return s.toString();
	}

	@Override
	public Question clone()
	{
		var c = new MultipleChoiceQuestion(getId(), getTitle(), getPoints());
		
		c.setLabel(getLabel().clone());
		c.setAbet(isAbet());
		
		for (var answer : answers)
		{
			c.addAnswer((SimpleAnswer) answer.clone(), isCorrect(answer));
		}
		
		return c;
	}
	
}
