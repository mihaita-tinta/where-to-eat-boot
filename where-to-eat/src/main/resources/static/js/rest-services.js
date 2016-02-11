'use strict';

angular.module('myApp.restServices', ['ngResource', 'angular-oauth2'])
    .factory('Employee', ['$resource',
        function ($resource) {
            return $resource('http://localhost:8080/api/user/list/:employeeId', {});
        }])

    .factory('Report', ['$resource',
        function ($resource) {
            return $resource('http://localhost:8080/api/user/list/:employeeId/reports', {});
        }]);