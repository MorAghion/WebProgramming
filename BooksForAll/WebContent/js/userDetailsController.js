/**
 * USER DETAILS CONTROLLER gets information about a specific user
 */

app.controller('userDetailsController',function($scope, $rootScope, $http)
{

	$scope.username=localStorage.getItem("userDetails");	
	
//GET USER function gets the specified user's details.
	function getUser()
	{	
		
		$scope.name=$scope.user;
		var User = 
		{
			"name": $scope.username
		};
		
		//http call to userDetails servlet. GET method sends the user's  username the server.	   

	  	$http(
	  			{
	  				
	  				method: 'GET',
	  				url: 'userDetailsServlet',
			        headers: {'Content-Type': 'application/json'},
			        params: User
			        }).then(function(response) {
			        		$scope.currentUser=response.data;									//the information is stored in $scope.currentUser that will be binded in the html file.
			        		
	
			        },
				        	function (failure) {
			        		$scope.failMsg = failure.data;
			        
			        });
	}
	
	getUser();

//DELETE USER function is called when the admin wants to delete a user by clicking "delete user" button.
	
	$scope.toDelete = function(descision,username)
	  {
		  if(descision=="yes")
			  {
			  	$('#deleteUserModal').modal('hide');
	  			$('body').removeClass('modal-open');
	  			$('.modal-backdrop').remove();
			  	$scope.deleteUser(username);
			  }
		  
		  else if(descision=="no")
			  {
			  	$('#deleteUserModal').modal('hide');
	  			$('body').removeClass('modal-open');
	  			$('.modal-backdrop').remove();
			  }
	  }
	
	 $scope.deleteUser = function(username)
	   {
			var User = 
			{
				"name": username
			};
		
//http call to userDetails servlet. DELETE method sends the user's  username the server.
				$http(
			  			{
			  				
			  				method: 'DELETE',
			  				url: 'usersServlet',
					        headers: {'Content-Type': 'application/json'},
					        params:User
					        }).then(function(response) {
					        		$scope.responseMsg = response.data;
					        		$scope.newpath('users.html');
					        },
						        	function (failure) {
					        		$scope.failMsg = failure.data;
					        });
	  
	   }
	
	
	
});

app.component
('userDetails',
		{
			templateUrl: 'userDetails.html',
			controller: 'userDetailsController'
		}
);
