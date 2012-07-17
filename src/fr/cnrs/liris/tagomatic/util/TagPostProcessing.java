package fr.cnrs.liris.tagomatic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.xeustechnologies.googleapi.spelling.Language;

import fr.cnrs.liris.tagomatic.entities.RankingMethod;
import fr.cnrs.liris.tagomatic.entities.TagRank;
import fr.cnrs.liris.tagomatic.text.GoogleSpellcheck;

public class TagPostProcessing {

	private Set<String> blackList;

	public TagPostProcessing() {
		initBlackList();
	}

	public String googleDisambiguate(String tag) {

		String corrTag = GoogleSpellcheck.getMostProbaleSugesstionOfCorrection(
				tag, 0, Language.ENGLISH);
		return corrTag;

	}

	public boolean isInBlackList(String t) {

		for (String w : blackList) {
			if (t.equalsIgnoreCase(w))
				return true;
		}
		return false;
	}

	public void initBlackList() {

		blackList = new HashSet<String>();
		blackList.add("Copyright");
		blackList.add("my_favourite");
		blackList.add("img");
		blackList.add("photo");
		blackList.add("©_all_rights_reserved");
		blackList.add("©_All_rights_reserved");
		blackList.add("flickr");
		blackList.add("image");
		blackList.add("panoramio");
		blackList.add("geotagged");
		blackList.add("Www.Tenalps.Com");
		blackList.add("most_popular");
		blackList.add("canon");
		blackList.add("dsc");
		blackList.add("imga");
		blackList.add("mg");
		blackList.add("img");
		blackList.add("_best_of");
		blackList.add("iphone");
		blackList.add("best_of");
		blackList.add("�?нтиподы Рим");
		blackList.add("best");
		blackList.add("jpg");
		blackList.add("google");
		blackList.add("foto");
		blackList.add("picture");
		blackList.add("__mg");
		blackList.add("dscf");
		blackList.add("pictures");
		blackList.add("gt_lt");
		blackList.add("fotografia");
		blackList.add("aaatopnocontest");
		blackList.add("kivlasztottak_google_earth");
		blackList.add("january");
		blackList.add("asi");
		blackList.add("february");
		blackList.add("march");
		blackList.add("april");
		blackList.add("may");
		blackList.add("aam");
		blackList.add("june");
		blackList.add("july");
		blackList.add("august");
		blackList.add("september");
		blackList.add("october");
		blackList.add("november");
		blackList.add("december");

		blackList.add("paisajes");
		blackList.add("invec");
		blackList.add("cattedrale");

		blackList.add("parigi");
		blackList.add("rivegauche");
		blackList.add("judgement_day");
		blackList.add("impressed_beauty");
		blackList.add("reathtaking");
		blackList.add("bravo");
		blackList.add("themoulinrouge");
		blackList.add("moulin_rouge");
		blackList.add("magic_donkey");
		blackList.add("interestingness");
		blackList.add("urlaub");
		blackList.add("travel");
		blackList.add("holidaysvacanzeurlaub");
		blackList.add("infine_style");
		blackList.add("aaamybestfotos");
		blackList.add("outstandingshots");
		blackList.add("megashot");
		blackList.add("superhearts");
		blackList.add("photofaceoffwinner");
		blackList.add("betterthangood");
		blackList.add("thegardenofzen");
		blackList.add("topf25");
		blackList.add("searchthebest");
		blackList.add("ef1740mmf4lusm");
		blackList.add("breathtaking");
		blackList.add("challengeyouwinner");
		blackList.add("blueribbonwinner");
		blackList.add("25faves");
		blackList.add("platinumphoto");
		blackList.add("DSC_7470");
		blackList.add("anawesomeshot");
		blackList.add("superbmasterpiece");
		blackList.add("diamondclassphotographer");
		blackList.add("theunforgettablepictures");
		blackList.add("theunforgettablepicture");
		blackList.add("theperfectphotographer");
		blackList.add("ishflickr");
		blackList.add("xcellentphotographerawards");
		blackList.add("theperfectphotographer");
		blackList.add("superbmasterpiece");
		blackList.add("una");
		blackList.add("cnf");
		blackList.add("cnfmx");
		blackList.add("de_gdl");

		blackList.add("vez");
		blackList.add("ã");
		blackList.add("¡");
		blackList.add("í");
		blackList.add("francia");
		blackList.add("nuestra");
		blackList.add("desde");
		blackList.add("nikkor");
		blackList.add("a_toptotviews");
		blackList.add("eme");
		blackList.add("mmfd");
		blackList.add("agfa");
		blackList.add("agfa_precisa");
		blackList.add("agfa_rodinal");
		blackList.add("agfactprecisa");

		blackList.add("abigfave");
		blackList.add("nôtre");
		blackList.add("été");
		blackList.add("dri");
		blackList.add("formatfilm");
		blackList.add("obiettivo_canon");
		blackList.add("obiettivo");
		blackList.add("adobe");
		blackList.add("nm");
		blackList.add("nikon");
		blackList.add("hdr");
		blackList.add("digital");
		blackList.add("photos");
		blackList.add("digitalphotos");
		blackList.add("digitalphoto");
		blackList.add("sonyhandycam");
		blackList.add("digitaal");
		blackList.add("fotofreakske");
		blackList.add("cal");
		blackList.add("er");
		blackList.add("iso");
		blackList.add("fujifilm");
		blackList.add("iso");
		blackList.add("rg");
		blackList.add("n");
		blackList.add("explore");
		blackList.add("af");
		blackList.add("pentaxk100d");
		blackList.add("nycmar");
		blackList.add("worldwidephotographers");
		blackList.add("my_favorite_photos");
		blackList.add("flickrite");
		blackList.add("photo_selected");
		blackList.add("mfd");
		blackList.add("mfes");
		blackList.add("___");
		blackList.add("my_favourites");
		blackList.add("my favourites");
		blackList.add("my");
		blackList.add("favourites");
		blackList.add("favourite");
		blackList.add("canonrebelxti");
		blackList.add("vplc");

		blackList.add("jfk");
		blackList.add("flickr:user=poopoorama");

		blackList.add("explored");
		blackList.add("aplusphoto");
		blackList.add("tps");
		blackList.add("temporaryprotectedstatus");
		blackList.add("wwwdigitalgracecom");
		blackList.add("digitalgrace");

		blackList.add("colorphotoaward");
		blackList.add("bymarc");
		blackList.add("marcfoto");
		blackList.add("isugrandprix2007gala");
		blackList.add("lookoutexpress");
		blackList.add("katrinareyphotography");
		blackList.add("wonderfulworldmix");
		blackList.add("multiple");
		blackList.add("expiredfilm redscalefilm");
		blackList.add("many");
		blackList.add("contactforstockusage");
		blackList.add("nikon");
		blackList.add("thisimagemaybeavailableforlicensecontactformoreinfo");
		blackList.add("multiply");
		blackList.add("redscalefilmtechnique");
		blackList.add("nopeaches");
		blackList.add("igwesttangente");
		blackList.add("heinrichselig");
		blackList.add("photoessay");
		blackList.add("md");
		blackList.add("guessedberlin");
		blackList.add("photojournalist");
		blackList.add("selig");
		blackList.add("geotoolgmif");
		blackList.add("cnd");
		blackList.add("ottawaphotographer");
		blackList.add("answercoalition");
		blackList.add("able_today");
		blackList.add("answerprotest");
		blackList.add("westtangenteplus");
		blackList.add("voicedown");
		blackList.add("igwesttangenteplus");
		blackList.add("ac_lookaroundsd");
		blackList.add("leapkey");
		blackList.add("olympusxa");
		blackList.add("pics");
		blackList.add("sony");
		blackList.add("lg");
		blackList.add("sd");
		blackList.add("gt");
		blackList.add("hd");
		blackList.add("olympus");
		blackList.add("nv");
		blackList.add("rus");
		blackList.add("cooldigital");
		blackList.add("outdoor");
		blackList.add("sony");
		blackList.add("sumsung");
		blackList.add("flickrfly");
		blackList.add("flickrbestpics");
		blackList.add("qualitypixels");
		blackList.add("c_gt=ltlikebossd");
		blackList.add("c_gt");
		blackList.add("c_y_l");
		blackList.add("impressedbeauty");
		blackList.add("ccc");
		blackList.add("fc");
		blackList.add("flickrchallengegroup");
		blackList.add("friendlychallenges");
		blackList.add("ilovemypics");
		blackList.add("goldstaraward");
		blackList.add("critiquewelcome");
		blackList.add("criticismwelcome");
		blackList.add("arcticphotocom");
		blackList.add("supershot flickrsbest");
		blackList.add("rubyphotographer");
		blackList.add("aaacontestssuccess");
		blackList.add("colorfullaward");
		blackList.add("scenicsnotjustlandscapes");
		blackList.add("a_best_of_selection");
		blackList.add("a_gt=lttotviews");
		blackList.add("a_kivlasztottak_google_earth");
		blackList.add("aaa");
		blackList.add("a_gt");
		blackList.add("a_c");
		blackList.add("holiday");
		blackList.add("vacation");
		blackList.add("aaa_my_best");
		blackList.add("picture_a_day");
		blackList.add("aaa_view_gt");
		blackList.add("Favoriti");
		blackList.add("Favoritosi");
		blackList.add("abc");
		blackList.add("zz_print_scan");
		blackList.add("above_popular_pictures_npszer_kpek");
		blackList.add("=_mis_favoritas");
		blackList.add("distinguishedpictures");
		blackList.add("wetraveltheworld");
		blackList.add("thebigone");
		blackList.add("٢٠١١");
		blackList.add("·ɗȋvȋɛto_ɖȋ");
		blackList.add("adorable");
		blackList.add("favorites");
		blackList.add("photograph");
		blackList.add("attractive");
		blackList.add("dflickr");
		blackList.add("uc");
		blackList.add("aab");
		blackList.add("firstquality");
		blackList.add("supershot");
		blackList.add("golddragon");
		blackList.add("flickrdiamond");
		blackList.add("citritgroup");
		blackList.add("personal_top");
		blackList.add("goldstaraward");
		blackList.add("magicdonkeysbest");
		blackList.add("tramontonellafoschia");
		blackList.add("michiganfavorites");
		blackList.add("ultimateshot");
		blackList.add("avianexcellence");
		blackList.add("impressivemood");
		blackList.add("louisthibaudchambon");
		blackList.add("marennesplage");
		blackList.add("comingclouds");
		blackList.add(":::");
		blackList.add(":_like");
		blackList.add("like");
		blackList.add("aamy_favorite_photos");

		blackList.add("goldmedalwinner");
		blackList.add("heartawards");
		blackList.add("photographi");
		blackList.add("flickrspecial");
		blackList.add("beautifulcapture");
		blackList.add("favoloso");
		blackList.add("flickrsmileys");
		blackList.add("flickrhearts");
		blackList.add("picturepages");
		blackList.add("grouptripod");
		blackList.add("naturesfinest");
		blackList.add("travelerphotos");
		blackList.add("flickrdiamond");
		blackList.add("alemdagqualityonlyclub");
		blackList.add("tiltshift");
		blackList.add("for");
		blackList.add("the");
		blackList.add("and");
		blackList.add("what");
		blackList.add("who");
		blackList.add("or");
		blackList.add("img__");
		blackList.add("dsc__");
		blackList.add("why");
		blackList.add("and_more");
		blackList.add("and more");
		blackList.add("aa");
		blackList.add("a");
		blackList.add("aaa");
		blackList.add("aaaa");
		blackList.add("aaaaa");
		blackList.add("google_earth");
		blackList.add("google earth");
		blackList.add("canon");
		blackList.add("IMG_0219");
		blackList.add("???");
	}
	
	//Remove some bad tags and stop words from the recommendation list. Furthermore, order that tags according to their ranking score in descending order
		public Map<String, TagRank> removeStopWordsFromTagList(Map<String, TagRank> tagRecomm, boolean useGoogleDYM) {
			
			Map<String, TagRank>  tagRecommCleand = new HashMap<String, TagRank>();
			
			TagPostProcessing tagPostPrcoss = new TagPostProcessing();
			
			for(String tag : tagRecomm.keySet()){
				
				if(tag.toLowerCase().contains("img") || tag.toLowerCase().contains("dsc") || tag.contains("???") )
				
					continue;
				
				if(!tagPostPrcoss.isInBlackList(tag)){
					
					TagRank rank = tagRecomm.get(tag);
					
					if(useGoogleDYM){
						String googleTag = tagPostPrcoss.googleDisambiguate(tag);
						if(googleTag!=null){
							tagRecommCleand.put(googleTag.replace("_", " "), rank);
						}
						else{
							tagRecommCleand.put(tag.replace("_", " "), rank);
						}
					}
					else {
						tagRecommCleand.put(tag.replace("_", " "), rank);
					}
					
				}
				
			}
		
			return tagRecommCleand;
		}
		

	// The methods combine tags which are similar to each others, i.e.,
	// syntactically in one tag and sum their ranks
	public Map<String, TagRank> mergeSimilarTags(
			Map<String, TagRank> suggestedTags) {

		Map<String, TagRank> finalTagMap = new HashMap<String, TagRank>();

		Map<String, Double> tagsWithCorrespondences = new HashMap<String, Double>();

		for (String tag : suggestedTags.keySet()) {
			String newTag = tag;

			if (tagsWithCorrespondences.get(tag) != null)
				continue;
			tagsWithCorrespondences.put(tag, 0.0);

			HashMap<String, TagRank> temp = new HashMap<String, TagRank>(suggestedTags);

			TagRank newRank = new TagRank();

			for (String tempTag : temp.keySet()) {

				if (tagsWithCorrespondences.get(tempTag) != null)
					continue;

				if (tempTag.trim().toLowerCase().contains(tag.trim().toLowerCase())) {
					newTag = tempTag;
				}

							
				if (tag.trim().toLowerCase()
						.equals(tempTag.trim().toLowerCase())
						|| tag.trim().toLowerCase().contains(tempTag.trim().toLowerCase())
						|| tempTag.trim().toLowerCase().contains(tag.trim().toLowerCase())) {

					TagRank tempTagScore = temp.get(tempTag);

					newRank.setFreqRelOnlyWord(newRank.getFreqRelOnlyWord()
							+ tempTagScore.getFreqRelOnlyWord());
					newRank.setFreqRelWordAndOnlyUniqueUser(newRank
							.getFreqRelWordAndOnlyUniqueUser()
							+ tempTagScore.getFreqRelWordAndOnlyUniqueUser());
					newRank.setFreqRelWordsAndAllUsers(newRank
							.getFreqRelWordsAndAllUsers()
							+ tempTagScore.getFreqRelWordsAndAllUsers());

					newRank.setTfIdfRelOnlyWord(newRank.getTfIdfRelOnlyWord()
							+ tempTagScore.getTfIdfRelOnlyWord());
					newRank.setTfIdfRelWordAndOnlyUniqueUser(newRank
							.getTfIdfRelWordAndOnlyUniqueUser()
							+ tempTagScore.getTfIdfRelWordAndOnlyUniqueUser());
					newRank.setTfIdfRelWordsAndAllUsers(newRank
							.getTfIdfRelWordsAndAllUsers()
							+ tempTagScore.getTfIdfRelWordsAndAllUsers());

					tagsWithCorrespondences.put(tempTag, 0.0);

				}

			}
			if (newRank.getFreqRelOnlyWord() != 0) {
				newRank.setFreqRelOnlyWord(newRank.getFreqRelOnlyWord()
						+ suggestedTags.get(tag).getFreqRelOnlyWord());

				newRank.setFreqRelWordAndOnlyUniqueUser(newRank
						.getFreqRelWordAndOnlyUniqueUser()
						+ suggestedTags.get(tag)
								.getFreqRelWordAndOnlyUniqueUser());
				newRank.setFreqRelWordsAndAllUsers(newRank
						.getFreqRelWordsAndAllUsers()
						+ suggestedTags.get(tag).getFreqRelWordsAndAllUsers());

				newRank.setTfIdfRelOnlyWord(newRank.getTfIdfRelOnlyWord()
						+ suggestedTags.get(tag).getTfIdfRelOnlyWord());
				newRank.setTfIdfRelWordAndOnlyUniqueUser(newRank
						.getTfIdfRelWordAndOnlyUniqueUser()
						+ suggestedTags.get(tag)
								.getTfIdfRelWordAndOnlyUniqueUser());
				newRank.setTfIdfRelWordsAndAllUsers(newRank
						.getTfIdfRelWordsAndAllUsers()
						+ suggestedTags.get(tag).getTfIdfRelWordsAndAllUsers());
				finalTagMap.put(newTag, newRank);
			} else
				finalTagMap.put(tag, suggestedTags.get(tag));
		}

		return finalTagMap;
	}

	public Map<String, Double> orderTagList(Map<String, TagRank> tags,
			RankingMethod rm) {

		Map<String, Double> targetMap = new HashMap<String, Double>();

		if (rm == RankingMethod.freqNoUsers) {

			for (String key : tags.keySet()) {

				targetMap.put(key, tags.get(key).getFreqRelOnlyWord());
			}
		}
		if (rm == RankingMethod.freqAllUsers) {

			for (String key : tags.keySet()) {

				targetMap.put(key, tags.get(key).getFreqRelWordsAndAllUsers());
			}
		}
		if (rm == RankingMethod.freqUniqueUsers) {

			for (String key : tags.keySet()) {

				targetMap.put(key, tags.get(key).getFreqRelWordAndOnlyUniqueUser());
			}
		}
		if (rm == RankingMethod.tfidfAllUsers) {

			for (String key : tags.keySet()) {

				targetMap.put(key, tags.get(key).getTfIdfRelWordsAndAllUsers());
			}
		}
		if (rm == RankingMethod.tfidfNoUsers) {

			for (String key : tags.keySet()) {

				targetMap.put(key, tags.get(key).getTfIdfRelOnlyWord());
			}
		}
		if (rm == RankingMethod.tfidfUniqueUsers) {

			for (String key : tags.keySet()) {

				targetMap.put(key, tags.get(key)
						.getTfIdfRelWordAndOnlyUniqueUser());
			}
		}

		// Get entries and sort them.
		List<Entry<String, Double>> entries = new ArrayList<Entry<String, Double>>(
				targetMap.entrySet());

		Collections.sort(entries, new Comparator<Entry<String, Double>>() {
			public int compare(Entry<String, Double> e1,
					Entry<String, Double> e2) {

				return e2.getValue().compareTo(e1.getValue());
			}

		});

		// Put entries back in an ordered map.
		Map<String, Double> orderedMap = new LinkedHashMap<String, Double>();
		for (Entry<String, Double> entry : entries) {
			orderedMap.put(entry.getKey(), entry.getValue());
		}
		return orderedMap;
	}
	
	public Map<String, TagRank> orderTagListAccordingToTagLength(Map<String, TagRank> tags){
		// Get entries and sort them.
		List<Entry<String, TagRank>> entries = new ArrayList<Entry<String, TagRank>>(tags.entrySet());

		Collections.sort(entries, new Comparator<Entry<String, TagRank>>() {
			public int compare(Entry<String, TagRank> e1,Entry<String, TagRank> e2) {

				Integer e1Length = e1.getKey().length();
				Integer e2Length = e2.getKey().length();
				
				return e2Length.compareTo(e1Length);
			}

		});

		// Put entries back in an ordered map.
		Map<String, TagRank> orderedMap = new LinkedHashMap<String, TagRank>();
		for (Entry<String, TagRank> entry : entries) {
			orderedMap.put(entry.getKey(), entry.getValue());
		}
		return orderedMap;
	}

	public void testMergSimilar(){
		Map<String, TagRank> tags = new  TreeMap<String, TagRank>();
		
		TagRank tr = new TagRank();
		tr.setFreqRelOnlyWord(1);
		tr.setFreqRelWordAndOnlyUniqueUser(2);
		tr.setFreqRelWordsAndAllUsers(14);
		tr.setTfIdfRelOnlyWord(4);
		tr.setTfIdfRelWordAndOnlyUniqueUser(5);
		tr.setTfIdfRelWordsAndAllUsers(6);
		TagRank tr2 = new TagRank();
		tr2.setFreqRelOnlyWord(10);
		tr2.setFreqRelWordAndOnlyUniqueUser(20);
		tr2.setFreqRelWordsAndAllUsers(30);
		tr2.setTfIdfRelOnlyWord(40);
		tr2.setTfIdfRelWordAndOnlyUniqueUser(50);
		tr2.setTfIdfRelWordsAndAllUsers(60);
		
		
		tags.put("Wall", tr);
		tags.put("Big Wall", tr2);
		tags.put("Big China Wall", tr2);
		tags.put("Syria", tr);
		tags.put("Syria News", tr2);
		tags.put("Something", tr);
		TagPostProcessing tt = new TagPostProcessing();
		Map<String, TagRank> mergedTags = tt.mergeSimilarTags(tags);
		System.out.println(mergedTags);
		
		System.out.println("length order tags");
		tags = tt.orderTagListAccordingToTagLength(tags);
		for(String tag : tags.keySet()){
//			
			System.out.println(tag + "," + tags.get(tag));
//											
		}
		
		System.out.println(".................");
		Map<String, Double> orderedTags = tt.orderTagList(tags, RankingMethod.freqAllUsers);
		
		for(String tag : orderedTags.keySet()){
//			
			System.out.println(tag + "," + orderedTags.get(tag));
//											
		}
		System.out.println(".................");
		Map<String, Double> orderMergedTags = tt.orderTagList(mergedTags, RankingMethod.freqAllUsers);
		for(String tag : orderMergedTags.keySet()){
		
			System.out.println(tag + "," + orderMergedTags.get(tag));									
		}
	
		
	}
}
