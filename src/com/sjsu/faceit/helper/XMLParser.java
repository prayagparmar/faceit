package com.sjsu.faceit.helper;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sjsu.faceit.bean.LoginImageData;
import com.sjsu.faceit.bean.SignUpData;
import com.sjsu.faceit.exception.InvalidEmailException;
import com.sjsu.faceit.exception.InvalidNameException;
import com.sjsu.faceit.exception.InvalidSecurityAnswerException;
import com.sjsu.faceit.exception.InvalidStateException;
import com.sjsu.faceit.exception.InvalidZipException;
import com.sjsu.faceit.validation.ValidateSignUpData;

public class XMLParser {
	
	private static final String TAG_FIRST_NAME 			= "firstname";
	private static final String TAG_LAST_NAME  			= "lastname";
	private static final String TAG_EMAIL      			= "email";
	private static final String TAG_STATE      			= "state";
	private static final String TAG_ZIP        			= "zip";
	private static final String TAG_SECURITY_QUESTION   = "securityQuestion";
	private static final String TAG_SECURITY_ANSWER     = "securityAnswer";
	private static final String TAG_IMAGE         		= "image";
	private static final String TAG_IMAGE_1      		= "image1";
	private static final String TAG_IMAGE_2      		= "image2";
	private static final String TAG_IMAGE_3      		= "image3";
	private static final String TAG_IMAGE_4      		= "image4";
	private static final String TAG_IMAGE_5      		= "image5";
	
	public static SignUpData readSignUpXMLData(String xmlFile) throws ParserConfigurationException, SAXException, IOException, InvalidNameException, InvalidEmailException, InvalidStateException, InvalidZipException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, InvalidSecurityAnswerException{
		Document document = parseXML(xmlFile);
		return castToSignUpData(document);
	}
	
	public static LoginImageData readLoginXMLData(String xmlFile) throws ParserConfigurationException, SAXException, IOException{
		Document document = parseXML(xmlFile);
		return castToLoginData(document);
	}
	
	private static LoginImageData castToLoginData(Document document){
		String imageData = document.getElementsByTagName(TAG_IMAGE).item(0).getTextContent();

		return new LoginImageData(imageData);
	}
	
	private static SignUpData castToSignUpData(Document document) throws InvalidNameException, InvalidEmailException, InvalidStateException, InvalidZipException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, InvalidSecurityAnswerException{
		
		//Fetch the text content from XML
		String firstName 		= document.getElementsByTagName(TAG_FIRST_NAME).item(0).getTextContent();
		String lastName  		= document.getElementsByTagName(TAG_LAST_NAME).item(0).getTextContent();
		String password  		= ImplHelper.generatePassword();
		String email     		= document.getElementsByTagName(TAG_EMAIL).item(0).getTextContent();
		String state     		= document.getElementsByTagName(TAG_STATE).item(0).getTextContent();
		String zip       		= document.getElementsByTagName(TAG_ZIP).item(0).getTextContent();
		String securityQuestion = document.getElementsByTagName(TAG_SECURITY_QUESTION).item(0).getTextContent();
		String securityAnswer   = document.getElementsByTagName(TAG_SECURITY_ANSWER).item(0).getTextContent();
		String imageKey         = UUID.randomUUID().toString();
		String imageData1 		= document.getElementsByTagName(TAG_IMAGE_1).item(0).getTextContent();
		String imageData2 		= document.getElementsByTagName(TAG_IMAGE_2).item(0).getTextContent();
		String imageData3 		= document.getElementsByTagName(TAG_IMAGE_3).item(0).getTextContent();
		String imageData4 		= document.getElementsByTagName(TAG_IMAGE_4).item(0).getTextContent();
		String imageData5 		= document.getElementsByTagName(TAG_IMAGE_5).item(0).getTextContent();
		
		//Validate the content
		if(!ValidateSignUpData.validateFirstName(firstName)){
			throw new InvalidNameException("First Name accepts only uppercase and lowercase letters.");
		}else if(!ValidateSignUpData.validateLastName(lastName)){
			throw new InvalidNameException("Last Name accepts only uppercase and lowercase letters.");
		}else if(!ValidateSignUpData.validateEmail(email)){
			throw new InvalidEmailException("Invalid email format");
		}else if(!ValidateSignUpData.validateState(state)){
			throw new InvalidStateException("Enter only valid two letter initials for the state");
		}else if(!ValidateSignUpData.validateZip(zip)){
			throw new InvalidZipException("Zip should follow format 12345 or 12345-6789");
		}else if(securityAnswer == ""){
			throw new InvalidSecurityAnswerException("Security Answer cannot be empty");
		}
		
		return new SignUpData(firstName, lastName, password, email, state, zip, securityQuestion, securityAnswer, imageKey, imageData1, imageData2, imageData3, imageData4, imageData5);
	}
	
	//Processes file and returns Document Object
	private static Document parseXML(String xmlFile) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document parsedDocument = null;

		
        builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xmlFile));
		parsedDocument = builder.parse(inputSource);
		
		return parsedDocument;
	}
	
	
}
