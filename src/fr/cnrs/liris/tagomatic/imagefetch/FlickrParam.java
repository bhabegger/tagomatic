package fr.cnrs.liris.tagomatic.imagefetch;

public class FlickrParam {

	public String apiKey= "4b1f8453276ecc729863e33beaf96846" ;
	public String secret= "b0b809b0119b2c1e" ;
	public String token="72157626454975218-c6525a27a304ff3e";
	
	
//	private String apiKey = "api_key";
//	private String userId = "user_id"; 
//	private String tags = "tags" ; 
//	private String text ="text"; 
//	private String minUploadDate ="min_upload_date"; 
//	private String maxUploadDate ="max_upload_date" ;
//	private String minTakeDate ="min_taken_date"; 
//	private String maxTakenDate ="max_taken_date"; 
//	private String license ="license"; 
//	private String sort ="sort" ; 
//	private String privacyFilter ="privacy_filter"; 
//	private String bbox ="bbox";
//	private String accuracy ="accuracy"; 
//	private String safeSearch ="safe_search"; 
//	private String contentType ="content_type"; 
//	private String machineTags = "machine_tags"; 
//	private String machineTagMode = "machine_tag_mode"; 
//	private String groupId ="group_id"; 
//	private String contacts ="contacts"; 
//	private String woeID = "woe_id" ; 
//	private String placeId = "place_id"; 
//	private String meidia ="media"; 
	private int hasGeo ;  //	Any photo that has been geotagged, or if the value is "0" any photo that has not been geotagged. 
//	private String geoContext ="geo_context"; 
	private float lat ; 
	private float lon ; 
	private float radius  ; 
//	private String radiusUnits ="radius_units"; 
//	private String isCommons ="is_commons"; 
//	private String inGallery ="in_gallery"; 
//	private String isGetty ="is_getty"; 
//	private String extras ="extras";
	private int perPage ; 
	private int page ;
	private int pages ;
//	private String format ="format" ;
	
	
	public FlickrParam(String apiKey, int hasGeo, 
			float lat, float lon,
			float radius,
			int page,
			int pages, 
			int perPage) {
	
		this.apiKey = apiKey;
		this.hasGeo = hasGeo;
		this.lat = lat;
		this.lon = lon;
		this.radius = radius;
		this.pages = pages;
		this.perPage = perPage;
		this.page = page ;
	}
	
	public FlickrParam(int hasGeo, 
			float lat, float lon,
			float radius,
			int page,
			int pages, 
			int perPage) {
	
		
		this.hasGeo = hasGeo;
		this.lat = lat;
		this.lon = lon;
		this.radius = radius;
		this.pages = pages;
		this.perPage = perPage;
		this.page = page ;
	}
	
	public FlickrParam(float lat, float lon,
			float radius,
			int page,
			int perPage) {
	
		
//		this.hasGeo = hasGeo;
		this.lat = lat;
		this.lon = lon;
		this.radius = radius;
		this.perPage = perPage;
		this.page = page ;
	}
	
	
	public FlickrParam(float lat, float lon) {
	
		this.hasGeo = 1;
		this.lat = lat;
		this.lon = lon;
		this.radius = 0.25f;
		this.pages = 10 ;
		this.perPage = 100;
		this.page = 1;
	}


	public String getApiKey() {
		return apiKey;
	}


	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}


	public int getHasGeo() {
		return hasGeo;
	}


	public void setHasGeo(int hasGeo) {
		this.hasGeo = hasGeo;
	}


	public float getLat() {
		return lat;
	}


	public void setLat(float lat) {
		this.lat = lat;
	}


	public float getLon() {
		return lon;
	}


	public void setLon(float lon) {
		this.lon = lon;
	}


	public float getRadius() {
		return radius;
	}


	public void setRadius(float radius) {
		this.radius = radius;
	}


	public int getPerPage() {
		return perPage;
	}


	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}


	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public int getPages() {
		return pages;
	}


	public void setPages(int pages) {
		this.pages = pages;
	}


	public String getSecret() {
		return secret;
	}


	public void setSecret(String secret) {
		this.secret = secret;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	

	
	
}
