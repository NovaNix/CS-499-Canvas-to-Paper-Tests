package io.github.csgroup.quizmaker.data.events.project;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.utils.ListUpdateType;

/**
 * An event fired when a QuestionBank is added or removed from the Project
 * 
 * @author Michael Nix
 */
public class ProjectBankUpdateEvent extends ProjectEvent 
{

	private final ListUpdateType action;
	private final QuestionBank modified;
	private final int index;
	
	public ProjectBankUpdateEvent(Project project, ListUpdateType action, QuestionBank modified, int index) 
	{
		super(project);
		
		this.action = action;
		this.modified = modified;
		this.index = index;
	}

	/**
	 * @return What action was performed on the Project
	 */
	public ListUpdateType getAction()
	{
		return action;
	}
	
	/**
	 * @return What item was added or removed from the Project's QuestionBank list
	 */
	public QuestionBank getModified()
	{
		return modified;
	}
	
	/**
	 * @return The index of the modified item
	 */
	public int getIndex()
	{
		return index;
	}
	
}
