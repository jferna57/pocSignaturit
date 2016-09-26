(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('ContractDetailController', ContractDetailController);

    ContractDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Contract', 'Signer', 'Creator'];

    function ContractDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Contract, Signer, Creator) {
        var vm = this;

        vm.contract = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('pocSignaturitApp:contractUpdate', function(event, result) {
            vm.contract = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
