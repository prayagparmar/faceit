package com.sjsu.faceit.client;

import java.io.File;
import java.util.Iterator;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Metadata metadata = JpegMetadataReader.readMetadata(new File("/Users/prayag/Desktop/facedetect1334351264977.png"));			
			Iterator directories = metadata.getDirectoryIterator();
			while (directories.hasNext()) {
				Directory directory = (Directory)directories.next(); //iterate through tags and print to System.out 
				
				// try to extract width and height
				try {
					int width = directory.getInt(ExifDirectory.TAG_EXIF_IMAGE_WIDTH);
					System.out.println(width);
					int height = directory.getInt(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT);
					System.out.println(height);
					int scanWindowPixelSize = (int) Math.max(10, Math.round(((double)Math.min(width, height) * ((double) 15 / 100.0f))));
					System.out.println("Width: " + scanWindowPixelSize);
				} catch (MetadataException e){
					e.printStackTrace();
				}
				
			}
		} catch (JpegProcessingException e) {
		}

	}

}
