package io.github.csgroup.quizmaker.ui;

import io.github.csgroup.quizmaker.data.Project;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;

/**
 * Controls and creates the panel that is first seen when the system is started up
 * 
 * @author Emily Palmer
 */
public class BankPanel 
{       
        /**
         * Creates the panel that will show question banks and their 
         * questions and answers
         * 
         * @return the panel that holds the list of quiz bank names, 
         * table of questions and answers, and add and remove buttons
         */
        public JPanel createBankPanel()
        {
                // quiz bank project variable used to add banks to QuestionBank list
                Project bankProject = new Project();
            
                // label for the quiz question banks
                JLabel quizBankLabel = new JLabel("Quiz Banks");
                
                DefaultListModel<String> quizBankNameList = new DefaultListModel<>();
                JList quizBankList = new JList(quizBankNameList);  
                //JScrollPane for the JList quizBankList
                JScrollPane bankScrollPane = new JScrollPane(quizBankList);
                bankScrollPane.setPreferredSize(new Dimension(190, 380));
                
                // button to add a quiz bank
                JButton addBankButton = new JButton("+");
                // button to remove a quiz bank
                JButton removeBankButton = new JButton("-");
                // disable the removeBankButton until the user selects a quiz bank
                removeBankButton.setEnabled(false);
                
                // contains quizBankLabel and bankScrollPane 
                JPanel bankPanel = new JPanel(new GridBagLayout());
                GridBagConstraints bankLabelConstraint = new GridBagConstraints();
                GridBagConstraints bankScrollPaneConstraint = new GridBagConstraints();
                
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
                
                // contains addBankButton and removeBankButton
                JPanel buttonPanel = new JPanel(new GridBagLayout());
                GridBagConstraints addButtonConstraint = new GridBagConstraints();
                GridBagConstraints removeButtonConstraint = new GridBagConstraints();
        
                // places addBankButton on the left side of the panel
                addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                addButtonConstraint.gridx = 0;
                addButtonConstraint.gridy = 0;
                addButtonConstraint.ipadx = 55;
                buttonPanel.add(addBankButton, addButtonConstraint);
        
                // places removeBankButton on the right side of the panel
                removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                removeButtonConstraint.gridx = 1;
                removeButtonConstraint.gridy = 0;
                removeButtonConstraint.ipadx = 55;
                buttonPanel.add(removeBankButton, removeButtonConstraint);
        
                // contains bankPanel and buttonPanel
                JPanel bankButtonPanel = new JPanel(new GridBagLayout());
                GridBagConstraints quizPanelConstraint = new GridBagConstraints();
                GridBagConstraints buttonPanelConstraint = new GridBagConstraints();
        
                // places bankPanel at the top of bankButtonPanel
                addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                quizPanelConstraint.gridx = 0;
                quizPanelConstraint.gridy = 0;
                bankButtonPanel.add(bankPanel, quizPanelConstraint);
        
                // places buttonPanel at the bottom of bankButtonPanel
                buttonPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                buttonPanelConstraint.gridx = 0;
                buttonPanelConstraint.gridy = 1;
                bankButtonPanel.add(buttonPanel, buttonPanelConstraint);
        
                // column headers for dataTable
                String[] columnHeaders = {"Questions", "Answers"};
                // the default number of columns that will be displayed on the panel
                DefaultTableModel model = new DefaultTableModel(columnHeaders, 30);
                JTable dataTable = new JTable(model);
                // the height of each row in the JTable
                dataTable.setRowHeight(17);
                TableColumnModel columnModel = dataTable.getColumnModel();
                // the preferred width of each column in the JTable
                columnModel.getColumn(0).setPreferredWidth(250);
                columnModel.getColumn(1).setPreferredWidth(250);
        
                // JScrollPane for the JTable dataTable
                JScrollPane tableScrollPane = new JScrollPane(dataTable);
        
                // contains dataTable
                JPanel tablePanel = new JPanel(new GridBagLayout());
                GridBagConstraints tableConstraint = new GridBagConstraints();
        
                tableConstraint.fill = GridBagConstraints.HORIZONTAL;
                tableConstraint.gridx = 0;
                tableConstraint.gridy = 0;
                tablePanel.add(tableScrollPane, tableConstraint); 
        
                // contains the bankButton panel and tablePanel
                JPanel containerPanel = new JPanel(new GridBagLayout());
                GridBagConstraints bankPanelConstraint = new GridBagConstraints();
                GridBagConstraints tablePanelConstraint = new GridBagConstraints();
        
                // places bankButtonPanel on the left side of the panel
                bankPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                bankPanelConstraint.gridx = 0;
                bankPanelConstraint.gridy = 0;
                containerPanel.add(bankButtonPanel, bankPanelConstraint);
        
                // places tablePanel on the right side of the panel
                tablePanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                tablePanelConstraint.gridx = 1;
                tablePanelConstraint.gridy = 0;
                containerPanel.add(tablePanel, tablePanelConstraint);
                     
                // listens for when addBankButton is clicked 
                addBankButton.addActionListener((ActionEvent e) -> {
                    // display the frame that prompts the user to enter a bank name
                    AddBankFrame addQuizBank = new AddBankFrame();
                    addQuizBank.createAddBankFrame(bankProject, quizBankNameList);
                });    
                
                // listens for when a quiz bank is selected
                quizBankList.addListSelectionListener((ListSelectionEvent e) -> {
                    // once a quiz bank has been selected enable removaBankButton
                    removeBankButton.setEnabled(true);
                });
                                                
                // listens for when removeBankButton is clicked
                removeBankButton.addActionListener((ActionEvent e) -> {
                    // the name of the selected quiz bank
                    String removeBankName = (String)quizBankList.getSelectedValue();
                    int bankIndex = quizBankNameList.indexOf(removeBankName);
                    // display the frame that ensures that the user is deleting the
                    // correct quiz bank
                    RemoveBankFrame removeBank = new RemoveBankFrame();
                    removeBank.createRemoveBankFrame(removeBankName, bankProject, bankIndex, quizBankNameList);
                });                            
                return containerPanel;
        }    
}
