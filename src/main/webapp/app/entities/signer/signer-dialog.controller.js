(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('SignerDialogController', SignerDialogController);

    SignerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Signer', 'Contract'];

    function SignerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Signer, Contract) {
        var vm = this;

        vm.signer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.contracts = Contract.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.signer.id !== null) {
                Signer.update(vm.signer, onSaveSuccess, onSaveError);
            } else {
                Signer.save(vm.signer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pocSignaturitApp:signerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
