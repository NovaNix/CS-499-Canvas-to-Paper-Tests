package io.github.csgroup.quizmaker.data.questions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.events.question.QuestionUpdateEvent;

public class FillInTheBlankQuestion extends Question
{

	private static final Pattern TAG_REGEX = Pattern.compile("\\[(.+?)\\]"); 
	
	private Map<String, BlankAnswer> answers = new HashMap<String, BlankAnswer>();
	
	/**
	 * Whether the possible answers for each blank will be shown underneath the question
	 */
	private boolean showAnswers = false;
	
	public FillInTheBlankQuestion(String title, float points)
	{
		super(title, points);
	}
	
	public FillInTheBlankQuestion(String id, String title, float points) 
	{
		super(id, title, points);
	}
	
	public void setAnswer(String tag, BlankAnswer answer)
	{
		answers.put(tag, answer);
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public BlankAnswer getAnswer(String tag)
	{
		return answers.get(tag);
	}
	
	// TODO potentially externalize getTags so it can be reused and tested easier 
	
	public List<String> getTags()
	{
		List<String> tags = new ArrayList<String>();
		
		Matcher m = TAG_REGEX.matcher(this.getLabel().asText());
		
		while (m.find())
		{
			tags.add(m.group(1));
		}
		
		return tags;
	}
	
	
	@Override
	public String getAnswerString() 
	{
		StringBuilder a = new StringBuilder();
		
		List<String> tags = getTags();
	
		boolean added = false;
		
		for (String tag : tags)
		{
			BlankAnswer answer = getAnswer(tag);
			
			if (added)
				a.append(", ");
			
			if (answer != null)
			{
				a.append(answer.getCorrect().asText());
			}
			
			added = true;
		}
		
		return a.toString();
	}
	
	public void setShowAnswers(boolean showAnswers)
	{
		this.showAnswers = showAnswers;
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	public boolean shouldShowAnswers()
	{
		return showAnswers;
	}


	

	
}
