/**
 * READ BOOK CONTROLLER gets and saves the scrolling of the user while reading a book.
 */

app.controller('readBookController',function($scope, $rootScope, $http, $window)
{
	
//functions that saves the last scroll when the window closes
	$window.onbeforeunload = function(event) {
		$scope.saveScroll($window.pageYOffset)
	}

	$scope.$on("$destroy", function() {
		$scope.saveScroll($window.pageYOffset)
	})
	
//READ function is called when a user opens his book for reading and displays it on the last scroll location.	
	function read()
	{
		var username = $scope.getCookie();
		var bookname=localStorage.getItem("myBook");

		$scope.readingPage=localStorage.getItem("howToRead");
		var PAGING = 
		{
			"username": username,
			"bookname": bookname,
			
		};
		
	//http call to readBooks servlet. GET method gets the user'slast location on the book by sending the username and book name to the server.

		$http(
	  			{
	  				
	  				method: 'GET',
	  				url: 'readBookServlet',
			        headers: {'Content-Type': 'application/json'},
			        params: PAGING
			        }).then(function(response) {
			        	if($scope.readingPage==0)
			        		{
			        			$window.scrollTo(0, 0);

			        		}
			        	else if($scope.readingPage==1)
			        		{
			        		
				        		var page=response.data.page;
				        		$scope.scrollToPage=page;
					     		$window.scrollTo(0, $scope.scrollToPage);

			        		}
	    			        
			        },
				        	function (failure) {
								$scope.failMsg = failure.data;
			        });
	   
		
	}read();
	

//SAVE SCROLL function saves the last scroll location of the user while reading a book.	
	$scope.saveScroll = function(page)
	{
		var username =$scope.getCookie();
		var bookname=localStorage.getItem("myBook");
		var PAGING = 
		{
			"username": username,
			"bookname": bookname,
			"page": page
		};
		
		//http call to readBooks servlet. POST method sends the user's last page on the book, the username and book name to the server.	   
		$http(
	  			{
	  				
	  				method: 'POST',
	  				url: 'readBookServlet',
			        headers: {'Content-Type': 'application/json'},
			        params: PAGING
			        }).then(function(response) {
							$scope.responseMsg = response.data;
			        },
				        	function (failure) {
		        		$scope.failMsg = failure.data;
			        });
	   
}
	

	
});

