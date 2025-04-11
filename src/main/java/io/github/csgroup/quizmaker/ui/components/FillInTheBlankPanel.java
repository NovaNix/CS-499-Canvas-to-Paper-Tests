package io.github.csgroup.quizmaker.ui.components;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion;

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
    
    public FillInTheBlankPanel(JFrame frame, JTextArea mcQuestion, JTextField points, QuestionBank bank, QuestionTable table, JTextField title)
    {
        mainFrame = frame;
        question = mcQuestion;
        pointsValue = points;
        questionBank = bank;
        questionTable = table;
        questionTitle = title;
        fillInTheBlankPanel();          
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
        JCheckBox abetCheckBox = new JCheckBox("ABET Question");
        
        // contains answerLabel and abetCheckBox
        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints answerConstraint = new GridBagConstraints();
        GridBagConstraints checkBoxConstraint = new GridBagConstraints();
        
        // places answerLabel at the top of labelsPanel
        answerConstraint.fill = GridBagConstraints.HORIZONTAL;
        answerConstraint.gridx = 0;
        answerConstraint.gridy = 0;
        answerConstraint.insets = new Insets(0, 4, 0, 122);
        labelsPanel.add(answerLabel, answerConstraint); 
        
        // places abetCheckBox on the right side of answerLabel
        checkBoxConstraint.fill = GridBagConstraints.HORIZONTAL;
        checkBoxConstraint.gridx = 1;
        checkBoxConstraint.gridy = 0;
        checkBoxConstraint.insets = new Insets(0, 102, 0, 0);
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
            answerFields[i].setPreferredSize(new Dimension(190, 25));
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
            textFieldConstraints[i].insets = new Insets(0, 0, 0, 90);
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
                if (i < 2)
                {
                    layout.show(cardPanels[i], "row " + (i));
                }
                // show the add/remove button panel
                if (i == 2)
                {
                    layout.show(cardPanels[i], "button " + (i));
                }
                // show three blank panels upon initialization 
                if ((i > 2) && (i < 5))
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
        JPanel addButton= addButtonPanel();
        addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        addButtonConstraint.gridx = 0;
        addButtonConstraint.gridy = 11;
        addButtonConstraint.insets = new Insets(0, 100, 0, 0);
        answersPanel.add(addButton, addButtonConstraint);
                      
        return answersPanel;
    }
    
    /**
     * Creates the button panel that allows the user to add/remove answers
     * 
     * @return button panel
     */
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
        removeConstraint.insets = new Insets(0, 0, 0, 90);
        buttonPanel.add(removeButton, removeConstraint);
        
        // listens for when addButton is selected
        addButton.addActionListener((ActionEvent e) -> { 
            // add another text field
            addAnswer();          
        });
        
        // listens for when removeButton is selected
        removeButton.addActionListener((ActionEvent e) -> {
            // remove the bottom text field
            removeAnswer();
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
        JButton addButton = new JButton("Add");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        
        addButton.addActionListener((ActionEvent e) -> { 
            // the question the user entered
            String questionString = question.getText();
            // the point value of the question
            String pointsString = pointsValue.getText();
            // the title of the question
            String questionLabel = questionTitle.getText();
            
            // check for empty strings
            boolean emptyQuestion = questionString.isEmpty();             
            boolean emptyPoints = pointsString.isEmpty();
            boolean emptyTitle = questionLabel.isEmpty();                       
            // add the question to the quiz if the user has entered the points value,
            // question, and question title
            if ((emptyQuestion == false) && (emptyPoints == false) && (emptyTitle == false))
            {
                try
                {
                    float floatPoints = Float.parseFloat(pointsString);               
                    FillInTheBlankQuestion fitbQuestion = new FillInTheBlankQuestion(questionLabel, floatPoints);
                    fitbQuestion.setLabel(new Label(questionString));
                    addFITBQuestion(fitbQuestion, questionString);

                    mainFrame.dispose();
                }
                catch(NumberFormatException n) {}            
            }
        });
        
        return buttonPanel;
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
    }
    
    /**
     * Adds a fill in the blank question to a quiz/bank
     * 
     * @param question the question to be added
     */
    private void addFITBQuestion(FillInTheBlankQuestion question, String questionString)
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