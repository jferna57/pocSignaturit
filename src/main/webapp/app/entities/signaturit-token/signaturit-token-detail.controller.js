(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('SignaturitTokenDetailController', SignaturitTokenDetailController);

    SignaturitTokenDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SignaturitToken'];

    function SignaturitTokenDetailController($scope, $rootScope, $stateParams, previousState, entity, SignaturitToken) {
        var vm = this;

        vm.signaturitToken = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pocSignaturitApp:signaturitTokenUpdate', function(event, result) {
            vm.signaturitToken = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
