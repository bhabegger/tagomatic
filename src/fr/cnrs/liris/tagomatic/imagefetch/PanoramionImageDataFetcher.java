package fr.cnrs.liris.tagomatic.imagefetch;



import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import fr.cnrs.liris.tagomatic.entities.Photo;
import fr.cnrs.liris.tagomatic.text.WordCleaner;





public class PanoramionImageDataFetcher extends ImageDataFetcher {
	
	
		
	private static final int MAX_RESULT_ITER_SIZE = 10;
	static int GEO_PHOTO_THRESHOLD = 0;
	private PanoParam params ;
	
	boolean tagsRequired = true; //By Default
	boolean techTagRequired;
	
	
	
	
	public PanoramionImageDataFetcher(PanoParam params,String inputPhotoId) {
	
		loadParameters();
		
		this.params = params;
		this.inputPhotoId = inputPhotoId;
//		Properties progParams = new Properties();
//		try {
//			progParams.load(new FileInputStream("parameters.properties"));
//			
//			GEO_PHOTO_THRESHOLD = Integer.valueOf(progParams.getProperty("GEO_PHOTO_THRESHOLD").trim());
//			
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	


	public void setTagsRequired(boolean tagsRequired) {
		this.tagsRequired = tagsRequired;
	}




	public void setTechTagRequired(boolean techTagRequired) {
		this.techTagRequired = techTagRequired;
	}




	public PanoParam getParams() {
		return params;
	}
	/**
	 * Demo harness.
	 * 
	 * <ul>
	 * <li>aArgs[0] : an HTTP URL
	 * <li>aArgs[1] : (header | content)
	 * </ul>
	 */
	public static void main(String... aArgs) throws MalformedURLException {
		
		
		
		PanoParam panoParams = new PanoParam(40.687570f,-74.043313f,0.002f);
		panoParams.setFrom(1);
		panoParams.setTo(10);
		
		PanoramionImageDataFetcher pf = new PanoramionImageDataFetcher(panoParams, null);

	
		
		
		String resp = pf.handleRequest();
		
		Map<String, Photo> photos = pf.createPhotoObjects(resp);
		
		for(String phID : photos.keySet()){
			
			System.out.println(photos.get(phID).getPhotoTitle());
			System.out.println(photos.get(phID).getTags());
			
			System.out.println(".............................");
		}
		

	}

	public PanoramionImageDataFetcher(URL aURL) {
		if (!HTTP.equals(aURL.getProtocol())) {
			throw new IllegalArgumentException("URL is not for HTTP Protocol: "
					+ aURL);
		}
		fURL = aURL;
	}


	/**
	 * Extrat non-geo tags from the photo
	 * 
	 * @return
	 */
	public static Set<String> getImageTags(String aURL) {
		
		URL fURL = null;
		
		Set<String> tags = new HashSet<String>();
		URLConnection connection = null;
		try {
			fURL = new URL(aURL);
			connection = fURL.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {

				if (line.contains("_element")) {
					reader.readLine();
					
					
					
					String tag = reader.readLine().trim().toLowerCase();
					String newTag = WordCleaner.cleanTag(tag,singularize,wordNetCheck,googleCheck);
					if(newTag != null){
						tags.add(newTag);
					}
				}
			}
		} catch (IOException ex) {
			log("Cannot open connection to " + fURL.toString());
		}
		return tags;
	}

	

	/**
	 * Extract Technical Details from the photo
	 * 
	 * @return
	 */
	public static Collection<String> getImageTechnichalTags(String aURL) {
		
		URL fURL = null;
		Collection<String> tags = new ArrayList<String>();
		URLConnection connection = null;
		try {
			fURL = new URL(aURL);
			connection = fURL.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {

				if (line.contains("Extra information")) {
					String line2;
					while (!(line2 = reader.readLine().trim())
							.contains("</ul>")) {
						if (line2.startsWith("<li>")) {
							String tag = line2.replace("<li>", "");
							tag = tag.replace("</li>", "");
							tags.add(tag);
						}
					}
				}
			}
		} catch (IOException ex) {
			log("Cannot open connection to " + fURL.toString());
		}
		return tags;
	}

	public static void getPart(String contents) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(
					contents)));

			Element tableElement = document.getElementById("photo_page_stats");
			System.out.println(tableElement);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// PRIVATE //
	private URL fURL;

	private static final String HTTP = "http";

	private static void log(Object aObject) {
		System.out.println(aObject);
	}
	
	/**
	 * Create a photo object from a JSONObject. The data are read from the JSON response given by Panoramio 
	 * and parsed to initialize a photo object
	 * @param photoElement
	 * @return
	 */
	private Photo createPanoramioPhotoObject(JSONObject photoElement, 
			boolean tagRequired, boolean techTagReq) {

		
		Photo photo = new Photo();

		try {
			//Add the owner of the photo to the unique owner list
			this.uniqueUsers.add(photoElement.getString("owner_id"));
			
			photo.setHeight(photoElement.getDouble("height"));
			photo.setLatitude(photoElement.getDouble("latitude"));
			photo.setLongitude(photoElement.getDouble("longitude"));

			photo.setOwnerId(photoElement.getString("owner_id"));
			photo.setOwnerName(photoElement.getString("owner_name"));
			photo.setOwnerUrl(photoElement.getString("owner_url"));
			photo.setPhotoFileUrl(photoElement.getString("photo_file_url"));
			photo.setPhotoId(photoElement.getString("photo_id"));
			photo.setLatitude(photoElement.getDouble("latitude"));
			photo.setLongitude(photoElement.getDouble("longitude"));
			photo.setPhotoTitle(photoElement.getString("photo_title"));

			// System.out.println(photo.getPhotoTitle());

			photo.setPhotoUrl(photoElement.getString("photo_url"));
			photo.setUploadDate(photoElement.getString("upload_date"));
			photo.setWidth(photoElement.getDouble("width"));
			if(tagRequired){
				Set<String> tags = PanoramionImageDataFetcher.getImageTags(photo.getPhotoUrl());
				photo.setTags(tags);
			
			}
			if(techTagReq){
				Collection<String> techTags = PanoramionImageDataFetcher.getImageTechnichalTags(photo.getPhotoUrl());
				photo.setTechnichalTags(techTags.toString());
			}
			//Deal with titles
//			String title = WordNetUtil.extractNounsAsString(photo.getPhotoTitle(),true);
//			if(title != null)
//				photo.getTags().add(title);
			
			
			String photoTitle = photo.getPhotoTitle();
			
			//If title is composed of comma separated sentences deal with it as a list of titles
			photoTitle = photoTitle.replace("-", ",");
			String titles[]  = photoTitle.split(",");
			for (int i = 0; i < titles.length; i++) {
				//Prepare titles
				convertTitleToTags(photo, titles[i]);
				
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return photo;
	}

	public Map<String, Photo> createPhotoObjects(String jsonResponse){
		
		return createPhotoObjects(jsonResponse,uniqueUser,maxUserOccurances);
	}
	
	/**
	 * Create photo objects from a Panoramio response and store it map indexed by photo IDs
	 * @param jsonResponse
	 * @return
	 */
	public Map<String, Photo> createPhotoObjects(String jsonResponse, boolean uniqueUser, int max){
		
		HashMap<String,Integer> uniqueUsers = new HashMap<String, Integer>();
		
		Map<String, Photo> photoList = new HashMap<String, Photo>();
		try {
			JSONObject root = new JSONObject(jsonResponse);
			

			
			JSONArray photos = root.getJSONArray("photos");

			for (int i = 0; i < photos.length(); i++) {

				JSONObject photoElement = (JSONObject) photos.get(i);
				
				//Exclude the photo if it is the same as the input photo
				if(photoElement.getString("photo_id").equals(inputPhotoId)){
					System.out.println("Found the Same Photo :)");
					continue;
				}
				
				String ownerID = photoElement.getString("owner_id");
				
				if(uniqueUser){
					

					if(uniqueUsers.get(ownerID) == null){
						
						uniqueUsers.put(ownerID,1);
						Photo photo = createPanoramioPhotoObject(photoElement,tagsRequired,techTagRequired);
						photoList.put(photo.getPhotoId(),photo);
					}
					else if(uniqueUsers.get(ownerID) != null && uniqueUsers.get(ownerID)< max){
						
						uniqueUsers.put(ownerID, uniqueUsers.get(ownerID)+1);
						Photo photo = createPanoramioPhotoObject(photoElement,tagsRequired,techTagRequired);
						photoList.put(photo.getPhotoId(),photo);
						
					}
					
					else{
						System.out.println("Same USER: "+ ownerID);
					}
				}
				else{ //No unique user constraints
					
					Photo photo = createPanoramioPhotoObject(photoElement,tagsRequired,techTagRequired);
					photoList.put(photo.getPhotoId(),photo);
					
				}

			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return photoList ;
	}
	
	public void getPanoramioGeoSimilarPhotoList(int maxCycles, boolean tagReq, boolean techTagReq,
			Map<String, Photo> photoList) {
		//Panoramio 
		//handle initial request
		String resp = handleRequest();

		int numGeoClosePhotos = getNumGeoClosePhotosFromPanoramio(resp);

		// If no geo close photos have been found return
		if (numGeoClosePhotos == GEO_PHOTO_THRESHOLD) {
			
			return ;
		}
		// Determining the number of Panoramio requests.
		int numCycle = numGeoClosePhotos / MAX_RESULT_ITER_SIZE;

		if (numCycle == 0) {
			numCycle = 1;
		} else if (numCycle > maxCycles) {
			numCycle = maxCycles;
		}
		
		// The list which will hold the retured photos
		

		// Request Panoramio for geo-close photos
		for (int i = 0; i < numCycle; i++) {

			photoList.putAll(createPhotoObjects(resp));
				
			//Avoid additional request
			if(i < numCycle-1){
				params.setFrom(params.getFrom() + 100);
				params.setTo(params.getTo() + 100);
				resp = handleRequest();
			}
		}
	}
	
	public int getNumGeoClosePhotosFromPanoramio(String resp) {
		int result = -1 ;
		JSONObject root = null;
		try {
			root = new JSONObject(resp);
			result = root.getInt("count");
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return result ; //If some error happened
	}
	
	/**
	 * Send a search request to Panoramio
	 * @param params The parameters of the request. (See http://www.panoramio.com/api/data/api.html)
	 * @return The response in JSON representation
	 */
	public  String handleRequest(){

		
		
		StringBuffer jsonResponse = new StringBuffer();
		URLConnection uc = preparePanoramioRequest();
				
		DataInputStream dis;
		try {
			dis = new DataInputStream(uc.getInputStream());
			String nextline = "";
			while ((nextline = dis.readLine()) != null) {
					jsonResponse.append(nextline).append("\n");

			}
			dis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResponse.toString();
	}
	
	/**
	 * Prepare Panoramio Seach Request
	 * @param params params The parameters of the request. (See http://www.panoramio.com/api/data/api.html)
	 * @return 
	 */
	private  URLConnection preparePanoramioRequest() {
		
		String panoramionService = "http://www.panoramio.com/map/get_panoramas.php?set="
				+ params.getSet()
				+ "&from="
				+ params.getFrom()
				+ "&to="
				+ params.getTo()
				+ "&minx="
				+ params.getMinx()
				+ "&miny="
				+ params.getMiny()
				+ "&maxx="
				+ params.getMaxx()
				+ "&maxy="
				+ params.getMaxy()
				+ "&size="
				+ params.getSize()
				+ "&mapfilter=" + params.getMapFilter();

		URLConnection uc = null;
		try {
			uc = new URL(panoramionService).openConnection();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uc;
	}
	
	/**
	 * Send a search request to Panoramio
	 * @param params The parameters of the request. (See http://www.panoramio.com/api/data/api.html)
	 * @param responseOut The file where the response will be stored.
	 * @return The response in JSON representation
	 */
	protected String handlePanoramioRequest(String responseOut){

		StringBuffer jsonResponse = new StringBuffer();
		FileWriter outFile;
		try {
			outFile = new FileWriter(responseOut);
			PrintWriter out = new PrintWriter(outFile);
			
			URLConnection uc = preparePanoramioRequest();
			DataInputStream dis = new DataInputStream(uc.getInputStream());
			String nextline = "";
			while ((nextline = dis.readLine()) != null) {
				out.println(nextline);
				jsonResponse.append(nextline).append("\n");

			}
			dis.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResponse.toString();
	}

	
	


}
