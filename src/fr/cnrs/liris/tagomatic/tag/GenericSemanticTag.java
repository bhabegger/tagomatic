package fr.cnrs.liris.tagomatic.tag;

public class GenericSemanticTag extends SemanticTag{
	
	
	protected String representative ;
	protected String[] synonyms ;
	
	
	public String getRepresentative() {
		return representative;
	}
	public void setRepresentative(String representative) {
		this.representative = representative;
	}
	public String[] getSynonyms() {
		return synonyms;
	}
	public void setSynonyms(String[] synonyms) {
		this.synonyms = synonyms;
	}
	public String getWordNetEntry() {
		return wordNetEntry;
	}
	public void setWordNetEntry(String wordNetEntry) {
		this.wordNetEntry = wordNetEntry;
	}

	
	
	
}
