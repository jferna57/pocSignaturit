(function() {
    'use strict';

    angular
        .module('pocSignaturitApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('creator', {
            parent: 'entity',
            url: '/creator',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pocSignaturitApp.creator.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/creator/creators.html',
                    controller: 'CreatorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('creator');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('creator-detail', {
            parent: 'entity',
            url: '/creator/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pocSignaturitApp.creator.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/creator/creator-detail.html',
                    controller: 'CreatorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('creator');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Creator', function($stateParams, Creator) {
                    return Creator.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'creator',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('creator-detail.edit', {
            parent: 'creator-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/creator/creator-dialog.html',
                    controller: 'CreatorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Creator', function(Creator) {
                            return Creator.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('creator.new', {
            parent: 'creator',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/creator/creator-dialog.html',
                    controller: 'CreatorDialogController',
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
                    $state.go('creator', null, { reload: 'creator' });
                }, function() {
                    $state.go('creator');
                });
            }]
        })
        .state('creator.edit', {
            parent: 'creator',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/creator/creator-dialog.html',
                    controller: 'CreatorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Creator', function(Creator) {
                            return Creator.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('creator', null, { reload: 'creator' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('creator.delete', {
            parent: 'creator',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/creator/creator-delete-dialog.html',
                    controller: 'CreatorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Creator', function(Creator) {
                            return Creator.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('creator', null, { reload: 'creator' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
