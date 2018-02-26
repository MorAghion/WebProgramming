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
import com.google.gson.Gson;
import entities.CHelpMe;
import entities.Consts;

/**
 * Servlet implementation class helpMeServlet
 */

public class helpMeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public helpMeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * Do get.
	 * Gets all massages written by the user with the given user name from the request.
	 * Responses with a collection of CHELPME.
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
    		ArrayList<CHelpMe> myhelpme=new ArrayList<CHelpMe>(); 
    			try {
    				PreparedStatement ps;
    				ps = conn.prepareStatement(Consts.SELECT_HELPME_BY_USERNAME);
    				ps.setString(1, request.getParameter("name"));
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
	 * Insert a HelpMe in to the HELPME table with the massage from the user given in the request.
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 **/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			PrintWriter writer = response.getWriter();
			Context context;
			if(request.getParameter("msg")!="" && request.getParameter("msg")!=null)
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
						if(request.getParameter("msg").length()>1000)
						{
							writer.print("Your massage is too long");
						}
							PreparedStatement ps2=null;
							ps2=conn.prepareStatement(Consts.SELECT_ALL_CHELPME);
							ResultSet rs = (ResultSet)ps2.executeQuery();
							int newid=0;
		    				while (rs.next())
		    				{
		    					newid=rs.getInt(1);
		    				}
		    				newid+=1;
								ps = conn.prepareStatement(Consts.INSERT_HELPME_STMT);
								ps.setInt(1,newid);
								ps.setString(2,request.getParameter("username"));
								ps.setString(3,request.getParameter("msg"));
								ps.setString(4,"");
								ps.setString(5,"false");
								ps.executeUpdate();
								conn.commit();
								writer.print("HelpMe submitted to DB");
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
			else 	writer.print("HelpMe cannot be empty");
	}
		
	catch (NamingException e) 
	{
		getServletContext().log("Error while closing connection", e);
		response.sendError(500);		//internal server error
	}
		
	}
}
