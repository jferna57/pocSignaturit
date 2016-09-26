(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('signaturit-token', {
            parent: 'entity',
            url: '/signaturit-token',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pocSignaturitApp.signaturitToken.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/signaturit-token/signaturit-tokens.html',
                    controller: 'SignaturitTokenController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('signaturitToken');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('signaturit-token-detail', {
            parent: 'entity',
            url: '/signaturit-token/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pocSignaturitApp.signaturitToken.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/signaturit-token/signaturit-token-detail.html',
                    controller: 'SignaturitTokenDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('signaturitToken');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SignaturitToken', function($stateParams, SignaturitToken) {
                    return SignaturitToken.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'signaturit-token',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('signaturit-token-detail.edit', {
            parent: 'signaturit-token-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signaturit-token/signaturit-token-dialog.html',
                    controller: 'SignaturitTokenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SignaturitToken', function(SignaturitToken) {
                            return SignaturitToken.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('signaturit-token.new', {
            parent: 'signaturit-token',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signaturit-token/signaturit-token-dialog.html',
                    controller: 'SignaturitTokenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                token: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('signaturit-token', null, { reload: 'signaturit-token' });
                }, function() {
                    $state.go('signaturit-token');
                });
            }]
        })
        .state('signaturit-token.edit', {
            parent: 'signaturit-token',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signaturit-token/signaturit-token-dialog.html',
                    controller: 'SignaturitTokenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SignaturitToken', function(SignaturitToken) {
                            return SignaturitToken.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('signaturit-token', null, { reload: 'signaturit-token' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('signaturit-token.delete', {
            parent: 'signaturit-token',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signaturit-token/signaturit-token-delete-dialog.html',
                    controller: 'SignaturitTokenDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SignaturitToken', function(SignaturitToken) {
                            return SignaturitToken.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('signaturit-token', null, { reload: 'signaturit-token' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
