package io.github.csgroup.quizmaker.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Project;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 * Creates a frame that ensures the user is deleting the correct quiz bank
 * they selected 
 * 
 * @author Emily Palmer
 */
public class RemoveBankFrame 
{ 
        /**
         * Creates a frame that contains asks if the user is deleting the correct
         * quiz bank. If they confirm that they are deleting the correct quiz bank, 
         * it gets deleted.
         * 
         * @param name the name of the quiz bank to be removed
         * @param index the index of the quiz bank in the QuestionBank list 
         * @param bankProject used to remove the new question bank to the QuestionBank list
         * @param inputBankList list of quiz bank names from the BankPanel class
         */
        public void createRemoveBankFrame(String name, Project bankProject, int index, DefaultListModel<String> inputBankList)
        {            
                JFrame removeFrame = new JFrame();
                removeFrame.setSize(400, 290);
        
                JLabel questionLabel = new JLabel("Are you sure you want to remove quiz bank: " + name + "?");
        
                JButton yesButton = new JButton("Yes");
                JButton noButton = new JButton("No");
            
                // contains yesButton and noButton
                JPanel buttonPanel = new JPanel(new GridBagLayout());
                GridBagConstraints yesButtonConstraint = new GridBagConstraints();
                GridBagConstraints noButtonConstraint = new GridBagConstraints();
            
                // places yesButton on the left side of the panel
                yesButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                yesButtonConstraint.gridx = 0;
                yesButtonConstraint.gridy = 0;
                buttonPanel.add(yesButton, yesButtonConstraint);
        
                // places noButton on the right side of the panel
                noButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                noButtonConstraint.gridx = 1;
                noButtonConstraint.gridy = 0;
                buttonPanel.add(noButton, noButtonConstraint);
                
                // contains questionLabel and buttonPanel
                JPanel removeBankPanel = new JPanel(new GridBagLayout());
                GridBagConstraints questionConstraint = new GridBagConstraints();
                GridBagConstraints buttonConstraint = new GridBagConstraints();
                
                // places questionLabel at the top of the panel
                questionConstraint.fill = GridBagConstraints.HORIZONTAL;
                questionConstraint.gridx = 0;
                questionConstraint.gridy = 0;
                removeBankPanel.add(questionLabel, questionConstraint);
        
                // places buttonPanel at the bottom of the panel
                buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
                buttonConstraint.gridx = 0;
                buttonConstraint.gridy = 1;
                removeBankPanel.add(buttonPanel, buttonConstraint);    
                
                removeFrame.add(removeBankPanel);
                
                // listens for when yesButton is clicked
                yesButton.addActionListener((ActionEvent e) -> {
                    // close removeBankFrame
                    removeFrame.dispose();
                    List<QuestionBank> quizBankList = bankProject.getQuestionBanks();
                    // remove the quiz bank from the QuestionBank list
                    bankProject.removeBank(quizBankList.get(index));
                    // remove the quiz bank from the list on the screen
                    inputBankList.removeElement(name);
                });
                
                // listens for when noButton is clicked
                noButton.addActionListener((ActionEvent e) -> {
                    // close removeFrame
                    removeFrame.dispose();
                });
                
                // makes the JFrame appear in the center of the screen
                removeFrame.setLocationRelativeTo(null);
                // make the JFrame visible
                removeFrame.setVisible(true); 
        }
}
