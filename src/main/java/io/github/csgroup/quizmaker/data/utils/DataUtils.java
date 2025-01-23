package io.github.csgroup.quizmaker.data.utils;

import java.util.UUID;

/**
 * A collection of several utilities for working with data classes
 * 
 * @author Michael Nix
 */
public final class DataUtils 
{

	// Prevent instantiation of the class
	private DataUtils() {}
	
	public static String generateId()
	{
		return UUID.randomUUID().toString();
	}
	
}
