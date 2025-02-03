/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.csgroup.quizmaker.ui;

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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * Controls and creates the panel that is first seen when the system is started up
 * 
 * @author Emily Palmer
 */
public class BankPanel 
{
        /**
         * This method creates the panel that will show quiz banks and their 
         * questions and answers
         * 
         * @return containerPanel the panel that holds the list of quiz names, 
         * table of questions and answers, and add and remove buttons
         */
        public JPanel createBankPanel()
        {
                System.out.println("made it here");
                // label that is placed above the list of quiz banks
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
                
                // panel that will contain the JLabel quizBankLabel and JScrollPanel
                // bankScrollPane using GridBagLayout
                JPanel bankPanel = new JPanel(new GridBagLayout());
                GridBagConstraints bankLabelConstraint = new GridBagConstraints();
                GridBagConstraints bankScrollPaneConstraint = new GridBagConstraints();
                
                // add quiBankLabel at the top of the panel
                bankLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
                bankLabelConstraint.gridx = 0;
                bankLabelConstraint.gridy = 0;
                bankPanel.add(quizBankLabel, bankLabelConstraint);
        
                // add bankScrollPanel to the bottom of the panel
                bankScrollPaneConstraint.fill = GridBagConstraints.HORIZONTAL;
                bankScrollPaneConstraint.gridx = 0;
                bankScrollPaneConstraint.gridy = 1;
                bankPanel.add(bankScrollPane, bankScrollPaneConstraint);
                
                // panel that will contain the JButtons addBankButton and 
                // removeBankButton
                /*JPanel buttonPanel = new JPanel(new GridBagLayout());
                GridBagConstraints addButtonConstraint = new GridBagConstraints();
                GridBagConstraints removeButtonConstraint = new GridBagConstraints();
        
                // add addBankButton on the left side of the panel
                addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                addButtonConstraint.gridx = 0;
                addButtonConstraint.gridy = 0;
                addButtonConstraint.ipadx = 55;
                buttonPanel.add(addBankButton, addButtonConstraint);
        
                // add removeBankButton on the right side of the panel
                removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                removeButtonConstraint.gridx = 1;
                removeButtonConstraint.gridy = 0;
                removeButtonConstraint.ipadx = 55;
                buttonPanel.add(removeBankButton, removeButtonConstraint);
        
                // panel that will contain the panels bankPanel and buttonPanel
                JPanel bankButtonPanel = new JPanel(new GridBagLayout());
                GridBagConstraints quizPanelConstraint = new GridBagConstraints();
                GridBagConstraints buttonPanelConstraint = new GridBagConstraints();
        
                // add bankPanel to the top of bankButtonPanel
                addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                quizPanelConstraint.gridx = 0;
                quizPanelConstraint.gridy = 0;
                bankButtonPanel.add(bankPanel, quizPanelConstraint);
        
                // add buttonPanel to the bottom of bankButtonPanel
                buttonPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                buttonPanelConstraint.gridx = 0;
                buttonPanelConstraint.gridy = 1;
                bankButtonPanel.add(buttonPanel, buttonPanelConstraint);
        
                // column headers for the JTable dataTable
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
        
                // panel that will contain dataTable
                JPanel tablePanel = new JPanel(new GridBagLayout());
                GridBagConstraints tableConstraint = new GridBagConstraints();
        
                tableConstraint.fill = GridBagConstraints.HORIZONTAL;
                tableConstraint.gridx = 0;
                tableConstraint.gridy = 0;
                tablePanel.add(tableScrollPane, tableConstraint); */
        
                // panel that will contain the bankButton panel and tablePanel
                JPanel containerPanel = new JPanel(new GridBagLayout());
                /*GridBagConstraints bankPanelConstraint = new GridBagConstraints();
                GridBagConstraints tablePanelConstraint = new GridBagConstraints();
                GridBagConstraints c = new GridBagConstraints();
        
                // add the bankButtonPanel on the left side of the containerPanel
                bankPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                bankPanelConstraint.gridx = 0;
                bankPanelConstraint.gridy = 0;
                containerPanel.add(bankButtonPanel, bankPanelConstraint);
        
                // add the tablePanel on the right side of the containerPanel
                tablePanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                tablePanelConstraint.gridx = 1;
                tablePanelConstraint.gridy = 0;
                containerPanel.add(tablePanel, tablePanelConstraint);
       
                // action listener for when the user clicks the addBankButton
                // to add a new quiz bank
                addBankButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {  
                        // call the method to make the frame appear that 
                        // prompts the user to enter a bank name
                        AddBankFrame addBank = new AddBankFrame();
                        JFrame frame = addBank.createAddBankFrame(); 
                        
                        // window listener for when the 
                        frame.addWindowListener(new WindowListener() 
                        {
                            @Override
                            public void windowOpened(WindowEvent e) {}
                 
                            @Override
                            public void windowClosing(WindowEvent e) {}

                            @Override
                            public void windowClosed(WindowEvent e) 
                            {
                                // get the name of the bank and display it 
                                // on the JList 
                                String data = addBank.getData();
                                quizBankNameList.addElement(data);
                            }

                            @Override
                            public void windowIconified(WindowEvent e) {}
                      
                            @Override
                            public void windowDeiconified(WindowEvent e) {}                        

                            @Override
                            public void windowActivated(WindowEvent e) {}                        

                            @Override
                            public void windowDeactivated(WindowEvent e) {}
                        }); 
                    }
                });   
                
                removeBankButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {                                                
                
                    }
                });   */
                   
                return containerPanel;
        }    
}
