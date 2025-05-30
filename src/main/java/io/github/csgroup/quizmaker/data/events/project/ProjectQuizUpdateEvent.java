package io.github.csgroup.quizmaker.data.events.project;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.events.ListUpdateEvent;
import io.github.csgroup.quizmaker.utils.ListUpdateType;

/**
 * An event fired when a Quiz is added or removed from the Project
 * 
 * @author Michael Nix
 */
public class ProjectQuizUpdateEvent extends ProjectEvent implements ListUpdateEvent<Quiz>
{
	private final ListUpdateType action;
	private final Quiz modified;
	private final int index;
	
	public ProjectQuizUpdateEvent(Project project, ListUpdateType action, Quiz modified, int index) 
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
	 * @return What item was added or removed from the Project's Quiz list
	 */
	@Override
	public Quiz getModified()
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
