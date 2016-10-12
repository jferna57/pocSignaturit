(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sign-request', {
            parent: 'entity',
            url: '/sign-request',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pocSignaturitApp.signRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sign-request/sign-requests.html',
                    controller: 'SignRequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('signRequest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sign-request-detail', {
            parent: 'entity',
            url: '/sign-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pocSignaturitApp.signRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sign-request/sign-request-detail.html',
                    controller: 'SignRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('signRequest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SignRequest', function($stateParams, SignRequest) {
                    return SignRequest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sign-request',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sign-request-detail.edit', {
            parent: 'sign-request-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sign-request/sign-request-dialog.html',
                    controller: 'SignRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SignRequest', function(SignRequest) {
                            return SignRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sign-request.new', {
            parent: 'sign-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sign-request/sign-request-dialog.html',
                    controller: 'SignRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                bodyEmail: null,
                                subjectEmail: null,
                                signaturitId: null,
                                signaturitDocumentId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sign-request', null, { reload: 'sign-request' });
                }, function() {
                    $state.go('sign-request');
                });
            }]
        })
        .state('sign-request.edit', {
            parent: 'sign-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sign-request/sign-request-dialog.html',
                    controller: 'SignRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SignRequest', function(SignRequest) {
                            return SignRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sign-request', null, { reload: 'sign-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sign-request.delete', {
            parent: 'sign-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sign-request/sign-request-delete-dialog.html',
                    controller: 'SignRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SignRequest', function(SignRequest) {
                            return SignRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sign-request', null, { reload: 'sign-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
