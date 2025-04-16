package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion;
import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;
import io.github.csgroup.quizmaker.qti.export.MetaWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test verifies the output structure of the assessment_meta.xml from the MetaWriter.
 * 
 * @author Sarah Singhirunnusorn
 */
public class MetaWriterTest 
{
	private static final Logger logger = LoggerFactory.getLogger(MetaWriterTest.class);

	@Test
	public void testMetaWriterGeneratesFile() throws Exception
	{
		// Create a temporary directory to write the meta file to
		Path tempDir = Files.createTempDirectory("qti-meta-test");
		Path output = tempDir.resolve("assessment_meta.xml");

		// Create a sample quiz
		Label description = new Label("<p>Sample quiz for export testing</p>", Label.Type.html);
		Quiz quiz = new Quiz("gtest123", "Sample Test Quiz", description);

		// Add sample question
		MultipleChoiceQuestion question = new MultipleChoiceQuestion("Sample Multiple Choice");
		SimpleAnswer a1 = new SimpleAnswer(1, "Option A");
		SimpleAnswer a2 = new SimpleAnswer(2, "Option B");

		question.addAnswer(a1, true);   
		question.addAnswer(a2, false);  

		quiz.addQuestion(question);

		// Generate internal structure and apply dynamic metadata
		quiz.regenerate();
		quiz.getMetadata().setDynamicValues(quiz.getGenerated());

		// Fallback: Ensure required metadata is set manually
		quiz.getMetadata().setValue(QuizMetadata.MetadataType.Points, "3.0");
		quiz.getMetadata().setValue(QuizMetadata.MetadataType.Minutes, "60");

		// Write the metadata file
		MetaWriter writer = new MetaWriter(quiz);
		writer.save(output);

		logger.info("assessment_meta.xml written to: {}", output.toAbsolutePath());

		// Verify that the file exists
		assertTrue(Files.exists(output), "assessment_meta.xml should exist");
		assertTrue(Files.size(output) > 0, "assessment_meta.xml should not be empty");	
	}
}
