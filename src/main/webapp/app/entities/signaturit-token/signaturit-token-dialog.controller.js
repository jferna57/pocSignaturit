(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('SignaturitTokenDialogController', SignaturitTokenDialogController);

    SignaturitTokenDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SignaturitToken'];

    function SignaturitTokenDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SignaturitToken) {
        var vm = this;

        vm.signaturitToken = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.signaturitToken.id !== null) {
                SignaturitToken.update(vm.signaturitToken, onSaveSuccess, onSaveError);
            } else {
                SignaturitToken.save(vm.signaturitToken, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pocSignaturitApp:signaturitTokenUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
