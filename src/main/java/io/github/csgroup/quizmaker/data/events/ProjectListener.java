package io.github.csgroup.quizmaker.data.events;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.events.project.ProjectEvent;

/**
 * An object that runs code when {@link ProjectEvent ProjectEvents} are fired
 * 
 * @author Michael Nix
 */
public interface ProjectListener 
{

	/**
	 * Called when a ProjectEvent is fired
	 * @param source The source of the event
	 * @param e The fired event
	 */
	public void onProjectEvent(Project source, ProjectEvent e);
	
}
