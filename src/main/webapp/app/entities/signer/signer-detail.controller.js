(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('SignerDetailController', SignerDetailController);

    SignerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Signer', 'Contract'];

    function SignerDetailController($scope, $rootScope, $stateParams, previousState, entity, Signer, Contract) {
        var vm = this;

        vm.signer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pocSignaturitApp:signerUpdate', function(event, result) {
            vm.signer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
