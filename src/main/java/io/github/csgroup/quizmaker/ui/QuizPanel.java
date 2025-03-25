package io.github.csgroup.quizmaker.ui;

import io.github.csgroup.quizmaker.ui.components.QuestionTable;
import io.github.csgroup.quizmaker.ui.dialogs.BankToQuizDialog;
import io.github.csgroup.quizmaker.ui.dialogs.ExportWordDialog;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.ui.dialogs.QuizQuestionsDialog;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.JList;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Creates the panel that allows the user to create quizzes
 * 
 * @author Emily Palmer
 */
public class QuizPanel extends JComponent
{
    private final Project quizProject;
    private DefaultTableModel model;
    private JTable bankTable;
    private QuestionTable quizTableComponent;
    private JPanel quizDescPanel;
    private JPanel tablePanel;
    private JPanel tableButtonPanel;
    private JPanel quizButtonPanel;
    private JPanel quizActionButtonPanel;
    
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
        quizScrollPane.setPreferredSize(new Dimension(190, 418));
            
        // contains quizLabel and quizScrollPane
        JPanel listPanel = new JPanel(new GridBagLayout());
        GridBagConstraints quizLabelConstraint = new GridBagConstraints();
        GridBagConstraints quizListConstraint = new GridBagConstraints();
        GridBagConstraints buttonsConstraint = new GridBagConstraints();
              
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
        
        // places quizScrollPane below quizScrollPane
        JPanel quizButtons = quizButtonPanel();
        buttonsConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonsConstraint.gridx = 0;
        buttonsConstraint.gridy = 2;
        listPanel.add(quizButtons, buttonsConstraint);
                
        return listPanel;
    }
    
    /**
     * Creates the button panel that allows the user to add or remove a quiz
     * 
     * @return the button panel
     */
    private JPanel quizButtonPanel()
    {
        JButton addQuizButton = new JButton("+");
        JButton removeQuizButton = new JButton("-");
                
        // contains addQuizbutton and removeQuizButton
        quizButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints addButtonConstraint = new GridBagConstraints();
        GridBagConstraints removeButtonConstraint = new GridBagConstraints();
        
        // places addQuizButton on the left side of bankButtonPanel
        addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        addButtonConstraint.gridx = 0;
        addButtonConstraint.gridy = 0;
        addButtonConstraint.ipadx = 55;
        addButtonConstraint.ipady = 2;
        quizButtonPanel.add(addQuizButton, addButtonConstraint); 
        
        // places removeQuizButton on the right side of bankButtonPanel
        removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeButtonConstraint.gridx = 1;
        removeButtonConstraint.gridy = 0;
        removeButtonConstraint.ipadx = 55;
        removeButtonConstraint.ipady = 2;
        quizButtonPanel.add(removeQuizButton, removeButtonConstraint); 
        
        // listens for when addQuizButton has been selected
        addQuizButton.addActionListener((ActionEvent e) -> {
            // make these components visible
            quizTableComponent.setVisible(true);
            tablePanel.setVisible(true);
            quizDescPanel.setVisible(true);
            tableButtonPanel.setVisible(true);
            quizActionButtonPanel.setVisible(true);
        });
                
        return quizButtonPanel;
    }
    
    /**
     * Creates the panel that allows the user to enter in the the description/
     * instructions of the quiz
     * 
     * @return the description panel
     */
    private JPanel descriptionPanel()
    {
        JTextField quizNameField = new JTextField("Unnamed Quiz");  
        quizNameField.setPreferredSize(new Dimension(200, 25));
        JPanel nameFieldPanel = new JPanel();
        nameFieldPanel.add(quizNameField);
        
        JLabel descriptionLabel = new JLabel("Quiz Description:");
        JTextArea descriptionTextArea = new JTextArea();
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        JScrollPane textScrollPane = new JScrollPane(descriptionTextArea);
        textScrollPane.setPreferredSize(new Dimension(360, 150));
              
        // contains nameFieldPanel, descriptionLabel, and textScrollPane
        quizDescPanel = new JPanel(new GridBagLayout());
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
        descLabelConstraint.insets = new Insets(0, 5, 0, 0);
        quizDescPanel.add(descriptionLabel, descLabelConstraint);
        
        // places textScrollPane below descriptionLabel
        textAreaConstraint.fill = GridBagConstraints.HORIZONTAL;
        textAreaConstraint.gridx = 0;
        textAreaConstraint.gridy = 2;
        textAreaConstraint.insets = new Insets(0, 5, 0, 0);
        quizDescPanel.add(textScrollPane, textAreaConstraint); 
        
        quizDescPanel.setVisible(false);
        
        return quizDescPanel;                
    }
    
    /** 
     * Creates the table panel that displays the list of questions banks, their points, 
     * number of questions, and number of questions excluded from the quiz
     * 
     * @return quiz bank table panel
     */
    private JPanel tablePanel()
    {   
        JLabel banksLabel = new JLabel("Banks");
       
        String [] columnHeaders = {"Name", "Points", "Questions", "Questions Excluded"};        
        model = new DefaultTableModel(columnHeaders, 8);
        bankTable = new JTable(model);
        TableColumnModel columnModel = bankTable.getColumnModel();
        // the preferred width of each column in the JTable
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(30);
        columnModel.getColumn(2).setPreferredWidth(30);
                        
        // JScrollPane for the JTable dataTable
        JScrollPane tableScrollPane = new JScrollPane(bankTable);
        tableScrollPane.setPreferredSize(new Dimension(450, 142));
        
        // contains banksLabel and tableScrollPane
        tablePanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints tableConstraint = new GridBagConstraints();
        
        // places addButton on the left side of tableButtonPanel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        tablePanel.add(banksLabel, labelConstraint);
        
        // places addButton on the left side of tableButtonPanel
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 0;
        tableConstraint.gridy = 1;
        tableConstraint.insets = new Insets(0, 0, 2, 0);
        tablePanel.add(tableScrollPane, tableConstraint);
                        
        tablePanel.setVisible(false);
                                       
        return tablePanel;                     
    }
                    
    /**
     * Creates the button panel that allows the user to add or remove question banks
     * from a quiz 
     * 
     * @return button panel 
     */
    private JPanel tableButtonPanel()
    {
        JButton addButton = new JButton("+");
        JButton removeButton = new JButton("-");
        
        // contains addButton and removeButton
        tableButtonPanel = new JPanel(new GridBagLayout());
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
        
        tableButtonPanel.setVisible(false);
        
        // listens for when addButton is selected
        addButton.addActionListener((ActionEvent e) -> {
            // show the dialog that allows the user to add a question bank to a 
            // quiz
            BankToQuizDialog addBankDialog = new BankToQuizDialog(quizProject, model);
            addBankDialog.show();
        });
                
        return tableButtonPanel;
    }
          
    /**
     * Creates the panel that contains the description panel, panel labels, table, and
     * button panels
     * 
     * @return the panel
     */
    private JPanel detailsPanel()
    {
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints descriptionConstraint = new GridBagConstraints();
        GridBagConstraints tableConstraint = new GridBagConstraints();
        GridBagConstraints bankButtonConstraint = new GridBagConstraints();
        GridBagConstraints exportGenConstraint = new GridBagConstraints();
           
        // places the descPanel at the top of detailsPanel
        JPanel descPanel = descriptionPanel();
        descriptionConstraint.fill = GridBagConstraints.HORIZONTAL;
        descriptionConstraint.gridx = 0;
        descriptionConstraint.gridy = 0;
        descriptionConstraint.insets = new Insets(0, 0, 7, 70);
        detailsPanel.add(descPanel, descriptionConstraint);
        
        // places bankTable below banksLabel
        JPanel table = tablePanel();
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 0;
        tableConstraint.gridy = 1;
        detailsPanel.add(table, tableConstraint);
        
        // places addDelQuizButtons below bankTable
        JPanel addDelQuizButtons = tableButtonPanel(); 
        bankButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        bankButtonConstraint.gridx = 0;
        bankButtonConstraint.gridy = 2;
        bankButtonConstraint.insets = new Insets(0, 335, 10, 0);
        detailsPanel.add(addDelQuizButtons, bankButtonConstraint);
        
        // places expportGenButtons below addDelQuizButtons
        JPanel exportGenButtons = quizActionButtonPanel();
        exportGenConstraint.fill = GridBagConstraints.HORIZONTAL;
        exportGenConstraint.gridx = 0;
        exportGenConstraint.gridy = 3;
        detailsPanel.add(exportGenButtons, exportGenConstraint);

        detailsPanel.setPreferredSize(new Dimension(486, 440));
                       
        return detailsPanel;
    }
    
    /**
     * Creates the button panel that contains the button to export a quiz and 
     * generate a quiz
     * 
     * @return the button panel
     */
    private JPanel quizActionButtonPanel()
    {
        JButton exportButton = new JButton("Export");
        JButton generateButton = new JButton("Generate");
        
        // contains exportButton and generateButton
        quizActionButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints exportConstraint = new GridBagConstraints();
        GridBagConstraints generateConstraint = new GridBagConstraints();
        
        // places generateButton on the left side of quizActionButtonPanel
        exportConstraint.fill = GridBagConstraints.HORIZONTAL;
        exportConstraint.gridx = 0;
        exportConstraint.gridy = 0;
        exportConstraint.insets = new Insets(0, 0, 0, 1);
        quizActionButtonPanel.add(generateButton, exportConstraint);
        
        // places exportButton on the right side of quizActionButtonPanel
        generateConstraint.fill = GridBagConstraints.HORIZONTAL;
        generateConstraint.gridx = 1;
        generateConstraint.gridy = 0;
        generateConstraint.insets = new Insets(0, 1, 0, 0);
        quizActionButtonPanel.add(exportButton, generateConstraint);   
        
        quizActionButtonPanel.setVisible(false);
        
        // listens for when exportButton is clicked
        exportButton.addActionListener((ActionEvent e) -> {
            // show the export to word document dialog 
            ExportWordDialog exportDoc = new ExportWordDialog();
            exportDoc.show();
        });
                
        return quizActionButtonPanel;
    }
        
    /** 
     * Creates the table that displays the quiz questions and answers
     * 
     * @return the table panel
     */
    private JPanel questionsPanel()
    {        
        String[] quizTableHeaders = {"Questions", "Answers"};
        int numRows = 24;

        quizTableComponent = new QuestionTable(quizTableHeaders, numRows);
        quizTableComponent.setTableSize(485, 431);      
        quizTableComponent.setTableRowHeight(17);
        quizTableComponent.setVisible(false);
        
        JPanel quizTablePanel = new JPanel();
        quizTablePanel.add(quizTableComponent);
        quizTablePanel.setPreferredSize(new Dimension(486, 440));
        
        JTable quizTable = quizTableComponent.getTable();     
        quizTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {                
                if (e.getClickCount() == 2)
                {
                    QuizQuestionsDialog questionDialog = new QuizQuestionsDialog();
                    questionDialog.show();
                    System.out.println("clicked");
                }        
            }        
        });

        return quizTablePanel;
    }   
}