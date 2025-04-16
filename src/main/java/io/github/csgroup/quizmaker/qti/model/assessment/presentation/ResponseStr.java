package io.github.csgroup.quizmaker.qti.model.assessment.presentation;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Represents the {@code <response_str>} element in a QTI item,<br>
 * used for short answer, numerical, or essay-style response fields.
 * 
 * @author Sarah Singhirunnusorn
 */
@XmlRootElement(name = "response_str", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
public class ResponseStr
{
	
	private String ident;

	@XmlAttribute(name = "ident")
	public void setIdent(String ident)
	{
		this.ident = ident;
	}

	public String getIdent()
	{
		return ident;
	}

	@Override
	public String toString()
	{
		return "ResponseStr{ident='" + ident + "'}";
	}
}

