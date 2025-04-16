package io.github.csgroup.quizmaker.qti.export;

import org.w3c.dom.Document;

import io.github.csgroup.quizmaker.data.Project;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.export.utils.QTIFileWriter;
import org.w3c.dom.Element;

/**
 * Converts a {@link Project} into a QTI imsmanifest.xml file
 * 
 * @author Michael Nix, Sarah Singhirunnusorn
 */
public class ManifestWriter extends QTIFileWriter
{
	private Project project;
	private String manifestId;
	
	public ManifestWriter(Project p, String manifestId)
	{
		super();
		
		this.project = p;
		
		this.manifestId = manifestId;
		
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
		// TODO add logging 
		
		// Root element = <manifest> (with namespaces and schema locations)
		Element manifest = d.createElement("manifest");
		manifest.setAttribute("identifier", manifestId);
		manifest.setAttribute("xmlns", "http://www.imsglobal.org/xsd/imsccv1p1/imscp_v1p1");
		manifest.setAttribute("xmlns:lom", "http://ltsc.ieee.org/xsd/imsccv1p1/LOM/resource");
		manifest.setAttribute("xmlns:imsmd", "http://www.imsglobal.org/xsd/imsmd_v1p2");
		manifest.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		manifest.setAttribute("xsi:schemaLocation",
			"http://www.imsglobal.org/xsd/imsccv1p1/imscp_v1p1 " +
			"http://www.imsglobal.org/xsd/imscp_v1p1.xsd " +
			"http://ltsc.ieee.org/xsd/imsccv1p1/LOM/resource " +
			"http://www.imsglobal.org/profile/cc/ccv1p1/LOM/ccv1p1_lomresource_v1p0.xsd " +
			"http://www.imsglobal.org/xsd/imsmd_v1p2 " +
			"http://www.imsglobal.org/xsd/imsmd_v1p2p2.xsd");
		
		d.appendChild(manifest);
		
		// <metadata> block with general title and copyrights info
		Element metadata = d.createElement("metadata");
		
		Element schema = d.createElement("schema");
		schema.setTextContent("IMS Content");
		metadata.appendChild(schema);
		
		Element version = d.createElement("schemaversion");
		version.setTextContent("1.1.3");
		metadata.appendChild(version);
		
		Element lom = d.createElement("imsmd:lom");
		
		// <general> → <title>
		Element general = d.createElement("imsmd:general");
		Element title = d.createElement("imsmd:title");
		Element titleString = d.createElement("imsmd:string");
		titleString.setTextContent("QTI Quiz Export from QuizMaker");
		title.appendChild(titleString);
		general.appendChild(title);
		lom.appendChild(general);
		
		// <lifeCycle> → <contribute> → <data>
		Element lifeCycle = d.createElement("imsmd:lifeCycle");
		Element contribute = d.createElement("imsmd:contribute");
		Element date = d.createElement("imsmd:date");
		Element dateTime = d.createElement("imsmd:dateTime");
		dateTime.setTextContent(java.time.LocalDate.now().toString());
		date.appendChild(dateTime);
		contribute.appendChild(date);
		lifeCycle.appendChild(contribute);
		lom.appendChild(lifeCycle);
		
		// <rights> → copyright info
		Element rights = d.createElement("imsmd:rights");
		Element copyright = d.createElement("imsmd:copyrightAndOtherRestrictions");
		Element copyrightValue = d.createElement("imsmd:value");
		copyrightValue.setTextContent("yes");
		copyright.appendChild(copyrightValue);
		rights.appendChild(copyright);
		
		Element copyrightDesc = d.createElement("imsmd:description");
		Element descString = d.createElement("imsmd:string");
		descString.setTextContent("Private (Copyrighted) - http://en.wikipedia.org/wiki/Copyright");
		copyrightDesc.appendChild(descString);
		rights.appendChild(copyrightDesc);
		
		lom.appendChild(rights);
		metadata.appendChild(lom);
		manifest.appendChild(metadata);
		
		// <organizations> block: required but empty by default
		manifest.appendChild(d.createElement("organizations"));
		
		// <resources> block: contain quiz and metadata references 
		Element resources = d.createElement("resources");
		
		for (Quiz quiz : p.getQuizzes())
		{
			String quizId = quiz.getId();
			String folder = quizId + "/";
			
			// Resource for quiz content (e.g., quizId.xml)
			Element resource = d.createElement("resource");
			resource.setAttribute("identifier", quizId);
			resource.setAttribute("type", "imsqti_xmlv1p2");
			resource.setAttribute("href", folder + quizId + ".xml");
			
			Element file = d.createElement("file");
			file.setAttribute("href", folder + quizId + ".xml");
			resource.appendChild(file);
			
			Element dependency = d.createElement("dependency");
			dependency.setAttribute("identifierref", quizId + "-meta");
			resource.appendChild(dependency);
			
			resources.appendChild(resource);
			
			// Resource for quiz metadata (assessment_meta.xml)
			Element meta = d.createElement("resource");
			meta.setAttribute("identifier", quizId + "-meta");
			meta.setAttribute("type", "associatedcontent/imscc_xmlv1p1/learning-application-resource");
			meta.setAttribute("href", folder + "assessment_meta.xml");
			
			Element metaFile = d.createElement("file");
			metaFile.setAttribute("href", folder + "assessment_meta.xml");
			meta.appendChild(metaFile);
			
			resources.appendChild(meta);
		}
		
		manifest.appendChild(resources);
	}
}
