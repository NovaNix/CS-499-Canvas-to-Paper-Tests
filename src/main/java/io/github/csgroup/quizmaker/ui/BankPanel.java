package io.github.csgroup.quizmaker.ui;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.QuestionBank;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListModel;

/**
 * Creates the JPanel that contains the JList of quiz bank names 
 * 
 * @author Emily Palmer
 */
public class BankPanel 
{       
        /**
         * Creates the quiz bank names JList and places them along with the 
         * add and remove bank buttons in a JPanel. The JPanel is organized 
         * using the GridBagLayout layout manager
         * 
         * @return the panel that holds the list of quiz bank names
         * and add and remove buttons
         */
        public JPanel createBankPanel()
        {
                // quiz bank project variable used to add banks to QuestionBank list
                Project bankProject = new Project();
            
                // label for the quiz question banks
                JLabel quizBankLabel = new JLabel("Quiz Banks");
                
                // list of the quiz bank names to be displayed on the JList
                ListModel<QuestionBank> quizBankNameList = bankProject.getBankModel();
                JList quizBankList = new JList(quizBankNameList);  
                //JScrollPane for the JList quizBankList
                JScrollPane bankScrollPane = new JScrollPane(quizBankList);
                bankScrollPane.setPreferredSize(new Dimension(190, 380));
                
                // contains quizBankLabel and bankScrollPane 
                JPanel bankPanel = new JPanel(new GridBagLayout());
                GridBagConstraints bankLabelConstraint = new GridBagConstraints();
                GridBagConstraints bankScrollPaneConstraint = new GridBagConstraints();
                
                // places quiBankLabel at the top of the panel
                bankLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
                bankLabelConstraint.gridx = 0;
                bankLabelConstraint.gridy = 0;
                bankPanel.add(quizBankLabel, bankLabelConstraint);
        
                // places bankScrollPanel at the bottom of the panel
                bankScrollPaneConstraint.fill = GridBagConstraints.HORIZONTAL;
                bankScrollPaneConstraint.gridx = 0;
                bankScrollPaneConstraint.gridy = 1;
                bankPanel.add(bankScrollPane, bankScrollPaneConstraint);
                
                // add and remove button panel
                BankButtonPanel addRemoveButtons = new BankButtonPanel();
                JPanel buttonPanel = addRemoveButtons.createBankButtonPanel();
               
                // contains bankPanel and buttonPanel
                JPanel bankButtonPanel = new JPanel(new GridBagLayout());
                GridBagConstraints quizPanelConstraint = new GridBagConstraints();
                GridBagConstraints buttonPanelConstraint = new GridBagConstraints();
        
                // places bankPanel at the top of bankButtonPanel
                quizPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                quizPanelConstraint.gridx = 0;
                quizPanelConstraint.gridy = 0;
                bankButtonPanel.add(bankPanel, quizPanelConstraint);
        
                // places buttonPanel at the bottom of bankButtonPanel
                buttonPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                buttonPanelConstraint.gridx = 0;
                buttonPanelConstraint.gridy = 1;
                bankButtonPanel.add(buttonPanel, buttonPanelConstraint);
                
                JButton addBankButton = addRemoveButtons.getAddBankButton();
                JButton removeBankButton = addRemoveButtons.getRemoveBankButton();
                                
                // listens for when addBankButton is clicked 
                addBankButton.addActionListener((ActionEvent e) -> {
                    // display the frame that prompts the user to enter a bank name
                    CreateBankDialogue addQuizBank = new CreateBankDialogue();
                    addQuizBank.createAddBankFrame(bankProject);
                });    
                
                // listens for when a quiz bank is selected
                quizBankList.addListSelectionListener((ListSelectionEvent e) -> {
                    // once a quiz bank has been selected enable removaBankButton
                    removeBankButton.setEnabled(true);
                });
                                                
                // listens for when removeBankButton is clicked
                removeBankButton.addActionListener((ActionEvent e) -> {
                    // the name of the selected quiz bank
                    QuestionBank removeBankName = (QuestionBank) quizBankList.getSelectedValue();
                    RemoveBankDialogue removeBank = new RemoveBankDialogue();
                    // display the frame that prompts the user if they are sure they 
                    // are deleting the correct quiz bank
                    removeBank.createRemoveBankFrame(removeBankName, bankProject);                    
                });                            
                return bankButtonPanel;
        }    
}
