package io.github.csgroup.quizmaker.ui.components;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * Creates the panel that allows user to create the written response type
 * answers. After the answers have been entered, they are added to the quiz or 
 * question bank
 * 
 * @author Emily Palmer
 */
public class WrittenResponsePanel extends JComponent
{
    private final JFrame mainFrame;
    private JTextArea answerArea;
    private final JTextArea question;
    private final JTextField pointsValue;
    private Quiz newQuiz;
    private final QuestionTable questionTable;
    private final JTextField questionTitle;
    private QuestionBank questionBank;
    private JButton addQuestionButton;
    
    public WrittenResponsePanel(JFrame frame, JTextArea wrQuestion, JTextField points, Quiz quiz, QuestionTable table, JTextField title)
    {
        mainFrame = frame;
        question = wrQuestion;
        pointsValue = points;
        newQuiz = quiz;
        questionTable = table;
        questionTitle = title;
        writtenResponsePanel();
    }
    
    public WrittenResponsePanel(JFrame frame, JTextArea wrQuestion, JTextField points, QuestionBank bank, QuestionTable table, JTextField title)
    {
        mainFrame = frame;
        question = wrQuestion;
        pointsValue = points;
        questionBank = bank;
        questionTable = table;
        questionTitle = title;
        writtenResponsePanel();          
    }
    
    /**
     * Places the labelsPanel, answersPane, and the button pane on the component 
     * together 
     */
    private void writtenResponsePanel()
    {        
        answerArea = new JTextArea();
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        JScrollPane questionScrollPane = new JScrollPane(answerArea);
        questionScrollPane.setPreferredSize(new Dimension(380, 130));
        JPanel answerPane = new JPanel();
        answerPane.add(questionScrollPane);
        
        // contains labelsPanel, answerPane, and buttonPanel
        this.setLayout(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints answerConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        
        // places labels panel at the top of the component
        JPanel labelsPanel = labelsPanel();
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        this.add(labelsPanel, labelConstraint);   
        
        // places answersPane below labelsPanel
        answerConstraint.fill = GridBagConstraints.HORIZONTAL;
        answerConstraint.gridx = 0;
        answerConstraint.gridy = 1;
        answerConstraint.insets = new Insets(0, 0, 35, 0);
        this.add(answerPane, answerConstraint); 
        
        // places buttonPanel below answersPane
        JPanel buttonPanel = addButtonPanel();
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 2;
        this.add(buttonPanel, buttonConstraint); 
    }
    
    /**
     * Creates the "Answer" label and the ABET checkbox 
     * 
     * @return panel containing the label and checkbox
     */
    private JPanel labelsPanel()
    {
        JLabel answerLabel = new JLabel("Answer");
        JCheckBox abetCheckBox = new JCheckBox("ABET Question");
        
        // contains answerLabel and abetCheckBox
        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints checkBoxConstraint = new GridBagConstraints();
        
        // places answerLabel at the top of labelsPanel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(0, 3, 0, 95);
        labelsPanel.add(answerLabel, labelConstraint); 
        
        // places abetCheckBox on the right side of answerLabel
        checkBoxConstraint.fill = GridBagConstraints.HORIZONTAL;
        checkBoxConstraint.gridx = 1;
        checkBoxConstraint.gridy = 0;
        checkBoxConstraint.insets = new Insets(0, 130, 0, 0);
        labelsPanel.add(abetCheckBox, checkBoxConstraint); 
                
        return labelsPanel;
    }
    
    /**
     * Creates the "Add" button that allows the user to add the question to the 
     * quiz/question bank
     * 
     * @return add button panel
     */
    private JPanel addButtonPanel()
    {
        addQuestionButton = new JButton("Add");
        addQuestionButton.setEnabled(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addQuestionButton);
        
        componentListeners();
        
        addQuestionButton.addActionListener((ActionEvent e) -> { 
            // the question the user entered
            String questionString = question.getText();
            // the answer the user entered
            String answerString = answerArea.getText();
            // the point value of the question
            String pointsString = pointsValue.getText();
            // get the title of the question
            String questionLabel = questionTitle.getText();
            
            try
            {
                float floatPoints = Float.parseFloat(pointsString);
                WrittenResponseQuestion wrQuestion = new WrittenResponseQuestion(questionLabel, floatPoints); 
                wrQuestion.setLabel(new Label(questionString));
                wrQuestion.setAnswer(answerString);
                
                if (newQuiz != null)
                {
                    newQuiz.addQuestion(wrQuestion);
                }
                if (questionBank != null)
                {
                    questionBank.add(wrQuestion);
                }
                
                populateTable();
                mainFrame.dispose();
            }
             catch (NumberFormatException n) {}
        });
        
        return buttonPanel;
    }
    
    private void componentListeners()
    {       
        Document titleDocument = questionTitle.getDocument();
        titleDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                boolean points = (pointsValue.getText()).isEmpty();
                boolean fitbQuestion = (question.getText()).isEmpty();
                if ((points == false) && (fitbQuestion == false))
                {
                    addQuestionButton.setEnabled(true);
                }
            }
            
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String text = questionTitle.getText();
                boolean empty = text.isEmpty();
                
                if (empty == true)
                {
                    addQuestionButton.setEnabled(false);
                }                
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
            }                 
        });
        
        Document pointsDocument = pointsValue.getDocument();
        pointsDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                boolean title = (questionTitle.getText()).isEmpty();
                boolean fitbQuestion = (question.getText()).isEmpty();
                if ((title == false) && (fitbQuestion == false))
                {
                    addQuestionButton.setEnabled(true);
                }
            }
            
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String text = pointsValue.getText();
                boolean empty = text.isEmpty();
                
                if (empty == true)
                {
                    addQuestionButton.setEnabled(false);
                }                
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {}                 
        });
        
        Document questionDocument = question.getDocument();
        questionDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                boolean title = (questionTitle.getText()).isEmpty();
                boolean points = (pointsValue.getText()).isEmpty();
                if ((title == false) && (points == false))
                {
                    addQuestionButton.setEnabled(true);
                }
            }
            
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String text = question.getText();
                boolean empty = text.isEmpty();
                
                if (empty == true)
                {
                    addQuestionButton.setEnabled(false);
                }                
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {}                 
        });
    }
    
    /**
     * Adds the question to the table 
     */
    private void populateTable()
    {
        if (newQuiz != null)
        {
            int size = (newQuiz.getQuestionCount()) - 1;
            Question displayQuestion = newQuiz.getQuestion(size); 
            String displayAnswer = displayQuestion.getAnswerString();
            
            questionTable.setValue(displayQuestion, size, 0);
            questionTable.setValue(displayAnswer, size, 1);
        }
        
        if (questionBank != null)
        {
            int size = (questionBank.getQuestionCount()) - 1;
            Question displayQuestion = questionBank.getQuestion(size); 
            String displayAnswer = displayQuestion.getAnswerString();
            
            questionTable.setValue(displayQuestion, size, 0);
            questionTable.setValue(displayAnswer, size, 1);
        }
    }
}