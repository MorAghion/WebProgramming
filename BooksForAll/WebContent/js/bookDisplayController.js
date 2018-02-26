/**
 * BOOK DISPLAY CONTROLLER consists of functions that connect to the server in order to get and display information of a single book.
 */


var app = angular.module('main');

app.controller('bookDisplayController',function($scope, $rootScope, $http)
{
	$scope.user=$scope.getCookie();
	$scope.isAdmin=false;
	$(document).ready(function(){									//user tooltip
	    $('[data-toggle="tooltip"]').tooltip();   
	}); 
	
	$(document).ready(function(){									//amin tooltip
        $(".dropmenu").mouseover(function(){
            $(".dropdown-content").show();
        });
        $(".dropmenu").mouseout(function(){
            $(".dropdown-content").hide();
        });
    });

	
//getbook() is a functions that loads whenever bookDisplay path is being load.	
	function getbook()												
	{	
		$scope.cvvErr=true;											//boolean veriables initializations
		$scope.expiryErr=true;
		$scope.ownerErr=true;
		$scope.cardamexErr=true;
		$scope.cardvisaErr=true;
		$scope.dirtycvvErr=false;
		$scope.dirtyexpiryErr=false;
		$scope.dirtyownerErr=false;
		$scope.dirtycardamexErr=false;
		$scope.dirtycardvisaErr=false;
		
		$scope.visa=true;
		$scope.amex=false;
		
	    $scope.colortored=false;
		$scope.colortoblack=true;
		$scope.showPurchaseBtn=true;
		$scope.showBookLnk=false;
		
		$scope.name=localStorage.getItem("myBook");
		
		if($scope.user=="admin")
			{
			$scope.isAdmin=true;
			}
			
		var Book = 
			{
				"name": $scope.name,
			};
		
		var USERBOOK =
			{
				"username":$scope.user,
				"bookname": $scope.name
			};
		

	//http call to bookDisplay servlet. GET method sends the book name that was stored in local storage.
	  	$http(
	  			{
	  				
	  				method: 'GET',
	  				url: 'bookDisplayServlet',
			        headers: {'Content-Type': 'application/json'},
			        params: Book
			        }).then(function(response) 
						{
			        		$scope.bookName=response.data;
							
			        		var stringlikers=$scope.bookName[0].likedBy;
			    		    $scope.likers=stringlikers;
			    		    $scope.Linklikers = [];
			    		    $scope.Linklikers=stringlikers.split(",");
			    		    var mylikers=$scope.Linklikers;										//displays a string of the users who liked this book
			    		    var i=0;
			    		    var j=0;
			    		    var MyPurchasedBy=$scope.bookName[0].purchasedBy;					//displays a string of the users who purchased this book
			    		    var splittedPurchasedBy=MyPurchasedBy.split(",");
							
			    		    for (i=0;i< mylikers.length; i++ )
			    		    {
			    		    	  if(mylikers[i]==$scope.user)
			    		    		  {
			    		    			  $scope.colortored=true;
			    		    			  $scope.colortoblack=false;
			    		    		  }
			    		    }
			    		    for (j=0;j< splittedPurchasedBy.length; j++ )
			    		    {
			    		    	  if(splittedPurchasedBy[j]==$scope.user)
			    		    		  {
			    		    		    $scope.showBookLnk=true;
					        			$scope.showPurchaseBtn=false;
			    		    		  }
			    		    }
			    		   
			        },
				        	function (failure) {
							$scope.failMsg = failure.data;
						
			        });
	  	$scope.bookname=localStorage.getItem("myBook");
	  	} getbook();
	  	
/***************************************************READING SECTION*************************************************************/
//This functions is invoked when the user decides where she wants to start reading from, and clicks on the proper button.
//The decision is stored in localStorage
	  	
	$scope.howToRead = function(how)
	{
		if(how=="start")
			{
				localStorage.setItem("howToRead", 0);
				$('#readingModalModal').modal('hide');
        		$('body').removeClass('modal-open');
        		$('.modal-backdrop').remove();
				$scope.newpath($scope.bookName[0].link);
				
			}
		else if(how=="page")
			{
				localStorage.setItem("howToRead", 1);
				$('#readingModalModal').modal('hide');
        		$('body').removeClass('modal-open');
        		$('.modal-backdrop').remove();
				$scope.newpath($scope.bookName[0].link);
			}
	}
	
/*****************************************************************************PAYMENT*****SECTION**********************************************************************************************/
	
//PAY function is called when a user clicks on "pay" button when purchasing a book	
	$scope.pay = function()
	{
		$scope.name=localStorage.getItem("myBook");

		if($scope.visa==true)												//check payment method
			{
				var myCard = "visa";
				$scope.cardType = myCard;
				$scope.cardNum = $scope.visanum;
			}
		
		else if($scope.amex==true)
			{
				var myCard = "amex";
				$scope.cardType = myCard;
				$scope.cardNum = $scope.amexnum;
			}

		var PAY =															//create an object to send to the server, consists of user's payment details.
		{
			"username":$scope.user,
			"bookname": $scope.name,
			"cardType": $scope.cardType,
			"cardNum": $scope.cardNum,
			"owner": $scope.owner,
			"month": $scope.month,
			"year": $scope.year,
			"cvv": $scope.cvv,

		};
	
	
	
	//http call to bookDisplay servlet. POST method sends the user's payment details for verification and approval.
		$http(
	  			{
	  				
	  				method: 'POST',
	  				url: 'paymentServlet',
			        headers: {'Content-Type': 'application/json'},
			        params: PAY
			        }).then(function(response) {
			        	$scope.isPurchase=response.data;
			        	if($scope.isPurchase == "purchase succeeded")						//if purchase succeeded, hide the payment modal and go to "my books" page
			        		{
				        		$('#exampleModal').modal('hide');
				        		$('body').removeClass('modal-open');
				        		$('.modal-backdrop').remove();
				        		$scope.newpath('myBooks.html');
			        			$scope.purchSucceeded=true;
			        			$scope.purchNotSucceeded=false;
			        			
			        		}
			        	
			        	else																//if purchase did not succeed, display an error message to the user.
			        		{
			        		 $scope.errmsg=response.data;
			        			$scope.purchNotSucceeded=true;
				        		$scope.purchSucceeded=false;
			        		}
		        		
			        },
				        	function (failure) {
							var response = failure.data;
			        })
			        
			        
	}
	

//PAY METHOD function checks the payment methos of the user in order to display to her the correct input field.
	$scope.payMethod = function(paymentMethod)
	{
		$scope.payHow=paymentMethod;
		if($scope.payHow=="visa")
			{
				$scope.visa=true;
				$scope.amex=false;
			}
		else
			{
				$scope.amex=true;
				$scope.visa=false;
			}
		
	}
	
//CHECK CARD AMEX function veifies the user's input in case she chose to pay with "amex" card. 
//Verification details: total of 15 digits starting with '34', only dgits.
//in case of wrong input, a bollean var turns true and using "ng-show", displays an error message to the user.

	$scope.checkCardamex = function()
	{
		
		$scope.dirtycardamexErr=true;
		
		var mynum=$scope.amexnum;
		var numRegex = new RegExp("^(34[0-9]{13,13})$");
        var isMatch=numRegex.test(mynum);
        if (!isMatch) 
			{			
				$scope.cardamexErr = true; 
			} 
        else 
			{
				$scope.cardamexErr = false; 
				$scope.cardvisaErr = false; 
			}      
	}
	
//CHECK CARD VISA function veifies the user's input in case she chose to pay with "visa" card. 
//Verification details: total of 16 digits starting with '4', only dgits.
//in case of wrong input, a bollean var turns true and using "ng-show", displays an error message to the user.
	
	$scope.checkCardvisa = function()
	{
		
		$scope.dirtycardvisaErr=true;
		var mynum=$scope.visanum;
		var numRegex = new RegExp("^(4[0-9]{15,15})$");
        var isMatch=numRegex.test(mynum);
		if (!isMatch) 
				{
					$scope.cardvisaErr = true; 				
				} 
		else 
				{
					$scope.cardvisaErr = false; 
					$scope.cardamexErr = false; 
				}
	}
	
	
//CHECK EXPIRY function checks if the expiry date of the card has not yet expired.	
//In case of expiration, a boolen var turns "true", and using "ng-show", displays an error message to the user.
	
	$scope.checkExpiry= function()
	{
		$scope.dirtyexpiryErr=true;
		var month = $scope.month;
		var year = 2000 + Number($scope.year);

		if (!month || !year)
			return;
		var expiry = new Date(year,month,0);	
		var today = new Date();

		if (today > expiry) 
		{	
			$scope.expiryErr = true; 	
		} 
		else 
		{
			$scope.expiryErr = false; 
		}
	}
	
	
//CHECK OWNER function checks if the card owner name is valid: at least two words seperated by space.
//In case of an invalid name, a boolen var turns "true", and using "ng-show", displays an error message to the user.

	$scope.checkOwner= function() 
	{	
		$scope.dirtyownerErr=true;
        var ownerRegex = new RegExp('[^A-Za-z\\s]');
        var twoWordsRegex = new RegExp('.+\\s');
        
		if (ownerRegex.test($scope.owner) || !twoWordsRegex.test($scope.owner)) 
		{
			$scope.ownerErr = true;			
		} 
		else 
		{
			$scope.ownerErr = false;
			$scope.total = false;
		}
	}

//CHECK CVV function checks if the cvv code is valid: 3 digits only.
//In case of an invalid cvv, a boolen var turns "true", and using "ng-show", displays an error message to the user.
	
	$scope.checkCvv = function() {
		$scope.dirtycvvErr=true;
        var cvvRegex = new RegExp("^[0-9]{3,3}$");
		
		if (!cvvRegex.test($scope.cvv)) 
		{
			$scope.cvvErr = true;
		} else 
		{
			$scope.cvvErr = false;
		}
	}
	
/******************************************************REVIEWS**AND**LIKES**SECTION**********************************************************************************************/
	
//ADD REVIEW function is called when a user that purchased the book wants to add a review.

	$scope.addReview = function()
	{
		$scope.calltoaddreview=true;
		$scope.book=localStorage.getItem("myBook");
		var myBook = 
		{
			"name": $scope.user,
			"book": $scope.name
		};

	//http call to reviews servlet. POST method sends the username and book to review name to the server.
	
		$http(
	  			{
	  				
	  				method: 'POST',
	  				url: 'reviewsServlet',
			        headers: {'Content-Type': 'application/json'},
			        params: myBook
			        }).then(function(response) {
			        		$scope.ispurch=response.data;								//server checks if the user did purchase the book
			        		if($scope.ispurch=="book not purchased")					//if user did'nt purchase the book, a boolean var turns "true"
			        		{															//and using "ng-show" dsplays an error message to the user.
			        			$('#myModal').modal('hide')
			        			$scope.showErrmsg=true;
			        		}
			        		else
			        		{
			        			$('#myModal').modal('show')								//if user did purchased the book, a modal is shown with a textarea.
			        		}
			        },
				        	function (failure) {
			        		$scope.ispurch = failure.data;

			        });
	}
	
	
//IS ALREADY LIKED function checks if the user has already clicked the "like" button of a book.

	$scope.isalreadyliked = function()
	{
	  	$scope.bookname=localStorage.getItem("myBook");
		var myparams = 
		{	
			"username": username,
			"bookname":$scope.bookname
		};
		

	  	$http(
	  			{
	  				
	  				method: 'PUT',
	  				url: 'bookDisplayServlet',
			        headers: {'Content-Type': 'application/json'},
			        params: myparams
			        }).then(function(response) {
			        		if(response.data == "red")									//if the user has already liked the book, a boolean var turns "true" and the heart-glyphicon is painted in red
			        			{
			        				$scope.colortored=true;
			        				$scope.colortoblack=false;
			        			}
			        		else if(response.data == "black")							//if the user did not yet liked the book, a boolean var turns "true" and the heart-glyphicon is painted black
			        			{
			        				$scope.colortored=false;
			        				$scope.colortoblack=true;
			        			}
			        },
				        	function (failure) {
							$scope.failMsg = failure.data;
			        });	
	}
	
	
//LIKE function is called when a user that purchased the book wants to like/unlike the book.
	
	$scope.like = function()
	{
		$scope.bookToLike=localStorage.getItem("myBook");
		var MyParams = 
		{
			"name": $scope.user,
			"bookToLike":$scope.bookToLike
		};
		
	//http call to bookDisplay servlet. POST method sends the username and book to like name to the server.
	
	  	$http(
	  			{	  				
	  				method: 'POST',
	  				url: 'bookDisplayServlet',
			        headers: {'Content-Type': 'application/json'},
			        params: MyParams
			        }).then(function(response) 
					{
			        		$scope.msg=response.data;													//server checks if the user did purchase the book
			        		$scope.displayMsg_Purchase=false;
			        		$scope.likeToDB=false;
			        		$scope.unlikeToDB=false;
			        		
							if($scope.msg=="book not purchased")										//if user did'nt purchase the book, a boolean var turns "true"							
			        		{	
			        			$scope.displayMsg_Purchase=true;										//and using "ng-hide", hides the "like" button.
			        			return;
			        		}
			        		else if($scope.msg=="book purchased and already liked")						//if user did purchase the book, already liked it, he can "unlike" the book.
			        		{
				        		$scope.unlikeToDB=true;
				        		$scope.bookName[0].likes = Number($scope.bookName[0].likes) - 1;		//number of likes is decreased with 1
				        		$scope.colortored=false;												//the glyphicon color is changed from red to black using "ng-show", "ng-hide"
		        				$scope.colortoblack=true;
				        		return;
			        		}
			        		
			        		else																		//if user did purchase the book, and wants to like it
			        		{
			        			$scope.likeToDB=true;
			        			$scope.bookName[0].likes = Number($scope.bookName[0].likes) + 1;		//number of likes is increased with 1
				        		$scope.colortored=true;													//the glyphicon color is changed from black to red using "ng-show", "ng-hide"
		        				$scope.colortoblack=false;
			        			return;
			        		}
			        },
				        	function (failure) {
							$scope.failMsg = failure.data;
			        });
	
	
	  	
	}
	
//SHOW BOOK REVIEWS function is called when a user clicks "view reviews".
	 $scope.ShowBookReviews = function(bookname)
	   {
	//http call to reviews servlet. GET method sends the book name to the server andd displays the information.
			   
				$http(
			  			{
			  				
			  				method: 'GET',
			  				url: 'reviewsServlet',
					        headers: {'Content-Type': 'application/json'},
					        params: {name: bookname},
					        }).then(function(response) {
					        		$scope.reviews=response.data;
					        },
						        	function (failure) {
									$scope.failMsg = failure.data;
					        });
			   
	   }
	   
	   
//SUBMIT REV function is called when a user that purchased the book wants to add a review the book.	 
	 $scope.SubmitRev= function()
	 {
		 $scope.bookname=localStorage.getItem("myBook");
		 
		 var newReview = 
			{
				"username": $scope.user,
				"bookname": $scope.bookname,
				"review" : $scope.review,
				"approved": "false"
			};
		 
		 var review=$scope.review;
		
		 if(review=="" || review==null)														//checks if the review is empty
			 {
			 	$scope.reviewEmpty=true;
			 	$('#myModal').modal('hide');
			 	return;
			 }

	//http call to reviews servlet. PUT method sends to the server the username, book name, the review and setes it to be  un-approved.		 
		 $http(
		  			{
		  				
		  				method: 'PUT',
		  				url: 'reviewsServlet',
				        headers: {'Content-Type': 'application/json'},
				        params: newReview
				        }).then(function(response) {

				        	if(response.data=="review submitted to DB")
				        	{
				        		$scope.reviewSubmitted=true;
				        		
				        		$('#myModal').modal('hide');
				        	}
				        	
				        	else
				        	{
				        		$scope.reviewSubmitted=false;
				        		$scope.showErrMsg=true;
		        				return;
				        		
				        	}
				        		

				        	
				        },
					        	function (failure) {
								$scope.failMsg = failure.data;

				        });
		 
	 }

/**********************************************************************UTILITY**FUNCTIONS***************************************************************************************************/	 
	 
	 $scope.getUserName = function(userName)
	   {

		   localStorage.setItem("userDetails", userName);
	   }
	 
	 $('#myModal').on('hidden.bs.modal', function (e) {
		  $(this)
		    .find("input,textarea,select")
		       .val('')
		       .end()
		})
		
	$('#exampleModal').on('hidden.bs.modal', function (e) {
		  $(this)
		    .find("input,textarea,select")
		       .val('')
		       .end()
		})


$scope.LinkToUser = function(userName)									//admin's tooltip that contains links to he users' profiles.
{
		 localStorage.setItem("userDetails", userName);
		 $scope.newpath('userDetails.html');

}
	
});


app.component
('bookDisplay',
		{
			templateUrl: 'bookDisplay.html',
			controller: 'bookDisplayController'
		}
);
