package io.github.csgroup.quizmaker.qti.model.assessment.response;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

/**
 * Represents the {@code <setvar>} element within a QTI {@code <respcondition>} block.
 * <p>
 * This element defines an action to be performed on a variable when the <br>
 * corresponding condition is met. In the context of scoring, this is typically <br>
 * used to assign points to the user's response.
 * 
 * @author Sarah Singhirunnusorn
 */
public class SetVar 
{
	
	private String action;
	private String value;
	
	public void setAction(String action) 
	{
		this.action = action;
	}
	
	public void setValue(String value) 
	{
		this.value = value;
	}
	
	@XmlAttribute(name = "action")
	public String getAction() 
	{
		return action;
	}
	
	@XmlValue
	public String getValue() 
	{
		return value;
	}
	
	@Override
	public String toString() 
	{
		return "SetVar{" +
				"action='" + action + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}


