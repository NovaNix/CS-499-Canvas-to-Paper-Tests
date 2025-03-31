package io.github.csgroup.quizmaker.tests.stores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.testingutils.GCUtils;
import io.github.csgroup.quizmaker.utils.stores.derived.DerivedStore;
import io.github.csgroup.quizmaker.utils.stores.writable.DefaultWritableStore;
import io.github.csgroup.quizmaker.utils.stores.writable.WritableStore;

/**
 * Tests for {@link DerivedStore}
 * 
 * @author Michael Nix
 */
public class DerivedStoreTests 
{
	private static final Logger logger = LoggerFactory.getLogger(DerivedStoreTests.class);
	
	@Test
	@DisplayName("DerivedStore updating")
	public void testUpdate()
	{
		WritableStore<String> testStore = new DefaultWritableStore<String>("Test value 1");
		
		var derived = new DerivedStore<String>(testStore, () -> {
			return testStore.get() + " updated";
		});
		
		assertEquals("Test value 1 updated", derived.get(), "The derived value should be properly calculated");
		
		// Test updating the store
		
		testStore.set("Test value 2");
		
		assertEquals("Test value 2 updated", derived.get(), "The derived value should be properly calculated when store changed");
	}
	
	@Test
	@DisplayName("DerivedStore Weak Reference")
	public void testWeakReference()
	{
		WritableStore<String> testStore = new DefaultWritableStore<String>("Test value 1");
		
		// Make the derived store
		@SuppressWarnings("unused")
		var derived = new DerivedStore<String>(testStore, () -> {
			return testStore.get() + " updated";
		});
		
		// Delete the reference to the store
		derived = null;
		
		// Force the garbage collector to be called
		boolean called = GCUtils.forceGC(2000);
		
		// Update the store to force the derivedstore to be removed 
		testStore.set("Testing!!");
		
		if (!called)
		{
			logger.error("Failed to call garbage collector!");
			fail("Could not verify Weak Reference code: Garbage Collector could not be called");
			
			return;
		}
		
		assertEquals(0, testStore.getListenerCount(), "The DerivedStore should remove itself from its store's listeners when the reference to the DerivedStore is lost");
		
	}
}
