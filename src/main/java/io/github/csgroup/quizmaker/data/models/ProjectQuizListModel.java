package io.github.csgroup.quizmaker.data.models;

import javax.swing.AbstractListModel;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.events.ProjectListener;
import io.github.csgroup.quizmaker.data.events.QuizListener;
import io.github.csgroup.quizmaker.data.events.project.ProjectEvent;
import io.github.csgroup.quizmaker.data.events.project.ProjectQuizUpdateEvent;
import io.github.csgroup.quizmaker.data.events.quiz.QuizEvent;
import io.github.csgroup.quizmaker.data.events.quiz.QuizRenameEvent;

public class ProjectQuizListModel extends AbstractListModel<Quiz> implements ProjectListener, QuizListener
{
	private static final long serialVersionUID = -2779441913308296983L;
	
	private Project project;
	
	public ProjectQuizListModel(Project project)
	{
		this.project = project;
		
		project.addListener(this);
	}
	
	@Override
	public void onProjectEvent(Project source, ProjectEvent e) 
	{
		if (e instanceof ProjectQuizUpdateEvent)
		{
			ProjectQuizUpdateEvent update = (ProjectQuizUpdateEvent) e;
			
			Quiz quiz = update.getModified();
			
			switch (update.getAction())
			{
				case Addition:
					fireIntervalAdded(this, update.getIndex(), update.getIndex());
					quiz.addListener(this); // We need to add ourselves as a listener to get rename events
					break;
				case Deletion:
					fireIntervalRemoved(this, update.getIndex(), update.getIndex());
					quiz.removeListener(this); // When the bank is removed from the project we need to stop listening to it
					break;
			}
		}
		
	}
	
	@Override
	public void onQuizEvent(Quiz source, QuizEvent e) 
	{
		if (e instanceof QuizRenameEvent)
		{
			// Fire list change event
			
			int index = project.getQuizIndex(source);
			
			if (index != -1)
			{
				// This bank is included in the project, fire the event
				fireContentsChanged(this, index, index);
			}
		}
		
	}
	
	@Override
	public int getSize() 
	{
		return project.getQuizCount();
	}

	@Override
	public Quiz getElementAt(int index) 
	{
		return project.getQuiz(index);
	}
}
