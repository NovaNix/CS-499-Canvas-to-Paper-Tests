package io.github.csgroup.quizmaker.qti.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * This class represents the metadata for a QTI assessment parsed from the <code>assessment_meta.xml</code> file.<br>
 * This includes the quiz's:<br>
 *  - Title<br>
 *  - Identifier<br>
 *  - Scoring policy<br>
 *  - Due date<br>
 *  - Quiz Description<br>
 * It is designed for JAXB parsing and will later be mapped to the application's core data model.
 * 
 * @author Sarah Singhirunnusorn
 */
@XmlRootElement(name = "quiz", namespace = "http://canvas.instructure.com/xsd/cccv1p0") 
public class AssessmentMetadata 
{
    
	private String title;
	private String identifier;
	private String pointsPossible;
	private String dueDate;
	private String quizDescription;
	
	// Setters with @XmlElement annotations
	@XmlElement(name = "title", namespace = "http://canvas.instructure.com/xsd/cccv1p0")
	public void setTitle(String title) 
	{
		this.title = title;
	}

	@XmlAttribute(name = "identifier")
	public void setIdentifier(String identifier) 
	{
		this.identifier = identifier;
	}

	@XmlElement(name = "points_possible", namespace = "http://canvas.instructure.com/xsd/cccv1p0")
	public void setPointsPossible(String pointsPossible)
	{
		this.pointsPossible = pointsPossible;
	}

	@XmlElement(name = "due_at", namespace = "http://canvas.instructure.com/xsd/cccv1p0")
	public void setDueDate(String dueDate) 
	{
		this.dueDate = dueDate;
	}
	
	@XmlElement(name = "description", namespace = "http://canvas.instructure.com/xsd/cccv1p0")
	public void setQuizDescription(String quizDescription)
	{
		this.quizDescription = quizDescription;
	}
	
	// Getters 
	public String getTitle() 
	{
		return title;
	}

	public String getIdentifier() 
	{
		return identifier;
	}

	public String getPointsPossible() 
	{
		return pointsPossible;
	}

	public String getDueDate() 
	{
		return dueDate;
	}
	
	public String getQuizDescription() 
	{
		return quizDescription;
	}

	@Override
	public String toString() 
	{
		return "AssessmentMetadata{" +
			"title='" + title + '\'' +
			", identifier='" + identifier + '\'' +
			", pointsPossible='" + pointsPossible + '\'' +
			", dueDate='" + dueDate + '\'' +
			", quizDescription='" + quizDescription + '\'' +
			'}';
	}    
}
