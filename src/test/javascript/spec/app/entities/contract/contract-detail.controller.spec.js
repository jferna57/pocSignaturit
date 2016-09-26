'use strict';

describe('Controller Tests', function() {

    describe('Contract Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockContract, MockSigner, MockCreator;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockContract = jasmine.createSpy('MockContract');
            MockSigner = jasmine.createSpy('MockSigner');
            MockCreator = jasmine.createSpy('MockCreator');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Contract': MockContract,
                'Signer': MockSigner,
                'Creator': MockCreator
            };
            createController = function() {
                $injector.get('$controller')("ContractDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pocSignaturitApp:contractUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
