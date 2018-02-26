/**
 * admin help me controller gets the users' messages from the server and displays them in a table to the admin.
 * the admin can also responde to users.
 */

app.controller('adminHelpMeController', function ($scope, $rootScope, $http) 
{
	getadminHelpMe=function()
	{
		
	$http(
  			{
  				
  				method: 'GET',
  				url: 'adminHelpMeServlet',
		        headers: {'Content-Type': 'application/json'},
		        }).then(function(response) {
		        		$scope.toAdminData=response.data;	
		        },
			        	function (failure) {
		        		$scope.failMsg = failure.data;

		        });
	}
	getadminHelpMe();
	
	$scope.answerHelpMe = function(id)
	{
		$('#toUserModal').modal('show');
		$scope.myId=id;
	}
	
	
	 $scope.SubmitResponse= function()
	 {
			$scope.user=$scope.getCookie();			
			var myMsg = 
			{
				"myResponse": $scope.msgToUser,
				"id": $scope.myId
			};

				//http call to the servlet. POST method sends the response and id to to the server.
		
			$http(
		  			{
		  				
		  				method: 'POST',
		  				url: 'adminHelpMeServlet',
				        headers: {'Content-Type': 'application/json'},
				        params: myMsg
				        }).then(function(response) {
				        		$scope.isAdded=true;
				        		
				        		if(response.data == "Your response is too long")
				        			{
				        				$scope.showErrMsg=true;
				        				return;
				        			}
				        		$('#toUserModal').modal('hide');
				        		getadminHelpMe();

				        },
					        	function (failure) {
				        		$scope.failMgs = failure.data;

				        });

}
	 
		$('#toUserModal').on('hidden.bs.modal', function (e) {
			  $(this)
			    .find("input,textarea,select")
			       .val('')
			       .end()
			})
	

});







app.component
('adminHelpMe',
		{
			templateUrl: 'adminHelpMe.html',
			controller: 'adminHelpMeController'
		}
);
