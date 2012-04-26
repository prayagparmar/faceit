package com.sjsu.faceit.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.imageio.ImageIO;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FaceItClient {

	/**
	 * @param args
	 */
//	Local
//	private static final String endPoint = "http://localhost:8080/faceit/service";
	
//	Linux
//	private static final String endPoint = "http://ec2-23-20-101-146.compute-1.amazonaws.com:8080/faceit/service";
	
//	Windows
	private static final String endPoint = "http://ec2-50-16-89-175.compute-1.amazonaws.com:8080/faceit/service";
	
	public static void main(String [] args) throws IOException, TransformerException {
//		signUpUser();
		loginUser();
	}
	
	private static void signUpUser() throws IOException, TransformerException {
		
		String url = endPoint + "/signup";

		RestClient client = new RestClient();
		Resource resource = client.resource(url);
		
		String xmlString = generateSignUpXML();
		System.out.println(xmlString);
		
		ClientResponse clr = resource.contentType(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).post(xmlString);
		System.out.println("Client Id:" + clr.getStatusCode());
		System.out.println(clr.getEntity(String.class));
		
	}
	
	private static void loginUser() throws IOException, TransformerException{
		
		String url = endPoint + "/login/username/shahmaniel@gmail.com/password/gP_lm9E";

		RestClient client = new RestClient();
		Resource resource = client.resource(url);
		
		String xmlString = generateLoginXML();
		System.out.println(xmlString);
		
		ClientResponse clr = resource.contentType(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).post(xmlString);
		System.out.println("Client Id:" + clr.getStatusCode());
		System.out.println(clr.getEntity(String.class));

	}
	
	private static String generateLoginXML() throws IOException, TransformerException{
		
		String image = generateImageXMLString("/Users/prayag/Desktop/Login/6.jpg");
		
		Document document = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.newDocument();
			
			Element rootElement = document.createElement("faceit");
			document.appendChild(rootElement);
			
			Element imageData = document.createElement("image");
		    imageData.setTextContent(image);
		    rootElement.appendChild(imageData);
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		    
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
        String xmlString = sw.toString();
        
        return xmlString;
	}
	
	private static String generateSignUpXML() throws IOException, TransformerException{
		
		String image1 = generateImageXMLString("/Users/prayag/Desktop/Signup/1.jpg");
		String image2 = generateImageXMLString("/Users/prayag/Desktop/Signup/2.jpg");
		String image3 = generateImageXMLString("/Users/prayag/Desktop/Signup/3.jpg");
		String image4 = generateImageXMLString("/Users/prayag/Desktop/Signup/4.jpg");
		String image5 = generateImageXMLString("/Users/prayag/Desktop/Signup/5.jpg");
		
		Document document = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.newDocument();
			
			Element rootElement = document.createElement("faceit");
			document.appendChild(rootElement);
			
			Element firstNameElement = document.createElement("firstname");
			firstNameElement.setTextContent("Maniel");
			rootElement.appendChild(firstNameElement);
			
			Element lastNameElement = document.createElement("lastname");
			lastNameElement.setTextContent("Shah");
			rootElement.appendChild(lastNameElement);
			
			Element emailElement = document.createElement("email");
			emailElement.setTextContent("shahmaniel@gmail.com");
			rootElement.appendChild(emailElement);
			
			Element stateElement = document.createElement("state");
			stateElement.setTextContent("CA");
			rootElement.appendChild(stateElement);
			
			Element zipElement = document.createElement("zip");
			zipElement.setTextContent("95112-7439");
			rootElement.appendChild(zipElement);
			
			Element securtiyQuestionElement = document.createElement("securityQuestion");
			securtiyQuestionElement.setTextContent("Pet's Name?");
			rootElement.appendChild(securtiyQuestionElement);
			
			Element securityAnswerElement = document.createElement("securityAnswer");
			securityAnswerElement.setTextContent("Tommy");
			rootElement.appendChild(securityAnswerElement);
			
			Element imageData = document.createElement("image1");
		    imageData.setTextContent(image1);
		    rootElement.appendChild(imageData);
		    
		    imageData = document.createElement("image2");
		    imageData.setTextContent(image2);
		    rootElement.appendChild(imageData);
		    
		    imageData = document.createElement("image3");
		    imageData.setTextContent(image3);
		    rootElement.appendChild(imageData);
		    
		    imageData = document.createElement("image4");
		    imageData.setTextContent(image4);
		    rootElement.appendChild(imageData);
		    
		    imageData = document.createElement("image5");
		    imageData.setTextContent(image5);
		    rootElement.appendChild(imageData);
		}catch(Exception e){
			e.printStackTrace();
		}
		    
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
        String xmlString = sw.toString();
        
        return xmlString;
        
	}
	
	private static String generateImageXMLString(String filePath) throws IOException{
		BufferedImage img = ImageIO.read(new File(filePath));    
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, "jpg", baos);    
		baos.flush();
		
		return new Base64().encodeToString(baos.toByteArray());
	}


}
