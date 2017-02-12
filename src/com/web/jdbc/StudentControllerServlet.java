package com.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDBUtil studentDbUtil; 
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		try {
			//create studentDBUtil and pass it to connection pool
			studentDbUtil = new StudentDBUtil(dataSource);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			//Read the command parameter
			String theCommand = request.getParameter("command");
			
			//if the command is missing then defult to listing student
			if (theCommand==null) {
				theCommand = "LIST";
			}
			
			//route to the appropiate method
			switch (theCommand) {
			case "LIST":
				liststudents(request, response);
				break;
			case "ADD":
				addStudent(request, response);
				break;
			case "LOAD":
				loadStudent(request, response);
				break;
			case "UPDATE":
				updateStudent(request, response);
				break;
			case "DELETE":
				deleteStudent(request, response);
				break;
			default:
				liststudents(request, response);
				
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			// read student id from form data
			String theStudentId = request.getParameter("studentId");
			
			// delete student from database
			studentDbUtil.deleteStudent(theStudentId);
			
			// send them back to "list students" page
			liststudents(request, response);
		}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//read the student info from the form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");	
		
		//create new student object
		Student theStudent = new Student(id, firstName, lastName, email);
		
		//perform update on database
		studentDbUtil.updateStudent(theStudent);
		
		//send them back into list-students page
		liststudents(request, response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {

			// read student id from form data
			String theStudentId = request.getParameter("studentId");
			
			// get student from database (db util)
			Student theStudent = studentDbUtil.getStudent(theStudentId);
			
			// place student in the request attribute
			request.setAttribute("THE_STUDENT", theStudent);
			
			// send to jsp page: update-student-form.jsp
			RequestDispatcher dispatcher = 
					request.getRequestDispatcher("/update-student-form.jsp");
			dispatcher.forward(request, response);		
		}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");		
		
		// create a new student object
		Student theStudent = new Student(firstName, lastName, email);
		
		// add the student to the database
		studentDbUtil.addStudent(theStudent);
				
		// send back to main page (the student list)
		liststudents(request, response);
	}

	private void liststudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// Getting student list from the db
		List<Student> students = studentDbUtil.getStudents();
		
		//add students to reuqest
		request.setAttribute("STUDENT_LIST", students);
		
		//send the request to jsp page
		RequestDispatcher disPatcher = request.getRequestDispatcher("/list-students.jsp");
		disPatcher.forward(request, response);
		
	}

}
