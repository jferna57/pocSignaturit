(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('SignRequestDeleteController',SignRequestDeleteController);

    SignRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'SignRequest'];

    function SignRequestDeleteController($uibModalInstance, entity, SignRequest) {
        var vm = this;

        vm.signRequest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SignRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
