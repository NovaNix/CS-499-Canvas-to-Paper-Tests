package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Project;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;

/**
 * Creates a frame that allows the user to enter a name for a new quiz bank
 * 
 * @author Emily Palmer
 */
public class CreateBankDialog
{
    private JFrame newBankFrame;
    private final Project project;
    private JButton addButton;
    
    public CreateBankDialog(Project currentProject)
    {
        project = currentProject;
        createAddBankFrame();
    }
        
    /**
     * Creates a frame that prompts the user to enter a quiz bank name 
     * and then add the bank with a button. Once the button is clicked, 
     * the new quiz bank is displayed on the main dashboard.
     * 
     * @param bankProject used to add the new question bank to the QuestionBank list
     */
    private void createAddBankFrame()
    {                
        newBankFrame = new JFrame();
        newBankFrame.setSize(400, 260);
                
        JLabel instructionLabel = new JLabel("Enter question bank name: ");
        JTextField bankTextField = new JTextField();
        bankTextField.setPreferredSize(new Dimension(250, 22));
        JPanel namePanel = new JPanel();
        namePanel.add(bankTextField);
                
        // contains instructionLabel, namePanel, and addQuizBankButton
        JPanel newBankPanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints textFieldConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
                
        // places intructionLabel at the top of the panel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(0, 5, 0, 0);
        newBankPanel.add(instructionLabel, labelConstraint);
        
        // places bankTextField in the middel of the panel
        textFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        textFieldConstraint.gridx = 0;
        textFieldConstraint.gridy = 1;
        newBankPanel.add(namePanel, textFieldConstraint);
                
        // places addButton to the right of bankTextField
        JButton addBankButton = addButton(bankTextField);
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 1;
        buttonConstraint.gridy = 1;
        newBankPanel.add(addBankButton, buttonConstraint);
                
        // listens for when addButton is clicked
        Document textFieldDocument = bankTextField.getDocument();
        textFieldDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                addButton.setEnabled(true);
            }
            
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String text = bankTextField.getText();
                boolean empty = text.isEmpty();
                
                if (empty == true)
                {
                    addButton.setEnabled(false);
                }                
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {}                 
        });
        
        newBankFrame.add(newBankPanel);          
    }
    
    /**
     * Creates the add button that allows the user to add the new bank
     * 
     * @return the add button
     */
    private JButton addButton(JTextField bankField)        
    {
        addButton = new JButton("Add");
        addButton.setEnabled(false);
        
        // listens for when addButton is clicked
        addButton.addActionListener((ActionEvent e) -> {
            // get the question bank name from bankTextField
            String name = bankField.getText();
            addBank(name);                 
        });     
        
        bindEnterKey(addButton, bankField);

        return addButton;       
    }
    
    /**
     * Binds the enter key to the import functionality 
     * 
     * @param buttonPanel the add button 
     */
    private void bindEnterKey(JButton buttonPanel, JTextField nameField)
    {
        // creates input and action maps for buttonPanel
        InputMap importInputMap = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap importActionMap = buttonPanel.getActionMap();
        
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);       
        importInputMap.put(enter, buttonPanel);
       
        // once the "Enter" key has been pressed import the file
        importActionMap.put(buttonPanel, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the question bank name from bankTextField
                String bankName = nameField.getText();
                addBank(bankName);          
            }
        });
    }
    
    /**
     * Creates a new question bank
     * 
     * @param bankNameField gets the name of the bank
     */
    private void addBank(String bankName)
    {   
        boolean emptyName = bankName.isEmpty();
        if (emptyName == false)
        {
            // create a QuestionBank object for bankName
            QuestionBank newBank = new QuestionBank(bankName);
            // add the QuesionBank object to the QuestionBank list                    
            project.addBank(newBank);
            newBankFrame.dispose();
        }    
    }
        
    /**
     * Controls when and where the frame appears
     * 
     */
    public void show()
    {
        // makes the JFrame appear in the center of the screen
        newBankFrame.setLocationRelativeTo(null);
        // makes the JFrame visible
        newBankFrame.setVisible(true);            
    } 
}