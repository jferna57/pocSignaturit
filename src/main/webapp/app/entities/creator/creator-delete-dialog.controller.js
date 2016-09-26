(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('CreatorDeleteController',CreatorDeleteController);

    CreatorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Creator'];

    function CreatorDeleteController($uibModalInstance, entity, Creator) {
        var vm = this;

        vm.creator = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Creator.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
