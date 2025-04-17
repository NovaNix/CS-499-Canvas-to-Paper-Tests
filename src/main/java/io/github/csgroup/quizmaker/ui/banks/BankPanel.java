package io.github.csgroup.quizmaker.ui.banks;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;
import io.github.csgroup.quizmaker.ui.dialogs.CreateBankDialog;
import io.github.csgroup.quizmaker.ui.dialogs.RemoveBankDialog;
import io.github.csgroup.quizmaker.ui.dialogs.RemoveQuestionDialog;
import io.github.csgroup.quizmaker.ui.quizzes.QuestionsDialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        containerPanel.setPreferredSize(new Dimension(522, 535));
        
        // contains bankNamePanel and questionTable
        this.setLayout(new GridBagLayout());
        GridBagConstraints listConstraint = new GridBagConstraints();
        GridBagConstraints tableConstraint = new GridBagConstraints();
        
        // places bankNamePanel on the left of the component
        JPanel bankNamePanel = listPanel();
        listConstraint.fill = GridBagConstraints.HORIZONTAL;
        listConstraint.gridx = 0;
        listConstraint.gridy = 0;
        listConstraint.insets = new Insets(25, 3, 0, 0);
        this.add(bankNamePanel, listConstraint);
        
        // places questionTable on the right of the component
        JPanel tablePanel = bankTablePanel();
        containerPanel.add(tablePanel);
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 1;
        tableConstraint.gridy = 0;
        this.add(containerPanel, tableConstraint);                
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
        bankScrollPane.setPreferredSize(new Dimension(215, 455));
                
        // contains quizBankLabel and bankScrollPane 
        JPanel bankPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bankLabelConstraint = new GridBagConstraints();
        GridBagConstraints bankScrollPaneConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
                
        // places quiBankLabel at the top of the panel
        bankLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        bankLabelConstraint.gridx = 0;
        bankLabelConstraint.gridy = 0;
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
        addButtonConstraint.ipadx = 68;
        addButtonConstraint.ipady = 3;
        buttonPanel.add(addBankButton, addButtonConstraint);
        
        // places removeBankButton on the right side of buttonPanel
        removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeButtonConstraint.gridx = 1;
        removeButtonConstraint.gridy = 0;
        removeButtonConstraint.ipadx = 67;
        removeButtonConstraint.ipady = 3;
        buttonPanel.add(removeBankButton, removeButtonConstraint); 
                
        // listens for when addBankButton is clicked 
        addBankButton.addActionListener((ActionEvent e) -> {
            // display the frame that prompts the user to enter a bank name
            CreateBankDialog addbank = new CreateBankDialog(project);
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
                populateBankTable(index); 
                
                QuestionBank bank = project.getBank(index);
                String bankName = bank.getTitle();
                nameField.setText(bankName);
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
        int numRows = 29;
                
        bankTable = new QuestionTable(bankTableHeaders, numRows);
        bankTable.setTableSize(520, 487);
        bankTable.setTableRowHeight(16);
        bankTable.setVisible(false);
              
        JPanel tablePanel = new JPanel();
        tablePanel.add(bankTable); 
        tablePanel.setPreferredSize(new Dimension(521, 490));
        
        table = bankTable.getTable();
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            { 
                int index = bankList.getSelectedIndex();
                QuestionBank bank = project.getBank(index);
                
                // if the user double clicks the table show the dialog that allows them 
                // to add a question
                if (e.getClickCount() == 2)
                {
                    QuestionsDialog questionDialog = new QuestionsDialog(bank, bankTable);
                    questionDialog.show();
                }    
                // if the user right clicks the table allow them to add or delete
                // a question
                if (SwingUtilities.isRightMouseButton(e))
                {
                    int row = table.getSelectedRow();
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
        JPopupMenu quizMenu = new JPopupMenu();
        
        JMenuItem addQuestionItem = new JMenuItem("Add question");
        JMenuItem removeQuestionItem = new JMenuItem("Remove question");
        removeQuestionItem.setEnabled(false);
        
        quizMenu.add(addQuestionItem);
        quizMenu.add(removeQuestionItem);
        
        Object value = bankTable.getValue(row, 0);
        // if the user selects an empty row allow them to add a question
        if ((bank.getQuestionCount() > 0) && (value != null))
        {
            removeQuestionItem.setEnabled(true);
            addQuestionItem.setEnabled(false);
        }
        // if the user selects a row with a question allow them to delete it 
        if ((bank.getQuestionCount() > 0) && (value == null))
        {
            removeQuestionItem.setEnabled(false);
            addQuestionItem.setEnabled(true);
        }
        
        // listens for when the "Add question" menu item is clicked
        addQuestionItem.addActionListener((ActionEvent e) -> {
            // display the frame that allows the user to add a bank question
            QuestionsDialog questionDialog = new QuestionsDialog(bank, bankTable);
            questionDialog.show();
        });
        
        // listens for when the "Remove question" menu item is clicked
        removeQuestionItem.addActionListener((ActionEvent e) -> {
            DefaultTableModel tableModel = bankTable.getModel();
            // display the frame that allows the user to delete a quiz question
            RemoveQuestionDialog remove = new RemoveQuestionDialog(bank, question, tableModel);
            remove.show();
        });
        
        return quizMenu;
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
        nameField.setPreferredSize(new Dimension(190, 25));
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
        nameConstraint.insets = new Insets(0, 0, 0, 233);
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