package io.github.csgroup.quizmaker.qti.model.assessment.response;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Represents a logical OR block within a QTI {@code <conditionvar>} element.
 * <p>
 * Contains a list of nested {@code <conditionvar>} elements where at least one must be true.<br>
 * Used to support alternative correct responses and flexible matching logic.
 * 
 * @author Sarah Singhirunnusorn
 */
@XmlRootElement(name = "or")
public class ConditionVarOr 
{
	
	private List<ConditionVar> conditionVars;
	private List<VarEqual> varEquals;

	public void setConditionVars(List<ConditionVar> conditionVars) 
	{
		this.conditionVars = conditionVars;
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
	
	@XmlElement(name = "varequal", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public List<VarEqual> getVarEquals() 
	{
		return varEquals;
	}
}
