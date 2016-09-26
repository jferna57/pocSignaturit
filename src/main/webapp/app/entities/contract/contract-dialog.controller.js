(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('ContractDialogController', ContractDialogController);

    ContractDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Contract', 'Signer', 'SignRequest', 'Creator'];

    function ContractDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Contract, Signer, SignRequest, Creator) {
        var vm = this;

        vm.contract = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.signers = Signer.query();
        vm.signrequests = SignRequest.query();
        vm.creators = Creator.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.contract.id !== null) {
                Contract.update(vm.contract, onSaveSuccess, onSaveError);
            } else {
                Contract.save(vm.contract, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pocSignaturitApp:contractUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setDocument = function ($file, contract) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        contract.document = base64Data;
                        contract.documentContentType = $file.type;
                    });
                });
            }
        };

        vm.setDocumentSigned = function ($file, contract) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        contract.documentSigned = base64Data;
                        contract.documentSignedContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.signedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
