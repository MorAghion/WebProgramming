package com.eBooks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import com.google.gson.Gson;
import entities.CHelpMe;
import entities.Consts;

/**
 * Servlet implementation class aminHelpMeServlet
 */

public class adminHelpMeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public adminHelpMeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Do get.
	 * Gets from the DB all the unanswered helpMe and response them with a json format. 
	 * @param request the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			try {
	    		Context context = new InitialContext();
	    		BasicDataSource ds = (BasicDataSource)context.lookup(
	    		getServletContext().getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
	    		Connection conn = ds.getConnection();
	    		ArrayList<CHelpMe> myhelpme=new ArrayList<CHelpMe>(); 
	    		try {
	    				PreparedStatement ps;
	    				ps = conn.prepareStatement(Consts.SELECT_UNHELPED_HELPME);
	    				ps.setString(1,"false");
	    				ResultSet rs = (ResultSet)ps.executeQuery();

	    				while (rs.next())
	    				{
	    					myhelpme.add(new CHelpMe(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
	    				}
	    				rs.close();
	    				ps.close();
	    			} catch (SQLException e) {
	    				getServletContext().log("Error while querying for books", e);
	    	    		response.sendError(500);//internal server error
	    			}
	    		conn.close();		
	    		Gson gson = new Gson();
	        	String bookJsonResult = gson.toJson(myhelpme, Consts.HELPME_COLLECTION);
	        	response.addHeader("Content-Type", "application/json");
	        	PrintWriter writer = response.getWriter();
	        	writer.println(bookJsonResult);
	        	writer.close();
	    	} catch (SQLException | NamingException e) {
	    		getServletContext().log("Error while closing connection", e);
	    		response.sendError(500);//internal server error
	    	}
	}

		/**
		 * Do post.
		 * Update a HelpMe in the DB as with the admin answer by it's id given in the request.
		 * @param request the request
		 * @param response the response
		 * @throws ServletException the servlet exception
		 * @throws IOException Signals that an I/O exception has occurred.
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 **/
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
		{
			try {
				PrintWriter writer = response.getWriter();
	    		Context context = new InitialContext();
	    		BasicDataSource ds = (BasicDataSource)context.lookup(
	    				getServletContext().getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
	    		Connection conn = ds.getConnection();
	    		try {
	    			if(request.getParameter("myResponse").length()>1000)
					{
						writer.print("Your response is too long");
					}
	    				PreparedStatement ps;
	    				ps = conn.prepareStatement(Consts.UPDATE_HELPME_BY_ID_STMT);
	    				ps.setString(1, request.getParameter("myResponse"));
	    				ps.setString(2, "true");
	    				ps.setString(3, request.getParameter("id"));
	    				
	    				ps.executeUpdate();
	    				ps.close();
	    			} catch (SQLException e) {
	    				getServletContext().log("Error while querying for books", e);
	    	    		response.sendError(500);//internal server error
	    			}
	    		conn.close();
	    	} catch (SQLException | NamingException e) {
	    		getServletContext().log("Error while closing connection", e);
	    		response.sendError(500);//internal server error
	    	}
		}
		
}
