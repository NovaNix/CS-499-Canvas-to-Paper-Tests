package io.github.csgroup.quizmaker.ui.components;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;

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
    private final int numAnswers = 10;
    private QuestionBank questionBank;
    
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
        answersConstraint.insets = new Insets(0, 0, 0, 175);
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
        JCheckBox abetCheckBox = new JCheckBox("ABET Question");
        
        // contains answerLabel and abetCheckbox
        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints checkBoxConstraint = new GridBagConstraints();
        
        // places answerLabel on the left side of labelsPanel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(0, 22, 0, 116);
        labelsPanel.add(answerLabel, labelConstraint); 
        
        // places abetCheckBox on the right side of answerLabel
        checkBoxConstraint.fill = GridBagConstraints.HORIZONTAL;
        checkBoxConstraint.gridx = 1;
        checkBoxConstraint.gridy = 0;
        checkBoxConstraint.insets = new Insets(0, 90, 0, 0);
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
            answerFields[i].setPreferredSize(new Dimension(185, 25));
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
            group.add(radioButtons[i]);
            // only display the first four radio buttons upon initialization
            if (i > 3)
            {
                radioButtons[i].setVisible(false);
            }            
        }
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
                if (i < 4)
                {
                    layout.show(cardPanels[i], "row " + (i));
                }
                // show the add/remove button panel
                if (i == 4)
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
        JPanel addButton= addButtonPanel();
        addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        addButtonConstraint.gridx = 0;
        addButtonConstraint.gridy = 11;
        addButtonConstraint.insets = new Insets(0, 175, 0, 0);
        answersPanel.add(addButton, addButtonConstraint);
                      
        return answersPanel;
    }
       
    /**
     * Creates the button panel that allows the user to add/remove answer choices
     * 
     * @return button panel
     */
    public JPanel buttonPanel()
    {
        JButton addButton = new JButton("+");
        JButton removeButton = new JButton("-");
        
        // contains addButton and removeButton
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints addConstraint = new GridBagConstraints();
        GridBagConstraints removeConstraint = new GridBagConstraints();
        
        // places addButton on the left side of buttonPanel
        addConstraint.fill = GridBagConstraints.HORIZONTAL;
        addConstraint.gridx = 0;
        addConstraint.gridy = 0;
        addConstraint.insets = new Insets(0, 20, 0, 2);
        buttonPanel.add(addButton, addConstraint);
        
        // places removeButton on the right side of buttonPanel
        removeConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeConstraint.gridx = 1;
        removeConstraint.gridy = 0;
        buttonPanel.add(removeButton, removeConstraint);
        
        // listens for when addButton is selected
        addButton.addActionListener((ActionEvent e) -> { 
            // add another text field and radio button on the panel
            addAnswerChoices();           
        });
        
        // listens for when removeButton is selected
        removeButton.addActionListener((ActionEvent e) -> { 
            // remove the bottom text field and radio button
            removeAnswerChoices();
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
            // the point value of the question
            String pointsString = pointsValue.getText();
            // the title of the question
            String questionLabel = questionTitle.getText();

            try 
            {
                float floatPoints = Float.parseFloat(pointsString);                
                MultipleChoiceQuestion mcQuestion = new MultipleChoiceQuestion(questionLabel, floatPoints);
                mcQuestion.setLabel(new Label(questionString));
                addMCQuestion(mcQuestion);
                
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
     * Adds a text field and radio button to the panel
     */
    private void addAnswerChoices()
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
                break;
            }
        }
    }
    
    /**
     * Removes a text field and radio button from the panel
     */
    private void removeAnswerChoices()
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
    }
    
    /**
     * Adds a multiple choice question to a quiz/bank
     * 
     * @param question the question to be added
     */
    private void addMCQuestion(MultipleChoiceQuestion question)
    {
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
        if (newQuiz != null)
        {
            newQuiz.addQuestion(question);
        }
        if (questionBank != null)
        {
            questionBank.add(question);
        }
        populateTable();
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