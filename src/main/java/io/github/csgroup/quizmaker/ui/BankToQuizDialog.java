package io.github.csgroup.quizmaker.ui;

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
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumnModel;
import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.QuestionBank;
import java.util.List;

/**
 * Creates a frame that allows the user to select what bank questions to add/remove 
 * to the quiz 
 * 
 * @author Emily Palmer
 */
public class BankToQuizDialog 
{
    private JFrame bankFrame;
    private final Project bankToQuizProject;
    
    public BankToQuizDialog(Project quizBankProject)
    {
        bankToQuizProject = quizBankProject;
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
        bankFrame.setSize(350, 340);
        
        // contains bankInfoPanel and questionPanel
        JPanel addBankPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bankInfoConstraint = new GridBagConstraints();
        GridBagConstraints questionsConstraint = new GridBagConstraints();
        
        // places bankInfoPanel at the top of addBankPanel
        JPanel bankInfoPanel = bankInfoPanel();
        bankInfoConstraint.fill = GridBagConstraints.HORIZONTAL;
        bankInfoConstraint.gridx = 0;
        bankInfoConstraint.gridy = 0;
        bankInfoConstraint.insets = new Insets(0, 0, 10, 90);
        addBankPanel.add(bankInfoPanel, bankInfoConstraint);
        
        // places questionPanel below bankInfoPanel
        JPanel questionPanel = excludedPanel();
        questionsConstraint.fill = GridBagConstraints.HORIZONTAL;
        questionsConstraint.gridx = 0;
        questionsConstraint.gridy = 1;
        questionsConstraint.insets = new Insets(0, 0, 0, 45);
        addBankPanel.add(questionPanel, questionsConstraint);
        
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
        List<QuestionBank> quizBankNameList = bankToQuizProject.getQuestionBanks();
        JComboBox bankList = new JComboBox(quizBankNameList.toArray());
        bankList.setPreferredSize(new Dimension(180, 20));
        
        JLabel pointsLabel = new JLabel ("Points:");
        JTextField pointsTextField = new JTextField();
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
        
        return bankInfoPanel;
    }
    
    /**
     * Creates the table panel that contains bank questions and a button for each 
     * question. The user can select this button to exclude a question
     * 
     * @return table panel
     */
    private JPanel bankTable()
    {
        String [] columnHeaders = {"1", "2"};
        DefaultTableModel model = new DefaultTableModel(columnHeaders, 15);
        JTable questionsTable = new JTable(model);
        questionsTable.setTableHeader(null);
        TableColumnModel columnModel = questionsTable.getColumnModel();
        // the preferred width of each column in the JTable
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(240);
                
        // JScrollPane for the JTable table
        JScrollPane tableScrollPane = new JScrollPane(questionsTable);
        tableScrollPane.setPreferredSize(new Dimension(270, 200));
        JPanel tablePanel = new JPanel();
        tablePanel.add(tableScrollPane);
       
        return tablePanel;
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
        excludedPanel.add(excludedLabel, excludedConstraint);
        
        // places table below excludedLabel
        JPanel table = bankTable();
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 0;
        tableConstraint.gridy = 1;
        excludedPanel.add(table, tableConstraint);
        
        return excludedPanel;            
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
