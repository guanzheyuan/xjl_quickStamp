/**
* 创建时间：2017-09-28 10:59:54
*/
"use strict";
define([], function () {
	return ['$scope', 'groupExcList', 'Upload','$window', function ($scope, queryRemote,Upload,$window) {
		//分页索引
		$scope.pageIndex = 1;
		//页面的大小
		$scope.pageSize = 10;
		//总页数
		$scope.pages = 1;
		
		//显示页面：列表list与编辑edit
		$scope.state = 'list';
		
		//搜索关键字
		$scope.searchText = '';
		//当前编辑的系统配置项
		$scope.sysparam = {};
		//总条数
		$scope.total = 0;
		//当前数据列表
		$scope.objs = [];
		
		//查询列表函数
		var loadList = function (index, size, searchText) {
			queryRemote.qryList(index, size, searchText).success(function (res) {
				console.log(res);
				if(res.ret=="0"){
					$scope.objs = res.data.data|| [];
					$scope.total = res.data.total || 0;
					if($scope.total!=0){
						 if($scope.total % $scope.pageSize>0){
							 $scope.pages = parseInt($scope.total / $scope.pageSize) + 1;
						 }else{
							 $scope.pages = parseInt($scope.total / $scope.pageSize);
						 }
					 }else{
						 $scope.pages = 1;
						 $scope.pageIndex = 1;
					 }
				}else{
					layer.msg('请求失败，请重试');
				}
			});
		};
		loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());

		//首页
		$scope.home = function () {
			if ($scope.pageIndex == 1) {
				layer.msg('已经是首页了');
			}
			else {
				$scope.pageIndex = 1;
				loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());
			}
		};

		//前一页
		$scope.prev = function () {
			if ($scope.pageIndex == 1) {
				layer.msg('没有上一页了');
			}
			else {
				$scope.pageIndex--;
				loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());
			}
		};

		//下一页
		$scope.next = function () {
			if ($scope.pageIndex == $scope.pages) {
				layer.msg('没有下一页了');
			}
			else {
				$scope.pageIndex++;
				loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());
			}
		};

		//最后一页
		$scope.last = function () {
			if ($scope.pageIndex == $scope.pages) {
				layer.msg('已经是尾页了');
			}
			else {
				$scope.pageIndex = $scope.pages;
				loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());
			}
		};
		//搜索按钮
		$scope.search = function(){
			$scope.pageIndex = 1;
			loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());
		}
		//重置按钮
		$scope.reset = function(){
			$scope.searchText='';
			loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());
		}
		//添加
		$scope.gotoAdd = function () {
			$scope.stateInfo = "添加";
			$scope.currentError={};
			$scope.state = 'edit';
		};

		//编辑
		$scope.gotoEdit = function (obj) {
			$scope.stateInfo = "编辑";
			$scope.currentError={};
			$scope.obj = obj;
			$scope.state = 'edit';
		};

		//添加、编辑->保存
		$scope.objSave = function () {
			//容错信息提示对象
			$scope.currentError={};
			if ($scope.obj.objTitle == "") {
				$scope.currentError.objTitle='请输入标题';
				return;
			}
			queryRemote.objSave($scope.obj).success(function (res) {
				if (res.ret == "0") {
					loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());
					$scope.state = 'list';
				}else{
					layer.msg('保存失败，message：'+res.desc);
				}
			});
		};

		//取消
		$scope.save_cancel = function () {
			$scope.state = 'list';
			//loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());
		};
		$scope.printExcel =function(obj){
			queryRemote.printExcel(obj).success(function(res){
				console.log(res);
				if(res.ret =='0'){
					window.open(res.data);
				}
			});
		}
		//删除
		$scope.objDelete = function (obj) {
			$scope.obj = obj;
			if($scope.obj.objId == ""){
				layer.msg('标识丢失，删除失败');
				return;
			}
			layer.open({
				title:'确认删除',
				content: '确定要删除：'+$scope.obj.objTitle+'？',
				btn: ['确认', '取消'],
				shadeClose: false,
				yes: function(){
					$scope.newError = {};
					queryRemote.objDelete($scope.obj).success(function (res) {
						if (res.ret == "0") {
							layer.msg('成功删除'+$scope.obj.objTitle);
							if($scope.objs.length==1&&$scope.pageIndex>1){
								$scope.pageIndex = $scope.pageIndex-1;
							}
							loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());
							$scope.state = 'list';
						}else{
							layer.msg('删除失败，请重试或联系系统管理员');
						}
					});
				}, no: function(){
					//layer.open({content: '你选择了取消', time: 1});
				}
			});
		};

		$scope.$apply();
	}];
});