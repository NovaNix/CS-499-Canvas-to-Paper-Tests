package cs499.group7.utils;

public final class HTMLUtils 
{

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
