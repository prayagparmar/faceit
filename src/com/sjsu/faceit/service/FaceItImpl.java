package com.sjsu.faceit.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.sjsu.faceit.bean.LoginImageData;
import com.sjsu.faceit.bean.SignUpData;
import com.sjsu.faceit.db.AmazonS3Manager;
import com.sjsu.faceit.db.MySqlDbManager;
import com.sjsu.faceit.exception.AmazonS3Exception;
import com.sjsu.faceit.exception.EmailAlreadyRegisteredException;
import com.sjsu.faceit.exception.InvalidEmailException;
import com.sjsu.faceit.exception.InvalidNameException;
import com.sjsu.faceit.exception.InvalidSecurityAnswerException;
import com.sjsu.faceit.exception.InvalidStateException;
import com.sjsu.faceit.exception.InvalidZipException;
import com.sjsu.faceit.exception.UnableToProcesImageException;
import com.sjsu.faceit.helper.ImageCodec;
import com.sjsu.faceit.helper.ImplHelper;
import com.sjsu.faceit.helper.XMLParser;

import de.offis.faint.controller.MainController;
import de.offis.faint.global.Constants;
import de.offis.faint.model.FaceDatabase;

public class FaceItImpl implements FaceItService{
	private static FaceItImpl svc;
	
	private final static int SIGN_UP_IMAGE_1 = 1;
	private final static int SIGN_UP_IMAGE_2 = 2;
	private final static int SIGN_UP_IMAGE_3 = 3;
	private final static int SIGN_UP_IMAGE_4 = 4;
	private final static int SIGN_UP_IMAGE_5 = 5;
	private final static int LOGIN_IMAGE     = 6;
	
	public synchronized static FaceItImpl getInstance() {
		if (svc == null) {
			svc = new FaceItImpl();
		}

		return svc;
	}

	@Override
	public Response signUpUser(String signUpXMLData) {
		
		System.out.println("Sign Up User called!!");
		
		SignUpData signUpData = null;
		
		try {
			
			//Validate and convert XML data to SignUp object
			signUpData = XMLParser.readSignUpXMLData(signUpXMLData);
			
			//Check if the email is already registered
			ImplHelper.isEmailRegistered(signUpData.getEmail());
			
			//Fetch data for decoding the image
			String imageKey   = signUpData.getImageKey();
			String imageData1 = signUpData.getImageData1();
			String imageData2 = signUpData.getImageData2();
			String imageData3 = signUpData.getImageData3();
			String imageData4 = signUpData.getImageData4();
			String imageData5 = signUpData.getImageData5();
			
			//create a folder with imageKey in temp folder inside tomcat
			String folderPath = ImplHelper.createImageFolder(imageKey);
			
			//Decode the 5 images
			File imageFile1 = ImageCodec.convertXMLtoImage(imageData1 , folderPath, SIGN_UP_IMAGE_1);
			File imageFile2 = ImageCodec.convertXMLtoImage(imageData2 , folderPath, SIGN_UP_IMAGE_2);
			File imageFile3 = ImageCodec.convertXMLtoImage(imageData3 , folderPath, SIGN_UP_IMAGE_3);
			File imageFile4 = ImageCodec.convertXMLtoImage(imageData4 , folderPath, SIGN_UP_IMAGE_4);
			File imageFile5 = ImageCodec.convertXMLtoImage(imageData5 , folderPath, SIGN_UP_IMAGE_5);
			
			//Create a Face DB to store all 5 image detection data
			FaceDatabase faceDB = new FaceDatabase();
			
			//Put the entry of first person into the database
			faceDB.put(null, "1");
			
			// Give the image file path to face detection algorithm.
			MainController.getInstance().detectFace(imageFile1, faceDB);
			MainController.getInstance().detectFace(imageFile2, faceDB);
			MainController.getInstance().detectFace(imageFile3, faceDB);
			MainController.getInstance().detectFace(imageFile4, faceDB);
			MainController.getInstance().detectFace(imageFile5, faceDB);
			
			//Store success data into detection.txt
			String currentTimeStamp = ImplHelper.getCurrentTimeStamp();
			ImplHelper.storeDetectionSuccessData(signUpData.getEmail(), currentTimeStamp);
		
			//Store the facedb in the same folder as images
			File faceDBFile = faceDB.writeToDisk(folderPath);
			
			//Store the image and faceDB data to Amazon S3
			AmazonS3Manager.storeImageData(imageKey, imageFile1);
			AmazonS3Manager.storeImageData(imageKey, imageFile2);
			AmazonS3Manager.storeImageData(imageKey, imageFile3);
			AmazonS3Manager.storeImageData(imageKey, imageFile4);
			AmazonS3Manager.storeImageData(imageKey, imageFile5);
			AmazonS3Manager.storeImageData(imageKey, faceDBFile);
			
			//delete the folder from temp folder once written to Amazon S3
			boolean isDeleted = ImplHelper.deleteFolder(folderPath);
			System.out.println("Checking if the folder was deleted: " + isDeleted);
			
			//store the complete data along with unique retrieval key into MySQL DB. MySQL is on EC2.
			MySqlDbManager.getInstance().AddSignUpDataToDB(signUpData);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return Response.status(500).entity("Parser Error!!").build();
		} catch (SAXException e) {
			e.printStackTrace();
			return Response.status(500).entity("SAX Error!!").build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(500).entity("IOException Error!!").build();
		} catch (InvalidNameException e) {
			e.printStackTrace();
			return Response.status(500).entity("Invalid Name Error!!").build();
		} catch (InvalidEmailException e) {
			e.printStackTrace();
			return Response.status(500).entity("Invalid Email Error!!").build();
		} catch (InvalidStateException e) {
			e.printStackTrace();
			return Response.status(500).entity("Invalid state Error!!").build();
		} catch (InvalidZipException e) {
			e.printStackTrace();
			return Response.status(500).entity("Invalid Zip Error!!").build();
		} catch (AmazonS3Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Amazon S3 Error!!").build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(500).entity("SQL Error!!").build();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return Response.status(500).entity("Instantiation Error!!").build();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return Response.status(500).entity("Illegal Access Exception Error!!").build();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return Response.status(500).entity("Class not found Error!!").build();
		} catch (InvalidSecurityAnswerException e) {
			e.printStackTrace();
			return Response.status(500).entity("Invalid security answer Error!!").build();
		} catch (EmailAlreadyRegisteredException e) {
			e.printStackTrace();
			return Response.status(500).entity("Email registered Error!!").build();
		} catch (UnableToProcesImageException e) {
			e.printStackTrace();
			
			//store failure data into detectionFailure.txt
			String currentTimeStamp = ImplHelper.getCurrentTimeStamp();
			ImplHelper.storeDetectionFailureData(signUpData.getEmail(), currentTimeStamp);
			
			return Response.status(500).entity(e.getMessage()).build();
		} catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("General Exception!!").build();
		}
		
		return Response.status(200).entity(signUpData.getPassword()).build();
	}

	@Override
	public Response loginUser(String userName, String password, String imageXMLData) {
		
		try {
			
			//check if the userName exists
			boolean isEmailRegistered = MySqlDbManager.getInstance().isEmailRegistered(userName);
			if(!isEmailRegistered){
				Response.status(200).entity("Username/Email is not registered!!").build();
			}
			
			//check if username and password matches
			boolean isPasswordCorrect = MySqlDbManager.getInstance().checkUserNamePasswordMatch(userName, password);;
			if(!isPasswordCorrect){
				Response.status(200).entity("Access code is incorrect!!").build();
			}
			
			//Store the xml image data
			LoginImageData loginImageData = XMLParser.readLoginXMLData(imageXMLData);
			String imageData = loginImageData.getImageData();
			
			//fetch the original image key from the database
			String originalImageKey = MySqlDbManager.getInstance().fetchImageKey(userName);
			System.out.println("\n\nImage Key: " + originalImageKey + "\n\n");
			
			//create a folder with imageKey in temp folder inside tomcat
			String folderPath = ImplHelper.createImageFolder(originalImageKey);
			System.out.println("\n\nFolder: " + folderPath + "\n\n");
			
			//fetch the image from the cloud and generate a thumbnail
			AmazonS3Manager.fetchImageData(originalImageKey, folderPath);
			
			//Convert XML to image
			File imageFile = ImageCodec.convertXMLtoImage(imageData, folderPath, LOGIN_IMAGE);
			
			//Generate Face DB from the file
			System.out.println("Trying to generate Face DB..");
			FaceDatabase faceDB = FaceDatabase.recoverFromDisk(folderPath + File.separator + Constants.FACE_DB_FILE);
			
			//crop the image using face detection code
			HashMap<String, Integer> recognitionMap = MainController.getInstance().recognizeFace(imageFile, faceDB);
			
			//Iterate through the Set with recognition values
			Set<Entry<String, Integer>> recognitionSet = recognitionMap.entrySet();
			Iterator<Entry<String, Integer>> list = recognitionSet.iterator();
			
			int recognitionScore = 0;
			
			//Check the score and decide whether the face matched or not
			while(list.hasNext()){
				recognitionScore = list.next().getValue();
				System.out.println("Recognition score: " + recognitionScore);
				ImplHelper.storeRecognitionData(userName, ImplHelper.getCurrentTimeStamp(), recognitionScore);
			}
			
			//Delete the folder once recognition score is obtained
			boolean isDeleted = ImplHelper.deleteFolder(folderPath);
			System.out.println("Checking if the folder was deleted: " + isDeleted);
			
			//return success if score is greater than 40
			if(recognitionScore > 40){
				return Response.status(200).entity("Face Recognition SUCCEEDED!!").build();
			}
			
			return Response.status(500).entity("Face Recognition FAILED!!").build();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(500).entity("SQL exception").build();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return Response.status(500).entity("Instantiation exception").build();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return Response.status(500).entity("Illegal access exception").build();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return Response.status(500).entity("Class not found exception").build();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return Response.status(500).entity("Parser Configuration exception").build();
		} catch (SAXException e) {
			e.printStackTrace();
			return Response.status(500).entity("SAX exception").build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(500).entity("IO exception").build();
		} catch (AmazonS3Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Amazon S3 exception").build();
		} catch (UnableToProcesImageException e) {
			e.printStackTrace();
			return Response.status(500).entity(e.getMessage()).build();
		} catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("General Exception!!").build();
		}
		
	}

	@Override
	public Response deleteUser(String userName) {
		// TODO Auto-generated method stub
		return null;
	}
	 
	@Override
	public Response resetPassword(String userName) {
		
		try {
			//Check the email if it is registered and fetch the security question
			String securityQuestion = MySqlDbManager.getInstance().fetchSecurityQuestion(userName);
			
			return Response.status(200).entity(securityQuestion).build();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(500).entity("SQL exception").build();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return Response.status(500).entity("Instantiation exception").build();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return Response.status(500).entity("Illegal access exception").build();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return Response.status(500).entity("Class not found exception").build();
		} catch (InvalidEmailException e) {
			e.printStackTrace();
			return Response.status(500).entity(e.getMessage()).build();
		} catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("General Exception!!").build();
		}

	}

	@Override
	public Response resetPasswordAnswer(String userName, String securityanswer) {
		
		String newPassword = null;
		
		try {
			//verify the security answer
			MySqlDbManager.getInstance().verifySecurityAnswer(userName, securityanswer);
			
			//regenerate the password
			newPassword = ImplHelper.generatePassword();
			
			//update the database with new password
			MySqlDbManager.getInstance().updateNewPassword(userName, newPassword);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(500).entity("SQL exception").build();
		} catch (InvalidSecurityAnswerException e) {
			e.printStackTrace();
			return Response.status(500).entity(e.getMessage()).build();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return Response.status(500).entity("Instantiation exception").build();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return Response.status(500).entity("Illegal access exception").build();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return Response.status(500).entity("Class not found exception").build();
		} catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("General Exception!!").build();
		}
		
		return Response.status(200).entity(newPassword).build();
	}
}
