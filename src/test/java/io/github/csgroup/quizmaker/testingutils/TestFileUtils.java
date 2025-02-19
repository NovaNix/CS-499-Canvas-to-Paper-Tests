package io.github.csgroup.quizmaker.testingutils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides utilities and helper functions for working with files during tests 
 * 
 * @author Michael Nix
 */
public class TestFileUtils 
{

	/**
	 * Loads an internal resource file and returns the contents as a string. 
	 * @param resource
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static String resourceAsString(String resource) throws URISyntaxException, IOException
	{
		URI r = TestFileUtils.class.getResource(resource).toURI();
		
		Path p = Paths.get(r);
		
		return Files.readString(p);
	}
	
}
