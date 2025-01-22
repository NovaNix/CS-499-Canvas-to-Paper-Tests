package io.github.csgroup.quizmaker;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.ui.UIManager;

public class App 
{

	public static final Logger logger = LoggerFactory.getLogger(App.class);
	
	private static UIManager ui;
	
	private static Project project;
	
	public static void main(String[] args)
	{
		logger.info("Starting Application");
		
		// Create a new blank project
		project = new Project();
		
		// Initiate the UI code
		SwingUtilities.invokeLater(() -> {
			// The UI needs to be started on the event threads
			ui = new UIManager();
		});
	}

	// TODO add event listeners
	
}
