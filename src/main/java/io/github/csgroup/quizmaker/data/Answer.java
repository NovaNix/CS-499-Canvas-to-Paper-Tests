package io.github.csgroup.quizmaker.data;

/**
 * An answer to a {@link Question Question}
 * 
 * @author Michael Nix
 */
public abstract class Answer 
{
	private final int id;
	
	// TODO add default constructor with randomly generating id
	
	public Answer(int id)
	{
		this.id = id;
	}
	
	public abstract String asText();
	
	public int getId()
	{
		return id;
	}
	
	@Override
	public String toString()
	{
		return asText();
	}
}
