package fr.cnrs.liris.tagomatic.mgr;


import fr.cnrs.liris.tagomatic.entities.GpsCoordinates;
import fr.cnrs.liris.tagomatic.entities.RankingMethod;
import fr.cnrs.liris.tagomatic.entities.TagRank;
import fr.cnrs.liris.tagomatic.entities.WordImage;
import fr.cnrs.liris.tagomatic.exif.ExifExtractor;
import fr.cnrs.liris.tagomatic.util.TagPostProcessing;
import hms.wikipedia.search.WikiEntryFinder;
import ij.ImagePlus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.objectml.xml.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



/**
 * Geo-based automatic image annotator. It expect as input a url for a geotagged image with its coordinates
 * and delivers ranked tag recommendations
 * @author Hatem
 *
 */

public class OnlinePhotoAnnotatorFrontEnd implements Processor{


	private float lat;
	private float lon;
	private String photoURL;
	private Map<String, TagRank> tagRankMap;
	
	//You can set this parameters using parameters.properties
	private boolean useWikipedia = true;
	private int wikiQuerySize = 3; 
	private String wikiSearchLanaguag = "en";
	private boolean useGoogleDYM = false;
	private RankingMethod rankingMethod ;
	private String xmlTagRecommendation;
	
	
	public OnlinePhotoAnnotatorFrontEnd(float lat, float lon, String photoURL) {
		this.lat = lat;
		this.lon = lon;
		this.photoURL = photoURL;
		loadPrameters();
	}
	
	public OnlinePhotoAnnotatorFrontEnd(String photoURL) {
		this.photoURL = photoURL;
		ExifExtractor exifEx = new ExifExtractor();
		GpsCoordinates coord = exifEx.exifExtractor(photoURL);
		this.lat = coord.getLatitude();
		this.lon = coord.getLongitude();
		loadPrameters();
	}


	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public String getPhotoURL() {
		return photoURL;
	}


	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	
	
	public Map<String, TagRank> getTagRankMap() {
		return tagRankMap;
	}

	

	public String getXmlTagRecommendation() {
		return xmlTagRecommendation;
	}


	/**
	 * Recommend a tag with different kinds of file reporting statistics about the images and the tags 
	 * used to generate the recommendations
	 * @param iter if true then iterative image matching will be applied
	 */
	public void suggestTagsVerbose(String outResult) {
		
		
		OnlinePhotoAnnotator tagger = new OnlinePhotoAnnotator(this.photoURL, this.lat,this.lon,false);
		
		// The list of tag suggestions
		Set<String> allCandidateWords = new HashSet<String>();
		Map<String, WordImage> wordImageRel = new HashMap<String, WordImage>();
		
		// Start searching and get relevant tags
		tagger.findGeoSurfSimilarPhotosWithTagStatistics(allCandidateWords,wordImageRel, true, false, false);
		// Calculate the relevance between the candidate tags and the input  image
		System.out.println("Calculate Word Image Relevance");
	
		tagger.performNewCalculations(wordImageRel, outResult);
		
		tagger.shutdown();
			
		System.out.println("Done !!!");
	}



	
	/**
	 * Recommend a tag without generating statistics file
	 * @param iter if true then iterative image matching will be applied
	 */
	public void suggestTagsCompact() {
		
	
		OnlinePhotoAnnotator tagger = new OnlinePhotoAnnotator(this.photoURL, this.lat,this.lon,true);
		
		// The list of tag suggestions
		Set<String> allCandidateWords = new HashSet<String>();
		Map<String, WordImage> wordImageRel = new HashMap<String, WordImage>();
		
		// Start searching and get relevant tags
		tagger.findGeoSurfSimilarPhotosWithTagStatistics(allCandidateWords,wordImageRel, true, false, true);
		
		// Calculate the relevance between the candidate tags and the input  image
		System.out.println("Calculate Word Image Relevance");
	
		this.tagRankMap = tagger.recommedTagsComapct(wordImageRel);
	
		
	}


	/**
	 * Search for a Wikipedia article corresponding to a collection of tags
	 * @param tagRecommCleand The collection of tags
	 * @param maxTerms The max number of tags used in building the query
	 * @param lang The language of the articles.
	 * @return Set of article suggestion if some are available and null otherwise
	 */
	private String[] suggestWikiArticle(Collection<String> tagRecommCleand, int maxTerms,String lang) {
		
		String wikiQuery = "";
		
		for(String tag : tagRecommCleand){
			wikiQuery +=  tag;
			int queryLength = wikiQuery.split(" ").length;
			
			if(queryLength > maxTerms)
				break;
		
			wikiQuery += " " ;
		}	
		
		String[] wikiSuggestions = WikiEntryFinder.findWikiEntryWithSuggestions(wikiQuery.trim(), lang);
		
		return wikiSuggestions;
		
	}


	
	
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		Date startDate = new Date(startTime);
		System.out.println("Starting Image Annotator: " + startDate);
		
		OnlinePhotoAnnotatorFrontEnd tagRec = new OnlinePhotoAnnotatorFrontEnd("E:/photos/2012-06-04 Hongk ong/100MEDIA/IMAG1695.jpg");
				
		tagRec.generateTagRecommentations();
		
		System.out.println(tagRec.getXmlTagRecommendation());
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("Time Taken: " + ((double)endTime-startTime)/(double)1000 + " Seconds");

		System.out.println("Done !!!");

	}


	public void generateTagRecommentations() {
		
		suggestTagsCompact();
		
		Map<String, TagRank> rwaTags = getTagRankMap();
		//remove stop words
		TagPostProcessing tpp = new TagPostProcessing();
		rwaTags = tpp.removeStopWordsFromTagList(rwaTags,useGoogleDYM);
		//Merge Similar Tags
		
		Map<String, TagRank> mergedTags = tpp.mergeSimilarTags(tpp.orderTagListAccordingToTagLength(rwaTags));
		//Order the tags
		Map<String, Double> orderedTags1 = tpp.orderTagList(mergedTags,rankingMethod);
		
		
		//Search for a corresponding Wikipedia article based on the top n tags
		String[] wikiSugg  = null;
		
		if(this.useWikipedia){
			wikiSugg = suggestWikiArticle(orderedTags1.keySet(),wikiQuerySize,wikiSearchLanaguag);
			//Output Possible Wiki Entries
			
		}
		
		
		//Output tags:
		StringBuffer xmlOutput = new StringBuffer();
		xmlOutput.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>");
		xmlOutput.append("\n");
		xmlOutput.append("<wordlist>");
		xmlOutput.append("\n");
		System.out.println("----------------- Tag Ordered According to freqAllUsers -----------------------");
		
		for(String tag : orderedTags1.keySet()){
			
			xmlOutput.append("<tag score=\""+orderedTags1.get(tag)+"\">"+tag+"</tag>");
			xmlOutput.append("\n");			
		}
		
//		text, description and url
		if(wikiSugg!=null){
			
			xmlOutput.append("<WikiEntry text =\"" +wikiSugg[0]+ "\">\n");
			xmlOutput.append("<Description>"+wikiSugg[1]+"</Description>\n");
			xmlOutput.append("<url>"+wikiSugg[2]+"</url>\n");
			xmlOutput.append("</WikiEntry>\n");
			
		}
		
		xmlOutput.append("</wordlist>\n");

		this.xmlTagRecommendation = xmlOutput.toString();
	}
	
	
	private void loadPrameters(){
		
		Properties properties = new Properties();
		
		try {
			properties.load(new FileInputStream("parameters.properties"));
			this.useGoogleDYM = Boolean.valueOf(properties.getProperty("useGoogleDYM").trim());
			this.useWikipedia = Boolean.valueOf(properties.getProperty("useWikipedia").trim());
			this.wikiQuerySize =  Integer.valueOf(properties.getProperty("wikiQuerySize").trim());
			this.wikiSearchLanaguag = properties.getProperty("wikiSearchLanaguag").trim();
			String rankMethod = properties.getProperty("rankingMethod").trim();
			
			if(rankMethod.equals("freqNoUsers")){
				this.rankingMethod = RankingMethod.freqNoUsers;
			}
			if(rankMethod.equals("freqUniqueUsers")){
				this.rankingMethod = RankingMethod.freqUniqueUsers;
			}
			if(rankMethod.equals("freqAllUsers")){
				this.rankingMethod = RankingMethod.freqAllUsers;
			}
			if(rankMethod.equals("tfidfNoUsers")){
				this.rankingMethod = RankingMethod.tfidfNoUsers;
			}
			if(rankMethod.equals("tfidfAllUsers")){
				this.rankingMethod = RankingMethod.tfidfAllUsers;
			}
			if(rankMethod.equals("tfidfUniqueUsers")){
				this.rankingMethod = RankingMethod.tfidfUniqueUsers;
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	@Override
	public void process(Exchange exchange) throws Exception {
		
		//Get Image Path
		Message in = exchange.getIn();
		File input = in.getBody(File.class);
		
		String imagePath = input.getPath();
		
		OnlinePhotoAnnotatorFrontEnd tagRec = new OnlinePhotoAnnotatorFrontEnd(imagePath); 
		
		tagRec.generateTagRecommentations();
		
		Document output = XMLHelper.parse(tagRec.getXmlTagRecommendation());
		
		Message out = exchange.getOut();
			
		out.setBody(output);
			
		
	}
	

}
