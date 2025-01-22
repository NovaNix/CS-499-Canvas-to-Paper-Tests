package cs499.group7.qti;

import java.util.ArrayList;
import java.util.List;

import cs499.group7.data.QuestionBank;
import cs499.group7.data.Quiz;

/**
 * A container for the contents extracted from a QTI file
 * 
 * @author Michael Nix
 */
public class QTIContents 
{
	public final List<QuestionBank> banks = new ArrayList<QuestionBank>();
	public final List<Quiz> quizzes = new ArrayList<Quiz>();
	
	// TODO add additional information extracted from the file if necessary
	
	// TODO consider adding statistics or debugging info (number of questions loaded, number of sections, etc)
	
	// TODO add helper methods 
	
	// TODO consider converting this to a record
}
