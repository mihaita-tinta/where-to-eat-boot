(function () {
    'use strict';

    angular
        .module('myApp')
        .factory('AuthenticationService', AuthenticationService);
	
    AuthenticationService.$inject = ['$http', '$cookieStore', '$rootScope', '$timeout', 'UserService', 'OAuth', 'OAuthToken', 'urls'];
    function AuthenticationService($http, $cookieStore, $rootScope, $timeout, UserService, OAuth, OAuthToken, urls) {
        var service = {};

        service.Login = Login;
        service.SetCredentials = SetCredentials;
        service.ClearCredentials = ClearCredentials;
        service.register = register;// after login we get the user with this call

        return service;

        function Login(username, password, callback) {
			
            $http.defaults.headers.common['Authorization'] = 'Basic ' + btoa("gui:mihabc"); // jshint ignore:line
			OAuth.getAccessToken({username: username, password: password}, {
    	            	'client_id':'gui', "grant_type":'password',
    	            	'username':username, 'password':password
    	    }).then(function(response) {		
				UserService.getCurrentUser().then(function (user) {
					if (user) {
						SetCredentials(user);
						callback({ success: true });
					} else {
						callback({message: 'Server is down :('});
					}
				});
            }, function (error) {
				callback({message: error.data.error_description});
			});
        }

        function SetCredentials(user) {
            $rootScope.globals = {
                currentUser: user
            };

            $cookieStore.put('globals', $rootScope.globals);
        }

        function register(user) {
            return $http.post(urls.apiRoot + '/register', user)
						.then(function successCallback(res) {
							return res.data;
						}, function errorCallback(data, status, headers, config) {
							return { success: false, message: data.data.message };
						});
        }
		
        function ClearCredentials() {
            $rootScope.globals = {};
            $cookieStore.remove('globals');
			OAuthToken.removeToken();
        }		
    }

})();