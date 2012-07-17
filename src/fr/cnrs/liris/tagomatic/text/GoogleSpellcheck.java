package fr.cnrs.liris.tagomatic.text;


import java.util.HashSet;
import java.util.Set;

import org.xeustechnologies.googleapi.spelling.Language;
import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;
import org.xeustechnologies.googleapi.spelling.SpellRequest;
import org.xeustechnologies.googleapi.spelling.SpellResponse;

public class GoogleSpellcheck {

	public static final String COMBINED_WORD_SEPARATOR = "_";
	public static final String SUGESSTION_SEPARATOR = "	";

	
	   
	public static SpellCorrection[] checkSpell(String word, Language lang) {
		SpellChecker checker = new SpellChecker();
		checker.setLanguage(lang);
		SpellRequest req = new SpellRequest(word);
		SpellResponse spellResponse = null;

		spellResponse = checker.check(req);
		return spellResponse.getCorrections();

	}

	public static String[] getSuggestionsOfCorrection(SpellCorrection sc) {
		String[] suggestions = sc.getValue().split(SUGESSTION_SEPARATOR);
		return suggestions;
	}
	
	public static Set<String> getCorrections(String word, Language lang){
		
		Set<String> corrections = new HashSet<String>() ;
		SpellCorrection[] checkSpells = checkSpell(word,lang) ;
		
		if(checkSpells != null){
		
			for (int i = 0; i < checkSpells.length; i++) {
				String[] corr = getSuggestionsOfCorrection(checkSpells[i]);
				
	//				for (int j = 0; j < corr.length; j++) {
						corrections.add(corr[0]);
	//				}
			}
//
		}
		return corrections ;
	}

	public static int getLengthOfSuggestion(SpellCorrection sc) {

		return getSuggestionsOfCorrection(sc).length;
	}

	public static String getMostProbaleSugesstionOfCorrection(String word,
			int sugIndex, Language lang) {

		SpellCorrection[] corrs = checkSpell(word, lang);
		if (corrs != null) {
			String[] sugs = getSuggestionsOfCorrection(corrs[0]);
			if (sugIndex < sugs.length) {
				
				return sugs[sugIndex].replace(" ", COMBINED_WORD_SEPARATOR); 
			}
		}
		return null;
	}

	public static String getMostProbaleSugesstionOfCorrection(String word) {

		return getMostProbaleSugesstionOfCorrection(word, 0, Language.ENGLISH);
	}

	public static void main(String[] args) {

		
		System.out.println("w4 "+ getMostProbaleSugesstionOfCorrection("littlegirl", 0,
							Language.ENGLISH));
		
		System.out.println(getCorrections("Beijing ExhibitionCenter", Language.ENGLISH));
		
		
//		
//		System.out.println("w4 "+ getMostProbaleSugesstionOfCorrection("new-york", 0,
//				Language.ENGLISH));
		
//		System.out.println("little_girl: " + WordNetUtil.isInWordNet("little_girl"));
//		System.out.println(WordNetUtil.isInWordNet("blue_car"));
//		System.out.println(WordNetUtil.isInWordNet(new String[]{"big","car"}));
//		String[] str1 = "big_car_bmw".split("_") ;
//		for (int i = 0; i < str1.length; i++) {
//			System.out.println(str1[i]);
//		}
//		
//		String[] str2 = "bigcar".split("_") ;
//		for (int i = 0; i < str2.length; i++) {
//			System.out.println(str2[i]);
//		}
//		
//		System.out.println("little_girl: " + WordNetUtil.isInWordNet("big_car_bmw","_"));
//		
//		Inflector inf = new Inflector() ;
//		String sig = inf.singularize("women");
//		System.out.println(sig);
//		System.out.println(inf.camelCase("hatem_mousselly-sergieh", true, new char[]{'_'}));
//		System.out.println(inf.humanize("hatem_mousselly-sergieh", new String[]{"-"}));
//		System.out.println(inf.singularize("buildings"));
	}
}
