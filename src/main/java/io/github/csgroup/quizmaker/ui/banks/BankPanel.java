package io.github.csgroup.quizmaker.ui.banks;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;
import io.github.csgroup.quizmaker.ui.dialogs.CreateBankDialog;
import io.github.csgroup.quizmaker.ui.dialogs.RemoveBankDialog;
import io.github.csgroup.quizmaker.ui.Dialogs.QuestionsDialog;

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
import javax.swing.JTable;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

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
        // contains bankNamePanel and questionTable
        this.setLayout(new GridBagLayout());
        GridBagConstraints listConstraint = new GridBagConstraints();
        GridBagConstraints tableConstraint = new GridBagConstraints();
        
        // places bankNamePanel on the left of the component
        JPanel bankNamePanel = listPanel();
        listConstraint.fill = GridBagConstraints.HORIZONTAL;
        listConstraint.gridx = 0;
        listConstraint.gridy = 0;
        listConstraint.insets = new Insets(4, 0, 0, 0);
        this.add(bankNamePanel, listConstraint);
        
        // places questionTable on the right of the component
        JPanel questionTable = bankTable();
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 1;
        tableConstraint.gridy = 0;
        this.add(questionTable, tableConstraint);                
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
        bankScrollPane.setPreferredSize(new Dimension(200, 410));
                
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
        addButtonConstraint.ipadx = 60;
        buttonPanel.add(addBankButton, addButtonConstraint);
        
        // places removeBankButton on the right side of buttonPanel
        removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        removeButtonConstraint.gridx = 1;
        removeButtonConstraint.gridy = 0;
        removeButtonConstraint.ipadx = 60;
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
                removeBankButton.setEnabled(true);           
                bankTable.clearTable();
            
                int num = bankList.getSelectedIndex();
                populateBankTable(num); 
            }      
        });
                                                
        // listens for when removeBankButton is clicked
        removeBankButton.addActionListener((ActionEvent e) -> {
            // the name of the selected quiz bank
            int index = bankList.getSelectedIndex();
            QuestionBank removeBank = project.getBank(index);
            RemoveBankDialog removeBankName = new RemoveBankDialog(removeBank, project, bankList, removeBankButton, bankTable);
            // display the frame that prompts the user if they are sure they 
            // are deleting the correct quiz bank
            removeBankName.show();            
        });             
                                                                
        return buttonPanel;            
    }
    
    /**
     * Creates the table that display the bank's questions and answers
     * 
     * @return 
     */
    private JPanel bankTable()
    {        
        String[] bankTableHeaders = {"Questions", "Answers"};
        int numRows = 27;
                
        bankTable = new QuestionTable(bankTableHeaders, numRows);
        bankTable.setTableSize(478, 455);
        bankTable.setTableRowHeight(16);
        bankTable.setVisible(false);
              
        JPanel bankTablePanel = new JPanel();
        bankTablePanel.add(bankTable);  
        bankTablePanel.setPreferredSize(new Dimension(485, 465));
        
        table = bankTable.getTable();
        
        // if the user double clicks the table show the dialog that allows them 
        // to add a question
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            { 
                if (e.getClickCount() == 2)
                {
                    int index = bankList.getSelectedIndex();
                    QuestionBank bank = project.getBank(index);
                    QuestionsDialog questionDialog = new QuestionsDialog(bank, bankTable);
                    questionDialog.show();
                }        
            }        
        });
                               
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