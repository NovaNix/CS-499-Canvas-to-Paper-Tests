package io.github.csgroup.quizmaker.data.events.project;

import io.github.csgroup.quizmaker.data.Project;

/**
 * An event related to {@link Project Projects}
 * 
 * @author Michael Nix
 */
public abstract class ProjectEvent 
{
	private final Project project;
	
	public ProjectEvent(Project project)
	{
		this.project = project;
	}
	
	public Project getProject()
	{
		return project;
	}
}
