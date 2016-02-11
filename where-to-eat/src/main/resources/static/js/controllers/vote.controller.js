(function () {
    'use strict';

    angular
        .module('myApp')
        .controller('VoteController', VoteController);

    VoteController.$inject = ['LocationService', 'VoteService', 'FlashService', '$rootScope'];
    function VoteController(LocationService, VoteService, FlashService, $rootScope) {
        var vm = this;

        vm.locations = [];
        vm.results = null;
        init();
        
        function init() {
        	list();
        	reloadResults();
        	vm.showChart = false;
        }
        
        function reloadResults() {

            VoteService.todayPoll()
			            .then(function (results) {
			            	vm.results = results;
			            	$rootScope.todayLocation = location(results[results.length - 1].locationId);
			            });
        }
        function list() {
        	LocationService.list()
                .then(function (locations) {
                    vm.locations = locations;
                });
        }
        
        vm.vote = function(location) {
        	VoteService.vote(location)
        	.then(function(response) {
            	$rootScope.votedLocation = location;
                FlashService.success('Vote for ' + location.name + ' was successful');
            	reloadResults();
        	});
        }
        vm.toggleChart = function() {
        	vm.showChart = !vm.showChart;
        }
        vm.location = location;
        
        function location(locationId) {
        	 var len=vm.locations.length;
        	    for (var i=0; i<len; i++) {
        	      if (vm.locations[i].locationId == locationId) {
        	        return vm.locations[i];
        	      }
        	    }
        	
        }

    }

})();