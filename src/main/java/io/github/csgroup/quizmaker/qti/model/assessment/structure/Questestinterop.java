package io.github.csgroup.quizmaker.qti.model.assessment.structure;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Represents the root element {@code <questestinterop>} in a QTI assessment content file.
 * <p>
 * This element wraps the full assessment structure and serves as the main entry point for JAXB parsing.
 * 
 * @author Sarah Singhirunnusorn
 */
@XmlRootElement(name = "questestinterop", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
public class Questestinterop 
{
	
	private Assessment assessment;
	
	public void setAssessment(Assessment assessment) 
	{
		this.assessment = assessment;
	}
	
	@XmlElement(name = "assessment", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public Assessment getAssessment() 
	{
		return assessment;
	}
	
	@Override
	public String toString() 
	{
		return "Questestinterop{" +
				"assessment=" + assessment +
				'}';
	}
}
