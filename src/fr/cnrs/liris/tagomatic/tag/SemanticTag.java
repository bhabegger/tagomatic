package fr.cnrs.liris.tagomatic.tag;

public class SemanticTag {
	
	protected String tag;
	protected SemanticTagClass clazz ;
	protected String wordNetEntry;
	protected String wikiEntry ;

	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public SemanticTagClass getClazz() {
		return clazz;
	}
	public void setClazz(SemanticTagClass clazz) {
		this.clazz = clazz;
	}
	public String getWordNetEntry() {
		return wordNetEntry;
	}
	public void setWordNetEntry(String wordNetEntry) {
		this.wordNetEntry = wordNetEntry;
	}
	public String getWikiEntry() {
		return wikiEntry;
	}
	public void setWikiEntry(String wikiEntry) {
		this.wikiEntry = wikiEntry;
	}

	
	
}
