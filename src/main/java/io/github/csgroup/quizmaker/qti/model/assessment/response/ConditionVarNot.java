package io.github.csgroup.quizmaker.qti.model.assessment.response;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Represents a logical NOT block within a QTI {@code <conditionvar>} element.
 * <p>
 * Inverts the logic of a single nested {@code <conditionvar>}.<br>
 * Used to exclude specific answers or handle negative logic.
 * 
 * @author Sarah Singhirunnusorn
 */
@XmlRootElement(name = "not")
public class ConditionVarNot 
{
	
	private ConditionVar conditionVar;
	private List<VarEqual> varEquals;

	@XmlElement(name = "conditionvar", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setConditionVar(ConditionVar conditionVar) 
	{
		this.conditionVar = conditionVar;
	}

	@XmlElement(name = "varequal", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setVarEquals(List<VarEqual> varEquals) 
	{
		this.varEquals = varEquals;
	}

	public ConditionVar getConditionVar() 
	{
		return conditionVar;
	}

	public List<VarEqual> getVarEquals() 
	{
		return varEquals;
	}
}
