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
 * Creates a JPanel that contains a table 
 * 
 * @author Emily Palmer
 */
public class QuestionTable extends JComponent
{    
    private final String[] columnNames;
    private final int defaultRowNumbers;
    private JTable dataTable;
    private JScrollPane tableScrollPane;
    private DefaultTableModel model;
        
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
    
    public DefaultTableModel getModel()
    {
        return model;
    }
    
    /**
     * Creates a reusable JTables
     *
     * @return JPanel containing the JTable
     */
    private void createTablePanel()
    {
        // column headers for dataTable
        model = new DefaultTableModel(columnNames, defaultRowNumbers) {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
         
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
    
    /**
     * Sets the height of the rows in the table
     * 
     * @param rowHeight 
     */
    public void setTableRowHeight(int rowHeight)
    {
        dataTable.setRowHeight(rowHeight);
    }
    
    /**
     * Gets the selected row in the table
     * 
     * @return the row selected
     */
    public int getRowSelected()
    {
        int row = dataTable.getSelectedRow();
        return row;
    }
    
    /**
     * Gets the number of rows in the table
     * 
     * @return number of rows
     */
    public int getRows()
    {
        int rows = dataTable.getRowCount();
        return rows;
    }
    
    /**
     * Sets the width of a column in the table
     * 
     * @param column
     * @param columnWidth 
     */
    public void setColumnWidth(int column, int columnWidth)
    {
        TableColumnModel columnModel = dataTable.getColumnModel();
        columnModel.getColumn(column).setPreferredWidth(columnWidth);
    }
    
    /**
     * Sets the size of the scroll pane
     * 
     * @param width
     * @param height 
     */
    public void setTableSize(int width, int height)
    {
        tableScrollPane.setPreferredSize(new Dimension(width, height));        
    } 
    
    /**
     * Sets a cell value in the table
     * 
     * @param value display value
     * @param row 
     * @param column 
     */
    public void setValue(Object value, int row, int column)
    {
        dataTable.setValueAt(value, row, column);
    }
    
    /**
     * Gets a cell value in the table
     * 
     * @param row specified row in the table
     * @param column specified column in the table
     * @return the value at the cell
     */
    public Object getValue(int row, int column)
    {
        Object value = model.getValueAt(row, column);
        return value;
    }
    
    /**
     * Adds an empty row to the table
     */
    public void addEmptyRow()
    {
        model.addRow(new Object[]{null, null, null, null});
    }
    
    /**
     * Resets the table 
     */
    public void clearTable()
    {
        int rows = dataTable.getRowCount();
        int columns = dataTable.getColumnCount();
        for (int i = 0; i < columns; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                dataTable.setValueAt(null, j, i);
            }
        }
    }    
}
