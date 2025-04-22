package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Project;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import javax.swing.JList;

/**
 * Creates a frame that ensures the user is deleting the correct question bank
 * they selected 
 * 
 * @author Emily Palmer
 */
public class RemoveBankDialog 
{ 
    private JFrame removeFrame;
    private final Project project;
    private final JList bankList;
    private final JPanel tablePanel;
    
    public RemoveBankDialog(QuestionBank bankName, Project currentProject, JList list, JPanel panel)
    {
        bankList = list;
        project = currentProject;
        tablePanel = panel;
        createRemoveBankFrame(bankName);
    }
          
    /**
     * Creates a frame that contains asks if the user is deleting the correct
     * quiz bank. If they confirm that they are deleting the correct quiz bank, 
     * it gets deleted.
     * 
     * @param name the name of the quiz bank to be removed
     * @param bankProject used to remove the new question bank to the QuestionBank list
     */
    private void createRemoveBankFrame(QuestionBank name)
    {            
        removeFrame = new JFrame();
        removeFrame.setSize(400, 240);
        
        JLabel questionLabel = new JLabel("Are you sure you want to remove question bank: " + name + "?");
                            
        // contains questionLabel and buttonPanel
        JPanel removeBankPanel = new JPanel(new GridBagLayout());
        GridBagConstraints questionConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
                
        // places questionLabel at the top of removeBankPanel
        questionConstraint.fill = GridBagConstraints.HORIZONTAL;
        questionConstraint.gridx = 0;
        questionConstraint.gridy = 0;
        questionConstraint.insets = new Insets(0, 0, 10, 0);
        removeBankPanel.add(questionLabel, questionConstraint);
        
        // places buttonPanel at the bottom of removeBankPanel
        JPanel buttonPanel = removeButtonPanel(name);
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 1;
        removeBankPanel.add(buttonPanel, buttonConstraint);    
                
        removeFrame.add(removeBankPanel);                
    }
    
    /**
     * Creates button panel that allows to user to confirm or deny to delete 
     * the bank
     * 
     * @param questionBankName name of the question bank
     * @return the button panel
     */
    private JPanel removeButtonPanel(QuestionBank questionBankName)
    {
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        
        // contains yesButton and noButton
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints yesButtonConstraint = new GridBagConstraints();
        GridBagConstraints noButtonConstraint = new GridBagConstraints();
            
        // places yesButton on the left side buttonPanel
        yesButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        yesButtonConstraint.gridx = 0;
        yesButtonConstraint.gridy = 0;
        yesButtonConstraint.insets = new Insets(0, 0, 0, 5);
        buttonPanel.add(yesButton, yesButtonConstraint);
        
        // places noButton on the right side of buttonPanel
        noButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        noButtonConstraint.gridx = 1;
        noButtonConstraint.gridy = 0;
        buttonPanel.add(noButton, noButtonConstraint);
        
        // listens for when yesButton is clicked
        yesButton.addActionListener((ActionEvent e) -> {
            int index = bankList.getSelectedIndex();
            // remove the bank from the list            
            project.removeBank(questionBankName);
            
            int bankCount = project.getBankCount();  
            if (index == bankCount)
            {
                bankList.setSelectedIndex(index - 1);
            }         
            else
            {
                bankList.setSelectedIndex(index);
            }
            
            if (bankCount == 0)
            {
                tablePanel.setVisible(false);
            }
            
            // close removeFrame
            removeFrame.dispose();          
        });
                
        // listens for when noButton is clicked
        noButton.addActionListener((ActionEvent e) -> {
            // close removeFrame
            removeFrame.dispose();
        });
        
        return buttonPanel;
    }
             
    /**
     * Controls when and where the frame appears
     * 
     */
    public void show()
    {
        // makes the JFrame appear in the center of the screen
        removeFrame.setLocationRelativeTo(null);
        // make the JFrame visible
        removeFrame.setVisible(true);            
    }
}
