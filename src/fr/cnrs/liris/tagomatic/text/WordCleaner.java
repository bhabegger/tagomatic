package fr.cnrs.liris.tagomatic.text;

import java.util.regex.Pattern;

import org.xeustechnologies.googleapi.spelling.Language;





public class WordCleaner {

	
	// Compile the patten.
	private static final Pattern realPattern = Pattern
			.compile("\\b(\\d+)\\.\\d+\\b");
	private static final Pattern realPattern2 = Pattern
			.compile("\\b(\\d+)\\,\\d+\\b");
	private static final Pattern intPattern = Pattern.compile("\\d+");

	public static boolean isNumber(String str) {

		if (str.matches(intPattern.pattern())
				|| str.matches(realPattern.pattern())
				|| str.matches(realPattern2.pattern())) {

			return true;

		}
		return false;

	}

	static public String removeSpecialCharacters(String orig) {
		String rv;

		// replacing with space allows the camelcase to work a little better in
		// most cases.
		rv = orig.replace("\\", "");
		rv = rv.replace("(", "");
		rv = rv.replace(")", "");
		rv = rv.replace("/", "");
		rv = rv.replace("-", "");
		// rv = rv.replace(","," ");
		rv = rv.replace(">", "");
		rv = rv.replace("<", "");
		// rv = rv.replace("-"," ");
		rv = rv.replace("&", "");
		rv = rv.replace("?", "");
		rv = rv.replace("+", "");
		rv = rv.replace("*", "");
		rv = rv.replace("}", "");
		rv = rv.replace("{", "");
		rv = rv.replace("[", "");
		rv = rv.replace("]", "");
		rv = rv.replace("%", "");
		rv = rv.replace("$", "");
		rv = rv.replace("§", "");
		rv = rv.replace("!", "");
		rv = rv.replace("|", "");
		rv = rv.replace("#", "");
		rv = rv.replace("~", "");
		rv = rv.replace("^", "");
		rv = rv.replace(";", "");
		rv = rv.replace("@", "");
		rv = rv.replace(".", "");

		// single quotes shouldn't result in CamelCase variables like Patient's
		// -> PatientS
		// "smart" forward quote
		rv = rv.replace("'", "");

		rv = rv.replace("\"", "");

		// if you have to find any more weird unicode chars, look here:
		// http://seth.positivism.org/man.cgi/7/groff_char
		rv = rv.replace("\u2019", ""); // smart forward (possessive) quote.

		// make sure to get rid of double spaces.
		// rv = rv.replace("   "," ");
		// rv = rv.replace("  "," ");
		
		//Needed to make the pareser recoginze combinded terms
		rv = rv.trim(); // Remove leading and trailing spaces. 
		rv= rv.replaceAll("(\\s)++", "_");
//		 rv = rv.replaceAll(";","");
//		 rv = rv.replaceAll("#","");

		

		return (rv);
	}

	static public String removeNumbers(String orig) {
		String rv;
		rv = orig.replaceAll(realPattern.pattern(), "");
		rv = orig.replaceAll(realPattern2.pattern(), "");
		rv = orig.replaceAll(intPattern.pattern(), "");
		return rv.trim();
	}

	
	public static void main(String[] args) {
		
//		System.out.println(removeNumbers("2012 Canada 2010 Acq"));
//		System.out.println(removeSpecialCharacters(removeNumbers("[new_york  usa  030 New York  City Hall Park  City Hall  Municipal Building]")));
//		
//		System.out.println(cleanTag("blue_skies",true));
//		System.out.println(cleanTag("bigLakes",false));
//		System.out.println(cleanTag("graetreafsrq2 car",false));
		
//		System.out.println(cleanTag("blue_skies",true,false,false));
//		System.out.println(cleanTag("blue_skies",true,true,false));
		System.out.println(cleanTag("bigLakes",true,false,true));
		System.out.println(cleanTag("bigLakes",true,true,true));

	}


	public static String cleanTag(String tag, boolean singularize) {
		
		Inflector inflator = new Inflector();
		//1- Remove Numbers and Special Charaters
		String newtag = WordCleaner.removeSpecialCharacters(WordCleaner.removeNumbers(tag));
		
		if(newtag.length() > 2){
			
			if(WordNetUtil.isInWordNet(newtag,GoogleSpellcheck.COMBINED_WORD_SEPARATOR)){
				if(singularize){
					newtag = inflator.singularize(newtag);
				}
				return newtag.toLowerCase();
			}
//			else{
//				//Handle combined words which are not in WordNet
//				String corrTag = GoogleSpellcheck.getMostProbaleSugesstionOfCorrection(newtag,0, Language.ENGLISH);
//				
//				if(corrTag != null){
//			
//					if(WordNetUtil.isInWordNet(corrTag,GoogleSpellcheck.COMBINED_WORD_SEPARATOR)){
//						
//						if(singularize){
//							corrTag = inflator.singularize(corrTag);
//						}
//						return corrTag.toLowerCase();
//					}
//				}
//				else{
//					return null ;
//				}
//			}
		}
		
		return null ;
		
	}
	
	public static String cleanTag(String tag, boolean singularize,
			boolean wordNet, boolean googleDYM) {


		// 1- Remove Numbers and Special Charaters
		String newtag = WordCleaner.removeSpecialCharacters(WordCleaner.removeNumbers(tag)).toLowerCase();

		
		if (newtag.length() < 3){
			return null;
		}

		//Do a wordNet check if needed
		if (wordNet && googleDYM) {
			
			if (!WordNetUtil.isInWordNet(newtag,GoogleSpellcheck.COMBINED_WORD_SEPARATOR)){ 
			
				// Handle combined words which are not in WordNet
				String corrTag = GoogleSpellcheck.getMostProbaleSugesstionOfCorrection(newtag, 0,Language.ENGLISH);

				if (corrTag != null) {

					newtag = corrTag;
					if (!WordNetUtil.isInWordNet(newtag,GoogleSpellcheck.COMBINED_WORD_SEPARATOR)) {
						return null ;
					}
				}
			}
		} 
		//In case that only a WordNet check is required
		else if (wordNet){
			if (!WordNetUtil.isInWordNet(newtag,GoogleSpellcheck.COMBINED_WORD_SEPARATOR))
				return null;
		}
		else if(googleDYM){
			String corrTag = GoogleSpellcheck.getMostProbaleSugesstionOfCorrection(newtag, 0,Language.ENGLISH);
			if(corrTag!= null)
				newtag = corrTag;
			
		}

		if (singularize) {
			Inflector inflator = new Inflector();
			newtag = inflator.singularize(newtag);
		}

		return newtag;

	}
	
	public static boolean compareTags(String tag1, String tag2, String sep, int numWordsThreshold){
		
		
		
		if(Math.abs(tag1.split(sep).length - tag2.split(sep).length) > numWordsThreshold)
			return false ;
		
		//In case the length
		
		
		String longerTag = null;
		String shorterTag = null ;
		
		//1-
		if(tag1.length() >= tag2.length()){
		
			longerTag = tag1;
			shorterTag = tag2 ;
		}
		else{
			longerTag = tag2;
			shorterTag = tag1 ;
		}
		//Compare
		if(longerTag.contains(shorterTag)){
				return true ;
		}
		else{ //Compare a word wise to the longer tag
			
			String[] subTags = shorterTag.split(sep);
			for (int i = 0; i < subTags.length; i++) {
				if(!longerTag.contains(subTags[i])){
					return false;
				}
			}
				
		}
		return true ;
	}
	
//	public String cleanTitle(String title, boolean singularize){
//		
//		WordNetUtil.extractNounsAsString(title,singularize);
//		
//		return null ;
//	}


}
