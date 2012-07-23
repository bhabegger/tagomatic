package fr.cnrs.liris.tagomatic.mgr;

import java.io.File;
import java.util.HashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.objectml.xml.XMLHelper;
import org.w3c.dom.Document;

public class OnlinePhotoAnnotatorFactory implements Processor {

//	HashMap<String,String> settings = new HashMap<String, String>();
//	
//	public void setUseGoogleDYM(boolean val) {
//		settings.put("useGoogleDYM",""+val);
//	}
//	
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		
		//Get Image Path
		Message in = exchange.getIn();
		File input = in.getBody(File.class);
		
		String imagePath = input.getPath();
		
		OnlinePhotoAnnotatorFrontEnd tagRec = new OnlinePhotoAnnotatorFrontEnd(imagePath); 
		
		tagRec.generateTagRecommentations();
		
		Document output = XMLHelper.parse(tagRec.getXmlTagRecommendation());
		
		Message out = exchange.getOut();
			
		out.setBody(output);
			
	}
	

}
