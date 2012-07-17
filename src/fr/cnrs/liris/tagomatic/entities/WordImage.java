package fr.cnrs.liris.tagomatic.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Hatem
 * 
 */
public class WordImage {

	private String word;
	private int uniqueUserCount ;
	private Set<SimilarImageInfo> simlarImages = new HashSet<SimilarImageInfo>();
	
	//Indexed by image ID
	private Map<String, SimilarImageInfo> simlarImagesMap = new HashMap<String, SimilarImageInfo>();
	
	
	private Set<String> notSimlarImages = new HashSet<String>();
	
	private Map<String, SimilarImageInfo> notSimlarImagesMap = new HashMap<String, SimilarImageInfo>();

	private int ocurrances;
	
	//Word probability.. different approaches are available
	private double probability;
	
	//1. Word probability without user assumptions
	private double PwOnlyWords;
	//2. Word probability with user proportion to the total number of users
	private double PwAllUsers ;
	//3. Word probability. Word freq is represented by the number of unique users only
	private double PwOnlyUniqueUsers ;
	
	//1. Word probability TF.IDF approach without user assumptions
	private double PwTFIDFlOnlyWords ; 
	//2. Word probability TF.IDF approach with user proportion to the total number of users
	private double PwTFIDFlAllUsers ; 
	//3. Word probability TF.IDF approach.  Word freq is represented by the number of unique users only
	private double PwTFIDFOnlyUniqueUsers ; 
	
	
	
	private double imageProbability ;
	
	
	private Set<String> relatedImgIDs = new HashSet<String>() ;
	private Set<String> notSimilarImageIDs = new HashSet<String>();
	private int totalOcurences;
	
	

	public double getImageProbability() {
		return imageProbability;
	}

	public void setImageProbability(double imageProbability) {
		this.imageProbability = imageProbability;
	}

	public WordImage(String word) {
		this.word = word;
		uniqueUserCount = 1 ;
	}

	public int getOcurrances() {
		return ocurrances;
	}
	
	

//	
	public int getUniqueUserCount() {
		return uniqueUserCount;
	}

	public void setUniqueUserCount(int uniqueUserCount) {
		this.uniqueUserCount = uniqueUserCount;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	

	public double getPwOnlyWords() {
		return PwOnlyWords;
	}

	public void setPwOnlyWords(double pwOnlyWords) {
		PwOnlyWords = pwOnlyWords;
	}

	public double getPwAllUsers() {
		return PwAllUsers;
	}

	public void setPwAllUsers(double pwAllUsers) {
		PwAllUsers = pwAllUsers;
	}

	public double getPwOnlyUniqueUsers() {
		return PwOnlyUniqueUsers;
	}

	public void setPwOnlyUniqueUsers(double pwOnlyUniqueUsers) {
		PwOnlyUniqueUsers = pwOnlyUniqueUsers;
	}

	public double getPwTFIDFlOnlyWords() {
		return PwTFIDFlOnlyWords;
	}

	public void setPwTFIDFlOnlyWords(double pwTFIDFlOnlyWords) {
		PwTFIDFlOnlyWords = pwTFIDFlOnlyWords;
	}

	public double getPwTFIDFlAllUsers() {
		return PwTFIDFlAllUsers;
	}

	public void setPwTFIDFlAllUsers(double pwTFIDFlAllUsers) {
		PwTFIDFlAllUsers = pwTFIDFlAllUsers;
	}

	public double getPwTFIDFOnlyUniqueUsers() {
		return PwTFIDFOnlyUniqueUsers;
	}

	public void setPwTFIDFOnlyUniqueUsers(double pwTFIDFOnlyUniqueUsers) {
		PwTFIDFOnlyUniqueUsers = pwTFIDFOnlyUniqueUsers;
	}

	public void setOcurrances(int numberOfOcurrances) {
		this.ocurrances = numberOfOcurrances;
	}

	public void addSimilarImage(SimilarImageInfo img) {
		simlarImages.add(img);
		relatedImgIDs.add(img.getId());

	}

	public SimilarImageInfo getSimilarImage(String id){
		return simlarImagesMap.get(id);
	}
	
	public SimilarImageInfo getNotSimilarImage(String id){
		return notSimlarImagesMap.get(id);
	}
	
	public void addSimilarImage(String imgId, SimilarImageInfo info){
		
		this.simlarImagesMap.put(imgId, info);
		this.relatedImgIDs.add(info.getId());
		
	}
	
	public void addSimilarImageId(String imgID){
		this.relatedImgIDs.add(imgID);
	}
	
	public void addNotSimilarImage(String imgId, SimilarImageInfo info){
		
		this.notSimlarImagesMap.put(imgId, info);
		this.notSimilarImageIDs.add(info.getId());
		this.relatedImgIDs.add(info.getId());
	}
	
	

	public Set<String> getNotSimilarImageIDs() {
		return notSimilarImageIDs;
	}

	public void addNotSimilarImageIDs(String id) {
		 notSimilarImageIDs.add(id);
	}

	public String getWord() {
		return word;
	}

	public Set<SimilarImageInfo> getSimlarImages() {
		return simlarImages;
	}

	public Map<String, SimilarImageInfo> getSimlarImagesMap() {
		return simlarImagesMap;
	}
	
	public Map<String, SimilarImageInfo> putSimlarImagesMap() {
		return simlarImagesMap;
	}
	
	public Set<String> getNotSimlarImages() {
		return notSimlarImages;
	}
	
	

	public Map<String, SimilarImageInfo> getNotSimlarImagesMap() {
		return notSimlarImagesMap;
	}

	public Set<String> getSimilarImageIDs() {
		return relatedImgIDs;
	}

	public double calculateSimilarityOfRelatedImages() {
		double totalSim = 0;
		for (SimilarImageInfo imgInfo : simlarImages) {

			// Convert distance to similarity
			double sim = Math.exp((-0.5) * imgInfo.getScore()
					* imgInfo.getScore());
			totalSim += sim;
		}

		return totalSim;
	}

	// public double calculateAvgDistanceOfRelatedImages(){
	// //The format used for similar image string is score, #common keypoints |
	// photoID
	//
	// int size = simlarImages.size();
	// if(size > 0){
	// double totalSim = 0;
	// for(SimilarImageInfo imgInfo : simlarImages){
	// double sim = Double.valueOf(imgInfo.getScore());
	// totalSim += sim;
	// }
	// return totalSim/size ;
	// }
	// System.out.println("No Similar Images");
	// return -10 ;
	// }

	public double calculatePointSimilarityOfRelatedImages() {
		double pointSim = 0;
		for (SimilarImageInfo imgInfo : simlarImages) {
			double sim = Double.valueOf(imgInfo.getPointSimilarity());
			pointSim += sim;
		}

		return pointSim;
	}
	
	public Set<String> getWordUniqueUsers(Map<String, Photo> photoList ){
		
		
		Set<String> userIdList = new HashSet<String>();
		Iterator<SimilarImageInfo> simImgsIter = simlarImages.iterator();

		while (simImgsIter.hasNext()) {
			SimilarImageInfo similarImageInfo = (SimilarImageInfo) simImgsIter.next();
			String photoID = similarImageInfo.getId();
			String userID = photoList.get(photoID).getOwnerId();
			
			if(!userIdList.contains(userID)){
				
				userIdList.add(userID);
			}
			
		}
		for(String photoID : notSimilarImageIDs){
			
			String userID = photoList.get(photoID).getOwnerId();
			
			if(!userIdList.contains(userID)){
			
				userIdList.add(userID);
			}
		}
		
		return userIdList;
	}

	public void setTotalOcurances(int totalOcur) {

		this.totalOcurences = totalOcur;
		
	}

	public int getTotalOcurences() {
		return totalOcurences;
	}

	public void setTotalOcurences(int totalOcurences) {
		this.totalOcurences = totalOcurences;
	}
	
	

	// public static void main(String[] args) {
	// WordImage w = new WordImage("city");
	//
	// w.addSimilarImage("0.1,10|id1");
	// w.addSimilarImage("0.3,10|id2");
	// w.addSimilarImage("0.2,10|id3");
	// w.addSimilarImage("0.5,10|id4");
	//
	// System.out.println(w.calculateDistanceOfRelatedImages());
	//
	// Set<String> set = new HashSet<String>() ;
	//
	// set.add("hatem");
	// set.add("carl");
	//
	//
	// set.add("hatem");
	// set.add("carl");
	//
	// set.add("hatem");
	// set.add("carl");
	//
	// set.add("hatem");
	// set.add("carl");
	//
	// System.out.println(set);
	//
	// }
}
