package io.github.csgroup.quizmaker.io;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Provides utilities for working with JSON files
 * 
 * @author Michael Nix
 */
public class JSONUtils 
{

	/**
	 * A common JsonMapper that can be used by multiple classes
	 */
	public static final ObjectMapper mapper = JsonMapper.builder()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(SerializationFeature.INDENT_OUTPUT, true)
			.disable(		
					MapperFeature.AUTO_DETECT_GETTERS,
					MapperFeature.AUTO_DETECT_IS_GETTERS
					)
			.build();
	
}
