package io.github.csgroup.quizmaker.data.quiz;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.Quiz;

/**
 * The result of taking a {@link Quiz} and generating it.<br> 
 * <br>
 * This is incomplete, do not use yet
 * 
 * @author Michael Nix
 */
public class GeneratedQuiz 
{

	private Quiz quiz;
	
	private List<Question> questions = new ArrayList<Question>();
	
	private float pointTotal;
	
}
