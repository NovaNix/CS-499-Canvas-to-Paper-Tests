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
import io.github.csgroup.quizmaker.utils.stores.writable.DefaultWritableStore;
import io.github.csgroup.quizmaker.utils.stores.writable.WritableStore;

/**
 * A {@link Question} where the user fills in blank spots.<br>
 * <br>
 * This question type should be used for the following QTI question types:
 * <ul>
 *   <li>Fill in the Blank</li>
 *   <li>Fill in Multiple Blanks</li>
 *   <li>Dropdown</li>
 *   <li>Multiple Dropdowns</li>
 * </ul>
 * 
 * @author Michael Nix
 */
public class FillInTheBlankQuestion extends Question
{

	public static final Pattern TAG_REGEX = Pattern.compile("\\[(.+?)\\]"); 
	
	private final Map<String, BlankAnswer> answers = new HashMap<String, BlankAnswer>();
	
	/**
	 * Whether the possible answers for each blank will be shown underneath the question
	 */
	private WritableStore<Boolean> showAnswers = new DefaultWritableStore<Boolean>(false);
	
	public FillInTheBlankQuestion(String title)
	{
		super(title);
	}
	
	public FillInTheBlankQuestion(String title, float points)
	{
		super(title, points);
	}
	
	public FillInTheBlankQuestion(String id, String title, float points) 
	{
		super(id, title, points);
	}
	
	/**
	 * Sets the answer for a specific placeholder tag<br>
	 * <br>
	 * For example, if your label is "Fill in the [0]", the tag is "0"
	 * @param tag 
	 * @param answer 
	 */
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
	
	/**
	 * Finds all of the tags inside of this question.<br>
	 * <br>
	 * For example, if your label is "Fill in the [0], hello [world]", the tags will be "0" and "world"
	 * 
	 * @return A list of the placeholder tags used in this question's label
	 */
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
				a.append(answer.getAnswer().asText());
			}
			
			added = true;
		}
		
		return a.toString();
	}
	
	/**
	 * Sets whether the possible answers for each blank should be displayed under the question
	 * @param showAnswers
	 */
	public void setShowAnswers(boolean showAnswers)
	{
		this.showAnswers.set(showAnswers);
		
		fireEvent(new QuestionUpdateEvent(this));
	}
	
	/**
	 * @return whether the possible answers for each blank should be shown under the question when exported to a Word file
	 */
	public boolean shouldShowAnswers()
	{
		return showAnswers.get();
	}
	
	public WritableStore<Boolean> getShouldShowAnswersStore()
	{
		return showAnswers;
	}

	@Override
	public Question clone()
	{
		var c = new FillInTheBlankQuestion(getId(), getTitle(), getPoints());
		
		c.setLabel(getLabel().clone());
		c.setShowAnswers(shouldShowAnswers());
		
		for (String key : answers.keySet())
		{
			c.setAnswer(key, (BlankAnswer) answers.get(key).clone());
		}
		
		return c;
	}
	
}
