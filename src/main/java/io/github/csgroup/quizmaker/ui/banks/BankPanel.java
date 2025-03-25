package io.github.csgroup.quizmaker.ui.banks;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;
import io.github.csgroup.quizmaker.ui.dialogs.CreateBankDialog;
import io.github.csgroup.quizmaker.ui.dialogs.RemoveBankDialog;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListModel;
import javax.swing.JComponent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

/**
 * Creates the JPanel that contains the JList of quiz bank names 
 * 
 * @author Emily Palmer
 */
public class BankPanel extends JComponent
{  
    private final Project bankProject;
    private ListModel<QuestionBank> quizBankNameList;
    private QuestionTable questionAnswerTable;
    
    public BankPanel(Project currentProject)
    {
        bankProject = currentProject;
        bankPanel();
    }
    
    private void bankPanel()
    {
        this.setLayout(new GridBagLayout());
        GridBagConstraints listConstraint = new GridBagConstraints();
        GridBagConstraints tableConstraint = new GridBagConstraints();
        
        JPanel bankListPanel = listPanel();
        listConstraint.fill = GridBagConstraints.HORIZONTAL;
        listConstraint.gridx = 0;
        listConstraint.gridy = 0;
        listConstraint.insets = new Insets(4, 0, 0, 0);
        this.add(bankListPanel, listConstraint);
        
        JPanel tablePanel = bankTable();
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 1;
        tableConstraint.gridy = 0;
        this.add(tablePanel, tableConstraint);                
    }
    
    /**
    * Creates the quiz bank names JList and places them along with the 
     * add and remove bank buttons in a JPanel. The JPanel is organized 
     * using the GridBagLayout layout manager
     * 
     * @param currentProject project used to add new quiz bank to Project list
     * @return the panel that holds the list of quiz bank names
     * and add and remove buttons
     */
    private JPanel listPanel()
    {            
        // label for the quiz question banks
        JLabel quizBankLabel = new JLabel("Banks");
                
        // list of the quiz bank names to be displayed on the JList
        quizBankNameList = bankProject.getBankModel();
        JList quizBankList = new JList(quizBankNameList);  
        //JScrollPane for the JList quizBankList
        JScrollPane bankScrollPane = new JScrollPane(quizBankList);
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
        bankPanel.add(quizBankLabel, bankLabelConstraint);
        
        // places bankScrollPanel at the bottom of the panel
        bankScrollPaneConstraint.fill = GridBagConstraints.HORIZONTAL;
        bankScrollPaneConstraint.gridx = 0;
        bankScrollPaneConstraint.gridy = 1;
        bankPanel.add(bankScrollPane, bankScrollPaneConstraint);
                
        // add and remove button panel
        JPanel buttonPanel = bankButtonPanel(quizBankList);
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 2;
        bankPanel.add(buttonPanel, buttonConstraint);  
        
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
            CreateBankDialog addQuizBank = new CreateBankDialog(bankProject);
            addQuizBank.show();
        });    
                
        // listens for when a quiz bank is selected
        bankList.addListSelectionListener((ListSelectionEvent e) -> {
            // once a quiz bank has been selected enable removaBankButton
            removeBankButton.setEnabled(true);
        });
                                                
        // listens for when removeBankButton is clicked
        removeBankButton.addActionListener((ActionEvent e) -> {
            // the name of the selected quiz bank
            QuestionBank removeBankName = (QuestionBank) bankList.getSelectedValue();
            RemoveBankDialog removeBank = new RemoveBankDialog(removeBankName, bankProject);
            // display the frame that prompts the user if they are sure they 
            // are deleting the correct quiz bank
            removeBank.show();                    
        });             
                                                                
        return buttonPanel;            
    }
    
    private JPanel bankTable()
    {        
        String[] bankTableHeaders = {"Questions", "Answers"};
        int numRows = 27;
        questionAnswerTable = new QuestionTable(bankTableHeaders, numRows);
        questionAnswerTable.setTableSize(478, 455);
        questionAnswerTable.setTableRowHeight(16);
        questionAnswerTable.setVisible(false);
              
        JPanel bankTablePanel = new JPanel();
        bankTablePanel.add(questionAnswerTable);  
        bankTablePanel.setPreferredSize(new Dimension(485, 465));
        
        quizBankNameList.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                questionAnswerTable.setVisible(true);                
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {}

            @Override
            public void contentsChanged(ListDataEvent e) {}                                             
        });
        
        
        
               
        return bankTablePanel;
    }  
}