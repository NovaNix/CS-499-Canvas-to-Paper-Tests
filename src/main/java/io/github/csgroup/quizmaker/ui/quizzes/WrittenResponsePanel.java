package io.github.csgroup.quizmaker.ui.quizzes;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;

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
    private JButton addQuestionButton;
    private boolean edit;
    private JCheckBox abetCheckBox;
    private int editedRow;
    private Question editQuestion;
    private QuestionBank questionBank;
    
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
     * Determines if a question is being edited
     * 
     * @param result determines where or not the question is being edited
     * @param row the row where the question is located 
     */
    public void isEditable(boolean result, int row)
    {
        edit = result;
        editedRow = row;
        if (edit == true)
        {
            // populate the dialog with the question's information
            editQuestion(editedRow);
        }
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
        questionScrollPane.setPreferredSize(new Dimension(425, 150));
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
        answerConstraint.insets = new Insets(0, 0, 15, 0);
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
        abetCheckBox = new JCheckBox("ABET Question");
        
        // contains answerLabel and abetCheckBox
        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints checkBoxConstraint = new GridBagConstraints();
        
        // places answerLabel at the top of labelsPanel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(0, 3, 0, 100);
        labelsPanel.add(answerLabel, labelConstraint); 
        
        // places abetCheckBox on the right side of answerLabel
        checkBoxConstraint.fill = GridBagConstraints.HORIZONTAL;
        checkBoxConstraint.gridx = 1;
        checkBoxConstraint.gridy = 0;
        checkBoxConstraint.insets = new Insets(0, 180, 0, 0);
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
            // get the title of the question
            String questionLabel = questionTitle.getText();
 
            try
            {  
                // the point value of the question
                String pointsString = pointsValue.getText();
                float floatPoints = Float.parseFloat(pointsString);
                
                // add a new quiz question
                if ((newQuiz != null) && (edit == false))
                {
                    WrittenResponseQuestion wrQuestion = new WrittenResponseQuestion(questionLabel, floatPoints); 
                    wrQuestion.setLabel(new Label(questionString));
                    wrQuestion.setAnswer(answerString);
                    
                    newQuiz.addQuestion(wrQuestion);
                    int index = newQuiz.getQuestionIndex(wrQuestion);
                    Question newQuestion = newQuiz.getQuestion(index);
                    // if the abetCheckBox is selected mark the question as an abet question
                    if (abetCheckBox.isSelected() == true)
                    {
                        newQuestion.setAbet(true);
                    }            
                    else
                    {
                        newQuestion.setAbet(false);
                    }
                    
                }
                // update a quiz question
                if (newQuiz != null && (edit == true))
                {
                    updateQuizQuestion(questionLabel, floatPoints, questionString, answerString);
                }
                // add a question bank question
                if ((questionBank != null) && (edit == false))
                {
                    WrittenResponseQuestion wrQuestion = new WrittenResponseQuestion(questionLabel); 
                    wrQuestion.setLabel(new Label(questionString));
                    wrQuestion.setAnswer(answerString);
                    
                    questionBank.add(wrQuestion);
                    int index = questionBank.getQuestionIndex(wrQuestion);
                    Question newQuestion = questionBank.getQuestion(index);
                    if (abetCheckBox.isSelected() == true)
                    {
                        newQuestion.setAbet(true);
                    }            
                    else
                    {
                        newQuestion.setAbet(false);
                    }
                }
                // update a question bank question
                if ((questionBank != null) && (edit == true))
                {
                    updateBankQuestion(questionLabel, questionString, answerString);
                }
                
                populateTable();
                mainFrame.dispose();
            }
            catch (NumberFormatException n) {}
        });
        
        return buttonPanel;
    }
    
    /**
     * Populates the dialog with the question's title, point value, question, and
     * abet check box
     * 
     * @param index the index of the question being edited
     */
    private void editQuestion(int index)
    {  
        // populate the quiz's question information
        if (newQuiz != null)
        {
            editQuestion = newQuiz.getQuestion(index);
            
            questionTitle.setText(editQuestion.getTitle());           
            pointsValue.setText(Float.toString(editQuestion.getPoints()));
            
            if (editQuestion.isAbet() == true)
            {
                abetCheckBox.setSelected(true);
            }
            else
            {
                abetCheckBox.setSelected(false);
            }
            
            Label questionLabel = editQuestion.getLabel();
            question.setText(questionLabel.asText());
            
            String answer = editQuestion.getAnswerString();
            answerArea.setText(answer);
        }
        // populate the quiz's question information
        if (questionBank != null)
        {
            editQuestion = questionBank.getQuestion(index);            
            questionTitle.setText(editQuestion.getTitle());           
            
            if (editQuestion.isAbet() == true)
            {
                abetCheckBox.setSelected(true);
            }
            else
            {
                abetCheckBox.setSelected(false);
            }
            
            Label questionLabel = editQuestion.getLabel();
            question.setText(questionLabel.asText());
            
            String answer = editQuestion.getAnswerString();
            answerArea.setText(answer);
        }        
    }
        
    /**
     * Ensures that the question title, points value, and question description 
     * have been entered before addQuestionButton is enabled
     */
    private void componentListeners()
    {       
        Document titleDocument = questionTitle.getDocument();
        titleDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                boolean points = (pointsValue.getText()).isEmpty();
                boolean wrQuestion = (question.getText()).isEmpty();
                
                if (newQuiz != null)
                {
                    if ((points == false) && (wrQuestion == false))
                    {
                        addQuestionButton.setEnabled(true);
                    }
                }
                if (questionBank != null)
                {
                    if (wrQuestion == false)
                    {
                        addQuestionButton.setEnabled(true);
                    }
                }
            }
            
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String text = questionTitle.getText();
                boolean empty = text.isEmpty();
                // disable the button if the title field is empty
                if (empty == true)
                {
                    addQuestionButton.setEnabled(false);
                }                
            }          
            @Override
            public void changedUpdate(DocumentEvent e) {}                 
        });
        
        if (newQuiz != null)
        {
            Document pointsDocument = pointsValue.getDocument();
            pointsDocument.addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) 
                {
                    try
                    {
                        String text = pointsValue.getText();
                        Float.valueOf(text);
                        boolean title = (questionTitle.getText()).isEmpty();
                        boolean wrQuestion = (question.getText()).isEmpty();
                        if ((title == false) && (wrQuestion == false))
                        {
                            addQuestionButton.setEnabled(true);
                        }
                    }
                    catch (NumberFormatException n) {}
                }
            
                @Override
                public void removeUpdate(DocumentEvent e)
                {
                    String text = pointsValue.getText();
                    boolean empty = text.isEmpty();
                    // disable the button if the title field is empty
                    if (empty == true)
                    {
                        addQuestionButton.setEnabled(false);
                    }                
                }          
                @Override
                public void changedUpdate(DocumentEvent e) {}                 
            });
        }
        
        Document questionDocument = question.getDocument();
        questionDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                boolean title = (questionTitle.getText()).isEmpty();
                boolean points = (pointsValue.getText()).isEmpty();
                
                if (newQuiz != null)
                {
                    if ((title == false) && (points == false))
                    {
                        addQuestionButton.setEnabled(true);
                    }
                }
                if (questionBank != null)
                {
                    if (title == false)
                    {
                        addQuestionButton.setEnabled(true);
                    }
                }
            }
            
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String text = question.getText();
                boolean empty = text.isEmpty();
                // disable the button if the title field is empty
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
     * Updates a quiz question
     * 
     * @param updateTitle the title of the quiz
     * @param updatePoints the point amount for the quiz question
     * @param updateQuestion the question
     * @param updateAnswer the answer 
     */
    private void updateQuizQuestion(String updateTitle, float updatePoints, String updateQuestion, String updateAnswer)
    {
        WrittenResponseQuestion newQuestion = (WrittenResponseQuestion) editQuestion;        
        newQuestion.setPoints(updatePoints);
        newQuestion.setTitle(updateTitle);
        newQuestion.setLabel(new Label(updateQuestion));
        
        newQuestion.setLabel(new Label(updateQuestion));
        newQuestion.setTitle(updateTitle);
        newQuestion.setAnswer(updateAnswer);
        
        if (abetCheckBox.isSelected() == true)
        {
            newQuestion.setAbet(true);
        }            
        else
        {
            newQuestion.setAbet(false);
        }

        questionTable.setValue(newQuestion.getAnswerString(), editedRow, 1);
    } 
    
    /**
     * Updates a bank question
     * 
     * @param updateTitle the title of the question
     * @param updateQuestion the question 
     * @param updateAnswer the answer
     */
    private void updateBankQuestion(String updateTitle, String updateQuestion, String updateAnswer)
    {
        WrittenResponseQuestion newQuestion = (WrittenResponseQuestion) editQuestion;        
        newQuestion.setTitle(updateTitle);
        newQuestion.setLabel(new Label(updateQuestion));
        
        newQuestion.setLabel(new Label(updateQuestion));
        newQuestion.setTitle(updateTitle);
        newQuestion.setAnswer(updateAnswer);
        if (abetCheckBox.isSelected() == true)
        {
            newQuestion.setAbet(true);
        }            
        else
        {
            newQuestion.setAbet(false);
        }

        questionTable.setValue(newQuestion.getAnswerString(), editedRow, 1);
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