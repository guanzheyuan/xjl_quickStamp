/**
 * Created by nalantianyi on 15/11/27.
 */
'use strict';
//define([
//    'angular',
//    'angularRoute'
//], function (angular) {
    angular.module('home', ['ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/home', {
                templateUrl: baseUrl + '/home/home.html',
                controller: 'HomeCtrl'
            });
        }])
        .factory('homeRemote', ['$http', function ($http) {

            return {

                getVnoNum1: function (custManagerId) {
                    return $http({
                    	method:'post',
                        url: '/zzb/CustManagerService/countCurrentMonthVnoByCustManagerId',
                        params: {
                            custManagerId: custManagerId
                        }
                    });
                },

                getVnoNum2: function (custManagerId) {
                    return $http({
                    	method:'post',
                        url: '/zzb/CustManagerService/countVnoByCustManagerId',
                        params: {
                            custManagerId: custManagerId
                        }
                    });
                },
                getQuestionNum:function(custManagerId){
                    return $http({
                    	method:'post',
                        url: '/zzb/CustManagerService/queryQuestionNumByCustManagerId',
                        params: {
                            custManagerId: custManagerId
                        }
                    });
                }
            };
        }])
        .controller('HomeCtrl', ['$scope', '$injector', 'homeRemote','$rootScope', function ($scope, $injector, homeRemote,$rootScope) {
            $rootScope.isShowSubMenu = false;
            require(['home/home.ctrl'], function (ctrl) {
                $injector.invoke(ctrl, this, {'$scope': $scope, 'homeRemote': homeRemote});
            });

        }]);
//});

