package entities;

/**
 * The Class CBook.
 * hold every relevant information about the book. 
 * including which user bought it, and which liked it, the total amount of likes, and the age range for the book.
 */


public class CBook {
	
	
	private String name, price, description, likes, photo, link, likedBy, purchasedBy;
	
	
	private Integer ageRange;
	
	/**
	 * Instantiates a new c book.
	 * @param _name the name
	 * @param _price the price
	 * @param _description the description
	 * @param _likes the number of likes
	 * @param _photo the photo
	 * @param _link the link
	 * @param _likedBy the user names who liked the book
	 * @param _purchasedBy the user names who purchased the book
	 * @param _ageRange the age range of the books.
	 */
	public CBook(String _name, String _price, String _description, String _likes, String _photo, String _link,  String _likedBy,String _purchasedBy,Integer _ageRange)
	{
		name=_name;
		price=_price;
		description=_description;
		likes=_likes;
		photo=_photo;
		link=_link;
		likedBy=_likedBy;
		purchasedBy=_purchasedBy;
		ageRange=_ageRange;
		
	}

	public void SetName(String _name)
	{
		name=_name;
	}
	

	public String GetName()
	{
		return name;
	}
	

	public void SeAgeRange(Integer _ageRange)
	{
		ageRange=_ageRange;
	}
	
	public Integer GetAgeRange()
	{
		return ageRange;
	}

	public void SetPrice(String _price)
	{
		price=_price;
	}

	public String GetPrice()
	{
		return price;
	}

	public void SetDescription(String _description)
	{
		description=_description;
	}

	public String GetDescription()
	{
		return description;
	}

	public void SetLikes(String _likes)
	{
		likes=_likes;
	}

	public String GetLikes()
	{
		return likes;
	}
	
	public void SetPhoto(String _photo)
	{
		photo=_photo;
	}

	public String GetPhoto()
	{
		return photo;
	}

	public void SetLink(String _link)
	{
		link=_link;
	}

	public String GetLink()
	{
		return link;
	}

	public void SetlikedBy(String _likedBy)
	{
		likedBy=_likedBy;
	}

	public String GetlikedBy()
	{
		return likedBy;
	}

	public String GetPurchasedBy() 
	{
		return purchasedBy;
	}

	public void SetPurchasedBy(String _purchasedBy)
	{
		purchasedBy=_purchasedBy;
	}
}
