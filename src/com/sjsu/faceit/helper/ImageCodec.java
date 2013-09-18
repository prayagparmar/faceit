package com.sjsu.faceit.helper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.codec.binary.Base64;
import org.xml.sax.SAXException;

public class ImageCodec {

//	/**
//	 * @param args
//	 * @throws IOException 
//	 * @throws TransformerException 
//	 * @throws ParserConfigurationException 
//	 * @throws SAXException 
//	 */
//	public static void main(String[] args) throws IOException, TransformerException, SAXException, ParserConfigurationException {
//		String imagePath = "/Users/prayag/Desktop/";
//		String xmlString = encodeImage(imagePath + "1.jpg");
//		System.out.println(xmlString);
//		
//		convertXMLtoImage(xmlString, imagePath + "3.jpg");
//	}
	
	/**
	 * 
	 * @param imagePath
	 * @return
	 * @throws IOException
	 * @throws TransformerException
	 */
	public static String encodeImage(String imagePath) throws TransformerException{
		 
		BufferedImage img;
		String encodedImage = null;
		try {
			img = ImageIO.read(new File("../temp/" + imagePath + ".jpg"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", baos);
			baos.flush();
			encodedImage = new Base64().encodeToString(baos.toByteArray());
			baos.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}    

		return encodedImage;
		 
	}
	
	/**
	 * 
	 * @param xmlImageData
	 * @param folderPath
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static File convertXMLtoImage(String xmlImageData, String folderPath, int imageNumber) throws IOException{
        
		byte[] bytes = new Base64().decode(xmlImageData);
		BufferedImage image;
			
		try {
			image = ImageIO.read(new ByteArrayInputStream(bytes));
			File file = new File(folderPath + "/" + imageNumber + ".jpg");
			file.setWritable(true);
			ImageIO.write(image, "jpg", file);
			
			return file;
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new IOException();
		}
		
	}

}
