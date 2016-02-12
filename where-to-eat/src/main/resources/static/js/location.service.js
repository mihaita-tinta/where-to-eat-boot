(function () {
    'use strict';
    angular
        .module('myApp')
        .factory('LocationService', LocationService);

    LocationService.$inject = ['$http', 'urls'];
    function LocationService($http,urls) {
		var API_USER_URL = urls.apiRoot + '/locations';
        return {
        	list : list,
        	save : save        	
        };

        function list() {
            return $http.get(API_USER_URL).then(handleSuccess, handleError('Error getting all locations'));
        }
        
        function save(location) {
            return $http.post(API_USER_URL, location).then(handleSuccess, handleError('Could not add location'));
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