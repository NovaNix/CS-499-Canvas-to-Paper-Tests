package io.github.csgroup.quizmaker.data.labels;

/**
 * A basic text component of a {@link Label}. 
 * 
 * @author Michael Nix
 */
public class LabelText extends LabelPart
{

	private String contents;
	private Type type = Type.plain;
	
	public LabelText(String text)
	{
		this.contents = text;
	}
	
	public LabelText(String text, Type type)
	{
		this.contents = text;
		this.type = type;
	}
	
	@Override
	public String asText() 
	{
		return contents;
	}
	
	public String getText()
	{
		return contents;
	}
	
	public Type getType()
	{
		return type;
	}
	
	@Override
	public LabelPart clone() 
	{	
		return new LabelText(contents, type);
	}
	
	public enum Type
	{
		plain, html
	}
	
}
