package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;
import io.github.csgroup.quizmaker.ui.components.GeneratePanel;

import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

/**
 * 
 * 
 * @author Emily Palmer
 */
public class GenerateDialog
{
    private JFrame generateFrame;
    private final Quiz quiz;
    
    public GenerateDialog(Quiz currentQuiz)
    {
        quiz = currentQuiz;
        generateDialog();
    }
    
    private void generateDialog()
    {
        generateFrame = new JFrame("Generate Quiz");
        generateFrame.setSize(430, 380);
        
        JPanel dialogPanel = new JPanel(new GridBagLayout());
        GridBagConstraints generateConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        
        GeneratePanel generatePanel = new GeneratePanel(205);
        generateConstraint.fill = GridBagConstraints.HORIZONTAL;
        generateConstraint.gridx = 0;
        generateConstraint.gridy = 0;
        generateConstraint.insets = new Insets(0, 0, 35, 80);
        dialogPanel.add(generatePanel, generateConstraint); 
        
        JPanel buttonPanel = buttonPanel();
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 1;
        dialogPanel.add(buttonPanel, buttonConstraint); 
        
        generateFrame.add(dialogPanel);
    }
    
    private JPanel buttonPanel()
    {
        JButton generateButton = new JButton("Generate");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateButton);
        
        generateButton.addActionListener((ActionEvent e) -> { 
            GeneratedQuiz generatedQuiz = new GeneratedQuiz(quiz);
            generateFrame.dispose();
        });
               
        return buttonPanel;
    }
   
    /**
     * Controls when and where the frame appears 
     */
    public void show()
    {
        // makes the JFrame appear in the center of the screen
        generateFrame.setLocationRelativeTo(null);
        // makes the JFrame visible
        generateFrame.setVisible(true);
    }
}