/**
 * WELCOME PAGE CONTROLLER.
 * The first page of the website. Both regisered ueres, un-registered useres and admins see this page.
 * Gets the Cookie
 */

var app = angular.module('main');

app.controller('welcomeController', function($scope,$rootScope,  $http)
{
	
	$scope.user=$scope.getCookie();
  				
	  		
});

app.component
('welcome',
		{
			templateUrl: 'welcome.html',
			controller: 'welcomeController'
		}
);
