/**
 * APP CONTROLLER is the main controler of thi web application.
 * The website is a Single Page App website.
 * This controller consists of general function that responsible to the navigation inside the website.
 */

var app = angular.module('app', [ 'main' ]);
var main = angular.module('main', []);

app.controller('appController', function($scope, $rootScope) 
{
	$rootScope.path = "welcome.html";								//$rootScope is a local parameter for every function that initially directs to thw welcome page and login navbar
	$rootScope.navBar = "NavBarLogin.html";
	
	$scope.filePath = $rootScope.path;								
	$scope.navPath = $rootScope.navBar
	
	$scope.newpath = function(pth) 									//the newpath function responsible to navigating between html pages with no need to reload the whole page every time
	{
		$rootScope.path = pth;
	}
	
	
	
	$scope.setNav = function(nav) 									//the setNav function is responsible to switching between 3 different navbars with no need to reload the whole page
	{
		$rootScope.navBar = nav;
	}

	$scope.$watch(function() {										//if there is a change of an html page, $socope.flepath will get this change and the page will be changed to the specified path
		return $rootScope.path;
	}, function() {
		$scope.filePath = $rootScope.path;
	}, true);
	
	
	$scope.$watch(function() {
		return $rootScope.navBar;
	}, function() {
		$scope.navPath = $rootScope.navBar;
	}, true);

	
	$scope.getCookie=function() 
	{
	    var name = "username=";
	    var decodedCookie = decodeURIComponent(document.cookie);
	    var ca = decodedCookie.split(';');
	    for(var i = 0; i <ca.length; i++) 
	    {
	        var c = ca[i];
	        while (c.charAt(0) == ' ') {
	            c = c.substring(1);
	        }
	        if (c.indexOf(name) == 0) {
	            return c.substring(name.length, c.length);
	        }
	    }
	    return "";
	}
	
	
	
	
});