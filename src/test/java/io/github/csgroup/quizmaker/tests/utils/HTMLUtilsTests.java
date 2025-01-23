package io.github.csgroup.quizmaker.tests.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.csgroup.quizmaker.utils.HTMLUtils;

public class HTMLUtilsTests 
{
	@Test
	@DisplayName("Add HTML Entities")
	public void addEntities()
	{
		String initial = "<p>Hello World!! I sure do hope this \"works\", otherwise me & you are going to have a problem...</p>";
		
		// The expected string was generated using https://mothereff.in/html-entities
		String expected = "&lt;p&gt;Hello World!! I sure do hope this &quot;works&quot;, otherwise me &amp; you are going to have a problem...&lt;/p&gt;";
		
		assertEquals(HTMLUtils.addEntities(initial), expected, "Potentially disruptive characters should be converted to entities");
	}
	
}
