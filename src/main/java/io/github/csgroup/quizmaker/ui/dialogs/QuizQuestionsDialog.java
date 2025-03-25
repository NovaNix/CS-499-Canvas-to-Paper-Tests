package io.github.csgroup.quizmaker.ui.dialogs;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JScrollPane;


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
        questionsFrame.setSize(430, 430);
        
        JPanel questionDialogPanel = new JPanel(new GridBagLayout());
        GridBagConstraints questionPanelConstraint = new GridBagConstraints();
        GridBagConstraints answerConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
          
        JPanel questionPanel = quizQuestionPanel();
        questionPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        questionPanelConstraint.gridx = 0;
        questionPanelConstraint.gridy = 0;
        questionDialogPanel.add(questionPanel, questionPanelConstraint);
        
        JPanel answerPanel = multipleChoiceAnswerPanel();
        answerConstraint.fill = GridBagConstraints.HORIZONTAL;
        answerConstraint.gridx = 0;
        answerConstraint.gridy = 1;
        answerConstraint.insets = new Insets(0, 0, 0, 185);
        questionDialogPanel.add(answerPanel, answerConstraint); 
        
        JPanel buttonPanel = buttonPanel();
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 2;
        buttonConstraint.insets = new Insets(0, 0, 0, 170);
        questionDialogPanel.add(buttonPanel, buttonConstraint); 
        
        questionsFrame.add(questionDialogPanel);
    }
            
    private JPanel quizQuestionPanel()
    {        
        JTextField questionTitle = new JTextField("Question");
        questionTitle.setPreferredSize(new Dimension(170, 25));
        JPanel titlePanel = new JPanel();
        titlePanel.add(questionTitle);
        
        String [] questionTypes = {"Multiple Choice", "Fill in the Blank", "Short Answer", "True or False", "Matching"};
        JComboBox questionTypesList = new JComboBox(questionTypes);
        
        JTextArea quizQuestionArea = new JTextArea();
        quizQuestionArea.setLineWrap(true);
        quizQuestionArea.setWrapStyleWord(true);
        JScrollPane questionScrollPane = new JScrollPane(quizQuestionArea);
        questionScrollPane.setPreferredSize(new Dimension(360, 130));
        JPanel test = new JPanel();
        test.add(questionScrollPane);
        
        JLabel answersLabel = new JLabel("Answers");
        
        JPanel quizInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints titleConstraint = new GridBagConstraints();
        GridBagConstraints typeConstraint = new GridBagConstraints();
        GridBagConstraints labelConstraint = new GridBagConstraints();
        
        titleConstraint.fill = GridBagConstraints.HORIZONTAL;
        titleConstraint.gridx = 0;
        titleConstraint.gridy = 0;
        titleConstraint.insets = new Insets(0, 3, 0, 67);
        quizInfoPanel.add(titlePanel, titleConstraint);
        
        typeConstraint.fill = GridBagConstraints.HORIZONTAL;
        typeConstraint.gridx = 1;
        typeConstraint.gridy = 0;
        quizInfoPanel.add(questionTypesList, typeConstraint);
        
        JPanel quizQuestionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints infoConstraint = new GridBagConstraints();
        GridBagConstraints questionConstraint = new GridBagConstraints();
        
        infoConstraint.fill = GridBagConstraints.HORIZONTAL;
        infoConstraint.gridx = 0;
        infoConstraint.gridy = 0;
        infoConstraint.insets = new Insets(0, 0, 0, 30);
        quizQuestionPanel.add(quizInfoPanel, infoConstraint);
        
        questionConstraint.fill = GridBagConstraints.HORIZONTAL;
        questionConstraint.gridx = 0;
        questionConstraint.gridy = 1;
        questionConstraint.insets = new Insets(0, 0, 5, 20);
        quizQuestionPanel.add(test, questionConstraint);
        
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 2;
        labelConstraint.insets = new Insets(0, 32, 0, 0);
        quizQuestionPanel.add(answersLabel, labelConstraint);
        
        return quizQuestionPanel;
    }
    
    private JPanel multipleChoiceAnswerPanel()
    {
        JTextField answerTextArea = new JTextField();
        answerTextArea.setPreferredSize(new Dimension(170, 25));
        JPanel answerTextPanel = new JPanel();
        answerTextPanel.add(answerTextArea);
        
        JTextField answerTextArea2 = new JTextField();
        answerTextArea2.setPreferredSize(new Dimension(170, 25));
        JPanel answerTextPanel2 = new JPanel();
        answerTextPanel2.add(answerTextArea2);
        
        JTextField answerTextArea3 = new JTextField();
        answerTextArea3.setPreferredSize(new Dimension(170, 25));
        JPanel answerTextPanel3 = new JPanel();
        answerTextPanel3.add(answerTextArea3);
        
        JTextField answerTextArea4 = new JTextField();
        answerTextArea4.setPreferredSize(new Dimension(170, 25));
        JPanel answerTextPanel4 = new JPanel();
        answerTextPanel4.add(answerTextArea4);
        
        JCheckBox correctAnswer = new JCheckBox();
        JCheckBox correctAnswer2 = new JCheckBox();
        JCheckBox correctAnswer3 = new JCheckBox();
        JCheckBox correctAnswer4 = new JCheckBox();
        
        JPanel answerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints answerConstraint = new GridBagConstraints();
        GridBagConstraints checkConstraint = new GridBagConstraints();
        
        GridBagConstraints answerConstraint2 = new GridBagConstraints();
        GridBagConstraints checkConstraint2 = new GridBagConstraints();
        
        GridBagConstraints answerConstraint3 = new GridBagConstraints();
        GridBagConstraints checkConstraint3 = new GridBagConstraints();
        
        GridBagConstraints answerConstraint4 = new GridBagConstraints();
        GridBagConstraints checkConstraint4 = new GridBagConstraints();
        
        checkConstraint.fill = GridBagConstraints.HORIZONTAL;
        checkConstraint.gridx = 0;
        checkConstraint.gridy = 0;
        answerPanel.add(correctAnswer, checkConstraint);
        
        answerConstraint.fill = GridBagConstraints.HORIZONTAL;
        answerConstraint.gridx = 1;
        answerConstraint.gridy = 0;
        answerPanel.add(answerTextPanel, answerConstraint);
               
        checkConstraint2.fill = GridBagConstraints.HORIZONTAL;
        checkConstraint2.gridx = 0;
        checkConstraint2.gridy = 1;
        answerPanel.add(correctAnswer2, checkConstraint2);
        
        answerConstraint2.fill = GridBagConstraints.HORIZONTAL;
        answerConstraint2.gridx = 1;
        answerConstraint2.gridy = 1;
        answerPanel.add(answerTextPanel2, answerConstraint2);
               
        checkConstraint3.fill = GridBagConstraints.HORIZONTAL;
        checkConstraint3.gridx = 0;
        checkConstraint3.gridy = 2;
        answerPanel.add(correctAnswer3, checkConstraint3);
        
        answerConstraint3.fill = GridBagConstraints.HORIZONTAL;
        answerConstraint3.gridx = 1;
        answerConstraint3.gridy = 2;
        answerPanel.add(answerTextPanel3, answerConstraint3);
               
        checkConstraint4.fill = GridBagConstraints.HORIZONTAL;
        checkConstraint4.gridx = 0;
        checkConstraint4.gridy = 3;
        answerPanel.add(correctAnswer4, checkConstraint4);
        
        answerConstraint4.fill = GridBagConstraints.HORIZONTAL;
        answerConstraint4.gridx = 1;
        answerConstraint4.gridy = 3;
        answerPanel.add(answerTextPanel4, answerConstraint4);
        
        return answerPanel;
    }
    
    public JPanel buttonPanel()
    {
        JButton addButton = new JButton("+");
        JButton removeButton = new JButton("-");
        
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints addConstraint = new GridBagConstraints();
        GridBagConstraints removeConstraint = new GridBagConstraints();
        
        addConstraint.fill = GridBagConstraints.HORIZONTAL;
        addConstraint.gridx = 0;
        addConstraint.gridy = 0;
        addConstraint.insets = new Insets(0, 0, 0, 2);
        buttonPanel.add(addButton, addConstraint);
        
        removeConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeConstraint.gridx = 1;
        removeConstraint.gridy = 0;
        buttonPanel.add(removeButton, removeConstraint);
        
        return buttonPanel;
    }
       
    public void show()
    {
        questionsFrame.setLocationRelativeTo(null);
        questionsFrame.setVisible(true);
    }
}
