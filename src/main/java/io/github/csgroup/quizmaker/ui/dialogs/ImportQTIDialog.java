package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.qti.QTIContents;
import io.github.csgroup.quizmaker.qti.QTIReader;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Creates a frame that will allow the user to import the QTI file from 
 * their file system
 * 
 * @author Emily Palmer
 */
public class ImportQTIDialog 
{ 
    private JFrame importFrame;
    private String filePath;
    private final Project importFileProject;
    
    public ImportQTIDialog(Project importProject)
    {
        importFileProject = importProject;
        importFileFrame();
    }
        
    private void setPath(String path)
    {
        filePath = path;
    }
        
    private String getPath()
    {
        return filePath;
    }
        
    /**
     * Creates a frame that prompts the user to attach a QTI file
     * 
     */
    private void importFileFrame()
    {            
        importFrame = new JFrame("Import File");
        importFrame.setSize(380, 220);
                
        JLabel fileLabel = new JLabel("File: ");
                       
        // contains importButtonPanel and importFilePanel
        JPanel importPanel = new JPanel(new GridBagLayout());
        GridBagConstraints importPanelConstraint = new GridBagConstraints();
        GridBagConstraints buttonPanelConstraint = new GridBagConstraints();
        GridBagConstraints fileLabelConstraint = new GridBagConstraints();
        
        // places importFilePanel at the top of the panel
        fileLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        fileLabelConstraint.gridx = 0;
        fileLabelConstraint.gridy = 0;
        fileLabelConstraint.insets = new Insets(40, 5, 0, 0);
        importPanel.add(fileLabel, fileLabelConstraint);
                
        // places importFilePanel at the top of the panel
        JPanel filePanel = importFilePanel();
        importPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        importPanelConstraint.gridx = 0;
        importPanelConstraint.gridy = 1;
        importPanelConstraint.insets = new Insets(0, 0, 35, 0);
        importPanel.add(filePanel, importPanelConstraint);
        
        // places importButtonPanel at the bottom of the panel
        JPanel importFileButtonPanel = importButtonPanel();
        buttonPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonPanelConstraint.gridx = 0;
        buttonPanelConstraint.gridy = 2;
        importPanel.add(importFileButtonPanel, buttonPanelConstraint);
        
        importFrame.add(importPanel);  
    }
    
    /**
     * Creates a panel that contains the text field that display the file path 
     * and the button to attach the file
     * 
     * @return the file panel
     */
    private JPanel importFilePanel()
    {
        JTextField fileTextField = new JTextField();
        fileTextField.setPreferredSize(new Dimension(250, 22));
        fileTextField.setFocusable(false);
        JPanel textPanel = new JPanel();
        textPanel.add(fileTextField);
        
        // contains fileTextField and attachButton
        JPanel importFilePanel = new JPanel(new GridBagLayout());
        GridBagConstraints attachButtonConstraint = new GridBagConstraints();
        GridBagConstraints textFieldConstraint = new GridBagConstraints();
        
        // places fileTextField at the top of importFilePanel
        textFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        textFieldConstraint.gridx = 0;
        textFieldConstraint.gridy = 0;
        importFilePanel.add(textPanel, textFieldConstraint);
        
        // places attachButton at the bottom of importFilePanel
        JButton attachFileButton = attachButton(fileTextField);
        attachButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        attachButtonConstraint.gridx = 1;
        attachButtonConstraint.gridy = 0;
        attachButtonConstraint.insets = new Insets(0, 5, 0, 0);
        importFilePanel.add(attachFileButton, attachButtonConstraint);
        
        return importFilePanel;
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
                String fileName = fileChooser.getSelectedFile().getName();
                String qtiFilePath = fileChooser.getSelectedFile().getPath();
                setPath(qtiFilePath);     
                // display the file name in the text field
                textField.setText(fileName);   
            }   
            catch (NullPointerException n) {}
        }); 
                
        return attachButton;
    }
        
    /**
     * Creates the button panel that contains the import file button
     * 
     * @return the button panel
     */
    private JPanel importButtonPanel()
    {
        JButton importButton = new JButton("Import");               
        JPanel importButtonPanel = new JPanel();
        importButtonPanel.add(importButton);
               
        // listens for when importButton is selected
        importButton.addActionListener((ActionEvent e) -> {
            // get the path of the file
            String importFilePath = getPath();
            if (importFilePath != null)
            {
                importFile(importFilePath);
                // close the frame
                importFrame.dispose();
            }
        });
        
        return importButtonPanel;
    }   
    
    /**
     * Imports the QTI file
     * 
     * @param qtiFilePath file path of the QTI file
     */
    private void importFile(String qtiStringPath)
    {
        Path qtiFilePath = Paths.get(qtiStringPath);
        QTIReader importQTIFile = new QTIReader();
        QTIContents test = importQTIFile.readFile(qtiFilePath);
        importFileProject.addQTI(test);
    }
            
    /**
     * Controls when and where the frame appears
     * 
     */
    public void show()
    {
        // makes the JFrame appear in the center of the screen
        importFrame.setLocationRelativeTo(null);
        // makes the JFrame visible
        importFrame.setVisible(true);
    }
}
