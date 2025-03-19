package io.github.csgroup.quizmaker.ui.events.questioneditor;

import io.github.csgroup.quizmaker.ui.questions.QuestionEditor;

/**
 * An event related to {@link QuestionEditor QuestionEditors}
 * 
 * @author Michael Nix
 */
public class QuestionEditorEvent 
{

	private QuestionEditor source;
	
	public QuestionEditorEvent(QuestionEditor source)
	{
		this.source = source;
	}
	
	public QuestionEditor getSource()
	{
		return source;
	}
	
}
