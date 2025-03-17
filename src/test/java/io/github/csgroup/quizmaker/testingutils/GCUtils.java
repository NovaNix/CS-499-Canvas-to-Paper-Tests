package io.github.csgroup.quizmaker.testingutils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for working with the Java Garbage Collector
 * 
 * @author Michael Nix
 */
public class GCUtils 
{
	private static final Logger logger = LoggerFactory.getLogger(GCUtils.class);

	private static boolean collected = false;
	
	/**
	 * Forces the Garbage Collector to be called.<br>
	 * This is super janky, is not thread safe in the slightest, and should not be used if possible<br>
	 * I would not be surprised if this breaks or doesnt work  
	 * @return whether the garbage collector was called or not
	 */
	public static synchronized boolean forceGC(long timeout)
	{
		logger.info("Attempting to call Garbage Collector");
		
		collected = false;
		
		@SuppressWarnings("unused")
		GCDetect detect = new GCDetect(() -> {
			collected = true;
		});
		
		detect = null;
		
		// Attempt to force the garbage collector to activate
		System.gc();
		
		long start = System.currentTimeMillis();
		
		while (!collected)
		{
			long now = System.currentTimeMillis();
			
			if (now - start > timeout)
			{
				logger.warn("Failed to call Garbage Collector (Timeout: " + timeout + " milliseconds)");
				return false;
			}
		}
		
		logger.info("Forced Garbage Collector in " + (System.currentTimeMillis() - start) + " milliseconds");
		
		return collected;
	}
	
	/**
	 * Runs code when the Garbage Collector is called
	 * 
	 * @author Michael Nix
	 */
	private static class GCDetect
	{
		Runnable r;
		
		public GCDetect(Runnable r)
		{
			this.r = r;
		}
		
		@Override
		public void finalize()
		{
			r.run();
		}
	}
	
	// Self Tests
	
	@Test
	@DisplayName("Force GC")
	public void testForceGC()
	{
		assertTrue(forceGC(2000), "Garbage Collector should be called within 2 seconds");
	}
	
}
