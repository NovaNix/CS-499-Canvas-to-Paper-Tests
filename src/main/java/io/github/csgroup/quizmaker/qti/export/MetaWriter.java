package io.github.csgroup.quizmaker.qti.export;

import org.w3c.dom.Document;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.export.utils.QTIFileWriter;

/**
 * Converts a {@link Quiz} into a QTI assessment_meta.xml file
 * 
 * @author Michael Nix, Sarah Singhirunnusorn
 */
public class MetaWriter extends QTIFileWriter
{
	private Quiz project;
	
	public MetaWriter(Quiz p)
	{
		super();
		
		this.project = p;
		
		writeMeta(getDocument(), project);
	}
	
	/**
	 * Writes the metadata file to the document
	 * @param d the document to write to
	 * @param q the quiz to write the metadata of
	 * @author Sarah Singhirunnusorn
	 */
	private void writeMeta(Document d, Quiz q)
	{
		// TODO implement
	}
}
