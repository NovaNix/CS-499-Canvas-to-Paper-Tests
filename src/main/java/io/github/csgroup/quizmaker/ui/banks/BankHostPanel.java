package io.github.csgroup.quizmaker.ui.banks;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;

/**
 * Creates a JPanel that contains the quiz bank JList, add and remove 
 * buttons, and JTable containing the bank's questions and answers 
 *
 * @author Emily Palmer
 */
public class BankHostPanel extends JComponent
{ 
	private final Project quizBankProject;

	public BankHostPanel(Project bankProject)
	{
		quizBankProject = bankProject;
		createBankHostPanel();                
	}

	/**
	 * Organizes the quiz bank panel and table panel using the 
	 * GridBagLayout layout manager
	 * 
	 * @return JPanel containing the quiz bank list, buttons, and table
	 */
	private void createBankHostPanel()
	{
		// get the quiz bank panel from the BankPanel class
		BankPanel quizBankPanel = new BankPanel(quizBankProject);

		// get the table panel from the TablePanel class
		QuestionTable questionAnswerTable = new QuestionTable();

		this.setLayout(new GridBagLayout());
		GridBagConstraints bankPanelConstraint = new GridBagConstraints();
		GridBagConstraints tablePanelConstraint = new GridBagConstraints();

		// places bankButtonPanel on the left side of BankHostPanel
		bankPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
		bankPanelConstraint.gridx = 0;
		bankPanelConstraint.gridy = 0;
		this.add(quizBankPanel, bankPanelConstraint);

		// places tablePanel on the right side of BankHostPanel
		tablePanelConstraint.fill = GridBagConstraints.HORIZONTAL;
		tablePanelConstraint.gridx = 1;
		tablePanelConstraint.gridy = 0;
		this.add(questionAnswerTable, tablePanelConstraint);           
	}   
}
