package io.github.csgroup.quizmaker.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * HTMLEntities stores all of the HTML entity mappings, 
 * and provides utility functions to map named entities and codepoints to the characters they represent
 * 
 * @author Michael Nix
 */
public class HTMLEntities 
{
	public static final Logger logger = LoggerFactory.getLogger(HTMLEntities.class);
	
	/** A map containing a named entity and its corresponding character */
	private final Map<String, String> entityMap = new HashMap<String, String>();
	
	public HTMLEntities()
	{
		
	}
	
	public String resolveName(String entityName)
	{
		return entityMap.get(entityName);
	}
	
	public String resolveDecimalCodepoint(int codepoint)
	{
		return String.valueOf(Character.toChars(codepoint));
	}
	
	public String resolveHexCodepoint(String hex)
	{
		return resolveDecimalCodepoint(Integer.parseInt(hex, 16));  
	}
	
	/**
	 * Used to add entities to this class. This is used to map the JSON object values to our maps.<br>
	 * This function should not be used by anyone. It is purely for Jackson's JSON deserialization. 
	 * @param entityString the name of the entity
	 * @param pair the entity pair
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@JsonAnySetter
	private void addEntity(String entityString, EntityPair pair) throws JsonMappingException, JsonProcessingException
	{
		String entityName = entityString;
		
		if (!entityName.endsWith(";"))
		{
			// We shouldn't support entities that don't end in semicolons right now.
			return;
		}

		entityMap.put(entityName, pair.characters);
	}
	
	/**
	 * A pair of codepoints with their associated characters.<br> 
	 * <br>
	 * This is used for JSON mapping purposes. <br>
	 * The relation between the codepoints and characters could be figured out in code, but this is simpler for now. <br>
	 * <br>
	 * This class should never be used outside of HTMLEntities. It is purely a container for JSON mapping.
	 */
	protected static class EntityPair
	{
		/**
		 * A list of all of the codepoints associated with the character, in decimal format. 
		 */
		int[] codepoints;
		
		String characters;
		
		@JsonCreator
		public EntityPair(@JsonProperty("codepoints") int[] codepoints, @JsonProperty("characters") String characters)
		{
			this.codepoints = codepoints;
			this.characters = characters;
		}
	}
	
	// Debugging functions
	
	public int getNamedEntityCount()
	{
		return entityMap.size();
	}
	
}
