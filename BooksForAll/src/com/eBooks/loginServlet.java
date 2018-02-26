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
import entities.Consts;

/**
 * Servlet implementation class loginServlet.
 */
public class loginServlet extends HttpServlet 
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
       
    /**
     * Instantiates a new login servlet.
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
        super();
    }

	/**
	 * Do post.
	 * Checks by the given parameters in the request if there is a user name with that password in the db.
	 * If there isn't responses with a appropriate massage.
	 *ï If there is, checks if that user is a administrator and responses with the appropriate massage.  
	 * @param request the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
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
					
					try
						{
							
							if(request.getParameter("username")==null ||request.getParameter("password")==null || !isUsernameAndPasswordMatch(request.getParameter("username"), request.getParameter("password"),conn))
							{
									writer.print("Incorrect user name or password, please try again");
							}
				
							else		//user exists
							{	
								
								if(isUsernameAdmin(request.getParameter("username"), request.getParameter("password"),conn))
								{
									writer.print("isAdmin");
								}
								else 
								{
									writer.print("User name exists");
								}
								
								Cookie loginCookie = new Cookie("username", request.getParameter("username"));
					            loginCookie.setMaxAge(48 * 60 * 60);
					            response.addCookie(loginCookie);
								
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
	 * Checks by the given parameters if there is a user name with that password in the db.
	 * @param name the String
	 * @param password the String
	 * @param conn the connection
	 * @return true, if is user name and password match
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 */
	public boolean isUsernameAndPasswordMatch( String name, String password, Connection conn) throws SQLException , NamingException
	{
			PreparedStatement ps;
			boolean retValue = false;
			ps = conn.prepareStatement(Consts.SELECT_USER_BY_NAME_AND_PASSWORD_STMT);

			ps.setString(1, name);
			ps.setString(2,  password);
			ResultSet rs = (ResultSet)ps.executeQuery();
			
			if (rs.next() ) {
			    retValue = true;
			}
			rs.close();
			ps.close();
			return retValue;
		
	}
	

	/**
	 * Checks if the user with the given user name is an administrator.
	 * @param name the String
	 * @param password the String
	 * @param conn the connection
	 * @return true, if the user name is admin.
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 */
	public boolean isUsernameAdmin( String name, String password, Connection conn) throws SQLException , NamingException
	{
			PreparedStatement ps;
			ps = conn.prepareStatement(Consts.SELECT_USER_BY_NAME_AND_PASSWORD_STMT);

			ps.setString(1, name);
			ps.setString(2,  password);
			ResultSet rs = (ResultSet)ps.executeQuery();
			String Isadmin="";
			if (rs.next() ) 
			{
				Isadmin=rs.getString(14);
			}
			rs.close();
			ps.close();
			if(Isadmin.equals("No"))
			{
				return false;
			}
			else return true;
			
		
	}
	
	/**
	 * Do delete.
	 * Delete the user name cookie.  
	 * @param request the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	
        Cookie loginCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    loginCookie = cookie;
                    break;
                }
            }
        }
        if (loginCookie != null) {
            loginCookie.setMaxAge(0);
            response.addCookie(loginCookie);
        }
	
	}
}
