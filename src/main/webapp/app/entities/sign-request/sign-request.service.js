(function() {
    'use strict';
    angular
        .module('pocSignaturitApp')
        .factory('SignRequest', SignRequest);

    SignRequest.$inject = ['$resource'];

    function SignRequest ($resource) {
        var resourceUrl =  'api/sign-requests/:id';

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
