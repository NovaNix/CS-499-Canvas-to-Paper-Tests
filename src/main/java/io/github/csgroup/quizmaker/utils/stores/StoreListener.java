package io.github.csgroup.quizmaker.utils.stores;

import io.github.csgroup.quizmaker.utils.stores.readable.ReadableStore;

public interface StoreListener<T> 
{
	public void onStoreChange(ReadableStore<T> store, T value);
}
