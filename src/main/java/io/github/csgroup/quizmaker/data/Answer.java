package io.github.csgroup.quizmaker.data;

/**
 * An answer to a {@link Question Question}
 * 
 * @author Michael Nix
 */
public class Answer 
{

	private final int id;
	
	private Label label;
	
	public Answer(int id, String text)
	{
		this.id = id;
		this.label = Label.text(text);
	}
	
	public Answer(int id, Label label)
	{
		this.id = id;
		this.label = label;
	}
	
	public void setLabel(Label label)
	{
		this.label = label;
	}
	
	public void setLabel(String contents)
	{
		this.label = Label.text(contents);
	}
	
	public int getId()
	{
		return id;
	}
	
	public Label getLabel()
	{
		return label;
	}
	
	public String asText()
	{
		return label.asText();
	}
	
	@Override
	public String toString()
	{
		return asText();
	}
}
