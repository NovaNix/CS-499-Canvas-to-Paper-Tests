package io.github.csgroup.quizmaker.ui;

import io.github.csgroup.quizmaker.ui.banks.BankPanel;
import io.github.csgroup.quizmaker.ui.dialogs.ImportQTIDialog;
import io.github.csgroup.quizmaker.ui.dialogs.ExportQTIDialog;
import io.github.csgroup.quizmaker.App;
import io.github.csgroup.quizmaker.data.Project;

import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 * The central manager for the user interface. 
 * 
 * @author Emily Palmer
 */
public class GUIManager 
{    
	public static final Logger logger = LoggerFactory.getLogger(GUIManager.class);
	
	public GUIManager()
	{
		logger.info("Starting UI");
	}
        
    /**
     * Creates a frame that will host multiple panels and the 
     * file menu for the system
     */      
    public void createHomeFrame() 
    {
        // main JFrame that will host all JPanels for the system
        JFrame homeFrame = new JFrame("Canvas to Paper Tests");
        homeFrame.setResizable(false);
               
        //size of the JFrame (width, height)
        homeFrame.setSize(775, 625);
            
        // will contain the menus "File" and "About"
        JMenuBar menuBar = new JMenuBar();
        // "File" menu
        JMenu fileMenu = new JMenu("File");
        // items for the user to select in the "File" menu
        JMenuItem importFileItem = new JMenuItem("Import QTI File");
        JMenuItem exportFileItem = new JMenuItem("Export QTI File");           
        // add items to the JMenu fileMenu
        fileMenu.add(importFileItem);
        fileMenu.add(exportFileItem);       
        // add JMenu "File" to the JMenuBar menuBar
        menuBar.add(fileMenu);
            
        // "About" menu
        JMenu aboutMenu = new JMenu("About");               
        // add JMenu "About" to the JMenuBar menuBar
        menuBar.add(aboutMenu);
        
        // add the JMenuBar menuBar to homeFrame
        homeFrame.setJMenuBar(menuBar);
            
        // panel that will hold the quiz bank list and quiz bank questions 
        // and answers     
        Project project = App.getCurrentProject();
        BankPanel bankPanel = new BankPanel(project);
        QuizPanel quizPanel = new QuizPanel(project);
                
        // adds two tabs two the homeFrame
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Quizzes", quizPanel);
        tabs.addTab("Question Banks", bankPanel);
        // setting the looks and feel of the tabs
        tabs.setBackgroundAt(0, new Color(237, 237, 237));
        tabs.setBackgroundAt(1, new Color(237, 237, 237));
        UIManager.put("TabbedPane.contentAreaColor", new Color(237, 237, 237));
        UIManager.put("TabbedPane.highlight", Color.GRAY);
        tabs.setUI(new BasicTabbedPaneUI());
        // add the tabs to homeFrame
        homeFrame.add(tabs);
            
        // listens for when the user selects importFileItems
        importFileItem.addActionListener((ActionEvent e) -> {
            // display the frame that lets the user attach their QTI files
            ImportQTIDialog importFrame = new ImportQTIDialog(project);
            importFrame.show();
        });
        
        // listens for when the user selects exportFileItem
        exportFileItem.addActionListener((ActionEvent e) -> {
            ExportQTIDialog exportQTIFrame = new ExportQTIDialog(project);
            // display the frame that lets the user export their QTI files
            exportQTIFrame.show();            
        });
                                                                                                  
        // if the JFrame is closed terminate the running program
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // makes the JFrame appear in the center of the screen
        homeFrame.setLocationRelativeTo(null);
        // make the JFrame visible
        homeFrame.setVisible(true);  
    }	
}
