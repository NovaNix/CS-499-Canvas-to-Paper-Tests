package io.github.csgroup.quizmaker.ui.components;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.questions.MatchingQuestion;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.CardLayout;
import javax.swing.JTextArea;

/**
 * Creates the panel that allows user to create the matching question type
 * answers. After the answers have been entered, they are added to the quiz or 
 * question bank
 * 
 * @author Emily Palmer
 */
public class MatchingPanel extends JComponent
{
    private final JFrame mainFrame;
    private JTextField leftTextFields[];
    private JPanel leftPanels[];
    private JTextField rightTextFields[];
    private JPanel rightPanels[];
    private JPanel rowPanels[];
    private GridBagConstraints leftConstraints[];
    private GridBagConstraints rightConstraints[];
    private GridBagConstraints rowConstraints[];
    private JPanel cardPanels[];
    private JPanel buttonPanels[];
    private JPanel containerPanels[];
    private JPanel answerButtonPanel;
    private final JTextArea question;
    private final JTextField pointsValue;
    private Quiz newQuiz;
    private final QuestionTable questionTable;
    private final JTextField questionTitle;
    private QuestionBank questionBank;
    private final int numAnswers = 15;
    
    public MatchingPanel(JFrame frame, JTextArea matchQuestion, JTextField points, Quiz quiz, QuestionTable table, JTextField title)
    {
        question = matchQuestion;
        pointsValue = points;
        newQuiz = quiz;
        questionTable = table;
        mainFrame = frame;
        questionTitle = title;
        matchingPanel();       
    }
    
    public MatchingPanel(JFrame frame, JTextArea mcQuestion, JTextField points, QuestionBank bank, QuestionTable table, JTextField title)
    {
        mainFrame = frame;
        question = mcQuestion;
        pointsValue = points;
        questionBank = bank;
        questionTable = table;
        questionTitle = title;
        matchingPanel();          
    }
    
    /**
     * Places the labelsPanel and answersPanel on the component together 
     */
    private void matchingPanel()
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
        JPanel answersPane = answersPanel();
        answersConstraint.fill = GridBagConstraints.HORIZONTAL;
        answersConstraint.gridx = 0;
        answersConstraint.gridy = 1;
        this.add(answersPane, answersConstraint);  
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
        labelConstraint.insets = new Insets(0, 3, 0, 140);
        labelsPanel.add(answerLabel, labelConstraint); 
        
        // places abetCheckBox on the right side of answerLabel
        checkBoxConstraint.fill = GridBagConstraints.HORIZONTAL;
        checkBoxConstraint.gridx = 1;
        checkBoxConstraint.gridy = 0;
        checkBoxConstraint.insets = new Insets(0, 85, 0, 0);
        labelsPanel.add(abetCheckBox, checkBoxConstraint); 
                
        return labelsPanel;
    }
    
    /**
     * Creates an array of 15 text fields for the user's answers
     */
    private void createTextFields()
    {
        rightTextFields = new JTextField[numAnswers];
        rightPanels = new JPanel[numAnswers];        
        leftTextFields = new JTextField[numAnswers];
        leftPanels = new JPanel[numAnswers];
        for (int i = 0; i < numAnswers; i++)
        {
            rightTextFields[i] = new JTextField();
            rightTextFields[i].setPreferredSize(new Dimension(175, 25));
            rightPanels[i] = new JPanel();
            rightPanels[i].add(rightTextFields[i]);
            
            leftTextFields[i] = new JTextField();
            leftTextFields[i].setPreferredSize(new Dimension(175, 25));
            leftPanels[i] = new JPanel();
            leftPanels[i].add(leftTextFields[i]);
            // only display the first four text fields upon initialization
            if (i > 3)
            {
                rightPanels[i].setVisible(false);
                leftPanels[i].setVisible(false);
            }
        }       
    }
    
    /**
     * Places each text field panel in 15 panels using GridBagLayout
     */
    private void rowPanels()
    {
        rowPanels = new JPanel[numAnswers];
        leftConstraints = new GridBagConstraints[numAnswers];
        rightConstraints = new GridBagConstraints[numAnswers];
        for (int i = 0; i < numAnswers; i++)
        {
            rowPanels[i] = new JPanel(new GridBagLayout());
            leftConstraints[i] = new GridBagConstraints();
            rightConstraints[i] = new GridBagConstraints();
        
            leftConstraints[i].fill = GridBagConstraints.HORIZONTAL;
            leftConstraints[i].gridx = 0;
            leftConstraints[i].gridy = 0;
            leftConstraints[i].insets = new Insets(0, 0, 0, 19);
            rowPanels[i].add(leftPanels[i], leftConstraints[i]);
            
            rightConstraints[i].fill = GridBagConstraints.HORIZONTAL;
            rightConstraints[i].gridx = 1;
            rightConstraints[i].gridy = 0;
            rowPanels[i].add(rightPanels[i], rightConstraints[i]);            
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
                
                // show the first four row panels upon initialization 
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
        GridBagConstraints addButtonConstraint = new GridBagConstraints();
        
        answerButtonPanel = buttonPanel();
        answerButtonPanel.setVisible(false);
        buttonPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonPanelConstraint.gridx = 0;
        buttonPanelConstraint.gridy = 15;
        answersPanel.add(answerButtonPanel, buttonPanelConstraint);
        
        JPanel addButton = addButtonPanel();
        addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        addButtonConstraint.gridx = 0;
        addButtonConstraint.gridy = 16;
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
        addConstraint.insets = new Insets(0, 210, 0, 2);
        buttonPanel.add(addButton, addConstraint);
        
        removeConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeConstraint.gridx = 1;
        removeConstraint.gridy = 0;
        buttonPanel.add(removeButton, removeConstraint);
        
        addButton.addActionListener((ActionEvent e) -> { 
            addAnswer();          
        });
        
        removeButton.addActionListener((ActionEvent e) -> { 
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
                    MatchingQuestion matchingQuestion = new MatchingQuestion(questionLabel, floatPoints);
                    matchingQuestion.setLabel(new Label(questionString));
                    addMatchingQuestion(matchingQuestion);
                    
                    mainFrame.dispose();
                }
                catch(NumberFormatException n) {}            
            }
        });
        
        return buttonPanel;
    }
     
    /**
     * Adds two text fields to the panel
     */ 
    private void addAnswer()
    {
        for (int i = 0; i < numAnswers; i++)
        {
            if (leftPanels[i].isVisible() == false)
            {
                // make the components visible
                leftPanels[i].setVisible(true);
                rightPanels[i].setVisible(true);
                // handle the card layout panels if i <= 4
                if (i < 4)
                {
                    // show the row panel (text fields) at index i 
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
     * Removes two text fields from the panel
     */
    private void removeAnswer()
    {
        // loop until a non-visible text field is found
        int index = 0;
        for (int i = 0; i < numAnswers; i++)
        {
            if (leftPanels[i].isVisible() == false)
            {
                index = i - 1;
                break;
            }
            // if the maximum amount of answers are displayed on the panel
            // remove the last one 
            else
            {
                leftPanels[14].setVisible(false);
                rightPanels[14].setVisible(false);
                leftTextFields[14].setText(null);
                rightTextFields[14].setText(null);
            }
        }
        
        // make this components non-visible
        leftPanels[index].setVisible(false);
        rightPanels[index].setVisible(false);
        leftTextFields[index].setText(null);
        rightTextFields[index].setText(null);
        if (index < 4)
        {
            answerButtonPanel.setVisible(false);
            // hide the row panel (text fields) at index i     
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
     * Adds a matching question to a quiz/bank
     * 
     * @param question the question to be added
     */
    private void addMatchingQuestion(MatchingQuestion question)
    {
        for (int i = 0; i < numAnswers; i++)
        {
            // get the answers
            String leftAnswer = leftTextFields[i].getText();
            String rightAnswer = rightTextFields[i].getText();
            
            // check to see if answer fields are empty
            boolean leftFieldEmpty = leftAnswer.isEmpty();
            boolean rightFieldEmpty = rightAnswer.isEmpty();
            // if the text fields aren't empty and they're visible add the answer to the quiz
            if ((leftFieldEmpty == false) && (rightFieldEmpty == false) && (leftPanels[i].isVisible() == true))
            {
                question.addAnswer(new MatchingAnswer(i, leftAnswer, rightAnswer));
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
