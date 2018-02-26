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
 * Servlet implementation class myBooksServlet.
 */

public class myBooksServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
       
    /**
     * Instantiates a new my books servlet.
     * @see HttpServlet#HttpServlet()
     */
    public myBooksServlet() {
        super();
    }

	/**
	 * Do get.
	 * Gets all the books purchased by the user with the given user name.
	 * responses with a CBook collection of all that books.
	 * @param request the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		try 
		{
		Context context = new InitialContext();
		BasicDataSource ds = (BasicDataSource)context.lookup(
		getServletContext().getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
		Connection conn = ds.getConnection();
		Collection<CBook> booksResult = new ArrayList<CBook>(); 
			try {
				PreparedStatement ps;
				ps = conn.prepareStatement(Consts.SELECT_USER_BY_NAME_STMT);
				ps.setString(1, request.getParameter("name"));
				ResultSet rs = (ResultSet)ps.executeQuery();
				String[]MyBooks=new String[10];
				while (rs.next())
				{
					MyBooks= getAllPurchasedBooks(rs.getString(12));
				}
				
				for (String Mybook : MyBooks) 
				{
					PreparedStatement ps2;
					ps2 = conn.prepareStatement(Consts.SELECT_BOOK_BY_NAME_STMT);
					ps2.setString(1,Mybook);
					ResultSet rs2 = (ResultSet)ps2.executeQuery();
					while (rs2.next())
					{
						booksResult.add(new CBook(rs2.getString(1),rs2.getString(2),rs2.getString(3),rs2.getString(4),rs2.getString(5),rs2.getString(6), rs2.getString(7),rs2.getString(8),rs2.getInt(9)));
					}
					rs2.close();
					ps2.close();
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
    	writer.print(bookJsonResult);
    	writer.close();
	} catch (SQLException | NamingException e) {
		getServletContext().log("Error while closing connection", e);
		response.sendError(500);//internal server error
	}
	
}
	
	/**
	 * Splits the given string of all purchased books and returns it an array.
	 * @param books the String array.
	 * @return the all purchased books
	 */
	private String[] getAllPurchasedBooks(String books) 
	{
		String[] mybooks=new String[10];
		mybooks=books.split(",+");
		return mybooks;
	}

}
