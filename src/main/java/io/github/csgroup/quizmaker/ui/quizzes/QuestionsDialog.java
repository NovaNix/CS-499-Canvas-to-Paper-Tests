package io.github.csgroup.quizmaker.ui.quizzes;

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
    private final QuestionTable table;
    private JTextField title;
    private QuestionBank bank;
    
    public QuestionsDialog(Quiz newQuiz, QuestionTable questionTable)
    {        
        table = questionTable;
        quiz = newQuiz;
        questionDialog();
    }
    
    public QuestionsDialog(QuestionBank questionBank, QuestionTable questionTable)
    {
        table = questionTable;
        bank = questionBank;
        questionDialog();
    }
    
    
    private void questionDialog()
    {
        questionFrame = new JFrame("Questions");
        questionFrame.setSize(460, 465);
        
        JPanel questionDialogPanel = new JPanel(new GridBagLayout());
        questionPane = new JScrollPane(questionDialogPanel);
        GridBagConstraints questionPanelConstraint = new GridBagConstraints();
        GridBagConstraints answerConstraint = new GridBagConstraints();
          
        JPanel questionPanel = questionPanel();
        questionPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        questionPanelConstraint.gridx = 0;
        questionPanelConstraint.gridy = 0;
        questionDialogPanel.add(questionPanel, questionPanelConstraint);
       
        answerPanel = questionPanels();
        displayAnswerPanel("Select");        
        answerConstraint.fill = GridBagConstraints.HORIZONTAL;
        answerConstraint.gridx = 0;
        answerConstraint.gridy = 1;
        questionDialogPanel.add(answerPanel, answerConstraint);
        
        questionFrame.add(questionPane);
    }
    
    /**
     * Adds and blank panel and the four question types (multiple choice, fill in
     * the blank, matching, and written response) to a CardLayout panel
     * 
     * @return CardLayout panel
     */
    private JPanel questionPanels()
    {      
        cardPanel = new JPanel(new CardLayout());
        
        JPanel containerPanel = new JPanel();
        containerPanel.setPreferredSize(new Dimension(100, 245));
        cardPanel.add(containerPanel, "Select");
        
        if (quiz != null)
        {
            fitbPanel = new FillInTheBlankPanel(questionFrame, question, points, quiz, table, title);
            matchingPanel = new MatchingPanel(questionFrame, question, points, quiz, table, title);
            mcPanel = new MultipleChoicePanel(questionFrame, question, points, quiz, table, title);
            wrPanel = new WrittenResponsePanel(questionFrame, question, points, quiz, table, title);
        }
        if (bank != null)
        {
            fitbPanel = new FillInTheBlankPanel(questionFrame, question, points, bank, table, title);
            matchingPanel = new MatchingPanel(questionFrame, question, points, bank, table, title);
            mcPanel = new MultipleChoicePanel(questionFrame, question, points, bank, table, title);
            wrPanel = new WrittenResponsePanel(questionFrame, question, points, bank, table, title);
        }
        
        fitbContainer = new JPanel();
        fitbContainer.add(fitbPanel);
        cardPanel.add(fitbContainer, "Fill in the Blank");
        
        matchingContainer = new JPanel();
        matchingContainer.add(matchingPanel);
        cardPanel.add(matchingContainer, "Matching");
        
        multipleChoiceContainer = new JPanel();
        multipleChoiceContainer.add(mcPanel);
        cardPanel.add(multipleChoiceContainer, "Multiple Choice");
        
        writtenResponseContainer = new JPanel();
        writtenResponseContainer.add(wrPanel);
        cardPanel.add(writtenResponseContainer, "Written Response");
                
        return cardPanel;
    }
    
            
    private JPanel questionPanel()
    {                  
        question = new JTextArea();
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        JScrollPane questionScrollPane = new JScrollPane(question);
        questionScrollPane.setPreferredSize(new Dimension(380, 130));
        JPanel panePanel = new JPanel();
        panePanel.add(questionScrollPane);
        
        JPanel questionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints infoConstraint = new GridBagConstraints();
        GridBagConstraints questionConstraint = new GridBagConstraints();
        
        JPanel questionInfoPanel = informationPanel();
        infoConstraint.fill = GridBagConstraints.HORIZONTAL;
        infoConstraint.gridx = 0;
        infoConstraint.gridy = 0;
        questionPanel.add(questionInfoPanel, infoConstraint);
        
        questionConstraint.fill = GridBagConstraints.HORIZONTAL;
        questionConstraint.gridx = 0;
        questionConstraint.gridy = 1;
        questionPanel.add(panePanel, questionConstraint);
        
        return questionPanel;
    }
    
    private JPanel informationPanel()
    {
        title = new JTextField("Question");
        title.setPreferredSize(new Dimension(140, 25));
        JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        
        String [] questionTypes = {"Select", "Fill in the Blank", "Matching", "Multiple Choice", "Written Response"};
        JComboBox questionTypesList = new JComboBox(questionTypes);
        
        JLabel pointsLabel = new JLabel("Points: ");
        
        points = new JTextField();
        points.setPreferredSize(new Dimension(35, 25));
        JPanel pointsPanel = new JPanel();
        pointsPanel.add(points);
        
        JPanel informationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints titleConstraint = new GridBagConstraints();
        GridBagConstraints checkBoxConstraint = new GridBagConstraints();
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints pointsFieldConstraint = new GridBagConstraints();
        
        titleConstraint.fill = GridBagConstraints.HORIZONTAL;
        titleConstraint.gridx = 0;
        titleConstraint.gridy = 0;
        informationPanel.add(titlePanel, titleConstraint);
        
        checkBoxConstraint.fill = GridBagConstraints.HORIZONTAL;
        checkBoxConstraint.gridx = 1;
        checkBoxConstraint.gridy = 0;
        checkBoxConstraint.insets = new Insets(0, 0, 0, 24);
        informationPanel.add(questionTypesList, checkBoxConstraint);
        
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 2;
        labelConstraint.gridy = 0;
        informationPanel.add(pointsLabel, labelConstraint);
        
        pointsFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        pointsFieldConstraint.gridx = 3;
        pointsFieldConstraint.gridy = 0;
        informationPanel.add(pointsPanel, pointsFieldConstraint);
               
        questionTypesList.addActionListener((ActionEvent e) -> { 
            String item = (String) questionTypesList.getSelectedItem();
            displayAnswerPanel(item);            
        });
        
        return informationPanel;
    }
    
    /**
     * Shows the 
     * 
     * @param questionType 
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