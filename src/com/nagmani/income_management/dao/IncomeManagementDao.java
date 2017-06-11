package com.nagmani.income_management.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Properties;



public class IncomeManagementDao {

	private Connection tipDataTableConn= null;
	private PreparedStatement myStmt= null;
	private ResultSet rs;
	
	public IncomeManagementDao() throws FileNotFoundException, IOException, SQLException{
		Properties props= new Properties();
		InputStream input= null;
		try{
		input = getClass().getClassLoader().getResourceAsStream("IncomeManagementDao.properties");
	 
		props.load(input);
		//    props.load(new FileInputStream(""));
		
		String user= props.getProperty("user");
		String password= props.getProperty("password");
		String dbURL= props.getProperty("dbUrl");
		System.out.println(user+ " " + " " +password+ " "+dbURL);
		tipDataTableConn= DriverManager.getConnection(dbURL,user,password);
		}	finally  {
			if (input !=null){
				try{
					input.close();
				}catch (IOException e) {
					e.printStackTrace();
					
				}
			}
		}
	} 
	
	public void	insertIntoDateTable(int tip_amount, String work_Date){
		
		try{
			String sql = "insert into tip (tip_amount,work_date) values (?,?)";
			myStmt= tipDataTableConn.prepareStatement(sql);
			myStmt.setInt(1, tip_amount);
			myStmt.setString(2, work_Date);
			myStmt.execute();
			
			
		}catch (Exception e){
			
		}
		
		
		
	}
	
	public int getTotalTipAmount() {
		String sql = "select * from tip";
		
	
		int total_tip_amount= 0;
		try {
			myStmt= tipDataTableConn.prepareStatement(sql);
			rs= myStmt.executeQuery();
			while (rs.next()){
				rs.getInt("id");
				int tip_amount= rs.getInt("tip_amount");
				rs.getTimestamp("work_date");
				 total_tip_amount += tip_amount;
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total_tip_amount;

		
		
	}
	
	public void	insertIntoHomeIncomeTable(int income_amount,String income_catagory, String work_Date){
		
		try{
			String sql = "insert into home_income (income_amount,income_catagory,work_date) values (?,?,?)";
			myStmt= tipDataTableConn.prepareStatement(sql);
			myStmt.setInt(1, income_amount);
			myStmt.setString(2, income_catagory);
			myStmt.setString(3, work_Date);
			myStmt.execute();
			
			
		}catch (Exception e){
			
		}
		
	}
	
	public int getTotalHomeIncomeAmount() {
		String sql = "select * from home_income";
		
	
		int total_home_income_amount= 0;
		try {
			myStmt= tipDataTableConn.prepareStatement(sql);
			rs= myStmt.executeQuery();
			while (rs.next()){
				rs.getInt("id");
				int home_amount= rs.getInt("income_amount");
				rs.getString("income_catagory");
				rs.getTimestamp("work_date");
				
				total_home_income_amount += home_amount;
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total_home_income_amount;

		
		
	}
	public void closeConnection(){
		try {
			tipDataTableConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
