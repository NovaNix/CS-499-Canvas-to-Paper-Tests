package io.github.csgroup.quizmaker.data;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.labels.LabelPart;
import io.github.csgroup.quizmaker.data.labels.LabelText;

/**
 * A label displayed when showing a question or answer<br>
 * 
 * Labels consist of multiple {@link LabelPart LabelParts}. 
 * 
 * @author Michael Nix
 */
public class Label 
{

	private List<LabelPart> parts = new ArrayList<LabelPart>();
	
	public Label()
	{
		
	}
	
	public Label(List<LabelPart> parts)
	{
		for (var part : parts)
		{
			this.parts.add(part.clone());
		}
	}
	
	/**
	 * Shorthand for creating a new label consisting of a single plaintext part.
	 * @param contents The text to be used in the label
	 * @return The generated label
	 */
	public static Label text(String contents)
	{
		return text(contents, LabelText.Type.plain);
	}
	
	/**
	 * Shorthand for creating a new label consisting of a single text part
	 * @param contents The text to be used in the label
	 * @param type The type of the text
	 * @return The generated label
	 */
	public static Label text(String contents, LabelText.Type type)
	{
		Label label = new Label();
		
		label.add(new LabelText(contents, type));
		
		return label;
	}
	
	public boolean add(LabelPart part)
	{
		return this.parts.add(part);
	}
	
	public boolean remove(LabelPart part)
	{
		return this.parts.remove(part);
	}
	
	public LabelPart get(int index)
	{
		return this.parts.get(index);
	}
	
	
	/**
	 * 
	 * @return Each of the LabelParts of the Label as a String
	 */
	public String asText()
	{
		StringBuilder text = new StringBuilder();
		
		for (var part : parts)
		{
			text.append(part.asText());
		}
		
		return text.toString();
	}
	
	@Override
	public String toString()
	{
		return asText();
	}
	
}
