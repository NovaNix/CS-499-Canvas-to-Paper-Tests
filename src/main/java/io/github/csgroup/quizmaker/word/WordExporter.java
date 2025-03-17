package io.github.csgroup.quizmaker.word;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;

/**
 * Responsible for taking a {@link Quiz} and turning it into a .docx file
 * 
 * @author 
 */
public class WordExporter 
{
	public static final Logger logger = LoggerFactory.getLogger(WordExporter.class);

	public WordExporter()
	{
		
	}
	
	public void exportTest(GeneratedQuiz quiz, Path template, Path destination, boolean isKey)
	{
		
	}
	
}
