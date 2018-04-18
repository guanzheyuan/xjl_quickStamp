"use strict";
angular.module('group_exc', ['ngRoute','selectAddress','ngSanitize','ngKeditor','ui.select'])
	.config(['$routeProvider', function ($routeProvider) {
		$routeProvider.when('/group_exc', {
			templateUrl: baseUrl + '/group_exc/group_exc.html',
				controller: 'groupExcCtrl'
		})
	}])
	.factory('groupExcList', ['$http', function ($http) {
			return {
				qryList: function (index, size,searchText) {
					return $http({
						method:'post',
						url: '/dw/mobile/ActivityService/queryGroupPageForPc',
						params: {
							PAGE_INDEX: index,
							PAGE_SIZE: size,
							searchKeyWord: searchText || ''
						}
					});
				},
				objSave: function (obj) {
					return $http({
						method:'post',
						url: '/wechat/VnoService/saveWxuserList',
						params: obj
					});
				},
				objDelete: function (obj) {
					return $http({
						method:'post',
						url: '/wechat/VnoService/deleteWxuserList',
						params: obj
					});
				},
				printExcel:function(obj){
					return $http({
						method:'post',
						url: '/dw/mobile/ActivityService/printExcel',
						params:{
							groupBuyId:obj.groupBuyId
						}
					});
				}
			}
		}])
	.controller('groupExcCtrl',['$scope', '$injector','$window', function ($scope, $injector,$window) {
		require(['group_exc/group_exc.ctrl'], function (ctrl) {
			$injector.invoke(ctrl, this, {'$scope': $scope,'$window':$window});
		});
	}]);
