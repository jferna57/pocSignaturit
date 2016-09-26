(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('signer', {
            parent: 'entity',
            url: '/signer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pocSignaturitApp.signer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/signer/signers.html',
                    controller: 'SignerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('signer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('signer-detail', {
            parent: 'entity',
            url: '/signer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pocSignaturitApp.signer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/signer/signer-detail.html',
                    controller: 'SignerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('signer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Signer', function($stateParams, Signer) {
                    return Signer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'signer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('signer-detail.edit', {
            parent: 'signer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signer/signer-dialog.html',
                    controller: 'SignerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Signer', function(Signer) {
                            return Signer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('signer.new', {
            parent: 'signer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signer/signer-dialog.html',
                    controller: 'SignerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                email: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('signer', null, { reload: 'signer' });
                }, function() {
                    $state.go('signer');
                });
            }]
        })
        .state('signer.edit', {
            parent: 'signer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signer/signer-dialog.html',
                    controller: 'SignerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Signer', function(Signer) {
                            return Signer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('signer', null, { reload: 'signer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('signer.delete', {
            parent: 'signer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signer/signer-delete-dialog.html',
                    controller: 'SignerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Signer', function(Signer) {
                            return Signer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('signer', null, { reload: 'signer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
