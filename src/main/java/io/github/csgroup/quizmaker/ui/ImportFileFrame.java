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
public class ImportFileFrame {
    
        public void createImportFileFrame()
        {            
                JFrame importFrame = new JFrame();
                importFrame.setSize(400, 250);
        
                JButton attachButton = new JButton("Attach");
                JButton importButton = new JButton("Import");
        
                JTextField fileTextField = new JTextField(15);
        
                JPanel importFilePanel = new JPanel(new GridBagLayout());
                GridBagConstraints attachButtonConstraint = new GridBagConstraints();
                GridBagConstraints textFieldConstraint = new GridBagConstraints();
        
                textFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
                textFieldConstraint.gridx = 0;
                textFieldConstraint.gridy = 0;
                importFilePanel.add(fileTextField, textFieldConstraint);
        
                attachButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                attachButtonConstraint.gridx = 1;
                attachButtonConstraint.gridy = 0;
                attachButtonConstraint.insets = new Insets(0, 5, 0, 0);
                importFilePanel.add(attachButton, attachButtonConstraint);
        
                JPanel importButtonPanel = new JPanel(new GridBagLayout());
                GridBagConstraints importButtonConstraint = new GridBagConstraints();
        
                importButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                importButtonConstraint.gridx = 0;
                importButtonConstraint.gridy = 0;
                importButtonPanel.add(importButton, importButtonConstraint);
        
                JPanel importPanel = new JPanel(new GridBagLayout());
                GridBagConstraints importPanelConstraint = new GridBagConstraints();
                GridBagConstraints buttonPanelConstraint = new GridBagConstraints();
        
                importPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                importPanelConstraint.gridx = 0;
                importPanelConstraint.gridy = 0;
                importPanel.add(importFilePanel, importPanelConstraint);
        
                buttonPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                buttonPanelConstraint.gridx = 0;
                buttonPanelConstraint.gridy = 1;
                buttonPanelConstraint.insets = new Insets(15, 50, 0, 0);
                importFilePanel.add(importButton, buttonPanelConstraint);
        
                importFrame.add(importPanel);
        
                importFrame.setLocationRelativeTo(null);
                importFrame.setVisible(true);  
        }
}