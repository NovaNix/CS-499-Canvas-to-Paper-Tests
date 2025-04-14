package io.github.csgroup.quizmaker.data.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.utils.RandomBag;

/**
 * The result of taking a {@link Quiz} and generating it.
 * 
 * @author Michael Nix
 */
public class GeneratedQuiz 
{
	// This class is designed to be immutable on the outside

	private final Quiz quiz;
	
	private final List<Question> questions = new ArrayList<Question>();
	
	private float pointTotal = 0f;
	
	private final QuizMetadata metadata;
	
	/**
	 * Generates a new quiz with a random seed
	 * @param quiz
	 */
	public GeneratedQuiz(Quiz quiz)
	{
		this(quiz, new Random());
	}
	
	/**
	 * Generates a new quiz
	 * @param quiz the quiz to generate
	 * @param random the source of randomness
	 */
	
	public GeneratedQuiz(Quiz quiz, Random random)
	{
		this.quiz = quiz;
		
		// Add the BankSelection questions
		
		for (BankSelection selection : quiz.getBankSelections())
		{
			RandomBag<Question> bag = selection.asRandomBag(random);

			for (int i = 0; i < selection.getQuestionCount(); i++)
			{
				Question next = bag.next();

				if (next == null)
					break; // This only happens if the question count is higher than the number of allowed questions!!

				addQuestion(next, selection.getPointsPerQuestion());
			}
		}

		// Add the internal questions

		for (Question q : quiz.getQuestions())
		{
			addQuestion(q);
		}
		
		// Shuffle the added questions
		Collections.shuffle(questions, random);
		
		// Generate the metadata
		this.metadata = quiz.getMetadata().clone();
		metadata.setDynamicValues(this);
	}
	
	/**
	 * Adds a copy of a Question to the GeneratedQuiz
	 * @param q the question to add. the question will be copied
	 */
	protected void addQuestion(Question q)
	{
		addQuestion(q, q.getPoints());
	}
	
	/**
	 * Adds a copy of a Question to the GeneratedQuiz
	 * @param q the question to add. the question will be copied
	 * @param points the points the question should be worth, overriding the existing points on the question
	 */
	protected void addQuestion(Question q, float points)
	{
		q = q.clone(); // get a copy of the question
		
		q.setPoints(points);
		
		if (quiz.shouldShuffleAnswers())
		{
			// TODO Shuffle the answers
		}
		
		questions.add(q);
		
		// Update the point total
		pointTotal += q.getPoints();
	}
	
	public List<Question> getQuestions()
	{
		return new ArrayList<Question>(questions);
	}
	
	public float getTotalPoints()
	{
		return pointTotal;
	}
	
	/**
	 * @return the metadata associated with the quiz<br>
	 * note: be careful using the metadata object, as it is mutable
	 */
	public QuizMetadata getQuizMetadata()
	{
		return metadata;
	}
}
