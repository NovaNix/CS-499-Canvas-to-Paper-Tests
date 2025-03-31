package io.github.csgroup.quizmaker.utils.stores.derived;

/**
 * Code to call to update the value of a {@link DerivedStore} when its Stores are updated.
 * 
 * @param <T> the type of value stored in the DerivedStore
 * 
 * @author Michael Nix
 */
public interface DerivedStoreCalculator<T> 
{
	/**
	 * Calculates a new value for the DerivedStore. Called when one of the stores that it listens to are updated.<br>
	 * <br>
	 * Note: the calculate function should be a <a href="https://en.wikipedia.org/wiki/Pure_function">pure function</a>,
	 * meaning it should not have any <a href="https://en.wikipedia.org/wiki/Side_effect_(computer_science)">side effects</a>. 
	 * 
	 * @return the new store value
	 */
	public T calculate();
}
