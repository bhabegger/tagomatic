package fr.cnrs.liris.tagomatic.entities;

import java.io.PrintWriter;

import fr.cnrs.liris.tagomatic.tag.SemanticTag;

public class WordImageRelevanceStatistics {
	
	private String word ;
	
	private SemanticTag semanticTag;
	
	
	private double totalDistBasedSimilarity ; // The distance between an unlabled image Iu and a set of images I
//	private double totalSimilarity;// The visual similarity between an unlabled image Iu and a set of images I
	
	private double totalWDistBasedSimilarity ;   // The visual distance between an unlabled image Iu and a set of images annotated with a word (w)
//	private double totalWSimilarity ; // The visual similarity between an unlabled image Iu and a set of images annotated with a word (w)
	
	
	
	//Point based similarity
	private double totalWPiontSimilarity;
	private double totalPiontSimilarity;
	
	private double PRwDist ;
	private double PRDist;
	
	private int Rw ; // (corresponds to tf(w)) The number of photos which are visually similar to Iu and annotated with (w)
	private int Qw ; //The number of photos which are visually not similar to Iu and annotated with (w)
	private int Lw ; // (corresponds to df(w)) Rw + Lw the number of images annotated with w.
	private int C ; // The total size of image set
	
	//Word distribution measures
	//1) Relevance measure which gives more weight for the word according to it usage frequency
//	private double Pwrw; // It is calculated as Pwrw = Rw - Rnw*(Lw/C) 
	
	//2) tf.idf measure. Consider the IR retrieval approach for determining word importance
//	private double idfW ; //The inverse document frequency of a word (w) idf(w) = 1/log2(df(w)+1)
//	private double tfIdfW ;
	
	//Word Image Relevance Measure: it is a combination of word importance (distribution) and image visual similarity. 
	//The idea is to adjust the importance of a word by a factor derived from the visual similarity of the unlabled image 
	//and the set of images annotated with this word
	
	//1) Using Pwrw
	double PwICtfIdfDist ;
	//2) Using tf.idf measure
	double PwICtfIdfVoting  ;
	
	
	//New Measure : p(w|Iu,C)= p(Iu|w,c).p(w|C)/p(Iu|C) 
	//                       = p(Rw).p(w|C)/p(R) = wTotalSim.p(w|C)/totalSim
	//TFIDF : p(w|C) = (|Rw|/|R|).log2(|C|/|Lw|)
	//Voting: p(w|C) = |Rw| - |R'w|.(|Lw|/|C|)
	
	double PRw ;
	double PR ;
	double PwCTFIDF ;

	private double PwCVoting;

	private int R;
	
	private double PwICtfIdf ;
	private double PwICVoting ;
	
	
	public WordImageRelevanceStatistics(String word, int C,
			int R, int Rw, int Qw, double totalWDistBasedSimilarity, double totalDistBasedSimilarity, 
			double totalWPiontSimilarity, 
			double totalPiontSimilarity ) {
		
		this.word = word ;
	
		this.Rw = Rw;
		this.Qw = Qw;
		this.C = C;
		this.R = R ;
		this.Lw = Rw + Qw; 
		
		this.totalDistBasedSimilarity = totalDistBasedSimilarity;
		this.totalWDistBasedSimilarity = totalWDistBasedSimilarity;
		this.totalWPiontSimilarity = totalWPiontSimilarity ;
		this.totalPiontSimilarity = totalPiontSimilarity ;
		
		//calculate p(r) and p(Rw)
		//1- distance based calculation
		PRwDist = totalWDistBasedSimilarity/C;
		PRDist = totalDistBasedSimilarity/C;
		//2- point similarity based calculation
		PRw = totalWPiontSimilarity / C ;
		PR = totalPiontSimilarity/C ;
		
		//Calculate P(w|C)
		//1- TF.IDF approach //Normalize by log2C
		PwCTFIDF = ((double)Rw/R)*(Math.log((double) C/Lw )/ Math.log( 2 ))/(Math.log((double) C)/ Math.log( 2 )); 
		//2- Voting based approach // normalize by |Rw|
		PwCVoting = ((double)Rw - Qw*((double)Lw/C))/Rw;
		
		//Calculate the word image relevance p(w|Iu,C) 
		PwICtfIdf = (PRw * PwCTFIDF)/PR; 
		PwICVoting = (PRw * PwCVoting)/PR;
		PwICtfIdfDist =  (PRwDist*PwCTFIDF)/PRDist;
    	PwICtfIdfVoting =  (PRwDist*PwCVoting)/PRDist ;
		
		
	}
	
	
	
	
	
	
	
	public SemanticTag getSemanticTag() {
		return semanticTag;
	}







	public void setSemanticTag(SemanticTag semanticTag) {
		this.semanticTag = semanticTag;
	}







	public String getWord() {
		return word;
	}





	public void setWord(String word) {
		this.word = word;
	}





	public int getR() {
		return R;
	}





	public double getPwICtfIdf() {
		return PwICtfIdf;
	}





	public double getPwICVoting() {
		return PwICVoting;
	}





	public double getTotalWPiontSimilarity() {
		return totalWPiontSimilarity;
	}





	public void setTotalWPiontSimilarity(double totalWPiontSimilarity) {
		this.totalWPiontSimilarity = totalWPiontSimilarity;
	}





	public double getTotalPiontSimilarity() {
		return totalPiontSimilarity;
	}





	public void setTotalPiontSimilarity(double totalPiontSimilarity) {
		this.totalPiontSimilarity = totalPiontSimilarity;
	}





	public double getPwCTFIDF() {
		return PwCTFIDF;
	}





	public void setPwCTFIDF(double pwCTFIDF) {
		PwCTFIDF = pwCTFIDF;
	}





	public double getPwCVoting() {
		return PwCVoting;
	}





	public void setPwCVoting(double pwCVoting) {
		PwCVoting = pwCVoting;
	}





	public double getPRw() {
		return PRw;
	}





	public double getPR() {
		return PR;
	}





	public static void main(String[] args) {
		
		WordImageRelevanceStatistics iwrs = new WordImageRelevanceStatistics("car",547, 17, 2, 0, 5,12,6,18);
		
	
		System.out.println("Tag," +
				"C," +
				"R," +
				"Rw," +
				"R'w," +
				"Lw," +
				"P(R)," +
				"P(Rw)," +
				"P(w|C):TF," +
				"P(w|C):Voting," +
				"P(w|Iu-C):TF-Point," +
				"P(w|Iu-C):Voting-Point," +
				"P(w|Iu-C):TF-Dist," +
				"P(w|Iu-C):Voting-Dist,"
		); 
		
		System.out.println(
				"word ," +
				iwrs.getC() + "," + 
				iwrs.getR() + "," +
				iwrs.getRw()+ "," +
				iwrs.getQw() + "," +
				iwrs.getLw() + "," +
				iwrs.getPR() + "," +
				iwrs.getPRw() + "," +
				iwrs.getPwCTFIDF() + "," +
				iwrs.getPwCVoting() + "," +
				iwrs.getPwICtfIdf() + "," +
				iwrs.getPwICVoting() + "," +
				iwrs.getPwICtfIdfDist() + "," +
				iwrs.getPwICtfIdfVoting() + ","
				
				);	
		
		System.out.println();
		
	}
	public double getTotaldistance() {
		return totalDistBasedSimilarity;
	}
//	public double getTotalSimilarity() {
//		return totalSimilarity;
//	}
	public double getTotalWDistance() {
		return totalWDistBasedSimilarity;
	}
//	public double getTotalWSimilarity() {
//		return totalWSimilarity;
//	}
	public double getRwVisualSimilarityFactor() {
		return PRwDist;
	}
	public int getRw() {
		return Rw;
	}
	public int getQw() {
		return Qw;
	}
	public int getLw() {
		return Lw;
	}
	public int getC() {
		return C;
	}
//	public double getPwrw() {
//		return Pwrw;
//	}
//	public double getIdfW() {
//		return idfW;
//	}
//	public double getTfIdfW() {
//		return tfIdfW;
//	}
	public double getPwICtfIdfDist() {
		return PwICtfIdfDist;
	}
	public double getPwICtfIdfVoting() {
		return PwICtfIdfVoting;
	}



	
	
	
}
