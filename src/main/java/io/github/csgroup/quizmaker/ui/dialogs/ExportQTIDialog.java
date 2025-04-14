package io.github.csgroup.quizmaker.ui.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Creates a frame that will allow the user to export the QTI file from 
 * their file system
 * 
 * @author Emily Palmer
 */
public class ExportQTIDialog 
{
    private JFrame exportQTIFrame;
    private String filePath;
    private JButton exportButton;
    
    public ExportQTIDialog()
    {
        exportFileFrame();
    }
        
    private void setPath(String path)
    {
        filePath = path;
    }
        
    private String getPath()
    {
        return filePath;
    }
       
    private void exportFileFrame()
    {
        exportQTIFrame = new JFrame("Export File");
        exportQTIFrame.setSize(380, 220);
                
        JLabel fileLabel = new JLabel("File: ");
                       
        // contains importButtonPanel and importFilePanel
        JPanel exportPanel = new JPanel(new GridBagLayout());
        GridBagConstraints exportPanelConstraint = new GridBagConstraints();
        GridBagConstraints buttonPanelConstraint = new GridBagConstraints();
        GridBagConstraints fileLabelConstraint = new GridBagConstraints();
        
        // places importFilePanel at the top of the panel
        fileLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        fileLabelConstraint.gridx = 0;
        fileLabelConstraint.gridy = 0;
        fileLabelConstraint.insets = new Insets(40, 5, 0, 0);
        exportPanel.add(fileLabel, fileLabelConstraint);
                
        // places importFilePanel at the top of the panel
        JPanel filePanel = exportFilePanel();
        exportPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        exportPanelConstraint.gridx = 0;
        exportPanelConstraint.gridy = 1;
        exportPanelConstraint.insets = new Insets(0, 0, 35, 0);
        exportPanel.add(filePanel, exportPanelConstraint);
        
        // places importButtonPanel at the bottom of the panel
        JPanel importFileButtonPanel = exportButtonPanel();
        buttonPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonPanelConstraint.gridx = 0;
        buttonPanelConstraint.gridy = 2;
        exportPanel.add(importFileButtonPanel, buttonPanelConstraint);
        
        exportQTIFrame.add(exportPanel);  
    }
    
    /**
     * Creates a panel that contains the text field that display the file path 
     * and the button to attach the file
     * 
     * @return the file panel
     */
    private JPanel exportFilePanel()
    {
        JTextField fileTextField = new JTextField();
        fileTextField.setPreferredSize(new Dimension(250, 22));
        fileTextField.setFocusable(false);
        JPanel textPanel = new JPanel();
        textPanel.add(fileTextField);
        
        // contains fileTextField and attachButton
        JPanel exportFilePanel = new JPanel(new GridBagLayout());
        GridBagConstraints attachButtonConstraint = new GridBagConstraints();
        GridBagConstraints textFieldConstraint = new GridBagConstraints();
        
        // places fileTextField at the top of importFilePanel
        textFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        textFieldConstraint.gridx = 0;
        textFieldConstraint.gridy = 0;
        exportFilePanel.add(textPanel, textFieldConstraint);
        
        // places attachButton at the bottom of importFilePanel
        JButton attachFileButton = attachButton(fileTextField);
        attachButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        attachButtonConstraint.gridx = 1;
        attachButtonConstraint.gridy = 0;
        attachButtonConstraint.insets = new Insets(0, 5, 0, 0);
        exportFilePanel.add(attachFileButton, attachButtonConstraint);
        
        return exportFilePanel;
    }
    
    /**
     * Creates the button that allows the user to attach a QTi file
     * 
     * @param  textField the text field that will display the name of the file
     * @return the attach button
     */
    private JButton attachButton(JTextField textField)
    {
        JButton attachButton = new JButton("Attach");
                
        // listens for when attachButton is clicked 
        attachButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            // only allow zip files to be selected
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Zip files", "zip");
            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.showOpenDialog(null);
            // get the name of the selected file
            try
            {
                exportButton.setEnabled(true);
                String fileName = fileChooser.getSelectedFile().getName();
                String qtiFilePath = fileChooser.getSelectedFile().getPath();
                setPath(qtiFilePath);     
                // display the file name in the text field
                textField.setText(fileName);  
            }   
            catch (NullPointerException n) {
                exportButton.setEnabled(false);
            }
        }); 
        
        return attachButton;
    }
    
    /**
     * Creates the button panel that contains the export file button
     * 
     * @return the button panel
     */
    private JPanel exportButtonPanel()
    {
        exportButton = new JButton("Export");
        exportButton.setEnabled(false);
        JPanel importButtonPanel = new JPanel();
        importButtonPanel.add(exportButton);
               
        // listens for when importButton is selected
        exportButton.addActionListener((ActionEvent e) -> {
            // get the path of the file
            String exportFilePath = getPath();
            if (exportFilePath != null)
            {
                // close the frame
                exportQTIFrame.dispose();
            }
        });
        
        return importButtonPanel;
    }   
               
    /**
     * Controls when and where the frame appears
     * 
     */
    public void show()
    {
        // makes the JFrame appear in the center of the screen
        exportQTIFrame.setLocationRelativeTo(null);
        // makes the JFrame visible
        exportQTIFrame.setVisible(true);
    }
}
    
    

