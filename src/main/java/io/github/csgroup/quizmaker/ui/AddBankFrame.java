/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.csgroup.quizmaker.ui;

import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Project;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Creates a frame that allows the user to enter a name for a new quiz bank
 * 
 * @author Emily Palmer
 */
public class AddBankFrame 
{
        public String data;
    
        /**
         * 
         * @param name sets the name of the quiz bank
         */
        public void setData(String name)
        {
                data = name;
        }
    
        /**
         * 
         * @return data returns the name of the quiz bank
         */
        public String getData()
        {
                return data;
        }
        
        /**
         * This method creates a frame that contains an instruction message, 
         * text field, and create bank button
         * 
         * @return newBankFrame 
         */
        public JFrame createAddBankFrame()
        {
                JFrame newBankFrame = new JFrame();
                /*newBankFrame.setSize(400, 290);
                
                JLabel instructionLabel = new JLabel("Enter quiz bank name: ");
                JTextField bankTextField = new JTextField();
                JButton addQuizBankButton = new JButton("Create");
                
                JPanel newBankPanel = new JPanel(new GridBagLayout());
                GridBagConstraints labelConstraint = new GridBagConstraints();
                GridBagConstraints textFieldConstraint = new GridBagConstraints();
                GridBagConstraints buttonConstraint = new GridBagConstraints();
                
                labelConstraint.fill = GridBagConstraints.HORIZONTAL;
                labelConstraint.gridx = 0;
                labelConstraint.gridy = 0;
                newBankPanel.add(instructionLabel, labelConstraint);
        
                textFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
                textFieldConstraint.gridx = 0;
                textFieldConstraint.gridy = 1;
                newBankPanel.add(bankTextField, textFieldConstraint);
                
                buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
                buttonConstraint.gridx = 1;
                buttonConstraint.gridy = 1;
                newBankPanel.add(addQuizBankButton, buttonConstraint);
        
                addQuizBankButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {                                                
                        data = bankTextField.getText();
                        setData(data);
                        newBankFrame.dispose();                        
                        // add more code here 
                    }
                });     
        
                newBankFrame.add(newBankPanel);                                   
                newBankFrame.setLocationRelativeTo(null);
                // make the JFrame visible
                newBankFrame.setVisible(true); */ 
        
                return newBankFrame;
    }
}
