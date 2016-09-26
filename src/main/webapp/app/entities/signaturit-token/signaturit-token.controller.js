(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .controller('SignaturitTokenController', SignaturitTokenController);

    SignaturitTokenController.$inject = ['$scope', '$state', 'SignaturitToken'];

    function SignaturitTokenController ($scope, $state, SignaturitToken) {
        var vm = this;
        
        vm.signaturitTokens = [];

        loadAll();

        function loadAll() {
            SignaturitToken.query(function(result) {
                vm.signaturitTokens = result;
            });
        }
    }
})();
