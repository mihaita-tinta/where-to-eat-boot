(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['AuthenticationService', '$location', '$rootScope', 'FlashService'];
    function RegisterController(AuthenticationService, $location, $rootScope, FlashService) {
        var vm = this;

        vm.register = register;

        function register() {
        	if (vm.user.password != vm.user.passwordConfirmation) {
                FlashService.error('Password does not match');
                return;
        	}
        	
            vm.dataLoading = true;
            AuthenticationService.register({id:null, email:vm.user.username, password: vm.user.password})
                .then(function successCallback(response) {
                	if (response.success == false) {
                        FlashService.error(response.message);
                        vm.dataLoading = false;
                	} else {
                        FlashService.success('Registration successful', true);
                        vm.dataLoading = false;
                        $location.path('/login');
                	}
				});
        }
    }

})();