package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;
import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;
import io.github.csgroup.quizmaker.ui.components.GeneratePanel;
import io.github.csgroup.quizmaker.word.TemplateReplacements;

import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

/**
 * Creates a frame that allows the user to enter in the generated quiz's information
 * 
 * @author Emily Palmer
 */
public class GenerateDialog
{
    private JFrame generateFrame;
    private final Quiz quiz;
    private final boolean isTemplateMode;
    private final TemplateReplacements replacements;
    private final QuizMetadata metadata;
    private GeneratePanel generatePanel;
    
    public GenerateDialog(Quiz currentQuiz)
    {
        this.quiz = currentQuiz;
        this.metadata = currentQuiz.getMetadata();
        this.replacements = null;
        this.isTemplateMode = false;
        generateDialog();
    }
    
    public GenerateDialog(TemplateReplacements replacements)
    {
        this.quiz = null;
        this.metadata = null;
        this.replacements = replacements;
        this.isTemplateMode = true;
        generateDialog();
    }
    
    /**
     * Organizes the GeneratePanel contents and generate button on the frame
     */
    private void generateDialog()
    {
        generateFrame = new JFrame("Generate Quiz");
        generateFrame.setSize(430, 355);
        
        // contains generatePanel and buttonPanel
        JPanel dialogPanel = new JPanel(new GridBagLayout());
        GridBagConstraints generateConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        
        // places generatePanel at the top of dialogPanel
        if(isTemplateMode)
        {
        	generatePanel = new GeneratePanel(205, replacements);
        } else
        {
        	generatePanel = new GeneratePanel(205, metadata);
        }
        generateConstraint.fill = GridBagConstraints.HORIZONTAL;
        generateConstraint.gridx = 0;
        generateConstraint.gridy = 0;
        generateConstraint.insets = new Insets(0, 0, 45, 80);
        dialogPanel.add(generatePanel, generateConstraint); 
        
        // places buttonPanel below dialogPanel
        JPanel buttonPanel = buttonPanel();
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 1;
        dialogPanel.add(buttonPanel, buttonConstraint); 
        
        generateFrame.add(dialogPanel);
    }
    
    /**
     * Creates the button and its action listener that allows the user to 
     * generate a quiz
     * 
     * @return the button panel
     */
    private JPanel buttonPanel()
    {
        JButton generateButton = new JButton("Generate");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateButton);
        
        // listens for when generateButton is selected
        generateButton.addActionListener((ActionEvent e) -> { 
            generatePanel.collectData();
            if(!isTemplateMode && quiz != null) 
            {
            	quiz.regenerate();
            }
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