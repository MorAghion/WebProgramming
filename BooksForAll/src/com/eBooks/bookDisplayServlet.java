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
import entities.CBook;
import entities.Consts;

/**
 * Servlet implementation class bookDisplayServlet.
 */
public class bookDisplayServlet extends HttpServlet 
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
       
    /**
     * Instantiates a new book display servlet.
     * @see HttpServlet#HttpServlet()
     */
    public bookDisplayServlet() {
        super();
    }

	/**
	 * Do get.
	 * Gets the book from the BOOKS table By the name given in the request
	 * Responses with CBook collection in a json format. 
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
    		Collection<CBook> booksResult = new ArrayList<CBook>();     	
    			try 
    			{
    				PreparedStatement ps;
    				ps = conn.prepareStatement(Consts.SELECT_BOOK_BY_NAME_STMT);
    				ps.setString(1, request.getParameter("name"));
    				ResultSet rs = (ResultSet)ps.executeQuery();
    				while (rs.next())
    				{
    					booksResult.add(new CBook(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getInt(9)));
    				}	
    				rs.close();
    				ps.close();
    			} catch (SQLException e) 
    			{
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
	
	/**
	 * Do post.
	 * Check if the user already purchased the book by calling to the "isUsernameAndBookPurchased" function.
	 * if not outputs a massage that he isn't.
	 * if he does purchased the book it checks if he already liked it, by calling isAlreadyLiked function.
	 * and if so sends a response he did, and call the unlikeInDB function.
	 * if he didn't liked it, it calls the likeInDB function.
	 * @param request the HttpServletRequest
	 * @param response the HttpServletResponse
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
							if(!isUsernameAndBookPurchased(request.getParameter("name"), request.getParameter("bookToLike"),conn))
							{
									writer.print("book not purchased");
									
							}
				
							else		//user exists
							{	
								if(isAlreadyLiked(request.getParameter("name"), request.getParameter("bookToLike"),conn)) 
								{
									writer.print("book purchased and already liked");
									unlikeInDB(request.getParameter("name"), request.getParameter("bookToLike"),conn,response);
									
									return;
								}
								else
								{
									writer.print("book purchased and not liked yet");
									likeInDB(request.getParameter("name"), request.getParameter("bookToLike"),conn,response);
									return;
								}
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
	 * Checks if is the user with the given username purchased the given book.(checking the "booksPurchased" column of the user from the USERS table)
	 * @param username the String
	 * @param book the String
	 * @param conn the Connection
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
	
	/**
	 * Checks if  the user with the given user name already liked the given book.(checking the "booksLiked" column of the user from the USERS table)
	 * @param username the String
	 * @param book the String
	 * @param conn the connection.
	 * @return true, if the user already liked the book
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 */
	public boolean isAlreadyLiked( String username, String book, Connection conn) throws SQLException , NamingException
	{
			PreparedStatement ps;
			boolean retValue = false;
			ps = conn.prepareStatement(Consts.SELECT_USER_BY_NAME_STMT);
			ps.setString(1, username);
		
			ResultSet rs = (ResultSet)ps.executeQuery();
			while (rs.next() )
			{
				String[] LikedBooks=rs.getString(13).split(",+");

				for(String Likedbook:LikedBooks)
				{
					if (Likedbook.equals(book)) 
					{
						retValue= true;
					}
					
				}

			}
			rs.close();
			ps.close();
			return retValue;	
	}
	
	/**
	 * Adds a like to the DB by selecting the user with the given user name from the USERS table and adds the book name to the "booksLiked" column of that user    
	 *also, selects the book by the given book name from the BOOKS table and adds the user name to the "likedBy" column of that book, and adds 1 to the "likes" count of that book.
	 * @param username the String
	 * @param book the String
	 * @param conn the connection
	 * @param response the response
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void likeInDB( String username, String book, Connection conn,HttpServletResponse response) throws SQLException , NamingException, IOException
	{
		PreparedStatement ps;
		ps = conn.prepareStatement(Consts.SELECT_USER_BY_NAME_STMT);
		ps.setString(1, username);
	
		ResultSet rs = (ResultSet)ps.executeQuery();
		String LikedBooks="";
		while (rs.next() )
		{
			 LikedBooks=rs.getString(13)+","+book;
		}
		ps = conn.prepareStatement(Consts.UPDATE_LIKED_BOOKS_USERSTABLE_STMT);
		ps.setString(1, LikedBooks);
		ps.setString(2, username);
		ps.executeUpdate();			
		rs.close();
		ps.close();
		
		PreparedStatement ps2;
		
		ps2 = conn.prepareStatement(Consts.SELECT_BOOK_BY_NAME_STMT);
		ps2.setString(1, book);
	
		ResultSet rs2 = (ResultSet)ps2.executeQuery();
		String likedby="";
		String likesAmn="";
		int amount;
		while (rs2.next() )
		{
			if(!rs2.getString(7).equals("") && rs2.getString(7)!=null)
			{
			likedby=rs2.getString(7)+","+username;
			}
			else
			{
				likedby=rs2.getString(7)+username;
			}
			likesAmn=rs2.getString(4);
			
		}		
		ps2 = conn.prepareStatement(Consts.UPDATE_LIKED_BY_BOOKSTABLE_STMT);
		amount=Integer.parseInt(likesAmn)+1;
		ps2.setString(1, likedby);
		ps2.setString(2, Integer.toString(amount));
		ps2.setString(3, book);
		ps2.executeUpdate();		
		rs2.close();
		ps2.close();
		return;
	}
	
	/**
	 * removes a like from the DB, selects the user by the given user name and removes the book name from the "booksLiked" column of that user    
	 *also, selects the book by the given book name and removes the user name from the "likedBy" column of that book, and removes 1 from the "likes" count of that book.
	 * @param username the String
	 * @param book the String
	 * @param conn the connection
	 * @param response the response
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void unlikeInDB( String username, String book, Connection conn,HttpServletResponse response) throws SQLException , NamingException, IOException
	{
		PreparedStatement ps;
		ps = conn.prepareStatement(Consts.SELECT_USER_BY_NAME_STMT);
		ps.setString(1, username);
	
		ResultSet rs = (ResultSet)ps.executeQuery();
		String rsLikedBooks="";
		String toDB="";
		while ( rs.next() )
		{
			rsLikedBooks  = rs.getString(13);
			String[] LikedBooks=rsLikedBooks.split(",+");
			
			for(String Likedbook:LikedBooks)
			{
				if (!Likedbook.equals(book) && !Likedbook.equals("")) 
				{
					if(!toDB.equals(""))
					 toDB+=(","+Likedbook);
					else
					 toDB+=(Likedbook);
				}
			}
			
			
			
			    if (toDB != null && toDB.length() > 0 && toDB.charAt(toDB.length() - 1) == ',') {
			    	toDB = toDB.substring(0, toDB.length() - 1);
			    }
			    
			
		}
		
		
		ps = conn.prepareStatement(Consts.UPDATE_LIKED_BOOKS_USERSTABLE_STMT);
		ps.setString(1, toDB);
		ps.setString(2, username);
		ps.executeUpdate();
		rs.close();
		ps.close();
		PreparedStatement ps2;
		ps2 = conn.prepareStatement(Consts.SELECT_BOOK_BY_NAME_STMT);
		ps2.setString(1, book);
		String newlikedby="";
		ResultSet rs2 = (ResultSet)ps2.executeQuery();
		String likesAmn="";
		int amount;
		while (rs2.next() )
		{
			String oldLikedBy=rs2.getString(7);
			String[] LikedBy=oldLikedBy.split(",+");
			likesAmn=rs2.getString(4);
			for(String user:LikedBy)
			{
				if (!user.equals(username)  && !user.equals("")) 
				{
					newlikedby+=(user+",");
				}
			}
		}
		   if (newlikedby != null && newlikedby.length() > 0 && newlikedby.charAt(newlikedby.length() - 1) == ',') {
			   newlikedby = newlikedby.substring(0, newlikedby.length() - 1);
		    }
		ps2 = conn.prepareStatement(Consts.UPDATE_LIKED_BY_BOOKSTABLE_STMT);
		amount=Integer.parseInt(likesAmn)-1;
		ps2.setString(1, newlikedby);
		ps2.setString(2, Integer.toString(amount));
		ps2.setString(3, book);
		ps2.executeUpdate();		
		rs2.close();
		ps2.close();
		return;
	}
	
	
	
	
	/**
	 * Do put.
	 * Checks if the user by the given user name already liked the book by the given book name by calling the isAlreadyLiked function.
	 * if so it responses with the string "red", otherwise with the string "black". 
	 * @param request the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
 	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 	 */
 	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
					if((isAlreadyLiked(request.getParameter("username"), request.getParameter("bookname"),conn)))
					{
						writer.print("red");
					}
					else 
					{
						writer.print("black");
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
}

