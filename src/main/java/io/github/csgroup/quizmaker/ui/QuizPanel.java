package io.github.csgroup.quizmaker.ui;

import io.github.csgroup.quizmaker.ui.components.QuestionTable;
import io.github.csgroup.quizmaker.ui.dialogs.BankToQuizDialog;
import io.github.csgroup.quizmaker.ui.dialogs.ExportWordDialog;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.quiz.BankSelection;
import io.github.csgroup.quizmaker.ui.dialogs.RemoveQuizDialog;
import io.github.csgroup.quizmaker.ui.quizzes.QuestionsDialog;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension; 
import javax.swing.JScrollPane;
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
import javax.swing.JFrame;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;

/**
 * Creates the panel that allows the user to create quizzes and their questions
 * 
 * @author Emily Palmer
 */
public class QuizPanel extends JComponent
{
    private Project project;
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
    private JFrame removeFrame;
    private JButton addButton;
    private JButton removeButton;
    private JButton generateButton;
    private JTextArea descriptionTextArea;
    private JButton removeQuizButton;
    private Quiz newQuiz;
    private JList quizList;
    private JPanel detailsPanel;
    
    public QuizPanel(Project currentProject)
    {
        project = currentProject;
        quizzesPanel();
    }
    
    public QuizPanel()
    {
        
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
            
        quizNames = project.getQuizModel();
        quizList = new JList(quizNames);  
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
        JPanel listButtons = quizButtonPanel();
        buttonsConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonsConstraint.gridx = 0;
        buttonsConstraint.gridy = 2;
        listPanel.add(listButtons, buttonsConstraint);
        
        // once a quiz has been added select it on quizList
        quizNames.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                int index = (project.getQuizCount()) - 1;
                quizList.setSelectedIndex(index);
                Quiz quiz = project.getQuiz(index);
                String quizName = quiz.getTitle();
                nameTextField.setText(quizName);  

                Label description = quiz.getDescription();
                String stringDescription = description.asText();
                descriptionTextArea.setText(stringDescription);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {}

            @Override
            public void contentsChanged(ListDataEvent e) {}                                             
        });  
                
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
        removeQuizButton = new JButton("-");
        removeQuizButton.setEnabled(false);
                
        // contains addQuizbutton and removeQuizButton
        listButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints addButtonConstraint = new GridBagConstraints();
        GridBagConstraints removeButtonConstraint = new GridBagConstraints();
        
        // places addQuizButton on the left side of bankButtonPanel
        addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        addButtonConstraint.gridx = 0;
        addButtonConstraint.gridy = 0;
        addButtonConstraint.ipadx = 55;
        addButtonConstraint.ipady = 2;
        listButtonPanel.add(addQuizButton, addButtonConstraint); 
        
        // places removeQuizButton on the right side of bankButtonPanel
        removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeButtonConstraint.gridx = 1;
        removeButtonConstraint.gridy = 0;
        removeButtonConstraint.ipadx = 55;
        removeButtonConstraint.ipady = 2;
        listButtonPanel.add(removeQuizButton, removeButtonConstraint); 
        
        // listens for when addQuizButton has been selected and adds the quiz
        addQuizButton.addActionListener((ActionEvent e) -> {
            showComponents(true);
            // reset components
            nameTextField.setText("Unnamed Quiz");
            descriptionTextArea.setText("");
            // create quiz           
            String quizName = nameTextField.getText();
            newQuiz = new Quiz(quizName);
            project.addQuiz(newQuiz);      
            enableComponents(true);
        });
        
        // listens for when a quiz is selected and populates the information for 
        // that quiz
        quizList.addListSelectionListener((ListSelectionEvent e) -> { 
            // show the quiz edit components and enable quiz removal
            showComponents(true); 
            removeQuizButton.setEnabled(true);
            
            int index = quizList.getSelectedIndex();
            
            if (project.getQuizCount() > 0 && (index < (project.getQuizCount() - 1)) && (index != -1))
            {
                Quiz selectedQuiz = project.getQuiz(index);
                Quiz quiz = project.getQuiz(index);
                String quizName = quiz.getTitle();
                nameTextField.setText(quizName);  

                Label description = quiz.getDescription();
                String stringDescription = description.asText();
                descriptionTextArea.setText(stringDescription);                          
            }
            // clear both quiz and bank tables       
            quizTable.clearTable();
            bankTable.clearTable();            
            // populate the bank table with all the question bank information 
            // for the selected quiz
            populateBankTable(project.getQuiz(index));
            // populate the quiz table with the questions and answers of the selected
            // quiz
            populateQuizTable(project.getQuiz(index));  
        });
        
        // listens for when removeQuizButton has been selected and removes the quiz
        removeQuizButton.addActionListener((ActionEvent e) -> {
            int index = quizList.getSelectedIndex();                        
            Quiz removedQuiz = project.getQuiz(index);  
            
            RemoveQuizDialog removeQuizDialog = new RemoveQuizDialog(removedQuiz, project, quizList, detailsPanel);
            removeQuizDialog.show();          
        });
                
        return listButtonPanel;
    }
    
    /**
     * Creates the panel that allows the user to enter in the the description/
     * instructions of the quiz
     * 
     * @return the description panel
     */
    private JPanel descriptionPanel()
    {
        nameTextField = new JTextField("Unnamed Quiz");  
        nameTextField.setPreferredSize(new Dimension(200, 25));
        JPanel nameFieldPanel = new JPanel();
        nameFieldPanel.add(nameTextField);
        
        JLabel descriptionLabel = new JLabel("Quiz Description:");
        descriptionTextArea = new JTextArea();
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        JScrollPane textScrollPane = new JScrollPane(descriptionTextArea);
        textScrollPane.setPreferredSize(new Dimension(360, 150));
              
        // contains nameFieldPanel, descriptionLabel, and textScrollPane
        descriptPanel = new JPanel(new GridBagLayout());
        GridBagConstraints nameFieldConstraint = new GridBagConstraints();
        GridBagConstraints descLabelConstraint = new GridBagConstraints();
        GridBagConstraints textAreaConstraint = new GridBagConstraints();
        
        // places nameFieldPanel at the top of quizDescPanel
        nameFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        nameFieldConstraint.gridx = 0;
        nameFieldConstraint.gridy = 0;
        nameFieldConstraint.insets = new Insets(0, 0, 3, 180);
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
        textAreaConstraint.insets = new Insets(0, 5, 0, 0);
        descriptPanel.add(textScrollPane, textAreaConstraint); 
        
        descriptPanel.setVisible(false);
        
        return descriptPanel;                
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
        int numRows = 7;
        bankTable = new QuestionTable(columnHeaders, numRows);
        bankTable.setTableSize(450, 135);
        bankTable.setColumnWidth(0, 100);
        bankTable.setColumnWidth(1, 30);
        bankTable.setColumnWidth(2, 30);
        
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
        addButton = new JButton("+");
        removeButton = new JButton("-");
        
        // contains addButton and removeButton
        bankButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints addButtonConstraint = new GridBagConstraints();
        GridBagConstraints removeButtonConstraint = new GridBagConstraints();
        
        // places addButton on the left side of tableButtonPanel
        addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        addButtonConstraint.gridx = 0;
        addButtonConstraint.gridy = 0;
        bankButtonPanel.add(addButton, addButtonConstraint);
        
        // places removeButton on the right side of tableButtonPanel
        removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeButtonConstraint.gridx = 1;
        removeButtonConstraint.gridy = 0;
        bankButtonPanel.add(removeButton, removeButtonConstraint);
        
        bankButtonPanel.setVisible(false);
        
        // listens for when addButton is selected
        addButton.addActionListener((ActionEvent e) -> {
            // show the dialog that allows the user to add a question bank to a quiz
            if (project.getBankCount() > 0)
            {
                int index = quizList.getSelectedIndex();
                Quiz quiz = project.getQuiz(index);            
                BankToQuizDialog addBankDialog = new BankToQuizDialog(project, bankTable, quiz);
                addBankDialog.show();
            }
        });
        
        // listens for when addButton is selected
        removeButton.addActionListener((ActionEvent e) -> {
            // if the user selects a row with a bank prompt them to delete it 
            int row = bankTable.getRowSelected();
            if (row >= 0)
            {
                int index = quizList.getSelectedIndex();
                Quiz quiz = project.getQuiz(index);    
                model = bankTable.getModel();
                Object value = model.getValueAt(row, 0);
                if (value != null)
                {
                    removeBankFrame(row, quiz);
                }
            }
        });
                
        return bankButtonPanel;
    }
          
    /**
     * Creates the panel that contains the description panel, panel labels, table, and
     * button panels
     * 
     * @return the panel
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
        containerPanel.setPreferredSize(new Dimension(486, 440));
        containerPanel.add(detailsPanel);
                       
        return containerPanel;
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
        exportButton.setEnabled(false);
        generateButton = new JButton("Generate");
        
        // contains exportButton and generateButton
        quizButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints exportConstraint = new GridBagConstraints();
        GridBagConstraints generateConstraint = new GridBagConstraints();
        
        // places generateButton on the left side of quizActionButtonPanel
        exportConstraint.fill = GridBagConstraints.HORIZONTAL;
        exportConstraint.gridx = 0;
        exportConstraint.gridy = 0;
        exportConstraint.insets = new Insets(0, 0, 0, 1);
        quizButtonPanel.add(generateButton, exportConstraint);
        
        // places exportButton on the right side of quizActionButtonPanel
        generateConstraint.fill = GridBagConstraints.HORIZONTAL;
        generateConstraint.gridx = 1;
        generateConstraint.gridy = 0;
        generateConstraint.insets = new Insets(0, 1, 0, 0);
        quizButtonPanel.add(exportButton, generateConstraint);   
        
        quizButtonPanel.setVisible(false);
        
        // listens for when exportButton is clicked
        exportButton.addActionListener((ActionEvent e) -> {
            ExportWordDialog exportDialog = new ExportWordDialog();
            exportDialog.show();
        });
                
        // listens for when exportButton is clicked
        generateButton.addActionListener((ActionEvent e) -> {
            exportButton.setEnabled(true);     
            String description = descriptionTextArea.getText();
            Label instructions = new Label(description);          
            if (newQuiz == null)
            {
                int index = quizList.getSelectedIndex();
                newQuiz = project.getQuiz(index);
            }            
            // get the name and description of the quiz
            String quizName = nameTextField.getText();
            // set the name and description of the quiz
            newQuiz.setTitle(quizName);
            newQuiz.setDescription(instructions);
            // disable details panel buttons
            enableComponents(false);
        });
                
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
        int numRows = 24;

        quizTable = new QuestionTable(quizTableHeaders, numRows);
        quizTable.setTableSize(485, 431);      
        quizTable.setTableRowHeight(17);
        quizTable.setVisible(false);
        
        JPanel quizTablePanel = new JPanel();
        quizTablePanel.add(quizTable);
        quizTablePanel.setPreferredSize(new Dimension(486, 440));
        
        JTable table = quizTable.getTable();     
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            { 
                if (e.getClickCount() == 2)
                {
                    QuestionsDialog questionDialog = new QuestionsDialog(newQuiz, quizTable);
                    questionDialog.show();
                }        
            }        
        });

        return quizTablePanel;
    } 
    
    /**
     * Creates the button panel that will remove a question bank
     * 
     * @param row the selected row 
     * @param quiz the selected quiz
     * @return the button panel
     */
    private JPanel removeButtonPanel(int row, Quiz quiz)
    {
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        
        // contains yesButton and noButton
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints yesButtonConstraint = new GridBagConstraints();
        GridBagConstraints noButtonConstraint = new GridBagConstraints();
        
        // places yesButton on the left side of buttonPanel
        yesButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        yesButtonConstraint.gridx = 0;
        yesButtonConstraint.gridy = 0;
        yesButtonConstraint.insets = new Insets(0, 0, 0, 1);
        buttonPanel.add(yesButton, yesButtonConstraint);
        
        // places noButton on the right side of buttonPanel
        noButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        noButtonConstraint.gridx = 1;
        noButtonConstraint.gridy = 0;
        noButtonConstraint.insets = new Insets(0, 1, 0, 0);
        buttonPanel.add(noButton, noButtonConstraint);
        
        // listens for when exportButton is clicked
        yesButton.addActionListener((ActionEvent e) -> { 
            // remove row from table
            model.removeRow(row);
            model.addRow(new Object[]{null,null,null,null});
            // remove bank from quiz
            List<BankSelection> banks = quiz.getBankSelections();
            quiz.removeBank(banks.get(row));
            removeFrame.dispose();
        });
        
        // listens for when exportButton is clicked
        noButton.addActionListener((ActionEvent e) -> {
            // close the frame
            removeFrame.dispose();          
        });
        
        return buttonPanel;
    }
    
    /**
     * Creates the frame that prompts the user to delete the selected bank
     * 
     * @param selectedRow row the user selected
     * @param quiz the quiz that the bank is removed from 
     */
    private void removeBankFrame(int selectedRow, Quiz quiz)
    {
        removeFrame = new JFrame();
        removeFrame.setSize(300, 200);
        
        JLabel removeLabel = new JLabel("Delete question bank?");
        
        // contains removeLabel and buttonPanel
        JPanel removePanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        
        // places removeLabel at the top of removePanel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(0, 0, 10, 0);
        removePanel.add(removeLabel, labelConstraint);
        
        // places buttonPanel below removeLabel
        JPanel buttonPanel = removeButtonPanel(selectedRow, quiz);
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 1;
        removePanel.add(buttonPanel, buttonConstraint);
        
        removeFrame.add(removePanel);       
        removeFrame.setLocationRelativeTo(null);
        removeFrame.setVisible(true);
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
     * Enables/disables buttons on the quiz details panel
     * 
     * @param enable controls if the buttons are enabled/disabled
     */
    private void enableComponents(boolean enable)
    {
        generateButton.setEnabled(enable);
        addButton.setEnabled(enable);
        removeButton.setEnabled(enable);        
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
            if (i > bankTable.getRows() - 1)
            {
                bankTable.addEmptyRow();
            }
            
            List<BankSelection> selectedBanks = quiz.getBankSelections();
            QuestionBank bank = selectedBanks.get(i).getBank();
            int totalQuestions = bank.getQuestionCount();
                                
            bankTable.setValue(bank, i, 0);
            bankTable.setValue("holder", i, 1);
            bankTable.setValue(totalQuestions, i, 2); 
            bankTable.setValue("holder", i, 3);            
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
            Question question = quiz.getQuestion(i);
            String answer = question.getAnswerString();
            
            quizTable.setValue(question, i, 0);
            quizTable.setValue(answer, i, 1);
        }
    }
}