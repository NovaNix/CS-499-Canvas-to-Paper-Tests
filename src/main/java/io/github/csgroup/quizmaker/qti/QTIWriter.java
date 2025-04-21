package io.github.csgroup.quizmaker.qti;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.export.AssessmentWriter;
import io.github.csgroup.quizmaker.qti.export.ManifestWriter;
import io.github.csgroup.quizmaker.qti.export.MetaWriter;
import io.github.csgroup.quizmaker.qti.export.utils.QTIExportZipper;
import java.io.IOException;
import java.nio.file.Files;
import javax.xml.transform.TransformerException;

/**
 * An object responsible for writing Projects to QTI files
 * 
 * @author 
 */
public class QTIWriter 
{
	public static final Logger logger = LoggerFactory.getLogger(QTIWriter.class);

	public QTIWriter()
	{
		
	}
	
	public void writeProject(Project project, Path destination) throws IOException, TransformerException, Exception
	{
		// Create a temporary directory
		Path tempDir = Files.createTempDirectory("qti-export");

		logger.info("Created temporary QTI export directory: {}", tempDir.toAbsolutePath());

		// Write imsmanifest.xml at the root
		String manifestId = "manifest-" + System.currentTimeMillis();
		ManifestWriter manifestWriter = new ManifestWriter(project, manifestId);
		manifestWriter.save(tempDir.resolve("imsmanifest.xml"));
		logger.info("Manifest file written.");

		// Write one folder per quiz with its metadata
		for (Quiz quiz : project.getQuizzes())
		{
			String quizId = quiz.getId();
			Path quizFolder = tempDir.resolve(quizId);
			Files.createDirectories(quizFolder);

			// Write assessment_meta.xml into the quiz folder
			MetaWriter metaWriter = new MetaWriter(quiz);
			metaWriter.save(quizFolder.resolve("assessment_meta.xml"));
			logger.info("Meta file written for quiz [{}]", quizId);

			// Write assessment.xml into the quiz folder
			AssessmentWriter assessmentWriter = new AssessmentWriter(quiz);
			assessmentWriter.save(quizFolder.resolve(quizId + ".xml"));
			logger.info("Assessment file written for quiz [{}]", quizId);
		}

		// Zip the full folder into the final destination
		QTIExportZipper.zipFolder(tempDir, destination);
		logger.info("QTI export zipped to: {}", destination.toAbsolutePath());
	}
}
