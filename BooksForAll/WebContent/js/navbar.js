/**
 *NAVBER CONTROLLER manages and controlls the user's/admin's navigations on the website.
 */

var app = angular.module('main');
app.controller('navbarController', function($scope, $rootScope, $http)
{			
	$scope.siteName = "eBook";
	
	$scope.user=$scope.getCookie();
//LOGOUT function called when a user clicks on "logout" button	
	$scope.logOut=function()
	{
	
	//http call to login servlet. DELETE method is called and deletes the cookie that saved the logged-in user.

		$http(
	  			{
	  				method: 'DELETE',
	  				url: 'loginServlet',
			        headers: {'Content-Type': 'application/json'},
			        }).then(function(response) {
			        		$scope.msg=response.data;
			        		$scope.newpath('welcome.html');
			        		$scope.setNav('NavBarLogin.html');
			        		
			        },
				        	function (failure) {
							$scope.failMsg = failure.data;
							
			        });
	}
	
	 


//SET AGE RANGE function is called when a user choses age range to display books properly	 
	 $scope.SetAgeRange=function(MyageRange)
		{
		  localStorage.setItem("ageRange", MyageRange);								
		  if(MyageRange==0)															//age range "0" - go to all books
			$scope.newpath('books.html');
		  if(MyageRange==1)															//age range "1" - go to ages 5-8 books
				$scope.newpath('AgeRange1.html');
		  if(MyageRange==2)															//age range "2" - go to ages 8-13 books
				$scope.newpath('AgeRange2.html');
		  if(MyageRange==3)															//"age range "3" - go to ages 13 and above books
				$scope.newpath('AgeRange3.html');
		}

//LOGIN function called when a user clicks on "login" button
	$scope.login=function()
	{
		var loginUser = 
		{
			"username": $scope.username,
			"password":$scope.password,
		};
		
		var username=$scope.username;
		var password=$scope.password;
		
		
	  	if(username==null || username=="" || 																		//if the username or password are invalid, show error message.
	  			password==null || password=="")
		{
			$scope.errLoginMsg = true;
			return;
		}	
	  	
		//http call to login servlet. POST method sends an object that contains username and password to the server.
	  	$http(
	  			{
	  				method: 'POST',
	  				url: 'loginServlet',
			        headers: {'Content-Type': 'application/json'},
			        params: loginUser
			        }).then(function(response) {
			        		$scope.msg=response.data;
			        		if($scope.msg=="Incorrect user name or password, please try again")						//if the username doesn't exists, a boolean var turns "true"
			        		{																						//and using "ng-show", an error message is displayed.
			        			$scope.loginResult=false;
			        			$scope.errIncorrectUser = true;
			        			
			        		}
			        		else																					//if the user exists, the function checks if it is admin or a user.
			        		{
			        			$scope.loginResult=true;
				        		$scope.isAdmin=response.data;
				        		if($scope.isAdmin=="isAdmin")														//if it's admin, navigate to landing page and display admin's navbar
				        			{
					        			$scope.newpath('landingPage.html')
						        		$scope.setNav('NavBarAdmin.html');
				        			}
				        		else																				//if it's a user, navigate to landing page and display user's navbar
				        			{
					        			$scope.newpath('landingPage.html')
						        		$scope.setNav('NavBarUser.html');
				        			}
			        		}
			        },
				        	function (failure) {
							$scope.failMsg = failure.data;	
			        
			        	
			        });
	}
	}

);

app.component
('navbar',
		{
			templateUrl: 'navbar.html',
			controller: 'navbarController'
		}
);