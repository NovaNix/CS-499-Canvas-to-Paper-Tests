package io.github.csgroup.quizmaker.utils.stores.writable;

import io.github.csgroup.quizmaker.utils.stores.readable.ReadableStore;

/**
 * A {@link ReadableStore Store} that can be written to.<br>
 * <br>
 * Based on <a href="https://svelte.dev/docs/svelte/stores">Stores</a> from Svelte.
 * @param <T> the type of value stored inside
 * @see <a href="https://svelte.dev/docs/svelte/stores">Svelte Stores</a>
 * @author Michael Nix
 */
public interface WritableStore<T> extends ReadableStore<T> 
{
	/**
	 * Sets the value of the store
	 * @param value the new value of the store
	 */
	public void set(T value);
}
