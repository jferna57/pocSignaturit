(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('CreatorDialogController', CreatorDialogController);

    CreatorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Creator', 'Contract'];

    function CreatorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Creator, Contract) {
        var vm = this;

        vm.creator = entity;
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
            if (vm.creator.id !== null) {
                Creator.update(vm.creator, onSaveSuccess, onSaveError);
            } else {
                Creator.save(vm.creator, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pocSignaturitApp:creatorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
