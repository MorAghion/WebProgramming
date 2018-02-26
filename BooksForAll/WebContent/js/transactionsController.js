/**
 * TRANACTIONS CONTROLLER gets all of he information about the last transactions on the site.
 */


app.controller('transactionsController',function($scope, $rootScope, $http)
{

//SORT BY USERS function sorts the transactions by usernames
	$scope.sortByUsers = function($index)
	{
	    $scope.booksPurchased = [];

		
		$http(
	  			{
	  				method: 'GET',
	  				url: 'usersServlet',
			        headers: {'Content-Type': 'application/json'},
			        }).then(function(response) {
			        		$scope.allUsers=response.data;
			        		$scope.byUsers=true;
			        		$scope.byBooks=false;

			        },
				        	function (failure) {
							$scope.failMsg = failure.data;
			        });
		
	}
	
//SORT BY BOOKS function sorts the transactions by book name
	
	$scope.sortByBooks = function()
	{
		var Setage = 
		{
			"AgeRange": 0,
		};
		$http(
	  			{
	  				method: 'GET',
	  				url: 'booksServlet',
			        headers: {'Content-Type': 'application/json'},
			        params: Setage,
			        }).then(function(response) {
			        		$scope.allBooks=response.data;
			        		$scope.byUsers=false;
			        		$scope.byBooks=true;
			        },
				        	function (failure) {
							$scope.failMsg = failure.data;
			        });
		
	}
	

});

app.component
('transactions',
		{
			templateUrl: 'transactions.html',
			controller: 'transactionsController'
		}
);
