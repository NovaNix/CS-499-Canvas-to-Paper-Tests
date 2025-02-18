package io.github.csgroup.quizmaker.ui;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 * Creates the JPanel that contains the add and remove buttons 
 *  
 * @author Emily Palmer
 */
public class BankButtonPanel {
 
        // button to add a quiz bank
        private JButton addBankButton;
        // button to remove a quiz bank
        private JButton removeBankButton;
        
        /**
         * Creates the add and remove buttons and places them 
         * in a JPanel using the GridBagLayout layout manager
         * 
         * @return the button panel
         */
        public JPanel createBankButtonPanel()
        {           
                // button to add a quiz bank
                addBankButton = new JButton("+");
                // button to remove a quiz bank
                removeBankButton = new JButton("-");
                // disable the removeBankButton until the user selects a quiz bank
                removeBankButton.setEnabled(false);
            
                JPanel buttonPanel = new JPanel(new GridBagLayout());
                GridBagConstraints addButtonConstraint = new GridBagConstraints();
                GridBagConstraints removeButtonConstraint = new GridBagConstraints();
        
                // places addBankButton on the left side of buttonPanel
                addButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                addButtonConstraint.gridx = 0;
                addButtonConstraint.gridy = 0;
                addButtonConstraint.ipadx = 55;
                buttonPanel.add(addBankButton, addButtonConstraint);
        
                // places removeBankButton on the right side of buttonPanel
                removeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
                removeButtonConstraint.gridx = 1;
                removeButtonConstraint.gridy = 0;
                removeButtonConstraint.ipadx = 55;
                buttonPanel.add(removeBankButton, removeButtonConstraint);                                
               
                return buttonPanel;
        }
          
        public JButton getAddBankButton()
        {
                return addBankButton;
        }  
        
        public JButton getRemoveBankButton()
        {
                return removeBankButton;
        }
}
