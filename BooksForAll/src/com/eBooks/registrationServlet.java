package com.eBooks;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entities.CUser;
import entities.Consts;



/**
 * Servlet implementation class registrationServlet.
 */
//@WebServlet("/registrationServlet")

public class registrationServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
       
    /**
     * Instantiates a new registration servlet.
     * @see HttpServlet#HttpServlet()
     */
    public registrationServlet() {
        super();
    }

	/**
	 * Do post.
	 * Checks if all the registration parameters are valid and if so Inserting a new user to the USERS table.
	 * otherwise, responding with an appropriate massage. 
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		try
		{
			PrintWriter writer = response.getWriter();
			Context context;
	
			try 
				{
					context = new InitialContext();
					BasicDataSource ds = (BasicDataSource)context.lookup(
					getServletContext().getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
					Connection conn = ds.getConnection();
					PreparedStatement ps = null; 
					Gson gson = new GsonBuilder().create();
					CUser user = gson.fromJson(request.getReader(), CUser.class);
					try
						{
						if(user.getPhoto() == null || user.getPhoto().equals("")|| user.getPhoto()==""){
							user.setPhoto("noPic.jpg");
						}
							if(isUsernameExists(user.getUserName(), conn))
							{
									writer.print("User name exists");
									return;
							}
							if(user.getUserName()==null ||user.getNickname()==null || user.getPassword()==null || user.getEmail()==null || user.getPhone()==null || user.getCity()==null || user.getStreet()==null || user.getNumber()==null || user.getZipcode()==null)
							{
								writer.print("Some required feilds are empty");
							}
							else 			// insert user
							{	
								if(user.getUserName().equals("") ||!(user.getUserName().matches("[a-zA-Z0-9]+") &&user.getUserName().length()<=10 && user.getUserName().length()>0) )
								{
									writer.print("User name invalid");		
									return;
								}
								else if(user.getEmail().equals("")||!user.getEmail().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))
								{
									writer.print("Email is invalid");		
									return;
								}
								else if(user.getPhone().equals("") ||!(user.getPhone().matches("[0-9]+") &&(user.getPhone().length()==9||user.getPhone().length()==10)) )
								{
									writer.print("Phone is invalid");		
									return;
								}
								else if(user.getCity().equals("") ||!(user.getCity().matches("[a-zA-z ]+") &&user.getCity().length()>=3))
								{
									writer.print("City is invalid");
									return;
								}
								else if(user.getStreet().equals("") ||!(user.getStreet().matches("[a-zA-z ]+") &&user.getStreet().length()>=3))
								{
									writer.print("Street is invalid");	
									return;
								}
								else if(user.getNumber().equals("")|| (!user.getNumber().matches("[0-9]+") ))
								{
									writer.print("House number is invalid");		
									return;
								}
								else if(user.getZipcode().equals("")||!(user.getZipcode().matches("[0-9]+") &&user.getZipcode().length()==7) )
								{
									writer.print("Zipcode is invalid");		
									return;
								}
								else if(user.getPassword().equals("")||!(user.getPassword().length()<=8 && user.getPassword().length()>0 ))
								{
									writer.print("Password is invalid");
									return;
								}
								else if(user.getNickname().equals("")||!(user.getNickname().length()<=20 && user.getNickname().length()>0 ))
								{
									writer.print("Nickname is invalid");
									return;
								}
								
								else if((!user.getDescription().equals("")&&!(user.getDescription()==null)) &&!(user.getDescription().length()<=50))
								{
									writer.print("description is invalid");	
									return;
								}
								else if((!user.getPhoto().equals("")&& !(user.getPhoto()==null)) &&!(user.getPhoto().length()<=300))
								{
									writer.print("Photo url is invalid");	
									return;
								}
							else 			// insert user
							{	
								ps = conn.prepareStatement(Consts.INSERT_USER_STMT);
								ps.setString(1,user.getUserName());
								ps.setString(2,user.getEmail());
								ps.setString(3,user.getPhone());
								ps.setString(4,user.getCity());
								ps.setString(5,user.getStreet());
								ps.setString(6,user.getNumber());
								ps.setString(7,user.getZipcode());
								ps.setString(8,user.getPassword());
								ps.setString(9,user.getNickname());
								ps.setString(10,user.getDescription());
								ps.setString(11,user.getPhoto());
								ps.setString(12,"");
								ps.setString(13,"");
								ps.setString(14,"No");
								ps.executeUpdate();
								Cookie loginCookie = new Cookie("username", user.getUserName());
					            loginCookie.setMaxAge(48 * 60 * 60);
					            response.addCookie(loginCookie);
								writer.print("registration succeeded");
								conn.commit();
								user.setUsername(null);
								user.setPassword(null);
								request.getSession().setAttribute("user", user);
								
							}
							ps.close();
							
						}
						
						}
					catch (SQLException | NamingException e) 
						{
							getServletContext().log("Error while closing connection", e);
							response.sendError(500);// internal server error
						}
				
				finally
					{
						writer.close();
						conn.close();
					}	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
		
	catch (NamingException e) 
	{
		getServletContext().log("Error while closing connection", e);
		response.sendError(500);		//internal server error
	}
}
	
	/**
	 * Checks if the user name already exists in the DB.
	 * @param username the String
	 * @param conn the connection
	 * @return true, if the user name exists
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 */
	public boolean isUsernameExists( String username, Connection conn) throws SQLException , NamingException
	{
			PreparedStatement ps;
			boolean retValue = false;
			ps = conn.prepareStatement(Consts.SELECT_USER_BY_NAME_STMT);

			ps.setString(1, username);
			ResultSet rs = (ResultSet)ps.executeQuery();
			if (rs.next() ) {
			    retValue = true;
			}
			rs.close();
			ps.close();
			return retValue;
	}
}

