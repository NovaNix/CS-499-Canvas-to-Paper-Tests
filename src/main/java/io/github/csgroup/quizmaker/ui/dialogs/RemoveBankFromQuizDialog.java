package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.quiz.BankSelection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Emily Palmer
 */
public class RemoveBankFromQuizDialog 
{
    private final Quiz quiz;
    private final int row;
    private JFrame removeFrame;
    private final DefaultTableModel model;
    private final JButton removeButton;
   
    public RemoveBankFromQuizDialog(Quiz currentQuiz, int selectedRow, DefaultTableModel tableModel, JButton button)
    {        
        quiz = currentQuiz;
        row = selectedRow;
        model = tableModel;
        removeButton = button;
        removeBankFrame();
    }
      
    private void removeBankFrame()
    {
        removeFrame = new JFrame();
        removeFrame.setSize(300, 200);
        
        JLabel removeLabel = new JLabel("Delete question bank?");
        
        // contains removeLabel and buttonPanel
        JPanel removePanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        
        // places removeLabel at the top of removePanel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(0, 0, 10, 0);
        removePanel.add(removeLabel, labelConstraint);
        
        // places buttonPanel below removeLabel
        JPanel buttonPanel = buttonPanel();
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 1;
        removePanel.add(buttonPanel, buttonConstraint);
        
        removeFrame.add(removePanel);       
        removeFrame.setLocationRelativeTo(null);
        removeFrame.setVisible(true);
    }
    
    private JPanel buttonPanel()
    {
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        
        // contains yesButton and noButton
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints yesButtonConstraint = new GridBagConstraints();
        GridBagConstraints noButtonConstraint = new GridBagConstraints();
        
        // places yesButton on the left side of buttonPanel
        yesButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        yesButtonConstraint.gridx = 0;
        yesButtonConstraint.gridy = 0;
        yesButtonConstraint.insets = new Insets(0, 0, 0, 1);
        buttonPanel.add(yesButton, yesButtonConstraint);
        
        // places noButton on the right side of buttonPanel
        noButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        noButtonConstraint.gridx = 1;
        noButtonConstraint.gridy = 0;
        noButtonConstraint.insets = new Insets(0, 1, 0, 0);
        buttonPanel.add(noButton, noButtonConstraint);
        
        // listens for when exportButton is clicked
        yesButton.addActionListener((ActionEvent e) -> { 
            // remove bank from quiz
            List<BankSelection> banks = quiz.getBankSelections();
            quiz.removeBank(banks.get(row));
            
            // remove row from table
            model.removeRow(row);
            model.addRow(new Object[]{null,null,null,null});
            
            // disable button if all banks have been deleted
            List<BankSelection> remainingBanks = quiz.getBankSelections();
            int size = remainingBanks.size();
            if (size == 0)
            {
                removeButton.setEnabled(false);
            }
            
            removeFrame.dispose();
        });
        
        // listens for when exportButton is clicked
        noButton.addActionListener((ActionEvent e) -> {
            // close the frame
            removeFrame.dispose();          
        });
        
        return buttonPanel;
    }   
    
    /**
     * Controls when and where the frame appears
     * 
     */
    public void show()
    {
        // makes the JFrame appear in the center of the screen
        removeFrame.setLocationRelativeTo(null);
        // makes the JFrame visible
        removeFrame.setVisible(true);            
    } 
}
