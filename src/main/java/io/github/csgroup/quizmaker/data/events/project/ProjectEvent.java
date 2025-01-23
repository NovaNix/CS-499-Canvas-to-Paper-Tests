package io.github.csgroup.quizmaker.data.events.project;

import io.github.csgroup.quizmaker.data.Project;

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
