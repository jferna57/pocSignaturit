(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('CreatorDetailController', CreatorDetailController);

    CreatorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Creator', 'Contract'];

    function CreatorDetailController($scope, $rootScope, $stateParams, previousState, entity, Creator, Contract) {
        var vm = this;

        vm.creator = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pocSignaturitApp:creatorUpdate', function(event, result) {
            vm.creator = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
