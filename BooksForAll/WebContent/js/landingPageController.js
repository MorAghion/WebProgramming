/**
 * LANDING PAGE CONTROLLER
 */

var app = angular.module('main');

app.controller('landingPageController',function($scope, $rootScope, $http)
{
	//$('body').css({"background":"linear-gradient(to bottom, PaleTurquoise  , #ffff99 70%, white )", "padding":"0", "background-repeat": "no-repeat", "background-size": "100%"});

});

app.component
('landingPage',
		{
			templateUrl: 'landingPage.html',
			controller: 'landingPageController'
		}
);
