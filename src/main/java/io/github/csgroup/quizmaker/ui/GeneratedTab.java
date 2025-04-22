package io.github.csgroup.quizmaker.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
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
		splitPane.setDividerLocation(215);
		
		this.add(splitPane, BorderLayout.CENTER);
	}

	private JPanel createSidebar(Project p)
	{
		JPanel sidebar = new JPanel(new BorderLayout());

		JLabel label = new JLabel("Assignments");

		var assignments = project.getGeneratedQuizModel();
		list = new JList<GeneratedQuiz>(assignments);  
		JScrollPane scrollPane = new JScrollPane(list);

		sidebar.add(label, BorderLayout.NORTH);
		sidebar.add(scrollPane, BorderLayout.CENTER); 

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
        
        
        JButton exportButton = new JButton("Export");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exportButton);
		       
        JPanel contents = new JPanel(new GridBagLayout());
        GridBagConstraints titleConstraint = new GridBagConstraints();
        GridBagConstraints dataConstraint = new GridBagConstraints();
        GridBagConstraints tableConstraint = new GridBagConstraints();
        GridBagConstraints buttonConstraint = new GridBagConstraints();
        
        JLabel title = new JLabel(quiz.toString());
        titleConstraint.fill = GridBagConstraints.HORIZONTAL;
        titleConstraint.gridx = 0;
        titleConstraint.gridy = 0;
        titleConstraint.insets = new Insets(20, 0, 10, 0);
        contents.add(title, titleConstraint);        
      
        dataConstraint.fill = GridBagConstraints.HORIZONTAL;
        dataConstraint.gridx = 0;
        dataConstraint.gridy = 1;
        contents.add(createMetadataPanel(quiz), dataConstraint);
        
        tableConstraint.fill = GridBagConstraints.HORIZONTAL;
        tableConstraint.gridx = 0;
        tableConstraint.gridy = 2;
        tableConstraint.insets = new Insets(0, 0, 100, 110);
        contents.add(createQuestionTable(quiz), tableConstraint);        
      
        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraint.gridx = 0;
        buttonConstraint.gridy = 3;
        buttonConstraint.insets = new Insets(0, 0, 0, 60);
        contents.add(buttonPanel, buttonConstraint);
        
        exportButton.addActionListener((ActionEvent e) -> {
            // show the export dialog
            ExportWordDialog exportDialog = new ExportWordDialog(quiz);
            exportDialog.show();
        });
        
        
		/*JPanel contents = new JPanel();
		contents.setLayout(new BoxLayout(contents, BoxLayout.PAGE_AXIS));
		
		// Add the header
		
		JLabel title = new JLabel(quiz.toString());
		contents.add(title);
		
		// Add the metadata fields
		
		contents.add(createMetadataPanel(quiz));
		
		// Add the question table
		
		contents.add(createQuestionTable(quiz));
		
		// Add the export button
		
		JButton exportButton = new JButton("Export");
		
		exportButton.addActionListener((ActionEvent e) -> {
            // show the export dialog
            ExportWordDialog exportDialog = new ExportWordDialog(quiz);
            exportDialog.show();
        });
		
		contents.add(exportButton);*/
		
		details.add(contents);
		
		details.validate();
	}
	
	private JPanel createMetadataPanel(GeneratedQuiz quiz)
	{
		JPanel panel = new JPanel(new GridBagLayout());
        JPanel finalPanel = new JPanel(new GridBagLayout());
		
		QuizMetadata data = quiz.getQuizMetadata();
		
		int y = 0;
		
		for (var type : MetadataType.values())
		{
			String value = data.getValue(type);
			
			// Make the left label
			JLabel label = new JLabel(type.getDisplayName() + ": ");
			
			// Make the right box
			
			JTextField field = new JTextField();
			field.setText(value);
			
			field.setEditable(false); // CHANGE THIS LATER
			//StringStoreBinder binder = new StringStoreBinder();
			
			
			GridBagConstraints labelConstraints = new GridBagConstraints();
			labelConstraints.gridx = 0;
			labelConstraints.gridy = y;
            labelConstraints.insets = new Insets(0, 0, 5, 0);
			GridBagConstraints fieldConstraints = new GridBagConstraints();
			fieldConstraints.gridx = 1;
			fieldConstraints.gridy = y;
          	fieldConstraints.insets = new Insets(0, 0, 5, 160);
            
			panel.add(label, labelConstraints);
			panel.add(field, fieldConstraints);
                        			
			y++;                       
		}
        		
		return panel;
	}
	
	private JPanel createQuestionTable(GeneratedQuiz quiz)
    {        
        String[] bankTableHeaders = {"Questions", "Answers"};
        int numRows = 13;
                
        var questionTable = new QuestionTable(bankTableHeaders, numRows);
        questionTable.setTableSize(570, 235);
        questionTable.setTableRowHeight(16);
              
        JPanel tablePanel = new JPanel();
        tablePanel.add(questionTable); 
        tablePanel.setPreferredSize(new Dimension(571, 245));
        
        populateQuestionTable(quiz, questionTable);
                               
        return tablePanel;
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