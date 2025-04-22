package io.github.csgroup.quizmaker.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;
import io.github.csgroup.quizmaker.ui.components.QuestionTable;
import io.github.csgroup.quizmaker.ui.dialogs.CreateBankDialog;
import io.github.csgroup.quizmaker.ui.dialogs.RemoveBankDialog;
import io.github.csgroup.quizmaker.ui.quizzes.QuestionsDialog;

public class GeneratedTab extends JComponent
{

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
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(215);

		//splitPane.setPreferredSize(new Dimension(522, 535));
		
		this.add(splitPane, BorderLayout.CENTER);
	}

	private JPanel createSidebar(Project p)
	{
		JPanel sidebar = new JPanel(new GridBagLayout());

		JLabel label = new JLabel("Assignments");

		var assignments = project.getGeneratedQuizModel();
		list = new JList<GeneratedQuiz>(assignments);  
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(215, 455));

		GridBagConstraints labelConstraint = new GridBagConstraints();
		GridBagConstraints scrollPaneConstraint = new GridBagConstraints();
		//GridBagConstraints buttonConstraint = new GridBagConstraints();

		// places quiBankLabel at the top of the panel
		labelConstraint.fill = GridBagConstraints.HORIZONTAL;
		labelConstraint.gridx = 0;
		labelConstraint.gridy = 0;
		sidebar.add(label, labelConstraint);

		// places bankScrollPanel at the bottom of the panel
		scrollPaneConstraint.fill = GridBagConstraints.HORIZONTAL;
		scrollPaneConstraint.gridx = 0;
		scrollPaneConstraint.gridy = 1;
		sidebar.add(scrollPane, scrollPaneConstraint);

		//        // add and remove button panel
		//        JPanel buttonPanel = bankButtonPanel(bankList);
		//        buttonConstraint.fill = GridBagConstraints.HORIZONTAL;
		//        buttonConstraint.gridx = 0;
		//        buttonConstraint.gridy = 2;
		//        bankPanel.add(buttonPanel, buttonConstraint);  

		// once a bank has been added select it on bankList
		assignments.addListDataListener(new ListDataListener() {
			@Override
			public void intervalAdded(ListDataEvent e) {
				int index = e.getIndex0();
				//list.setSelectedIndex(index);
				//                table.setVisible(true);
				//                bankLabelPanel.setVisible(true);
			}
			@Override
			public void intervalRemoved(ListDataEvent e) {}

			@Override
			public void contentsChanged(ListDataEvent e) {}                                             
		});  

		return sidebar;
	}

	private JPanel createDetailsPanel(Project p)
	{
		JPanel details = new JPanel();
		
		list.addListSelectionListener((ListSelectionEvent e) -> {
			System.out.println("Updating!!");
			populateDetailsPanel(details, p.getGeneratedQuiz(e.getFirstIndex()));
			
		});
		
		
		return details;
	}
	
	private void populateDetailsPanel(JPanel details, GeneratedQuiz quiz)
	{
		details.removeAll(); // Remove the previous components
		
		if (quiz == null)
			return;
		
		System.out.println("2");
		
		JPanel contents = new JPanel();
		contents.setLayout(new BoxLayout(contents, BoxLayout.PAGE_AXIS));
		
		JScrollPane scroll = new JScrollPane(contents);
		
		JLabel title = new JLabel(quiz.toString());
		contents.add(title);
		
		contents.add(createQuestionTable(quiz));
		
		details.add(scroll);
		
		details.validate();
	}
	
	
	private JPanel createQuestionTable(GeneratedQuiz quiz)
    {        
        String[] bankTableHeaders = {"Questions", "Answers"};
        int numRows = 29;
                
        var questionTable = new QuestionTable(bankTableHeaders, numRows);
        questionTable.setTableSize(520, 487);
        questionTable.setTableRowHeight(16);
        //questionTable.setVisible(false);
              
        JPanel tablePanel = new JPanel();
        tablePanel.add(questionTable); 
        tablePanel.setPreferredSize(new Dimension(521, 490/2));
        
        var table = questionTable.getTable();
        
        populateQuestionTable(quiz, questionTable);
        
//        table.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e)
//            { 
//                int index = list.getSelectedIndex();
//                //QuestionBank bank = project.getBank(index);
//                
//                // if the user double clicks the table show the dialog that allows them 
//                // to add a question
//                if (e.getClickCount() == 2)
//                {
//                    //QuestionsDialog questionDialog = new QuestionsDialog(bank, questionTable);
//                    //questionDialog.show();
//                }    
//                // if the user right clicks the table allow them to add or delete
//                // a question
//                if (SwingUtilities.isRightMouseButton(e))
//                {
//                    int row = table.getSelectedRow();
//                    if (row >= 0)
//                    {
//                        try
//                        {
//                            //Question question = bank.getQuestion(row);                        
//                            //JPopupMenu menu = quizPopUpMenu(bank, question, row);
//                            //menu.show(e.getComponent(), e.getX(), e.getY());                            
//                        }
//                        catch(IndexOutOfBoundsException n)
//                        {
//                            //JPopupMenu menu = quizPopUpMenu(bank, null, row);
//                            //menu.show(e.getComponent(), e.getX(), e.getY());  
//                        }
//                    }                    
//                }               
//            }        
//        });
                               
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
