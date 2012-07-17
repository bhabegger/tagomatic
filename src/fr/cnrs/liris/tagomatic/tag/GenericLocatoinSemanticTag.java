package fr.cnrs.liris.tagomatic.tag;

import java.util.List;


public class GenericLocatoinSemanticTag extends GenericSemanticTag {
	
	
	private List<SpecificLocation> locations  ;
	private List<SpecificLocation> suggestions  ;

	public List<SpecificLocation> getInstances() {
		return locations;
	}

	public void setInstances(List<SpecificLocation> locations) {
		this.locations = locations;
	}

	public void addLocation(SpecificLocation loc){
		this.locations.add(loc);
	}

	public List<SpecificLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<SpecificLocation> locations) {
		this.locations = locations;
	}

	public List<SpecificLocation> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<SpecificLocation> suggestions) {
		this.suggestions = suggestions;
	}
	
	
	
}
