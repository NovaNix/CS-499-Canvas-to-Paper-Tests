package io.github.csgroup.quizmaker.ui.dialogs;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import io.github.csgroup.quizmaker.App;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;

/**
 * A dialog for showing which {@link GeneratedQuiz GeneratedQuizzes} a {@link Question} is used in.
 * 
 * @author Michael Nix
 */
public class QuestionUsageDialog 
{
	private JFrame frame;
	
	public QuestionUsageDialog(Question q)
	{
		frame = new JFrame("Question Usage");
        frame.setSize(450, 240);
        
        JPanel contents = new JPanel(new BorderLayout());
        
        contents.add(new JLabel(q.getTitle()), BorderLayout.NORTH);
        
        contents.add(getTrackingPanel(q), BorderLayout.CENTER);
        
        frame.add(contents);
	}
	
	private JPanel getTrackingPanel(Question q)
	{
		JPanel tracking = new JPanel(new BorderLayout());
		
		JLabel label = new JLabel("This question is used in the following assignments:");
		
		JList<GeneratedQuiz> assignments = new JList<GeneratedQuiz>(getUsageListModel(q));
		JScrollPane scroll = new JScrollPane(assignments);
		
		tracking.add(label, BorderLayout.NORTH);
		tracking.add(scroll, BorderLayout.CENTER);
		
		return tracking;
	}
	
	private ListModel<GeneratedQuiz> getUsageListModel(Question q)
	{
		var model = new DefaultListModel<GeneratedQuiz>();
		
		List<GeneratedQuiz> usage = App.getCurrentProject().getGeneratedQuizzesWith(q);
		
		for (var item : usage)
		{
			model.addElement(item);
		}
		
		return model;
	}
	
    public void show()
    {
        // makes the JFrame appear in the center of the screen
        frame.setLocationRelativeTo(null);
        // makes the JFrame visible
        frame.setVisible(true);
    }
}
