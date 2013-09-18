package com.sjsu.faceit.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.sjsu.faceit.exception.AmazonS3Exception;

import de.offis.faint.global.Constants;

public class AmazonS3Manager {
	
	private static AmazonS3 instance = null;
	private static final String BUCKET_NAME = "faceitservice";
	private static String exceptionString = null;
	private static final String JPG_EXTENSION = ".jpg";
	
	public static void createConnection() throws AmazonS3Exception{
		instance = new AmazonS3Client(new AWSCredentials() {
			
			
			//Modify the Amazon S3 keys with the new account created
			@Override
			public String getAWSSecretKey() {
				return "7hnuFtnUOlNw/diD6ezw3pJmtMwaomBwaeAerfqu";
			}
			
			@Override
			public String getAWSAccessKeyId() {
				return "AKIAJ6EFRWXIAUC5FOTQ";
			}
		});
			
		//Check if the bucket is created. If not, create one. 
		if(!instance.listBuckets().contains(BUCKET_NAME)){
			
			//Create bucket
			try{
				instance.createBucket(BUCKET_NAME);
			}catch(AmazonServiceException ase){
				ase.printStackTrace();
				throw new AmazonS3Exception("Amazon Service Exception");
			}catch(AmazonClientException ace){
				ace.printStackTrace();
				throw new AmazonS3Exception("Amazon Client Exception");
			}
			
		}
	}
	
	public static PutObjectResult storeImageData(String S3Folder, File imageFile) throws IOException, AmazonS3Exception{
		
		createConnection();
		
		System.out.println("created the connection!");
		
		//Check if any exception occured in static block
		if(exceptionString != null){
			throw new AmazonS3Exception(exceptionString);
		}
		
		String S3FilePath = S3Folder + "/" + imageFile.getName();
		
//		System.out.println("pre createFileFromString()");
//		File imageFile = createFileFromString(key, imageData);
//		System.out.println("post createFileFromString()");
//		System.out.println(imageFile.toString());
		
		return instance.putObject(new PutObjectRequest(BUCKET_NAME, S3FilePath, imageFile));
	}
	
	public static void fetchImageData(String imageKey, String folderPath) throws IOException, AmazonS3Exception{
		
		createConnection();
		
		//Check if any exception occured in static block
		if(exceptionString != null){
			throw new AmazonS3Exception(exceptionString);
		}
		
		try{
			
			//Fetch all 5 images from the cloud
			for( int i = 1 ; i <= 5 ; i++){
				S3Object imageObject = instance.getObject(new GetObjectRequest(BUCKET_NAME, imageKey + "/" + i + JPG_EXTENSION));
		        createFileFromS3Object(imageObject.getObjectContent(), folderPath, i + JPG_EXTENSION);
			}
			
			//Fetch the faceDB file
			S3Object faceDBObject = instance.getObject(new GetObjectRequest(BUCKET_NAME, imageKey + "/" + Constants.FACE_DB_FILE));
			createFileFromS3Object(faceDBObject.getObjectContent(), folderPath, Constants.FACE_DB_FILE);
			
		}catch(AmazonClientException ace){
			ace.printStackTrace();
			throw new AmazonClientException("Amazon Client Error!!");
		}
        
	}
	
//	private static File createFileFromString(String key, String imageData) throws IOException{
//		System.out.println("first statement");
//		File file = null;
//		file = new File("../temp/" + key + ".txt");
//		
//		System.out.println("created temp file key");
//		FileOutputStream fileOutputStream = null;
//		
//		try{
//			fileOutputStream = new FileOutputStream(file);
//		}catch(FileNotFoundException fileNotFoundException){
//			fileNotFoundException.printStackTrace();
//		}
//		
//        Writer writer = new OutputStreamWriter(fileOutputStream);
//        System.out.println("instantiated new writer");
//        writer.write(imageData);
//        System.out.println("wrote image data");
//        writer.close();
//        System.out.println("writer is closed");
//
//        return file;
//	}
	
	private static void createFileFromS3Object(InputStream imageDataStream, String folderPath, String fileName) throws IOException{
		
		File imageFile = new File(folderPath + File.separator + fileName);
    	OutputStream out = new FileOutputStream(imageFile);
    	
    	byte buf[] = new byte[1024];
    	int streamLength;
    	
    	while((streamLength = imageDataStream.read(buf)) > 0){
    		out.write(buf,0,streamLength);
    	}
    	
    	out.close();
    	imageDataStream.close();
    	
    	System.out.println("File created from input Stream");
	}
	
}
