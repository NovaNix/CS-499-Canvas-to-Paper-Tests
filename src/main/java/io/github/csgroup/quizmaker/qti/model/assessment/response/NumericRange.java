package io.github.csgroup.quizmaker.qti.model.assessment.response;

/**
 * Represents a numeric range answer (e.g., between 1.0 and 10.0).<br>
 * Used in QTI numerical question response processing logic.
 * 
 * @param respident the response ID
 * @param min the lower bound
 * @param max the upper bound
 * 
 * @author Sarah Singhirunnusorn
 */
public record NumericRange(String respident, String min, String max)
{
	
	@Override
	public String toString()
	{
		return "between " + min + " and " + max;
	}
}

