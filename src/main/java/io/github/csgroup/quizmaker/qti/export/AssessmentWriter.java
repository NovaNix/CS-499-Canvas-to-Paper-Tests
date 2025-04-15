package io.github.csgroup.quizmaker.qti.export;

import org.w3c.dom.Document;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.export.utils.QTIFileWriter;

/**
 * Converts a {@link Quiz} into a QTI assessment file
 * 
 * @author Michael Nix, Sarah Singhirunnusorn
 */
public class AssessmentWriter extends QTIFileWriter
{
	private Quiz project;
	
	public AssessmentWriter(Quiz p)
	{
		super();
		
		this.project = p;
		
		writeAssessment(getDocument(), project);
	}
	
	/**
	 * Writes the assessment file to the document
	 * @param d the document to write to
	 * @param q the quiz to write the assessment data of
	 * @author Sarah Singhirunnusorn
	 */
	private void writeAssessment(Document d, Quiz q)
	{
		// TODO implement
	}
}
