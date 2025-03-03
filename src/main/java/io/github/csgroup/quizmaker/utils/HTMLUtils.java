package io.github.csgroup.quizmaker.utils;

import java.io.IOException;
import java.net.URL;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.csgroup.quizmaker.io.JSONUtils;

/**
 * Provides several common utilities for working with HTML
 * 
 * @author Michael Nix
 */
public final class HTMLUtils 
{

	public static final Logger logger = LoggerFactory.getLogger(HTMLUtils.class);
	
	private static Pattern entityPattern = Pattern.compile("&#?x?(.+?);", Pattern.CASE_INSENSITIVE);
	
	public static HTMLEntities entities; 
	
	// Prevent instantiation of the class
	private HTMLUtils() {};
	
	static 
	{
		// Initialize the HTML entities
		
		try 
		{
			logger.debug("Loading HTML Entities File");
			URL entityFile = HTMLUtils.class.getClassLoader().getResource("html/entities.json");
			
			entities = JSONUtils.mapper.readValue(entityFile, HTMLEntities.class);
		} 
		
		catch (IOException e) 
		{
			logger.error("Fatal Error: Failed to load HTML Entity file!");
			e.printStackTrace();
			
			System.exit(1); // We shouldn't risk trying to run the program if this file is missing. 
		}
	}
	
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
	
	/**
	 * Takes a string with HTML entities, and replaces those entities with their corresponding characters
	 * @param text the text to remove the entities from
	 * @return the text without HTML entities
	 */
	public static String removeEntities(String text)
	{
		Matcher m = entityPattern.matcher(text);
		
		// NOTE: this may have the possibility of replacing text where entities have been removed, if the resulting text is itself an HTML entity. 
		// It is unlikely that we'll run into this case, but this should be fixed at a later time
		return m.replaceAll(HTMLUtils::replaceEntity);
	}
	
	/**
	 * A callback function used to replace matching HTML entities using replaceAll
	 * @param match The match to be replaced
	 * @return The matched entity replaced with its corresponding character
	 */
	private static String replaceEntity(MatchResult match)
	{
		String entityText = match.group();
		
		String contents = match.group(1);
		
		String replacement;
		
		if (entityText.startsWith("&#x"))
		{
			// This is a hex entity
			
			replacement = entities.resolveHexCodepoint(contents);
		}
		
		else if (entityText.startsWith("&#"))
		{
			// This is a decimal entity
			
			replacement = entities.resolveDecimalCodepoint(Integer.parseInt(contents));
		}
		
		else 
		{
			// This is a named entity
			
			replacement = entities.resolveName(entityText);
		}
		
		if (replacement == null)
		{
			logger.warn("Failed to find replacement for entity " + entityText);
			replacement = "";
		}
		
		return replacement;
	}
	
}
