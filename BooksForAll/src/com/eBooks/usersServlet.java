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
import entities.CUser;
import entities.Consts;

/**
 * Servlet implementation class usersServlet.
 */
public class usersServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
       
    /**
     * Instantiates a new users servlet.
     * @see HttpServlet#HttpServlet()
     */
    public usersServlet() {
        super();
    }

	/**
	 * Do get.
	 * Getting from the USERS table all the non admin users.
	 * Responding with a collection of CUser
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    		getServletContext().getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
    		Connection conn = ds.getConnection();
    		Collection<CUser> myusers=new ArrayList<CUser>();
    			try {
    				PreparedStatement ps;    				
    				ps = conn.prepareStatement(Consts.SELECT_ALL_NOTADMIN_USERS_STMT);
    				ps.setString(1, "No");
    				ResultSet rs = (ResultSet)ps.executeQuery();
    				while (rs.next())
    				{
    					myusers.add(new CUser(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14)));
    				}
    				
    				rs.close();
    				ps.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for books", e);
    	    		response.sendError(500);//internal server error
    			}
    		conn.close();    		
    		Gson gson = new Gson();
        	String bookJsonResult = gson.toJson(myusers, Consts.USERS_COLLECTION);
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
	 * Do delete.
	 * Delete the user with the given user name from the USERS table
	 * Delete all the reviews of that user from the REVIEWS table
	 * Update  the "purchsedBy" column of all of the books in the BOOKS table that the user purchased, by calling the unPurchasebook function.  
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try {
			PrintWriter writer = response.getWriter();
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    		getServletContext().getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
    		Connection conn = ds.getConnection();
    		try {
    				PreparedStatement ps;
    				unlikebooks(request.getParameter("name"),conn,response);
    				ps = conn.prepareStatement(Consts.DELETE_USER_BY_NAME_STMT);
    				ps.setString(1, request.getParameter("name"));
    				ps.executeUpdate();			
    				writer.print("DELETE SUCCEEDED");
    	        	writer.close();    				
    				ps.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for dlete user", e);
    	    		response.sendError(500);//internal server error
    			}

    			try {
    				PreparedStatement ps;
    				
    				ps = conn.prepareStatement(Consts.DELETE_REVIEW_BY_USERNAME_STMT);
    				ps.setString(1, request.getParameter("name"));
    				ps.executeUpdate();			
    				ps.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for dlete user", e);
    	    		response.sendError(500);//internal server error
    			}
    			Statement stmt2;
    			try {
    				stmt2 = conn.createStatement();
    				ResultSet rs = stmt2.executeQuery(Consts.SELECT_ALL_BOOKS_STMT);
    				while (rs.next())
    				{
    					unPurchasebook(request.getParameter("name"),rs.getString(1),conn,response);
    				}
    				rs.close();
    				stmt2.close();
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
	 * Removes the given user name from the "purchsedBy" column of the book with the given book name. 
	 * @param username the user name
	 * @param book the book
	 * @param conn the connection 
	 * @param response the response
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void unPurchasebook( String username,String book,Connection conn,HttpServletResponse response) throws SQLException , NamingException, IOException
	{
		PreparedStatement ps2 = conn.prepareStatement(Consts.SELECT_BOOK_BY_NAME_STMT);
		ps2.setString(1, book);
		String newpurchsedby="";
		ResultSet rs2 = (ResultSet)ps2.executeQuery();
		while (rs2.next() )
		{
			String oldpurchasedBy=rs2.getString(8);
			String[] LikedBy=oldpurchasedBy.split(",+");
			for(String user:LikedBy)
			{
				if (!user.equals(username)  && !user.equals("")) 
				{
					newpurchsedby+=(user+",");
				}
			}
			if (newpurchsedby != null && newpurchsedby.length() > 0 && newpurchsedby.charAt(newpurchsedby.length() - 1) == ',') {
				newpurchsedby = newpurchsedby.substring(0, newpurchsedby.length() - 1);
			    }
		}			
		ps2 = conn.prepareStatement(Consts.UPDATE_PURCHASED_BY_BOOKSTABLE_STMT);
		ps2.setString(1, newpurchsedby);
		ps2.setString(2, book);
		ps2.executeUpdate();		
		rs2.close();
		ps2.close();
	}
	
	/**
	 * Unlikebooks.
	 * Select all books from the BOOKS table and for each call the "Unlikebook" function which will remove the given user name from the "likedBY" column of the book.  
	 * @param username the user name
	 * @param conn the connection
	 * @param response the response
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void unlikebooks( String username, Connection conn,HttpServletResponse response) throws SQLException , NamingException, IOException
	{
    			Statement stmt;
    			try {
    				stmt = conn.createStatement();
    				ResultSet rs = stmt.executeQuery(Consts.SELECT_ALL_BOOKS_STMT);
    				while (rs.next()){
    					unlikebook(username,rs.getString(1),conn,response);
    				}
    				rs.close();
    				stmt.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for books", e);
    	    		response.sendError(500);//internal server error
    			}
	}
	
	
	
	/**
	 * Selects the book by the given book name and removes the given user name from the "likedby" column of that book. 
	 * @param username the user name
	 * @param book the book name
	 * @param conn the connection
	 * @param response the response
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void unlikebook( String username, String book, Connection conn,HttpServletResponse response) throws SQLException , NamingException, IOException
	{

		PreparedStatement ps2;
		ps2 = conn.prepareStatement(Consts.SELECT_BOOK_BY_NAME_STMT);
		ps2.setString(1, book);
		String newlikedby="";
		ResultSet rs2 = (ResultSet)ps2.executeQuery();
		String likesAmn="";
		int amount=0;
		while (rs2.next() )
		{
			likesAmn=rs2.getString(4);
			amount=Integer.parseInt(likesAmn);
			String oldLikedBy=rs2.getString(7);
			String[] LikedBy=oldLikedBy.split(",+");

			for(String user:LikedBy)
			{
				if (!user.equals(username)   && !user.equals("")) 
				{
					newlikedby+=(user+",");
				}
				else if( user.equals(username))
				{
					amount=Integer.parseInt(likesAmn)-1;
				}
			}
		}
		
		if (newlikedby != null && newlikedby.length() > 0 && newlikedby.charAt(newlikedby.length() - 1) == ',') {
			   newlikedby = newlikedby.substring(0, newlikedby.length() - 1);
		    }
		
		ps2 = conn.prepareStatement(Consts.UPDATE_LIKED_BY_BOOKSTABLE_STMT);
		ps2.setString(1, newlikedby);
		ps2.setString(2, Integer.toString(amount));
		ps2.setString(3, book);
		ps2.executeUpdate();		
		rs2.close();
		ps2.close();

	}
}
