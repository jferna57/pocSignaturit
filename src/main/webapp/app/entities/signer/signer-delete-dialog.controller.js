(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('SignerDeleteController',SignerDeleteController);

    SignerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Signer'];

    function SignerDeleteController($uibModalInstance, entity, Signer) {
        var vm = this;

        vm.signer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Signer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
