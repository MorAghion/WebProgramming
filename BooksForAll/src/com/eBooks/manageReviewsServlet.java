package com.eBooks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import com.google.gson.Gson;
import entities.CBookReviews;
import entities.Consts;

/**
 * Servlet implementation class manageReviewsServlet.
 */

public class manageReviewsServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
       
    /**
     * Instantiates a new manage reviews servlet.
     * @see HttpServlet#HttpServlet()
     */
    public manageReviewsServlet() {
        super();
    }

	/**
	 * Do get.
	 *Gets from the DB all the un approved reviews and response them with a json format. 
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
    		Collection<CBookReviews> unapprovedReviews=new ArrayList<CBookReviews>();
    			try {
    				PreparedStatement ps;
    				
    				ps = conn.prepareStatement(Consts.SELECT_ALL_UNAPREVOED_REVIEWS_STMT);
    				ps.setString(1, "false");
    				ResultSet rs = (ResultSet)ps.executeQuery();
    				while (rs.next())
    				{
    					unapprovedReviews.add(new CBookReviews(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
    				}
    				
    				rs.close();
    				ps.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for books", e);
    	    		response.sendError(500);//internal server error
    			}

    		

    		conn.close();    		
    		Gson gson = new Gson();
        	String bookJsonResult = gson.toJson(unapprovedReviews, Consts.REVIEWS_COLLECTION);
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
	 * Update a review in the DB as "approved" by it's id given in the request.
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try {
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
    		Connection conn = ds.getConnection();
    		try {
    				PreparedStatement ps;
    				ps = conn.prepareStatement(Consts.UPDATE_BOOK_REVIEW_BY_ID_STMT);
    				ps.setString(1, "true");
    				ps.setString(2, request.getParameter("id"));
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
	
	/**
	 * Do delete.
	 * Delete a review in the DB by it's id given in the request.
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try {
			
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
    		Connection conn = ds.getConnection();
    		try {
    				PreparedStatement ps;
    				ps = conn.prepareStatement(Consts.DELETE_REVIEW_BY_ID_STMT);
    				ps.setString(1, request.getParameter("id"));
    				ps.executeUpdate();			
    				
    				ps.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for dlete user", e);
    	    		response.sendError(500);//internal server error
    			}
    		conn.close();
    		
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
		
	}
}
