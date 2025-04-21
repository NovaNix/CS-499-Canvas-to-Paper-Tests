package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.QTIWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
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

		// Create a dummy project with one quiz
		Project project = new Project();
		Label desc = new Label("<p>Demo Quiz</p>", Label.Type.html);
		Quiz quiz = new Quiz("gtest123", "Test Export Quiz", desc);
		quiz.addQuestion(new io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion("Explain photosynthesis.", 1.0f));
		project.addQuiz(quiz);
		
		String quizId = quiz.getId();
		
		// Run the QTI Writer
		QTIWriter writer = new QTIWriter();
		writer.writeProject(project, zipPath);
	
		// Display results
		logger.info("QTI export written to: {}", zipPath.toAbsolutePath());
		File zipFile = zipPath.toFile();
		logger.info("Exists: {}", zipFile.exists());
		logger.info("Size: {}", zipFile.length());

		assertTrue(zipFile.exists(), "Exported ZIP file should exist");
		assertTrue(zipFile.length() > 0, "Exported ZIP file should not be empty");

		try (ZipFile zip = new ZipFile(zipFile)) 
		{
			Set<String> entryNames = zip.stream().map(ZipEntry::getName).collect(Collectors.toSet());
			
			logger.info("Contains entries:");
			zip.stream().forEach(entry -> logger.info(" - {}", entry.getName()));
			
			assertTrue(entryNames.contains("imsmanifest.xml"), "ZIP should contain imsmanifest.xml");
			assertTrue(entryNames.contains(quizId + "/assessment_meta.xml"), "ZIP should contain assessment_meta.xml");
			assertTrue(entryNames.contains(quizId + "/" + quizId + ".xml"), "ZIP should contain assessment content file");
		}
	}
}
