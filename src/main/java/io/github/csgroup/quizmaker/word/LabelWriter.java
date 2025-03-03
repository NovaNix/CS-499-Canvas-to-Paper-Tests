package io.github.csgroup.quizmaker.word;

import io.github.csgroup.quizmaker.data.Label;

/**
 * Responsible for writing {@link Label Labels} to docx files
 * 
 * @author 
 */
public class LabelWriter
{

	// You dont have to adhere too carefully to this skeleton code, this is just a rough outline and some ideas
	// Do what works, and keep things clean and reusable if possible
	
	public LabelWriter()
	{
		// TODO take in the docx file to write to, along with any other persistent information you might need
	}
	
	public void write(Label label)
	{
		// TODO consider taking in additional writing information, such as where to write the label, or information for writing the answer key
	}
	
	{
		// These are example labels that should be used to test the label writer
		// These should be removed or moved to a test file when functionality of the label writer has been fully tested
		
		var example = new Label("This is a test.");
		
		var multiline = new Label("Line 1\nLine 2");
		
		var basicHTML = new Label("<p>This should be HTML</p>", Label.Type.html);
		
		var boldHTML = new Label("<p>This should be <b>bold</b></p>", Label.Type.html);
		
		var complexHTML = new Label("<p>Mixing <i>and <b>matching</b></i></p>", Label.Type.html);
		
		// TODO add HTML with tables and images and lists
	}
	
}
