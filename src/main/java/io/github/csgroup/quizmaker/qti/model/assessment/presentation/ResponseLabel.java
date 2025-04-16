package io.github.csgroup.quizmaker.qti.model.assessment.presentation;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Represents a {@code <response_label>} element in a QTI item.
 * <p>
 * This element defines an individual answer option for a question,<br>
 * typically used in conjunction with {@code <render_choice>}.<br>
 * Each label has an identifier and a display value.
 * 
 * @author Sarah Singhirunnusorn 
 */
@XmlRootElement(name = "response_label", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
public class ResponseLabel 
{
	
	private String ident;
	private List<Material> materials;

	@XmlAttribute(name = "ident")
	public void setIdent(String ident) 
	{
		this.ident = ident;
	}

	@XmlElement(name = "material", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setMaterials(List<Material> materials) 
	{
		this.materials = materials;
	}

	public String getIdent() 
	{
		return ident;
	}

	public List<Material> getMaterials() 
	{
		return materials;
	}

	public String getDisplayText() 
	{
		if (materials != null && !materials.isEmpty()) 
		{
			var mattextList = materials.get(0).getMattext();
			
			if (mattextList != null && !mattextList.isEmpty()) 
			{
				return mattextList.get(0).getText();
			}
		}
		
		return "(missing)";
	}

	@Override
	public String toString() 
	{
		return "ResponseLabel{" +
				"ident='" + ident + '\'' +
				", materials=" + materials +
				'}';
	}
}
