package io.github.csgroup.quizmaker.tests.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.csgroup.quizmaker.data.Label;

/**
 * Tests for {@link Label Labels}
 * 
 * @author Michael Nix
 */
public class LabelTests 
{
	@Test
	@DisplayName("Set and Get Plain Content")
	public void testContent()
	{
		String expected = "Expected Label";
		
		Label testLabel = new Label(expected);
		
		assertEquals(expected, testLabel.asText(), "The text content of a label should be extracted from the document properly");
		
		// Test setting text
		
		String replacement = "Replaced Label";
		
		testLabel.setContent(replacement);
		
		assertEquals(replacement, testLabel.asText(), "Label text should be properly set");
	}
	
	@Test
	@DisplayName("Set and Get HTML Content")
	public void testHTMLContent()
	{
		String expected = "<p>Expected Label</p>";
		
		Label testLabel = new Label(expected, Label.Type.html);
		
		assertEquals(expected, testLabel.asText(), "The text content of a label should be extracted from the document properly");
		
		// Test setting text
		
		String replacement = "<span>Replaced Label</span>";
		
		testLabel.setContent(replacement);
		
		assertEquals(replacement, testLabel.asText(), "Label text should be properly set");
	}
}
