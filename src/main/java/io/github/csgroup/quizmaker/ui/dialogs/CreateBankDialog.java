package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Project;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;

/**
 * Creates a frame that allows the user to enter a name for a new quiz bank
 * 
 * @author Emily Palmer
 */
public class CreateBankDialog
{
	private JFrame newBankFrame;

	public CreateBankDialog(Project currentProject)
	{
		createAddBankFrame(currentProject);
	}

	/**
	 * Creates a frame that prompts the user to enter a quiz bank name 
	 * and then add the bank with a button. Once the button is clicked, 
	 * the new quiz bank is displayed on the main dashboard.
	 * 
	 * @param bankProject used to add the new question bank to the QuestionBank list
	 */
	private void createAddBankFrame(Project bankProject)
	{                
		newBankFrame = new JFrame();
		newBankFrame.setSize(400, 290);

		JLabel instructionLabel = new JLabel("Enter quiz bank name: ");
		JTextField bankTextField = new JTextField();
		JButton addButton = new JButton("Add");

		// contains instructionLabel, bankTextField, and addQuizBankButton
		JPanel newBankPanel = new JPanel(new GridBagLayout());
		GridBagConstraints labelConstraint = new GridBagConstraints();
		GridBagConstraints textFieldConstraint = new GridBagConstraints();
		GridBagConstraints buttonConstraint = new GridBagConstraints();

		// places intructionLabel at the top of the panel
		labelConstraint.fill = GridBagConstraints.HORIZONTAL;
		labelConstraint.gridx = 0;
		labelConstraint.gridy = 0;
		newBankPanel.add(instructionLabel, labelConstraint);

		// places bankTextField in the middel of the panel
		textFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
		textFieldConstraint.gridx = 0;
		textFieldConstraint.gridy = 1;
		newBankPanel.add(bankTextField, textFieldConstraint);

		// places addButton beside bankTextField
		buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
		buttonConstraint.gridx = 1;
		buttonConstraint.gridy = 1;
		newBankPanel.add(addButton, buttonConstraint);

		newBankFrame.add(newBankPanel);

		// listens for when addButton is clicked
		addButton.addActionListener((ActionEvent e) -> {
			// get the question bank name from bankTextField
			String bankName = bankTextField.getText();
			// create a QuestionBank object for bankName
			QuestionBank newBank = new QuestionBank(bankName);
			// add the QuesionBank object to the QuestionBank list                    
			bankProject.addBank(newBank);
			// close newBankFrame                    
			newBankFrame.dispose();
			// add quiz bank name to nameList to display on BankPanel
		});     
	}

	/**
	 * Controls when and where the frame appears
	 * 
	 */
	public void show()
	{
		// makes the JFrame appear in the center of the screen
		newBankFrame.setLocationRelativeTo(null);
		// makes the JFrame visible
		newBankFrame.setVisible(true);            
	} 
}
