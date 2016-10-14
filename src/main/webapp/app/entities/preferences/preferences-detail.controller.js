(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('PreferencesDetailController', PreferencesDetailController);

    PreferencesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Preferences'];

    function PreferencesDetailController($scope, $rootScope, $stateParams, previousState, entity, Preferences) {
        var vm = this;

        vm.preferences = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pocSignaturitApp:preferencesUpdate', function(event, result) {
            vm.preferences = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
