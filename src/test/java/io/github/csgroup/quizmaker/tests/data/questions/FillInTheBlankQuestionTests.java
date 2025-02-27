package io.github.csgroup.quizmaker.tests.data.questions;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion;

/**
 * Tests for {@link FillInTheBlankQuestion FillInTheBlankQuestions}
 * 
 * @author Michael Nix
 */
public class FillInTheBlankQuestionTests 
{

	@Test
	@DisplayName("Get Tags")
	public void getTags()
	{
		var question = new FillInTheBlankQuestion("Test Question", 5.0f);
		question.setLabel(new Label("This is a fill in the [0] question! [this] should be filled with [something]"));
		
		var expected = Arrays.asList("0", "this", "something");
		var tags = question.getTags();
		
		assertArrayEquals(expected.toArray(), tags.toArray(), "Tags should be properly extracted and ordered");
	}
	
}
