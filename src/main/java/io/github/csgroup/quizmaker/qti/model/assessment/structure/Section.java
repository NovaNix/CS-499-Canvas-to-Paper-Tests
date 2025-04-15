package io.github.csgroup.quizmaker.qti.model.assessment.structure;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Represents a {@code <section>} element within a QTI assessment.
 * <p>
 * A section is a grouping of one or more {@code <item>} elements (i.e., questions).<br>
 * Each section may correspond to a category or topic within the assessment. 
 * 
 * @author Sarah Singhirunnusorn
 */
public class Section 
{
	
	private String ident;
	private List<Item> items;
	private List<Section> subsections;

	@XmlAttribute(name = "ident")
	public void setIdent(String ident) 
	{
		this.ident = ident;
	}

	@XmlElement(name = "item", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setItems(List<Item> items) 
	{
		this.items = items;
	}

	@XmlElement(name = "section", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setSubsections(List<Section> subsections) 
	{
		this.subsections = subsections;
	}

	public String getIdent() 
	{
		return ident;
	}

	public List<Item> getItems() 
	{
		return items;
	}

	public List<Section> getSubsections() 
	{
		return subsections;
	}

	@Override
	public String toString() 
	{
		return "Section{" +
				"ident='" + ident + '\'' +
				", items=" + (items != null ? items.size() : 0) +
				", subsections=" + (subsections != null ? subsections.size() : 0) +
				'}';
	}
}
