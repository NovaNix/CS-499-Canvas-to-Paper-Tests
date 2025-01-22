package cs499.group7;

import javax.swing.SwingUtilities;

import cs499.group7.data.Project;
import cs499.group7.ui.UIManager;

public class App 
{

	private static UIManager ui;
	
	private static Project project;
	
	public static void main(String[] args)
	{
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
