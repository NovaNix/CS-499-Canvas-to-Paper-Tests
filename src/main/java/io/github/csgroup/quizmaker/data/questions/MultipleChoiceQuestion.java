package io.github.csgroup.quizmaker.data.questions;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.events.question.QuestionUpdateEvent;

public class MultipleChoiceQuestion extends Question
{

	// TODO this system should be refactored. I'm not happy with how answers and correct answers are stored right now. 
	
	private List<SimpleAnswer> answers = new ArrayList<SimpleAnswer>();
	private List<Integer> correctAnswers = new ArrayList<Integer>();
	
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
			if (correctAnswers.contains(answer.getId()))
			{
				correct.add(answer);
			}
		}
		
		return correct;
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
	
}
