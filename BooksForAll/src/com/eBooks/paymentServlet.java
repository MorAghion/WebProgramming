package com.eBooks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import entities.Consts;

/**
 * Servlet implementation class paymentServlet.
 */

public class paymentServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
       
    /**
     * Instantiates a new payment servlet.
     * @see HttpServlet#HttpServlet()
     */
	
	
    public paymentServlet() 
    {
        super();
    }

	/**
	 * Do get.
	 * Checks if the user name given in the request is purchased the book with the book name given in the request by sending it to the isUsernameAndBookPurchased function.
	 * Responses with the appropriate massage.  
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
							
							if(!isUsernameAndBookPurchased(request.getParameter("username"), request.getParameter("bookname"),conn))
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
	 * Checks if the user name given in the request is purchased the book with the book name given in the request.
	 * @param username the username
	 * @param book the book
	 * @param conn the conn
	 * @return true, if is username and book purchased
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
	 * Purchase book.
	 * Selects the user by the given user name in the parameters and update his "booksPurchased" column.
	 * Selects the book by the given book name in the parameters and update it's "purchasedBy" column
	 * Add a new row to the PAGING table with the current book, user name,  and page 0. 
	 * @param username the String
	 * @param book the String
	 * @param conn the connection
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void PurchaseBook( String username, String book, Connection conn) throws SQLException , NamingException, IOException
	{
		PreparedStatement ps;
		ps = conn.prepareStatement(Consts.SELECT_USER_BY_NAME_STMT);
		ps.setString(1, username);
	
		ResultSet rs = (ResultSet)ps.executeQuery();
		String purchasedBooks="";
		while (rs.next() )
		{
			if(!rs.getString(12).equals(""))
			{
			purchasedBooks=rs.getString(12)+","+book;
			}
			else purchasedBooks=book;
		}
		ps = conn.prepareStatement(Consts.UPDATE_PURCHASEDD_BOOKS_USERSTABLE_STMT);
		ps.setString(1, purchasedBooks);
		ps.setString(2, username);
		ps.executeUpdate();			
		rs.close();
		ps.close();
		
		PreparedStatement ps2;
		
		ps2 = conn.prepareStatement(Consts.SELECT_BOOK_BY_NAME_STMT);
		ps2.setString(1, book);
	
		ResultSet rs2 = (ResultSet)ps2.executeQuery();
		String purchasedBy="";
		while (rs2.next() )
		{
			purchasedBy=rs2.getString(7)+","+username;
			
		}
		
		ps2 = conn.prepareStatement(Consts.UPDATE_PURCHASED_BY_BOOKSTABLE_STMT);
		ps2.setString(1, purchasedBy);
		ps2.setString(2, book);
		ps2.executeUpdate();		
		ps2 = conn.prepareStatement(Consts.SELECT_ALL_PAGING);
		rs2=ps2.executeQuery();
		Integer id=0;
		while (rs2.next() )
		{
			id=Integer.parseInt(rs2.getString(1));
			
		}
		
		id=id+1;
		ps2 = conn.prepareStatement(Consts.INSERT_PAGING_STMT);
		ps2.setInt(1, id);
		ps2.setString(2, username);
		ps2.setString(3, book);
		ps2.setInt(4,0);
		ps2.executeUpdate();
		rs2.close();
		ps2.close();
	}
	
	/**
	 * Do post.
	 * Checks if the Payment information is valid by calling the isPAymentValid function.
	 * if so, calling the  PurchaseBook function which buys the book and update the DB about it.
	 * Responding with appropriate massage if the book was purchased. 
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
						if(isPAymentValid(request.getParameter("cardType"), request.getParameter("cardNum"), request.getParameter("owner"), Integer.parseInt(request.getParameter("month")), Integer.parseInt(request.getParameter("year"))+2000, request.getParameter("cvv"), conn).equals("all fields are fine"))
							{
							PurchaseBook(request.getParameter("username"), request.getParameter("bookname"),conn);
							writer.print("purchase succeeded");
							return;
							}
						writer.print(isPAymentValid(request.getParameter("cardType"), request.getParameter("cardNum"), request.getParameter("owner"), Integer.parseInt(request.getParameter("month")), Integer.parseInt(request.getParameter("year"))+2000, request.getParameter("cvv"), conn));	
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
	 * Checks if the Payment information is valid.
	 * @param type the type
	 * @param CardNum the card number
	 * @param owner the owner name
	 * @param month the month
	 * @param year the year
	 * @param cvv the cvv number
	 * @param conn the connection
	 * @return the string
	 * @throws SQLException the SQL exception
	 * @throws NamingException the naming exception
	 */
	public String isPAymentValid(String type,String CardNum,String owner, Integer month, Integer year,String cvv,  Connection conn) throws SQLException , NamingException
	{
		if(owner==null||owner.equals("") ||!owner.matches("^[A-Za-z]+\\s[A-Za-z]+$") )
		{
			return "Owner name is invalid";
		}
	
		if (cvv == null || cvv.equals("")|| !cvv.matches("^[0-9]{3,3}$")) 
		{
			return 	"cvv is invalid";
		}
	
		if(!type.equals("visa")&& !type.equals("amex"))
		{
			return 	"Card type is invalid";
		}
		if(type.equals("visa"))
		{
			if (CardNum == null || CardNum.equals("")|| !CardNum.matches("^(4[0-9]{15,15})$")) 
			{
				return 	"Card Number is invalid";
			}
		}
		if(type.equals("amex"))
		{
			if (CardNum == null || CardNum.equals("")|| !CardNum.matches("^(34[0-9]{13,13})$")) 
			{
				return 	"Card Number is invalid";
			}
		}
		if ( month==null || month > 12 || month <= 0 ) 
		{
			return 	"month is invalid";
		}
		if (  year==null ||year <2018) 
		{
			return 	"year is invalid";
		}
		LocalDate today = LocalDate.now();	
		LocalDate expiry = LocalDate.now().withYear(year).withMonth(month).withDayOfMonth(1).plusMonths(1);
		if (today.isAfter(expiry)) 
		{
			return 	"this card is expired";
		}
		return "all fields are fine";
	}
}
