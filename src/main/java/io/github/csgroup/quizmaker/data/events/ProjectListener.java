package io.github.csgroup.quizmaker.data.events;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.events.bank.BankEvent;
import io.github.csgroup.quizmaker.data.events.project.ProjectEvent;

public interface ProjectListener 
{

	public void onProjectEvent(Project source, ProjectEvent e);
	
}
