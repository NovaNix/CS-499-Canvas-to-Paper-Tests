package io.github.csgroup.quizmaker.qti.manifest;

/**
 * This class pairs the assessment file with its corresponding metadata file for each quiz data file in the QTI file. <br>
 * - Quiz Assessment file: contains quiz questions, answers, and scoring details <br>
 * - Quiz Metadata file: contains details about the quiz (quiz title and the number of allowed attempts)
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIDataFileMapping 
{

	private final String quizAssessmentFile; 
	private final String quizMetadataFile;       

	/**
	 * Constructs a new QTIDataFileMapping object.
	 * 
	 * @param quizAssessmentFile The file path to the quiz assessment XML file.
	 * @param quizMetadataFile The file path to the quiz metadata XML file.
	 */
	public QTIDataFileMapping(String quizAssessmentFile, String quizMetadataFile)
	{
		this.quizAssessmentFile = quizAssessmentFile;
		this.quizMetadataFile = quizMetadataFile;
	}

	public String getQuizAssessmentFile()
	{
		return quizAssessmentFile;
	}

	public String getQuizMetadataFile()
	{
		return quizMetadataFile;
	}

	/**
	 * Verifies that the quiz data file contains a valid quiz metadata file.
	 * 
	 * @return true if a non-empty quiz metadata file exists, otherwise false. 
	 */
	public boolean hasMetadataFile()
	{
		return quizMetadataFile != null && !quizMetadataFile.trim().isEmpty();
	}

	@Override
	public String toString()
	{
		return String.format("QTIDataFileMapping{quizAssessmentFile='%s', quizMetadataFile='%s'}", quizAssessmentFile, quizMetadataFile);
	}

}
