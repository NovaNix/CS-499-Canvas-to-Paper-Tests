package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.qti.QTIWriter;
import io.github.csgroup.quizmaker.utils.SessionMemory;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.transform.TransformerException;

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
    private Project project;
    
    public ExportQTIDialog(Project currentProject)
    {
        this.project = currentProject;
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
        exportQTIFrame = new JFrame("Export QTI File");
        exportQTIFrame.setSize(450, 240);
                
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
        fileLabelConstraint.insets = new Insets(55, 5, 0, 0);
        exportPanel.add(fileLabel, fileLabelConstraint);
                
        // places importFilePanel at the top of the panel
        JPanel filePanel = exportFilePanel();
        exportPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        exportPanelConstraint.gridx = 0;
        exportPanelConstraint.gridy = 1;
        exportPanelConstraint.insets = new Insets(0, 0, 55, 0);
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
        fileTextField.setPreferredSize(new Dimension(310, 22));
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
            Path lastFolder = SessionMemory.getInstance().getLastExportQtiFolder();
            if (lastFolder != null) {
                fileChooser.setCurrentDirectory(lastFolder.toFile());
            }
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Zip files", "zip");
            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.showOpenDialog(null);
            // get the name of the selected file
            try
            {
                exportButton.setEnabled(true);
                String fileName = fileChooser.getSelectedFile().getName();
                if(!fileName.toLowerCase().endsWith(".zip"))
                {
                	fileName += ".zip";
                }
                String qtiFilePath = fileChooser.getSelectedFile().getPath();
                if(!qtiFilePath.toLowerCase().endsWith(".zip"))
                {
                	qtiFilePath += ".zip";
                }
                setPath(qtiFilePath);
                SessionMemory.getInstance().setLastExportQtiFolder(fileChooser.getSelectedFile().toPath().getParent());
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
        JPanel exportButtonPanel = new JPanel();
        exportButtonPanel.add(exportButton);
               
        // listens for when importButton is selected
        exportButton.addActionListener((ActionEvent e) -> {
            // get the path of the file
            String exportFilePath = getPath();
            if (exportFilePath != null)
            {
                try
                {
                    Path path = Paths.get(exportFilePath);
                    QTIWriter writer = new QTIWriter();
                    writer.writeProject(project, path);                             
                    // close the frame
                    exportQTIFrame.dispose();
                    successDialog();
                }
                catch (IOException n) 
                {
                    errorDialog(n.getMessage());
                    
                } 
                catch (TransformerException e1) 
                {
					errorDialog(e1.getMessage());
				} 
                catch (Exception e1) 
                {
					errorDialog(e1.getMessage());
				}
            }
        });
        
        return exportButtonPanel;
    } 
    
    /**
     * Shows a JOptionPane to let the user know if an error has occurred importing 
     * their file
     */
    private void errorDialog(String error)
    {
        JFrame errorFrame = new JFrame();
        JOptionPane.showMessageDialog(errorFrame, error, "Error", JOptionPane.ERROR_MESSAGE);        
    }

    private void successDialog()
    {
        JFrame successFrame = new JFrame();
        JOptionPane.showMessageDialog(successFrame, "QTI file successfully exported", null, JOptionPane.PLAIN_MESSAGE);        
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
    
    

