package io.github.csgroup.quizmaker.qti.export;

import org.w3c.dom.Document;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.qti.export.utils.QTIFileWriter;

/**
 * Converts a {@link Project} into a QTI imsmanifest.xml file
 * 
 * @author Michael Nix, Sarah Singhirunnusorn
 */
public class ManifestWriter extends QTIFileWriter
{
	private Project project;
	
	public ManifestWriter(Project p)
	{
		super();
		
		this.project = p;
		
		writeManifest(getDocument(), project);
	}
	
	/**
	 * Writes the manifest file to the document
	 * @param d the document to write to
	 * @param p the project to write the manifest of
	 * @author Sarah Singhirunnusorn
	 */
	private void writeManifest(Document d, Project p)
	{
		// TODO implement
	}
}
