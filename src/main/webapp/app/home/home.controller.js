(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', 'Contract', 'ParseLinks', 'AlertService', '$state'];

    function HomeController ($scope, Principal, LoginService, Contract, ParseLinks, AlertService, $state) {
        var vm = this;

        vm.contracts = [];
        vm.page = 0;
        vm.links = {
            last: 0
        };

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        loadAll();

        function loadAll () {
            Contract.query({page: vm.page,
               size: 20
            }, onSuccess, onError);

            function onSuccess(data, headers) {
               vm.links = ParseLinks.parse(headers('link'));
               vm.totalItems = headers('X-Total-Count');
               for (var i = 0; i < data.length; i++) {
                   vm.contracts.push(data[i]);
               }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }

        }

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
