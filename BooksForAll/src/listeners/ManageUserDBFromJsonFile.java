package listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.Consts;
import entities.CBook;
import entities.CUser;
import entities.CBookReviews;
import entities.CPaging;

/**
 * An example listener that reads the customer json file and populates the data into a Derby database.
 */
@WebListener
public class ManageUserDBFromJsonFile implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ManageUserDBFromJsonFile() {
    	System.out.println("Initiallization");
    }
    
    /**
     * Checks if table already exists.
     * @param e the SQLException
     * @return true, if ecists
     */
    private boolean tableAlreadyExists(SQLException e) {
        boolean exists;
        if(e.getSQLState().equals("X0Y32")) {
            exists = true;
        } else {
            exists = false;
        }
        return exists;
    }

	/**
	 * Context initialized.  Creats  all tables and gets the information from the Json files.
	 * @param event the ServletContextEvent
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
    public void contextInitialized(ServletContextEvent event)  { 
    	ServletContext cntx = event.getServletContext();
    	
    	try{
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    		cntx.getInitParameter(Consts.DB_DATASOURCE) + Consts.OPEN);
    		Connection conn = ds.getConnection();
    		boolean users_created = false;
    		boolean books_created = false;
    		boolean bookReviews_created=false;
    		boolean paging_created=false;
    		boolean helpme_created=false;

    	//_________________________________________________________________________
    		try{
    			Statement stmt = conn.createStatement();
    			System.out.println("creating table users");
    			stmt.executeUpdate(Consts.CREATE_USERS_TABLE);
    			System.out.println("Table USERS created");
    			//commit update
        		conn.commit();
        		stmt.close();
    		}catch (SQLException e){
    			//check if exception thrown since table was already created (so we created the database already 
    			//in the past
    			users_created = tableAlreadyExists(e);
    			if (!users_created){
    				throw e;//re-throw the exception so it will be caught in the
    				//external try..catch and recorded as error in the log
    			}
    		}
    		
    		//if no database exist in the past - further populate its records in the table
    		if (!users_created){
    			//populate users table with customer data from json file
    			Collection<CUser> users = loadUsers(cntx.getResourceAsStream(File.separator +
    														   Consts.USERS_FILE));
    			PreparedStatement pstmt = conn.prepareStatement(Consts.INSERT_USER_STMT);
    			for (CUser user : users){
    				pstmt.setString(1,user.getUserName());
    				pstmt.setString(2,user.getEmail());
    				pstmt.setString(3,user.getPhone());
    				pstmt.setString(4,user.getCity());
    				pstmt.setString(5,user.getStreet());
    				pstmt.setString(6,user.getNumber());
    				pstmt.setString(7,user.getZipcode());
    				pstmt.setString(8,user.getPassword());
      				pstmt.setString(9,user.getNickname());
    				pstmt.setString(10,user.getDescription());
    				pstmt.setString(11,user.getPhoto());
    				pstmt.setString(12,user.getBookPurchased());
    				pstmt.setString(13,user.getBookLiked());
    				pstmt.setString(14,user.getIsAdmin());

    				pstmt.executeUpdate();
    			}
    			conn.commit();
    			pstmt.close();
    		}
    			
    			
   //------------------------------------------------------------------------------------------------------------------//
    			
        		try{
        			Statement stmt = conn.createStatement();
        			System.out.println("creating table books");
        			stmt.executeUpdate(Consts.CREATE_BOOKS_TABLE);
        			System.out.println("Table BOOKS created");
        			//commit update
            		conn.commit();
            		stmt.close();
        		}catch (SQLException e){
        			//check if exception thrown since table was already created (so we created the database already 
        			//in the past
        			books_created = tableAlreadyExists(e);
        			if (!books_created){
        				throw e;//re-throw the exception so it will be caught in the
        				//external try..catch and recorded as error in the log
        			}
        		}
        		
        		//if no database exist in the past - further populate its records in the table
        		if (!books_created){
        			Collection<CBook> books = loadBooks(cntx.getResourceAsStream(File.separator +
        														   Consts.BOOKS_FILE));
        			PreparedStatement ps = conn.prepareStatement(Consts.INSERT_BOOK_STMT);
        			for (CBook book : books){
        				ps.setString(1,book.GetName());
        				ps.setString(2,book.GetPrice());
        				ps.setString(3,book.GetDescription());
        				ps.setString(4,book.GetLikes());
        				ps.setString(5,book.GetPhoto());
        				ps.setString(6,book.GetLink());
        				ps.setString(7,book.GetlikedBy());
        				ps.setString(8,book.GetPurchasedBy());
        				ps.setInt(9,book.GetAgeRange());

        				ps.executeUpdate();
        			}
    			
    			

    			//commit update
    			conn.commit();
    			//close statements
    			ps.close();
    		}
    		

    	
//  ___________________________________________________________________________________________  		
    	
			
    		try{
    			Statement stmt = conn.createStatement();
    			System.out.println("creating table BOOK_REVIEWS");
    			stmt.executeUpdate(Consts.CREATE_BOOK_REVIEWS_TABLE);
    			System.out.println("Table BOOK_REVIEWS created");
    			//commit update
        		conn.commit();
        		stmt.close();
    		}catch (SQLException e){
    			//check if exception thrown since table was already created (so we created the database already 
    			//in the past
                bookReviews_created = tableAlreadyExists(e);
    			if (!bookReviews_created){
    				throw e;//re-throw the exception so it will be caught in the
    				//external try..catch and recorded as error in the log
    			}
    		}
    		
    		//if no database exist in the past - further populate its records in the table
    		if (!bookReviews_created){
    			Collection<CBookReviews> booksReviews = loadBookReviews(cntx.getResourceAsStream(File.separator +
    														   Consts.BOOK_REVIEWS_FILE));
    			PreparedStatement ps = conn.prepareStatement(Consts.INSERT_BOOK_REVIEWS_STMT);
    			for (CBookReviews bookReviews : booksReviews){
    				ps.setInt(1,bookReviews.getid());
    				ps.setString(2,bookReviews.getUsername());
    				ps.setString(3,bookReviews.getbookname());
    				ps.setString(4,bookReviews.getreview());
    				ps.setString(5,bookReviews.getapproved());
    				ps.executeUpdate();
    			}
			
			

			//commit update
			conn.commit();
			//close statements
			ps.close();
		}
		

		
    		
    //___________________________________________________________________________		
			
    		try{
    			Statement stmt = conn.createStatement();
    			System.out.println("creating table PAGING");
    			stmt.executeUpdate(Consts.CREATE_PAGING_TABLE);
    			System.out.println("Table PAGING created");
    			//commit update
        		conn.commit();
        		stmt.close();
    		}catch (SQLException e){
    			//check if exception thrown since table was already created (so we created the database already 
    			//in the past
                paging_created = tableAlreadyExists(e);
    			if (!paging_created){
    				throw e;//re-throw the exception so it will be caught in the
    				//external try..catch and recorded as error in the log
    			}
    		}
    		
    		//if no database exist in the past - further populate its records in the table
    		if (!paging_created){
    			Collection<CPaging> paging = loadPaging(cntx.getResourceAsStream(File.separator +
    														   Consts.PAGING_FILE));
    			PreparedStatement ps = conn.prepareStatement(Consts.INSERT_PAGING_STMT);
    			for (CPaging page : paging){
    				ps.setInt(1,page.getid());
    				ps.setString(2,page.getUsername());
    				ps.setString(3,page.getbookname());
    				ps.setInt(4,page.getpage());
    				ps.executeUpdate();
    			}
			//commit update
			conn.commit();
			//close statements
			ps.close();
		}
    //_____________________________________________________________________________________________
    	try{
    			Statement stmt = conn.createStatement();
    			System.out.println("creating table HELPME");
    			stmt.executeUpdate(Consts.CREATE_HELPME_TABLE);
    			System.out.println("Table HELPME created");
    			//commit update
        		conn.commit();
        		stmt.close();
    		}catch (SQLException e){
    			//check if exception thrown since table was already created (so we created the database already 
    			//in the past
    			helpme_created = tableAlreadyExists(e);
    			if (!helpme_created){
    				throw e;//re-throw the exception so it will be caught in the
    				//external try..catch and recorded as error in the log
    			}
    		}	
   //_____________________________________________________________________________________________	
    		//close connection
    		conn.close();		
    		
    		
    	} catch (IOException | SQLException | NamingException e) {
    		//log error 
    		cntx.log("Error during database initialization",e);
    	}
    }

	/**
	 * Context destroyed.
	 *
	 * @param event the event
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
    public void contextDestroyed(ServletContextEvent event)  { 
    	 ServletContext cntx = event.getServletContext();
    	 
         //shut down database
    	 try {
     		//obtain CustomerDB data source from Tomcat's context and shutdown
     		Context context = new InitialContext();
     		BasicDataSource ds = (BasicDataSource)context.lookup(
     				cntx.getInitParameter(Consts.DB_DATASOURCE) + Consts.SHUTDOWN);
     		ds.getConnection();
     		ds = null;
		} catch (SQLException | NamingException e) {
			cntx.log("Error shutting down database",e);
		}

    }
    
    
    /**
     * Loads users data from json file that is read from the input stream into a collection of Customer objects.
     * @param is input stream to json file
     * @return collection of users
     * @throws IOException Signals that an I/O exception has occurred.
     */
	private Collection<CUser> loadUsers(InputStream is) throws IOException{
		
		//wrap input stream with a buffered reader to allow reading the file line by line
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}
		Gson gson = new Gson();
		//this is a require type definition by the Gson utility so Gson will 
		//understand what kind of object representation should the json file match
		Type type = new TypeToken<Collection<CUser>>(){}.getType();
		Collection<CUser> users = gson.fromJson(jsonFileContent.toString(), type);
		//close
		br.close();	
		return users;

	}
	
    /**
     * Loads books data from json file that is read from the input stream into a collection of books objects.
     * @param is input stream to json file
     * @return collection of books
     * @throws IOException Signals that an I/O exception has occurred.
     */
	private Collection<CBook> loadBooks(InputStream is) throws IOException{
		
		//wrap input stream with a buffered reader to allow reading the file line by line
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}
		Gson gson = new Gson();
		//this is a require type definition by the Gson utility so Gson will 
		//understand what kind of object representation should the json file match
		Type type = new TypeToken<Collection<CBook>>(){}.getType();
		Collection<CBook> books = gson.fromJson(jsonFileContent.toString(), type);
		//close
		br.close();	
		return books;

	}
	
	
    /**
     * Loads book reviews data from json file that is read from the input stream into a collection of book reviews objects.
     * @param is input stream to json file
     * @return collection of book reviews
     * @throws IOException Signals that an I/O exception has occurred.
     */
	private Collection<CBookReviews> loadBookReviews(InputStream is) throws IOException{
		
		//wrap input stream with a buffered reader to allow reading the file line by line
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}
		Gson gson = new Gson();
		//this is a require type definition by the Gson utility so Gson will 
		//understand what kind of object representation should the json file match
		Type type = new TypeToken<Collection<CBookReviews>>(){}.getType();
		Collection<CBookReviews> BooksReviews = gson.fromJson(jsonFileContent.toString(), type);
		//close
		br.close();	
		return BooksReviews;

	}
	
    /**
     * Loads books data from json file that is read from the input stream into a collection of CPaging objects.
     * CPaging- is an objects that save the page of the book ,the book nams and the user name.
     * @param is input stream to json file
     * @return collection of CPaging
     * @throws IOException Signals that an I/O exception has occurred.
     */
	
	private Collection<CPaging> loadPaging(InputStream is) throws IOException{
		
		//wrap input stream with a buffered reader to allow reading the file line by line
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}
		Gson gson = new Gson();
		//this is a require type definition by the Gson utility so Gson will 
		//understand what kind of object representation should the json file match
		Type type = new TypeToken<Collection<CPaging>>(){}.getType();
		Collection<CPaging> paging = gson.fromJson(jsonFileContent.toString(), type);
		//close
		br.close();	
		return paging;

	}
	
}

