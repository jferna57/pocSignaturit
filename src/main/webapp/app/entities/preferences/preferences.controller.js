(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('PreferencesController', PreferencesController);

    PreferencesController.$inject = ['$scope', '$state', 'Preferences'];

    function PreferencesController ($scope, $state, Preferences) {
        var vm = this;
        
        vm.preferences = [];

        loadAll();

        function loadAll() {
            Preferences.query(function(result) {
                vm.preferences = result;
            });
        }
    }
})();
