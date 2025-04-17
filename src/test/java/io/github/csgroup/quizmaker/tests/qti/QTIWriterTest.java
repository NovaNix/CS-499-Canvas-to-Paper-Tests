package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;
import io.github.csgroup.quizmaker.qti.QTIWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipFile;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test to verify QTI Writer
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIWriterTest 
{
	private static final Logger logger = LoggerFactory.getLogger(QTIWriterTest.class);
	
	@Test
	public void testQTIExportZipCreation() throws Exception
	{
		// Set up temporary export location
		Path exportDir = Files.createTempDirectory("qti-writer-test");
		Path zipPath = exportDir.resolve("qti-export.zip");

		// Create dummy project with one quiz
		Project project = new Project();
		Label desc = new Label("<p>Test Quiz Description</p>", Label.Type.html);
		Quiz quiz = new Quiz("gtest123", "Test Export Quiz", desc);

		// Add at least one question and regenerate points
		quiz.regenerate();
		quiz.getMetadata().setDynamicValues(quiz.getGenerated());

		// Safely initialize and set required metadata fields
		for (QuizMetadata.MetadataType type : QuizMetadata.MetadataType.values()) {
			if (!type.isDynamic()) 
			{
				quiz.getMetadata().setValue(type, "dummy");
			}
		}
		// Overwrite specific fields
		quiz.getMetadata().setValue(QuizMetadata.MetadataType.Points, "1.0");
		quiz.getMetadata().setValue(QuizMetadata.MetadataType.Professor, "Sarah");
		quiz.getMetadata().setValue(QuizMetadata.MetadataType.ClassNum, "CS499");
		quiz.getMetadata().setValue(QuizMetadata.MetadataType.SectionNum, "01");
		quiz.getMetadata().setValue(QuizMetadata.MetadataType.TestNum, "1");
		quiz.getMetadata().setValue(QuizMetadata.MetadataType.Date, "2025-04-17");

		project.addQuiz(quiz);

		// Run QTI export
		QTIWriter writer = new QTIWriter();
		writer.writeProject(project, zipPath);

		logger.info("QTI export written to: {}", zipPath.toAbsolutePath());
		File zipFile = zipPath.toFile();
		logger.info("Exists: {}", zipFile.exists());
		logger.info("Size: {}", zipFile.length());
		
		assertTrue(zipFile.exists(), "Exported ZIP file should exist");
		assertTrue(zipFile.length() > 0, "Exported ZIP file should not be empty");

		try (ZipFile zip = new ZipFile(zipFile)) 
		{
			logger.info("Contains entries:");
			zip.stream().forEach(entry -> logger.info(" - {}", entry.getName()));
		}
	}
}
