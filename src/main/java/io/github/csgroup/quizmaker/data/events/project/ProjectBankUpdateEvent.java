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
	
	public ProjectBankUpdateEvent(Project project, ListUpdateType action, QuestionBank modified) 
	{
		super(project);
		
		this.action = action;
		this.modified = modified;
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
	
}
