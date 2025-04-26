package io.github.csgroup.quizmaker.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;
import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;
import io.github.csgroup.quizmaker.data.quiz.QuizMetadata.MetadataType;
import io.github.csgroup.quizmaker.ui.components.GeneratePanel;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;
import io.github.csgroup.quizmaker.ui.dialogs.ExportWordDialog;
import java.awt.Insets;

/**
 * The panel placed inside of the "Assignments" tab
 * 
 * @author Michael Nix
 */
public class GeneratedTab extends JComponent
{
	private static final long serialVersionUID = 728471021049458310L;
    
	private Project project;
    
	private JList<GeneratedQuiz> list; 
       	
	public GeneratedTab(Project p)
	{
		this.project = p;
		
		// Attach the UI
		
		this.setLayout(new BorderLayout());
		
		JPanel sidebar = createSidebar(p);
		JPanel details = createDetailsPanel(p);
		
		var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, details);
        splitPane.setOneTouchExpandable(false);
		splitPane.setDividerLocation(260);
		
		this.add(splitPane, BorderLayout.CENTER);
	}

	private JPanel createSidebar(Project p)
    {
		JLabel label = new JLabel("Assignments");

		var assignments = project.getGeneratedQuizModel();
		list = new JList<GeneratedQuiz>(assignments);  
		JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(260, 600));
        
        JPanel sidebar = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraint = new GridBagConstraints();
        GridBagConstraints listConstraint = new GridBagConstraints();
        
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 0;
        labelConstraint.insets = new Insets(5, 0, 0, 0);
        sidebar.add(label, labelConstraint);
        
        listConstraint.fill = GridBagConstraints.HORIZONTAL;
        listConstraint.gridx = 0;
        listConstraint.gridy = 1;
        sidebar.add(scrollPane, listConstraint);

		return sidebar;
	}

	private JPanel createDetailsPanel(Project p)
	{
		JPanel details = new JPanel();
		
		list.addListSelectionListener((ListSelectionEvent e) -> {
			GeneratedQuiz quiz = list.getSelectedValue();	
            
			populateDetailsPanel(details, quiz);
		});
				
		return details;
	}
	
	private void populateDetailsPanel(JPanel details, GeneratedQuiz quiz)
	{
		details.removeAll(); // Remove the previous components
		
		if (quiz == null)
			return;       
                		               
        JPanel contents = new JPanel(new GridBagLayout());
        GridBagConstraints titleConstraint = new GridBagConstraints();
        GridBagConstraints dataConstraint = new GridBagConstraints();
        GridBagConstraints tableConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        
        JLabel title = new JLabel(quiz.toString());
        JPanel test = new JPanel();
        test.add(title);
        titleConstraint.fill = GridBagConstraints.HORIZONTAL;
        titleConstraint.gridx = 0;
        titleConstraint.gridy = 0;
        titleConstraint.insets = new Insets(10, 0, 10, 0);
        contents.add(test, titleConstraint);        
      
        dataConstraint.fill = GridBagConstraints.HORIZONTAL;
        dataConstraint.gridx = 0;
        dataConstraint.gridy = 1;
        contents.add(createMetadataPanel(quiz), dataConstraint);
        
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 0;
        tableConstraint.gridy = 2;
        tableConstraint.insets = new Insets(0, 0, 65, 0);
        contents.add(createQuestionTable(quiz), tableConstraint);        
      
        JPanel buttonPanel = exportButtonPanel(quiz);
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 3;
        contents.add(buttonPanel, buttonConstraint);
		
		details.add(contents);		
		details.validate();
	}
	
	private JPanel createMetadataPanel(GeneratedQuiz quiz)
	{
        GeneratePanel size = new GeneratePanel();
        int dataSize = size.getDataSize() + 1;
        GridBagConstraints labelConstraints[] = new GridBagConstraints[dataSize];
        GridBagConstraints fieldConstraints[] = new GridBagConstraints[dataSize];
        JPanel labelPanel = new JPanel(new GridBagLayout());
        JPanel fieldPanel = new JPanel(new GridBagLayout());        
				
		QuizMetadata data = quiz.getQuizMetadata();
        		
		int y = 0;		
		for (var type : MetadataType.values())
		{
			String value = data.getValue(type);
			
			// Make the left label
			JLabel label = new JLabel(type.getDisplayName() + ": ");
            
            labelConstraints[y] = new GridBagConstraints();
            labelConstraints[y].fill = GridBagConstraints.HORIZONTAL;
            labelConstraints[y].gridx = 0;
            labelConstraints[y].gridy = y;
            
            if (y == 0)
            {
                labelConstraints[y].insets = new Insets(7, 0, 15, 3);
            }
            else
            {
                labelConstraints[y].insets = new Insets(0, 0, 15, 3);
            }
                     
            labelPanel.add(label, labelConstraints[y]);
            
            JTextField field = new JTextField();
			field.setText(value);
            field.setPreferredSize(new Dimension(170, 24));
            field.setFocusable(false); 
			//StringStoreBinder binder = new StringStoreBinder();
            
            fieldConstraints[y] = new GridBagConstraints();
            fieldConstraints[y].fill = GridBagConstraints.HORIZONTAL;
            fieldConstraints[y].gridx = 0;
            fieldConstraints[y].gridy = y;
            fieldConstraints[y].insets = new Insets(0, 0, 7, 355);
            fieldPanel.add(field, fieldConstraints[y]);                       
                       			
			y++;                       
		}
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints fieldPanelConstraint = new GridBagConstraints();
        GridBagConstraints labelPanelConstraint = new GridBagConstraints();
        
        labelPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelPanelConstraint.gridx = 0;
        labelPanelConstraint.gridy = 0;
        panel.add(labelPanel, labelPanelConstraint); 
        
        fieldPanelConstraint.fill = GridBagConstraints.HORIZONTAL;
        fieldPanelConstraint.gridx = 1;
        fieldPanelConstraint.gridy = 0;
        panel.add(fieldPanel, fieldPanelConstraint);   
                       		
		return panel;
	}
	
	private JPanel createQuestionTable(GeneratedQuiz quiz)
    {        
        String[] bankTableHeaders = {"Questions", "Answers"};
        int numRows = 13;
                
        var questionTable = new QuestionTable(bankTableHeaders, numRows);
        questionTable.setTableSize(620, 235);
        questionTable.setTableRowHeight(16);
              
        JPanel tablePanel = new JPanel();
        tablePanel.add(questionTable); 
        tablePanel.setPreferredSize(new Dimension(621, 245));
        
        populateQuestionTable(quiz, questionTable);
                               
        return tablePanel;
    }
    
    private JPanel exportButtonPanel(GeneratedQuiz quiz)
    {
        JButton exportButton = new JButton("Export");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exportButton);
        
        exportButton.addActionListener((ActionEvent e) -> {
            // show the export dialog
            ExportWordDialog exportDialog = new ExportWordDialog(quiz);
            exportDialog.show();
        });
        
        return buttonPanel;
    }
	
	private void populateQuestionTable(GeneratedQuiz quiz, QuestionTable table)
    {
		for (int i = 0; i < quiz.getQuestions().size(); i++)
		{
			Question q = quiz.getQuestions().get(i);
			String answer = q.getAnswerString();
			table.setValue(q, i, 0);  
			table.setValue(answer, i, 1);
		}           
    }	
}