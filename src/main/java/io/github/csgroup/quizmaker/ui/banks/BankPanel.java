package io.github.csgroup.quizmaker.ui.banks;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;
import io.github.csgroup.quizmaker.ui.dialogs.CreateBankDialog;
import io.github.csgroup.quizmaker.ui.dialogs.QuestionUsageDialog;
import io.github.csgroup.quizmaker.ui.dialogs.RemoveBankDialog;
import io.github.csgroup.quizmaker.ui.dialogs.RemoveQuestionDialog;
import io.github.csgroup.quizmaker.ui.quizzes.QuestionsDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListModel;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import javax.swing.table.DefaultTableModel;

/**
 * Creates the components that allow the user to add and remove question banks 
 * and add and remove bank questions
 * 
 * @author Emily Palmer
 */
public class BankPanel extends JComponent
{  
    private final Project project;
    private ListModel<QuestionBank> bankNames;
    private QuestionTable bankTable;
    private JList bankList;
    private JTable table;
    private JTextField nameField;
    private JPanel bankLabelPanel;
    private JPanel bankTablePanel;
    
    public BankPanel(Project currentProject)
    {
        project = currentProject;
        bankPanel();
    }
    
    /**
     * Creates the component that contains the list of banks and the table of 
     * their questions
     */
    private void bankPanel()
    {
        JPanel containerPanel = new JPanel();
        containerPanel.setPreferredSize(new Dimension(648, 650));
        
        // contains bankNamePanel and questionTable
        this.setLayout(new BorderLayout());
        
        // places bankNamePanel on the left of the component
        JPanel bankNamePanel = listPanel();
        JPanel tablePanel = bankTablePanel();
        containerPanel.add(tablePanel);
          
        var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, bankNamePanel, containerPanel);
		splitPane.setOneTouchExpandable(false);
		splitPane.setDividerLocation(260);
		
		this.add(splitPane, BorderLayout.CENTER);
    }
    
    /**
     * Creates the panel that contains the JList of bank names and the buttons to 
     * add/remove a question bank
     * 
     * @return list and button panel
     */
    private JPanel listPanel()
    {            
        JLabel bankLabel = new JLabel("Banks");
                
        bankNames = project.getBankModel();
        bankList = new JList(bankNames);  
        JScrollPane bankScrollPane = new JScrollPane(bankList);
        bankScrollPane.setPreferredSize(new Dimension(260, 566));
                
        // contains quizBankLabel and bankScrollPane 
        JPanel bankPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bankLabelConstraint = new GridBagConstraints();
        GridBagConstraints bankScrollPaneConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
                
        // places quiBankLabel at the top of the panel
        bankLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        bankLabelConstraint.gridx = 0;
        bankLabelConstraint.gridy = 0;
        bankLabelConstraint.insets = new Insets(5, 0, 0, 0);
        bankPanel.add(bankLabel, bankLabelConstraint);
        
        // places bankScrollPanel at the bottom of the panel
        bankScrollPaneConstraint.fill = GridBagConstraints.HORIZONTAL;
        bankScrollPaneConstraint.gridx = 0;
        bankScrollPaneConstraint.gridy = 1;
        bankPanel.add(bankScrollPane, bankScrollPaneConstraint);
                
        // add and remove button panel
        JPanel buttonPanel = bankButtonPanel(bankList);
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 2;
        bankPanel.add(buttonPanel, buttonConstraint);  
        
        // once a bank has been added select it on bankList
        bankNames.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                int index = (project.getBankCount()) - 1;
                bankList.setSelectedIndex(index);
                bankTable.setVisible(true);
                bankLabelPanel.setVisible(true);
            }
            @Override
            public void intervalRemoved(ListDataEvent e) {}

            @Override
            public void contentsChanged(ListDataEvent e) {}                                             
        });  
        
        return bankPanel;
    }  
        
    /**
     * Creates the button panel that contains the buttons to add and remove
     * a quiz bank.
     *
     * @param currentProject the project that is used to add or remove the 
     * quiz banks
     * @param bankList list of quiz bank names
     * @return the button panel
     */
    private JPanel bankButtonPanel(JList bankList)
    {
        // button to add a quiz bank
        JButton addBankButton = new JButton("+");
        // button to remove a quiz bank
        JButton removeBankButton = new JButton("-");
        // disable the removeBankButton until the user selects a quiz bank
        removeBankButton.setEnabled(false);
          
        // contains addBankButton and removeBankButton
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints addButtonConstraint = new GridBagConstraints();
        GridBagConstraints removeButtonConstraint = new GridBagConstraints();
        
        // places addBankButton on the left side of buttonPanel
        addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        addButtonConstraint.gridx = 0;
        addButtonConstraint.gridy = 0;
        addButtonConstraint.ipadx = 108;
        addButtonConstraint.ipady = 13;
        buttonPanel.add(addBankButton, addButtonConstraint);
        
        // places removeBankButton on the right side of buttonPanel
        removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeButtonConstraint.gridx = 1;
        removeButtonConstraint.gridy = 0;
        removeButtonConstraint.ipadx = 108;
        removeButtonConstraint.ipady = 13;
        buttonPanel.add(removeBankButton, removeButtonConstraint); 
                
        // listens for when addBankButton is clicked 
        addBankButton.addActionListener((ActionEvent e) -> {
            // display the frame that prompts the user to enter a bank name
            CreateBankDialog addbank = new CreateBankDialog(project, bankTablePanel);
            addbank.show();
        });    
                
        // listens for when a quiz bank is selected
        bankList.addListSelectionListener((ListSelectionEvent e) -> {  
            // display the question and answers of the selected bank
            if (bankNames.getSize() > 0)
            {
                bankTable.setVisible(true);
                bankLabelPanel.setVisible(true);
                removeBankButton.setEnabled(true);           
                bankTable.clearTable();
            
                int index = bankList.getSelectedIndex();                
                if ((index <= (project.getBankCount() - 1)) && index != -1)
                {
                    populateBankTable(index); 
                
                    QuestionBank bank = project.getBank(index);
                    String bankName = bank.getTitle();
                    nameField.setText(bankName);
                }
            }      
        });
                                                
        // listens for when removeBankButton is clicked
        removeBankButton.addActionListener((ActionEvent e) -> {
            // the name of the selected quiz bank
            int index = bankList.getSelectedIndex();
            QuestionBank removeBank = project.getBank(index);
            RemoveBankDialog removeBankName = new RemoveBankDialog(removeBank, project, bankList, bankTablePanel);
            // display the frame that prompts the user if they are sure they 
            // are deleting the correct quiz bank
            removeBankName.show();            
        });             
                                                                
        return buttonPanel;            
    }
    
    /**
     * Creates the table that display the bank's questions and answers
     * 
     * @return the table panel
     */
    private JPanel bankTable()
    {        
        String[] bankTableHeaders = {"Questions", "Answers"};
        int numRows = 33;
                
        bankTable = new QuestionTable(bankTableHeaders, numRows);
        bankTable.setTableSize(654, 572);
        bankTable.setTableRowHeight(16);
        bankTable.setVisible(false);
              
        JPanel tablePanel = new JPanel();
        tablePanel.add(bankTable); 
        tablePanel.setPreferredSize(new Dimension(655, 573));
        
        table = bankTable.getTable();
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            { 
                int index = bankList.getSelectedIndex();
                QuestionBank bank = project.getBank(index);
                int row = table.getSelectedRow();
                Object value = bankTable.getValue(row, 0);
                
                // if the user double clicks the table show the dialog that allows them 
                // to add a question
                if ((e.getClickCount() == 2) && (value != null))
                {
                    QuestionsDialog questionDialog = new QuestionsDialog(bank, bankTable, true, row);
                    questionDialog.show();
                }    
                if ((e.getClickCount() == 2) && (value == null))
                {
                    QuestionsDialog questionDialog = new QuestionsDialog(bank, bankTable, false, row);
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
                            Question question = bank.getQuestion(row);                        
                            JPopupMenu menu = quizPopUpMenu(bank, question, row);
                            menu.show(e.getComponent(), e.getX(), e.getY());                            
                        }
                        catch(IndexOutOfBoundsException n)
                        {
                            JPopupMenu menu = quizPopUpMenu(bank, null, row);
                            menu.show(e.getComponent(), e.getX(), e.getY());  
                        }
                    }                    
                }               
            }        
        });
                               
        return tablePanel;
    } 
    
    /**
     * Creates the pop up menu that allows the user to add or delete a bank question
     * 
     * @param bank the selected question bank
     * @param question the question to be removed
     * @param row the selected row 
     * @return the pop up menu
     */
    private JPopupMenu quizPopUpMenu(QuestionBank bank, Question question, int row)
    {
        JPopupMenu bankMenu = new JPopupMenu();
        
        JMenuItem addQuestionItem = new JMenuItem("Add question");
        JMenuItem removeQuestionItem = new JMenuItem("Remove question");
        removeQuestionItem.setEnabled(false);
        JMenuItem editQuestionItem = new JMenuItem("Edit Question");
        editQuestionItem.setEnabled(false);
        JMenuItem questionUsage = new JMenuItem("Question Usage");
        questionUsage.setEnabled(false);
        
        bankMenu.add(addQuestionItem);
        bankMenu.add(editQuestionItem);
        bankMenu.add(removeQuestionItem);
        bankMenu.add(questionUsage);
        
        Object value = bankTable.getValue(row, 0);
        // if the user selects a row with a question allow them to delete it 
        if (bank.getQuestionCount() > 0 && (value != null))
        {
            removeQuestionItem.setEnabled(true);
            addQuestionItem.setEnabled(false);
            editQuestionItem.setEnabled(true);
            questionUsage.setEnabled(true);
        }
        // if the user selects an empty row allow them to add a question
        if (bank.getQuestionCount() > 0 && (value == null))
        {
            removeQuestionItem.setEnabled(false);
            addQuestionItem.setEnabled(true);
            editQuestionItem.setEnabled(false);
            questionUsage.setEnabled(false);
        }
        
        // listens for when the "Add question" menu item is clicked
        addQuestionItem.addActionListener((ActionEvent e) -> {
            // display the frame that allows the user to add a quiz question
            QuestionsDialog questionDialog = new QuestionsDialog(bank, bankTable, false, row);
            questionDialog.show();
        });
        
        // listens for when the "Add question" menu item is clicked
        editQuestionItem.addActionListener((ActionEvent e) -> {
            // display the frame that allows the user to add a quiz question
            QuestionsDialog questionDialog = new QuestionsDialog(bank, bankTable, true, row);
            questionDialog.show();
        });
        
        // listens for when the "Remove question" menu item is clicked
        removeQuestionItem.addActionListener((ActionEvent e) -> {
            DefaultTableModel tableModel = bankTable.getModel();
            // display the frame that allows the user to delete a quiz question
            RemoveQuestionDialog remove = new RemoveQuestionDialog(bank, question, tableModel);
            remove.show();
        });
        
        questionUsage.addActionListener((ActionEvent e) -> {
            QuestionUsageDialog dialog = new QuestionUsageDialog(question);
            dialog.show();
        });
        
        return bankMenu;
    }
    
     /**
      * Creates the panel that contains the label for the text field that displays
      * the selected bank
      * 
      * @return panel containing the label and text field
      */
    private JPanel bankLabelPanel()
    {
        JLabel bankLabel = new JLabel("Selected Bank:");
        
        nameField = new JTextField();
        nameField.setFocusable(false);
        nameField.setPreferredSize(new Dimension(190, 23));
        JPanel namePanel = new JPanel();
        namePanel.add(nameField);
        
        // contains bankLabel and namePanel
        bankLabelPanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints nameConstraint = new GridBagConstraints();
        
        // places bankLabel on the left side of bankLabelPanel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        bankLabelPanel.add(bankLabel, labelConstraint);
        
        // places namePanel on the right side of bankLabelPanel
        nameConstraint.fill = GridBagConstraints.HORIZONTAL;
        nameConstraint.gridx = 1;
        nameConstraint.gridy = 0;
        nameConstraint.insets = new Insets(0, 0, 0, 358);
        bankLabelPanel.add(namePanel, nameConstraint);
        
        bankLabelPanel.setVisible(false);
        
        return bankLabelPanel;
    }
    
    /**
     * Creates the panel that contains the bank table and selected bank text field
     * 
     * @return the panel containing the table and text field
     */
    private JPanel bankTablePanel()
    { 
        // contains labelPanel and tablePanel
        bankTablePanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints tableConstraint = new GridBagConstraints();
        
        // places labelPanel at the top of bankTablePanel
        JPanel labelPanel = bankLabelPanel();
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        bankTablePanel.add(labelPanel, labelConstraint);
        
        // places tablePanel below labelPanel
        JPanel tablePanel = bankTable();
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 0;
        tableConstraint.gridy = 1;
        bankTablePanel.add(tablePanel, tableConstraint);
                
        return bankTablePanel;
    }
               
    /**
     * Adds questions and answers to bankTable
     */
    private void populateBankTable(int index)
    {
        if ((index < bankNames.getSize()) && (index != -1))
        {
            QuestionBank bank = project.getBank(index);
            int count = bank.getQuestionCount();
            for (int i = 0; i < count; i++)
            {
                Question question = bank.get(i);
                String answer = question.getAnswerString();
                bankTable.setValue(question, i, 0);  
                bankTable.setValue(answer, i, 1);
            }   
        } 
    }
}