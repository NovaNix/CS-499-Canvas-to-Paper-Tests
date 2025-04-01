package io.github.csgroup.quizmaker.qti;

import io.github.csgroup.quizmaker.qti.manifest.QTIDataFileMapping;
import io.github.csgroup.quizmaker.qti.manifest.QTIManifestFileProcessor;
import io.github.csgroup.quizmaker.qti.importing.QTIZipManager;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion;
import io.github.csgroup.quizmaker.data.questions.MatchingQuestion;
import io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion;
import io.github.csgroup.quizmaker.data.quiz.BankSelection;

/**
 * An object responsible for reading QTI files and extracting the information contained inside
 * 
 * @author 
 */
public class QTIReader 
{

	private static final Logger logger = LoggerFactory.getLogger(QTIReader.class);
	private final QTIZipManager importManager;
	private final QTIManifestFileProcessor manifestProcessor;

	public QTIReader()
	{
		this.importManager = new QTIZipManager();
		this.manifestProcessor = new QTIManifestFileProcessor();	
	}

	/**
	 * Reads the QTI files, extracts the data files, and retrieves the file mappings of the quiz assessment and metadata files
	 * 
	 * @param qtiZipPath The path to the QTI ZIP file.
	 * @return A {@link QTIContents} object containing parsed quiz data.
	 * @throws IOException if the QTI file cannot be properly extracted.
	 */
	public QTIContents readFile(String qtiZipPath) throws IOException, Exception
	{
		logger.info("Reading QTI file from path: {}", qtiZipPath);

		// Extract the files from the QTI Zip file
		Path extractedQTIPackage = importManager.extractQTIFile(qtiZipPath);
		if (extractedQTIPackage == null)
		{
			logger.error("Failed to extract QTI file.");
			return new QTIContents();
		}
		logger.info("---- QTI File Extraction Complete ----");
		logger.info("Extracted QTI files to: {}", extractedQTIPackage.toAbsolutePath());

		// Process the manifest file to locate the quiz data files
		List<QTIDataFileMapping> mappings = manifestProcessor.processQTIFile(extractedQTIPackage.toAbsolutePath());

		// Ensure that it always returns a valid QTIContents object
		QTIContents qtiContents = new QTIContents();

		if (mappings == null || mappings.isEmpty())
		{
			logger.warn("No quizzes found in the QTI file.");
			return qtiContents;
		}
		logger.info("---- Processing Quiz Data Files ----");

		// Iterate through the file mappings to process the quiz assessment and metadata files
		for (QTIDataFileMapping mapping : mappings)
		{
			logger.info("Processing Assessment File: {}", mapping.getQuizAssessmentFile());

			if (mapping.hasMetadataFile())
			{
				logger.info("Located metadata file for assessment file: {} → {}", mapping.getQuizAssessmentFile(), mapping.getQuizMetadataFile());
			}

			// TODO Parse assessmement file
			// TODO Parse metadata file
		}

		return qtiContents;
	}	
}
