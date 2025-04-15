package io.github.csgroup.quizmaker.qti.model.assessment.presentation;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Represents the {@code <presentation>} element in a QTI item.
 * <p>
 * This element contains the components used to present a question<br>
 * to the user. It may include materials (e.g., text prompts) and possible<br> 
 * responses. 
 * 
 * @author Sarah Singhirunnusorn
 */
@XmlRootElement(name = "presentation", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
public class Presentation 
{
	
	private List<Material> materials;
	private List<ResponseLid> responseLids;
	private List<ResponseStr> responseStrs;
	
	public void setMaterials(List<Material> materials) 
	{
		this.materials = materials;
	}

	public void setResponseLids(List<ResponseLid> responseLids) 
	{
		this.responseLids = responseLids;
	}
	
	public void setResponseStrs(List<ResponseStr> responseStrs) 
	{
		this.responseStrs = responseStrs;
	}
	
	@XmlElement(name = "material", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public List<Material> getMaterials() 
	{
		return materials;
	}
	
	@XmlElement(name = "response_lid", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public List<ResponseLid> getResponseLids() 
	{
		return responseLids;
	}
	
	@XmlElement(name = "response_str", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public List<ResponseStr> getResponseStrs()
	{
		return responseStrs;
	}
	
	
	@Override
	public String toString()
	{
		return "Presentation{" +
				"materials=" + materials +
				", responseLids=" + responseLids +
				", responseStrs=" + responseStrs +
				'}';
	}
}


