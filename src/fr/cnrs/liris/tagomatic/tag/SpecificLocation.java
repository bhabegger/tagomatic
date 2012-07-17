package fr.cnrs.liris.tagomatic.tag;

public class SpecificLocation {
	
	private String name ;
	private String adminArea ;
	private double distAdminArea ;
	private String country ;
	private double distance ;
	private String wikiArticle;
	private String others ;
	private String type ;
	
	
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getDistAdminArea() {
		return distAdminArea;
	}
	public void setDistAdminArea(double distAdminArea) {
		this.distAdminArea = distAdminArea;
	}
	public String getAdminArea() {
		return adminArea;
	}
	public void setAdminArea(String adminArea) {
		this.adminArea = adminArea;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getWikiArticle() {
		return wikiArticle;
	}
	public void setWikiArticle(String wikiArticle) {
		this.wikiArticle = wikiArticle;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		
		str.append("(Name: ").append(name).append(" , ").append("Country: ").append(country).append(", distance : ").append(distance).
		append("Adminstrative: ").append(adminArea).
		append(", distance : ").append(distAdminArea).append(", Type: ").
		append(type).append(")");
		return str.toString();
	}

}
