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
import io.github.csgroup.quizmaker.qti.mapping.AssessmentMetadataMapper;
import io.github.csgroup.quizmaker.qti.model.AssessmentMetadata;
import io.github.csgroup.quizmaker.qti.parsing.AssessmentMetadataParser;
import java.io.File;

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
	private final AssessmentMetadataParser metadataParser;
	
	public QTIReader()
	{
        this.importManager = new QTIZipManager();
		this.manifestProcessor = new QTIManifestFileProcessor();
		this.metadataParser = new AssessmentMetadataParser();
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

		// Initialize QTI contents container 
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
				File metadataFile = new File(extractedQTIPackage.toFile(), mapping.getQuizMetadataFile());
				if (metadataFile.exists())
				{
					// Parse Quiz metadata
					AssessmentMetadata metadata = metadataParser.parse(metadataFile);;
					logger.info("Parsed metadata: {}", metadata);
					
					// Map data to Quiz
					Quiz quiz = AssessmentMetadataMapper.mapToQuiz(metadata);
					if (quiz != null)
					{
						qtiContents.quizzes.add(quiz);
						logger.info("Quiz added to QTIContents → ID: '{}', Title: '{}'", quiz.getId(), quiz.getTitle());
					}
					else 
					{
						logger.warn("Mapping returned null. Quiz not added for metadata file: {}", metadataFile.getName());
					}
				}
				else
				{
					logger.warn("Metadata file does not exist for this quiz: {}", metadataFile.getAbsolutePath());
				}
			}
			
			// TODO Parse assessment content file
		}

		return qtiContents;
	}	
}
