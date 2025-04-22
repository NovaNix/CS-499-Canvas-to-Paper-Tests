package io.github.csgroup.quizmaker.ui.quizzes;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import java.awt.CardLayout;
import javax.swing.JTextArea;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
    
/**
 * Creates the panel that allows user to create the fill in the blank question type
 * answers. After the answers have been entered, they are added to the quiz or 
 * question bank
 * 
 * @author Emily Palmer
 */
public class FillInTheBlankPanel extends JComponent
{
    private final JFrame mainFrame;
    private JTextField answerFields[];
    private JPanel answerPanels[];
    private JPanel rowPanels[];
    private GridBagConstraints textFieldConstraints[];
    private GridBagConstraints rowConstraints[];
    private JPanel cardPanels[];
    private JPanel containerPanels[];
    private JPanel buttonPanels[];
    private JPanel answerButtonPanel;
    private final JTextArea question;
    private final JTextField pointsValue;
    private Quiz newQuiz;
    private final QuestionTable questionTable;
    private final JTextField questionTitle;
    private QuestionBank questionBank;
    private JButton addQuestionButton;
    private JButton addButton;
    private JButton removeButton;
    private JCheckBox abetCheckBox;
    private final int defaultAnswers = 2;
    private boolean edit;
    private Question editQuestion;
    private int editedRow;
    private final int numAnswers = 10;
    
    public FillInTheBlankPanel(JFrame frame, JTextArea fitbQuestion, JTextField points, Quiz quiz, QuestionTable table, JTextField title)
    {
        mainFrame = frame;
        question = fitbQuestion;
        pointsValue = points;
        newQuiz = quiz;
        questionTable = table;
        questionTitle = title;
        fillInTheBlankPanel();       
    }
    
    public FillInTheBlankPanel(JFrame frame, JTextArea fitbQuestion, JTextField points, QuestionBank bank, QuestionTable table, JTextField title)
    {
        mainFrame = frame;
        question = fitbQuestion;
        pointsValue = points;
        questionBank = bank;
        questionTable = table;
        questionTitle = title;
        fillInTheBlankPanel();          
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
     * Places the labelsPanel and answersPanel on the component together 
     */
    private void fillInTheBlankPanel()
    {
        // contains labelsPanels and answersPanel
        this.setLayout(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints answersConstraint = new GridBagConstraints();
        
        // places labelsPanel at the top of the component
        JPanel labelsPanel = labelsPanel();
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        this.add(labelsPanel, labelConstraint); 
         
        // places answersPanel below labelsPanel
        JPanel answersPanel = answersPanel();
        answersConstraint.fill = GridBagConstraints.HORIZONTAL;
        answersConstraint.gridx = 0;
        answersConstraint.gridy = 1;
        answersConstraint.insets = new Insets(0, 0, 0, 101);
        this.add(answersPanel, answersConstraint);   
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
        GridBagConstraints answerConstraint = new GridBagConstraints();
        GridBagConstraints checkBoxConstraint = new GridBagConstraints();
        
        // places answerLabel at the top of labelsPanel
        answerConstraint.fill = GridBagConstraints.HORIZONTAL;
        answerConstraint.gridx = 0;
        answerConstraint.gridy = 0;
        answerConstraint.insets = new Insets(0, 0, 0, 112);
        labelsPanel.add(answerLabel, answerConstraint); 
        
        // places abetCheckBox on the right side of answerLabel
        checkBoxConstraint.fill = GridBagConstraints.HORIZONTAL;
        checkBoxConstraint.gridx = 1;
        checkBoxConstraint.gridy = 0;
        checkBoxConstraint.insets = new Insets(0, 170, 0, 0);
        labelsPanel.add(abetCheckBox, checkBoxConstraint); 
                
        return labelsPanel;
    }
    
    /**
     * Creates an array of 10 text fields for the user's answers
     */
    private void createTextFields()
    {
        answerFields = new JTextField[numAnswers];
        answerPanels = new JPanel[numAnswers];
        for (int i = 0; i < numAnswers; i++)
        {
            answerFields[i] = new JTextField();
            answerFields[i].setPreferredSize(new Dimension(200, 25));
            answerPanels[i] = new JPanel();
            answerPanels[i].add(answerFields[i]);
            // only display the first two text fields upon initialization
            if (i > 1)
            {
                answerPanels[i].setVisible(false);
            }
        }       
    }
    
    /**
     * Places each text field panel in 10 panels using GridBagLayout
     */
    private void rowPanels()
    {
        rowPanels = new JPanel[numAnswers];
        textFieldConstraints = new GridBagConstraints[numAnswers];
        for (int i = 0; i < numAnswers; i++)
        {
            rowPanels[i] = new JPanel(new GridBagLayout());
            textFieldConstraints[i] = new GridBagConstraints();   
                      
            textFieldConstraints[i].fill = GridBagConstraints.HORIZONTAL;
            textFieldConstraints[i].gridx = 0;
            textFieldConstraints[i].gridy = 0;
            textFieldConstraints[i].insets = new Insets(0, 0, 0, 125);
            rowPanels[i].add(answerPanels[i], textFieldConstraints[i]);   
        }
    }
    
    /**
     * Organizes each of the row panels on answerPanel using GridBagLayout and 
     * CardLayout
     *
     * @return answer choice panel
     */
    private JPanel answersPanel()
    {
        // create each of the answer's components
        createTextFields(); 
        rowPanels();
        JPanel answersPanel = new JPanel(new GridBagLayout());

        containerPanels = new JPanel[5];
        cardPanels = new JPanel[5];
        rowConstraints = new GridBagConstraints[numAnswers];
        buttonPanels = new JPanel[5];
        for (int i = 0; i < numAnswers; i++)
        {                        
            rowConstraints[i] = new GridBagConstraints();
            rowConstraints[i].fill = GridBagConstraints.HORIZONTAL;
            rowConstraints[i].gridx = 0;
            rowConstraints[i].gridy = i;
            
            // put the first five panels in CardLayout
            if (i < 5)
            {
                cardPanels[i] = new JPanel(new CardLayout());
                containerPanels[i] = new JPanel();
                containerPanels[i].setPreferredSize(new Dimension(190, 35));
                buttonPanels[i] = buttonPanel();
                
                cardPanels[i].add(containerPanels[i], "blank " + (i));
                cardPanels[i].add(rowPanels[i], "row " + (i));
                cardPanels[i].add(buttonPanels[i], "button " + i);
                
                CardLayout layout = (CardLayout) (cardPanels[i].getLayout());
                answersPanel.add(cardPanels[i], rowConstraints[i]);  
                
                // show the first two text fields upon initialization 
                if (i < defaultAnswers)
                {
                    layout.show(cardPanels[i], "row " + (i));
                }
                // show the add/remove button panel
                if (i == defaultAnswers)
                {
                    layout.show(cardPanels[i], "button " + (i));
                }
                // show three blank panels upon initialization 
                if ((i > defaultAnswers) && (i < 5))
                {
                    layout.show(cardPanels[i], "blank " + (i));
                }
            }
            else
            {
                answersPanel.add(rowPanels[i], rowConstraints[i]);  
            }
        }
        
        GridBagConstraints buttonPanelConstraint = new GridBagConstraints();
        answerButtonPanel = buttonPanel();
        answerButtonPanel.setVisible(false);
        buttonPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonPanelConstraint.gridx = 0;
        buttonPanelConstraint.gridy = 10;
        answersPanel.add(answerButtonPanel, buttonPanelConstraint);
        
        GridBagConstraints addButtonConstraint = new GridBagConstraints();
        JPanel buttonPanel = addButtonPanel();
        addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        addButtonConstraint.gridx = 0;
        addButtonConstraint.gridy = 11;
        addButtonConstraint.insets = new Insets(0, 100, 0, 0);
        answersPanel.add(buttonPanel, addButtonConstraint);
                      
        return answersPanel;
    }
    
    /**
     * Creates the button panel that allows the user to add/remove answers
     * 
     * @return button panel
     */
    public JPanel buttonPanel()
    {
        addButton = new JButton("+");
        removeButton = new JButton("-");
        
        // contains addButton and removeButton
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints addConstraint = new GridBagConstraints();
        GridBagConstraints removeConstraint = new GridBagConstraints();
        
        // places addButton on the left side of buttonPanel
        addConstraint.fill = GridBagConstraints.HORIZONTAL;
        addConstraint.gridx = 0;
        addConstraint.gridy = 0;
        addConstraint.insets = new Insets(0, 0, 0, 2);
        buttonPanel.add(addButton, addConstraint);
        
        // places removeButton on the right side of buttonPanel
        removeConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeConstraint.gridx = 1;
        removeConstraint.gridy = 0;
        removeConstraint.insets = new Insets(0, 0, 0, 125);
        buttonPanel.add(removeButton, removeConstraint);
        
        // listens for when addButton is selected
        addButton.addActionListener((ActionEvent e) -> { 
            // add another text field
            addAnswer();          
        });
        
        // listens for when removeButton is selected
        removeButton.addActionListener((ActionEvent e) -> {
            try
            {
                // remove the bottom text field
                removeAnswer();
            }
            catch (IndexOutOfBoundsException n)
            {
                removeButton.setEnabled(false);
            }
        });
                
        return buttonPanel;
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
            // the title of the question
            String questionLabel = questionTitle.getText();

            try
            {
                // the point value of the question
                String pointsString = pointsValue.getText();
                float floatPoints = Float.parseFloat(pointsString); 
                
                // add quiz question
                if ((newQuiz != null) && (edit == false))
                {            
                    FillInTheBlankQuestion fitbQuestion = new FillInTheBlankQuestion(questionLabel, floatPoints);
                    fitbQuestion.setLabel(new Label(questionString));
                    addFITBQuestion(fitbQuestion, questionString);                
                }
                // update quiz question
                if ((newQuiz != null) && (edit == true))
                {
                    updateQuizQuestion(questionLabel, floatPoints, questionString);
                }
                // add bank question
                if ((questionBank != null) && (edit == false))
                {
                    FillInTheBlankQuestion fitbQuestion = new FillInTheBlankQuestion(questionLabel);
                    fitbQuestion.setLabel(new Label(questionString));
                    addFITBQuestion(fitbQuestion, questionString);
                }
                // update bank question
                if ((questionBank != null) && (edit == true))
                { 
                    updateBankQuestion(questionLabel, questionString);                    
                }

                mainFrame.dispose();
            }
            catch(NumberFormatException n) {}            

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
            populateAnswers(answer);           
        }
        // populate the banks's question information
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
            populateAnswers(answer);           
        }        
    }
    
    /**
     * Populates the question's answer(s)
     * 
     * @param answer the question's answer
     */
    private void populateAnswers(String answer)
    {
        String[] answers = answer.split(",");
        
        // remove any unused text fields
        if (answers.length < defaultAnswers)
        {
            hidePanels(answers.length);
        }
        
        // display the answers
        for (int i = 0; i < answers.length; i++)
        {
            int spaceIndex = answers[i].indexOf(" ");
            if (spaceIndex == 0)
            {
                answers[i] = answers[i].substring(0, spaceIndex) + answers[i].substring(spaceIndex + 1);
            }
            
            if (i > 1)
            {
                addAnswer();
            }           
            answerPanels[i].setVisible(true);
            answerFields[i].setText(answers[i]);
        }
    }
    
    /**
     * Hides any unused answer text fields
     * 
     * @param answers the number of answers for the question
     */
    private void hidePanels(int answers)
    {
        for (int i = 0; i < (defaultAnswers - answers); i++)
        {
            removeAnswer();            
        }       
    }
      
    /**
     * Ensures that the question title, points value, and question description 
     * have been entered before addQuestionButton is enabled
     */
    private void componentListeners()
    {       
        // event listener for the question title text field
        Document titleDocument = questionTitle.getDocument();
        titleDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                boolean points = (pointsValue.getText()).isEmpty();
                boolean fitbQuestion = (question.getText()).isEmpty();
                
                if (newQuiz != null)
                {
                    if ((points == false) && (fitbQuestion == false))
                    {
                        addQuestionButton.setEnabled(true);
                    }
                }
                if (questionBank != null)
                {
                    if (fitbQuestion == false)
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
                        boolean fitbQuestion = (question.getText()).isEmpty();
                        if ((title == false) && (fitbQuestion == false))
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
                    // disable the button if the points text field is empty 
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
                // disabel the button if the description text area is empty
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
     * Adds a text field to the panel
     */
    private void addAnswer()
    {
        for (int i = 0; i < numAnswers; i++)
        {
            if ((answerPanels[i].isVisible() == false))
            {
                // make the component visible
                answerPanels[i].setVisible(true);
                // handle the card layout panels if i <= 4
                if (i < 4)
                {
                    // show the row panel (text field) at index i 
                    CardLayout layout = (CardLayout) (cardPanels[i].getLayout());
                    layout.show(cardPanels[i], "row " + i);
                    
                    // show the add/remove button panel at i + 1
                    CardLayout buttonLayout = (CardLayout) (cardPanels[i + 1].getLayout());
                    buttonLayout.show(cardPanels[i + 1], "button " + (i + 1));
                }
                if (i == 4)
                {
                    // show the row panel at i = 4
                    CardLayout rowLayout = (CardLayout) (cardPanels[i].getLayout());
                    rowLayout.show(cardPanels[i], "row " + i);
                    answerButtonPanel.setVisible(true);                        
                }
                if (i == (numAnswers - 1))
                {
                    addButton.setEnabled(false);
                }
                break;
            }
        }            
    }
    
    /**
     * Removes a text field from the panel
     */
    private void removeAnswer()
    {
        // loop until a non-visible text field is found
        int index = 0;
        for (int i = 0; i < numAnswers; i++)
        {            
            if ((answerPanels[i].isVisible() == false))
            {
                index = i - 1;
                break;
            }
            // if the maximum amount of answers are displayed on the panel
            // remove the last one 
            else
            {
                answerPanels[9].setVisible(false);
                answerFields[9].setText(null);
            }
        }
            
        // make this components non-visible
        answerPanels[index].setVisible(false);
        answerFields[index].setText(null);        
        if (index < 4)
        {
            answerButtonPanel.setVisible(false);
            // hide the row panel (text field) at index i     
            CardLayout layout = (CardLayout) (cardPanels[index + 1].getLayout());
            layout.show(cardPanels[index + 1], "blank " + (index + 1));
                   
            // show the button panel at index i 
            CardLayout buttonLayout = (CardLayout) (cardPanels[index].getLayout());
            buttonLayout.show(cardPanels[index], "button " + (index));
        }
        if (index == 4)
        {
            // show the card layout button panel at i = 4
            answerButtonPanel.setVisible(false);
            CardLayout layout = (CardLayout) (cardPanels[index].getLayout());
            layout.show(cardPanels[index], "button " + (index));
        }
        if (index > 4)
        {
            answerButtonPanel.setVisible(true);
        }
        if (index < (numAnswers - 1))
        {
            addButton.setEnabled(true);
        }
    }
    
    /**
     * Adds a fill in the blank question to a quiz/bank
     * 
     * @param question the question to be added
     */
    private void addFITBQuestion(FillInTheBlankQuestion question, String questionString)
    {
        addAnswers(question, questionString);
     
        if (newQuiz != null)
        {            
            newQuiz.addQuestion(question);
            int index = newQuiz.getQuestionIndex(question);
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
        if (questionBank != null)
        {
            questionBank.add(question);
            int index = questionBank.getQuestionIndex(question);
            Question newQuestion = questionBank.getQuestion(index);
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
        populateTable();
    }
    
    /**
     * Gets the tags in each fill in the blank question
     * 
     * @param tagQuestion blank/quiz question
     * @return the tag array list 
     */
    private ArrayList<String> getTag(String tagQuestion)
    {
        ArrayList<String> tags = new ArrayList<>();
        int length = tagQuestion.length();
        for (int i = 0; i < length; i++)
        {
            char Character = tagQuestion.charAt(i);
            if (Character == '[')
            {
                if ((i + 2) < length)
                {
                    char endBracket = tagQuestion.charAt(i + 2);
                    if (endBracket == ']')
                    {
                        char fitbTag = tagQuestion.charAt(i + 1);
                        tags.add(String.valueOf(fitbTag));
                    }       
                }
            }
        }
               
        return tags;
    }
    
    /**
     * Updates a quiz question
     * 
     * @param updateTitle the question title
     * @param updatePoints the points amount for the question
     * @param updateQuestion the question
     */
    private void updateQuizQuestion(String updateTitle, float updatePoints, String updateQuestion)
    {
        FillInTheBlankQuestion newQuestion = (FillInTheBlankQuestion) editQuestion;
        newQuestion.setPoints(updatePoints);
        newQuestion.setTitle(updateTitle);
        newQuestion.setLabel(new Label(updateQuestion));
        
        addAnswers(newQuestion, updateQuestion);        
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
     * @param updateTitle the question title
     * @param updateQuestion the question
     */
    private void updateBankQuestion(String updateTitle, String updateQuestion)
    {
        FillInTheBlankQuestion newQuestion = (FillInTheBlankQuestion) editQuestion;
        newQuestion.setTitle(updateTitle);
        newQuestion.setLabel(new Label(updateQuestion));
        
        addAnswers(newQuestion, updateQuestion);
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
     * Adds answers to a multiple choice question
     * 
     * @param question 
     */
    private void addAnswers(FillInTheBlankQuestion question, String questionString)
    {
        ArrayList<String> questionTags = getTag(questionString);
        // get each answer
        for (int i = 0; i < numAnswers; i++)
        {
            String answer = answerFields[i].getText();                    
            boolean empty = answer.isEmpty();
            // if the text field isn't empty and it's visible add the answer to the quiz
            if ((empty == false) && (answerPanels[i].isVisible() == true))
            {
                question.setAnswer(questionTags.get(i), new BlankAnswer(i, answer));
            }
        } 
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