(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('SignaturitTokenDeleteController',SignaturitTokenDeleteController);

    SignaturitTokenDeleteController.$inject = ['$uibModalInstance', 'entity', 'SignaturitToken'];

    function SignaturitTokenDeleteController($uibModalInstance, entity, SignaturitToken) {
        var vm = this;

        vm.signaturitToken = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SignaturitToken.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
