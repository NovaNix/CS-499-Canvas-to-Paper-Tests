package io.github.csgroup.quizmaker.data.models;

import javax.swing.AbstractListModel;
import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.events.BankListener;
import io.github.csgroup.quizmaker.data.events.ProjectListener;
import io.github.csgroup.quizmaker.data.events.bank.BankEvent;
import io.github.csgroup.quizmaker.data.events.bank.BankRenameEvent;
import io.github.csgroup.quizmaker.data.events.project.ProjectBankUpdateEvent;
import io.github.csgroup.quizmaker.data.events.project.ProjectEvent;

public class ProjectBankListModel extends AbstractListModel<QuestionBank> implements ProjectListener, BankListener
{
	private static final long serialVersionUID = -6841022113137439678L;

	private Project project;
	
	public ProjectBankListModel(Project project)
	{
		this.project = project;
		
		project.addListener(this); // TODO remove this listener when destroying the object, this might cause a small memory leak
	}
	
	@Override
	public int getSize() 
	{
		return project.getBankCount();
	}

	@Override
	public QuestionBank getElementAt(int index) 
	{
		return project.getBank(index);
	}

	@Override
	public void onProjectEvent(Project source, ProjectEvent e) 
	{
		if (e instanceof ProjectBankUpdateEvent)
		{
			ProjectBankUpdateEvent update = (ProjectBankUpdateEvent) e;
			
			QuestionBank bank = update.getModified();
			
			switch (update.getAction())
			{
				case Addition:
					fireIntervalAdded(this, update.getIndex(), update.getIndex());
					bank.addListener(this); // We need to add ourselves as a listener to get rename events
					break;
				case Deletion:
					fireIntervalRemoved(this, update.getIndex(), update.getIndex());
					bank.removeListener(this); // When the bank is removed from the project we need to stop listening to it
					break;
			}
		}
		
	}
	
	@Override
	public void onBankEvent(QuestionBank source, BankEvent e) 
	{
		if (e instanceof BankRenameEvent)
		{
			// Fire list change event
			
			int index = project.getBankIndex(source);
			
			if (index != -1)
			{
				// This bank is included in the project, fire the event
				fireContentsChanged(this, index, index);
			}
		}
		
	}

}
