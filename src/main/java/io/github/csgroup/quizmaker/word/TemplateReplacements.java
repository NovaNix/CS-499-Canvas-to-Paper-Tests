package io.github.csgroup.quizmaker.word;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.csgroup.quizmaker.data.quiz.QuizMetadata.MetadataType;
import io.github.csgroup.quizmaker.utils.Pair;
import io.github.csgroup.quizmaker.utils.stores.writable.DefaultWritableStore;
import io.github.csgroup.quizmaker.utils.stores.writable.WritableStore;

/**
 * A container for text that should be replaced in the template with {@link QuizMetadata metadata} from a {@GeneratedQuiz quiz} 
 * 
 * @author Michael Nix
 */
public class TemplateReplacements 
{

	private final Map<MetadataType, WritableStore<String>> replacementStrings = new HashMap<MetadataType, WritableStore<String>>();
	
	public TemplateReplacements()
	{
		for (var r : MetadataType.values())
		{
			replacementStrings.put(r, new DefaultWritableStore<String>(""));
		}
	}
	
	/**
	 * Sets the text that should be replaced with the particular metadata value
	 * @param value what metadata will be placed in that spot
	 * @param replacement what text will be replaced with the metadata
	 */
	public void setReplacementString(MetadataType value, String replacement)
	{
		replacementStrings.get(value).set(replacement);
	}
	
	/**
	 * @return a list of the strings that should be replaced for each metadata type
	 */
	public List<Pair<MetadataType, String>> getReplacementText()
	{
		return Pair.fromMap(replacementStrings).stream()
			.map(p -> new Pair<>(p.key(), p.value().get()))
			.toList();
	}
	
	/**
	 * @return a list of all of the metadatatype and store key value pairs
	 */
	public List<Pair<MetadataType, WritableStore<String>>> getStores()
	{
		return Pair.fromMap(replacementStrings);
	}
	
	/**
	 * @param r 
	 * @return the text that should be replaced for the metadata type
	 */
	public String getReplacementText(MetadataType r)
	{
		return replacementStrings.get(r).get();
	}
	
	public WritableStore<String> getStore(MetadataType r)
	{
		return replacementStrings.get(r);
	}
	
	
	
}
