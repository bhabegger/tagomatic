package fr.cnrs.liris.tagomatic.entities;



import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Photo {

	private String photoId ;
	private String photoTitle;
	private String photoUrl;
	private String photoFileUrl;
	private double longitude ;
    private double latitude ;
    private double width;
    private double height ;
    private String uploadDate ; //": 22 January 2007"
    private String ownerId;
    private String ownerName; 
    private String ownerUrl ;
    private Set<String> tags = new HashSet<String>() ;
    private String technichalTags;
    private Date dateTaken;
    private String description ;
    //@TODO Add more properties
    
    
    
	public Collection<String> getTags() {
		return tags;
	}
	public Date getDateTaken() {
		return dateTaken;
	}
	public void setDateTaken(Date dateTaken) {
		this.dateTaken = dateTaken;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
	public String getTechnichalTags() {
		return technichalTags;
	}
	public void setTechnichalTags(String technichalTags) {
		this.technichalTags = technichalTags;
	}
	
	public String getPhotoId() {
		return photoId;
	}
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	public String getPhotoTitle() {
		return photoTitle;
	}
	public void setPhotoTitle(String photoTitle) {
		this.photoTitle = photoTitle;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getPhotoFileUrl() {
		return photoFileUrl;
	}
	public void setPhotoFileUrl(String photoFileUrl) {
		this.photoFileUrl = photoFileUrl;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerUrl() {
		return ownerUrl;
	}
	public void setOwnerUrl(String ownerUrl) {
		this.ownerUrl = ownerUrl;
	}
	
	@Override
	public String toString() {
	
		StringBuffer str = new StringBuffer();
		str.append("[ID: ").append(photoId).append(", ").
		append("Title: ").append(photoTitle).append(", ").
		append("Photo URL: ").append(photoUrl).append(", ").
		append("Owner URL;").append(photoId).append(", ").
		append("(longitude,latitude):  (").append(longitude).append(",").append(latitude).append(")]");

		return str.toString();
	}
    
    
    
}
