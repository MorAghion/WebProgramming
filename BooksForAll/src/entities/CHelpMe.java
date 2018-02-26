package entities;


/**
 * The Class CBookReviews.
 * class that hold the 'help me' massages and the information about them.
 * id(unique),the massage from the user to the admin, the massage back from the admin to the user if there is one,
 * and a field-'responded' says if the admin already responded or not.
 * a new instance of that class is made when a user writes an 'help me' massage. 
 */
public class CHelpMe 
{

private String username, fromUser, toUser,responded;
private Integer id;

/**
 * Instantiates a new c book reviews.
 * @param _id the id of the massage
 * @param _username the user name of the sender
 * @param _fromUser the massage from the user
 * @param _review the answer massage from the admin
 * @param _approved the approved field that tells us if the admin already reponded
  */

public CHelpMe(Integer _id,  String _username,String _fromUser,String _toUser,String _responded)
{	
	id=_id; 
	username=_username;
	fromUser=_fromUser;
	toUser=_toUser;
	responded=_responded;
}


public CHelpMe() 
{
}

public void setUsername(String _username) {
	username = _username;
}

public String getUsername() 
{
	return username;
}

public void setResponded(String _responded) {
	responded = _responded;
}

public String getResponded() 
{
	return responded;
}


public void setfromUser(String _fromUser) {
	fromUser = _fromUser;
}

public String getfromUser() 
{
	return fromUser;
}

public void setid(Integer _id) {
	id = _id;
}

public Integer getid() 
{
	return id;
}


public String gettoUser() 
{
	return toUser;
}


public void settoUser(String _toUser) {
	toUser = _toUser;
}
}
