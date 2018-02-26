/**
 * MANAE REVIEWS CONTROLLER gets all of the pending users' reviews and displays them to the admin.
 * The admin choses weather to approve or decline a review. 
 */

app.controller('manageReviewsController', function ($scope, $rootScope, $http) 
{
	//http call to manageReviews servlet. GET method gets the pending reviews and displays them to the admin. 

	 getManageReviews = function()
	 {
		 $http.get('manageReviewsServlet').then(function (response) {
			     $scope.unApprovedReviews = response.data;
			  }, function(response){
				  $scope.failMsg=response.data;
			  });
	 } 
	 getManageReviews();
	 
//APPROVE function is called when the admin choses to approve a review and clicks on "approve" button. 
//The review is stored in the DB using POST method that sends the review id to the server.

	   $scope.approve = function(id)
	   {
		   var ID = 
			{
				"id": id
			};
		   
		   $http(
		  			{
		  				
		  				method: 'POST',
		  				url: 'manageReviewsServlet',
				        headers: {'Content-Type': 'application/json'},
				        params: ID
				        }).then(function(response) {
				        	 getManageReviews();
				        },
					        	function (failure) {
									$scope.failMsg = failure.data;
				        });
		   
	   }
	
	
//DECLINE function is called when the admin choses to decline a review and clicks on "decline" button.
// The review is deleted from the DB using DELETE method that sends the review id to the server.
	   
	   $scope.decline = function(id)
	   {
		   var ID = 
			{
				"id": id
			};
		   
		   $http(
		  			{
		  				
		  				method: 'DELETE',
		  				url: 'manageReviewsServlet',
				        headers: {'Content-Type': 'application/json'},
				        params: ID
				        }).then(function(response) {
				        	  $http.get('manageReviewsServlet').then(function (response) {
				     		     $scope.unApprovedReviews = response.data;
				     		  }, function(response){
				     			 $scope.failMsg=response.data;
				     		  });
				        },
					        	function (failure) {
									$scope.failMsg = failure.data;
				        });
		   
	   }

});
app.component
('manageReviews',
		{
			templateUrl: 'manageReviews.html',
			controller: 'manageReviewsController'
		}
);