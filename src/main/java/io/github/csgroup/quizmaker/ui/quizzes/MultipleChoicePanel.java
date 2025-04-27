package io.github.csgroup.quizmaker.ui.quizzes;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import java.awt.CardLayout;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * Creates the panel that allows user to create the multiple choice question type
 * answers. After the answers have been entered, they are added to the quiz or 
 * question bank
 * 
 * @author Emily Palmer
 */
public class MultipleChoicePanel extends JComponent
{    
    private JTextField[] answerFields;
    private JPanel[] answerPanels;
    private JRadioButton[] radioButtons;
    private JPanel[] rowPanels;
    private GridBagConstraints[] textFieldConstraints;
    private GridBagConstraints[] buttonConstraints;
    private GridBagConstraints[] rowConstraints;
    private JPanel[] cardPanels;
    private JPanel[] containerPanels;
    private JPanel[] buttonPanels;
    private final JFrame mainFrame;
    private JPanel answerButtonPanel;
    private final JTextArea question;
    private final JTextField pointsValue;
    private Quiz newQuiz;
    private final QuestionTable questionTable;
    private final JTextField questionTitle;
    private JButton addQuestionButton;
    private JButton addButton;
    private boolean edit;
    private JCheckBox abetCheckBox;
    private JButton removeButton;
    private final int numAnswers = 10;
    private final int defaultAnswers = 4;
    private QuestionBank questionBank;
    private Question editQuestion;
    private int editedRow;
    
    public MultipleChoicePanel(JFrame frame, JTextArea mcQuestion, JTextField points, Quiz quiz, QuestionTable table, JTextField title)
    {
        mainFrame = frame;
        question = mcQuestion;
        pointsValue = points;
        newQuiz = quiz;
        questionTable = table;
        questionTitle = title;
        multipleChoicePanel();          
    }
    
    public MultipleChoicePanel(JFrame frame, JTextArea mcQuestion, JTextField points, QuestionBank bank, QuestionTable table, JTextField title)
    {
        mainFrame = frame;
        question = mcQuestion;
        pointsValue = points;
        questionBank = bank;
        questionTable = table;
        questionTitle = title;
        multipleChoicePanel();          
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
    private void multipleChoicePanel()
    {     
        // contains labelsPanel and answersPanel
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
        //answersConstraint.insets = new Insets(0, 0, 0, 195);
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
        
        // contains answerLabel and abetCheckbox
        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints checkBoxConstraint = new GridBagConstraints();
        
        // places answerLabel on the left side of labelsPanel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(0, 24, 0, 132);
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
     * Creates an array of 10 text fields for the user's answer choices
     */
    private void createTextFields()
    {
        answerFields = new JTextField[numAnswers];
        answerPanels = new JPanel[numAnswers];
        for (int i = 0; i < numAnswers; i++)
        {
            answerFields[i] = new JTextField();
            answerFields[i].setPreferredSize(new Dimension(200, 25));
            answerFields[i].setText(null);
            answerPanels[i] = new JPanel();
            answerPanels[i].add(answerFields[i]);
            // only display the first four text fields upon initialization
            if (i > 3)
            {
                answerPanels[i].setVisible(false);
            }
        }       
    }
     
    /**
     * Creates an array of 10 radio buttons for the user to mark the correct answer
     */
    private void createRadioButtons()
    {
        ButtonGroup group = new ButtonGroup();
        radioButtons = new JRadioButton[numAnswers];
        for(int i = 0; i < numAnswers; i++)
        {
            radioButtons[i] = new JRadioButton();
            
            // by default set the first question to be correct
            if (i == 0)
            	radioButtons[i].setSelected(true);
            
            // only display the first four radio buttons upon initialization
            if (i > 3)
            {
                radioButtons[i].setVisible(false);
            }            
        }
        radioButtons[0].setSelected(true);
    }
    
    /**
     * Places each radio button and text field panel in 10 panels using GridBagLayout
     */
    private void rowPanels()
    {
        rowPanels = new JPanel[numAnswers];
        textFieldConstraints = new GridBagConstraints[numAnswers];
        buttonConstraints = new GridBagConstraints[numAnswers];
        for (int i = 0; i < numAnswers; i++)
        {
            rowPanels[i] = new JPanel(new GridBagLayout());
            textFieldConstraints[i] = new GridBagConstraints();
            buttonConstraints[i] = new GridBagConstraints();
            
            buttonConstraints[i].fill = GridBagConstraints.HORIZONTAL;
            buttonConstraints[i].gridx = 0;
            buttonConstraints[i].gridy = 0;
            rowPanels[i].add(radioButtons[i], buttonConstraints[i]);
        
            textFieldConstraints[i].fill = GridBagConstraints.HORIZONTAL;
            textFieldConstraints[i].gridx = 1;
            textFieldConstraints[i].gridy = 0;
            textFieldConstraints[i].insets = new Insets(0, 0, 0, 200);
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
        // create each of the answer choice components
        createTextFields();
        createRadioButtons(); 
        rowPanels();
        
        JPanel answersPanel = new JPanel(new GridBagLayout());
        rowConstraints = new GridBagConstraints[numAnswers];
        containerPanels = new JPanel[5];
        cardPanels = new JPanel[5];
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
                containerPanels[i].setPreferredSize(new Dimension(185, 35));
                buttonPanels[i] = buttonPanel();
                
                cardPanels[i].add(containerPanels[i], "blank " + (i));
                cardPanels[i].add(rowPanels[i], "row " + (i));
                cardPanels[i].add(buttonPanels[i], "button " + i);
                
                CardLayout layout = (CardLayout) (cardPanels[i].getLayout());
                answersPanel.add(cardPanels[i], rowConstraints[i]);  
                
                // show the first four text fields and radio buttons upon initialization 
                if (i < defaultAnswers)
                {
                    layout.show(cardPanels[i], "row " + (i));
                }
                // show the add/remove button panel
                if (i == defaultAnswers)
                {
                    layout.show(cardPanels[i], "button " + (i));
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
        //addButtonConstraint.insets = new Insets(0, 195, 0, 0);
        answersPanel.add(buttonPanel, addButtonConstraint);
                      
        return answersPanel;
    }
       
    /**
     * Creates the button panel that allows the user to add/remove answer choices
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
        removeConstraint.insets = new Insets(0, 0, 0, 190);
        buttonPanel.add(removeButton, removeConstraint);
        
        // listens for when addButton is selected
        addButton.addActionListener((ActionEvent e) -> { 
            // add another text field and radio button on the panel
            addAnswerChoice();           
        });
        
        // listens for when removeButton is selected
        removeButton.addActionListener((ActionEvent e) -> { 
            try
            {
                // remove the bottom text field
                removeAnswerChoice();
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
        addQuestionButton = new JButton("Save");
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
                    MultipleChoiceQuestion mcQuestion = new MultipleChoiceQuestion(questionLabel, floatPoints);
                    mcQuestion.setLabel(new Label(questionString));
                    addMCQuestion(mcQuestion);
                }
                // update quiz question
                if ((newQuiz != null) && (edit == true))
                { 
                    updateQuestion(questionLabel, floatPoints, questionString);
                }
                // add bank question
                if ((questionBank != null) && (edit == false))
                {
                    MultipleChoiceQuestion mcQuestion = new MultipleChoiceQuestion(questionLabel, floatPoints);
                    mcQuestion.setLabel(new Label(questionString));
                    addMCQuestion(mcQuestion);
                }
                // update bank question
                if ((questionBank != null) && (edit == true))
                {
                    updateQuestion(questionLabel, floatPoints, questionString);
                }
                
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
            populateAnswers(answer); 
        }
        // populate the bank's question information
        if (questionBank != null)
        {
            editQuestion = questionBank.getQuestion(index);    
            
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
    }
    
    /**
     * Populates the question's answer(s)
     * 
     * @param answer the question's answer
     */
    private void populateAnswers(String answer)
    {             
        MultipleChoiceQuestion mcQuestion = (MultipleChoiceQuestion) editQuestion;
        int size = mcQuestion.getCorrectAnswers().size();
        
        // parse the html out of the answer string
        String removeRoot = parseString(answer, "<html>");
        String removeRootEnd = parseString(removeRoot, "</html>");
        String removeBoldEnd = parseString(removeRootEnd, "</b>");
        String removeBold = parseString(removeBoldEnd, "<b>");

        String[] answers = removeBold.split(",");
        
        // removes any unused answer text fields
        if (answers.length < defaultAnswers)
        {
            hidePanels(answers.length);
        }
        
        for (int i = 0; i < answers.length; i++)
        {
            int spaceIndex = answers[i].indexOf(" ");
            if (spaceIndex == 0)
            {
                answers[i] = answers[i].substring(0, spaceIndex) + answers[i].substring(spaceIndex + 1);
            }
            
            if (i > (defaultAnswers - 1))
            {
                addAnswerChoice();
            }           
            answerPanels[i].setVisible(true);
            answerFields[i].setText(answers[i]);
            if (i < size)
            {
                radioButtons[i].setSelected(true);
            }
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
            removeAnswerChoice();            
        }       
    }
    
    /**
     * Parses strings
     * 
     * @param input the string to parse
     * @param delimiter the delimiter used to parse the string
     * @return the parsed string
     */
    private String parseString(String input, String delimiter)
    {
        String[] parsedStringArray = input.split(delimiter);
        String parsedString = String.join("", parsedStringArray);
        
        return parsedString;
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
                boolean mcQuestion = (question.getText()).isEmpty();

                if ((points == false) && (mcQuestion == false))
                {
                    addQuestionButton.setEnabled(true);
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
                    boolean mcQuestion = (question.getText()).isEmpty();
                    if ((title == false) && (mcQuestion == false))
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
     * Adds a text field and radio button to the panel
     */
    private void addAnswerChoice()
    {
        for (int i = 0; i < numAnswers; i++)
        {
            // loop until a non-visible radio button is found 
            if ((radioButtons[i].isVisible() == false))
            {
                // make these componentc visible
                answerPanels[i].setVisible(true);
                radioButtons[i].setVisible(true);
                // handle the card layout panels if i <= 4
                if (i < 4)
                {
                    // show the row panel (text field and radio button) at index i 
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
     * Removes a text field and radio button from the panel
     */
    private void removeAnswerChoice()
    {
        // loop until a non-visible radio button is found
        int index = 0;
        for (int i = 0; i < numAnswers; i++)
        {
            if (radioButtons[i].isVisible() == false)
            {
                index = i - 1;
                break;
            }
            // if the maximum amount of answer choices are displayed on the panel
            // remove the last one 
            else
            {
                answerPanels[9].setVisible(false);
                radioButtons[9].setVisible(false);
                answerFields[9].setText(null);
            }
        }           
               
        // make these components non-visible
        answerPanels[index].setVisible(false);
        radioButtons[index].setVisible(false);
        answerFields[index].setText(null);
        // handle the card layout panels if i <= 4
        if (index < 4)
        {
            answerButtonPanel.setVisible(false);                
            // hide the row panel (text field and radio button) at index i 
            CardLayout layout = (CardLayout) (cardPanels[index + 1].getLayout());
            layout.show(cardPanels[index + 1], "blank " + (index + 1));
                  
            // show the button panel at index i 
            CardLayout buttonLayout = (CardLayout) (cardPanels[index].getLayout());
            buttonLayout.show(cardPanels[index], "button " + (index));
        }
        if (index == 4)
        {
            // show the card layout utton panel at i = 4
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
     * Adds a multiple choice question to a quiz/bank
     * 
     * @param question the question to be added
     */
    private void addMCQuestion(MultipleChoiceQuestion question)
    {
        addAnswers(question);
        if (newQuiz != null)
        {
            newQuiz.addQuestion(question);
            int index = newQuiz.getQuestionIndex(question);
            Question newQuestion = newQuiz.getQuestion(index);
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
     * Updates a quiz question
     * 
     * @param updateTitle the question title
     * @param updatePoints the points amount for the question
     * @param updateQuestion the question
     */
    private void updateQuestion(String updateTitle, float updatePoints, String updateQuestion)
    {
        MultipleChoiceQuestion newQuestion = (MultipleChoiceQuestion) editQuestion;
        newQuestion.setPoints(updatePoints);
        newQuestion.setTitle(updateTitle);
        newQuestion.setLabel(new Label(updateQuestion));

        newQuestion.clearAnswers();
        addAnswers(newQuestion);
        
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
    private void addAnswers(MultipleChoiceQuestion question)
    {
        // get each answer
        for (int i = 0; i < numAnswers; i++)
        {
            // get each answer choice
            String answerChoice = answerFields[i].getText();                    
            boolean empty = answerChoice.isEmpty();
            // if the text field isn't empty and it's visible add the answer 
            // choice to the quiz
            if ((empty == false) && (answerPanels[i].isVisible() == true))
            {
                // get the correct answer
                if (radioButtons[i].isSelected() == true)
                {
                    question.addAnswer(new SimpleAnswer(i, answerChoice), true);
                }
                else
                {
                    question.addAnswer(new SimpleAnswer(i, answerChoice), false);
                }
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