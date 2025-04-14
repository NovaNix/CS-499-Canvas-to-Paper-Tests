package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.word.WordExporter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * Creates the frame that allows the user to export their quiz into a word 
 * document
 * 
 * @author Emily Palmer
 */
public class ExportWordDialog 
{
    private JFrame exportFrame;
    private String locationPath;
    private JRadioButton keyButton;
    private JRadioButton testButton;
    private JTextField templateTextField;
    private JTextField locationTextField;
    private JButton exportButton;
    private Quiz quiz;
    
    public ExportWordDialog(Quiz exportQuiz)
    {
        quiz = exportQuiz;
        createExportFileFrame();
    }
          
    private void setLocation(String path)
    {
        locationPath = path;
    }
        
    private String getLocation()
    {
        return locationPath;
    }
        
    /**
     * Places the label panel, radio button panel, file attachment panel,
     * and export button panel on the frame using the GridBagLayout layout manager
     * 
     */
    private void createExportFileFrame()
    {
        exportFrame = new JFrame("Export File");
        exportFrame.setSize(410, 280);
                
        // contains labelPanel, radioButtonPanel, filePanel, and exportButtonPanel
        JPanel exportPanel = new JPanel(new GridBagLayout());                
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints fileConstraint = new GridBagConstraints();
        GridBagConstraints exportButtonConstraint = new GridBagConstraints();
                
        // places labelPanel at the top of exportPanel
        JPanel labelPanel = labelPanel();
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(0, 0, 0, 285);
        exportPanel.add(labelPanel, labelConstraint);
                
        // places radioButtonPanel below labelPanel on exportPanel
        JPanel radioButtonPanel = radioButtonPanel();
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 2;
        buttonConstraint.insets = new Insets(0, 0, 5, 210);
        exportPanel.add(radioButtonPanel, buttonConstraint);
                
        // places filePanel below radioButtonPanel on exportPanel
        JPanel filePanel = fileAttachPanel();
        fileConstraint.fill = GridBagConstraints.HORIZONTAL;
        fileConstraint.gridx = 0;
        fileConstraint.gridy = 3;
        exportPanel.add(filePanel, fileConstraint);
                   
        // places exportButtonPanel at the bottmo of exportPanel
        JPanel expButtonPanel = exportButtonPanel();
        exportButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        exportButtonConstraint.gridx = 0;
        exportButtonConstraint.gridy = 4;
        exportPanel.add(expButtonPanel, exportButtonConstraint);
                
        exportFrame.add(exportPanel);                                                
    }
        
    /**
     * Creates the JPanel containing the labels for the quiz to be exported 
     * and the type of export document (either test or answer key)
     * 
     * @return panel containing the labels
     */
    private JPanel labelPanel()
    {
        JLabel quizName = new JLabel("Exporting Quiz:");
        JLabel typeLabel = new JLabel("Type:");
                
        // contains quizName and typeLabel
        JPanel exportLabelPanel = new JPanel(new GridBagLayout());
        GridBagConstraints quizNameConstraint = new GridBagConstraints();
        GridBagConstraints typeLabelConstraint = new GridBagConstraints();
                
        // places quizName at the top of exportLabelPanel
        quizNameConstraint.fill = GridBagConstraints.HORIZONTAL;
        quizNameConstraint.gridx = 0;
        quizNameConstraint.gridy = 0;
        quizNameConstraint.insets = new Insets(0, 0, 5, 0);
        exportLabelPanel.add(quizName, quizNameConstraint);
                
        // places typeLabel at the bottom of exportLabelPanel
        typeLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        typeLabelConstraint.gridx = 0;
        typeLabelConstraint.gridy = 1;
        typeLabelConstraint.insets = new Insets(0, 0, 5, 0);
        exportLabelPanel.add(typeLabel, typeLabelConstraint);
                                
        return exportLabelPanel;
    }
        
    /**
     * Creates the JPanel that allows the user to attach the export location 
     * and template file location
     * 
     * @return the file attachment panel
     */
    private JPanel fileAttachPanel()
    {
        JLabel templateLabel = new JLabel("Template:");
        JLabel locationLabel = new JLabel("Export Location:");;
                
        // contains templateLabel, templateTextField, tempButtonPanel, 
        // locationLabel, locationTextField, and locationButtonPanel
        JPanel fileAttachPanel = new JPanel(new GridBagLayout());
        GridBagConstraints templateConstraint = new GridBagConstraints();
        GridBagConstraints tempTextFieldConstraint = new GridBagConstraints();
        GridBagConstraints locationConstraint = new GridBagConstraints();
        GridBagConstraints locTextFieldConstraint = new GridBagConstraints();
        GridBagConstraints tempButtonConstraint = new GridBagConstraints();
        GridBagConstraints locButtonConstraint = new GridBagConstraints();
                
        // places templateLabel at the top of fileAttachPanel
        templateConstraint.fill = GridBagConstraints.HORIZONTAL;
        templateConstraint.gridx = 0;
        templateConstraint.gridy = 0;
        fileAttachPanel.add(templateLabel, templateConstraint);
                
        // places templateTextField below templateLabel on fileAttachPanel
        JPanel tempPanel = templateTextField();
        tempTextFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        tempTextFieldConstraint.gridx = 0;
        tempTextFieldConstraint.gridy = 1;
        tempTextFieldConstraint.insets = new Insets(0, 0, 5, 0);
        fileAttachPanel.add(tempPanel, tempTextFieldConstraint);
                
        // places tempButtonPanel to the right of templateTextField on fileAttachPanel
        JPanel tempButtonPanel = selectTempButtonPanel(templateTextField);
        tempButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        tempButtonConstraint.gridx = 2;
        tempButtonConstraint.gridy = 1;
        tempButtonConstraint.insets = new Insets(0, 0, 5, 0);
        fileAttachPanel.add(tempButtonPanel, tempButtonConstraint);
                
        // places locationLabel below templateTextField on fileAttachPanel
        locationConstraint.fill = GridBagConstraints.HORIZONTAL;
        locationConstraint.gridx = 0;
        locationConstraint.gridy = 2;
        locationConstraint.insets = new Insets(0, 2, 0, 0);
        fileAttachPanel.add(locationLabel, locationConstraint);
                
        // places locationTextField below locationLabel on fileAttachPanel
        JPanel locationPanel = locationTextField();
        locTextFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        locTextFieldConstraint.gridx = 0;
        locTextFieldConstraint.gridy = 3;
        locTextFieldConstraint.insets = new Insets(0, 0, 15, 0);
        fileAttachPanel.add(locationPanel, locTextFieldConstraint);
                
        // places locationButtonPanel to the right of locationTextField on fileAttachPanel
        JPanel locationButtonPanel = selectLocButtonPanel(locationTextField);
        locButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        locButtonConstraint.gridx = 2;
        locButtonConstraint.gridy = 3;
        locButtonConstraint.insets = new Insets(0, 0, 15, 0);
        fileAttachPanel.add(locationButtonPanel, locButtonConstraint);
                                
        return fileAttachPanel;            
    }
    
    /**
     * Creates the panel and text field that holds the file path of the template
     * and its event listener
     * 
     * @return text field panel
     */
    private JPanel templateTextField()
    {
        templateTextField = new JTextField();
        templateTextField.setFocusable(false);
        templateTextField.setPreferredSize(new Dimension(280, 22));
        JPanel tempPanel = new JPanel();
        tempPanel.add(templateTextField);
        
        // listens for when text appears in the text field
        Document templateDocument = templateTextField.getDocument();
        templateDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                // if text is in both the template and location text fields enable 
                // the export button
                boolean location = (locationTextField.getText()).isEmpty();
                if (location == false)
                {
                    exportButton.setEnabled(true);
                }
            }
            
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String text = templateTextField.getText();
                boolean empty = text.isEmpty();
                // disable the button if the template field is empty
                if (empty == true)
                {
                    exportButton.setEnabled(false);
                }                
            }            
            @Override
            public void changedUpdate(DocumentEvent e) {}                 
        });
             
        return tempPanel;
    }
    
    /**
     * Creates the panel and text field that holds the file path of the export 
     * location text field and its event listener
     * 
     * @return location field panel
     */
    private JPanel locationTextField()
    {
        locationTextField = new JTextField();
        locationTextField.setPreferredSize(new Dimension(280, 22));
        locationTextField.setFocusable(false);
        JPanel locationPanel = new JPanel();
        locationPanel.add(locationTextField);
        
        // listens for when text appears in the text field
        Document locationDocument = locationTextField.getDocument();
        locationDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                // if text is in both the template and location text fields enable 
                // the export button
                boolean template = (templateTextField.getText()).isEmpty();
                if (template == false)
                {
                    exportButton.setEnabled(true);
                }
            }
            
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String text = locationTextField.getText();
                boolean empty = text.isEmpty();
                // disable the button if the location field is empty
                if (empty == true)
                {
                    exportButton.setEnabled(false);
                }                
            }            
            @Override
            public void changedUpdate(DocumentEvent e) {}                 
        });
        
        return locationPanel;
    }
    
    /**
     * Creates the JPanel that holds the "Test" and "Key" radio buttons
     * 
     * @return the radio button panel
     */
    private JPanel radioButtonPanel()
    {
        testButton = new JRadioButton("Test");
        testButton.setSelected(true);
        keyButton = new JRadioButton("Key");
        ButtonGroup group = new ButtonGroup();
        group.add(testButton);
        group.add(keyButton);
                
        // contains testButton and keyButton
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints testButtonConstraint = new GridBagConstraints();
        GridBagConstraints keyButtonConstraint = new GridBagConstraints();
                
        // places testButton at the top of buttonPanel
        testButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        testButtonConstraint.gridx = 0;
        testButtonConstraint.gridy = 0;
        buttonPanel.add(testButton, testButtonConstraint);
                
        // places keyButton to the right of keyButton
        keyButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        keyButtonConstraint.gridx = 1;
        keyButtonConstraint.gridy = 0;
        buttonPanel.add(keyButton, keyButtonConstraint);
                
        return buttonPanel;            
    }
    
    /**
     * Creates the JPanel that holds the select template directory button
     * 
     * @param tempTextField the text field that display the template file
     * location
     * @return the template selection button panel
     */
    private JPanel selectTempButtonPanel(JTextField tempTextField)
    {
        JButton selectTempButton = new JButton("Select");
            
        // contains selectTempButton
        JPanel selectTempButtonPanel = new JPanel();        
        selectTempButtonPanel.add(selectTempButton);
                
        // listens for when the user selects selectTempButton
        selectTempButton.addActionListener((ActionEvent e) -> {
            JFileChooser tempFileChooser = new JFileChooser();
            // only allow directories to appear
            tempFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            tempFileChooser.showOpenDialog(null);
            // get the file path and display it on tempTextField
            try
            {
                String templateFilePath = tempFileChooser.getSelectedFile().getPath();             
                tempTextField.setText(templateFilePath);
            }
            catch (NullPointerException n) {}
        });
                
        return selectTempButtonPanel;
    }
        
    /**
     * Creates the JPanel that holds the select export location button
     * 
     * @param locTextField the text field that displays the export location
     * file path 
     * @return the select export location button panel
     */
    private JPanel selectLocButtonPanel(JTextField locTextField)
    {
        JButton selectLocButton = new JButton("Select");
            
        // contains selectLocButton 
        JPanel selectLocButtonPanel = new JPanel();        
        selectLocButtonPanel.add(selectLocButton);
            
        // listens for when the user selects selectLocButton
        selectLocButton.addActionListener((ActionEvent e) -> {
            JFileChooser locFileChooser = new JFileChooser();
            // only allow directories to appear 
            locFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            locFileChooser.showOpenDialog(null);
            // get the file path and display it on locTextField
            try 
            {
                String locationFilePath = locFileChooser.getSelectedFile().getPath(); 
                setLocation(locationFilePath);
                locTextField.setText(locationFilePath);    
            } 
            catch (NullPointerException n) {}
        }); 
                
        return selectLocButtonPanel;
    }
        
    /**
     * Creates the JPanel that holds the export button and exports the 
     * quiz or answer key
     * 
     * @return export button panel
     */
    private JPanel exportButtonPanel()
    {
        exportButton = new JButton("Export");
        exportButton.setEnabled(false);
        WordExporter exportFile = new WordExporter();
                
        // contains exportButton
        JPanel exportButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints exportButtonConstraint = new GridBagConstraints();
                
        // places exportButton at the top of exportButtonPanel
        exportButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        exportButtonConstraint.gridx = 0;
        exportButtonConstraint.gridy = 0;
        exportButtonConstraint.insets = new Insets(0, 0, 0, 0);
        exportButtonPanel.add(exportButton, exportButtonConstraint);
                
        // listens for when the user selects exportButton
        exportButton.addActionListener((ActionEvent e) -> {
            if ((testButton.isSelected() == true) || (keyButton.isSelected() == true))
            {
                if (testButton.isSelected() == true)
                {
                    String testLocation = getLocation();
                    if (testLocation.isEmpty() == false)
                    {
                        //exportFile.exportTest(quiz, testLocation);
                    }
                }
                if (keyButton.isSelected() == true)
                {
                    String keyLocation = getLocation();
                    if (keyLocation.isEmpty() == false)
                    {
                        //exportFile.exportAnswerKey(quiz, keyLocation);
                    }
                }
                exportFrame.dispose();
            }
        }); 
                
        return exportButtonPanel;
    }
        
    /**
     * Controls when and where the frame appears
     * 
     */
    public void show()
    {
        // makes the JFrame appear in the center of the screen
        exportFrame.setLocationRelativeTo(null);
        // makes the JFrame visible
        exportFrame.setVisible(true);
    }
}
