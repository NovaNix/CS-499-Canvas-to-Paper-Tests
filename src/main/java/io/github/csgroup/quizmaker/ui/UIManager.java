package io.github.csgroup.quizmaker.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * The central manager for the user interface. 
 * 
 * @author Emily Palmer
 */
public class UIManager 
{

	public static final Logger logger = LoggerFactory.getLogger(UIManager.class);
	
	public UIManager()
	{
		logger.info("Starting UI");
	}
        
        // add (probably javadoc comment) about what this method does      
        public void createHomeFrame() 
        {
            // main JFrame that will host all JPanels for the system
            JFrame homeFrame = new JFrame("Canvas to Paper Tests");
               
            //size of the JFrame (width, height)
            homeFrame.setSize(685, 495);
            
            // will contain the menus "File" and "About"
            JMenuBar menuBar = new JMenuBar();
            // "File" menu
            JMenu fileMenu = new JMenu("File");
            // items for the user to select in the "File" menu
            JMenuItem importFileItem = new JMenuItem("Import QTI Files");
            JMenuItem exportFileItem = new JMenuItem("Export QTI Files");
            JMenuItem exportDocumentItem = new JMenuItem("Export Word Document");            
            // add items to the JMenu fileMenu
            fileMenu.add(importFileItem);
            fileMenu.add(exportFileItem);
            fileMenu.add(exportDocumentItem);       
            // add JMenu "File" to the JMenuBar menuBar
            menuBar.add(fileMenu);
            
            // "About" menu
            JMenu aboutMenu = new JMenu("About");               
            // add JMenu "About" to the JMenuBar menuBar
            menuBar.add(aboutMenu);
        
            // add the JMenuBar menuBar to homeFrame
            homeFrame.setJMenuBar(menuBar);
                                                
            // if the JFrame is closed terminate the running program
            homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // makes the JFrame appear in the center of the screen
            homeFrame.setLocationRelativeTo(null);
            // make the JFrame visible
            homeFrame.setVisible(true);  
        }	
}
