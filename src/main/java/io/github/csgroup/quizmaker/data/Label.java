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
				
				content = new PlainDocument();
				
				break;
				
			case html:
				
				content = new HTMLDocument();
				
				break;
		}
		
		setContent(text);
	}
	
	/**
	 * Creates a deep copy of another label.<br>
	 * @param label The label to copy
	 */
//	public Label(Label label)
//	{
//		this.add(label.parts);
//	}
//	
//	public Label(List<LabelPart> parts)
//	{
//		this.add(parts);
//	}
	
	/**
	 * Shorthand for creating a new label consisting of a single plaintext part, without any text inside.
	 * @return the generated label
	 */
	public static Label blank()
	{
		return Label.text("");
	}
	
	/**
	 * Shorthand for creating a new label consisting of a single plaintext part.
	 * @param contents The text to be used in the label
	 * @return The generated label
	 */
	public static Label text(String contents)
	{
		return text(contents, Type.plain);
	}
	
	/**
	 * Shorthand for creating a new label consisting of a single text part
	 * @param contents The text to be used in the label
	 * @param type The type of the text
	 * @return The generated label
	 */
	public static Label text(String contents, Type type)
	{
		Label label = new Label(contents);
		
		//label.add(new LabelText(contents, type));
		
		return label;
	}
	
	public void setContent(String text)
	{
		try 
		{
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
