package io.github.csgroup.quizmaker.data.questions;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.Answer;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.events.question.QuestionUpdateEvent;

public class MultipleChoiceQuestion extends Question
{

	// TODO this system should be refactored. I'm not happy with how answers and correct answers are stored right now. 
	
	private List<Answer> answers = new ArrayList<Answer>();
	private List<Integer> correctAnswers = new ArrayList<Integer>();
	
	public MultipleChoiceQuestion(String id, String title, float points) 
	{
		super(id, title, points);
	}
	
	public void addAnswer(Answer answer, boolean correct)
	{
		answers.add(answer);
		
		if (correct)
		{
			correctAnswers.add(answer.getId());
		}
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public void removeAnswer(Answer answer)
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
	
	public List<Answer> getAnswers()
	{
		return new ArrayList<Answer>(answers);
	}
	
	public List<Answer> getCorrectAnswers()
	{
		ArrayList<Answer> correct = new ArrayList<Answer>();
		
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
		List<Answer> correctAnswers = getCorrectAnswers();
		List<Answer> incorrectAnswers = getAnswers();
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
