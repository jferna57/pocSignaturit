(function() {
    'use strict';
    angular
        .module('pocSignaturitApp')
        .factory('SignaturitToken', SignaturitToken);

    SignaturitToken.$inject = ['$resource'];

    function SignaturitToken ($resource) {
        var resourceUrl =  'api/signaturit-tokens/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
