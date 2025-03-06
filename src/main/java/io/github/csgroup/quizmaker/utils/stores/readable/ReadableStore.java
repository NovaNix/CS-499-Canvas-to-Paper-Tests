package io.github.csgroup.quizmaker.utils.stores.readable;

import io.github.csgroup.quizmaker.utils.stores.StoreListener;

/**
 * A read-only container for a value that will update {@link StoreListener StoreListeners} when its value changes.<br>
 * <br>
 * Based on <a href="https://svelte.dev/docs/svelte/stores">Stores</a> from Svelte.
 * @param <T> the type of value stored inside
 * @see <a href="https://svelte.dev/docs/svelte/stores">Svelte Stores</a>
 * @author Michael Nix
 */
public interface ReadableStore<T>
{

	/**
	 * @return the value stored inside of the store
	 */
	public T get();
	
	/**
	 * Adds a new listener to be notified when the value of the store changes
	 * @param listener the listener to notify
	 */
	public void addListener(StoreListener<T> listener);
	
	/**
	 * Unsubscribes a listener to changes to this store
	 * @param listener the listener to no longer notify
	 */
	public void removeListener(StoreListener<T> listener);
	
	/**
	 * @return the number of listeners actively listening to the store
	 */
	public int getListenerCount();
}
