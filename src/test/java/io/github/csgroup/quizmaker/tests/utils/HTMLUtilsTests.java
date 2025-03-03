package io.github.csgroup.quizmaker.tests.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.testingutils.TestFileUtils;
import io.github.csgroup.quizmaker.utils.HTMLEntities;
import io.github.csgroup.quizmaker.utils.HTMLUtils;

public class HTMLUtilsTests 
{
	public static final Logger logger = LoggerFactory.getLogger(HTMLUtilsTests.class);
	
	@Test
	@DisplayName("Add HTML Entities")
	public void addEntities()
	{
		String initial = "<p>Hello World!! I sure do hope this \"works\", otherwise me & you are going to have a problem...</p>";
		
		// The expected string was generated using https://mothereff.in/html-entities
		String expected = "&lt;p&gt;Hello World!! I sure do hope this &quot;works&quot;, otherwise me &amp; you are going to have a problem...&lt;/p&gt;";
		
		assertEquals(HTMLUtils.addEntities(initial), expected, "Potentially disruptive characters should be converted to entities");
	}
	
	@Test
	@DisplayName("Load HTML Entities")
	public void loadEntities()
	{
		HTMLEntities entities = HTMLUtils.entities;
		
		assertNotNull(entities);
		
		logger.info("Found " + entities.getNamedEntityCount() + " named entities");
	}
	
	@Test
	@DisplayName("Remove HTML Entities")
	public void removeEntities() throws URISyntaxException, IOException
	{
		String initial = TestFileUtils.resourceAsString("/html/textWithEntities.txt");
		String expected = TestFileUtils.resourceAsString("/html/textWithoutEntities.txt");
		
		String removed = HTMLUtils.removeEntities(initial);
		
		assertEquals(expected, removed, "All HTML entities should be properly removed.");
	}
	
}
