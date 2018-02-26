package entities;

// TODO: Auto-generated Javadoc
//Holding the attributes of the user

/**
 * The Class CUser.
 * hold every relevant information about the user. 
 * including which book the user bought, and which he liked , and all another information he has entered at the registration .
 * new instance of CUser is made when a new user register to the web site.
 */
public class CUser
{
	
	/** The photo. */
	private String username, email, phone, city, street, number, zipcode, password, nickname, description, photo;
	
	/** The books liked. */
	private String booksLiked;
	
	/** The books purchased. */
	private String booksPurchased;
	
	/** The is admin. */
	private String isAdmin;

	/**
	 * Instantiates a new c user.
	 *
	 * @param _username the username
	 * @param _email the email
	 * @param _phone the phone
	 * @param _city the city
	 * @param _street the street
	 * @param _number the number
	 * @param _zipcode the zipcode
	 * @param _pswrd the pswrd
	 * @param _nickname the nickname
	 * @param _description the description
	 * @param _photo the photo
	 * @param _bookPurchased the book purchased
	 * @param _bookLiked the book liked
	 * @param _isAdmin the is admin
	 */
	public CUser(String _username,String _email, String _phone, String _city, String _street, String _number,String _zipcode, String _pswrd, 
				String _nickname,String _description, String _photo, String _bookPurchased,String _bookLiked, String _isAdmin) 
	{
		username = _username;
		email=_email;
		city=_city;
		street=_street;
		number=_number;
		zipcode=_zipcode;
		password = _pswrd;
		nickname = _nickname;
		description = _description;
		phone=_phone;
		photo = _photo;
		booksLiked=_bookLiked;
		booksPurchased=_bookPurchased;
	}
	
	/**
	 * Instantiates a new c user.
	 */
	public CUser() {}
	
	/**
	 * Equals.
	 *
	 * @param otherUser the other user
	 * @return true, if successful
	 */
	public boolean equals(CUser otherUser){
		return nickname.equals(otherUser.nickname);
	}
	
//---------------set----------------------------
	
	/**
 * Sets the username.
 *
 * @param _username the new username
 */
public void setUsername(String _username) {
		username = _username;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param _email the new email
	 */
	public void setEmail(String _email) {
		email=_email;
	}
	
	/**
	 * Sets the city.
	 *
	 * @param _city the new city
	 */
	public void setCity(String _city) {
		city=_city;
	}
	
	/**
	 * Sets the street.
	 *
	 * @param _street the new street
	 */
	public void setStreet(String _street) {
		street=_street;
	}
	
	/**
	 * Sets the number.
	 *
	 * @param _number the new number
	 */
	public void setNumber(String _number) {
		number=_number;
	}
	
	/**
	 * Sets the phone.
	 *
	 * @param _phone the new phone
	 */
	public void setPhone(String _phone) {
		phone=_phone;
	}
	
	/**
	 * Sets the password.
	 *
	 * @param _password the new password
	 */
	public void setPassword(String _password) {
		password = _password;
	}
	
	/**
	 * Sets the nickname.
	 *
	 * @param _nickname the new nickname
	 */
	public void setNickname(String _nickname) {
		nickname=_nickname;
	}
	
	/**
	 * Sets the description.
	 *
	 * @param _description the new description
	 */
	public void setDescription(String _description) {
		description=_description;
	}
	
	/**
	 * Sets the photo.
	 *
	 * @param _photo the new photo
	 */
	public void setPhoto(String _photo) {
		photo = _photo;
	}
	
	/**
	 * Sets the zipcode.
	 *
	 * @param _zipcode the new zipcode
	 */
	public void setZipcode(String _zipcode) {
		zipcode = _zipcode;
	}
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	//------------------get--------------------------
	public String getUserName() 
	{
		return username;
	}
	
	/**
	 * Gets the phone.
	 *
	 * @return the phone
	 */
	public String getPhone() 
	{
		return phone;
	}
	
	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * Gets the street.
	 *
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}
	
	/**
	 * Gets the number.
	 *
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() 
	{
		return email;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() 
	{
		return password;
	}

	/**
	 * Gets the nickname.
	 *
	 * @return the nickname
	 */
	public String getNickname() 
	{
		return nickname;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() 
	{
		return description;
	}
	
	/**
	 * Gets the photo.
	 *
	 * @return the photo
	 */
	public String getPhoto() 
	{
		return photo;
	}
	
	/**
	 * Gets the zipcode.
	 *
	 * @return the zipcode
	 */
	public String getZipcode() 
	{
		return zipcode;
	}
	
	
	
	
	
	
	
	
	/**
	 * Gets the book purchased.
	 *
	 * @return the book purchased
	 */
	public String getBookPurchased() 
	{
		return booksPurchased;
	}
	
	/**
	 * Sets the book purchased.
	 *
	 * @param _bookPurchased the new book purchased
	 */
	public void setBookPurchased(String _bookPurchased) {
		booksPurchased = _bookPurchased;
	}

	/**
	 * Gets the book liked.
	 *
	 * @return the book liked
	 */
	public String getBookLiked() 
	{
		return booksLiked;
	}
	
	/**
	 * Sets the book liked.
	 *
	 * @param _bookLiked the new book liked
	 */
	public void setBookLiked(String _bookLiked) {
		booksLiked = _bookLiked;
	}

	/**
	 * Gets the checks if is admin.
	 *
	 * @return the checks if is admin
	 */
	public String getIsAdmin() 
	{
		return isAdmin;
	}
	
	/**
	 * Sets the checks if is admin.
	 *
	 * @param _isAdmin the new checks if is admin
	 */
	public void setIsAdmin(String _isAdmin)
	{
		isAdmin = _isAdmin;
	}
	
}
