package io.github.csgroup.quizmaker.ui.dialogs;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;
import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;
import io.github.csgroup.quizmaker.ui.components.GeneratePanel;
import io.github.csgroup.quizmaker.word.TemplateReplacements;
import io.github.csgroup.quizmaker.word.WordExporter;
import java.awt.CardLayout;

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
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
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
    private JRadioButton keyButton;
    private JRadioButton testButton;
    private JTextField templateTextField;
    private JTextField locationTextField;
    private JTextField referenceTextField;
    private JButton exportButton;
    private JPanel replacementPanel;
    private JPanel cardPanel;
    private Path lastUsedDirectory = Paths.get(System.getProperty("user.home"));
    private Quiz quiz;
    private TemplateReplacements replacements;
    
    public ExportWordDialog(Quiz exportQuiz)
    {
        quiz = exportQuiz;
        createExportFileFrame();
    }

        
    /**
     * Places the label panel, radio button panel, file attachment panel,
     * and export button panel on the frame using the GridBagLayout layout manager
     * 
     */
    private void createExportFileFrame()
    {
        exportFrame = new JFrame("Export File");
        exportFrame.setSize(435, 340);
                
        // contains labelPanel, radioButtonPanel, filePanel, and exportButtonPanel
        JPanel exportPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(exportPanel);       
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints fileConstraint = new GridBagConstraints();
        GridBagConstraints exportButtonConstraint = new GridBagConstraints();
                
        // places labelPanel at the top of exportPanel
        JPanel labelPanel = labelPanel();
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(0, 0, 0, 75);
        exportPanel.add(labelPanel, labelConstraint);
                
        // places radioButtonPanel below labelPanel on exportPanel
        JPanel radioButtonPanel = radioButtonPanel();
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 2;
        buttonConstraint.insets = new Insets(0, 0, 5, 200);
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
                
        exportFrame.add(scrollPane);                                                
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
        
        JTextField nameField = new JTextField(quiz.getTitle());
        nameField.setFocusable(false);
        nameField.setPreferredSize(new Dimension(200, 22));
        JPanel namePanel = new JPanel();
        namePanel.add(nameField);
        
        // contains quizName and namePanel
        JPanel exportPanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints textFieldConstraint = new GridBagConstraints();
        
        // places quizName on the left side of exportPanel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        exportPanel.add(quizName, labelConstraint);
        
        // places namePanel on the right side of exportPanel
        textFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        textFieldConstraint.gridx = 1;
        textFieldConstraint.gridy = 0;
        textFieldConstraint.insets = new Insets(0, 10, 5, 0);
        exportPanel.add(namePanel, textFieldConstraint);
                
        // contains quizName and typeLabel
        JPanel exportLabelPanel = new JPanel(new GridBagLayout());
        GridBagConstraints quizNameConstraint = new GridBagConstraints();
        GridBagConstraints typeLabelConstraint = new GridBagConstraints();
                
        // places quizName at the top of exportLabelPanel
        quizNameConstraint.fill = GridBagConstraints.HORIZONTAL;
        quizNameConstraint.gridx = 0;
        quizNameConstraint.gridy = 0;
        quizNameConstraint.insets = new Insets(0, 10, 5, 0);
        exportLabelPanel.add(exportPanel, quizNameConstraint);
                
        // places typeLabel at the bottom of exportLabelPanel
        typeLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        typeLabelConstraint.gridx = 0;
        typeLabelConstraint.gridy = 1;
        typeLabelConstraint.insets = new Insets(0, 10, 5, 0);
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
        JLabel templateLabel = new JLabel("Template (Leave blank to create):");
        JLabel locationLabel = new JLabel("Export Location:");
                
        // contains templateLabel, templateTextField, tempButtonPanel, 
        // locationLabel, locationTextField, and locationButtonPanel
        JPanel fileAttachPanel = new JPanel(new GridBagLayout());
        GridBagConstraints templateConstraint = new GridBagConstraints();
        GridBagConstraints tempTextFieldConstraint = new GridBagConstraints();
        GridBagConstraints locationConstraint = new GridBagConstraints();
        GridBagConstraints locTextFieldConstraint = new GridBagConstraints();
        GridBagConstraints tempButtonConstraint = new GridBagConstraints();
        GridBagConstraints locButtonConstraint = new GridBagConstraints();
        GridBagConstraints buttonPanelConstraint = new GridBagConstraints();
        GridBagConstraints panelConstraint = new GridBagConstraints();
                
        // places templateLabel at the top of fileAttachPanel
        templateConstraint.fill = GridBagConstraints.HORIZONTAL;
        templateConstraint.gridx = 0;
        templateConstraint.gridy = 0;
        templateConstraint.insets = new Insets(0, 5, 0, 0);
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
        tempButtonConstraint.insets = new Insets(0, 0, 3, 0);
        fileAttachPanel.add(tempButtonPanel, tempButtonConstraint);
        
        // places buttonPanel below tempPanel
        JPanel buttonPanel = replacementButtonPanel();
        buttonPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonPanelConstraint.gridx = 0;
        buttonPanelConstraint.gridy = 2;
        buttonPanelConstraint.insets = new Insets(0, 0, 8, 167);
        fileAttachPanel.add(buttonPanel, buttonPanelConstraint);
        
        // places replacementPanel below buttonPanel
        JPanel replacement = replacementPanel();
        panelConstraint.fill = GridBagConstraints.HORIZONTAL;
        panelConstraint.gridx = 0;
        panelConstraint.gridy = 3;
        panelConstraint.insets = new Insets(0, 35, 0, 0);
        fileAttachPanel.add(replacement, panelConstraint);
                       
        // places locationLabel below replacementPanel on fileAttachPanel
        locationConstraint.fill = GridBagConstraints.HORIZONTAL;
        locationConstraint.gridx = 0;
        locationConstraint.gridy = 4;
        locationConstraint.insets = new Insets(0, 6, 0, 0);
        fileAttachPanel.add(locationLabel, locationConstraint);
                
        // places locationTextField below locationLabel on fileAttachPanel
        JPanel locationPanel = locationTextField();
        locTextFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        locTextFieldConstraint.gridx = 0;
        locTextFieldConstraint.gridy = 5;
        locTextFieldConstraint.insets = new Insets(0, 0, 15, 0);
        fileAttachPanel.add(locationPanel, locTextFieldConstraint);
                
        // places locationButtonPanel to the right of locationTextField on fileAttachPanel
        JPanel locationButtonPanel = selectLocButtonPanel(locationTextField);
        locButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        locButtonConstraint.gridx = 2;
        locButtonConstraint.gridy = 5;
        locButtonConstraint.insets = new Insets(0, 0, 15, 0);
        fileAttachPanel.add(locationButtonPanel, locButtonConstraint);
        
        JLabel referenceLabel = new JLabel("Reference File:");
        GridBagConstraints referenceLabelConstraint = new GridBagConstraints();
        referenceLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
        referenceLabelConstraint.gridx = 0;
        referenceLabelConstraint.gridy = 6;
        referenceLabelConstraint.insets = new Insets(0, 6, 5, 0);
        fileAttachPanel.add(referenceLabel, referenceLabelConstraint);

        // Reference text field
        JPanel referencePanel = referenceTextField();
        GridBagConstraints referenceTextConstraint = new GridBagConstraints();
        referenceTextConstraint.fill = GridBagConstraints.HORIZONTAL;
        referenceTextConstraint.gridx = 0;
        referenceTextConstraint.gridy = 7;
        fileAttachPanel.add(referencePanel, referenceTextConstraint);

        // Select button for reference
        JPanel referenceButtonPanel = selectReferenceButtonPanel(referenceTextField);
        GridBagConstraints referenceButtonConstraint = new GridBagConstraints();
        referenceButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        referenceButtonConstraint.gridx = 2;
        referenceButtonConstraint.gridy = 7;
        fileAttachPanel.add(referenceButtonPanel, referenceButtonConstraint);

                                
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
        templateTextField.setPreferredSize(new Dimension(290, 22));
        JPanel tempPanel = new JPanel();
        tempPanel.add(templateTextField);
             
        return tempPanel;
    }
    
    /**
     * Reference text field panel to hold file path of the reference file
     * @return
     */
    private JPanel referenceTextField()
    {
        referenceTextField = new JTextField();
        referenceTextField.setFocusable(false);
        referenceTextField.setPreferredSize(new Dimension(290, 22));
        JPanel tempPanel = new JPanel();
        tempPanel.add(referenceTextField);
             
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
        locationTextField.setPreferredSize(new Dimension(290, 22));
        locationTextField.setFocusable(false);
        JPanel locationPanel = new JPanel();
        locationPanel.add(locationTextField);
        
        // listens for when text appears in the text field
        Document locationDocument = locationTextField.getDocument();
        locationDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                // if text is in the location field enable
                // the export button
                exportButton.setEnabled(true);
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
	private JPanel selectTempButtonPanel(JTextField tempTextField) {
		JButton selectTempButton = new JButton("Select");

		JPanel selectTempButtonPanel = new JPanel();
		selectTempButtonPanel.add(selectTempButton);

		selectTempButton.addActionListener((ActionEvent e) -> {
			JFileChooser tempFileChooser = new JFileChooser();

			// Allow files only (not directories)
			tempFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			// Filter to only show Word documents (.doc and .docx)
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Word Documents (*.doc, *.docx)", "doc", "docx");
			tempFileChooser.setFileFilter(filter);

			int result = tempFileChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = tempFileChooser.getSelectedFile();
				lastUsedDirectory = selectedFile.toPath().getParent();
				if (selectedFile != null) {
					tempTextField.setText(selectedFile.getAbsolutePath());
				}
			}
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
	private JPanel selectLocButtonPanel(JTextField locTextField) {
		JButton selectLocButton = new JButton("Select");

		JPanel selectLocButtonPanel = new JPanel();        
		selectLocButtonPanel.add(selectLocButton);

		selectLocButton.addActionListener((ActionEvent e) -> {
			JFileChooser locFileChooser = new JFileChooser();
			locFileChooser.setCurrentDirectory(lastUsedDirectory.toFile());
			// Allow file selection
			locFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			locFileChooser.setDialogTitle("Select and name your exported document");

			int result = locFileChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = locFileChooser.getSelectedFile();
				String path = selectedFile.getAbsolutePath();

				// Check and append file extension if necessary
				if (!path.toLowerCase().endsWith(".doc") && !path.toLowerCase().endsWith(".docx")) {
					path += ".docx";
					selectedFile = new File(path);
				}

				locTextField.setText(selectedFile.getAbsolutePath());
				lastUsedDirectory = selectedFile.toPath().getParent();
			}
		});
        return selectLocButtonPanel;
	}
	
	
	/**
	 * Panel that opens a file dialog to select a reference file
	 * @param refTextField Reference file to use
	 * @return
	 */
	private JPanel selectReferenceButtonPanel(JTextField refTextField) {
		JButton selectRefButton = new JButton("Select");
		JPanel selectRefButtonPanel = new JPanel();
		selectRefButtonPanel.add(selectRefButton);

		selectRefButton.addActionListener((ActionEvent e) -> {
			JFileChooser refChooser = new JFileChooser();
			refChooser.setCurrentDirectory(lastUsedDirectory.toFile());
			refChooser.setDialogTitle("Select Reference Material");
			refChooser.setFileFilter(new FileNameExtensionFilter("Word Documents (*.doc, *.docx)", "doc", "docx"));
			int result = refChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = refChooser.getSelectedFile();
				lastUsedDirectory = selectedFile.toPath().getParent();
				refTextField.setText(selectedFile.getAbsolutePath());
			}
		});

		return selectRefButtonPanel;
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
        exportButtonPanel.add(exportButton, exportButtonConstraint);
                
        // listens for when the user selects exportButton
        exportButton.addActionListener((ActionEvent e) -> {
            if ((testButton.isSelected() == true) || (keyButton.isSelected() == true))
            {
                // export the quiz 
                if (testButton.isSelected() == true)
                {
                    String templateLocation = templateTextField.getText();
                    Path templatePath = Paths.get(templateLocation);
                    
                    String exportLocation = locationTextField.getText();
                    Path exportPath = Paths.get(exportLocation);
                    
                    String referenceLocation = referenceTextField.getText();
                    Path referencePath = referenceLocation != null && !referenceLocation.isBlank()
                        ? Paths.get(referenceLocation)
                        : null;

                    GeneratedQuiz generatedQuiz = quiz.getGenerated();
                    try
                    {
                        exportFile.exportTest(generatedQuiz, templatePath, exportPath, replacements, referencePath, false);
                    }
                    catch (IOException n)
                    {
                        errorDialog(n.getMessage());
                    }
                }
                // export the answer key
                if (keyButton.isSelected() == true)
                {
                    String templateLocation = templateTextField.getText();
                    Path templatePath = Paths.get(templateLocation);
                    
                    String exportLocation = locationTextField.getText();
                    Path exportPath = Paths.get(exportLocation);
                    
                    String referenceLocation = referenceTextField.getText();
                    Path referencePath = referenceLocation != null && !referenceLocation.isBlank()
                        ? Paths.get(referenceLocation)
                        : null;

                    GeneratedQuiz generatedQuiz = quiz.getGenerated();
                    try
                    {
                        exportFile.exportTest(generatedQuiz, templatePath, exportPath, replacements, referencePath, true);
                    }
                    catch (IOException n)
                    {
                        
                        errorDialog(n.getMessage());
                    }
                }
                exportFrame.dispose();
            }
        }); 
                
        return exportButtonPanel;
    }
    
    private void errorDialog(String error)
    {
        JFrame errorFrame = new JFrame();
        JOptionPane.showMessageDialog(errorFrame, error, "Error", JOptionPane.ERROR_MESSAGE);  
    }
    
    /**
     * Creates the button panel that allows the user to expand/collapse the 
     * text replacement panel
     * 
     * @return replacement button panel
     */
    private JPanel replacementButtonPanel()
    {
        JLabel replacementLabel = new JLabel("Replacements");
        
        JButton showButton = new JButton("˅");
        JButton hideButton = new JButton("˄");
             
        cardPanel = new JPanel(new CardLayout());
        
        JPanel showButtonPanel = new JPanel();
        showButtonPanel.add(showButton);
        cardPanel.add(showButtonPanel, "Show");
        
        JPanel hideButtonPanel = new JPanel();
        hideButtonPanel.add(hideButton);
        cardPanel.add(hideButtonPanel, "Hide");
        
        // contains replacementLabel and cardPanel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints(); 
        
        // place cardPanel on the left side of buttonPanel
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 0;
        buttonPanel.add(cardPanel, buttonConstraint);
        
        // place replacementLabel on the right side of buttonPanel
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 1;
        labelConstraint.gridy = 0;
        buttonPanel.add(replacementLabel, labelConstraint);
        
        CardLayout layout = (CardLayout) (cardPanel.getLayout());
        layout.show(cardPanel, "Show");
        
        // listens for when showButton is clicked
        showButton.addActionListener((ActionEvent e) -> {
            // show the generatePanel and hideButton
            replacementPanel.setVisible(true);
            layout.show(cardPanel, "Hide");
        }); 
            
        hideButton.addActionListener((ActionEvent e) -> {
            // hide the generatePanel and show the showButton
            replacementPanel.setVisible(false);
            layout.show(cardPanel, "Show");
        }); 
        return buttonPanel;
    }
    
    /**
     * Creates the panel that allows the user to enter in text replacement information
     * @return 
     */
    private JPanel replacementPanel()
    {
        replacements = new TemplateReplacements();
        GeneratePanel generate = new GeneratePanel(180, replacements);
        JButton applyButton = new JButton("Apply");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);
        
        // contains generate and buttonPanel
        replacementPanel = new JPanel(new GridBagLayout());
        GridBagConstraints generateConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        
        // places generate at the top of replacementPanel
        generateConstraint.fill = GridBagConstraints.HORIZONTAL;
        generateConstraint.gridx = 0;
        generateConstraint.gridy = 0;
        replacementPanel.add(generate, generateConstraint);
        
        // places buttonPanel below generate
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 1;
        replacementPanel.add(buttonPanel, buttonConstraint);
        
        replacementPanel.setVisible(false);
        
        // listens for when applyButton is clicked
        applyButton.addActionListener((ActionEvent e) -> {
            // hide replacementPanel and show showButton
            CardLayout layout = (CardLayout) (cardPanel.getLayout());
            replacementPanel.setVisible(false);
            layout.show(cardPanel, "Show");
            generate.collectData();
        }); 
               
        return replacementPanel;
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