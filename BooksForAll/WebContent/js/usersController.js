/**
 * USERS CONTROLLER gets information about all users.
 */




app.controller('usersController', function ($scope, $rootScope, $http, $window) 
{
	 $scope.userToDelete = '';

	//http call to users servlet. GET method.	all users are stored in $scope.allUsers.
	getAllUsers = function()
	{
		   $http.get('usersServlet').then(function (response) {
		     $scope.allUsers = response.data;
		  }, function(response){
			  $scope.failMsg = response.data;
		  });
	}
	getAllUsers();

	$(document).ready(function(){										//admin's tooltip
	    $('[data-toggle="tooltip"]').tooltip();   
	}); 
	

 //DELETE USER function is called when the admin wants to delete a user by clicking "delete user" button.	

	$scope.setToDelete = function(username)
	{
		$scope.userToDelete=username;
	}
  $scope.toDelete = function(descision)
  {
	  if(descision=="yes")
		  {
		  	$('#deleteUserModal').modal('hide');
  			$('body').removeClass('modal-open');
  			$('.modal-backdrop').remove();
		  	$scope.deleteUser( $scope.userToDelete);
		  }
	  
	  else if(descision=="no")
		  {
		  	$('#deleteUserModal').modal('hide');
  			$('body').removeClass('modal-open');
  			$('.modal-backdrop').remove();
  			$scope.userToDelete = '';
		  }
  }
 
  
   $scope.deleteUser = function(username)
   {
		var User = 
		{
			"name": username
		};
	

//http call to users servlet. DELETE method sends the user's  username the server.

			$http(
		  			{
		  				method: 'DELETE',
		  				url: 'usersServlet',
				        headers: {'Content-Type': 'application/json'},
				        params:User
				        }).then(function(response) {
				        	getAllUsers();
				        		   
				        },
					        	function (failure) {
				        		$scope.failMsg2 = failure.data;
				        });
		   
		   
	  
	  
   }
   
   $scope.getUserName = function(userName)
   {
	   localStorage.setItem("userDetails", userName);
   }


   
   
});
app.component
('users',
		{
			templateUrl: 'users.html',
			controller: 'usersController'
		}
);