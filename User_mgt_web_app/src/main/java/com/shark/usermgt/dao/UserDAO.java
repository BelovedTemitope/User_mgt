package com.shark.usermgt.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shark.usermgt.model.User;

//data access object
//This DAO class provides CRUD database operations for the table users in the database
public class UserDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/usermgt?allowPublicKeyRetrieval=TRUE&useSSL=FALSE";
	private String jdbcuser = "user";
	private String jdbcpassword = "password";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + " (name, email, country) VALUES " + " (?,?,?);";  //Insert user into schema
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id=?"; //select user by id 
	private static final String SELECT_ALL_USERS = "select * from users"; //select all users
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;"; //delete user 
	private static final String UPDATE_USERS_SQL = "update users set name=?, email=?, country=?, where id=?;"; //update user value
	
	protected Connection getConnection(){
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcuser, jdbcpassword);
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();		}
		return connection;
	}
	
	// insert user
	public void insertUser(User user) throws SQLException {
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)){
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// update user
	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL);){
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.setInt(4, user.getId());
			rowUpdated = preparedStatement.executeUpdate() > 0;
		}
		return rowUpdated;
	}
	
	//select user by id
	public User selectUser(int id) throws SQLException {
		User user = null;
		//Step 1 Establishing a connection
		try(Connection connection = getConnection();
				//Step 2 Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);){
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			//Step 3 Execute the query or execute the query
			ResultSet rs = preparedStatement.executeQuery();
			
			//Process the result set object
			while(rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id,name,email,country);
			}
		}
		return user; 
	}
	
	//select all users
	public List<User> selectAllUsers() throws SQLException {
		List<User> users = new ArrayList<>();
		//Step 1 Establishing a connection
		try(Connection connection = getConnection();
				//Step 2 Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);){
			System.out.println(preparedStatement);
			//Step 3 Execute the query or execute the query
			ResultSet rs = preparedStatement.executeQuery();
			
			//Process the result set object
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id,name,email,country));
			}
		}
		return users; 
	}
	
	//delete user
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL);){
			preparedStatement.setInt(1, id);
			rowDeleted = preparedStatement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
}









