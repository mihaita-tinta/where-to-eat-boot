(function () {
    'use strict';
    angular
        .module('myApp')
        .factory('VoteService', VoteService);

    VoteService.$inject = ['$http', 'urls'];
    function VoteService($http,urls) {
		var API_USER_URL = urls.apiRoot + '/voting';
        return {
        	todayPoll : todayPoll,
        	vote : vote        	
        };

        function todayPoll() {
            return $http.get(API_USER_URL + '/todayPoll').then(handleSuccess, handleError('Error getting all todayPoll'));
        }
        
        function vote(location) {
            return $http.post(API_USER_URL, {
            	location:location,
            	comment: 'voted from gui'
            }).then(handleSuccess, handleError('Could not vote'));
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