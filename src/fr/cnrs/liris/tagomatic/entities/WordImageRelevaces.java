package fr.cnrs.liris.tagomatic.entities;

public class WordImageRelevaces {
	
	//1. Word probability without user assumptions
	private double freqRelOnlyWords;
	//2. Word probability with user proportion to the total number of users
	private double freqRelWordsAndAllUsers ;
	//3. Word probability. Word freq is represented by the number of unique users only
	private double freqRelWordAndOnlyUniqueUsers ;
	
	//1. Word probability TF.IDF approach without user assumptions
	private double tfIdfRelOnlyWords;
	//2. Word probability TF.IDF approach with user proportion to the total number of users
	private double tfIdfRelWordsAndAllUsers ;
	//3. Word probability TF.IDF approach.  Word freq is represented by the number of unique users only
	private double tfIdfRelWordAndOnlyUniqueUsers ;
	public double getFreqRelOnlyWords() {
		return freqRelOnlyWords;
	}
	public void setFreqRelOnlyWords(double freqRelOnlyWords) {
		this.freqRelOnlyWords = freqRelOnlyWords;
	}
	public double getFreqRelWordsAndAllUsers() {
		return freqRelWordsAndAllUsers;
	}
	public void setFreqRelWordsAndAllUsers(double freqRelWordsAndAllUsers) {
		this.freqRelWordsAndAllUsers = freqRelWordsAndAllUsers;
	}
	public double getFreqRelWordAndOnlyUniqueUsers() {
		return freqRelWordAndOnlyUniqueUsers;
	}
	public void setFreqRelWordAndOnlyUniqueUsers(
			double freqRelWordAndOnlyUniqueUsers) {
		this.freqRelWordAndOnlyUniqueUsers = freqRelWordAndOnlyUniqueUsers;
	}
	public double getTfIdfRelOnlyWords() {
		return tfIdfRelOnlyWords;
	}
	public void setTfIdfRelOnlyWords(double tfIdfRelOnlyWords) {
		this.tfIdfRelOnlyWords = tfIdfRelOnlyWords;
	}
	public double getTfIdfRelWordsAndAllUsers() {
		return tfIdfRelWordsAndAllUsers;
	}
	public void setTfIdfRelWordsAndAllUsers(double tfIdfRelWordsAndAllUsers) {
		this.tfIdfRelWordsAndAllUsers = tfIdfRelWordsAndAllUsers;
	}
	public double getTfIdfRelWordAndOnlyUniqueUsers() {
		return tfIdfRelWordAndOnlyUniqueUsers;
	}
	public void setTfIdfRelWordAndOnlyUniqueUsers(
			double tfIdfRelWordAndOnlyUniqueUsers) {
		this.tfIdfRelWordAndOnlyUniqueUsers = tfIdfRelWordAndOnlyUniqueUsers;
	}
	
	
}
