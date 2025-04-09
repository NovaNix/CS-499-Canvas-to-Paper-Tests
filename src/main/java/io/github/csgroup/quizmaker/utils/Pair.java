package io.github.csgroup.quizmaker.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A basic Pair implementation. Represents a key value pair
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @author Michael Nix
 */
public record Pair<K, V> (K key, V value)
{
	/**
	 * Extracts a list of key value pairs from a map
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V> List<Pair<K, V>> fromMap(Map<K, V> map)
	{
		List<Pair<K, V>> pairs = new ArrayList<Pair<K, V>>();
		
		for (var key : map.keySet())
		{
			pairs.add(new Pair<K, V>(key, map.get(key)));
		}
		
		return pairs;
	}
}
