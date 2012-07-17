package fr.cnrs.liris.tagomatic.entities;

public class GpsCoordinates {
	
	float latitude;
	float longitude;
	
	
	public GpsCoordinates(float latitude, float longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "Latitude: " + latitude + ", Longitude: " + longitude;
	}

}