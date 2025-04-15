package io.github.csgroup.quizmaker.qti.model.assessment.response;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * Represents a {@code <respcondition>} element in QTI, which defines conditional logic used<br>
 * in response processing.
 * <p>
 * It contains the logic for evaluating responses (via {@link ConditionVar}) and the scoring result<br>
 * or variable update (via {@link SetVar}).
 * 
 * 
 * @author Sarah Singhirunnusorn
 */
public class RespCondition 
{
	
	private String continueVal;
	private ConditionVar conditionVar;
	private SetVar setVar;

	public void setContinueVal(String continueVal) 
	{
		this.continueVal = continueVal;
	}

	public void setConditionVar(ConditionVar conditionVar) 
	{
		this.conditionVar = conditionVar;
	}
	
	public void setSetVar(SetVar setVar)
	{
		this.setVar = setVar;
	}

	@XmlAttribute(name = "continue")
	public String getContinueVal() 
	{
		return continueVal;
	}
	
	@XmlElement(name = "conditionvar", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public ConditionVar getConditionVar() 
	{
		return conditionVar;
	}
	
	@XmlElement(name = "setvar", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public SetVar getSetVar() 
	{
		return setVar;
	}
	
	@Override
	public String toString() 
	{
		return "RespCondition{" +
				"continue='" + continueVal + '\'' +
				", conditionVar=" + conditionVar +
				", setVar=" + setVar +
				'}';
	}
}
