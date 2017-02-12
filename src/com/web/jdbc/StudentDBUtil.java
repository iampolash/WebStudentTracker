package com.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDBUtil {

	private DataSource dataSource;

	public StudentDBUtil(DataSource thedataSource) {
		
		dataSource = thedataSource;
	}
	
	public List<Student> getStudents () throws Exception{
		
		List<Student> students=new ArrayList<>();
		
		Connection myCon=null;
		Statement myStmt=null;
		ResultSet myRs=null;
		
		try {
			//get connection
			myCon = dataSource.getConnection();
			//create sql statement
			String sql= "select * from student";
			myStmt = myCon.createStatement();
			//execute query
			myRs = myStmt.executeQuery(sql);
			//process resultset
			while (myRs.next()) {
				
				//Retrieve data from result set
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//Create new Student object
				Student tempStudent = new Student(id,firstName,lastName,email);
				
				//Add it to student list
				students.add(tempStudent);
			}
			return students;
			
		} finally {
			//close jdbc objects
			close(myCon,myStmt,myRs);
				
			}
		}
	private void close(Connection myCon, Statement myStmt, ResultSet myRs) {
		try {
			if (myCon!=null) {
				myCon.close();
			}
			if(myRs!=null){
				myRs.close();
			}
			if (myStmt!=null) {
				myStmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void addStudent(Student theStudent) throws Exception {
		
		Connection myCon=null;
		PreparedStatement myStmt=null;
		
		try {
			// Get the DB Connection
			myCon = dataSource.getConnection();
			
			// Create SQl to Insert
			String sql="insert into student"
					+"(first_name,last_name,email)"
					+ " values(?,?,?)";
			myStmt = myCon.prepareStatement(sql);
			
			//set the parameter value to the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			
			//excute the insert sql
			myStmt.execute();
			
		} finally {
			//clean the jdbc object
			close(myCon, myStmt, null);
		}
	}

	public Student getStudent(String theStudentId) throws Exception {

		Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			// convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			// get connection to database
			myConn = dataSource.getConnection();
			
			// create sql to get selected student
			String sql = "select * from student where id=?";
			
			// create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, studentId);
			
			// execute statement
			myRs = myStmt.executeQuery();
			
			// retrieve data from result set row
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				// use the studentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find student id: " + studentId);
			}				
			
			return theStudent;
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

public void updateStudent(Student theStudent) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// get db connection
			myConn = dataSource.getConnection();
			
			// create SQL update statement
			String sql = "update student "
						+ "set first_name=?, last_name=?, email=? "
						+ "where id=?";
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			// execute SQL statement
			myStmt.execute();
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

public void deleteStudent(String theStudentId) throws Exception {

	Connection myConn = null;
	PreparedStatement myStmt = null;
	
	try {
		// convert student id to int
		int studentId = Integer.parseInt(theStudentId);
		
		// get connection to database
		myConn = dataSource.getConnection();
		
		// create sql to delete student
		String sql = "delete from student where id=?";
		
		// prepare statement
		myStmt = myConn.prepareStatement(sql);
		
		// set params
		myStmt.setInt(1, studentId);
		
		// execute sql statement
		myStmt.execute();
	}
	finally {
		// clean up JDBC code
		close(myConn, myStmt, null);
	}	
}
}