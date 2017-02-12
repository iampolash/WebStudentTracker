package com.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/TestServelt")
public class TestServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//Define datasource/datapool  for resource injection
	@Resource(name="jdbc/web_student_tracker")
	private DataSource datasource;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Step 1: setting up the printwriter
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		//Step 2: get a connection to the database
		Connection myConn= null;
		Statement myStmt = null;
		ResultSet rs = null;
		
		try {
			myConn = datasource.getConnection();
			//step 3: Create a SQL Statement
			String sql ="select * from student";
			myStmt = myConn.createStatement();
			
			//Step 4: Execute SQL Query
			rs = myStmt.executeQuery(sql);
			
			//Step 5: process the resultset
			while (rs.next()){
				String email = rs.getString("email");
				out.println(email);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
