package entities;


/**
 * The Class CPaging.
 * class that holds information about the last page the user read.
 * new instance of that class made when some user buys a new book. 
 */
public class CPaging 
{

private String username,bookname;

private Integer id,page;

/**
 * Instantiates a new c paging.
 *
 * @param _id the id (unique)
 * @param _username the user name
 * @param _bookname the book name
 * @param _page the page number
 */
public CPaging(Integer _id,  String _username,String _bookname,Integer _page)
{	
	id=_id;
	username=_username;
	bookname=_bookname;
	page=_page;
}


public CPaging() 
{
}


public void setUsername(String _username) {
	username = _username;
}

public String getUsername() 
{
	return username;
}

public void setpage(Integer _page) {
	page = _page;
}


public Integer getpage() 
{
	return page;
}

public void setid(Integer _id) {
	id = _id;
}

public Integer getid() 
{
	return id;
}

public String getbookname() 
{
	return bookname;
}

public void set_bookname(String _bookname) {
	bookname = _bookname;
}


}
