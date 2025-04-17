package io.github.csgroup.quizmaker.ui.components;

import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;
import io.github.csgroup.quizmaker.word.TemplateReplacements;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.swing.JTextField;


/**
 * Creates the panel that allows the user to enter in the generated quiz's information
 * or replacement text
 * 
 * @author Emily Palmer
 */
public class GeneratePanel extends JComponent
{
    private JTextField[] textFields;
    private JPanel[] fieldPanels;
    private JLabel[] panelLabels;
    private GridBagConstraints[] fieldPanelConstraint;
    private GridBagConstraints[] labelConstraint;
    private final Map<QuizMetadata.MetadataType, String> initialValues = new LinkedHashMap<>();
    private final BiConsumer<QuizMetadata.MetadataType, String> onSubmit;
    private final boolean includeDynamicTypes;
    private final int width;
    
    public GeneratePanel(int setWidth, QuizMetadata metadata)
    {
        this(setWidth, metadata, metadata::setValue, false);
    }
    
    public GeneratePanel(int setWidth, TemplateReplacements replacements)
    {
        this(setWidth, replacements, (type, value) -> replacements.setReplacementString(type, value), true);
    }
    
    private GeneratePanel(int setWidth, Object dataSource, BiConsumer<QuizMetadata.MetadataType, String> onSubmit, boolean includeDynamics)
    {
    	this.width = setWidth;
    	this.onSubmit = onSubmit;
    	this.includeDynamicTypes = includeDynamics;
    	
    	for(QuizMetadata.MetadataType type : QuizMetadata.MetadataType.values())
    	{
    		if(!type.isDynamic() || includeDynamics)
    		{
    			if(dataSource instanceof QuizMetadata meta)
    			{
    				String safe = meta.getStore(type) != null ? meta.getStore(type).get() : "";
    				initialValues.put(type, safe);
    			} else if (dataSource instanceof TemplateReplacements tr)
    			{
    				initialValues.put(type, tr.getReplacementText(type));
    			}
    		}
    	}
    	generateDialog();
    }
    /**
     * Adds generatePanel to the component
     */
    private void generateDialog()
    {        
        this.setLayout(new GridBagLayout());
        
        JPanel generatePanel = generatePanel(); 
        this.add(generatePanel);
    }
    
    /**
     * Gets the number of non-dynamic quiz metadata types
     * 
     * @return the number of non-dynamic quiz metadata types
     */
    private int getDataSize()
    {
        int size = 0;
        for (QuizMetadata.MetadataType name : QuizMetadata.MetadataType.values())
        {
           if (name.isDynamic() == false)
           {
               size++;
           }
        }
        
        return size;
    }
    
    /**
     * Creates the labels for each text field
     */
    private void createLabels()
    {
        int size = initialValues.size();
        panelLabels = new JLabel[size];
        int i = 0;
        for (QuizMetadata.MetadataType type : initialValues.keySet())
        {
        	panelLabels[i] = new JLabel(type.getDisplayName() + ":");
        	i++;
        }
    }
            
    
    /**
     * Creates the text fields for the panel
     */
    private void createTextFields()
    {
        int size = initialValues.size();
        textFields = new JTextField[size];
        fieldPanels = new JPanel[size];
        int i = 0;
        for (String initial : initialValues.values())
        {
            textFields[i] = new JTextField(initial != null ? initial : "");
            textFields[i].setPreferredSize(new Dimension(width, 25));
            fieldPanels[i] = new JPanel();
            fieldPanels[i].add(textFields[i]);
            i++;
        }
    }
    
    /**
     * Organizes the text fields on a panel with GridBagLayout
     * 
     * @return the text field panel
     */
    private JPanel textFieldPanel()
    {
        createTextFields();
        
        int size = getDataSize();
        JPanel textFieldPanel = new JPanel(new GridBagLayout());
        fieldPanelConstraint = new GridBagConstraints[size];
        for (int i = 0; i < size; i++)
        {
            fieldPanelConstraint[i] = new GridBagConstraints();
            fieldPanelConstraint[i].fill = GridBagConstraints.HORIZONTAL;
            fieldPanelConstraint[i].gridx = 0;
            fieldPanelConstraint[i].gridy = i;
            textFieldPanel.add(fieldPanels[i], fieldPanelConstraint[i]);
        }
        
        return textFieldPanel;
    }
    
    /**
     * Organizes the labels on a panel using GridBagLayout
     * 
     * @return the label panel
     */
    private JPanel labelPanel()
    {
        createLabels();
        
        int size = getDataSize();
        JPanel labelPanel = new JPanel(new GridBagLayout());
        labelConstraint = new GridBagConstraints[size];
        for (int i = 0; i < size; i++)
        {
            labelConstraint[i] = new GridBagConstraints();
            labelConstraint[i].fill = GridBagConstraints.HORIZONTAL;
            labelConstraint[i].gridx = 0;
            labelConstraint[i].gridy = i;
            
            if (i == 0)
            {
                labelConstraint[i].insets = new Insets(20, 0, 15, 0);
            }
            else
            {
                labelConstraint[i].insets = new Insets(0, 0, 20, 0);
            }
            
            labelPanel.add(panelLabels[i], labelConstraint[i]);
        }
        
        return labelPanel;
    }
    
    /**
     * Creates a panel containing both the text fields and labels
     * 
     * @return the panel with the labels and text fields
     */
    private JPanel generatePanel()
    {
        // contains labelPanel and textFieldPanel
        JPanel generatePanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelPanelConstraint = new GridBagConstraints();
        GridBagConstraints textFieldConstraint = new GridBagConstraints();
        
        // places labelPanel on the left side of generatePanel
        JPanel labelPanel = labelPanel();
        labelPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelPanelConstraint.gridx = 0;
        labelPanelConstraint.gridy = 0;
        generatePanel.add(labelPanel, labelPanelConstraint); 
        
        // places textFieldPanel on the right side of generatePanel
        JPanel textFieldPanel = textFieldPanel();
        textFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        textFieldConstraint.gridx = 1;
        textFieldConstraint.gridy = 0;
        generatePanel.add(textFieldPanel, textFieldConstraint); 
                    
        return generatePanel;
    }
    
    /**
     * Collects the data entered from the user and passes it to the QuizMetadata class
     */
    public void collectData()
    {
        int i = 0;
        for (QuizMetadata.MetadataType type : initialValues.keySet())
        {
        	String value = textFields[i].getText();
        	onSubmit.accept(type, value);
        	i++;
        }
    }
}
