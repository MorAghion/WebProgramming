/**
 * registration Controller
 * This controller consists of the functions that helps a new user register to the website
 */

app.controller('registrationController', function($scope, $rootScope, $http)
{

//Register function called when a user clicks on "register" button after filling 
//all of the required fields of the registration form

	$scope.Register=function()
	{
		//getting prefix value of seleted element in "phone" field 
		var e=document.getElementById("prefix");
		var pre=e.options[e.selectedIndex].text;
		var totalPhone=pre+$scope.phone;
		
		//binding the elements of the html file into a var that will be sent to the servlet
		var registrationUser = 
			{
				"username": $scope.username,
				"email":$scope.email,
				"password":$scope.password,
				"nickname":$scope.nickname,
				"description":$scope.description,
				"phone": totalPhone,
				"photo":$scope.photo,
				"city": $scope.city,
				"street": $scope.street,
				"number": $scope.number,
				"zipcode":$scope.zipcode,
			};
		
		var username=$scope.username;
		var password=$scope.password;
		var nickname=$scope.nickname;
		var phone=$scope.phone;
		var city=$scope.city;
		var street=$scope.street;
		var number=$scope.number;
        var zipcode=$scope.zipcode;

		
			//http call to registration servlet, POST method sends to the server the object that was formed earlier 

					$http(
							{
								method: 'POST',
								url: 'registrationServlet',
						         headers: {'Content-Type': 'application/json'},
						         data: registrationUser
							}).then(function (response) {
								  $scope.msg = response.data;					//$scope.msg consists of the response fro the servlet
							      if($scope.msg == "User name exists")			//user name is unique. If already exists server will output the suitable message
							    	  {
							    	  	$scope.userExists = true;
							    	  	$scope.regSucceeded = false;
							    	  	$scope.somethingWrong = false;
							    	  }
							      else if($scope.msg == "registration succeeded")		//If registration succeeded server will output the suitable message
							    	  {
							    	  	$scope.regSucceeded = true;
							    	  	$scope.userExists = false;
							    	  	$scope.somethingWrong = false;
							    	  	$scope.setNav('NavBarUser.html');				//Change to user nav bar
							    	  	$scope.newpath('landingPage.html');				//go to landing page after registration
							    	  	
							    	  }
							      else
							    	  {
							    	  	$scope.somethingWrong = true;
							    	  	$scope.userExists = false;
							    	  	$scope.regSucceeded = false;
							    	  }
							  
				},
				      function (failure) {
					$scope.failMsg=response.data;
				});
	}
	
			
		
});


	
	
app.component
('registration',
		{
			templateUrl: 'registration.html',
			controller: 'registrationController'
		}
);


