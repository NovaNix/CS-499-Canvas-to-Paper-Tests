package io.github.csgroup.quizmaker.qti.model.assessment.presentation;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the {@code <render_choice>} element in a QTI item structure.
 * <p>
 * This element is used to define a set of selectable answer choices for a <br>
 * question, usually associated with multiple choice or multiple response <br>
 * type items.
 * 
 * @author Sarah Singhirunnusorn
 */
@XmlRootElement(name = "render_choice", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
public class RenderChoice 
{
	
	private List<ResponseLabel> responseLabels = new ArrayList<>();
	
	@XmlElement(name = "response_label", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setResponseLabels(List<ResponseLabel> responseLabels) 
	{
		this.responseLabels = responseLabels;
	}
	
	public List<ResponseLabel> getResponseLabels() 
	{
		return responseLabels;
	}
	
	@Override
	public String toString() 
	{
		return "RenderChoice{" +
				"responseLabels=" + responseLabels +
				'}';
	}
}


