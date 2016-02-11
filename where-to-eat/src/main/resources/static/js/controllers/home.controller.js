(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['UserService', 'VoteService', '$rootScope'];
    function HomeController(UserService, VoteService, $rootScope) {
        var vm = this;

        vm.user = null;
        vm.allUsers = [];
        
        initController();

        function initController() {
            loadCurrentUser();
            loadAllUsers();
        }

        function loadCurrentUser() {
            UserService.getCurrentUser()
                .then(function (user) {
                    vm.user = user;
                });
        }

        function loadAllUsers() {
            UserService.list()
                .then(function (users) {
                    vm.allUsers = users;
                });
        }

    }

})();