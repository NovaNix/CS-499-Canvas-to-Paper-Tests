package io.github.csgroup.quizmaker.ui.components;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JComponent;
import java.awt.Dimension;

/**
 * Creates a JPanel that contains a table of the quiz bank questions
 * and answers
 * 
 * @author Emily Palmer
 */
public class QuestionTable extends JComponent
{    
    private final String[] columnNames;
    private final int defaultRowNumbers;
    private JTable dataTable;
    private JScrollPane tableScrollPane;
    
    public QuestionTable(String[] columnHeaders, int rows)
    {
        columnNames = columnHeaders;
        defaultRowNumbers = rows;
        createTablePanel();
    }
    
    public JTable getTable()
    {
        return dataTable;
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
        DefaultTableModel model = new DefaultTableModel(columnNames, defaultRowNumbers);
        dataTable = new JTable(model);
        
        // JScrollPane for the JTable dataTable
        tableScrollPane = new JScrollPane(dataTable);
                
        this.setLayout(new GridBagLayout());
        GridBagConstraints tableConstraint = new GridBagConstraints();
                
        // places tablePanel at the top of QuestionTable
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 0;
        tableConstraint.gridy = 0;
        this.add(tableScrollPane, tableConstraint);
    } 
    
    public void setTableRowHeight(int rowHeight)
    {
        dataTable.setRowHeight(rowHeight);
    }
    
    public void setColumnWidth(int column, int columnWidth)
    {
        TableColumnModel columnModel = dataTable.getColumnModel();
        columnModel.getColumn(column).setPreferredWidth(columnWidth);
    }
    
    public void setTableSize(int width, int height)
    {
        tableScrollPane.setPreferredSize(new Dimension(width, height));        
    } 
}
