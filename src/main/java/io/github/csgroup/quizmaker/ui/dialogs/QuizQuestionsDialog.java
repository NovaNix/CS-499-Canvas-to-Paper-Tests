package io.github.csgroup.quizmaker.ui.dialogs;

import javax.swing.JFrame;


/**
 *
 * @author Emily Palmer
 */
public class QuizQuestionsDialog 
{
    private JFrame questionsFrame;
    
    public QuizQuestionsDialog()
    {
        questionDialog();
    }
    
    private void questionDialog()
    {
        questionsFrame = new JFrame("Quiz Questions");
        questionsFrame.setSize(200, 200);
    }
    
    public void show()
    {
        questionsFrame.setVisible(true);
    }
}
