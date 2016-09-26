(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('SignRequestDialogController', SignRequestDialogController);

    SignRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'SignRequest', 'Contract'];

    function SignRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, SignRequest, Contract) {
        var vm = this;

        vm.signRequest = entity;
        vm.clear = clear;
        vm.save = save;
        vm.contracts = Contract.query({filter: 'signrequest-is-null'});
        $q.all([vm.signRequest.$promise, vm.contracts.$promise]).then(function() {
            if (!vm.signRequest.contractId) {
                return $q.reject();
            }
            return Contract.get({id : vm.signRequest.contractId}).$promise;
        }).then(function(contract) {
            vm.contracts.push(contract);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.signRequest.id !== null) {
                SignRequest.update(vm.signRequest, onSaveSuccess, onSaveError);
            } else {
                SignRequest.save(vm.signRequest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pocSignaturitApp:signRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
