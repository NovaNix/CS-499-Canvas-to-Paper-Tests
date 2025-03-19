package io.github.csgroup.quizmaker.ui.events;

import io.github.csgroup.quizmaker.ui.events.questioneditor.QuestionEditorEvent;
import io.github.csgroup.quizmaker.ui.questions.QuestionEditor;

/**
 * An object that runs code when {@link QuestionEditorEvent QuestionEditorEvents} are fired
 * 
 * @author Michael Nix
 */
public interface QuestionEditorListener 
{
	/**
	 * Called when a QuestionEditorEvent is fired
	 * @param source The source of the event
	 * @param event The fired event
	 */
	public void onQuestionEditorEvent(QuestionEditor source, QuestionEditorEvent event);
	
}
