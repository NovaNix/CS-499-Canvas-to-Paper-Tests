package io.github.csgroup.quizmaker.utils.stores.derived;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.csgroup.quizmaker.utils.stores.StoreListener;
import io.github.csgroup.quizmaker.utils.stores.readable.AbstractReadableStore;
import io.github.csgroup.quizmaker.utils.stores.readable.ReadableStore;

/**
 * A store that automatically updates its value when other stores are changed.<br>
 * <br>
 * Based on <a href="https://svelte.dev/docs/svelte/stores">Stores</a> from Svelte.
 * @param <T> the type of value stored inside
 * @see <a href="https://svelte.dev/docs/svelte/stores">Svelte Stores</a>
 * @author Michael Nix
 */
public class DerivedStore<T> extends AbstractReadableStore<T>
{

	private DerivedStoreCalculator<T> calculator;
	
	// TODO consider passing the stores to the calculate function
	@SuppressWarnings("unused")
	private List<ReadableStore<?>> stores;
	
	private T value;
	
	/**
	 * 
	 * @param stores the stores that when updated cause this to recalculate 
	 * @param calculator a pure function used to determine the value of the store
	 * 
	 * @see {@link DerivedStoreCalculator}
	 */
	@SuppressWarnings("unchecked")
	public DerivedStore(List<ReadableStore<?>> stores, DerivedStoreCalculator<T> calculator)
	{
		// Note: this needs to be set before we listen to the stores
		// This is just in case one of those stores fires an event when we add it
		this.calculator = calculator; 
		
		this.stores = new ArrayList<ReadableStore<?>>(stores);
		
		for (var store : stores)
		{
			store.addListener(new WeakStoreListener(this));
		}
		
		recalculate(); // Set the initial value 
	}

	/**
	 * Recalculates the value of the store, and updates all listeners if any change was made.<br>
	 * <br>
	 * This will be called automatically when one of the stores that the DerivedStore listens to is updated.
	 * 
	 * @see {@link DerivedStoreCalculator}
	 */
	public void recalculate()
	{
		T oldValue = value;
		
		value = calculator.calculate();
		
		if (!Objects.equals(value, oldValue)) // Prevent unnecessary events
		{
			// Note: Objects.equals is used instead of .equals because it's null safe
			
			// If the new value is different than the previous value, fire an event
			fireChange(value);
		}
	}
	
	@Override
	public T get() 
	{
		return value;
	}

	/**
	 * A StoreListener that stores a weak reference to a DerivedStore<br>
	 * <br>
	 * This exists because of an issue where the main code might no longer have a reference to a DerivedStore,
	 * in which case it should be deleted (we can never get the value being calculated, therefore we shouldn't 
	 * call the calculate function. However, the stores that it listens to still have references to it.
	 * Because of this, the DerivedStore is never removed from memory, and a potentially costly calculate 
	 * function is still called when those stores are updated.<br>
	 * <br>
	 * This class fixes this issue by storing a weak reference to the DerivedStore instead, meaning if no 
	 * other references exist to the DerivedStore, it will be garbage collected as normal. It then detects
	 * that it was garbage collected and then removes itself from the StoreListener. 
	 * 
	 * @author Michael Nix
	 */
	@SuppressWarnings("rawtypes")
	private static class WeakStoreListener implements StoreListener
	{
		// TODO this could be made into a reusable class?
		// I don't see much of a point in it, but it might be useful somewhere
		
		private WeakReference<DerivedStore> derived;
		
		public WeakStoreListener(DerivedStore store)
		{
			derived = new WeakReference<DerivedStore>(store);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onStoreChange(ReadableStore store, Object value) 
		{
			DerivedStore d = derived.get();
			
			if (d != null)
			{
				d.recalculate();
			}
			
			else
			{
				// The DerivedStore is no longer used, so we should remove this from the store listeners
				store.removeListener(this);
			}
			
		}
	}
	
}
