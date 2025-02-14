package io.github.csgroup.quizmaker.ui;

import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * Creates a JPanel that contains the quiz bank JList, add and remove 
 * buttons, and JTable containing the bank's questions and answers 
 * 
 * 
 * @author Emily Palmer
 */
public class ContainerPanel {
      
        /**
         * Organizes the quiz bank panel and table panel using the 
         * GridBagLayout layout manager
         * 
         * @return JPanel containing the quiz bank list, buttons, and table
         */
        public JPanel createContainerPanel()
        {
                // get the quiz bank panel from the BankPanel class
                BankPanel quizBanks = new BankPanel();
                JPanel quizBankPanel = quizBanks.createBankPanel();
                         
                // get the table panel from the TablePanel class
                JPanel bankTablePanel;
                TablePanel questionAnswerTable = new TablePanel();
                bankTablePanel = questionAnswerTable.createTablePanel();
                
                JPanel containerPanel = new JPanel(new GridBagLayout());
                GridBagConstraints bankPanelConstraint = new GridBagConstraints();
                GridBagConstraints tablePanelConstraint = new GridBagConstraints();
        
                // places bankButtonPanel on the left side of the panel
                bankPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                bankPanelConstraint.gridx = 0;
                bankPanelConstraint.gridy = 0;
                containerPanel.add(quizBankPanel, bankPanelConstraint);
        
                // places tablePanel on the right side of the panel
                tablePanelConstraint.fill = GridBagConstraints.HORIZONTAL;
                tablePanelConstraint.gridx = 1;
                tablePanelConstraint.gridy = 0;
                containerPanel.add(bankTablePanel, tablePanelConstraint);
                                                
                return containerPanel;            
        }
    
}
