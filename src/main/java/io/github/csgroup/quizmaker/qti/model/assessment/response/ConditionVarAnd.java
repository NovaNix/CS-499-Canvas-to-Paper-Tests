package io.github.csgroup.quizmaker.qti.model.assessment.response;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Represents a logical AND block within a QTI {@code <conditionvar>} element.
 * <p>
 * Contains a list of nested {@code <conditionvar>} elements that must all be true.<br>
 * Used in support of multiple correct answers and complex condition logic.
 * 
 * @author Sarah Singhirunnusorn
 */
@XmlRootElement(name = "and")
public class ConditionVarAnd 
{
	
	private List<ConditionVar> conditionVars;
	private List<ConditionVarNot> nots;
	private List<VarEqual> varEquals;

	public void setConditionVars(List<ConditionVar> conditionVars) 
	{
		this.conditionVars = conditionVars;
	}

	public void setNots(List<ConditionVarNot> nots) 
	{
		this.nots = nots;
	}

	public void setVarEquals(List<VarEqual> varEquals) 
	{
		this.varEquals = varEquals;
	}

	@XmlElement(name = "conditionvar", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public List<ConditionVar> getConditionVars() 
	{
		return conditionVars;
	}

	@XmlElement(name = "not", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public List<ConditionVarNot> getNots() 
	{
		return nots;
	}

	@XmlElement(name = "varequal", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public List<VarEqual> getVarEquals() 
	{
		return varEquals;
	}
}