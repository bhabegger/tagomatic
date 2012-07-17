package fr.cnrs.liris.tagomatic.entities;

public class SimilarImageInfo {

	private String id ;
	private double score ;
	private double pointSimilarity ;
	private int numberOfCommonKeyPoints;
	
	
	
	
	public SimilarImageInfo() {
		
	}


	public SimilarImageInfo(String id, double score, double pointSimilarity,
			int numberOfCommonKeyPoints) {

		this.id = id;
		this.score = score;
		this.pointSimilarity = pointSimilarity;
		this.numberOfCommonKeyPoints = numberOfCommonKeyPoints;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getPointSimilarity() {
		return pointSimilarity;
	}
	public void setPointSimilarity(double pointSimilarity) {
		this.pointSimilarity = pointSimilarity;
	}
	public int getNumberOfCommonKeyPoints() {
		return numberOfCommonKeyPoints;
	}
	public void setNumberOfCommonKeyPoints(int numberOfCommonKeyPoints) {
		this.numberOfCommonKeyPoints = numberOfCommonKeyPoints;
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.getId()).append(" , ").append(this.getPointSimilarity())
		.append(" , ").append(this.numberOfCommonKeyPoints);
		return str.toString();
	}
	
	
}
