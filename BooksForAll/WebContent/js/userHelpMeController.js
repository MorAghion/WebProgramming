/**
 * user help me controller gets the user's previous messages and the admin's response from the server and displays them in a table.
 */

app.controller('userHelpMeController', function ($scope, $rootScope, $http) 
{
	$scope.showAddedmsg=false;
	$scope.user=$scope.getCookie();
	
	
//This function gets all of the user's prev. messages&responses and displays them.
	function getHelpMe() 
	{
		var user=
		{
			"name":$scope.user
		};

	$http(
  			{
  				
  				method: 'GET',
  				url: 'helpMeServlet',
		        headers: {'Content-Type': 'application/json'},
		        params: user
		        }).then(function(response) {
		        		$scope.userData=response.data;								

		        },
			        	function (failure) {
		        		$scope.failMsg = failure.data;

		        });
	}getHelpMe();
	

//This function is invoked when a user wants to send a message to the admin.	
	$scope.addHelpMe = function()
	{
		$('#fromUserModal').modal('show');
	}
	
	 $scope.SubmitHelpMe= function()
	 {
			$scope.user=$scope.getCookie();			
			var myMsg = 
			{
				"username": $scope.user,
				"msg": $scope.msgToAdmin
			};

				
			$http(
		  			{
		  				
		  				method: 'POST',
		  				url: 'helpMeServlet',
				        headers: {'Content-Type': 'application/json'},
				        params: myMsg
				        }).then(function(response) {
				        		$scope.isAdded=response.data;								
				        		if($scope.isAdded=="HelpMe submitted to DB")					
				        		{															
				        			$('#fromUserModal').modal('hide');
				        			$scope.showAddedmsg=true;
				        			getHelpMe();
				        		}
				        		else if(isAdded == "Your massage is too long")
				        			{
				        				$scope.showErrMsg=true;
				        				return;
				        			}
				        		else
				        		{
				        			$('#fromUserModal').modal('show');								
				        		}
				        },
					        	function (failure) {
				        		$scope.ispurch = failure.data;

				        });
	
	
}
		$('#fromUserModal').on('hidden.bs.modal', function (e) {
			  $(this)
			    .find("input,textarea,select")
			       .val('')
			       .end()
			})
	
});


app.component
('userHelpMe',
		{
			templateUrl: 'userHelpMe.html',
			controller: 'userHelpMeController'
		}
);
