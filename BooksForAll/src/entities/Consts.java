package entities;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

public interface Consts 
{
	public final String USERS = "users";
	public final String DB_NAME = "DB_NAME";
	public final String DB_DATASOURCE = "DB_DATASOURCE";
	public final String OPEN = "Open";
	public final String SHUTDOWN = "Shutdown";
	public final String DB_DIR_NAME = "db-derby-10.12.1.1-bin";
	public final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	public final String PROTOCOL = "jdbc:derby:";
	public final String CREATE_USERS_TABLE = "CREATE TABLE USERS(username varchar(100),"
											+ "email varchar(100)," + "phone varchar(10),"
											+ "city varchar(50)," + "street varchar(50)," + "number varchar (10)," +"zipcode varchar(7),"
											+ "password varchar(8)," +  "nickname varchar(20)," + "description varchar(50)," + "photo varchar(500)," 
											+"booksPurchased varchar(3000)," +"booksLiked varchar(3000)," +"isAdmin varchar(3))";
	public final String INSERT_USER_STMT = "INSERT INTO USERS VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public final String SELECT_ALL_USERS_STMT = "SELECT * FROM USERS";
	public final String SELECT_USER_BY_NAME_STMT = "SELECT * FROM USERS " + "WHERE username=?";
	public final String USERS_FILE = USERS + ".json";
	public final String CREATE_BOOKS_TABLE = "CREATE TABLE BOOKS(name varchar(100),"
											+ "price varchar(10)," + "description varchar(300)," + "likes varchar(5),"
											+ "photo varchar(100)," + "link varchar(300),"  + "likedBy varchar(3000),"+"purchasedBy varchar(3000),"+"ageRange integer)";
	public final String BOOKS = "books";
	public final String BOOKS_FILE = BOOKS + ".json";
	public final String INSERT_BOOK_STMT = "INSERT INTO BOOKS VALUES(?,?,?,?,?,?,?,?,?)";
	public final String NAME = "name";
	public final String SELECT_ALL_BOOKS_STMT = "SELECT * FROM BOOKS";
	public final Type BOOKS_COLLECTION =  new TypeToken<Collection<CBook>>() {}.getType();
	public final String SELECT_USER_BY_NAME_AND_PASSWORD_STMT = "SELECT * FROM USERS WHERE username=? AND password=?";
	public final String SELECT_BOOK_BY_NAME_STMT = "SELECT * FROM BOOKS WHERE name=? ";
	public final String CREATE_BOOK_REVIEWS_TABLE =  "CREATE TABLE BOOK_REVIEWS(id integer,"+"username varchar(100),"+"bookname varchar(100),"+"review varchar(1000),"+"approved varchar(5))";
	public final String BOOK_REVIEWS_FILE = "bookReviews.json";
	public final String INSERT_BOOK_REVIEWS_STMT = "INSERT INTO BOOK_REVIEWS VALUES(?,?,?,?,?)";;
	public final String UPDATE_LIKED_BOOKS_USERSTABLE_STMT = "UPDATE USERS SET booksLiked=? Where username=?";
	public final String UPDATE_LIKED_BY_BOOKSTABLE_STMT = "UPDATE BOOKS SET likedBy=?,likes=? Where name=?";
	public final Type BOOK_REVIEWS_COLLECTION =  new TypeToken<Collection<String>>() {}.getType();
	public final String SELECT_REVIEWS_BY_BOOKNAME = "SELECT * FROM BOOK_REVIEWS WHERE bookname=? and approved=? ";
	public final String SELECT_ALL_REVIEWS = "SELECT * FROM BOOK_REVIEWS";
	public final Type USERS_COLLECTION =  new TypeToken<Collection<CUser>>() {}.getType();
	public final Type USER =  new TypeToken<CUser>() {}.getType();
	public final String DELETE_USER_BY_NAME_STMT = "DELETE  FROM USERS WHERE username=?";
	public final String SELECT_ALL_UNAPREVOED_REVIEWS_STMT = "SELECT * FROM BOOK_REVIEWS WHERE approved=?";
	public final Type REVIEWS_COLLECTION =  new TypeToken<Collection<CBookReviews>>() {}.getType();
	public final String SELECT_BOOK_REVIEW_BY_ID_STMT =  "SELECT * FROM BOOK_REVIEWS WHERE id=?";
	public final Type REVIEW =new TypeToken<CBookReviews>() {}.getType();
	public final String DELETE_REVIEW_BY_ID_STMT = "DELETE  FROM BOOK_REVIEWS WHERE id=?";
	public final String SELECT_ALL_NOTADMIN_USERS_STMT = "SELECT * FROM USERS WHERE isAdmin=?";
	public final String UPDATE_BOOK_REVIEW_BY_ID_STMT = "UPDATE BOOK_REVIEWS SET approved=? Where id=?";
	public final String UPDATE_PURCHASEDD_BOOKS_USERSTABLE_STMT = "UPDATE USERS SET booksPurchased=? Where username=?";;
	public final String UPDATE_PURCHASED_BY_BOOKSTABLE_STMT = "UPDATE BOOKS SET purchasedBy=? Where name=?";
	public final String SELECT_BOOKS_BY_AGERANGE_STMT =  "SELECT * FROM BOOKS WHERE ageRange=?";
	public final String DELETE_REVIEW_BY_USERNAME_STMT = "DELETE  FROM BOOK_REVIEWS WHERE username=?";
	public final String CREATE_PAGING_TABLE =  "CREATE TABLE PAGING(id integer,"+"username varchar(100),"+"bookname varchar(100),"+"page integer)";
	public final String INSERT_PAGING_STMT = "INSERT INTO PAGING VALUES(?,?,?,?)";
	public final String PAGING_FILE ="paging.json";
	public final String SELECT_PAGING_BY_NAME_AND_BOOKNAME = "SELECT * FROM PAGING WHERE username=? AND bookname=?";
	public final String UPDATE_PAGING_BY_ID_STMT = "UPDATE PAGING SET page=? Where id=?";
	public final String UPDATE_PAGING_BY_NAME_AND_BOOKNAME_STMT = "UPDATE PAGING SET page=? WHERE username=? AND bookname=?";
	public final Type CPAGING=new TypeToken<CPaging>() {}.getType();
	public final String SELECT_ALL_PAGING =  "SELECT * FROM PAGING";
	
	
	public final String CREATE_HELPME_TABLE = "CREATE TABLE HELPME(id integer,"+"username varchar(100),"
			+ "fromUser varchar(1000)," + "toUser varchar(1000),"+"responded varchar(5))";
	public final String INSERT_HELPME_STMT = "INSERT INTO HELPME VALUES(?,?,?,?,?)";
	public final String SELECT_HELPME_BY_USERNAME = "SELECT * FROM HELPME WHERE username=?";
	public final Type HELPME_COLLECTION = new TypeToken<Collection<CHelpMe>>() {}.getType();
	public final String SELECT_UNHELPED_HELPME = "SELECT * FROM HELPME WHERE responded=?";
	public final String UPDATE_HELPME_BY_ID_STMT = "UPDATE HELPME SET toUser=?,responded=?"
			+ " Where id=?";
	public final String SELECT_ALL_CHELPME = "SELECT * FROM HELPME";

	
}

