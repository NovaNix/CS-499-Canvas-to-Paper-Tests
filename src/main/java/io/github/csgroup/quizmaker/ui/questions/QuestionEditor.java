package io.github.csgroup.quizmaker.ui.questions;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.events.QuestionListener;
import io.github.csgroup.quizmaker.data.events.question.QuestionEvent;
import io.github.csgroup.quizmaker.ui.events.QuestionEditorListener;
import io.github.csgroup.quizmaker.ui.events.questioneditor.QuestionChangeEvent;
import io.github.csgroup.quizmaker.ui.events.questioneditor.QuestionEditorEvent;

/**
 * A reusable UI component for editing {@link Question Questions}.<br>
 * <br>
 * This editor is designed to be reusable, for example it can be used in a dialog or in a side bar.
 * 
 * @author Michael Nix, Emily Palmer
 */
public class QuestionEditor extends JComponent implements QuestionListener
{
	private static final long serialVersionUID = 943871071827246090L;
	
	private Question question;
	
	public QuestionEditor(Question q)
	{
		this.question = q;
		
		q.addListener(this);
		
		generateUI();
	}
	
	/**
	 * Creates all of the UI components needed for the editor. 
	 * This will replace the existing components if they already exist.<br>
	 * <br>
	 * This should be called whenever the structure of the UI needs to radically change, 
	 * such as when the question is replaced.  
	 */
	private void generateUI()
	{
		// This may be a challenging function to implement
		// The structure of the editor should change depending on the type of the question
		// (pattern matching might be useful for that https://docs.oracle.com/en/java/javase/17/language/pattern-matching.html)
		
		// Be sure to not make this function too chunky. 
		// Split things up into separate components, use additional functions, etc 
		
		// If you'd like any help with this, let me know
		// I'm happy to help write parts of this or help you structure things
		// - Michael 
	}
	
	@Override
	public void onQuestionEvent(Question source, QuestionEvent e) 
	{
		if (question != source)
			return; // Prevent any accidental subscriptions
		
		// TODO process any events
	}
	
	/**
	 * Sets the question to be edited by this editor<br>
	 * <br>
	 * Fires a {@link QuestionChangeEvent}
	 * @param q the new question to edit
	 * @author Michael Nix
	 */
	public void setQuestion(Question q)
	{
		Question old = question;
		old.removeListener(this);
		
		this.question = q;
		q.addListener(this);
		
		// Tell all the listeners that the question was changed
		fireEvent(new QuestionChangeEvent(this, old, q));
		
		// Update the UI to reflect the new Question
		generateUI();
	}
	
	// Event Processing
	
	private static List<QuestionEditorListener> globalListeners = new ArrayList<QuestionEditorListener>();
	private List<QuestionEditorListener> localListeners = new ArrayList<QuestionEditorListener>();

	public void addListener(QuestionEditorListener listener)
	{
		localListeners.add(listener);
	}

	public void removeListener(QuestionEditorListener listener)
	{
		localListeners.remove(listener);
	}

	public static void addGlobalListener(QuestionEditorListener listener)
	{
		globalListeners.add(listener);
	}

	public static void removeGlobalListener(QuestionEditorListener listener)
	{
		globalListeners.remove(listener);
	}

	protected void fireEvent(QuestionEditorEvent event)
	{
		for (var listener : globalListeners)
		{
			listener.onQuestionEditorEvent(this, event);
		}

		for (var listener : localListeners)
		{
			listener.onQuestionEditorEvent(this, event);
		}
	}

}
