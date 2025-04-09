package io.github.csgroup.quizmaker.data.quiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.csgroup.quizmaker.utils.Pair;
import io.github.csgroup.quizmaker.utils.stores.writable.DefaultWritableStore;
import io.github.csgroup.quizmaker.utils.stores.writable.WritableStore;

/**
 * A container for course and quiz information, such as the professor's name and the total points possible.
 * 
 * @author Michael Nix
 */
public class QuizMetadata 
{
	private Map<MetadataType, WritableStore<String>> data = new HashMap<>();
	
	public QuizMetadata()
	{
		
	}
	
	/**
	 * Sets a specific nondynamic metadata value
	 * @param type the metadata being set
	 * @param value the value of the metadata
	 * @return whether the value was successfully set or not (you cannot set dynamic values)
	 * @see {@link MetadataType#isDynamic}
	 */
	public boolean setValue(MetadataType type, String value)
	{
		if (type.isDynamic())
			return false;
		
		set(type, value);
		
		return true;
	}
	
	/**
	 * Sets the value of the metadata, regardless of whether it's dynamic or not.
	 * @param type
	 * @param value
	 */
	private void set(MetadataType type, String value)
	{
		if (data.get(type) == null)
			data.put(type, new DefaultWritableStore<String>());
		
		data.get(type).set(value);
	}
	
	/**
	 * Sets the contents of the dynamic values from a GeneratedQuiz
	 * @param quiz
	 */
	public void setDynamicValues(GeneratedQuiz quiz)
	{
		set(MetadataType.Points, Float.toString(quiz.getTotalPoints()));
	}
	
	public String getValue(MetadataType r)
	{
		return data.get(r).get();
	}
	
	public WritableStore<String> getStore(MetadataType r)
	{
		return data.get(r);
	}
	
	public List<Pair<MetadataType, String>> asList()
	{
		return Pair.fromMap(data).stream()
			.map(p -> new Pair<>(p.key(), p.value().get()))
			.toList();
	}
	
	public Map<MetadataType, String> asMap()
	{
		Map<MetadataType, String> meta = new HashMap<>();
		
		for (var key : data.keySet())
		{
			meta.put(key, data.get(key).get());
		}
		
		return meta;
	}
	
	public List<Pair<MetadataType, WritableStore<String>>> getStores()
	{
		return Pair.fromMap(data);
	}
	
	@Override
	public QuizMetadata clone()
	{
		QuizMetadata meta = new QuizMetadata();
		
		for (var key : data.keySet())
		{
			meta.set(key, data.get(key).get());
		}
		
		return meta;
	}
	
	public static enum MetadataType
	{
		ClassNum("Class Number"), 
		SectionNum("Section Number"), 
		Points("Points", true), 
		TestNum("Test Number"), 
		Date("Date"), 
		Professor("Professor"), 
		Minutes("Minutes");
		
		private String displayName;
		private boolean dynamic;
		
		private MetadataType(String displayName)
		{
			this(displayName, false);
		}
		
		private MetadataType(String displayName, boolean dynamic)
		{
			this.displayName = displayName;
			this.dynamic = dynamic;
		}
		
		public String getDisplayName()
		{
			return displayName;
		}
		
		/**
		 * @return whether the value for this type will be set automatically when generating the quiz.<br>
		 * These values should not be set manually by the user.
		 */
		public boolean isDynamic()
		{
			return dynamic;
		}
	}
}
