package io.github.csgroup.quizmaker.qti.model.assessment.response;

import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Represents the {@code <conditionvar>} element inside a QTI {@code <respcondition>} block.
 * <p>
 * This element defines the logical condition that must be satisfied for a response<br>
 * to trigger a scoring or feedback action. It may contain multiple {@code <varequal>} comparisons,<br>
 * as well as logical constructs like {@code <and>}, {@code <or>}, and {@code <not>}.
 * 
 * @author Sarah Singhirunnusorn
 */
public class ConditionVar 
{
	
	private List<VarEqual> varEquals;
	private ConditionVarAnd and;
	private ConditionVarOr or;
	private ConditionVarNot not;

	public void setVarEquals(List<VarEqual> varEquals) 
	{
		this.varEquals = varEquals;
	}

	public void setAnd(ConditionVarAnd and) 
	{
		this.and = and;
	}

	public void setOr(ConditionVarOr or) 
	{
		this.or = or;
	}

	public void setNot(ConditionVarNot not) 
	{
		this.not = not;
	}
	
	@XmlElement(name = "varequal", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public List<VarEqual> getVarEquals() 
	{
		return varEquals;
	}

	@XmlElement(name = "and", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public ConditionVarAnd getAnd() 
	{
		return and;
	}

	@XmlElement(name = "or", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public ConditionVarOr getOr() 
	{
		return or;
	}

	@XmlElement(name = "not", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public ConditionVarNot getNot() 
	{
		return not;
	}
}

