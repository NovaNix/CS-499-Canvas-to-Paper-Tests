package io.github.csgroup.quizmaker.ui;

import io.github.csgroup.quizmaker.ui.banks.BankPanel;
import io.github.csgroup.quizmaker.ui.dialogs.ImportQTIDialog;
import io.github.csgroup.quizmaker.ui.dialogs.ExportQTIDialog;
import io.github.csgroup.quizmaker.App;
import io.github.csgroup.quizmaker.data.Project;

import java.awt.event.ActionEvent;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
        //homeFrame.setResizable(false);
               
//        Image icon = getIcon();
//        
//        if (icon != null)
//        	homeFrame.setIconImage(icon);
        
        //size of the JFrame (width, height)
        homeFrame.setSize(940, 694);
            
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
        JMenuItem creditsFileItem = new JMenuItem("Credits");           
        // add items to the JMenu fileMenu
        aboutMenu.add(creditsFileItem);    
        // add JMenu "About" to the JMenuBar menuBar
        menuBar.add(aboutMenu);
        
        // add the JMenuBar menuBar to homeFrame
        homeFrame.setJMenuBar(menuBar);
            
        // panel that will hold the quiz bank list and quiz bank questions 
        // and answers     
        Project project = App.getCurrentProject();
        BankPanel bankPanel = new BankPanel(project);
        QuizPanel quizPanel = new QuizPanel(project);
        GeneratedTab generatedTab = new GeneratedTab(project);
                
        // adds two tabs two the homeFrame
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Quizzes", quizPanel);
        tabs.addTab("Question Banks", bankPanel);
        tabs.addTab("Assignments", generatedTab);
        // setting the looks and feel of the tabs
        tabs.setBackgroundAt(0, new Color(242, 242, 242));
        tabs.setBackgroundAt(1, new Color(242, 242, 242));
        tabs.setBackgroundAt(2, new Color(242, 242, 242));
        UIManager.put("TabbedPane.contentAreaColor", new Color(242, 242, 242));
        UIManager.put("TabbedPane.highlight", new Color(181, 184, 183));
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
        
        // listens for when the user selects importFileItems
        creditsFileItem.addActionListener((ActionEvent e) -> {
            credits();
        });
                                                                                                  
        // if the JFrame is closed terminate the running program
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // makes the JFrame appear in the center of the screen
        homeFrame.setLocationRelativeTo(null);
        // make the JFrame visible
        homeFrame.setVisible(true);  
    }	
    
    private void credits()
    {
        JFrame creditsFrame = new JFrame();
        creditsFrame.setSize(300, 250);
        JLabel michaelLabel = new JLabel("Michael Nix: Full stack developer");
        JLabel sarahLabel = new JLabel("Srash Sing: Back end developer");
        JLabel samLabel = new JLabel("Samuel Garcia: Back end developer");
        JLabel emilyLabel = new JLabel("Emily Palmer: Front end developer");
        
        JPanel labelPanel = new JPanel(new GridBagLayout());
        GridBagConstraints michaelConstraint = new GridBagConstraints();
        GridBagConstraints sarahConstraint = new GridBagConstraints();
        GridBagConstraints samConstraint = new GridBagConstraints();
        GridBagConstraints emilyConstraint = new GridBagConstraints();
        
        michaelConstraint.fill = GridBagConstraints.HORIZONTAL;
        michaelConstraint.gridx = 0;
        michaelConstraint.gridy = 0;
        michaelConstraint.insets = new Insets(0, 0, 10, 0);
        labelPanel.add(michaelLabel, michaelConstraint);   
        
        sarahConstraint.fill = GridBagConstraints.HORIZONTAL;
        sarahConstraint.gridx = 0;
        sarahConstraint.gridy = 1;
        sarahConstraint.insets = new Insets(0, 0, 10, 0);
        labelPanel.add(sarahLabel, sarahConstraint); 
        
        samConstraint.fill = GridBagConstraints.HORIZONTAL;
        samConstraint.gridx = 0;
        samConstraint.gridy = 2;
        samConstraint.insets = new Insets(0, 0, 10, 0);
        labelPanel.add(samLabel, samConstraint); 
        
        emilyConstraint.fill = GridBagConstraints.HORIZONTAL;
        emilyConstraint.gridx = 0;
        emilyConstraint.gridy = 3;
        emilyConstraint.insets = new Insets(0, 0, 10, 0);
        labelPanel.add(emilyLabel, emilyConstraint); 
        
        creditsFrame.add(labelPanel);
        
        // makes the JFrame appear in the center of the screen
        creditsFrame.setLocationRelativeTo(null);
        // makes the JFrame visible
        creditsFrame.setVisible(true);
    }
        
    private Image getIcon() 
    {
    	try {
			return ImageIO.read(GUIManager.class.getResourceAsStream("/icon.png"));
		} catch (IOException e) {
			logger.error("Failed to load window icon");
			e.printStackTrace();
			
			return null;
		}
    }
}
