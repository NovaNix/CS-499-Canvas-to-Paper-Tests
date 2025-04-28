package io.github.csgroup.quizmaker;

import java.io.IOException;

import javax.swing.SwingUtilities;

import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formdev.flatlaf.FlatLightLaf;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.ui.GUIManager;
import io.github.csgroup.quizmaker.word.WordExporter;

/**
 * The central management point of the application. <br>
 * Contains the start of the application, and is responsible for starting the UI and managing some global state.
 */
public class App 
{

	public static final Logger logger = LoggerFactory.getLogger(App.class);

	private static GUIManager ui;
	
	private static Project project;
	

	public static void main(String[] args)
	{
		AnsiConsole.systemInstall();
		logger.info("Starting Application");

		// Create a new blank project
		project = new Project();        

		// Initiate the UI code
		
		FlatLightLaf.setup();
		
		SwingUtilities.invokeLater(() -> {
            // The UI needs to be started on the event threads
            ui = new GUIManager();
            ui.createHomeFrame();
		});
	}

	public static Project getCurrentProject()
	{
		return project;
	}

	// TODO add event listeners

}
