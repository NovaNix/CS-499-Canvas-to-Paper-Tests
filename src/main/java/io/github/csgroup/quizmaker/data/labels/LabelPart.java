package io.github.csgroup.quizmaker.data.labels;

/**
 * Part of a {@link Label}
 * 
 * @author Michael Nix
 */
public abstract class LabelPart
{

	public abstract String asText();
	public abstract LabelPart clone();
	
}
