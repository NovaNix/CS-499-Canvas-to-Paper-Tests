package io.github.csgroup.quizmaker.qti.model.assessment.response;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

/**
 * Represents the {@code <varlte>} element in a QTI {@code <conditionvar>} block. 
 * 
 * @author Sarah Singhirunnusorn
 */
public class VarLte
{
	
	private String respIdent;
	private String value;

	public void setRespIdent(String respIdent)
	{
		this.respIdent = respIdent;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	@XmlAttribute(name = "respident")
	public String getRespIdent()
	{
		return respIdent;
	}

	@XmlValue
	public String getValue()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return "VarLte{" +
			"respIdent='" + respIdent + '\'' +
			", value='" + value + '\'' +
			'}';
	}
}

