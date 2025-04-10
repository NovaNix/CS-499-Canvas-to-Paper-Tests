package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.ui.QuizPanel;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;

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
 * Creates a frame that ensures the user is deleting the correct quiz 
 * they selected 
 * 
 * @author Emily Palmer
 */
public class RemoveQuizDialog 
{ 
    private JFrame removeFrame;
    private final Project project;
    private final JList quizList;
    private final Quiz quiz;
    private final JPanel test;
    
    public RemoveQuizDialog(Quiz currentQuiz, Project currentProject, JList list, JPanel panel)
    {
        quizList = list;
        project = currentProject;
        quiz = currentQuiz;
        test = panel;
        createRemoveBankFrame();
    }
          
    /**
     * Creates a frame that contains asks if the user is deleting the correct
     * quiz bank. If they confirm that they are deleting the correct quiz bank, 
     * it gets deleted.
     * 
     * @param name the name of the quiz bank to be removed
     * @param bankProject used to remove the new question bank to the QuestionBank list
     */
    private void createRemoveBankFrame()
    {            
        removeFrame = new JFrame();
        removeFrame.setSize(380, 270);
        
        JLabel questionLabel = new JLabel("Are you sure you want to remove the quiz: " + quiz + "?");
                            
        // contains questionLabel and buttonPanel
        JPanel removeBankPanel = new JPanel(new GridBagLayout());
        GridBagConstraints questionConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
                
        // places questionLabel at the top of removeBankPanel
        questionConstraint.fill = GridBagConstraints.HORIZONTAL;
        questionConstraint.gridx = 0;
        questionConstraint.gridy = 0;
        questionConstraint.insets = new Insets(0, 0, 5, 0);
        removeBankPanel.add(questionLabel, questionConstraint);
        
        // places buttonPanel at the bottom of removeBankPanel
        JPanel buttonPanel = removeButtonPanel();
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
    private JPanel removeButtonPanel()
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
            int index = quizList.getSelectedIndex();
            // remove the bank from the list            
            project.removeQuiz(quiz);
            
            int quizCount = project.getQuizCount();                   
            if (index == quizCount)
            {
                quizList.setSelectedIndex(index - 1);
            }
            else if (index == 0)
            {
                quizList.setSelectedIndex(0);
            }
            else
            {
                quizList.setSelectedIndex(index);
            }
            // close removeFrame
            if (quizCount == 0)
            {
                test.setVisible(false);
            }
            
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