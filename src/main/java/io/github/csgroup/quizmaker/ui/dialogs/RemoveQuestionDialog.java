package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 * Creates a frame that prompts the user to delete a bank/quiz question
 *
 * @author Emily Palmer
 */
public class RemoveQuestionDialog 
{
    private JFrame removeFrame;
    private Quiz quiz;
    private QuestionBank bank;
    private final Question question;
    private final DefaultTableModel tableModel;
    
    public RemoveQuestionDialog(Quiz currentQuiz, Question selectedQuestion, DefaultTableModel model)
    {
        quiz = currentQuiz;
        question = selectedQuestion;
        tableModel = model;
        removeBankFrame();
    }
    
    public RemoveQuestionDialog(QuestionBank currentBank, Question selectedQuestion, DefaultTableModel model)
    {
        bank = currentBank;
        question = selectedQuestion;
        tableModel = model;
        removeBankFrame();
    }
    
    /**
     * Creates the frame that contains the label that prompts the user to delete 
     * a question and the button panel for the user's response
     */
    private void removeBankFrame()
    {
        removeFrame = new JFrame();
        removeFrame.setSize(300, 200);
        
        JLabel removeLabel = new JLabel("Delete selected question?");
        
        // contains removeLabel and buttonPanel
        JPanel removePanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        
        // places removeLabel at the top of removePanel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(0, 0, 10, 0);
        removePanel.add(removeLabel, labelConstraint);
        
        // places buttonPanel below removeLabel
        JPanel buttonPanel = buttonPanel();
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 1;
        removePanel.add(buttonPanel, buttonConstraint);
        
        removeFrame.add(removePanel);       
    }
    
    /**
     * Creates the button panel and their action listener
     * 
     * @return button panel
     */
    private JPanel buttonPanel()
    {
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        
        // contains yesButton and noButton
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints yesButtonConstraint = new GridBagConstraints();
        GridBagConstraints noButtonConstraint = new GridBagConstraints();
        
        // places yesButton on the left side of buttonPanel
        yesButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        yesButtonConstraint.gridx = 0;
        yesButtonConstraint.gridy = 0;
        yesButtonConstraint.insets = new Insets(0, 0, 0, 1);
        buttonPanel.add(yesButton, yesButtonConstraint);
        
        // places noButton on the right side of buttonPanel
        noButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        noButtonConstraint.gridx = 1;
        noButtonConstraint.gridy = 0;
        noButtonConstraint.insets = new Insets(0, 1, 0, 0);
        buttonPanel.add(noButton, noButtonConstraint);
        
        // listens for when yesButton is clicked
        yesButton.addActionListener((ActionEvent e) -> { 
            // delete the question bank question and its row in the table
            if (quiz == null)
            {
                int row = bank.getQuestionIndex(question);
                tableModel.removeRow(row);
                tableModel.addRow(new Object[]{null,null,null,null}); 
                bank.remove(question);
            }
            // delete the quiz question and its row in the table
            if (bank == null)
            {
                int row = quiz.getQuestionIndex(question);
                tableModel.removeRow(row);
                tableModel.addRow(new Object[]{null,null,null,null});
                quiz.removeQuestion(question);
            }
            
            removeFrame.dispose();
        });
        
        // listens for when noButton is clicked
        noButton.addActionListener((ActionEvent e) -> {
            // close the frame
            removeFrame.dispose();          
        });
        
        return buttonPanel;
    }   
    
    /**
     * Controls when and where the frame appears 
     */
    public void show()
    {
        // makes the JFrame appear in the center of the screen
        removeFrame.setLocationRelativeTo(null);
        // makes the JFrame visible
        removeFrame.setVisible(true);            
    } 
}
