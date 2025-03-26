package io.github.csgroup.quizmaker.utils.stores.writable;

/**
 * A default implementation of a {@link WritableStore}
 * @param <T> the type of value stored inside
 * @see <a href="https://svelte.dev/docs/svelte/stores">Svelte Stores</a>
 * @author Michael Nix
 */
public class DefaultWritableStore<T> extends AbstractWritableStore<T>
{
	// TODO could be renamed to "writable" to match svelte's naming conventions

	private T value;
	
	public DefaultWritableStore()
	{
		this.value = null;
	}
	
	public DefaultWritableStore(T defaultValue)
	{
		this.value = defaultValue;
	}
	
	@Override
	protected void setValue(T value) 
	{
		this.value = value;
	}

	@Override
	public T get() 
	{
		return value;
	}

}
