package fr.cnrs.liris.tagomatic.text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


//import shef.nlp.wordnet.similarity.SimilarityMeasure;


import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;



public class WordNetUtil {

	
	public static Dictionary WordNet ;
	private static Inflector inflector = new Inflector();
//	public static SimilarityMeasure SimilarityMeasure ;
	
	static{
		
		ResourceBundle params = ResourceBundle.getBundle("config", Locale.ENGLISH);

		String configFilePath = params.getString("config_file_path").trim();
		String simMeasureClass = params.getString("sim_measure_class").trim();

		String infoContentFileURL = params.getString("info_content_file_url").trim();
		try {
			JWNL.initialize(new FileInputStream(configFilePath));
			WordNet = Dictionary.getInstance();

			// Create a map to hold the similarity config params
			Map<String, String> params2 = new HashMap<String, String>();

			// the simType parameter is the class name of the measure to use
			params2.put("simType", simMeasureClass);

			// this param should be the URL to an infocontent file (if required
			// by the similarity measure being loaded)
			params2.put("infocontent", infoContentFileURL);

			// this param should be the URL to a mapping file if the
			// user needs to make synset mappings
			// params.put("mapping","file:C:/test/d.txt");

			// create the similarity measure
//			SimilarityMeasure = SimilarityMeasure.newInstance(params2);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Check if a word is in wordnet
	 * @param word
	 * @return
	 */
	public static boolean isInWordNet(String word){
		
		
		word = inflector.singularize(word);
		
		IndexWord w = null;
		try {
			w = WordNet.getIndexWord(POS.NOUN, word);
			if(w != null){
				return true ;
			}
			w = WordNet.getIndexWord(POS.ADJECTIVE, word);
			if(w != null){
				return true ;
			}
			w = WordNet.getIndexWord(POS.ADVERB, word);
			if(w != null){
				return true ;
			}
			w = WordNet.getIndexWord(POS.VERB, word);
			if(w != null){
				return true ;
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return false ;
	}

	public static boolean isInWordNet(String[] word){
		
		for (int i = 0; i < word.length; i++) {
			
			if(!isInWordNet(word[i])){
				return false ;
			}
		}
		
		return true ;
		
	}
	
	public static boolean isInWordNet(String words, String sep){
		
		if(isInWordNet(words))
			return true ;
		
		String[] wordArr = words.split(sep);

		return isInWordNet(wordArr);
				
	}
//	public static String makeSingular(String word) {
//		
//		
//		if(word.endsWith("ies")){
//			word = word.substring(0,word.length()-3);
//			word = word + "y";
//			return word ;
//		}
//		if(word.endsWith("es")){
//			word = word.substring(0,word.length()-1);
//			return word;
//		}
//		if(word.endsWith("s")){
//			word = word.substring(0,word.length()-1);
//			return word;
//		}
//		return word ;
//		
//	}
	
	public static List<String> extractNouns(String text, boolean singularize) {
		List<String> result = new ArrayList<String>();
		text =  WordCleaner.removeSpecialCharacters(WordCleaner.removeNumbers(text));
		String[] words = text.split("_");
		for (int i = 0; i < words.length; i++) {
			String word = words[i] ;
			
			if(singularize){
				word = inflector.singularize(word) ;
			}
			                  
			try {
				if(WordNet.getIndexWord(POS.NOUN, word) != null){
					if(word.length() > 2)
						result.add(word.toLowerCase());
				}
			} catch (JWNLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result ;
	}
		
	public static String extractNounsAsString(String text, boolean singularize) {
		
		if(text.length() < 3)
			return null ;
		
		StringBuffer result = new StringBuffer();
		text = WordCleaner.removeSpecialCharacters(WordCleaner
				.removeNumbers(text));
		
		String[] words = text.split("_");
		
		try{
			for (int i = 0; i < words.length; i++) {
				
				String word = words[i];
	
				if (singularize) {
					word = inflector.singularize(word);
					if (WordNet.getIndexWord(POS.NOUN, word) != null) {
						if (word.length() > 2)
							result.append(word.toLowerCase()).append("_");
					}
				}
				else {
					if (WordNet.getIndexWord(POS.NOUN, inflector.singularize(word)) != null) {
						if (word.length() > 2)
							result.append(word.toLowerCase()).append("_");
					}
	
				}
			}
		}
		catch (Exception e) {
		
			e.printStackTrace();
		}
		//some problems appear with foreign languages such as arabic
		if(result.length() < 3)
			return null ;
		return result.toString().substring(0,result.length()-1);
	}

	public static void main(String[] args)  {
		System.out.println(isInWordNet("escalators"));
		System.out.println(isInWordNet("skies"));
		System.out.println(extractNouns("Liverpool - St. George's Halls[26.09.2010]", true));
		System.out.println(WordCleaner.cleanTag("view_west_bank_east", true));
		System.out.println(extractNounsAsString("Liverpool - St. George's Halls[26.09.2010]", false));

	}
	
}
