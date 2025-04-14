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
import io.github.csgroup.quizmaker.qti.mapping.AssessmentContentMapper;
import io.github.csgroup.quizmaker.qti.mapping.AssessmentMetadataMapper;
import io.github.csgroup.quizmaker.qti.model.AssessmentMetadata;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Assessment;
import io.github.csgroup.quizmaker.qti.parsing.AssessmentMetadataParser;
import io.github.csgroup.quizmaker.qti.parsing.AssessmentContentParser;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

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
	private final AssessmentContentParser contentParser;
	
	public QTIReader()
	{
        this.importManager = new QTIZipManager();
		this.manifestProcessor = new QTIManifestFileProcessor();
		this.metadataParser = new AssessmentMetadataParser();
		this.contentParser = new AssessmentContentParser();
	}

	/**
	 * Reads the QTI files, extracts the data files, and retrieves the file mappings of the quiz assessment and metadata files.<br>
	 * For each quiz, it parses the quiz metadata and content files and populates QTIContents with a {@link Quiz}.
	 * 
	 * @param qtiZipPath The path to the QTI ZIP file.
	 * @return A {@link QTIContents} object containing parsed quiz data.
	 * @throws IOException if the QTI file cannot be properly extracted.
	 */
	public QTIContents readFile(String qtiZipPath) throws IOException, Exception
	{
		logger.info("Reading QTI file from path: {}", qtiZipPath);

		// Step 1: Unzip the QTI package and extract the files into a temporary directory
		Path extractedQTIPackage = importManager.extractQTIFile(qtiZipPath);
		if (extractedQTIPackage == null)
		{
			logger.error("Failed to extract QTI file.");
			return new QTIContents();
		}
		logger.info("---- QTI File Extraction Complete ----");
		logger.info("Extracted QTI files to: {}", extractedQTIPackage.toAbsolutePath());

		// Step 2: Parse the manifest file for the metadata and content file mappings
		List<QTIDataFileMapping> mappings = manifestProcessor.processQTIFile(extractedQTIPackage.toAbsolutePath());

		QTIContents qtiContents = new QTIContents();	// Initialize QTI contents container 

		if (mappings == null || mappings.isEmpty())
		{
			logger.warn("No quizzes found in the QTI file.");
			return qtiContents;
		}
		logger.info("---- Processing Quiz Data Files ----");
		
		// Tracks quiz IDs already added to avoid duplicates
		Set<String> addedQuizIds = new HashSet<>();

		//Step 3: Iterate through the file mappings to process the metadata and content files of each quiz
		for (QTIDataFileMapping mapping : mappings)
		{
			logger.info("Processing Quiz: {}", mapping.getQuizAssessmentFile());

			Quiz quiz;	// Create Quiz
			
			// Step 3a: If the metadata file exists, parse metadata elements and map them
			if (mapping.hasMetadataFile())
			{
				File metadataFile = new File(extractedQTIPackage.toFile(), mapping.getQuizMetadataFile());
				if (metadataFile.exists())
				{
					// Parse quiz metadata
					AssessmentMetadata metadata = metadataParser.parse(metadataFile);
					logger.info("Parsed metadata: {}", metadata);
					
					// Map quiz metadata to Quiz
					quiz = AssessmentMetadataMapper.mapToQuiz(metadata);
				}
				else
				{
					// Missing metadata file -> attempt to extract quiz title and quiz ID from content file
					logger.warn("Metadata file does not exist / MISSING for this quiz: {}", metadataFile.getAbsolutePath());
					File assessmentFile = new File(extractedQTIPackage.toFile(), mapping.getQuizAssessmentFile());
					
					if (!assessmentFile.exists())
					{
						logger.warn("Content File also does not exist / MISSING -> Skipping Quiz");
						continue;
					}
					
					Assessment assessment = contentParser.parse(assessmentFile);
					
					String quizID = (assessment.getIdent() != null) ? assessment.getIdent() : "";
					String quizTitle = (assessment.getTitle() != null) ? assessment.getTitle() : "";
					
					quiz = new Quiz (quizID, quizTitle);
					AssessmentContentMapper.mapToQuiz(assessment, quiz);
					
					if (quiz.getQuestionCount() == 0)
					{
						logger.warn("Quiz '{}' was added but contains ZERO questions.", quiz.getTitle());
					}
					
					if (!addedQuizIds.contains(quiz.getId()))
					{
						qtiContents.quizzes.add(quiz);
						addedQuizIds.add(quiz.getId());
						logger.info("Quiz added to QTIContents → ID: '{}', Title: '{}', Questions: '{}'", quiz.getId(), quiz.getTitle(), quiz.getQuestionCount());
					}
					continue;
				}
			}
			
			else
			{
				// Step 3b: No metadata file present -> ONLY parse and map data from content file
				logger.info("No metadata file. Creating quiz from assessment content.");

				File assessmentFile = new File(extractedQTIPackage.toFile(), mapping.getQuizAssessmentFile());

				if (!assessmentFile.exists())
				{
					logger.warn("Assessment content file is missing — skipping quiz.");
					continue;
				}

				Assessment assessment = contentParser.parse(assessmentFile);

				String quizId = (assessment.getIdent() != null) ? assessment.getIdent() : "";
				String quizTitle = (assessment.getTitle() != null) ? assessment.getTitle() : "";

				quiz = new Quiz(quizId, quizTitle);
				AssessmentContentMapper.mapToQuiz(assessment, quiz);

				if (quiz.getQuestionCount() == 0)
				{
					logger.warn("Quiz '{}' was added but contains no questions.", quiz.getTitle());
				}

				if (!addedQuizIds.contains(quiz.getId()))
				{
					qtiContents.quizzes.add(quiz);
					addedQuizIds.add(quiz.getId());
					logger.info("Quiz added to QTIContents → ID: '{}', Title: '{}', Questions: '{}'", quiz.getId(), quiz.getTitle(), quiz.getQuestionCount());
				}
				continue;
			}

			// Step 3c: Parse the content file for questions
			File assessmentFile = new File(extractedQTIPackage.toFile(), mapping.getQuizAssessmentFile());

			if (assessmentFile.exists())
			{
				Assessment assessment = contentParser.parse(assessmentFile);
				AssessmentContentMapper.mapToQuiz(assessment, quiz);

				if (quiz.getQuestionCount() == 0)
				{
					logger.warn("Quiz '{}' was added but contains no questions.", quiz.getTitle());
				}
			}
			else
			{
				logger.warn("Assessment content file is missing: {} — quiz '{}' will be added with no questions.", assessmentFile.getAbsolutePath(), quiz.getTitle());
			}

			// Step 4: Add quiz to QTIContents
			if (!addedQuizIds.contains(quiz.getId()))
			{
				qtiContents.quizzes.add(quiz);
				addedQuizIds.add(quiz.getId());
				logger.info("Quiz added to QTIContents → ID: '{}', Title: '{}', Questions: '{}'", quiz.getId(), quiz.getTitle(), quiz.getQuestionCount());
			}
		}

		return qtiContents;
	}	
}
