package io.github.csgroup.quizmaker.qti.model.assessment.presentation;

import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Represents the {@code <material>} element in QTI content.
 * <p>
 * This element serves as a container for the actual textual content of a question,<br>
 * answer option, or any other descriptive element within an assessment item.
 * 
 * @author Sarah Singhirunnusorn
 */
public class Material 
{
	
	private List<MatText> mattext;
	
	public void setMattext(List<MatText> mattext) 
	{
		this.mattext = mattext;
	}

	@XmlElement(name = "mattext", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public List<MatText> getMattext() 
	{
		return mattext;
	}

	@Override
	public String toString() 
	{
		return "Material{" +
				"mattext=" + mattext +
				'}';
	}
}