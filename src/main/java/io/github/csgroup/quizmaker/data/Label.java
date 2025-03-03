package io.github.csgroup.quizmaker.data;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.html.HTMLDocument;

/**
 * A label displayed when showing a question or answer<br>
 * 
 * @author Michael Nix
 */
public class Label
{

	// TODO consider adding Label events that bubble up to whatever data types contain the labels
	
	/**
	 * The display contents of the label<br>
	 * <br>
	 * This is not the most efficient way to store the data for the label,
	 * but it is the easiest way to synchronize the label contents with the UI.<br>
	 * <br>
	 * This current implementation has a number of issues. 
	 * For example, we cannot change the type of label because the document we use is based on the type.
	 * And because the document we give to the UI needs to stay consistent, we can't replace the document. 
	 * This could be fixed by making the label itself a document that forwards all of the function calls to the internal document,
	 * or by using a document wrapper class, but those wouldn't be as clean, and for now they are unnecessary.
	 */
	private Document content;

	// TODO consider adding the ability to change the type. This could require making a new document
	private Type type;
	
	/**
	 * Creates a blank label.
	 */
	public Label()
	{
		this("");
	}
	
	/**
	 * Creates a plain text label.
	 * @param text
	 */
	public Label(String text)
	{
		this(text, Type.plain);
	}
	
	public Label(String text, Type type)
	{
		this.type = type;
		
		switch (type)
		{
			case plain:
				// TODO research whether we can use HTMLDocument for plain text as well
				content = new PlainDocument();
				
				break;
				
			case html:
				
				content = new HTMLDocument();
				
				break;
		}
		
		setContent(text);
	}
	
	/**
	 * Creates a new label with the contents of another label
	 * @param label the label to clone
	 */
	public Label(Label label)
	{
		this(label.asText(), label.getType());
	}
	
	/**
	 * Sets the text content of the label
	 * @param text
	 */
	public void setContent(String text)
	{
		try 
		{
			// NOTE, this may cause issues with undo and redo if we decide to implement that
			content.remove(0, content.getLength());
			content.insertString(0, text, null);
		} 
		
		catch (BadLocationException e) 
		{
			// This should never be executed. This catch is here to remove the exception from remove and insertString. 
			// If it is executed, then we have a lot of problems
			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @return the contents of the label as a string
	 */
	public String asText()
	{
		try 
		{
			return content.getText(0, content.getLength());
		} 
		
		catch (BadLocationException e) 
		{
			// This should never be executed. This catch is here to remove the exception from getText. 
			// If it is executed, then we have a lot of problems
			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @return a Document synchronized with the contents of the label
	 */
	public Document getDocument()
	{
		return content;
	}
	
	@Override
	public String toString()
	{
		return asText();
	}
	
	public Type getType()
	{
		return type;
	}
	
	@Override
	public Label clone()
	{
		return new Label(this);
	}
	
	public static enum Type
	{
		plain("text/plain"), html("text/html");
		
		private String mime;
		
		private Type(String mime)
		{
			this.mime = mime;
		}
		
		public String getContentType()
		{
			return mime;
		}
	}

	
}
