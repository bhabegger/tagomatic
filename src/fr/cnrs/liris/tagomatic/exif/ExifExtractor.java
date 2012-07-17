package fr.cnrs.liris.tagomatic.exif;

import java.io.File;
import java.io.IOException;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;

import fr.cnrs.liris.tagomatic.entities.GpsCoordinates;

public class ExifExtractor {

	
	public GpsCoordinates exifExtractor(String imagePath){
		   
		GpsCoordinates gpsCoords = null ;
		File imageFile = new File(imagePath);
		
		Metadata metadata = null;
        try {
            metadata = JpegMetadataReader.readMetadata(imageFile);
            
         // obtain the Exif directory
            Directory directory = metadata.getDirectory(GpsDirectory.class);

            String  lat = directory.getDescription(GpsDirectory.TAG_GPS_LATITUDE).replace('°', ',').replace('\'', ',').replace("\"","");
            
            String lon = directory.getDescription(GpsDirectory.TAG_GPS_LONGITUDE).replace('°', ',').replace('\'', ',').replace("\"","");

            String[] latS = lat.split(",");
            String[] lonS = lon.split(",");
            
            float d = Float.valueOf(latS[0]);
            float m = Float.valueOf(latS[1]) ;
            float s = Float.valueOf(latS[2]);
            		

            float latitude = (float) (Math.signum(d) * (Math.abs(d) + (m / 60.0) + (s / 3600.0)));
           

            d = Float.valueOf(lonS[0]);
            m = Float.valueOf(lonS[1]) ;
            s = Float.valueOf(lonS[2]);
            float longitude = (float) (Math.signum(d) * (Math.abs(d) + (m / 60.0) + (s / 3600.0)));
     
            
            gpsCoords = new GpsCoordinates(latitude,longitude);
            
          
        } catch (JpegProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return gpsCoords;
	}
}
