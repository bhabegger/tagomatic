package fr.cnrs.liris.tagomatic.imagefetch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.json.JSONObject;

import fr.cnrs.liris.tagomatic.entities.Photo;
import fr.cnrs.liris.tagomatic.text.TextProcess;
import fr.cnrs.liris.tagomatic.text.WordCleaner;


public abstract class ImageDataFetcher {


	protected static boolean singularize = false;
	protected static boolean wordNetCheck = false  ;
	protected static boolean googleCheck  = false ;
	protected static boolean uniqueUser  ;
	protected static int maxUserOccurances  ;
	protected String inputPhotoId ;
	protected Set<String> uniqueUsers = new HashSet<String>();
	
	protected void loadParameters(){
		
		Properties properties = new Properties();
		
		try {
			properties.load(new FileInputStream("parameters.properties"));
			uniqueUser = Boolean.valueOf(properties.getProperty("uniqueUser").trim());
			maxUserOccurances  =Integer.valueOf(properties.getProperty("maxUserOccurances").trim());
			
		}
		catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
		
			e.printStackTrace();
		}
		
	}
	
	public static void setSingularize(boolean singularize) {
		PanoramionImageDataFetcher.singularize = singularize;
	}

	public static void setWordNetCheck(boolean wordNetCheck) {
		PanoramionImageDataFetcher.wordNetCheck = wordNetCheck;
	}

	public static void setGoogleCheck(boolean googleCheck) {
		PanoramionImageDataFetcher.googleCheck = googleCheck;
	}
	
	protected void convertTitleToTags(Photo photo, String tt) {
		System.out.println("Handle Image title: " + tt);
		
		String[] titleAsArray = tt.split(" ");
		if(titleAsArray.length <= 3){
			photo.getTags().add(tt);
			return;
		}
		
		if(tt!=null){
			
			//Extract Noun Phrases from the Title
			List<String> title = TextProcess.extractNounPhrases(tt,true);
		
			for(String str: title){
				//Build noun pharase of only two names @TODO enhance this approach
				Collection<String> result = TextProcess.getNounPhraseSet(str);
				for(String t:result){
					t = WordCleaner.cleanTag(t, singularize, wordNetCheck, googleCheck);
					if(t!= null)
						photo.getTags().add(t);
				}
				
			}
		}
	}
	
	public Set<String> getuniqueUsers(){
		return this.uniqueUsers ;
	}
	
	public abstract Map<String, Photo> createPhotoObjects(String jsonResp);

	
	public abstract String handleRequest();

}
