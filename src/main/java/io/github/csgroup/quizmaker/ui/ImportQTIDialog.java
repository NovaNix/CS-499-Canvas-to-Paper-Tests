package io.github.csgroup.quizmaker.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Creates a frame that will allow the user to import the QTI file from 
 * their file system
 * 
 * @author Emily Palmer
 */
public class ImportQTIDialog 
{ 
	private JFrame importFrame;

	public ImportQTIDialog()
	{
		createImportFileFrame();
	}

	/**
	 * Creates a frame that prompts the user to attach a QTI file
	 * 
	 */
	private void createImportFileFrame()
	{            
		importFrame = new JFrame();
		importFrame.setSize(400, 250);

		JButton attachButton = new JButton("Attach");
		JButton importButton = new JButton("Import");

		JTextField fileTextField = new JTextField(15);

		// contains fileTextField and attachButton
		JPanel importFilePanel = new JPanel(new GridBagLayout());
		GridBagConstraints attachButtonConstraint = new GridBagConstraints();
		GridBagConstraints textFieldConstraint = new GridBagConstraints();

		// places fileTextField at the top of importFilePanel
		textFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
		textFieldConstraint.gridx = 0;
		textFieldConstraint.gridy = 0;
		importFilePanel.add(fileTextField, textFieldConstraint);

		// places attachButton at the bottom of importFilePanel
		attachButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
		attachButtonConstraint.gridx = 1;
		attachButtonConstraint.gridy = 0;
		attachButtonConstraint.insets = new Insets(0, 5, 0, 0);
		importFilePanel.add(attachButton, attachButtonConstraint);

		// contains importButton
		JPanel importButtonPanel = new JPanel(new GridBagLayout());
		importButtonPanel.add(importButton);

		// contains importButtonPanel and importFilePanel
		JPanel importPanel = new JPanel(new GridBagLayout());
		GridBagConstraints importPanelConstraint = new GridBagConstraints();
		GridBagConstraints buttonPanelConstraint = new GridBagConstraints();

		// places importFilePanel at the top of the panel
		importPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
		importPanelConstraint.gridx = 0;
		importPanelConstraint.gridy = 0;
		importPanel.add(importFilePanel, importPanelConstraint);

		// places importButtonPanel at the bottom of the panel
		buttonPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
		buttonPanelConstraint.gridx = 0;
		buttonPanelConstraint.gridy = 1;
		buttonPanelConstraint.insets = new Insets(15, 50, 0, 0);
		importFilePanel.add(importButtonPanel, buttonPanelConstraint);

		importFrame.add(importPanel);  
	}

	/**
	 * Controls when and where the frame appears
	 * 
	 */
	public void show()
	{
		// makes the JFrame appear in the center of the screen
		importFrame.setLocationRelativeTo(null);
		// makes the JFrame visible
		importFrame.setVisible(true);
	}
}