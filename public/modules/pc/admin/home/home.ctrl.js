/**
 * Created by nalantianyi on 15/12/1.
 */
'use strict';
define([], function () {
    return ['$scope', 'homeRemote', function ($scope, homeRemote) {
        console.log('Home:' + 'roleCode:', 1);
        $scope.roleCode = 1;
        $scope.operator = {
            vno1: 0,
            vno2: 0,
            questionNum: 0
        };

        homeRemote.getVnoNum1(1).success(function (res) {
        	if(res.ret=="0"){
                $scope.operator.vnoCurrentMonthTotal = res.data;
        	}else{
                $scope.operator.vnoCurrentMonthTotal = "0";
        	}
        });
        homeRemote.getVnoNum2(1).success(function (res) {
        	if(res.ret=="0"){
                $scope.operator.vnoTotal = res.data;
        	}else{
                $scope.operator.vnoTotal = "0";
        	}
        });
        homeRemote.getQuestionNum(1).success(function (res) {
            $scope.operator.questionNum = res.data;
        });

        $scope.$apply();
    }];
});