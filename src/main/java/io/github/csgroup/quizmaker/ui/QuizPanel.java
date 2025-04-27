package io.github.csgroup.quizmaker.ui;

import io.github.csgroup.quizmaker.ui.components.QuestionTable;
import io.github.csgroup.quizmaker.ui.dialogs.BankToQuizDialog;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.quiz.BankSelection;
import io.github.csgroup.quizmaker.ui.dialogs.GenerateDialog;
import io.github.csgroup.quizmaker.ui.dialogs.RemoveBankFromQuizDialog;
import io.github.csgroup.quizmaker.ui.dialogs.*;
import io.github.csgroup.quizmaker.ui.dialogs.RemoveQuizDialog;
import io.github.csgroup.quizmaker.ui.quizzes.QuestionsDialog;

import javax.swing.JComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension; 
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.Document;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

/**
 * Creates the panel that allows the user to add/delete quizzes, their questions, 
 * and question banks
 * 
 * @author Emily Palmer
 */
public class QuizPanel extends JComponent
{
    private final Project project;
    private DefaultTableModel model;
    private QuestionTable bankTable;
    private QuestionTable quizTable;
    private JPanel descriptPanel;
    private JPanel tablePanel;
    private JPanel bankButtonPanel;
    private JPanel listButtonPanel;
    private JPanel quizButtonPanel;
    private ListModel<Quiz> quizNames;
    private JTextField nameTextField;
    private JButton addBankButton;
    private JButton removeBankButton;
    private JButton generateButton;
    private JTextArea descriptionTextArea;
    private JButton addQuizButton;
    private JButton removeQuizButton;
    private Quiz newQuiz;
    private JList quizList;
    private JPanel detailsPanel;
    private JPanel nameFieldPanel;
    private JScrollPane textScrollPane;
    
    public QuizPanel(Project currentProject)
    {
        project = currentProject;
        quizzesPanel();
    }
                
    /**
     * Creates the panel that contains the list of quizzes, "Details" tab, and 
     * "Quizzes" tab
     */
    private void quizzesPanel()
    {             		
        JPanel listPanel = quizListPanel();
        JTabbedPane quizTabs = createTabs(); 
            
        // contains listPanel and quizTabs
        this.setLayout(new BorderLayout());
         
    	var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listPanel, quizTabs);
		splitPane.setOneTouchExpandable(false);
		splitPane.setDividerLocation(260);
		
		this.add(splitPane, BorderLayout.CENTER);
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
        quizTabs.setBackgroundAt(0, new Color(242, 242, 242));
        quizTabs.setBackgroundAt(1, new Color(242, 242, 242));
        UIManager.put("TabbedPane.contentAreaColor", new Color(242, 242, 242));
        UIManager.put("TabbedPane.highlight", new Color(181, 184, 183));
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
            
        quizNames();
        quizList();
        JScrollPane quizScrollPane = new JScrollPane(quizList);
        quizScrollPane.setPreferredSize(new Dimension(260, 566));
            
        // contains quizLabel and quizScrollPane
        JPanel listPanel = new JPanel(new GridBagLayout());
        GridBagConstraints quizLabelConstraint = new GridBagConstraints();
        GridBagConstraints quizListConstraint = new GridBagConstraints();
        GridBagConstraints buttonsConstraint = new GridBagConstraints();
              
        // places quizLabel at the top of listPanel
        quizLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        quizLabelConstraint.gridx = 0;
        quizLabelConstraint.gridy = 0;
        quizLabelConstraint.insets = new Insets(5, 0, 0, 0);
        listPanel.add(quizLabel, quizLabelConstraint);
            
        // places quizScrollPane below quizScrollPane
        quizListConstraint.fill = GridBagConstraints.HORIZONTAL;
        quizListConstraint.gridx = 0;
        quizListConstraint.gridy = 1;
        listPanel.add(quizScrollPane, quizListConstraint); 
        
        // places quizScrollPane below quizScrollPane
        JPanel listButtons = quizButtonPanel();
        buttonsConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonsConstraint.gridx = 0;
        buttonsConstraint.gridy = 2;
        listPanel.add(listButtons, buttonsConstraint);
        
        return listPanel;
    }
    
    /**
     * Implements the ListModel of quiz names and its action listener 
     */
    private void quizNames()
    {
        quizNames = project.getQuizModel();
        
        // once a quiz has been added/imported select it on the JList
        quizNames.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {                    
                // set the selected JList index to the new quiz
                int index = (project.getQuizCount()) - 1;
                quizList.setSelectedIndex(index);
                
                // display the name of the quiz
                Quiz quiz = project.getQuiz(index);
                String quizName = quiz.getTitle();
                nameTextField.setText(quizName);  

                // display the quiz's description
                Label description = quiz.getDescription();
                String stringDescription = description.asText();
                descriptionTextArea.setText(stringDescription);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                // hide panel components and disable the remove button once all 
                // quizzes have been removed
                int quizCount = project.getQuizCount();
                if (quizCount == 0)
                {
                    removeQuizButton.setEnabled(false);
                    quizTable.setVisible(false);
                    showComponents(false);
                }
            }
            @Override
            public void contentsChanged(ListDataEvent e) {}                                             
        });  
    }
    
    /**
     * Implements an event listener for the question bank names ListModel
     */
    private void bankNames()
    {
        ListModel<QuestionBank> banks = project.getBankModel();
        
        // once a bank has been added enable add/remove bank buttons
        banks.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {                
                addBankButton.setEnabled(true);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {                    
                // if there are no question banks disable the add/remove buttons
                int bankCount = project.getBankCount();
                if (bankCount == 0)
                {
                    removeBankButton.setEnabled(false);
                    addBankButton.setEnabled(false);
                }
            }
            @Override
            public void contentsChanged(ListDataEvent e) {}                                             
        });  
    }
    
    /**
     * Creates the JList for the quizListPanel and its event listener
     */
    private void quizList()
    {
        quizList = new JList(quizNames);
        
        // listens for when a quiz is selected and populates the information for 
        // that quiz
        quizList.addListSelectionListener((ListSelectionEvent e) -> { 
            // show the quiz edit components and enable quiz removal
            showComponents(true); 
            removeQuizButton.setEnabled(true);
            
            // validate the selected index
            int index = quizList.getSelectedIndex();           
            if (project.getQuizCount() > 0 && (index <= (project.getQuizCount() - 1)) && (index != -1))
            {
                // display the quiz's name
                Quiz quiz = project.getQuiz(index);
                String quizName = quiz.getTitle();
                nameTextField.setText(quizName);  
                
                // display the quiz's instructions 
                Label description = quiz.getDescription();
                String stringDescription = description.asText();
                descriptionTextArea.setText(stringDescription);  
                
                // clear both quiz and bank tables       
                quizTable.clearTable();
                bankTable.clearTable();            
                // populate the bank table with all the question bank information 
                // for the quiz
                populateBankTable(quiz);
                // populate the quiz table with the questions and answers of the
                // quiz
                populateQuizTable(quiz);  
            }
        });
    }
         
    /**
     * Creates the button panel that allows the user to add or remove a quiz
     * 
     * @return the button panel
     */
    private JPanel quizButtonPanel()
    {
        addQuizButton();
        removeQuizButton();
                
        // contains addQuizbutton and removeQuizButton
        listButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints addButtonConstraint = new GridBagConstraints();
        GridBagConstraints removeButtonConstraint = new GridBagConstraints();
        
        // places addQuizButton on the left side of bankButtonPanel
        addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        addButtonConstraint.gridx = 0;
        addButtonConstraint.gridy = 0;
        addButtonConstraint.ipadx = 108;
        addButtonConstraint.ipady = 13;
        listButtonPanel.add(addQuizButton, addButtonConstraint); 
        
        // places removeQuizButton on the right side of bankButtonPanel
        removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeButtonConstraint.gridx = 1;
        removeButtonConstraint.gridy = 0;
        removeButtonConstraint.ipadx = 108;
        removeButtonConstraint.ipady = 13;
        listButtonPanel.add(removeQuizButton, removeButtonConstraint); 
                       
        return listButtonPanel;
    }
    
    /**
     * Creates the button to add quizzes and its event listener
     */
    private void addQuizButton()
    {
        addQuizButton = new JButton("+");
        
        // listens for when addQuizButton has been selected and adds the quiz
        addQuizButton.addActionListener((ActionEvent e) -> {
            showComponents(true);
            
            // add quiz with a default name
            newQuiz = new Quiz("Unnamed Quiz");
            descriptionTextArea.setText("");
            project.addQuiz(newQuiz);      
        });       
    }
    
    /**
     * Creates the button to remove quizzes and its event listener
     */
    private void removeQuizButton()
    {
        removeQuizButton = new JButton("-");
        removeQuizButton.setEnabled(false);
        
        // listens for when removeQuizButton has been selected
        removeQuizButton.addActionListener((ActionEvent e) -> {
            // get the quiz to be removed 
            int index = quizList.getSelectedIndex();                        
            Quiz removedQuiz = project.getQuiz(index);
            
            // show the dialog that prompts the user to delete the quiz  
            RemoveQuizDialog removeQuizDialog = new RemoveQuizDialog(removedQuiz, project, quizList);
            removeQuizDialog.show();          
        });
    }
        
    /**
     * Creates the panel that allows the user to enter in the the description/
     * instructions of the quiz
     * 
     * @return the description panel
     */
    private JPanel descriptionPanel()
    {
        nameFieldPanel();       
        JLabel descriptionLabel = new JLabel("Quiz Description:");
        descriptionPane();
              
        // contains nameFieldPanel, descriptionLabel, and textScrollPane
        descriptPanel = new JPanel(new GridBagLayout());
        GridBagConstraints nameFieldConstraint = new GridBagConstraints();
        GridBagConstraints descLabelConstraint = new GridBagConstraints();
        GridBagConstraints textAreaConstraint = new GridBagConstraints();
        
        // places nameFieldPanel at the top of quizDescPanel
        nameFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        nameFieldConstraint.gridx = 0;
        nameFieldConstraint.gridy = 0;
        nameFieldConstraint.insets = new Insets(0, 0, 5, 292);
        descriptPanel.add(nameFieldPanel, nameFieldConstraint); 
        
        // places descriptionLabel below nameFieldPanel
        descLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        descLabelConstraint.gridx = 0;
        descLabelConstraint.gridy = 1;
        descLabelConstraint.insets = new Insets(0, 5, 0, 0);
        descriptPanel.add(descriptionLabel, descLabelConstraint);
        
        // places textScrollPane below descriptionLabel
        textAreaConstraint.fill = GridBagConstraints.HORIZONTAL;
        textAreaConstraint.gridx = 0;
        textAreaConstraint.gridy = 2;
        textAreaConstraint.insets = new Insets(0, 5, 5, 90);
        descriptPanel.add(textScrollPane, textAreaConstraint); 
        
        descriptPanel.setVisible(false);
        
        return descriptPanel;                
    }
    
    /**
     * Creates the panel that allows the user to enter in the quiz name
     */
    private void nameFieldPanel()
    {
        nameTextField = new JTextField("Unnamed Quiz");  
        nameTextField.setPreferredSize(new Dimension(220, 25));
        nameFieldPanel = new JPanel();
        nameFieldPanel.add(nameTextField);
        
        // if the name is changed, update the JList value to the new name
        Document textFieldDocument = nameTextField.getDocument();
        textFieldDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                if (project.getQuizCount() > 0)
                {
                    int index = quizList.getSelectedIndex();
                    Quiz quiz = project.getQuiz(index);
                    quiz.setTitle(nameTextField.getText());                    
                }
            }            
            @Override
            public void removeUpdate(DocumentEvent e) {}           
            @Override
            public void changedUpdate(DocumentEvent e) {}                 
        });
    }
    
    /**
     * Creates the scroll pane that allows the user to enter in the quiz's 
     * description
     */
    private void descriptionPane()
    {
        descriptionTextArea = new JTextArea();
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        textScrollPane = new JScrollPane(descriptionTextArea);
        textScrollPane.setPreferredSize(new Dimension(430, 190));
        
        // update the quiz description as the user is typing it in the text area
        Document textAreaDocument = descriptionTextArea.getDocument();
        textAreaDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                if (project.getQuizCount() > 0)
                {
                    int index = quizList.getSelectedIndex();
                    Quiz quiz = project.getQuiz(index);
                    quiz.setDescription(new Label(descriptionTextArea.getText()));                  
                }
            }            
            @Override
            public void removeUpdate(DocumentEvent e) {}           
            @Override
            public void changedUpdate(DocumentEvent e) {}                 
        });
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
       
        String [] columnHeaders = {"Name", "Points", "Questions", "Questions Excluded "};  
        int numRows = 9;
        bankTable = new QuestionTable(columnHeaders, numRows);
        bankTable.setTableSize(550, 207);
        bankTable.setColumnWidth(0, 190);
        bankTable.setColumnWidth(1, 105);
        bankTable.setColumnWidth(2, 105);
        bankTable.setColumnWidth(3, 150);
        
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
        tablePanel.add(bankTable, tableConstraint);
                        
        tablePanel.setVisible(false);
        
        JTable table = bankTable.getTable();  
        // listens for when bankTable is seleted
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            { 
                // if there is a question bank in the row that the user has seleted
                // enable the remove button 
                int row = bankTable.getRowSelected();
                model = bankTable.getModel();
                Object value = model.getValueAt(row, 0);
                if (value != null)
                {
                    removeBankButton.setEnabled(true);
                }       
                if (value == null)
                {
                    removeBankButton.setEnabled(false);
                }
            }        
        });
                                               
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
        addBankButton();
        removeBankButton();
        bankNames();
        
        // contains addButton and removeButton
        bankButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints addButtonConstraint = new GridBagConstraints();
        GridBagConstraints removeButtonConstraint = new GridBagConstraints();
        
        // places addButton on the left side of tableButtonPanel
        addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        addButtonConstraint.gridx = 0;
        addButtonConstraint.gridy = 0;
        addButtonConstraint.insets = new Insets(0, 45, 0, 0);
        bankButtonPanel.add(addBankButton, addButtonConstraint);
        
        // places removeButton on the right side of tableButtonPanel
        removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeButtonConstraint.gridx = 1;
        removeButtonConstraint.gridy = 0;
        bankButtonPanel.add(removeBankButton, removeButtonConstraint);
        
        bankButtonPanel.setVisible(false);
                        
        return bankButtonPanel;
    }
    
    /**
     * Creates the button that allows the user to add a question bank to a quiz
     * and its event listener
     */
    private void addBankButton()
    {
        addBankButton = new JButton("+");
        addBankButton.setEnabled(false);
        
        // listens for when addBankButton is selected
        addBankButton.addActionListener((ActionEvent e) -> {
            // show the dialog that allows the user to add a question bank to a quiz
            int index = quizList.getSelectedIndex();
            Quiz quiz = project.getQuiz(index);            
            BankToQuizDialog addBankDialog = new BankToQuizDialog(project, bankTable, quiz);
            addBankDialog.show();
        });
    }
    
    /**
     * Creates the button that allows the user to remove a question bank to a quiz
     */
    private void removeBankButton()
    {
        removeBankButton = new JButton("-");
        removeBankButton.setEnabled(false);
        
        // listens for when removeBankButton is selected
        removeBankButton.addActionListener((ActionEvent e) -> {
            // if the user selects a row with a bank, show the dialog that 
            // prompts them to delete it 
            int row = bankTable.getRowSelected();
            if (row >= 0)
            {
                int index = quizList.getSelectedIndex();
                Quiz quiz = project.getQuiz(index);    
                model = bankTable.getModel();
                RemoveBankFromQuizDialog remove = new RemoveBankFromQuizDialog(quiz, row, model, removeBankButton);
                remove.show();
            }
        });        
    }
             
    /**
     * Creates the panel that contains the description panel, panel labels, table, and
     * button panels
     * 
     * @return the details panel
     */
    private JPanel detailsPanel()
    {
        JPanel containerPanel = new JPanel();
        detailsPanel = new JPanel(new GridBagLayout());
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
        tableConstraint.insets = new Insets(0, 0, 5, 36);
        detailsPanel.add(table, tableConstraint);
        
        // places addDelQuizButtons below bankTable
        JPanel addDelQuizButtons = tableButtonPanel(); 
        bankButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        bankButtonConstraint.gridx = 0;
        bankButtonConstraint.gridy = 2;
        bankButtonConstraint.insets = new Insets(0, 405, 30, 0);
        detailsPanel.add(addDelQuizButtons, bankButtonConstraint);
        
        // places expportGenButtons below addDelQuizButtons
        JPanel exportGenButtons = exportButtonPanel();
        exportGenConstraint.fill = GridBagConstraints.HORIZONTAL;
        exportGenConstraint.gridx = 0;
        exportGenConstraint.gridy = 3;
        detailsPanel.add(exportGenButtons, exportGenConstraint);

        detailsPanel.setPreferredSize(new Dimension(642, 592));
        containerPanel.setPreferredSize(new Dimension(642, 592));
        containerPanel.add(detailsPanel);
                               
        return containerPanel;
    }
    
    /**
     * Creates the button panel that contains the button to export a quiz and 
     * generate a quiz
     * 
     * @return the button panel
     */
    private JPanel exportButtonPanel()
    {
        //JButton exportButton = new JButton("Export");
        generateButton = new JButton("Generate");
        
        // contains exportButton and generateButton
        quizButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints exportConstraint = new GridBagConstraints();
        
        // places generateButton on the left side of quizActionButtonPanel
        exportConstraint.fill = GridBagConstraints.HORIZONTAL;
        exportConstraint.gridx = 0;
        exportConstraint.gridy = 0;
        exportConstraint.insets = new Insets(0, 0, 0, 1);
        quizButtonPanel.add(generateButton, exportConstraint);
                          
        // listens for when generateButton is clicked
        generateButton.addActionListener((ActionEvent e) -> { 
            int index = quizList.getSelectedIndex();
            Quiz quiz = project.getQuiz(index);
            
            GenerateDialog generate = new GenerateDialog(quiz);
            generate.show();
        });
        
        quizButtonPanel.setVisible(false);
                
        return quizButtonPanel;
    }
        
    /** 
     * Creates the table that displays the quiz questions and answers
     * 
     * @return the table panel
     */
    private JPanel questionsPanel()
    {        
        String[] quizTableHeaders = {"Questions", "Answers"};
        int numRows = 35;

        quizTable = new QuestionTable(quizTableHeaders, numRows);
        quizTable.setTableSize(651, 588);      
        quizTable.setTableRowHeight(16);
        quizTable.setVisible(false);
        
        JPanel quizTablePanel = new JPanel();
        quizTablePanel.add(quizTable);
        quizTablePanel.setPreferredSize(new Dimension(652, 589));
        
        JTable table = quizTable.getTable();  
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            { 
                int index = quizList.getSelectedIndex();
                Quiz quiz = project.getQuiz(index);
                int row = table.getSelectedRow();
                Object value = quizTable.getValue(row, 0);
                
                // if the user double clicks the table show the dialog that allows them 
                // to add a question
                if ((e.getClickCount() == 2) && (value != null))
                {
                    QuestionsDialog questionDialog = new QuestionsDialog(quiz, quizTable, true, row);
                    questionDialog.show();
                }   
                if ((e.getClickCount() == 2) && (value == null))
                {
                    QuestionsDialog questionDialog = new QuestionsDialog(quiz, quizTable, false, row);
                    questionDialog.show();
                } 
                // if the user right clicks the table allow them to add or delete
                // a question
                if (SwingUtilities.isRightMouseButton(e))
                {
                    if (row >= 0)
                    {
                        try
                        {
                            Question question = quiz.getQuestion(row);                        
                            JPopupMenu menu = quizPopUpMenu(quiz, question, row);
                            menu.show(e.getComponent(), e.getX(), e.getY());                            
                        }
                        catch(IndexOutOfBoundsException n)
                        {
                            JPopupMenu menu = quizPopUpMenu(quiz, null, row);
                            menu.show(e.getComponent(), e.getX(), e.getY());  
                        }
                    }                        
                }
            }        
        });

        return quizTablePanel;
    } 
    
    /**
     * Creates the pop up menu that allows the user to add or delete a bank question
     * 
     * @param bank the selected question bank
     * @param question the question to be removed
     * @param row the selected row 
     * @return the pop up menu
     */
    private JPopupMenu quizPopUpMenu(Quiz quiz, Question question, int row)
    {
        JPopupMenu quizMenu = new JPopupMenu();
        
        JMenuItem addQuestionItem = new JMenuItem("Add question");
        JMenuItem removeQuestionItem = new JMenuItem("Remove question");
        removeQuestionItem.setEnabled(false);
        JMenuItem editQuestionItem = new JMenuItem("Edit question");
        editQuestionItem.setEnabled(false);
        JMenuItem questionUsage = new JMenuItem("Question Usage");
        questionUsage.setEnabled(false);
        
        quizMenu.add(addQuestionItem);
        quizMenu.add(editQuestionItem); 
        quizMenu.add(removeQuestionItem);
        quizMenu.add(questionUsage);
        
        Object value = quizTable.getValue(row, 0);
        // if the user selects a row with a question allow them to delete it 
        if (quiz.getQuestionCount() > 0 && (value != null))
        {
            removeQuestionItem.setEnabled(true);
            addQuestionItem.setEnabled(false);
            editQuestionItem.setEnabled(true);
            questionUsage.setEnabled(true);
        }
        // if the user selects an empty row allow them to add a question
        if (quiz.getQuestionCount() > 0 && (value == null))
        {
            removeQuestionItem.setEnabled(false);
            addQuestionItem.setEnabled(true);
            editQuestionItem.setEnabled(false);
            questionUsage.setEnabled(false);
        }
        
        // listens for when the "Add question" menu item is clicked
        addQuestionItem.addActionListener((ActionEvent e) -> {
            // display the frame that allows the user to add a quiz question
            QuestionsDialog questionDialog = new QuestionsDialog(quiz, quizTable, false, row);
            questionDialog.show();
        });
        
        // listens for when the "Add question" menu item is clicked
        editQuestionItem.addActionListener((ActionEvent e) -> {
            // display the frame that allows the user to add a quiz question
            QuestionsDialog questionDialog = new QuestionsDialog(quiz, quizTable, true, row);
            questionDialog.show();
        });
        
        // listens for when the "Remove question" menu item is clicked
        removeQuestionItem.addActionListener((ActionEvent e) -> {
            DefaultTableModel tableModel = quizTable.getModel();
            // display the frame that allows the user to delete a quiz question
            RemoveQuestionDialog remove = new RemoveQuestionDialog(quiz, question, tableModel);
            remove.show();
        });
        
        questionUsage.addActionListener((ActionEvent e) -> {
            QuestionUsageDialog dialog = new QuestionUsageDialog(question);
            dialog.show();
        });
                
        return quizMenu;
    }
    
    /**
     * Sets the visibility of the quiz information components
     * 
     * @param visible controls if the components are visible
     */
    public void showComponents(boolean visible)
    {
        quizTable.setVisible(visible);
        tablePanel.setVisible(visible);
        descriptPanel.setVisible(visible);
        bankButtonPanel.setVisible(visible);
        quizButtonPanel.setVisible(visible);
    }  
    
    /**
     * Populates bankTable with all information about the question banks added
     * to the selected quiz
     * 
     * @param quiz the selected quiz
     */
    private void populateBankTable(Quiz quiz)
    {
        int size = quiz.getBankSelections().size();
        for (int i = 0; i < size; i++)
        {
            // add another row if the bank count exceeds the row count
            if (i > bankTable.getRows() - 1)
            {
                bankTable.addEmptyRow();
            }
            
            List<BankSelection> selectedBanks = quiz.getBankSelections();
            BankSelection selectedBank = selectedBanks.get(i);
            QuestionBank bank = selectedBank.getBank();
            
            int excludedQuestions = selectedBank.getBlockedQuestions().size();
            int totalQuestions = selectedBank.getQuestionCount();            
            float points = selectedBank.getPointsPerQuestion() * (totalQuestions - excludedQuestions);
                                    
            bankTable.setValue(bank, i, 0);
            bankTable.setValue(points, i, 1);
            bankTable.setValue(totalQuestions, i, 2); 
            bankTable.setValue(excludedQuestions, i, 3);            
        }
        
        // if there are no banks disable removeBankButton
        if (size == 0)
        {
            removeBankButton.setEnabled(false);
        }
    }
    
    /**
     * Populates quizTable with the questions and answers of the selected quiz
     * 
     * @param quiz the selected quiz
     */
    public void populateQuizTable(Quiz quiz)
    {
        int questionCount = quiz.getQuestionCount();
        for (int i = 0; i < questionCount; i++)
        {
            // add another row if the question count exceeds the row count
            if (i > quizTable.getRows() - 1)
            {
                quizTable.addEmptyRow();
            }
            
            Question question = quiz.getQuestion(i);
            String answer = question.getAnswerString();
            
            quizTable.setValue(question, i, 0);
            quizTable.setValue(answer, i, 1);
        }
    }
}