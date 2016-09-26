(function() {
    'use strict';
    angular
        .module('pocSignaturitApp')
        .factory('Creator', Creator);

    Creator.$inject = ['$resource'];

    function Creator ($resource) {
        var resourceUrl =  'api/creators/:id';

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
