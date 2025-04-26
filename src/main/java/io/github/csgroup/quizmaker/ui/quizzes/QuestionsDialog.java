package io.github.csgroup.quizmaker.ui.quizzes;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;


/**
 * Creates the frame that allows the user to add quiz/question bank questions
 * 
 * @author Emily Palmer
 */
public class QuestionsDialog 
{
    private JFrame questionFrame;
    private JPanel answerPanel;
    private JPanel cardPanel;
    private JPanel multipleChoiceContainer;
    private JScrollPane questionPane;
    private MultipleChoicePanel mcPanel;
    private MatchingPanel matchingPanel;
    private JPanel matchingContainer;
    private FillInTheBlankPanel fitbPanel;
    private JPanel fitbContainer;
    private WrittenResponsePanel wrPanel;
    private JPanel writtenResponseContainer;
    private JTextArea question;
    private JTextField points;
    private Quiz quiz;
    private JComboBox questionTypesList;
    private final QuestionTable table;
    private JTextField title;
    private QuestionBank bank;
    private int index;
    private boolean editResult;
    
    public QuestionsDialog(Quiz newQuiz, QuestionTable questionTable, boolean edit, int row)
    {        
        table = questionTable;
        quiz = newQuiz;
        editResult = edit;
        index = row;
        
        questionDialog();
        if (editResult == true)
        {
            Question quizQuestion = quiz.getQuestion(index);            
            String quizClassName = (quizQuestion.getClass()).getName();
            editQuestion(quizClassName);
        }
    }
    
    public QuestionsDialog(QuestionBank questionBank, QuestionTable questionTable, boolean edit, int row)
    {
        table = questionTable;
        bank = questionBank;
        editResult = edit;
        index = row;
        
        questionDialog();
        if (editResult == true)
        {
            Question bankQuestion = bank.getQuestion(index);            
            String bankClassName = (bankQuestion.getClass()).getName();
            editQuestion(bankClassName);
        }
    }  
    
    /**
     * Creates the panel that allows the user to enter in questions, question types, 
     * question title, and question point value
     */
    private void questionDialog()
    {
        questionFrame = new JFrame("Questions");
        questionFrame.setSize(490, 475);
        
        // contains questionPanel and answerPanel
        JPanel questionDialogPanel = new JPanel(new GridBagLayout());
        questionPane = new JScrollPane(questionDialogPanel);
        GridBagConstraints questionPanelConstraint = new GridBagConstraints();
        GridBagConstraints answerConstraint = new GridBagConstraints();
          
        // places questionPanel at the top of questionDialogPanel
        JPanel questionPanel = questionPanel();
        questionPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        questionPanelConstraint.gridx = 0;
        questionPanelConstraint.gridy = 0;
        questionDialogPanel.add(questionPanel, questionPanelConstraint);
       
        // places answerPanel below questionPanel
        answerPanel = questionPanels();
        displayAnswerPanel("Multiple Choice");        
        answerConstraint.fill = GridBagConstraints.HORIZONTAL;
        answerConstraint.gridx = 0;
        answerConstraint.gridy = 1;
        questionDialogPanel.add(answerPanel, answerConstraint);
        
        questionFrame.add(questionPane);
    }
    
    /**
     * Adds the four question type panels (multiple choice, fill in
     * the blank, matching, and written response) to a CardLayout panel
     * 
     * @return CardLayout panel
     */
    private JPanel questionPanels()
    {      
        cardPanel = new JPanel(new CardLayout());
        
        // if the user is adding quiz questions call the question type quiz constructors
        if (quiz != null)
        {
            fitbPanel = new FillInTheBlankPanel(questionFrame, question, points, quiz, table, title);
            matchingPanel = new MatchingPanel(questionFrame, question, points, quiz, table, title);
            mcPanel = new MultipleChoicePanel(questionFrame, question, points, quiz, table, title);
            wrPanel = new WrittenResponsePanel(questionFrame, question, points, quiz, table, title);
        }
        // if the user is adding bank questions call the question type bank constructors
        if (bank != null)
        {
            fitbPanel = new FillInTheBlankPanel(questionFrame, question, points, bank, table, title);
            matchingPanel = new MatchingPanel(questionFrame, question, points, bank, table, title);
            mcPanel = new MultipleChoicePanel(questionFrame, question, points, bank, table, title);
            wrPanel = new WrittenResponsePanel(questionFrame, question, points, bank, table, title);
        }
        
        multipleChoiceContainer = new JPanel();
        multipleChoiceContainer.add(mcPanel);
        cardPanel.add(multipleChoiceContainer, "Multiple Choice");
        
        fitbContainer = new JPanel();
        fitbContainer.add(fitbPanel);
        cardPanel.add(fitbContainer, "Fill in the Blank");
        
        matchingContainer = new JPanel();
        matchingContainer.add(matchingPanel);
        cardPanel.add(matchingContainer, "Matching");
        
        writtenResponseContainer = new JPanel();
        writtenResponseContainer.add(wrPanel);
        cardPanel.add(writtenResponseContainer, "Written Response");
                
        return cardPanel;
    }
    
    /**
     * Creates the panel that contains the text area for the users question and 
     * places it in a layout manager with the informationPanel
     * 
     * @return the question panel
     */        
    private JPanel questionPanel()
    {                  
        question = new JTextArea();
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        JScrollPane questionScrollPane = new JScrollPane(question);
        questionScrollPane.setPreferredSize(new Dimension(425, 150));
        JPanel panePanel = new JPanel();
        panePanel.add(questionScrollPane);
        
        // constains questionInfoPanel and panePanel
        JPanel questionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints infoConstraint = new GridBagConstraints();
        GridBagConstraints questionConstraint = new GridBagConstraints();
        
        // places questionInfoPanel at the top of questionPanel
        JPanel questionInfoPanel = informationPanel();
        infoConstraint.fill = GridBagConstraints.HORIZONTAL;
        infoConstraint.gridx = 0;
        infoConstraint.gridy = 0;
        questionPanel.add(questionInfoPanel, infoConstraint);
        
        // places panePanel below questionInfoPanel
        questionConstraint.fill = GridBagConstraints.HORIZONTAL;
        questionConstraint.gridx = 0;
        questionConstraint.gridy = 1;
        questionPanel.add(panePanel, questionConstraint);
        
        return questionPanel;
    }
    
    /**
     * Creates the panel that allows the user to enter in the question title, 
     * select the question type, and enter in the point value
     * 
     * @return 
     */
    private JPanel informationPanel()
    {
        title = new JTextField("Question");
        title.setPreferredSize(new Dimension(140, 25));
        JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        
        String [] questionTypes = {"Multiple Choice", "Fill in the Blank", "Matching", "Written Response"};
        questionTypesList = new JComboBox(questionTypes);
        
        JLabel pointsLabel = new JLabel("Points: ");
        
        points = new JTextField("2");
        points.setPreferredSize(new Dimension(35, 25));
        JPanel pointsPanel = new JPanel();
        pointsPanel.add(points);
        
        // contains titlePanel, questionTypesList, pointsLabel, and pointsPanel
        JPanel informationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints titleConstraint = new GridBagConstraints();
        GridBagConstraints checkBoxConstraint = new GridBagConstraints();
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints pointsFieldConstraint = new GridBagConstraints();
        
        // places titlePanel at the top of informationPanel
        titleConstraint.fill = GridBagConstraints.HORIZONTAL;
        titleConstraint.gridx = 0;
        titleConstraint.gridy = 0;
        informationPanel.add(titlePanel, titleConstraint);
        
        // places questinTypesList to the right of titlePanel
        checkBoxConstraint.fill = GridBagConstraints.HORIZONTAL;
        checkBoxConstraint.gridx = 1;
        checkBoxConstraint.gridy = 0;
        checkBoxConstraint.insets = new Insets(0, 0, 0, 48);
        informationPanel.add(questionTypesList, checkBoxConstraint);
        
        // places pointsLabel to the right of questionTypesList
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 2;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(0, 25, 0, 0);
        informationPanel.add(pointsLabel, labelConstraint);
        
        // places pointsPanel to the right of pointsLabel
        pointsFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        pointsFieldConstraint.gridx = 3;
        pointsFieldConstraint.gridy = 0;
        informationPanel.add(pointsPanel, pointsFieldConstraint);
        
        // listens for when a user selects a question type
        questionTypesList.addActionListener((ActionEvent e) -> { 
            // display the question type panel on the frame
            String item = (String) questionTypesList.getSelectedItem();
            displayAnswerPanel(item);            
        });
        
        return informationPanel;
    }
    
    private void editQuestion(String className)
    {               
        String fillInTheBlank = "io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion";
        String matching = "io.github.csgroup.quizmaker.data.questions.MatchingQuestion";
        String multipleChoice = "io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion";
        String writtenResponse = "io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion";
  
        if (className.equals(fillInTheBlank))
        {
            displayAnswerPanel("Fill in the Blank");
            questionTypesList.setSelectedIndex(1);
            fitbPanel.isEditable(editResult, index);
        }
        if (className.equals(matching))
        {
            displayAnswerPanel("Matching");
            questionTypesList.setSelectedIndex(2);
            matchingPanel.isEditable(editResult, index);
        }
        if (className.equals(multipleChoice))
        {
            displayAnswerPanel("Multiple Choice");
            questionTypesList.setSelectedIndex(0);
            mcPanel.isEditable(editResult, index);
        }
        if (className.equals(writtenResponse))
        {
            displayAnswerPanel("Written Response");
            questionTypesList.setSelectedIndex(3);
            wrPanel.isEditable(editResult, index);
        }
    }
    
    /**
     * Shows a question type panel depending on what they user selected in the 
     * JCombobox
     * 
     * @param questionType the type of question the user selected
     */
    private void displayAnswerPanel(String questionType)
    {
        CardLayout layout = (CardLayout) (cardPanel.getLayout());
        layout.show(cardPanel, questionType);  
    }
           
    /**
     * Controls when and where the frame appears
     */
    public void show()
    {
        questionFrame.setLocationRelativeTo(null);
        questionFrame.setVisible(true);
    }        
}