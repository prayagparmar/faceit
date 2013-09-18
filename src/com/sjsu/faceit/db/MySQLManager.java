package com.sjsu.faceit.db;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

import com.sjsu.faceit.bean.SignUpData;
import com.sjsu.faceit.exception.InvalidEmailException;
import com.sjsu.faceit.exception.InvalidSecurityAnswerException;

public class MySQLManager {
	
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_STRING = "jdbc:mysql://localhost:3306/faceit";
	private static final String DB_USER   = "root";
	private static final String DB_PASS   = "";
	private static final int DB_MAX_CONN  = 50;
	private static final int DB_ABANDONED_TIMEOUT = 30;
	private static final BasicDataSource bds = new BasicDataSource();
	
	static{
		init();
	}
	
	private MySQLManager(){
	}
	
	public static MySQLManager getInstance() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		return new MySQLManager();
	}
	
	private static void init() {
		bds.setDriverClassName(DB_DRIVER);
		bds.setUrl(DB_STRING);
		bds.setUsername(DB_USER);
		bds.setPassword(DB_PASS);
		bds.setMaxActive(DB_MAX_CONN);
		bds.setRemoveAbandoned(true);
		bds.setRemoveAbandonedTimeout(DB_ABANDONED_TIMEOUT);
	}
	
	private Connection connect() throws SQLException{
		return bds.getConnection();
	}
	
	private void release(Connection connection) throws SQLException{
		connection.close();
	}
	
	/**
	 * Adds Sign Up data of the user to the database
	 * @param signUpData
	 * @throws SQLException 
	 */
	public void AddSignUpDataToDB(SignUpData signUpData) throws SQLException{
		
		Connection connection = connect();
		
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
		
		release(connection);
	}
	
	/**
	 * Takes username as an argument and returns complete set of userNames that matches this userName
	 * @param email
	 * @return TRUE if the email/user already exists in the database
	 * @throws SQLException
	 */
	public boolean isEmailRegistered(String email) throws SQLException{
		
		Connection connection = connect();
		
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_data WHERE email=?");
		preparedStatement.setString(1, email);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		resultSet.last();
		
		//return false if the username does not exist
		if(resultSet.getRow() == 0){
			return false;
		}
		
		release(connection);
		
		return true;
	}
	
	/**
	 * Removes the user data from the database
	 * @param userName
	 * @throws SQLException 
	 */
	public void deleteUserFromDB(String email) throws SQLException{
		
		Connection connection = connect();
		
		//Allow commit only after query has executed successfully
		connection.setAutoCommit(false);
		
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user_data WHERE email=?");
		preparedStatement.setString(1, email);
		preparedStatement.executeUpdate();
		
		//commit the data
		connection.commit();
		
		release(connection);
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
		
		Connection connection = connect();
		
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_data WHERE email=? and password=?");
		preparedStatement.setString(1, userName);
		preparedStatement.setString(2, password);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		resultSet.last();
		
		//return false if the username and password combination does not match
		if(resultSet.getRow() == 0){
			return false;
		}
		
		release(connection);
		
		return true;
	}
	
	public String fetchImageKey(String userName) throws SQLException{
		
		Connection connection = connect();
		
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT image_key FROM user_data WHERE email=?");
		preparedStatement.setString(1, userName);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		String imageKey = null;
		
		while(resultSet.next()){
			imageKey = resultSet.getString(1);
			System.out.println("\n\nImage Key: " + imageKey + "\n\n");
		}
		
		release(connection);
		
		return imageKey;
	}
	
	public String fetchSecurityQuestion(String userName) throws SQLException, InvalidEmailException{
		
		Connection connection = connect();
		
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT security_question FROM user_data WHERE email=?");
		preparedStatement.setString(1, userName);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		String securityQuestion = null;
		
		while(resultSet.next()){
			securityQuestion = resultSet.getString(1);
		}
		
		release(connection);
		
		if(securityQuestion == null){
			throw new InvalidEmailException("Email not registered with us!!");
		}
		
		return securityQuestion;
	}
	
	public boolean verifySecurityAnswer(String userName, String securityanswer) throws SQLException, InvalidSecurityAnswerException{
		
		Connection connection = connect();
		
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT security_answer FROM user_data WHERE email=?");
		preparedStatement.setString(1, userName);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		boolean isAnswerCorrect = false;
		
		while(resultSet.next()){
			if(resultSet.getString(1).equalsIgnoreCase(securityanswer)){
				isAnswerCorrect = true;
			}
		}
		
		if(!isAnswerCorrect){
			throw new InvalidSecurityAnswerException("Security Answer is incorrect!!");
		}
		
		release(connection);
		
		return isAnswerCorrect;
	}
	
	public void updateNewPassword(String userName, String password) throws SQLException{
		
		Connection connection = connect();
		
		connection.setAutoCommit(false);
		
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_data SET password=? WHERE email=?");
		preparedStatement.setString(1, password);
		preparedStatement.setString(2, userName);
		preparedStatement.executeUpdate();
		
		connection.commit();
		
		release(connection);
	}
	
	public void storeRecognitionData(String email, String currentTimeStamp, int recognitionScore) throws SQLException, UnknownHostException {
		Connection connection = connect();
		connection.setAutoCommit(false);
		
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO recognition_data(user_name, time_stamp, server_name, recognition_score) VALUES (?,?,?,?)");
		preparedStatement.setString(1, email);
		preparedStatement.setString(2, currentTimeStamp);
		preparedStatement.setString(3, InetAddress.getLocalHost().getHostName());
		preparedStatement.setInt(4, recognitionScore);
		preparedStatement.executeUpdate();
		
		connection.commit();
		release(connection);
	}
	
	public static void main(String [] args) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		System.out.println(getInstance().isEmailRegistered("shahmanie@gmail.com"));
	}
	
}
