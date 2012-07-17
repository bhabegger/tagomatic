package fr.cnrs.liris.tagomatic.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageUtil {

	public static void downloadImage(String urlStr, String destFileName) {

		URL url;
		try {
			url = new URL(urlStr);
			BufferedImage image = ImageIO.read(url);
			
			ImageIO.write(image, "png", new FileOutputStream(destFileName));
		} 
//		catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
		catch (Throwable e) {
			System.out.println("Image Cannot be downloaded " + urlStr);
		}
	}
	
	public static void copyImage(String urlStr, String destFileName,String type) {

	
		try {
		
			BufferedImage image = ImageIO.read(new FileInputStream(urlStr));
			
			ImageIO.write(image, type, new FileOutputStream(destFileName));
		} 

		catch (Throwable e) {
			System.out.println("Image Cannot be downloaded " + urlStr);
		}
	}
	
	
	public static void copyBatch(String sourceDir, String type) {

		String[] srcImges = new File(sourceDir).list();
		for (int i = 0; i < srcImges.length; i++) {
			String destImg = sourceDir+srcImges[i].substring(0,srcImges[i].lastIndexOf(".")+1)+ type;
			copyImage(sourceDir+srcImges[i],destImg,type);
		}
		
	}
	public static void downloadImages(String inputFile, String outDir) throws IOException {
		
		FileInputStream fstream = new FileInputStream(inputFile);
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String strLine;
		// Read File Line By Line
		
		
		while ((strLine = br.readLine()) != null) {
			// Print the content on the console
			String imageName = strLine.substring(0, strLine.indexOf(","));
//			System.out.println(imageName);
//			System.out.println(strLine);
			String imgURL = strLine.substring(strLine.indexOf(",")+1);
			System.out.println("Downloading: " + imgURL);
			downloadImage(imgURL, outDir+"/"+imageName+".png");
		}
		
		in.close();

	}
	
	public static void main(String[] args) throws IOException {
//		downloadImage("http://static.panoramio.com/photos/original/43620671.jpg", "c:/hatem/testIo");
		
//		copyImage("C:/Hatem/MyWork/Development/panoramio/TestCases/VisMatching/done/test/2319264444.png", 
//				"C:/Hatem/MyWork/Development/panoramio/TestCases/VisMatching/done/test/2319264444.png","png");
		copyBatch("E:/DataSets/GroundTruth/All/522192756_41.383514_2.189583/test/", "png");
//		downloadImages("C:/Hatem/MyWork/Development/PhotoDataSets/NUS-WIDE/Nature/urlm_all.csv","C:/Hatem/MyWork/Development/PhotoDataSets/NUS-WIDE/Images");
	}
}
