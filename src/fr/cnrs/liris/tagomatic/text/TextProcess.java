package fr.cnrs.liris.tagomatic.text;

import dragon.config.ConceptExtractorConfig;
import dragon.config.LemmatiserConfig;
import dragon.config.PhraseExtractAppConfig;
import dragon.nlp.ontology.BasicVocabulary;
import dragon.nlp.tool.Annie;
import dragon.nlp.tool.HeppleTagger;
import dragon.nlp.tool.Lemmatiser;
import dragon.nlp.tool.lemmatiser.EngLemmatiser;
import dragon.nlp.tool.xtract.EngWordPairGenerator;
import dragon.nlp.tool.xtract.SimpleXtract;
import dragon.nlp.tool.xtract.WordPairStat;
import dragon.nlp.*;
import dragon.nlp.extract.BasicConceptFilter;
import dragon.nlp.extract.BasicPhraseExtractor;
import dragon.nlp.extract.EngDocumentParser;
import dragon.nlp.extract.PhraseExtractor;
import dragon.util.*;


import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class TextProcess {
	
	static{
		EnvVariable.setDragonHome("E:/Developement/Dragon/");
	

	}

	/**
	 * process a text file
	 * @param args args[0] file name for processing, args[1] output file name
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		

		
		List<String> result =extractNounPhrases("A mighty woman with a torch whose flame  Is the imprisoned lightning  Statue of Liberty",true);
		System.out.println(result);
		
//		extractNounPhrases("the beautiful Essen");
		
//		List<String>  result = extractNounPhrases("big nice animal");
		for(String str: result)
			System.out.println(getNounPhraseSet(str));
		
//		extractNounPhrases("Jack");
//		extractNounPhrases("a nice flower");
//		extractNounPhrases("big nice city");
//		extractNounPhrases("at a big nice city of Berlin");
//		extractNounPhrases("Passau and big Berlin");
//		extractNounPhrases("the wonderful great Neuschwanstein of Germany");
//		extractNounPhrases("Leuchtturm Langeland (Kelsnor)");
//		extractNounPhrases("Big General Statue");
//		extractNounPhrases("General Grant Statue and from the Capitol"); //Deal with this case
		
//		for(String noun:result){	
//			String rev = reverse(noun);
//			System.out.println(rev);
//		}

	}
	
	public static Sentence tagSentence(String s){	
	
		EngDocumentParser parser = new EngDocumentParser();
		HeppleTagger tagger = new HeppleTagger();
//		EngLemmatiser lemmatiser = new EngLemmatiser(false,false);		
		//Start working
		Sentence sentence = parser.parseSentence(s);
		tagger.tag(sentence);
		
		System.out.println(sentence.toPOSTaggedString());
	
		return sentence;
	}
	
	public static void extractPhrases(String s){
		
		
		EngLemmatiser lemmatiser = new EngLemmatiser(false,false);	
		Sentence sent = tagSentence(s);
		HeppleTagger tagger = new HeppleTagger();

		Word[] words = new Word[ sent.getWordNum()];
		

		//Extract Noun Phrases
		
		
		//VOC

		String vobFile = "E:/Developement/eclipseTagRecom/GeoTagger/eclipseTagRecom/DragonTest/nlpdata/exp/latimes.vob";
		BasicVocabulary vocabulary = new BasicVocabulary(vobFile,lemmatiser);
        
//        vocabulary.setAdjectivePhraseOption(true);
//        vocabulary.setCoordinateOption(true);
//        vocabulary.setNPPOption(true);
//        vocabulary.setNonBoundaryPunctuation();
		
		Word dd = vocabulary.findPhrase(sent.getFirstWord());
		System.out.println(dd);
		
		
		PhraseExtractor extractor = new BasicPhraseExtractor(vocabulary, lemmatiser, tagger) ;
		
//		 extractor.setConceptFilter(new BasicConceptFilter());
//	        extractor.setSubConceptOption(false);
//	        extractor.setFilteringOption(false);
//	        extractor.setSingleAdjectiveOption(false);
//	        extractor.setSingleNounOption(false);
//	        extractor.setSingleVerbOption(false);
	        
	       
		ArrayList list = extractor.extractFromSentence(sent);	
		System.out.println(list);
		EngDocumentParser parser = new EngDocumentParser();
		Document doc = parser.parse(FileUtil.readTextFile("c:/hatem/mywork/development/sampleText/1.txt"));
		list = extractor.extractFromDoc(doc);
		System.out.println(list);
	}
	
	public  static void testXtract(){
	
		SimpleXtract sX  = new SimpleXtract(4, "C:/Hatem/MyWork/Development/eclipseTagRecom/DragonTest/indexclustering/Newsgroup/phraseindex");
		sX.extract(1, 1, 4, 0.75, "c:/hatem/mywork/development/sampleText/23.txt");
	
		System.out.println("OK");
		
		
	}
	
	public static void testAnni() throws Exception{
		Annie a = new Annie();
		ArrayList re = a.extractEntities("syria is a Country In Western Asia, bordering Lebanon AND the Mediterranean Sea to the West, Turkey to the north, Iraq to the east, Jordan to the South, and Israel to the southwest.");
		System.err.println(re);
		
		 re = a.extractEntities("Jack Mosolini is an engineer at Apple Coorporation and Microsoft");
		 
		 EngDocumentParser parser = new EngDocumentParser();
		Document doc = parser.parse(FileUtil.readTextFile("c:/hatem/mywork/development/sampleText/england.txt"));
		re = a.extractFromDoc(doc);
		System.err.println(re);
	}
	
	public static List<String> extractNounPhrases(String text){
		
		List<String> result = new ArrayList<String>();
		EngDocumentParser parser = new EngDocumentParser();
		HeppleTagger tagger = new HeppleTagger();
		//EngLemmatiser lemmatiser = new EngLemmatiser(false,false);			
		//Start working
		Sentence sentence = parser.parseSentence(text);
		if(sentence==null)
			return result;
		tagger.tag(sentence);
		result = doNFExtraction(sentence);	
		return result ;
		
	}
	
	public static List<String> extractNounPhrases(String text,boolean prop){
		
		if(prop){
			text = text.toLowerCase().replace(" of ", " ");
			
//			text = text.replace(" with ", " ");
		}
		return extractNounPhrases(text);
		
	}

	public static String reverse(String noun) {
	
		String[] rightNs = noun.split(" ");
		if(rightNs.length == 1)
			return noun;
		
		
		StringBuffer result = new StringBuffer();
		
		for (int i = rightNs.length-1; i >= 0; i--) {
			result.append(rightNs[i]);
			if(i!=0)
				result.append("_");
			
		}
		return result.toString() ;
	}
	
	public static Collection<String> getNounPhraseSet(String noun) {
		
		
		Collection<String> nList = new HashSet<String>();
		
		String[] rightNs = noun.split(" ");
		
		if(rightNs.length == 0){
			return nList;
		}
		
		if(rightNs.length == 1){
			nList.add(noun.trim());
			
		}
		
		else{
			StringBuffer result = new StringBuffer();
			result.append(rightNs[rightNs.length-1]).append("_").append(rightNs[rightNs.length-2]);
			nList.add(result.toString());
			
			noun = noun.replace(rightNs[rightNs.length-1], "");
			noun = noun.replace(rightNs[rightNs.length-2], "");
			
			nList.addAll(getNounPhraseSet(noun));
			
		}
		return nList;
	}

	private static int getLastNounIndex(Sentence sentence) {
		for (int i = sentence.getWordNum()-1; i >= 0; i--) {
			
			Word word = sentence.getWord(i);
			if(word.getPOSLabel().equals("NN") || word.getPOSLabel().equals("NNP")){
			
				System.out.println(word.getName());
				System.out.println(word.getPOSLabel()+ " " + word.getPOSIndex());
				return i ;
			}
			
		}
		
		return -1;
	}
	
	private static List<String> doNFExtraction(Sentence sentence) {
		
		List<String> past =  new ArrayList<String>() ;
		List<String> nounPhrases = new ArrayList<String>();
		List<Integer> indices = new ArrayList<Integer>();
		
	
		int max = sentence.getWordNum()-1;
		StringBuffer nounPhrase = new StringBuffer() ;
		
		for (int i = max ; i >= 0; i--) {
				
			int nnVisted = 0 ;
			Word word = sentence.getWord(i);
			if(word.getPOSLabel().equals("NN") || word.getPOSLabel().equals("NNP")){
			
//				System.out.println(word.getName());
//				System.out.println(word.getPOSLabel()+ " " + word.getPOSIndex());
				
				
				nounPhrase = new StringBuffer(); 
				nounPhrase.append(word.getName());
				if(past.size() > 0){
					
					for(String pp : past){
						nounPhrase.append(" ").append(pp);					
					}
					
					past = new ArrayList<String>(); 
				}
				
				int wordProcessed = 0 ;
				
				for (int j = i-1; j >= 0; j--) {
					
					wordProcessed++ ;
					
					
					
				    Word preWord = sentence.getWord(j);
					
					if(preWord.getPOSLabel().equals("IN")){ //Proposition
						nounPhrases.remove(nounPhrase.toString());
						past.add(nounPhrase.toString());
						nounPhrase = null ;
						i -= (wordProcessed - nnVisted);
						break ;
						
					}
					if(preWord.getPOSLabel().equals("EX")||preWord.getPOSLabel().equals("RB")||preWord.getPOSLabel().equals("DT")){ //adverbs or articles
						continue;
					}
					if(preWord.getPOSLabel().equals("CC")){ //adverbs
						nounPhrases.add(nounPhrase.toString());
						
						i -= (wordProcessed - nnVisted);
						break ;
					}
					if(preWord.getPOSLabel().equals("VBN") || preWord.getPOSLabel().equals("JJ")||preWord.getPOSLabel().equals("NN") || preWord.getPOSLabel().equals("NNP")){ //adjective or nouns
						nounPhrase.append(" ").append(preWord.getName());
						i -= 1;
						nnVisted ++ ;
						
					}
//					if(j == 0){
//						nounPhrases.add(nounPhrase.toString());
//					}
					
				}
				indices.add(i) ;
				
				if(past.size() > 0  && !existNoun(sentence,i)){
					for(String pp : past){
						nounPhrases.add(pp);
					}
					
				}
				
				
			}
		}
		if(nounPhrase!= null){ //Only one word
			nounPhrases.add(nounPhrase.toString());

		}
		
		return nounPhrases;
	}

	private static boolean existNoun(Sentence sentence, int i) {
		for (int j = 0; j < i; j++) {
			Word preWord = sentence.getWord(j);
			if(preWord.getPOSLabel().equals("VBN") ||
					preWord.getPOSLabel().equals("JJ")||
					preWord.getPOSLabel().equals("NN") ||
					preWord.getPOSLabel().equals("NNP")){ //adjective or nouns
				return true ;
				
			}
		}
		return false;
	}
}
