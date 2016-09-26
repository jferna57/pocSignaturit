(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('SignRequestDetailController', SignRequestDetailController);

    SignRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SignRequest', 'Contract'];

    function SignRequestDetailController($scope, $rootScope, $stateParams, previousState, entity, SignRequest, Contract) {
        var vm = this;

        vm.signRequest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pocSignaturitApp:signRequestUpdate', function(event, result) {
            vm.signRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
