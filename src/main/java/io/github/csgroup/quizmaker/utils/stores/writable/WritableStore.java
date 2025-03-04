package io.github.csgroup.quizmaker.utils.stores.writable;

import io.github.csgroup.quizmaker.utils.stores.readable.ReadableStore;

public interface WritableStore<T> extends ReadableStore<T> 
{
	public void set(T value);
}
