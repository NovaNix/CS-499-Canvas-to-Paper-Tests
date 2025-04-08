package io.github.csgroup.quizmaker.qti.model.assessment.structure;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Represents the root {@code <assessment>} element in a QTI assessment content file.
 * <p>
 * This element encapsulates the entire assessment structure, including its<br>
 * identifier, title, and a list of sections containing questions.
 * 
 * @author Sarah Singhirunnusorn
 */
@XmlRootElement(name = "assessment", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
public class Assessment 
{
	
	private String ident;
	private String title;
	private List<Section> sections;

	@XmlAttribute(name = "ident")
	public void setIdent(String ident) 
	{
		this.ident = ident;
	}
	
	@XmlAttribute(name = "title")
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	@XmlElement(name = "section", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setSections(List<Section> sections) 
	{
		this.sections = sections;
	}
	
	public String getIdent() 
	{
		return ident;
	}
	
	public String getTitle() 
	{
		return title;
	}
	
	public List<Section> getSections() 
	{
		return sections;
	}
	
	@Override
	public String toString()
	{
		return "Assessment{" +
				"ident='" + ident + '\'' +
				", title='" + title + '\'' +
				", sections=" + (sections != null ? sections.size() : 0) +
				'}';
	}
}
