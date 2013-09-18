package com.sjsu.faceit.helper;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.sjsu.faceit.db.MySQLManager;
import com.sjsu.faceit.exception.EmailAlreadyRegisteredException;

public class ImplHelper {
	
	private final static String LOWER   = "abcdefghijklmnopqrstuvwxyz";
	private final static String UPPER   = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final static String NUMBERS = "0123456789";
//	private final static String DETECTION_SUCCESS_FILE = ".." + File.separator + "temp"  + File.separator + "detectionSuccess.txt";
//	private final static String DETECTION_FAILURE_FILE = ".." + File.separator + "temp"  + File.separator + "detectionFailure.txt";
//	private final static String RECOGNITION_SCORE_FILE = ".." + File.separator + "temp"  + File.separator + "recognitionScore.txt";
		
	// password
	private final static String PASSWORD_SYMBOLS  = "-_";
	private final static String PASSWORD_ELIGIBLE = LOWER + UPPER + NUMBERS + PASSWORD_SYMBOLS;
	private final static int PASSWORD_LENGTH      = 7;
	
	private final static Random random = new Random(); 
	
	public static String generatePassword(){
		
		StringBuilder password = new StringBuilder();
		
		// one char of each required class
		password.insert(random.nextInt(password.length() + 1), LOWER.charAt(random.nextInt(26)));
		password.insert(random.nextInt(password.length() + 1), UPPER.charAt(random.nextInt(26)));
		password.insert(random.nextInt(password.length() + 1), NUMBERS.charAt(random.nextInt(10)));
		password.insert(random.nextInt(password.length() + 1), PASSWORD_SYMBOLS.charAt(random.nextInt(PASSWORD_SYMBOLS.length())));

		//desired length of the password
		int targetLength = PASSWORD_LENGTH;
		
		while (password.length() < targetLength) {
			int insertIndex = random.nextInt(password.length() + 1);
			char c = PASSWORD_ELIGIBLE.charAt(random.nextInt(PASSWORD_ELIGIBLE.length()));
			password.insert(insertIndex, c);
		}
		
		return password.toString();
	}
	
	public static void isEmailRegistered(String email) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, EmailAlreadyRegisteredException{
		if(MySQLManager.getInstance().isEmailRegistered(email))
			throw new EmailAlreadyRegisteredException();
	}
	
	public static boolean deleteFolder(String folderPath){
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		
		for (File file : files)
		{
		   // Delete each file
		   if (!file.delete())
		   {
		       // Failed to delete file
		       System.out.println("Failed to delete "+file);
		   }
		}
		
		return folder.delete();
	}
	
	/**
	 * Creates a folder in the temp dir of tomcat
	 * @param imageKey
	 * @return Returns absolute path of the folder
	 */
	public static String createImageFolder(String imageKey) {
		File folder = new File("../temp/" + imageKey);
		folder.mkdir();
		return folder.getAbsolutePath();
	}

	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
//	public static void storeDetectionSuccessData(String email, String currentTimeStamp) {
//		storeDetectionData(email, DETECTION_SUCCESS_FILE, currentTimeStamp);
//	}
//	
//	public static void storeDetectionFailureData(String email, String currentTimeStamp){
//		storeDetectionData(email, DETECTION_FAILURE_FILE, currentTimeStamp);
//	}
	
//	public static void storeRecognitionData(String email, String currentTimeStamp, int recognitionScore) {
//		try{
//			  // Create file 
//			  FileWriter fstream = new FileWriter(RECOGNITION_SCORE_FILE, true);
//			  BufferedWriter out = new BufferedWriter(fstream);
//			  int lengthCounter = email.length();
//			  
//			  //write the data
//			  out.write(email);
//			  while(lengthCounter < 40){
//				out.write(" ");
//				lengthCounter++;
//			  }
//			  out.write(currentTimeStamp);
//			  while(lengthCounter < 50){
//					out.write(" ");
//					lengthCounter++;
//			  }
//			  out.write(Integer.toString(recognitionScore));
//			  out.newLine();
//			  out.flush();
//			  //Close the output stream
//			  out.close();
//			  
//		}catch (Exception e){//Catch exception if any
//			  System.err.println("Error: " + e.getMessage());
//		}
//	}

//	private static synchronized void storeDetectionData(String email, String file, String currentTimeStamp){
//		try{
//			  // Create file 
//			  FileWriter fstream = new FileWriter(file, true);
//			  BufferedWriter out = new BufferedWriter(fstream);
//			  int lengthCounter = email.length();
//			  
//			  //write the data
//			  out.write(email);
//			  while(lengthCounter < 50){
//				out.write(" ");
//				lengthCounter++;
//			  }
//			  out.write(currentTimeStamp);
//			  out.write("\n");
//			  out.flush();
//			  //Close the output stream
//			  out.close();
//			  
//		}catch (Exception e){//Catch exception if any
//			  System.err.println("Error: " + e.getMessage());
//		}
//	}
	
	
}
