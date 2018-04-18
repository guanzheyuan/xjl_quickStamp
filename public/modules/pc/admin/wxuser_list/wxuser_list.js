/**
* 创建时间：2017-09-28 10:59:54
*/
"use strict";
angular.module('wxuser_list', ['ngRoute','selectAddress','ngSanitize','ngKeditor','ui.select'])
	.config(['$routeProvider', function ($routeProvider) {
		$routeProvider.when('/wxuser_list', {
			templateUrl: baseUrl + '/wxuser_list/wxuser_list.html',
				controller: 'WxuserListCtrl'
		});
	}])
	.factory('wxuserListRemote', ['$http', function ($http) {
		return {
			qryList: function (index, size,searchText) {
				return $http({
					method:'post',
					url: '/wechat/VnoService/queryWxuserListByPageList',
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
			}
		}
	}])
	.controller('WxuserListCtrl',['$scope', '$injector','$window', function ($scope, $injector,$window) {
		require(['wxuser_list/wxuser_list.ctrl'], function (ctrl) {
			$injector.invoke(ctrl, this, {'$scope': $scope,'$window':$window});
		});
	}]);
