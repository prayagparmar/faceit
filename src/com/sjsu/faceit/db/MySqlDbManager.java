package com.sjsu.faceit.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sjsu.faceit.bean.SignUpData;
import com.sjsu.faceit.exception.InvalidEmailException;
import com.sjsu.faceit.exception.InvalidSecurityAnswerException;

public class MySqlDbManager {
	
	private static MySqlDbManager instance = null;
	private static Connection connection = null;
	private static final String DB_STRING = "jdbc:mysql://localhost:3306/faceit?autoReconnect=true";
	
	//Making Singleton. No object creation from outside.
	private MySqlDbManager(){
	}
	
	/**
	 * Provides an object of this class.
	 * This Object can be used to do various database operations.
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static synchronized MySqlDbManager getInstance() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		
		if(instance == null){
			instance = new MySqlDbManager();
			init();
		}
		
		return instance;
	}
	
	private static void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		//Create the MySQL DB connection here
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(DB_STRING, "root", "");
	}

	/**
	 * Adds Sign Up data of the user to the database
	 * @param signUpData
	 * @throws SQLException 
	 */
	public void AddSignUpDataToDB(SignUpData signUpData) throws SQLException{
		//Allow commit only after query has executed successfully
		connection.setAutoCommit(false);
		
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_data(first_name, last_name, password, email, state, zip, image_key, security_question, security_answer) VALUES (?,?,?,?,?,?,?,?,?)");
		preparedStatement.setString(1, signUpData.getFirstName());
		preparedStatement.setString(2, signUpData.getLastName());
		preparedStatement.setString(3, signUpData.getPassword());
		preparedStatement.setString(4, signUpData.getEmail());
		preparedStatement.setString(5, signUpData.getState());
		preparedStatement.setString(6, signUpData.getZip());
		preparedStatement.setString(7, signUpData.getImageKey());
		preparedStatement.setString(8, signUpData.getSecurityQuestion());
		preparedStatement.setString(9, signUpData.getSecurityAnswer());
		preparedStatement.executeUpdate();
		
		//commit the data
		connection.commit();
	}
	
	/**
	 * Takes username as an argument and returns complete set of userNames that matches this userName
	 * @param email
	 * @return TRUE if the email/user already exists in the database
	 * @throws SQLException
	 */
	public boolean isEmailRegistered(String email) throws SQLException{
		
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_data WHERE email=?");
		preparedStatement.setString(1, email);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		resultSet.last();
		
		//return false if the username does not exist
		if(resultSet.getRow() == 0){
			return false;
		}
		
		return true;
	}
	
	
	public static void main(String [] args) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
//		SignUpData signUpData = new SignUpData("Maniel", "Shah", "london@11", "shahmaniel@gmail.com", "CA", "95112-7439", 
//												"Maiden Name", "Shah", "uihiu", "oijoijo", "oijoijo", "oijoijo", "oijoijo", "oijoijo");
//		
//		getInstance().AddSignUpDataToDB(signUpData);
//		
//		System.out.println(getInstance().isEmailRegistered("shahmaniel@gmai.com"));
//		
//		System.out.println(getInstance().checkUserNamePasswordMatch("shahmaniel@gmail.com", "lndon@11"));
//		
//		System.out.println(getInstance().fetchImageKey("shahmaniel@gmail.com"));
		
		System.out.println(getInstance().fetchImageKey("shahmaniel@gmail.com"));
		
	}
	
	
	/**
	 * Removes the user data from the database
	 * @param userName
	 * @throws SQLException 
	 */
	public void DeleteUserFromDB(String email) throws SQLException{
		//Allow commit only after query has executed successfully
		connection.setAutoCommit(false);
		
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user_data WHERE email=?");
		preparedStatement.setString(1, email);
		preparedStatement.executeUpdate();
		
		//commit the data
		connection.commit();
	}
	
	/**
	 * Checks if the username and password matches.
	 * It is recommended to use isEmailRegistered(String email) first to check whether the user exists or not
	 * @param userName
	 * @param password
	 * @return TRUE if the username and password matches. FALSE otherwise. 
	 * @throws SQLException 
	 */
	public boolean checkUserNamePasswordMatch(String userName, String password) throws SQLException{
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_data WHERE email=? and password=?");
		preparedStatement.setString(1, userName);
		preparedStatement.setString(2, password);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		resultSet.last();
		
		//return false if the username and password combination does not match
		if(resultSet.getRow() == 0){
			return false;
		}
		
		return true;
	}
	
	public String fetchImageKey(String userName) throws SQLException{
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT image_key FROM user_data WHERE email=?");
		preparedStatement.setString(1, userName);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		while(resultSet.next()){
			String imageKey = resultSet.getString(1);
			System.out.println("\n\nImage Key: " + imageKey + "\n\n");
			return imageKey;
		}
		
		return null;
	}
	
	public String fetchSecurityQuestion(String userName) throws SQLException, InvalidEmailException{
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT security_question FROM user_data WHERE email=?");
		preparedStatement.setString(1, userName);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		while(resultSet.next()){
			return resultSet.getString(1);
		}
		
		throw new InvalidEmailException("Email not registered with us!!");
	}
	
	public boolean verifySecurityAnswer(String userName, String securityanswer) throws SQLException, InvalidSecurityAnswerException{
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT security_answer FROM user_data WHERE email=?");
		preparedStatement.setString(1, userName);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		while(resultSet.next()){
			if(resultSet.getString(1).equalsIgnoreCase(securityanswer)){
				return true;
			}
		}
		
		throw new InvalidSecurityAnswerException("Security Answer is incorrect!!");
	}
	
	public void updateNewPassword(String userName, String password) throws SQLException{
		connection.setAutoCommit(false);
		
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_data SET password=? WHERE email=?");
		preparedStatement.setString(1, password);
		preparedStatement.setString(2, userName);
		preparedStatement.executeUpdate();
		
		connection.commit();
	}
	
	/**
	 * Updates the user data in the database
	 * @param signUpData
	 */
	public void UpdateData(SignUpData signUpData){
		
	}
}
