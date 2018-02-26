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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import com.google.gson.Gson;
import entities.CBookReviews;
import entities.Consts;

/**
 * Servlet implementation class reviewsServlet.
 */

public class reviewsServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
       
    /**
     * Instantiates a new reviews servlet.
     * @see HttpServlet#HttpServlet()
     */
    public reviewsServlet() {
        super();
    }

	/**
	 * Do get.
	 * Gets all approved reviews for the book with the given book name in the request from the REVIEWS table.
	 * Responding with a collection of reviews
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
    		ArrayList<CBookReviews> myreviews=new ArrayList<CBookReviews>(); 
    			try {
    				PreparedStatement ps;
    				
    				ps = conn.prepareStatement(Consts.SELECT_REVIEWS_BY_BOOKNAME);
    				ps.setString(1, request.getParameter("name"));
    				ps.setString(2, "true");
    				ResultSet rs = (ResultSet)ps.executeQuery();
    				while (rs.next())
    				{
    					myreviews.add(new CBookReviews(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
    				}
    				
    				rs.close();
    				ps.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for books", e);
    	    		response.sendError(500);//internal server error
    			}
    		conn.close();		
    		Gson gson = new Gson();
        	String bookJsonResult = gson.toJson(myreviews, Consts.REVIEWS_COLLECTION);
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
	 * Checks if the the user with the given user name from the request purchased the book with the given book name by calling to isUsernameAndBookPurchased function.
	 * responses with an appropriate massage
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
					try
						{
							
							if(!isUsernameAndBookPurchased(request.getParameter("name"), request.getParameter("book"),conn))
							{
								
									writer.print("book not purchased");
									
							}
				
							else		//user exists
							{	
								writer.print("book purchased");

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
	 * Do put.
	 * Checking the id of the last reviews in the REVIEWS table, increase it by 1.
	 * Inserts a new Review with the new id and all other given parameters in the request to the REVIEWS table.
	 * Responding with the proper massage. 
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		try
		{
			PrintWriter writer = response.getWriter();
			Context context;
			if(request.getParameter("review")!="" && request.getParameter("review")!=null)
			{	
			try 
				{
					context = new InitialContext();
					BasicDataSource ds = (BasicDataSource)context.lookup(
					getServletContext().getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
					Connection conn = ds.getConnection();
					PreparedStatement ps = null; 
					try
						{
						if(request.getParameter("review").length()>1000)
						{
							writer.print("review is to long");
						}
							PreparedStatement ps2=null;
							ps2=conn.prepareStatement(Consts.SELECT_ALL_REVIEWS);
							ResultSet rs = (ResultSet)ps2.executeQuery();
							int newid=0;
		    				while (rs.next())
		    				{
		    					newid=rs.getInt(1);
		    				}
							
		    				newid+=1;
								ps = conn.prepareStatement(Consts.INSERT_BOOK_REVIEWS_STMT);
								ps.setInt(1,newid);
								ps.setString(2,request.getParameter("username"));
								ps.setString(3,request.getParameter("bookname"));
								ps.setString(4,request.getParameter("review"));
								ps.setString(5,request.getParameter("approved"));
								ps.executeUpdate();
								conn.commit();
								writer.print("review submitted to DB");
							ps.close();
							ps2.close();
						}
					catch (SQLException e) 
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
			else 	writer.print("review cannot be empty");

	}
		
	catch (NamingException e) 
	{
		getServletContext().log("Error while closing connection", e);
		response.sendError(500);		//internal server error
	}
		
	}

		
	
	
	
	/*----------------------------------------------------------UTILITY FUNCTIONS----------------------------------------------------*/
	
	/**
	 * Checks if the user with the given user name purchased the book with the given book name.
	 * @param username the user name
	 * @param book the book name
	 * @param conn the connection
	 * @return true, if the user purchased the book.
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 */
	public boolean isUsernameAndBookPurchased( String username, String book, Connection conn) throws SQLException , NamingException
	{

			PreparedStatement ps;
			boolean retValue = false;
			ps = conn.prepareStatement(Consts.SELECT_USER_BY_NAME_STMT);
			ps.setString(1, username);	
			ResultSet rs = (ResultSet)ps.executeQuery();
			while (rs.next() )
			{
				String[] PurchasedBooks=rs.getString(12).split(",+");

				for(String Purchasedbook:PurchasedBooks)
				{
					if (Purchasedbook.equals(book)) 
					{
						retValue= true;
					}
				}
			}
			rs.close();
			ps.close();
			return retValue;	
	}

}
