package io.github.csgroup.quizmaker.ui;

import javax.swing.JComponent;
import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.DefaultListModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 * Creates the panel that allows the user to create quizzes
 * 
 * @author Emily Palmer
 */
public class QuizPanel extends JComponent
{
    private final Project quizProject;
    private DefaultTableModel model;
    private JPanel tablePanel;
    
    public QuizPanel(Project currentQuizProject)
    {
        quizProject = currentQuizProject;
        quizzesPanel();
    }
        
    /**
     * Creates the panel that contains the list of quizzes and "Details" tab and 
     * "Quizzes" tab
     */
    private void quizzesPanel()
    {                
        JPanel listPanel = quizListPanel();
        JTabbedPane quizTabs = createTabs();        
            
        // contains listPanel and quizTabs
        this.setLayout(new GridBagLayout());
        GridBagConstraints listConstraint = new GridBagConstraints();
        GridBagConstraints tabsConstraint = new GridBagConstraints();
            
        // places listPanel on the left side of the component
        listConstraint.fill = GridBagConstraints.HORIZONTAL;
        listConstraint.gridx = 0;
        listConstraint.gridy = 0;
        listConstraint.insets = new Insets(6, 0, 0, 0);
        this.add(listPanel, listConstraint); 
           
        // places quizTabs on the right side of the component
        tabsConstraint.fill = GridBagConstraints.HORIZONTAL;
        tabsConstraint.gridx = 1;
        tabsConstraint.gridy = 0;
        this.add(quizTabs, tabsConstraint); 
    }
    
    /**
     * Creates and sets the look and feel of the "Details" and "Questions" tabs
     * 
     * @return the "Details" and "Questions" tabs
     */
    private JTabbedPane createTabs()
    {
        JPanel details = detailsPanel();
        JPanel questions = questionsPanel();
                
        JTabbedPane quizTabs = new JTabbedPane();                
        quizTabs.addTab("Details", details);
        quizTabs.addTab("Questions", questions);
        // setting the looks and feel of the tabs
        quizTabs.setBackgroundAt(0, new Color(237, 237, 237));
        quizTabs.setBackgroundAt(1, new Color(237, 237, 237));
        UIManager.put("TabbedPane.contentAreaColor", new Color(237, 237, 237));
        UIManager.put("TabbedPane.highlight", Color.GRAY);
        quizTabs.setUI(new BasicTabbedPaneUI());
                
        return quizTabs;
    } 
    
    /**
     * Creates the panel that displays the list of quiz names 
     * 
     * @return the quiz list panel
     */
    private JPanel quizListPanel()
    {
        JLabel quizLabel = new JLabel("Quizzes");
            
        // temporary list until the quiz functionality is implemented
        DefaultListModel<String> quizNameList = new DefaultListModel();
        // ListModel<Quiz> quizNameList = quizProject.getQuizModel();
        JList quizList = new JList(quizNameList);  
        JScrollPane quizScrollPane = new JScrollPane(quizList);
        quizScrollPane.setPreferredSize(new Dimension(190, 415));
            
        // contains quizLabel and quizScrollPane
        JPanel listPanel = new JPanel(new GridBagLayout());
        GridBagConstraints quizLabelConstraint = new GridBagConstraints();
        GridBagConstraints quizListConstraint = new GridBagConstraints();
              
        // places quizLabel at the top of listPanel
        quizLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        quizLabelConstraint.gridx = 0;
        quizLabelConstraint.gridy = 0;
        listPanel.add(quizLabel, quizLabelConstraint);
            
        // places quizScrollPane below quizScrollPane
        quizListConstraint.fill = GridBagConstraints.HORIZONTAL;
        quizListConstraint.gridx = 0;
        quizListConstraint.gridy = 1;
        listPanel.add(quizScrollPane, quizListConstraint); 
                
        return listPanel;
    }
    
    /**
     * Creates the panel that allows the user to enter in the the description/
     * instructions of the quiz
     * 
     * @return the description panel
     */
    private JPanel descriptionPanel()
    {
        JTextField quizNameField = new JTextField("Unamed Quiz");  
        quizNameField.setPreferredSize(new Dimension(200, 25));
        JPanel nameFieldPanel = new JPanel();
        nameFieldPanel.add(quizNameField);
        
        JLabel descriptionLabel = new JLabel("Quiz Description:");
        JTextArea descriptionTextArea = new JTextArea();
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        JScrollPane textScrollPane = new JScrollPane(descriptionTextArea);
        textScrollPane.setPreferredSize(new Dimension(350, 150));
              
        // contains nameFieldPanel, descriptionLabel, and textScrollPane
        JPanel quizDescPanel = new JPanel(new GridBagLayout());
        GridBagConstraints nameFieldConstraint = new GridBagConstraints();
        GridBagConstraints descLabelConstraint = new GridBagConstraints();
        GridBagConstraints textAreaConstraint = new GridBagConstraints();
        
        // places nameFieldPanel at the top of quizDescPanel
        nameFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        nameFieldConstraint.gridx = 0;
        nameFieldConstraint.gridy = 0;
        nameFieldConstraint.insets = new Insets(0, 0, 3, 180);
        quizDescPanel.add(nameFieldPanel, nameFieldConstraint); 
        
        // places descriptionLabel below nameFieldPanel
        descLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        descLabelConstraint.gridx = 0;
        descLabelConstraint.gridy = 1;
        descLabelConstraint.insets = new Insets(0, 5, 3, 0);
        quizDescPanel.add(descriptionLabel, descLabelConstraint);
        
        // places textScrollPane below descriptionLabel
        textAreaConstraint.fill = GridBagConstraints.HORIZONTAL;
        textAreaConstraint.gridx = 0;
        textAreaConstraint.gridy = 2;
        textAreaConstraint.insets = new Insets(0, 5, 0, 0);
        quizDescPanel.add(textScrollPane, textAreaConstraint); 
        
        return quizDescPanel;                
    }
    
    /** 
     * Creates the table panel that displays the list of questions banks, their points, 
     * number of questions, and number of questions excluded from the quiz
     * 
     * @return quiz bank table panel
     */
    public JPanel tablePanel()
    {
        String [] columnHeaders = {"Name", "Points", "Questions", "Questions Excluded"};        
        model = new DefaultTableModel(columnHeaders, 8);
        JTable bankTable = new JTable(model);
        TableColumnModel columnModel = bankTable.getColumnModel();
        // the preferred width of each column in the JTable
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(30);
        columnModel.getColumn(2).setPreferredWidth(30);
                
        // JScrollPane for the JTable dataTable
        JScrollPane tableScrollPane = new JScrollPane(bankTable);
        tableScrollPane.setPreferredSize(new Dimension(450, 140));
        tablePanel = new JPanel();
        tablePanel.add(tableScrollPane);
                
        return tablePanel;     
                
    }
    
    public void populateBankTable(String bank)
    {
        //System.out.println("made it here");
        
        List<QuestionBank> quizBankNameList = quizProject.getQuestionBanks();
        //ListModel<QuestionBank> quizBankNameList = quizProject.getBankModel();
        
        // use a for each loop here 
        int nameColumn = 0;
        int row = 1;

            model.setValueAt(bank, row, nameColumn);
            //System.out.println("loop");
            //System.out.println("name column: " + nameColumn);
            //System.out.println("row number: " + row)
    }
    
    /**
     * Creates the button panel that allows the user to add or remove question banks
     * from a quiz 
     * 
     * @return button panel 
     */
    private JPanel buttonPanel()
    {
        JButton addButton = new JButton("+");
        JButton removeButton = new JButton("-");
        
        // contains addButton and removeButton
        JPanel tableButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints addButtonConstraint = new GridBagConstraints();
        GridBagConstraints removeButtonConstraint = new GridBagConstraints();
        
        // places addButton on the left side of tableButtonPanel
        addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        addButtonConstraint.gridx = 0;
        addButtonConstraint.gridy = 0;
        tableButtonPanel.add(addButton, addButtonConstraint);
        
        // places removeButton on the right side of tableButtonPanel
        removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeButtonConstraint.gridx = 1;
        removeButtonConstraint.gridy = 0;
        tableButtonPanel.add(removeButton, removeButtonConstraint);
        
        // listens for when addButton is selected
        addButton.addActionListener((ActionEvent e) -> {
            BankToQuizDialog addBankDialog = new BankToQuizDialog(quizProject);
            addBankDialog.show();
        });
                
        return tableButtonPanel;
    }
          
    /**
     * Creates the panel that contains the description panel, labels, table, and
     * button panel
     * 
     * @return the panel
     */
    private JPanel detailsPanel()
    {
        JLabel banksLabel = new JLabel("Banks");
            
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bankLabelConstraint = new GridBagConstraints();
        GridBagConstraints descriptionConstraint = new GridBagConstraints();
        GridBagConstraints tableConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
           
        // places the descPanel at the top of detailsPanel
        JPanel descPanel = descriptionPanel();
        descriptionConstraint.fill = GridBagConstraints.HORIZONTAL;
        descriptionConstraint.gridx = 0;
        descriptionConstraint.gridy = 0;
        descriptionConstraint.insets = new Insets(0, 0, 7, 70);
        detailsPanel.add(descPanel, descriptionConstraint);
              
        // places banksLabel below descPanel
        bankLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        bankLabelConstraint.gridx = 0;
        bankLabelConstraint.gridy = 1;
        bankLabelConstraint.insets = new Insets(0, 5, 0, 0);
        detailsPanel.add(banksLabel, bankLabelConstraint); 
        
        // places bankTable below banksLabel
        JPanel bankTable = tablePanel();
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 0;
        tableConstraint.gridy = 2;
        detailsPanel.add(bankTable, tableConstraint);
        
        // places buttonPanel below bankTable
        JPanel buttonPanel = buttonPanel();
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 3;
        buttonConstraint.insets = new Insets(0, 335, 0, 0);
        detailsPanel.add(buttonPanel, buttonConstraint);
             
        detailsPanel.setPreferredSize(new Dimension(476, 409));
                
        return detailsPanel;
    }
               
    private JPanel questionsPanel()
    {
        JPanel questionPanel = new JPanel();
        questionPanel.setPreferredSize(new Dimension(476, 409));
                                                
        return questionPanel;
    }   
}