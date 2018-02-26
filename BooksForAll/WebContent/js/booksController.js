/**
 * BOOK DISPLAY CONTROLLER gets all books details from the server 
 */


app.controller('booksController', function ($scope, $rootScope, $http) 
{
	var ageR=localStorage.getItem("ageRange");
	

	var Setage = 
	{
		"AgeRange": ageR,
	};
	
	//http call to books servlet. GET method sends the desirable age range to the server, and gets the matching books list to display to the user. 
	$http(
  			{	
  				method: 'GET',
  				url: 'booksServlet',
		        headers: {'Content-Type': 'application/json'},
		        params: Setage,
		        }).then(function(response) {
		        	     $scope.data = response.data;
						$scope.length=$scope.data.length;	
		        },
			        	function (failure) {
						$scope.failMsg = failure.data;
		        });
				
//UTILITY FUNCTIONS
   
   $scope.getBookName = function(bookName)
   {

	   localStorage.setItem("myBook", bookName);
   }
   
  var $myGroup = $('#myGroup');
   $myGroup.on('show.bs.collapse','.collapse', function() {
       $myGroup.find('.collapse.in').collapse('hide');
   });
   
   
});
app.component
('books',
		{
			templateUrl: 'books.html',
			controller: 'booksController'
		}
);
