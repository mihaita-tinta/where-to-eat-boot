'use strict';
var API_BASE_URL = 'http://localhost:8080';

angular.module('myApp', [
    'ngTouch',
    'ngRoute',
    'ngCookies',
    'ngAnimate',
	'angular-oauth2'
])
.value('urls', {
						baseUrl : API_BASE_URL,
						apiRoot : API_BASE_URL + '/api'
})
.config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
	 $routeProvider
            .when('/', {
                controller: 'HomeController',
                templateUrl: 'partials/home.html',
                controllerAs: 'vm'
            })
            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'partials/login.html',
                controllerAs: 'vm'
            })
            .when('/register', {
                controller: 'RegisterController',
                templateUrl: 'partials/register.html',
                controllerAs: 'vm'
            })
            .when('/vote', {
                controller: 'VoteController',
                templateUrl: 'partials/vote.html',
                controllerAs: 'vm'
            })
            .otherwise({ redirectTo: '/login' });
		
	$httpProvider.defaults.headers.common['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';	
}])
.config(['OAuthProvider', 'OAuthTokenProvider', function(OAuthProvider, OAuthTokenProvider) {
    OAuthProvider.configure({
      clientId: 'gui',
      clientSecret: 'mihabc',
	  baseUrl: API_BASE_URL,
	  grantPath: '/oauth/token',
	  revokePath: '/oauth/revoke',
      response_type: 'token',
      redirect_uri:  '/login?callback',
    });
	
	OAuthTokenProvider.configure({
	  name: 'token',
		options: {
		  secure: false
		}
	});
  }])
.run(['$rootScope', '$window', 'OAuth', '$location', '$cookieStore', '$http', 'WebSocketService', '$timeout',
      function($rootScope, $window, OAuth, $location, $cookieStore, $http, WebSocketService, $timeout) {
    $rootScope.$on('oauth:error', function(event, rejection) {
      // Ignore `invalid_grant` error - should be catched on `LoginController`.
      if ('invalid_grant' === rejection.data.error) {
        return;
      }

      // Refresh token when a `invalid_token` error occurs.
      if ('invalid_token' === rejection.data.error) {
        return OAuth.getRefreshToken();
      }

      // Redirect to `/login` with the `error_reason`.
	  return $location.path('/login?error_reason=' + rejection.data.error);
    });
	
	// keep user logged in after page refresh
    $rootScope.globals = $cookieStore.get('globals') || {};

    $rootScope.$on('$locationChangeStart', function (event, next, current) {
		// redirect to login page if not logged in and trying to access a restricted page
		var allowedPage = ['/login', '/register'].indexOf($location.path()) === -1;

		if ('/' != $location.path() && '' != $location.path() && '/vote'.indexOf($location.path()) === 0)
			$rootScope.isVotePage = true;
		else
			$rootScope.isVotePage = false;
		
		var loggedIn = $rootScope.globals.currentUser;
		if (allowedPage == true && !loggedIn) {
					$location.path('/login');
		}

	    // websocket
	    if (loggedIn && current.indexOf('/login')<0 && next.indexOf('/logout')<0) {
	    	WebSocketService.connectIfNotConnected();
	    }
	    	
    });
    
  }]);