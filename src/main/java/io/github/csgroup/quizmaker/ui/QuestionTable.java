package io.github.csgroup.quizmaker.ui;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JComponent;

/**
 * Creates a JPanel that contains a table of the quiz bank questions
 * and answers
 * 
 * @author Emily Palmer
 */
public class QuestionTable extends JComponent
{    
        public QuestionTable()
        {
                createTablePanel();
        }
    
        /**
         * Creates a JTable to hold the quiz bank questions and answers and 
         * places the JTable in a JPanel
         *
         * @return JPanel containing the JTable
         */
        private void createTablePanel()
        {
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
                JPanel tablePanel = new JPanel();
                tablePanel.add(tableScrollPane);
                
                this.setLayout(new GridBagLayout());
                GridBagConstraints tableConstraint = new GridBagConstraints();
                
                // places tablePanel at the top of QuestionTable
                tableConstraint.fill = GridBagConstraints.HORIZONTAL;
                tableConstraint.gridx = 0;
                tableConstraint.gridy = 0;
                this.add(tablePanel, tableConstraint);
        }   
}
