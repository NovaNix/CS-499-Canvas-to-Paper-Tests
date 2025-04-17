package io.github.csgroup.quizmaker.data.events.project;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;
import io.github.csgroup.quizmaker.events.ListUpdateEvent;
import io.github.csgroup.quizmaker.utils.ListUpdateType;

/**
 * An event fired when a GeneratedQuiz is added or removed from the Project
 * 
 * @author Michael Nix
 */
public class ProjectGeneratedQuizUpdateEvent extends ProjectEvent implements ListUpdateEvent<GeneratedQuiz>
{
	private final ListUpdateType action;
	private final GeneratedQuiz modified;
	private final int index;
	
	public ProjectGeneratedQuizUpdateEvent(Project project, ListUpdateType action, GeneratedQuiz modified, int index) 
	{
		super(project);
		
		this.action = action;
		this.modified = modified;
		this.index = index;
	}

	/**
	 * @return What action was performed on the Project
	 */
	@Override
	public ListUpdateType getAction()
	{
		return action;
	}
	
	/**
	 * @return What item was added or removed from the Project's Generated Quiz list
	 */
	@Override
	public GeneratedQuiz getModified()
	{
		return modified;
	}
	
	/**
	 * @return The index of the modified item
	 */
	@Override
	public int getIndex()
	{
		return index;
	}
}
