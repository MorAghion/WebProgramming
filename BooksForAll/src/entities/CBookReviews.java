package entities;


/**
 * The Class CBookReviews.
 * every instance of this class hold the id of the review(unique for each review), the user that wrote it, the book name, the review it's self 
 * and if the admin already approved it.
 * new instance of this class is made when an user enters a review for a book he has.
 * 
 */
public class CBookReviews 
{


private String username,bookname ,approved,review;

private Integer id;

/**
 * Instantiates a new c book reviews.
 *
 * @param _id the id of the review
 * @param _username the user name
 * @param _bookname the book name
 * @param _review the review
 * @param _approved the approved
 */
public CBookReviews(Integer _id,  String _username,String _bookname,String _review,String _approved)
{	
	id=_id;
	username=_username;
	bookname=_bookname;
	review=_review;
	approved=_approved;
}


public CBookReviews() {}

public void setUsername(String _username) {
	username = _username;
}

public String getUsername() 
{
	return username;
}

public void setreview(String _review) {
	review = _review;
}

public String getreview() 
{
	return review;
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

public void set_bookname(String _bookname) 
{
	bookname = _bookname;
}

public String getapproved() 
{
	return approved;
}

public void setapproved(String _approved) 
{
	approved = _approved;
}

}
