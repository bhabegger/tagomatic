package fr.cnrs.liris.tagomatic.image;

import java.io.File;

import ij.ImagePlus;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.objectml.xml.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class SurfDescriptionExtractor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		File input = in.getBody(File.class);
		
		
		ImagePlus image = new ImagePlus(input.getPath());
		Document output = XMLHelper.parse("<output />");
		Element root = output.getDocumentElement();
		root.setAttribute("height",""+image.getHeight());
		
		Message out = exchange.getOut();
		out.setBody(output);
	}

}
