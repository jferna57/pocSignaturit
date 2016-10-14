(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('MenuController', MenuController);

    MenuController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function MenuController ($scope, Principal, LoginService, $state) {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;


    }
})();
