package io.github.csgroup.quizmaker.qti.model.assessment.presentation;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Represents a {@code <response_lid>} element in a QTI assessment item.<br>
 * This element defines a question’s possible response identifiers for<br>
 * multiple-choice and single-response interactions. 
 * <p>
 * It often contains a {@code <render_choice>} element that holds a list of<br>
 * available choices.
 * 
 * @author Sarah Singhirunnusorn
 */
@XmlRootElement(name = "response_lid", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
public class ResponseLid 
{
	
	private String ident;
	private Material material;
	private RenderChoice renderChoice;

	@XmlAttribute(name = "ident")
	public void setIdent(String ident) 
	{
		this.ident = ident;
	}

	@XmlElement(name = "material", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setMaterial(Material material) 
	{
		this.material = material;
	}

	@XmlElement(name = "render_choice", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setRenderChoice(RenderChoice renderChoice)
	{
		this.renderChoice = renderChoice;
	}

	public String getIdent() 
	{
		return ident;
	}
	
	public Material getMaterial() 
	{
		return material;
	}
	
	public RenderChoice getRenderChoice() 
	{
		return renderChoice;
	}

	@Override
	public String toString() 
	{
		return "ResponseLid{" +
			"ident='" + ident + '\'' +
			", material=" + material +
			", renderChoice=" + renderChoice +
			'}';
	}
}
