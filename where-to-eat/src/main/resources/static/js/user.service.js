(function () {
    'use strict';
    angular
        .module('myApp')
        .factory('UserService', UserService);

    UserService.$inject = ['$http', 'OAuth', 'urls'];
    function UserService($http, OAuth, urls) {
		var API_USER_URL = urls.apiRoot + '/users';
        return {
        	getCurrentUser : getCurrentUser,
        	list : list        	
        };

        function list() {
            return $http.get(API_USER_URL).then(handleSuccess, handleError('Error getting all users'));
        }
        
        function getCurrentUser(username) {
            return $http.get(API_USER_URL + '/account').then(handleSuccess, handleError('Aren\'t you logged in???'));
        }

        // private functions

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }

})();