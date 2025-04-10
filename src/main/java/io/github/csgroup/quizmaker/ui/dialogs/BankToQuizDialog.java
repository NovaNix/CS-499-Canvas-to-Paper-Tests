package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.quiz.BankSelection;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.TableColumnModel;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.util.ArrayList;

/**
 * Creates a frame that allows the user to select what bank questions to add/remove 
 * to the quiz 
 * 
 * @author Emily Palmer
 */
public class BankToQuizDialog 
{
    private JFrame bankFrame;
    private final Project project;
    private JComboBox bankList;
    private final QuestionTable table;
    private DefaultTableModel bankModel;
    private JTable questionTable;
    private final ArrayList<Question> excludedQuestions;
    private final Quiz quiz;
    private BankSelection selectedBank;
    
    private JScrollPane tableScrollPane;
    
    public BankToQuizDialog(Project currentProject, QuestionTable bankTable, Quiz currentQuiz)
    {
        this.excludedQuestions = new ArrayList<>();
        project = currentProject;
        table = bankTable;
        quiz = currentQuiz;
        createBankToQuizDialog();
    }
         
    /**
     * Creates frame that contains the list of bank names, the amount of points, 
     * and a table of selected questions
     * 
     */
    private void createBankToQuizDialog()
    {
        bankFrame = new JFrame("Add Bank to Quiz");
        bankFrame.setSize(370, 370);
        
        // contains bankInfoPanel and questionPanel
        JPanel addBankPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bankInfoConstraint = new GridBagConstraints();
        GridBagConstraints questionsConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        
        // places bankInfoPanel at the top of addBankPanel
        JPanel bankInfoPanel = bankInfoPanel();
        bankInfoConstraint.fill = GridBagConstraints.HORIZONTAL;
        bankInfoConstraint.gridx = 0;
        bankInfoConstraint.gridy = 0;
        bankInfoConstraint.insets = new Insets(10, 0, 10, 95);
        addBankPanel.add(bankInfoPanel, bankInfoConstraint);
        
        // places questionPanel below bankInfoPanel
        JPanel questionPanel = excludedPanel();
        questionsConstraint.fill = GridBagConstraints.HORIZONTAL;
        questionsConstraint.gridx = 0;
        questionsConstraint.gridy = 1;
        addBankPanel.add(questionPanel, questionsConstraint);
        
        // places addButtonPanel below bankInfoPanel
        JPanel addButtonPanel = buttonPanel();
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 2;
        addBankPanel.add(addButtonPanel, buttonConstraint);
        
        bankFrame.add(addBankPanel);        
    }
    
    /**
     * Creates a panel that contains the JComboBox of bank names and the text box 
     * containing their questions
     * 
     * @return panel containing bank details
     */
    private JPanel bankInfoPanel()
    {
        JLabel bankLabel = new JLabel("Bank:");
        List<QuestionBank> bankNames = project.getQuestionBanks();
        bankList = new JComboBox(bankNames.toArray());
        bankList.setPreferredSize(new Dimension(180, 20));
        
        float initPoints = getPoints(0);
        
        JLabel pointsLabel = new JLabel ("Points:");
        JTextField pointsTextField = new JTextField();
        pointsTextField.setFocusable(false);
        pointsTextField.setText(Float.toString(initPoints));
        pointsTextField.setPreferredSize(new Dimension(50, 20));
        
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.add(pointsTextField);
        
        // contains bankLabel, bankList, pointsLabel, and textFieldPanel
        JPanel bankInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bankLabelConstraint = new GridBagConstraints();
        GridBagConstraints bankListConstraint = new GridBagConstraints();
        GridBagConstraints pointsLabelConstraint = new GridBagConstraints();
        GridBagConstraints textFieldConstraint = new GridBagConstraints();
        
        // places bankLabel at the top of bankInfoPanel
        bankLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        bankLabelConstraint.gridx = 0;
        bankLabelConstraint.gridy = 0;
        bankInfoPanel.add(bankLabel, bankLabelConstraint);
        
        // places bankList to the right of bankInfoPanel
        bankListConstraint.fill = GridBagConstraints.HORIZONTAL;
        bankListConstraint.gridx = 1;
        bankListConstraint.gridy = 0;
        bankListConstraint.insets = new Insets(0, 7, 0, 0);
        bankInfoPanel.add(bankList, bankListConstraint);
        
        // places pointsLabel below bankList
        pointsLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        pointsLabelConstraint.gridx = 0;
        pointsLabelConstraint.gridy = 1;
        bankInfoPanel.add(pointsLabel, pointsLabelConstraint);
        
        // places textFieldPanel to the right of pointsLabel
        textFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        textFieldConstraint.gridx = 1;
        textFieldConstraint.gridy = 1;
        textFieldConstraint.insets = new Insets(0, 0, 0, 123); 
        bankInfoPanel.add(textFieldPanel, textFieldConstraint);
        
        // listens for when removeBankButton is clicked
        bankList.addActionListener((ActionEvent e) -> {
            // display the total points of the bank
            int index = bankList.getSelectedIndex();
            float bankPoints = getPoints(index);
            pointsTextField.setText(Float.toString(bankPoints));
            
            clearTable();
            populateTable(index); 
        });
                             
        return bankInfoPanel;
    }
    
    private void populateTable(int index)
    {
        QuestionBank bank = project.getBank(index);
        int size = bank.getQuestionCount();
        int rowCount = questionTable.getRowCount();
        
        if (rowCount > size)
        {
            for (int i = 0; i < size; i++)
            {
                bankModel.removeRow(0);
            }
        }
        else if (rowCount < size)
        {
            int addedRows = size - rowCount;
            for (int i = 0; i < addedRows; i++)
            {
                bankModel.addRow(new Object[]{null,null,null,null});
            }
        }                   
        
        for (int i = 0; i < size; i++)
        {
            Question question = bank.get(i);
            questionTable.setValueAt(question, i, 1);
            questionTable.setValueAt(false, i, 0);
        }
    }
     
    private float getPoints(int index)
    {
        int total = 0;
        QuestionBank bank = project.getBank(index);
        int size = bank.getQuestionCount();
        for (int i = 0; i < size; i++)
        {
            Question question = bank.get(i);
            float points = question.getPoints();
            total += points;
        }
        
        return total;
    }
    
    private void clearTable()
    {
        int rows = questionTable.getRowCount();

        for (int i = 0; i < rows; i++)
        {
            questionTable.setValueAt(null, i, 0);
            questionTable.setValueAt(null, i, 1);
        }
    }
       
    /**
     * Creates the table panel that contains bank questions and a button for each 
     * question. The user can select this button to exclude a question
     * 
     * @return table panel
     */
    private JScrollPane bankTable()
    {                
        String[] columnHeaders = {"1", "2"};
        
        QuestionBank bank = project.getBank(0);
        int size = bank.getQuestionCount();
        
        bankModel = new DefaultTableModel(columnHeaders, size) 
        {
            @Override
            public Class<?> getColumnClass(int column)
            {
                switch(column)
                {
                    case 0 -> 
                    {
                        return Boolean.class;
                    }
                    case 1 ->
                    {
                        return Object.class;
                    }
                    default ->
                    {
                        return Object.class;
                    }
                }                    
            }           
        };

        questionTable = new JTable(bankModel);
        questionTable.setTableHeader(null);
        questionTable.setRowHeight(18);

        TableColumnModel columnModel = questionTable.getColumnModel();
        // the preferred width of each column in the JTable
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(280);
                
        // JScrollPane for the JTable table
        tableScrollPane = new JScrollPane(questionTable);
        tableScrollPane.setPreferredSize(new Dimension(320, 200));
        
        populateTable(0);
        
        return tableScrollPane;      
    }
    
    private int excludedQuestionInfo()
    {
        int rows = questionTable.getRowCount();
        
        QuestionBank bank = project.getBank(bankList.getSelectedIndex());        
        selectedBank = new BankSelection(bank, bank.getQuestionCount(), 2.0f);
        quiz.addBank(selectedBank);
        
        for (int i = 0; i < rows; i++)
        {
            Boolean value = (Boolean) questionTable.getValueAt(i, 0);
            if (value == true)
            {
                excludedQuestions.add(bank.get(i));
            }
        }
        
        int finalSize = bank.getQuestionCount() - excludedQuestions.size();
               
        return finalSize;
    }
        
    /**
     * Places the question table along with a label on panel
     * 
     * @return 
     */
    private JPanel excludedPanel()
    {
        JLabel excludedLabel = new JLabel("Excluded:");        
         
        // contains excludedLabel and table
        JPanel excludedPanel = new JPanel(new GridBagLayout());
        GridBagConstraints excludedConstraint = new GridBagConstraints();
        GridBagConstraints tableConstraint = new GridBagConstraints();
        
        // places excludedLabel at the top of excludedPanel
        excludedConstraint.fill = GridBagConstraints.HORIZONTAL;
        excludedConstraint.gridx = 0;
        excludedConstraint.gridy = 0;
        excludedConstraint.insets = new Insets(0, 5, 0, 0);
        excludedPanel.add(excludedLabel, excludedConstraint);
        
        // places table below excludedLabel
        JScrollPane excludedTable = bankTable();
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 0;
        tableConstraint.gridy = 1;
        excludedPanel.add(excludedTable, tableConstraint);
        
        return excludedPanel;            
    }
    
    private JPanel buttonPanel()
    {
        JButton addButton = new JButton("Add");        
        JPanel buttonPanel = new JPanel();                        
        buttonPanel.add(addButton);
        
        // listens for when removeBankButton is clicked
        addButton.addActionListener((ActionEvent e) -> {                  
            int questions = excludedQuestionInfo();
            List<BankSelection> quizQuestionBanks = quiz.getBankSelections();
            int numBanks = quizQuestionBanks.size();
            
            for (int i = 0; i < numBanks; i++)
            {
                QuestionBank bank = quizQuestionBanks.get(i).getBank();
                if (i <= (table.getRows() - 1))
                {                   
                    table.setValue(bank, i, 0);
                    table.setValue("holder", i, 1);
                    table.setValue(questions, i, 2);
                    table.setValue("holder", i, 3);
                    bankFrame.dispose();
                }
                if (i > (table.getRows() - 1))
                {
                    table.addEmptyRow();
                    int newRow = table.getRows() - 1;
                    table.setValue(bankList.getSelectedItem(), newRow, 0);
                    table.setValue("holder", newRow, 1);
                    table.setValue(questions, newRow, 2);
                    table.setValue("holder", newRow, 3);
                    bankFrame.dispose();
                }
            }
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
        bankFrame.setLocationRelativeTo(null);
        // makes the JFrame visible
        bankFrame.setVisible(true);
    }
}