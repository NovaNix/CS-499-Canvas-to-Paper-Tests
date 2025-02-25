package io.github.csgroup.quizmaker.data.labels;

import javax.swing.text.Element;

/**
 * Part of a {@link Label}
 * 
 * @author Michael Nix
 */
public abstract class LabelPart implements Element
{

	public abstract String asText();
	public abstract LabelPart clone();
	
}
