/**
 * MY BOOKS CONTROLLER gets all of the current logged-in user's purchased books., and displays them to the user.
 */


app.controller('myBooksController', function ($scope, $rootScope, $http) 
{
	$scope.user=$scope.getCookie();
	
	function getMyBooks()
	   {
			var User=
				{
					"name": $scope.user
				};
			
		   $scope.reviews = [];

		   	//http call to myBooks servlet. GET method gets the user's books by sending the username to the server.

				$http(
			  			{
			  				
			  				method: 'GET',
			  				url: 'myBooksServlet',
					        headers: {'Content-Type': 'application/json'},
					        params: User

					        }).then(function(response) {
					        	$scope.data=response.data;
								$scope.length=$scope.data.length;
					        },
						        	function (response) {
									$scope.failMsg = failure.data;
					        });

	   }getMyBooks();
	   

	   
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
('myBooks',
		{
			templateUrl: 'myBooks.html',
			controller: 'myBooksController'
		}
);
