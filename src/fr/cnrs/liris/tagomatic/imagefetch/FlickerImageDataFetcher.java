package fr.cnrs.liris.tagomatic.imagefetch;


import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.Size;
import com.aetrion.flickr.tags.Tag;

import fr.cnrs.liris.tagomatic.entities.Photo;
import fr.cnrs.liris.tagomatic.text.WordCleaner;
import fr.cnrs.liris.tagomatic.text.WordNetUtil;



public class FlickerImageDataFetcher extends ImageDataFetcher {

	
	private  FlickrParam params ;
	private static Flickr flickr;
	private static RequestContext requestContext;
	private String IMAGE_SIZE = "Large"; //The preferable size of the dowloaded images. Values are Large and Medium. In it set to null small sized images will be considered. 

	
	
	public FlickerImageDataFetcher(FlickrParam params, String inputPhotoId){
		
		loadParameters();

		this.inputPhotoId = inputPhotoId;
		this.params = params ;
		
		try {

			flickr = new Flickr(params.getApiKey(), params.getSecret(),new REST());
			requestContext = RequestContext.getRequestContext();
			Auth auth = new Auth();
			auth.setPermission(Permission.DELETE);
			auth.setToken(params.getToken());
			requestContext.setAuth(auth);
			Flickr.debugRequest = false;
			Flickr.debugStream = false;

		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}
	}
	
	
	
	
	public  String prepareFlickrGeoRequest(){
		
		String url = null ;
		
		if(this.params != null){
			url = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key="+params.getApiKey()+
//			"&has_geo=" + param.getHasGeo()+
			"&min_taken_date=1%2F1%2F2005" +
			"&lat="+params.getLat()+"&lon="+params.getLon()
			+"&radius="+params.getRadius()+"&page="+params.getPage()//+"&pages="+params.getPages()
			+"&per_page="+params.getPerPage()+"&format=json";
			
			
	
		}
		else {
			System.out.println("Pleas initialize the flickr parameters using the method setParm(...)");
		}
		
		System.out.println(url);
		return url ;
	}
	
	
	
	public Map<String, Photo>  getFlickrGeoSimilarPhotoList() {
			
		String req = prepareFlickrGeoRequest();
		
		String jsonRS = handleFlickrRequest(req) ;
		
		System.out.println("Total: " + getTotalNumOfGeoClosePhotos(jsonRS));
		
		Map<String, Photo> photoList = createPhotoObjects(jsonRS);
		
		return photoList ;
		
		
		
	}
	
	public  int getTotalNumOfGeoClosePhotos(String jsonResp){
		jsonResp = jsonResp.replace("(", "").substring(jsonResp.indexOf("{")-1);
		JSONObject jsonObj;
		JSONObject rootElement;
		int total = -1 ;
		try {
			jsonObj = new JSONObject(jsonResp);
			jsonObj.keys();
			rootElement = jsonObj.getJSONObject("photos");
			total = rootElement.getInt("total");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return total ;
		
	}
	
//	public  Map<String, Photo> createPhotoObjects(String jsonResp){
//		
//		
//		Set<String> uniqueUsers = new HashSet<String>();
//		
//		Map<String, Photo> photoDataList = new HashMap<String, Photo>();
//		jsonResp = jsonResp.replace("(", "").substring(jsonResp.indexOf("{")-1);
//		
//		try {
//			JSONObject jsonObj = new JSONObject(jsonResp);
//			jsonObj.keys();
//			JSONObject rootElement = jsonObj.getJSONObject("photos");
//			
//			
//		
//			JSONArray photoElements = rootElement.getJSONArray("photo") ;
//			
//			for (int i = 0; i < photoElements.length(); i++) {
//				
//				JSONObject photoElement = (JSONObject) photoElements.get(i);
//				String ownerID = photoElement.getString("owner");
//				
//				if(!uniqueUsers.contains(ownerID)){
//					
//					uniqueUsers.add(photoElement.getString("owner"));
//					
//					Photo photo = new Photo();
//					String pID = photoElement.getString("id");
//					String pTitlte = photoElement.getString("title");
//					
//					photo.setPhotoId(pID);
//					photo.setPhotoTitle(pTitlte);
//					photo.setOwnerId(ownerID);
//					
//					//More properties of the photo (There are more flickr data to be considered)
//					PhotosInterface pIF = flickr.getPhotosInterface();
//			 		com.aetrion.flickr.photos.Photo flickPhoto = pIF.getPhoto(pID);
//			 		
//			 		
//			 		//Get the url of the photo. For the moment the URL for medium photo is being taken
//			 		String pURL = flickPhoto.getMediumUrl();
//			 		photo.setPhotoFileUrl(pURL);
//			 		photo.setPhotoUrl(flickPhoto.getUrl());
//			 		photo.setDateTaken(flickPhoto.getDateTaken());
//			 		photo.setDescription(flickPhoto.getDescription());
//			 		photo.setLatitude(flickPhoto.getGeoData().getLatitude());
//			 		photo.setLongitude(flickPhoto.getGeoData().getLongitude());
//			 		
//					//handle photo tags
//					Set<String> photoTags = getPhotoTags(pID);
//					photo.setTags(photoTags);
//					
//					//Simple title processing
//	//				String title = WordNetUtil.extractNounsAsString(photo.getPhotoTitle(),true);
//					
//					String photoTitle = photo.getPhotoTitle();
//					
//					//If title is composed of comma separated sentences deal with it as a list of titles
//					String titles[]  = photoTitle.split(",");
//					
//					for (int j = 0; j < titles.length; j++) {
//						//Prepare titles
//						convertTitleToTags(photo, titles[j]);
//						
//					}
//					
//					photoDataList.put(photo.getPhotoId(),photo);
//					}
//				else{
//					System.out.println("Same USER: "+ ownerID);
//				}
//				
//			}
//			
//		} catch (JSONException e) {
//		
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FlickrException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return photoDataList ;
//		
//	}
	
	
	public  Map<String, Photo> createPhotoObjects(String jsonResp){
		
		return createPhotoObjects(jsonResp,uniqueUser,maxUserOccurances);
		
		
	}
	public  Map<String, Photo> createPhotoObjects(String jsonResp, boolean uniqueUser, int max){
		
		
		HashMap<String,Integer> uniqueUsers = new HashMap<String, Integer>();
		
		Map<String, Photo> photoDataList = new HashMap<String, Photo>();
		jsonResp = jsonResp.replace("(", "").substring(jsonResp.indexOf("{")-1);
		
		try {
			JSONObject jsonObj = new JSONObject(jsonResp);
			jsonObj.keys();
			JSONObject rootElement = jsonObj.getJSONObject("photos");	
			JSONArray photoElements = rootElement.getJSONArray("photo") ;
			
			for (int i = 0; i < photoElements.length(); i++) {
			
				JSONObject photoElement = (JSONObject) photoElements.get(i);
				
				//Exclude the photo if it is the same as the input photo
				if(photoElement.getString("id").equals(inputPhotoId)){
					System.out.println("Found the Same Photo :)");
					continue;
				}
				
				String ownerID = photoElement.getString("owner");
				
				if(uniqueUser){
				
					if(uniqueUsers.get(ownerID) == null){
						
						uniqueUsers.put(ownerID,1);
						Photo photo = createPhotoObject(photoElement);
						photoDataList.put(photo.getPhotoId(),photo);
					}
					else if(uniqueUsers.get(ownerID) != null && uniqueUsers.get(ownerID)< max){
						
						uniqueUsers.put(ownerID, uniqueUsers.get(ownerID)+1);
						Photo photo = createPhotoObject(photoElement);
						photoDataList.put(photo.getPhotoId(),photo);
						
					}
					
					else{
						System.out.println("Flickr Same USER: "+ ownerID);
					}
				}
				else{ //No unique user constraints
					
					Photo photo = createPhotoObject(photoElement);
					photoDataList.put(photo.getPhotoId(),photo);
					
				}
				
			}
			
		} catch (JSONException e) {
		
			e.printStackTrace();
		}
		return photoDataList ;
		
	}
 
 	private Photo createPhotoObject(JSONObject photoElement) {
 		Photo photo = new Photo();
 		
		String pID;
		try {
			pID = photoElement.getString("id");
			String pTitlte = photoElement.getString("title");
			String ownerID = photoElement.getString("owner");
			
			photo.setPhotoId(pID);
			photo.setPhotoTitle(pTitlte);
			photo.setOwnerId(ownerID);
			
			//More properties of the photo (There are more flickr data to be considered)
			PhotosInterface pIF = flickr.getPhotosInterface();
	 		com.aetrion.flickr.photos.Photo flickPhoto = pIF.getPhoto(pID);
	 
	 		// Add User to Unique User Set
			this.uniqueUsers.add(flickPhoto.getOwner().getId());
	 		
	 		//Get the url of the photo. For the moment the URL for medium photo is being taken
	 		
	 		//Select the URL of the image. Prefer larger image size
	 		
	 		
	 		//TODO  check here
	 		String purl = getLagestPossibleSizeURL(pIF, flickPhoto,IMAGE_SIZE);
	 		photo.setPhotoFileUrl(purl);
	 	 		
//	 		photo.setPhotoFileUrl(flickPhoto.getMediumUrl());
	 		photo.setPhotoUrl(flickPhoto.getUrl());
	 		photo.setDateTaken(flickPhoto.getDateTaken());
	 		photo.setDescription(flickPhoto.getDescription());
	 		photo.setLatitude(flickPhoto.getGeoData().getLatitude());
	 		photo.setLongitude(flickPhoto.getGeoData().getLongitude());
	 		
			//handle photo tags
			Set<String> photoTags = getPhotoTags(pID);
			photo.setTags(photoTags);
			
			//Simple title processing
//					String title = WordNetUtil.extractNounsAsString(photo.getPhotoTitle(),true);
			
			String photoTitle = photo.getPhotoTitle();
			
			//If title is composed of comma separated sentences deal with it as a list of titles
			String titles[]  = photoTitle.split(",");
			
			for (int j = 0; j < titles.length; j++) {
				//Prepare titles
				convertTitleToTags(photo, titles[j]);	
			}
			
			
			
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return photo;
		
	}




	
 	private String getLagestPossibleSizeURL(PhotosInterface pIF,
			com.aetrion.flickr.photos.Photo flickPhoto,String preSize)
 	throws IOException, SAXException,FlickrException {
 		
 		
		String pURL;
		
		boolean large = false;
		boolean medium = false ; 
		boolean small = false ;
		
		Collection<Size> availableSizes = pIF.getSizes(flickPhoto.getId());
		
		for(Size s : availableSizes){
			if(s.getLabel() == Size.LARGE){
				large = true ;
				
			}
			if(s.getLabel() == Size.MEDIUM){
				medium = true ;
			}
			if(s.getLabel() == Size.SMALL){
				small = true ;
			}
		
		}
		if(preSize.equals("Large") && large){
			pURL = flickPhoto.getLargeUrl();
			System.out.println("URL is LAEG " +flickPhoto.getUrl() );
			return pURL;
		}
		
		else if(preSize.equals("Medium") && medium){
			pURL = flickPhoto.getMediumUrl();
			System.out.println("URL is MEDIUM " +flickPhoto.getUrl() );
			return pURL;
		}
		else if (small){
			pURL = flickPhoto.getSmallUrl();
			System.out.println("URL is SMALL " +flickPhoto.getUrl() );
			return pURL;
		}
		
		else{
		
			System.out.println("No Photo Object Can be Created");
			return null ;
		}
	
	}




	public String handleRequest(){
		
		
		String req = prepareFlickrGeoRequest() ;
		
		return handleFlickrRequest(req);
		
		
	}
 	
 	protected 	 String handleFlickrRequest(String req){
		
		
		
		StringBuffer total = new StringBuffer() ;
		
	
		URLConnection urlConn;
		try {
			urlConn = new URL(req).openConnection();
			
			DataInputStream dis = new DataInputStream(urlConn.getInputStream());
			
			String nextline = null;
			
			
			
			while ((nextline = dis.readLine()) != null) {
				
				total.append(nextline).append("\n");
			}
			
			dis.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total.toString();
		
	}
 	
 	public static	Set<String> getPhotoTags(String photoID) throws IOException, FlickrException, SAXException{

		Set<String> tagsAsStr = new HashSet<String>() ;
		
		PhotosInterface photoMain = flickr.getPhotosInterface();
		com.aetrion.flickr.photos.Photo photo = photoMain.getPhoto(photoID) ;
		
		
		if(photo.getTags().size() > 0){
		
			Collection<Tag> tags = photo.getTags();
			
			Iterator<Tag> tagIter = tags.iterator() ;
			
			while (tagIter.hasNext()) {
				
				Tag tag = (Tag) tagIter.next();
			
				String tValue = tag.getValue();
				if(!isMachineTag(tValue)){

					tValue = WordCleaner.cleanTag(tValue,singularize,wordNetCheck,googleCheck);
					if(tValue != null){
						tagsAsStr.add(tValue);
					}
					
				}
				else {
					//Handle machine tags
//					tagsAsStr.add(tValue);
					
				}
				
			}
		}
		
		return tagsAsStr ;
	}
 	
 
	public static boolean isMachineTag(String tag){
		
		return tag.matches("[\\S]*:[\\S]*=[\\S]*");
		
		
	}
	
	public static void main(String[] args) {
		
		FlickrParam params = new FlickrParam(40.687570f,-74.043313f);
		params.setPage(1);
		params.setPerPage(40);
		params.setRadius(0.1f);
		
		FlickerImageDataFetcher fFet = new FlickerImageDataFetcher(params,null);
		
		String resp = fFet.handleRequest();
		
		Map<String, Photo> photos = fFet.createPhotoObjects(resp);
		
		for(String phID : photos.keySet()){
			
			System.out.println(photos.get(phID).getPhotoTitle());
			System.out.println("User Name: "+ photos.get(phID).getOwnerId());
			System.out.println(photos.get(phID).getPhotoUrl());
			System.out.println(photos.get(phID).getTags());
			
			System.out.println(".............................");
		}
		
//		HashMap<String, Integer> x = new HashMap<String, Integer>();
//		
//		x.put("a", 1);
//		System.out.println(x);
//		x.put("b", 2);
//		System.out.println(x);
//		x.put("a", x.get("a")+100);
//		System.out.println(x);
//		x.put("a", x.get("a")+100);
//		System.out.println(x);
	}
	
	

}
