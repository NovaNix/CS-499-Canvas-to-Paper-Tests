package io.github.csgroup.quizmaker.word;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Quiz;

/**
 * Responsible for taking a {@link Quiz} and turning it into a .docx file
 * 
 * @author 
 */
public class WordExporter 
{
	public static final Logger logger = LoggerFactory.getLogger(WordExporter.class);

	// TODO add template files, add function parameters
	
	private Path testTemplate;
	private Path keyTemplate;
	
	public WordExporter()
	{
		
	}
	
	public void exportTest(Quiz quiz, Path destination)
	{
		
	}
	
	public void exportAnswerKey(Quiz quiz, Path destination)
	{
		
	}
	
}
