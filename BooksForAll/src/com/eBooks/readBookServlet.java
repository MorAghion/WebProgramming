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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import com.google.gson.Gson;
import entities.CPaging;
import entities.Consts;

/**
 * Servlet implementation class readBookServlet.
 */

public class readBookServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
       
    /**
     * Instantiates a new read book servlet.
     * @see HttpServlet#HttpServlet()
     */
    public readBookServlet() {
        super();
    }

	/**
	 * Do get.
	 * Selecting the Page from the PAGING table by the user and book name given in the request
	 * Responding with the CPging as a json format. 
	 * @param request the request
	 * @param response the response
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
			CPaging page=new CPaging();
    			try {
    				PreparedStatement ps;
    				
    				ps = conn.prepareStatement(Consts.SELECT_PAGING_BY_NAME_AND_BOOKNAME);
    				ps.setString(1, request.getParameter("username"));
    				ps.setString(2, request.getParameter("bookname"));
    				ResultSet rs = (ResultSet)ps.executeQuery();
    				while (rs.next())
    				{
    					page=(new CPaging(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4)));
    				}
    				rs.close();
    				ps.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for books", e);
    	    		response.sendError(500);//internal server error
    			}
    			
    		conn.close();
    		Gson gson = new Gson();
        	String bookJsonResult = gson.toJson(page, Consts.CPAGING);
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
	 * Updating the row in the PAGING table where the user name and book name given in the request and sets the given page number.
	 * @param request the HttpServletRequest
	 * @param response the HttpServletResponse
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
    				
    				ps = conn.prepareStatement(Consts.UPDATE_PAGING_BY_NAME_AND_BOOKNAME_STMT);
    				ps.setInt(1, Integer.parseInt(request.getParameter("page")));
    				ps.setString(2, request.getParameter("username"));
    				ps.setString(3, request.getParameter("bookname"));
    				ps.executeUpdate();
    				//rs.close();
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
