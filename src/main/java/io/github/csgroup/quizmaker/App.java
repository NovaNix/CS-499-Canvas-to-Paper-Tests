package io.github.csgroup.quizmaker;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.*;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.ui.UIManager;
import io.github.csgroup.quizmaker.word.WordExporter;

/**
 * The central management point of the application. <br>
 * Contains the start of the application, and is responsible for starting the UI and managing some global state.
 */
public class App 
{

	public static final Logger logger = LoggerFactory.getLogger(App.class);

	@SuppressWarnings("unused")
	private static UIManager ui;

	private static Project project;
	

	public static void main(String[] args)
	{
		logger.info("Starting Application");
		
		//Word test, keep commented for now unless testing the word exporting or until correctly implemented in the UI
		//logger.info("Testing word exporting!");
		//WordExporter wordExportTest = new WordExporter();
		//wordExportTest.exportTest(true);

		// Create a new blank project
		project = new Project();

		// Initiate the UI code
		SwingUtilities.invokeLater(() -> {
			// The UI needs to be started on the event threads
			ui = new UIManager();
			ui.createHomeFrame();
		});
	}

	public static Project getCurrentProject()
	{
		return project;
	}

	// TODO add event listeners

}
