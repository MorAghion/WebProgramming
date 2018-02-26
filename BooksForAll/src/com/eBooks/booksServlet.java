package com.eBooks;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import entities.CBook;
import entities.Consts;

/**
 * Servlet implementation class booksServlet.
 */
//@WebServlet("/booksServlet")

public class booksServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
       
    /**
     * Instantiates a new books servlet.
     * @see HttpServlet#HttpServlet()
     */
    public booksServlet() {
        super();
    }

	/**
	 * Do get.
	 * Checks the age range given in the request. 
	 * if it is '0' Gets all of the books from the DB, and output them in a json format.
	 * else, gets all the books with the requested age range from the DB and output it in a json format.
	 * @param request the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		if(Integer.parseInt(request.getParameter("AgeRange"))==0)
		{
			try {
		    		Context context = new InitialContext();
		    		BasicDataSource ds = (BasicDataSource)context.lookup(
		    		getServletContext().getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
		    		Connection conn = ds.getConnection();
		    		Collection<CBook> booksResult = new ArrayList<CBook>();     	
		    		Statement stmt;
		    		try {
		    				stmt = conn.createStatement();
		    				ResultSet rs = stmt.executeQuery(Consts.SELECT_ALL_BOOKS_STMT);
		    				while (rs.next()){
		    					booksResult.add(new CBook(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getInt(9)));
		    				}
		    				rs.close();
		    				stmt.close();
		    			} catch (SQLException e) {
		    				getServletContext().log("Error while querying for books", e);
		    	    		response.sendError(500);//internal server error
		    			}
		
		    		conn.close();
		    		Gson gson = new Gson();
		        	String bookJsonResult = gson.toJson(booksResult, Consts.BOOKS_COLLECTION);
		        	response.addHeader("Content-Type", "application/json");
		        	PrintWriter writer = response.getWriter();
		        	writer.println(bookJsonResult);
		        	writer.close();
		    	} catch (SQLException | NamingException e) {
		    		getServletContext().log("Error while closing connection", e);
		    		response.sendError(500);//internal server error
		    	}
		}
		else
		{
			try {
	    		Context context = new InitialContext();
	    		BasicDataSource ds = (BasicDataSource)context.lookup(
	    		getServletContext().getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
	    		Connection conn = ds.getConnection();
	    		Collection<CBook> booksResult = new ArrayList<CBook>(); 
	    	
	    			try {
	    				PreparedStatement ps;
	    				ps = conn.prepareStatement(Consts.SELECT_BOOKS_BY_AGERANGE_STMT);
	    				ps.setInt(1, Integer.parseInt(request.getParameter("AgeRange")) );
	    				ResultSet rs = (ResultSet)ps.executeQuery();
	    				while (rs.next())
	    				{
	    					booksResult.add(new CBook(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getInt(9)));
	    				}
	    				rs.close();
	    				ps.close();
	    			} catch (SQLException e) {
	    				getServletContext().log("Error while querying for books", e);
	    	    		response.sendError(500);//internal server error
	    			}
	
	    		conn.close();	    		
	    		Gson gson = new Gson();
	        	String bookJsonResult = gson.toJson(booksResult, Consts.BOOKS_COLLECTION);
	        	response.addHeader("Content-Type", "application/json");
	        	PrintWriter writer = response.getWriter();
	        	writer.println(bookJsonResult);
	        	writer.close();
	    	} catch (SQLException | NamingException e) {
	    		getServletContext().log("Error while closing connection", e);
	    		response.sendError(500);//internal server error
	    	}
			
		}
	}
				
			

}
