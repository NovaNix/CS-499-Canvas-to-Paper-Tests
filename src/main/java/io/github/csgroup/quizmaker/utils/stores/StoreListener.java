package io.github.csgroup.quizmaker.utils.stores;

import io.github.csgroup.quizmaker.utils.stores.readable.ReadableStore;

/**
 * A function that should be executed when the value of a {@link ReadableStore store} changes.
 * @param <T> the type of value stored in the store
 * 
 * @author Michael Nix
 */
public interface StoreListener<T> 
{
	/**
	 * A function called when the value of a {@link ReadableStore} changes. 
	 * @param store the store that was changed
	 * @param value the new value of the store
	 */
	public void onStoreChange(ReadableStore<T> store, T value);
}
