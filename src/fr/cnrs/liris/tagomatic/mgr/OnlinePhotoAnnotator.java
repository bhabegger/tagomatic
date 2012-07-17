package fr.cnrs.liris.tagomatic.mgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import com.labun.surf.InterestPoint;


import de.passau.uni.fim.dimis.surf.MatchingResult;
import de.passau.uni.fim.dimis.surf.SurfMatcher;
import fr.cnrs.liris.tagomatic.entities.Photo;
import fr.cnrs.liris.tagomatic.entities.SimilarImageInfo;
import fr.cnrs.liris.tagomatic.entities.TagRank;
import fr.cnrs.liris.tagomatic.entities.WordImage;
import fr.cnrs.liris.tagomatic.entities.WordImageRelevaces;
import fr.cnrs.liris.tagomatic.imagefetch.FlickerImageDataFetcher;
import fr.cnrs.liris.tagomatic.imagefetch.FlickrParam;


import fr.cnrs.liris.tagomatic.imagefetch.PanoParam;
import fr.cnrs.liris.tagomatic.imagefetch.PanoramionImageDataFetcher;
import fr.cnrs.liris.tagomatic.text.WordCleaner;
import fr.cnrs.liris.tagomatic.util.ImageUtil;

public class OnlinePhotoAnnotator {

	private static final String MAX_PHOTOS_FOR_ITER = null;
	private String inputImageURL;
	private float lt;
	private float ln;
	private String imageName;

	private Map<String, Double> topImagesURLs; // List of matching images urls
	private Map<String, String> topImagesNames;
	private Map<String, Photo> photoList; // A hash table of all nearby photos
											// indexed by photo ID
	private Map<String, Integer> wordNotSimilarImages = new HashMap<String, Integer>();

	// Directory Parameters
	private String DEFAULT_DIR;
	private String DEFAULT_PATH;
	private String similarImagesDir;
	private String dissimilarImagesDir;
	private String matchingResultUrl;
	private String irrResultUrl;
	private String statistics;
	private String word_prop_output;
	private String relevanceFile;
	private PrintWriter outRel;
	private PrintWriter outIrrRel;

	
	// Flickr and Panoramio Parameters
	private PanoramionImageDataFetcher pFet;
	private FlickerImageDataFetcher fFet;
	private int totalNumberOfImages; // The total number of images found in the
										// same geographical area as the input
										// image
	private int totalNumberOfSimilarImages; // The total number of visual
											// correspondences to the input
											// image

	//This parameters are set by the property file: parameters.properties
	private static float FLICKR_DEFAULT_RANGE;
	private static int FLICKR_DEFAULT_PAGE ;
	private static int FLICKR_DEFAULT_IMAG_NUMBER ;

	private static int PANORAMIO_DEFAULT_FROM ;
	private static int PANORAMIO_DEFAULT_TO ;
	private static int PANORAMIO_DEFUALT_NUM_ITERATIONS ;
	public static float PANORAMIO_RANGE ;
	
	// Visual Matching Parameters
	private static int MIN_COMMON_IP_COUNT ;

	public OnlinePhotoAnnotator(String inputImageUrl, float lt, float ln,
			boolean compact) {
		
		loadParameters();

		if (compact)
			initCompact(inputImageUrl, lt, ln);
		else
			init(inputImageUrl, lt, ln);

		FlickrParam flickrParams = new FlickrParam(lt, ln,
				FLICKR_DEFAULT_RANGE, FLICKR_DEFAULT_PAGE,
				FLICKR_DEFAULT_IMAG_NUMBER);
		fFet = new FlickerImageDataFetcher(flickrParams, this.imageName);
		PanoParam panoParams = new PanoParam(lt, ln,PANORAMIO_RANGE);
		
		panoParams.setFrom(PANORAMIO_DEFAULT_FROM);
		panoParams.setTo(PANORAMIO_DEFAULT_TO);
		pFet = new PanoramionImageDataFetcher(panoParams, this.imageName);

	}

	private void loadParameters(){
		
		Properties properties = new Properties();
		
		try {
			properties.load(new FileInputStream("parameters.properties"));
			FLICKR_DEFAULT_RANGE = Float.valueOf(properties.getProperty("FLICKR_DEFAULT_RANGE").trim());
			FLICKR_DEFAULT_PAGE = Integer.valueOf(properties.getProperty("FLICKR_DEFAULT_PAGE").trim());
			FLICKR_DEFAULT_IMAG_NUMBER = Integer.valueOf(properties.getProperty("FLICKR_DEFAULT_IMAG_NUMBER").trim());
			
			PANORAMIO_DEFAULT_FROM = Integer.valueOf(properties.getProperty("PANORAMIO_DEFAULT_FROM").trim());
			PANORAMIO_DEFAULT_TO = Integer.valueOf(properties.getProperty("PANORAMIO_DEFAULT_TO").trim());
			PANORAMIO_DEFUALT_NUM_ITERATIONS =Integer.valueOf(properties.getProperty("PANORAMIO_DEFUALT_NUM_ITERATIONS").trim());
			
			MIN_COMMON_IP_COUNT = Integer.valueOf(properties.getProperty("MIN_COMMON_IP_COUNT").trim());

			PANORAMIO_RANGE = Float.valueOf(properties.getProperty("PANORAMIO_RANGE").trim());
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initCompact(String inputImageUrl2, float lt2, float ln2) {

		inputImageURL = inputImageUrl2;
		lt = lt2;
		ln = ln2;
		imageName = inputImageURL.substring(inputImageURL.lastIndexOf('/') + 1,
				inputImageURL.length() - 4);

	}

	private void init(String inputImageUrl, float lat, float lon) {

		inputImageURL = inputImageUrl;

		lt = lat;
		ln = lon;
		imageName = inputImageURL.substring(inputImageURL.lastIndexOf('/') + 1,
				inputImageURL.length() - 4);

		DEFAULT_DIR = imageName + "_" + lt + "_" + ln;
		String base = DEFAULT_PATH + DEFAULT_DIR;

		new File(base).mkdir();
		// Directory of the downloaded images
		similarImagesDir = base + "/photos";
		new File(similarImagesDir).mkdir();

		dissimilarImagesDir = base + "/notSimPhotos";
		new File(dissimilarImagesDir).mkdir();

		// Matching Result Dir
		new File(base + "/result").mkdir();
		// result file name
		matchingResultUrl = base + "/result" + "/relevantResult.csv";
		irrResultUrl = base + "/result" + "/irrelevantResult.csv";
		statistics = base + "/result" + "/tagStatistics.csv";
		word_prop_output = base + "/result" + "/pw.csv";
		relevanceFile = base + "/result" + "/relevance.csv";

		// Copy Input Image to the Result folder
		ImageUtil.copyImage(inputImageUrl, base + "/" + imageName + ".jpg", "jpg");

	}

	/**
	 * The heart of the total program. The method which find geo and LLF similar
	 * photos and extract tag statistics
	 * 
	 * @param allCandidateWords
	 * @param relevantWordImages
	 * @param maxCycles
	 * @param tagReq
	 * @param techTagReq
	 */
	public void findGeoSurfSimilarPhotosWithTagStatistics(
			final Set<String> allCandidateWords,
			final Map<String, WordImage> relevantWordImages, boolean tagReq,
			boolean techTagReq, boolean compact) {

		topImagesURLs = new HashMap<String, Double>();
		topImagesNames = new HashMap<String, String>();
		// The list of all Photo objects
		photoList = new HashMap<String, Photo>();
		photoList = createGeoSimilarPhotoList(PANORAMIO_DEFUALT_NUM_ITERATIONS, tagReq,
				techTagReq);

		// Word Image Relevance Dictionary
		// The statistics consider for each word the set of images annotated
		// with them and similar
		// to the input image and the those which are tagged with it and not
		// similar to the input image
		// Map<String, WordImage> relevantWordImages = new HashMap<String,
		// WordImage>();

		// Prepare the result output

		if (!compact) {
			outRel.println("Image URL , Score , Distance , Point Similarity, Common Keypoints , Iter Point Similariy, Title , Tags , distance, userid");
			// An CSV file for the set of visually irrelevant images
			outIrrRel.println("Image URL  , Title , Tags , distance,  userid");
		}

		// *** Extract SURF feature for the input image

		SurfMatcher surfMatcher = new SurfMatcher();
		// The SURF feature of the input image
		List<InterestPoint> ipts1 = surfMatcher.extractKeypoints(inputImageURL,
				surfMatcher.p1);

		// inputImageInterstPoints = ipts1 ;

		// Add the photo with its interestpoint to the cache
		// this.photoInterestPoints.put(imageName, ipts1);

		// Find SURF correspondences in the geo-related images
		totalNumberOfSimilarImages = 0; // R

		for (String photoId : photoList.keySet()) {

			Photo photo = photoList.get(photoId);

	

			String toMatchedPhotoURL = photo.getPhotoFileUrl();
			// The SURF feature of a geo close image
			List<InterestPoint> ipts2 = surfMatcher.extractKeypoints(
					toMatchedPhotoURL, surfMatcher.p2);

			// this.photoInterestPoints.put(photo.getPhotoId(), ipts2);
			// this.cachedImageInterstPoint.put(photo.getPhotoId(), ipts2);

			MatchingResult surfResult = null;

			surfResult = surfMatcher.matchKeypoints(inputImageURL, photoId,
					ipts1, ipts2, MIN_COMMON_IP_COUNT);

			if (surfResult != null) { // the images are visually similar

				topImagesURLs.put(toMatchedPhotoURL,
						surfResult.getPiontSimilarity());

				topImagesNames.put(toMatchedPhotoURL, photo.getPhotoId());

				totalNumberOfSimilarImages += 1;
				if (!compact) {

					outRel.println(photo.getPhotoUrl()
							+ ","
							+ surfResult.getScore()
							+ ","
							+ surfResult.getAvgDistance()
							+ ","
							+ surfResult.getPiontSimilarity()
							+ ","
							+ surfResult.getCommonKeyPointsCount()
							+ ","
							+ "null ,"
							+ photo.getPhotoTitle().toString()
									.replace(",", " ") + ","
							+ photo.getTags().toString().replace(",", " ; ")
							);

					System.out.println("Downloading Similar Image");
					String destFileName = similarImagesDir + "/" + photoId
							+ ".jpg";
					ImageUtil.downloadImage(photo.getPhotoFileUrl(), destFileName);

				}

				// Generate Word Statistics
				// For the a certain word (tag) add image info to its list if
				// this image
				// is visually similar to the input image
				updateWordRelatedImageList(allCandidateWords,
						relevantWordImages, photo, surfResult);
			} else {

				if (!compact) {
					// Images are visually not similar
					outIrrRel.println(photo.getPhotoUrl()
							+ " ,"
							+ photo.getPhotoTitle().toString()
									.replace(",", " ") + ","
							+ photo.getTags().toString().replace(",", " ; ")
							);
					// We also need to get some information if a certain word is
					// also used by visually not similar images
					System.out.println("Downloading Non Similar Image");
					String destFileName = dissimilarImagesDir + "/" + photoId
							+ ".jpg";
					ImageUtil.downloadImage(photo.getPhotoFileUrl(), destFileName);

				}

				updateWordNotRelatedImageList(relevantWordImages, photo);

			}
		}
	}

	/**
	 * Create a list of Photo objects from Panoramio response. Since Panoramio
	 * retrieves at most 100 photo per a page of a JSON response, it is possible
	 * to acquire the remaining photos by re-issuing the same request to
	 * retrieve photos from the other pages.
	 * 
	 * @param maxCycles
	 *            Number define the number of pages that should be processed ( a
	 *            page has at most 100 elements)
	 * @param tagReq
	 *            Set to true if you want to retrieve photo tags
	 * @param techTagReq
	 *            Set to true if you want to retrieve photo technichal tags.
	 * @return List of Photo object indexed by photo id.
	 * 
	 * 
	 */
	public Map<String, Photo> createGeoSimilarPhotoList(int maxCycles,
			boolean tagReq, boolean techTagReq) {

		Map<String, Photo> photoList = new HashMap<String, Photo>();

		pFet.getPanoramioGeoSimilarPhotoList(maxCycles, tagReq, techTagReq,
				photoList);

		int panoPhotoReturend = photoList.size();

		Map<String, Photo> flickrPhotoList = fFet
				.getFlickrGeoSimilarPhotoList();

		int flickrPhotoReturend = flickrPhotoList.size();

		photoList.putAll(flickrPhotoList);

		totalNumberOfImages = flickrPhotoReturend + panoPhotoReturend;

		return photoList;
	}

	private static void updateWordRelatedImageList(
			final Collection<String> allCandidateWords,
			Map<String, WordImage> relevantWordImages, Photo photo,
			MatchingResult surfResult) {

		Collection<String> candidateWords = photo.getTags();

		for (String word : candidateWords) {

			// TODO Consider the title of the image
			allCandidateWords.add(word);
			updateRelatedImageList(relevantWordImages, photo, surfResult, word);
		}
	}

	private static void updateRelatedImageList(
			Map<String, WordImage> relevantWordImages, Photo photo,
			MatchingResult surfResult, String word) {

		// Not necessarily the same word may a similar one
		// Use the compareTag() method

		String key = getRelevantKeyForRelevantImages(relevantWordImages, word);

		if (key != null) {

			SimilarImageInfo info = new SimilarImageInfo(photo.getPhotoId(),
					surfResult.getScore(), surfResult.getPiontSimilarity(),
					surfResult.getCommonIPCount());

			relevantWordImages.get(key).getSimlarImages().add(info);

			relevantWordImages.get(key).getSimlarImagesMap()
					.put(info.getId(), info);

			relevantWordImages.get(key).addSimilarImageId(info.getId());

		}

		else {

			WordImage wi = new WordImage(word);
			SimilarImageInfo info = new SimilarImageInfo(photo.getPhotoId(),
					surfResult.getScore(), surfResult.getPiontSimilarity(),
					surfResult.getCommonIPCount());

			wi.addSimilarImage(info);

			wi.addSimilarImage(photo.getPhotoId(), info);

			relevantWordImages.put(word, wi);

		}
	}

	private static String getRelevantKeyForRelevantImages(
			Map<String, WordImage> relevantWordImages, String word) {

		Set<String> keySet = relevantWordImages.keySet();

		// TODO favor longer words :) Also set the parameter in good way
		for (String key : keySet) {

			if (WordCleaner.compareTags(key, word, "_", 1)) {

				return key;
			}

		}

		return null;
	}

	private void updateWordNotRelatedImageList(
			Map<String, WordImage> relevantWordImages, Photo photo) {

		Collection<String> candidateWords = photo.getTags();

		for (String word : candidateWords) {
			word = word.toLowerCase();

			// updateNotRelatedImageList(relevantWordImages, photo, word);

			if (wordNotSimilarImages.get(word) != null) {
				wordNotSimilarImages.put(word,
						wordNotSimilarImages.get(word) + 1);
			} else {
				wordNotSimilarImages.put(word, 1);
			}
		}
	}

	// Methods for calculating word ranks
	public void performNewCalculations(Map<String, WordImage> wordImageRel,
			String outResult) {

		wordImageRel = calculateWordsAndImagePropability(wordImageRel,
				this.fFet.getuniqueUsers().size()
						+ this.pFet.getuniqueUsers().size());
		Map<String, WordImageRelevaces> result = calculateImageWordRelevance(wordImageRel);

		try {
			PrintWriter outStat = new PrintWriter(outResult);
			outStat.println("Tag , " + "Relevance Frq Words ,"
					+ "Relevance Frq Words All Users ,"
					+ "Relevance Frq Word Unique Users,"
					+ "Relevance TFIDF Words,"
					+ "Relevance TFIDF Words All Users,"
					+ "Relevance TFIDF Words Unique Users," + "Occurances , "
					+ "Similar Images, " + "Non Similar Images, "
					+ "#unique users, " + "#total user, " + "user factor, "
					+ "unique users");

			for (String word : result.keySet()) {

				outStat.print(word + " , ");
				outStat.print(result.get(word).getFreqRelOnlyWords() + " , ");
				outStat.print(result.get(word).getFreqRelWordsAndAllUsers()
						+ " , ");
				outStat.print(result.get(word)
						.getFreqRelWordAndOnlyUniqueUsers() + " , ");
				outStat.print(result.get(word).getTfIdfRelOnlyWords() + " , ");
				outStat.print(result.get(word).getTfIdfRelWordsAndAllUsers()
						+ " , ");
				outStat.print(result.get(word)
						.getTfIdfRelWordAndOnlyUniqueUsers() + ",");

				// To check the results
				WordImage wordImg = wordImageRel.get(word);

				outStat.print(wordImg.getOcurrances() + " , ");
				outStat.print(wordImg.getSimlarImages().toString()
						.replace(",", " : ")
						+ " , ");
				outStat.print(wordImg.getNotSimlarImages().toString()
						.replace(",", " : ")
						+ " , ");

				Set<String> uniqueTagUsers = wordImg
						.getWordUniqueUsers(this.photoList);
				wordImg.setUniqueUserCount(uniqueTagUsers.size());

				int totalUserCount = this.fFet.getuniqueUsers().size()
						+ this.pFet.getuniqueUsers().size();

				outStat.print(" , " + uniqueTagUsers.size());
				outStat.print(" , " + totalUserCount);
				outStat.print(" , " + (double) uniqueTagUsers.size()
						/ (double) totalUserCount);
				outStat.println(" , "
						+ uniqueTagUsers.toString().replace(",", " : "));

			}
			outStat.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Provides ranked tag recommendations
	 * 
	 * @param wordImageRel
	 * @return
	 */
	public Map<String, TagRank> recommedTagsComapct(Map<String, WordImage> wordImageRel) {

		Map<String, TagRank> tagScore = new HashMap<String, TagRank>();

		wordImageRel = calculateWordsAndImagePropability(wordImageRel,
				this.fFet.getuniqueUsers().size()
						+ this.pFet.getuniqueUsers().size());
		Map<String, WordImageRelevaces> result = calculateImageWordRelevanceCompact(wordImageRel);

		for (String word : result.keySet()) {
			
			TagRank  tagRank = new TagRank() ;
			tagRank.setFreqRelOnlyWord(result.get(word).getFreqRelOnlyWords());
			tagRank.setFreqRelWordAndOnlyUniqueUser(result.get(word).getFreqRelWordAndOnlyUniqueUsers());
			tagRank.setFreqRelWordsAndAllUsers(result.get(word).getFreqRelWordsAndAllUsers());
			tagRank.setTfIdfRelOnlyWord(result.get(word).getTfIdfRelOnlyWords());
			tagRank.setTfIdfRelWordAndOnlyUniqueUser(result.get(word).getTfIdfRelWordAndOnlyUniqueUsers());
			tagRank.setTfIdfRelWordsAndAllUsers(result.get(word).getTfIdfRelWordsAndAllUsers());

			tagScore.put(word, tagRank);

		}
		return tagScore;
	}

	/**
	 * Calculate the probability of a word
	 * 
	 * @param wordImageRel
	 * @return
	 */

	public Map<String, WordImage> calculateWordsAndImagePropability(
			Map<String, WordImage> wordImageRel, int totalUniqueUserCount) {

		int totalOcurrance = 0;

		// Calculate the total number of word occurrences in the set of similar
		// and dissimilar images

		for (String word : wordImageRel.keySet()) { // For each word

			int wordOccur = wordImageRel.get(word).getSimlarImages().size();

			if (this.wordNotSimilarImages.get(word) != null)
				wordOccur += this.wordNotSimilarImages.get(word);

			wordImageRel.get(word).setOcurrances(wordOccur);

			totalOcurrance += wordOccur;
		}

		for (String word : wordImageRel.keySet()) { // For each word

			int wordOccur = wordImageRel.get(word).getOcurrances();

			// wordImageRel.get(word).setOcurrances(wordOccur);

			int uniqueUsers = wordImageRel.get(word)
					.getWordUniqueUsers(this.photoList).size();

			wordImageRel.get(word).setTotalOcurances(totalOcurrance);

			// .......... Voting-similar word probability
			// .......................

			// 1. Word probability without user assumptions
			double PwOnlyWords = ((double) wordOccur / (double) totalOcurrance);
			wordImageRel.get(word).setPwOnlyWords(PwOnlyWords);

			// 2. Word probability with user proportion to the total number of
			// users
			double PwAllUsers = PwOnlyWords
					* ((double) uniqueUsers / (double) totalUniqueUserCount);
			wordImageRel.get(word).setPwAllUsers(PwAllUsers);

			// 3. Word probability. Word freq is represented by the number of
			// unique users only
			double PwOnlyUniqueUsers = ((double) uniqueUsers / (double) totalOcurrance);
			wordImageRel.get(word).setPwOnlyUniqueUsers(PwOnlyUniqueUsers);

			// ..........TF-IDF Word Probability Calculation
			// .......................
			int C = totalNumberOfImages;
			int R = totalNumberOfSimilarImages;
			int Rw = wordImageRel.get(word).getSimlarImages().size();
			int Lw = wordImageRel.get(word).getOcurrances();

			// 1. Word probability TF.IDF approach without user assumptions
			double PwTFIDFlOnlyWords = ((double) Rw / R)
					* (Math.log((double) C / Lw) / Math.log(2))
					/ (Math.log((double) C) / Math.log(2));
			wordImageRel.get(word).setPwTFIDFlOnlyWords(PwTFIDFlOnlyWords);

			// 2. Word probability TF.IDF approach with user proportion to the
			// totall number of users
			double PwTFIDFlAllUsers = PwTFIDFlOnlyWords
					* ((double) uniqueUsers / (double) totalUniqueUserCount);
			wordImageRel.get(word).setPwTFIDFlAllUsers(PwTFIDFlAllUsers);

			// 3. Word probability TF.IDF approach. Word freq is represented by
			// the number of unique users only
			double PwTFIDFOnlyUniqueUsers = PwTFIDFlOnlyWords
					* (uniqueUsers / (double) wordOccur);
			wordImageRel.get(word).setPwTFIDFOnlyUniqueUsers(
					PwTFIDFOnlyUniqueUsers);

			wordImageRel.get(word)
					.setImageProbability(1.0 / (double) wordOccur); // P(c_j|w)

		}
		return wordImageRel;

	}

	// Calculate according to the new measure
	public Map<String, WordImageRelevaces> calculateImageWordRelevance(
			Map<String, WordImage> wordImageRel) {

		PrintWriter tempOut;
		Map<String, WordImageRelevaces> wordImgRelevance = new HashMap<String, WordImageRelevaces>();
		try {

			tempOut = new PrintWriter(word_prop_output);

			double IuWTotal1 = 0; // The normalization factor
			double IuWTotal2 = 0; // The normalization factor
			double IuWTotal3 = 0; // The normalization factor
			double IuWTotal4 = 0; // The normalization factor
			double IuWTotal5 = 0; // The normalization factor
			double IuWTotal6 = 0; // The normalization factor

			int index = 0;

			double IuWSingle1[] = new double[wordImageRel.size()];
			double IuWSingle2[] = new double[wordImageRel.size()];
			double IuWSingle3[] = new double[wordImageRel.size()];
			double IuWSingle4[] = new double[wordImageRel.size()];
			double IuWSingle5[] = new double[wordImageRel.size()];
			double IuWSingle6[] = new double[wordImageRel.size()];

			tempOut.println("Word , total Occur , #NotSimImgs, #SimImgs , WordOccur , #Unique Users "
					+ ", Pw1, Pw2,Pw3 , Pw4 , Pw5,Pw6 , C, R, Rw, Lw");

			for (String word : wordImageRel.keySet()) { // For each word

				WordImage wordImage = wordImageRel.get(word);
				double PcjW = wordImage.getImageProbability();

				double Pw1 = wordImage.getPwOnlyWords();
				double Pw2 = wordImage.getPwAllUsers();
				double Pw3 = wordImage.getPwOnlyUniqueUsers();
				double Pw4 = wordImage.getPwTFIDFlOnlyWords();
				double Pw5 = wordImage.getPwTFIDFlAllUsers();
				double Pw6 = wordImage.getPwTFIDFOnlyUniqueUsers();

				int C = totalNumberOfImages;
				int R = totalNumberOfSimilarImages;
				int Rw = wordImageRel.get(word).getSimlarImages().size();
				int Lw = wordImageRel.get(word).getOcurrances();

				tempOut.println(word
						+ ","
						+ wordImageRel.get(word).getTotalOcurences()
						+ ","
						+ wordNotSimilarImages.get(word)
						+ ","
						+ wordImageRel.get(word).getSimilarImageIDs().size()
						+ ","
						+ wordImageRel.get(word).getOcurrances()
						+ ","
						+ wordImageRel.get(word)
								.getWordUniqueUsers(this.photoList).size()
						+ "," + +Pw1 + "," + Pw2 + "," + Pw3 + "," + Pw4 + ","
						+ Pw5 + "," + Pw6 + "," + C + "," + R + "," + Rw + ","
						+ Lw);

				Set<String> imgIDs = wordImage.getSimilarImageIDs(); // Get
																		// images
																		// related
																		// to
																		// that
																		// word

				for (String imgID : imgIDs) {

					double PIc = wordImage.getSimilarImage(imgID)
							.getPointSimilarity();

					IuWSingle1[index] += Pw1 * PcjW * PIc;
					IuWSingle2[index] += Pw2 * PcjW * PIc;
					IuWSingle3[index] += Pw3 * PcjW * PIc;
					IuWSingle4[index] += Pw4 * PcjW * PIc;
					IuWSingle5[index] += Pw5 * PcjW * PIc;
					IuWSingle6[index] += Pw6 * PcjW * PIc;

				}

				// Normalise
				WordImageRelevaces wImgRels = new WordImageRelevaces();
				wImgRels.setFreqRelOnlyWords(IuWSingle1[index]);
				wImgRels.setFreqRelWordsAndAllUsers(IuWSingle2[index]);
				wImgRels.setFreqRelWordAndOnlyUniqueUsers(IuWSingle3[index]);
				wImgRels.setTfIdfRelOnlyWords(IuWSingle4[index]);
				wImgRels.setTfIdfRelWordsAndAllUsers(IuWSingle5[index]);
				wImgRels.setTfIdfRelWordAndOnlyUniqueUsers(IuWSingle6[index]);

				wordImgRelevance.put(word, wImgRels); // unnormalized value

				IuWTotal1 += IuWSingle1[index];
				IuWTotal2 += IuWSingle2[index];
				IuWTotal3 += IuWSingle3[index];
				IuWTotal4 += IuWSingle4[index];
				IuWTotal5 += IuWSingle5[index];
				IuWTotal6 += IuWSingle6[index];

				index++;
			}

			for (String word : wordImgRelevance.keySet()) {

				double notNormalizedValue1 = wordImgRelevance.get(word)
						.getFreqRelOnlyWords();
				wordImgRelevance.get(word).setFreqRelOnlyWords(
						notNormalizedValue1 / IuWTotal1);

				double notNormalizedValue2 = wordImgRelevance.get(word)
						.getFreqRelWordsAndAllUsers();
				wordImgRelevance.get(word).setFreqRelWordsAndAllUsers(
						notNormalizedValue2 / IuWTotal2);

				double notNormalizedValue3 = wordImgRelevance.get(word)
						.getFreqRelWordAndOnlyUniqueUsers();
				wordImgRelevance.get(word).setFreqRelWordAndOnlyUniqueUsers(
						notNormalizedValue3 / IuWTotal3);

				double notNormalizedValue4 = wordImgRelevance.get(word)
						.getTfIdfRelOnlyWords();
				wordImgRelevance.get(word).setTfIdfRelOnlyWords(
						notNormalizedValue4 / IuWTotal4);

				double notNormalizedValue5 = wordImgRelevance.get(word)
						.getTfIdfRelWordsAndAllUsers();
				wordImgRelevance.get(word).setTfIdfRelWordsAndAllUsers(
						notNormalizedValue5 / IuWTotal5);

				double notNormalizedValue6 = wordImgRelevance.get(word)
						.getTfIdfRelWordAndOnlyUniqueUsers();
				wordImgRelevance.get(word).setTfIdfRelWordAndOnlyUniqueUsers(
						notNormalizedValue6 / IuWTotal6);

				tempOut.close();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return wordImgRelevance;

	}

	// Calculate according to the new measure
	public Map<String, WordImageRelevaces> calculateImageWordRelevanceCompact(
			Map<String, WordImage> wordImageRel) {

		Map<String, WordImageRelevaces> wordImgRelevance = new HashMap<String, WordImageRelevaces>();

		double IuWTotal1 = 0; // The normalization factor
		double IuWTotal2 = 0; // The normalization factor
		double IuWTotal3 = 0; // The normalization factor
		double IuWTotal4 = 0; // The normalization factor
		double IuWTotal5 = 0; // The normalization factor
		double IuWTotal6 = 0; // The normalization factor

		int index = 0;

		double IuWSingle1[] = new double[wordImageRel.size()];
		double IuWSingle2[] = new double[wordImageRel.size()];
		double IuWSingle3[] = new double[wordImageRel.size()];
		double IuWSingle4[] = new double[wordImageRel.size()];
		double IuWSingle5[] = new double[wordImageRel.size()];
		double IuWSingle6[] = new double[wordImageRel.size()];

		for (String word : wordImageRel.keySet()) { // For each word

			WordImage wordImage = wordImageRel.get(word);
			double PcjW = wordImage.getImageProbability();

			double Pw1 = wordImage.getPwOnlyWords();
			double Pw2 = wordImage.getPwAllUsers();
			double Pw3 = wordImage.getPwOnlyUniqueUsers();
			double Pw4 = wordImage.getPwTFIDFlOnlyWords();
			double Pw5 = wordImage.getPwTFIDFlAllUsers();
			double Pw6 = wordImage.getPwTFIDFOnlyUniqueUsers();

			int C = totalNumberOfImages;
			int R = totalNumberOfSimilarImages;
			int Rw = wordImageRel.get(word).getSimlarImages().size();
			int Lw = wordImageRel.get(word).getOcurrances();

			Set<String> imgIDs = wordImage.getSimilarImageIDs(); // Get images
																	// related
																	// to that
																	// word

			for (String imgID : imgIDs) {

				double PIc = wordImage.getSimilarImage(imgID)
						.getPointSimilarity();

				IuWSingle1[index] += Pw1 * PcjW * PIc;
				IuWSingle2[index] += Pw2 * PcjW * PIc;
				IuWSingle3[index] += Pw3 * PcjW * PIc;
				IuWSingle4[index] += Pw4 * PcjW * PIc;
				IuWSingle5[index] += Pw5 * PcjW * PIc;
				IuWSingle6[index] += Pw6 * PcjW * PIc;

			}

			// Normalise
			WordImageRelevaces wImgRels = new WordImageRelevaces();
			wImgRels.setFreqRelOnlyWords(IuWSingle1[index]);
			wImgRels.setFreqRelWordsAndAllUsers(IuWSingle2[index]);
			wImgRels.setFreqRelWordAndOnlyUniqueUsers(IuWSingle3[index]);
			wImgRels.setTfIdfRelOnlyWords(IuWSingle4[index]);
			wImgRels.setTfIdfRelWordsAndAllUsers(IuWSingle5[index]);
			wImgRels.setTfIdfRelWordAndOnlyUniqueUsers(IuWSingle6[index]);

			wordImgRelevance.put(word, wImgRels); // unnormalized value

			IuWTotal1 += IuWSingle1[index];
			IuWTotal2 += IuWSingle2[index];
			IuWTotal3 += IuWSingle3[index];
			IuWTotal4 += IuWSingle4[index];
			IuWTotal5 += IuWSingle5[index];
			IuWTotal6 += IuWSingle6[index];

			index++;
		}

		for (String word : wordImgRelevance.keySet()) {

			double notNormalizedValue1 = wordImgRelevance.get(word)
					.getFreqRelOnlyWords();
			wordImgRelevance.get(word).setFreqRelOnlyWords(
					notNormalizedValue1 / IuWTotal1);

			double notNormalizedValue2 = wordImgRelevance.get(word)
					.getFreqRelWordsAndAllUsers();
			wordImgRelevance.get(word).setFreqRelWordsAndAllUsers(
					notNormalizedValue2 / IuWTotal2);

			double notNormalizedValue3 = wordImgRelevance.get(word)
					.getFreqRelWordAndOnlyUniqueUsers();
			wordImgRelevance.get(word).setFreqRelWordAndOnlyUniqueUsers(
					notNormalizedValue3 / IuWTotal3);

			double notNormalizedValue4 = wordImgRelevance.get(word)
					.getTfIdfRelOnlyWords();
			wordImgRelevance.get(word).setTfIdfRelOnlyWords(
					notNormalizedValue4 / IuWTotal4);

			double notNormalizedValue5 = wordImgRelevance.get(word)
					.getTfIdfRelWordsAndAllUsers();
			wordImgRelevance.get(word).setTfIdfRelWordsAndAllUsers(
					notNormalizedValue5 / IuWTotal5);

			double notNormalizedValue6 = wordImgRelevance.get(word)
					.getTfIdfRelWordAndOnlyUniqueUsers();
			wordImgRelevance.get(word).setTfIdfRelWordAndOnlyUniqueUsers(
					notNormalizedValue6 / IuWTotal6);

		}
		return wordImgRelevance;

	}

	// Close the files
	public void shutdown() {
		outRel.close();
		outIrrRel.close();
	}

}
