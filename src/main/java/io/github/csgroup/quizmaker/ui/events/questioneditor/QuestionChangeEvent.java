package io.github.csgroup.quizmaker.ui.events.questioneditor;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.ui.questions.QuestionEditor;

/**
 * A {@link QuestionEditorEvent} fired when the active question is changed.<br>
 * <br>
 * This could be used for a variety of purposes. 
 * If a copy of a question was passed into the editor, 
 * this could be used to update the question when the editor changes questions.<br>
 * <br>
 * Note: this should not be mistaken with {@link QuestionUpdateEvent}. 
 * QuestionUpdateEvent is fired when a {@link Question} is modified. 
 * This event is fired when the question inside of a {@link QuestionEditor} is changed.<br>
 * <br>
 * This class should be called QuestionEditorQuestionChangeEvent but that was too long.
 * 
 * @author Michael Nix
 */
public class QuestionChangeEvent extends QuestionEditorEvent
{
	
	private final Question oldQuestion;
	private final Question newQuestion;
	
	public QuestionChangeEvent(QuestionEditor source, Question oldQuestion, Question newQuestion) 
	{
		super(source);
		
		this.oldQuestion = oldQuestion;
		this.newQuestion = newQuestion;
	}
	
	public Question getOld()
	{
		return oldQuestion;
	}
	
	public Question getNew()
	{
		return newQuestion;
	}

	
	
}
