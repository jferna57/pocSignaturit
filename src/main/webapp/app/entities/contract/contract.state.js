(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contract', {
            parent: 'entity',
            url: '/contract',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pocSignaturitApp.contract.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contract/contracts.html',
                    controller: 'ContractController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contract');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contract-detail', {
            parent: 'entity',
            url: '/contract/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pocSignaturitApp.contract.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contract/contract-detail.html',
                    controller: 'ContractDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contract');
                    $translatePartialLoader.addPart('status');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Contract', function($stateParams, Contract) {
                    return Contract.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'contract',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('contract-detail.edit', {
            parent: 'contract-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contract/contract-dialog.html',
                    controller: 'ContractDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contract', function(Contract) {
                            return Contract.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contract.new', {
            parent: 'contract',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contract/contract-dialog.html',
                    controller: 'ContractDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                contractName: null,
                                document: null,
                                documentContentType: null,
                                status: null,
                                documentSigned: null,
                                documentSignedContentType: null,
                                creationDate: null,
                                signedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('contract', null, { reload: 'contract' });
                }, function() {
                    $state.go('contract');
                });
            }]
        })
        .state('contract.edit', {
            parent: 'contract',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contract/contract-dialog.html',
                    controller: 'ContractDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contract', function(Contract) {
                            return Contract.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contract', null, { reload: 'contract' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contract.delete', {
            parent: 'contract',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contract/contract-delete-dialog.html',
                    controller: 'ContractDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Contract', function(Contract) {
                            return Contract.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contract', null, { reload: 'contract' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
