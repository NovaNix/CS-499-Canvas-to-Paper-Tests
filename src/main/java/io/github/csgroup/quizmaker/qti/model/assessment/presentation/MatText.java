package io.github.csgroup.quizmaker.qti.model.assessment.presentation;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;

/**
 * Represents the {@code <mattext>} element in a QTI {@code <material>} block.
 * <p>
 * This element contains the textual content of a question prompt, answer<br> 
 * label, or other displayed content.
 * 
 * @author Sarah Singhirunnusorn
 */
@XmlRootElement(name = "mattext", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
public class MatText 
{

	private String texttype;
	private String text;
	
	public void setTexttype(String texttype) 
	{
		this.texttype = texttype;
	}
	
	public void setText(String text) 
	{
		this.text = text;
	}

	@XmlAttribute(name = "texttype")
	public String getTexttype() 
	{
		return texttype;
	}

	@XmlValue
	public String getText() 
	{
		return text;
	}

	@Override
	public String toString() 
	{
		return "MatText{" +
				"texttype='" + texttype + '\'' +
				", text='" + text + '\'' +
				'}';
	}
}
