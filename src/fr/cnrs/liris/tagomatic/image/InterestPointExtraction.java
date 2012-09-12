package fr.cnrs.liris.tagomatic.image;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;


public class InterestPointExtraction implements Processor{


	/*
	 * INPUT MESSAGE:
	 *   An InputStream with the image data
	 *   
	 * OUTPUT MESSAGE
	 *   An OutputStream (serialization of the surf descriptor)
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		Message out = exchange.getOut();
		
		InputStream is = in.getBody(InputStream.class);
		OutputStream os = null;
		
		
		// TODO Implement surf description extraction
		
		out.setHeaders(in.getHeaders());
		out.setBody(os);
		
	}

}
