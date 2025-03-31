package io.github.csgroup.quizmaker.utils.stores.writable;

import java.util.Objects;

import io.github.csgroup.quizmaker.utils.stores.readable.AbstractReadableStore;

/**
 * An abstract default implementation of a {@link WritableStore}. Useful for writing implementations of WritableStores.<br>
 * <br>
 * Automatically handles adding and removing listeners, along with updating them when the contents of the store change.
 * @param <T> the type of value stored inside
 * @author Michael Nix
 */
public abstract class AbstractWritableStore<T> extends AbstractReadableStore<T> implements WritableStore<T>
{
	@Override
	public void set(T value)
	{
		T oldValue = this.get();
		
		setValue(value);
		
		if (!Objects.equals(value, oldValue)) // Prevent unnecessary updates if the new value is the same as the old value
		{
			// Note: Objects.equals is used here because it is null safe
			
			fireChange(value);
		}
	}
	
	// TODO the documentation on this could be clarified
	/**
	 * A function responsible for setting the actual internal value of the store. It should not notify the listeners.<br>
	 * <br>
	 * Note: This is different than the {@link WritableStore.set set()} function. 
	 * This function does what set() normally does: store the value in the store. set() calls this function to set the actual value. 
	 * However this function is not called from the outside, and does not need to manually notify the listeners.
	 * 
	 * @param value the value to store
	 */
	protected abstract void setValue(T value);
}
