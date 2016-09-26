(function() {
    'use strict';
    angular
        .module('pocSignaturitApp')
        .factory('Contract', Contract);

    Contract.$inject = ['$resource', 'DateUtils'];

    function Contract ($resource, DateUtils) {
        var resourceUrl =  'api/contracts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                        data.sentToBeSignedDate = DateUtils.convertDateTimeFromServer(data.sentToBeSignedDate);
                        data.signedDate = DateUtils.convertDateTimeFromServer(data.signedDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
