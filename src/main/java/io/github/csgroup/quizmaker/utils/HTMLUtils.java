package io.github.csgroup.quizmaker.utils;

/**
 * Provides several common utilities for working with HTML
 * 
 * @author Michael Nix
 */
public final class HTMLUtils 
{

	// Prevent instantiation of the class
	private HTMLUtils() {};
	
	/**
	 * Replaces the most commonly used reserved characters (&, ", <, >) with HTML entities
	 * @param text The text to add entities to
	 * @return 
	 */
	public static String addEntities(String text)
	{
		
		return text
				.replaceAll("&", "&amp;")
				.replaceAll("\"", "&quot;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				;
	}
	
	public static String removeEntities(String html)
	{
		// TODO implement
		return "";
	}
	
}
