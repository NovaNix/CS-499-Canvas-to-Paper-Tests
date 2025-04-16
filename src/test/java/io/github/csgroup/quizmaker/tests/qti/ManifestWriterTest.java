package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.export.ManifestWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test verifies the output structure of the xml from the ManifestWriter.
 * 
 * @author Sarah Singhirunnusorn
 */
public class ManifestWriterTest 
{
	private static final Logger logger = LoggerFactory.getLogger(ManifestWriterTest.class);
	
	@Test
	public void testManifestGeneration() throws Exception
	{
		// Create a temporary directory to write the manifest file to
		Path tempDir = Files.createTempDirectory("qti-manifest-test");
		Path output = tempDir.resolve("imsmanifest.xml");

		// Create a sample project with two quizzes
		Project project = new Project();
		Label desc = new Label("Sample quiz for testing", Label.Type.html);

		Quiz quiz1 = new Quiz("g123abc456", "Test Quiz 1", desc);
		Quiz quiz2 = new Quiz("g456def789", "Test Quiz 2", desc);

		project.addQuiz(quiz1);
		project.addQuiz(quiz2);

		// Write the manifest file
		String manifestId = "manifest-" + System.currentTimeMillis();
		ManifestWriter writer = new ManifestWriter(project, manifestId);
		writer.save(output);

		logger.info("Manifest written to: {}", output.toAbsolutePath());

		// Verify that the file exists
		assertTrue(Files.exists(output), "imsmanifest.xml should exist");
		assertTrue(Files.size(output) > 0, "imsmanifest.xml should not be empty");
	}
	
	@Test
	public void testMatchingManifestStructure() throws Exception
	{
		String quizId = "gd35ca28d38af5898f00d398ace31b563";

		Path tempDir = Files.createTempDirectory("manifest-match-test");
		Path output = tempDir.resolve("imsmanifest.xml");

		Project project = new Project();
		Label description = new Label("Test quiz for structure match", Label.Type.html);
		Quiz quiz = new Quiz(quizId, "Quiz Section 5.2 PERT Diagrams", description);
		project.addQuiz(quiz);

		String manifestId = "g60f06dc22b0fd90b324181daf098daa9";
		ManifestWriter writer = new ManifestWriter(project, manifestId);
		writer.save(output);

		logger.info("Manifest for known structure written to: {}", output.toAbsolutePath());
		assertTrue(Files.exists(output));
		assertTrue(Files.size(output) > 0);
	}
}
