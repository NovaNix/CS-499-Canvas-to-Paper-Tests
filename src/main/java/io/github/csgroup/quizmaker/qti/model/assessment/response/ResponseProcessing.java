package io.github.csgroup.quizmaker.qti.model.assessment.response;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the {@code <resprocessing>} element in QTI, which defines how an assessment<br>
 * item should be scored or evaluated based on a user's response.
 * <p>
 * This class wraps a list of {@code <respcondition>} elements that specify the conditions<br>
 * for awarding points, providing feedback, or determining correctness of a response.
 * 
 * @author Sarah Singhirunnusorn 
 */
@XmlRootElement(name = "resprocessing", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
public class ResponseProcessing 
{
	
	private List<RespCondition> responseConditions = new ArrayList<>();

	@XmlElement(name = "respcondition", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setResponseConditions(List<RespCondition> responseConditions) 
	{
		this.responseConditions = responseConditions;
	}

	public List<RespCondition> getResponseConditions() 
	{
		return responseConditions;
	}

	@Override
	public String toString() 
	{
		return "ResponseProcessing{" +
				"responseConditions=" + responseConditions +
				'}';
	}
}


