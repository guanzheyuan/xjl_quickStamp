package util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import utils.DateUtil;
 /**
 * @author    姓名   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-3-21 下午4:19:22
 * @describe  类说明
 */
public class GenHtmlJSFileUtil {
	public static void main(String[] args) {
		String folder = "mobile/notice";
		String fileName = "notice_add";
		String functionName = "通知列表";
		GenHtml(folder,fileName,functionName);
		GenJS(folder,fileName);
		GenCtrlJS(folder,fileName);
	}
	
	public static void GenHtml(String folder,String fileName,String functionName){
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("<div class=\"sectionBox\" ng-show=\"state=='list'\">\r\n");
		strBuffer.append("\t<div class=\"tit\">\r\n");
		strBuffer.append("\t\t<h1>"+functionName+"</h1>\r\n");
		strBuffer.append("\t</div>\r\n");
		strBuffer.append("\t<div class=\"searchDiv\" style=\"background:none;\">\r\n");
		strBuffer.append("\t\t<input type=\"text\" class=\"txt\" placeholder=\"关键字，范围：列名\" ng-model=\"searchText\" />\r\n");
		strBuffer.append("\t\t<input type=\"button\" value=\"查&nbsp;询\" class=\"btnYellow c2\" ng-click=\"search()\" />\r\n");
		strBuffer.append("\t\t<input type=\"button\" value=\"重&nbsp;置\" class=\"btnGray c1\" ng-click=\"reset()\" />\r\n");
		strBuffer.append("\t\t<input type=\"button\" ng-click=\"gotoAdd()\" value=\"新&nbsp;增\" class=\"btnAdd btnYellow c2\" />");
		strBuffer.append("\t</div>\r\n");
		strBuffer.append("\t<div class=\"thDiv\">\r\n");
		strBuffer.append("\t\t<span class=\"t14\">编号</span>\r\n");
		strBuffer.append("\t\t<span class=\"t18\">列名</span>\r\n");
		strBuffer.append("\t\t<span class=\"t25\">列名</span>\r\n");
		strBuffer.append("\t\t<span class=\"t25\">列名</span>\r\n");
		strBuffer.append("\t\t<span class=\"t14\">操作</span>\r\n");
		strBuffer.append("\t</div>\r\n");
		strBuffer.append("\t<ul class=\"th_list clearfix\">\r\n");
		strBuffer.append("\t\t<li class=\"txtCt gray\" ng-if=\"total==0\">暂无数据</li>\r\n");
		strBuffer.append("\t\t<li ng-repeat=\"obj in objs\" ng-class=\"{bg:$odd}\" ng-if=\"total>0\" >\r\n");
		strBuffer.append("\t\t<span class=\"t14\">{{(pageIndex-1)*pageSize+$index+1}}</span>\r\n");
		strBuffer.append("\t\t<span class=\"t18\">{{obj.col1}}</span>\r\n");
		strBuffer.append("\t\t<span class=\"t25\">{{obj.col2}}</span>\r\n");
		strBuffer.append("\t\t<span class=\"t25\">{{obj.col3}}</span>\r\n");
		strBuffer.append("\t\t<span class=\"t14\">\r\n");
		strBuffer.append("\t\t\t<a class=\"pdR10\" title=\"编辑\" ng-click=\"gotoEdit(obj)\">编辑</a>\r\n");
		strBuffer.append("\t\t\t<a ng-click=\"objDelete(obj)\" class=\"red pdL10\">删除</a>\r\n");
		strBuffer.append("\t\t</span>\r\n");
		strBuffer.append("\t\t</li>\r\n");
		strBuffer.append("\t</ul>\r\n");
		strBuffer.append("\t<div class=\"page\">\r\n");
		strBuffer.append("\t\t<span>共{{pages}}页&nbsp;/&nbsp;{{total}}条记录 &nbsp;&nbsp;当前第{{pageIndex}}页</span>\r\n");
		strBuffer.append("\t\t<a ng-click=\"home()\">首页</a><a ng-click=\"prev()\">上一页</a><a ng-click=\"next()\">下一页</a>\r\n");
		strBuffer.append("\t\t<a ng-click=\"last()\">尾页</a>\r\n");
		strBuffer.append("\t</div>\r\n");
		strBuffer.append("</div>\r\n");
		strBuffer.append("<div class=\"sectionBox\" ng-show=\"state=='edit'\">\r\n");
		strBuffer.append("\t<div class=\"tit\">\r\n");
		strBuffer.append("\t\t<h1>"+functionName+"-{{stateInfo}}</h1>\r\n");
		strBuffer.append("\t</div>\r\n");
		strBuffer.append("\t<div class=\"txtBox\" style=\"padding-left:100px;\">\r\n");
		strBuffer.append("\t\t<dl>\r\n");
		strBuffer.append("\t\t\t<dt>参数名1</dt>\r\n");
		strBuffer.append("\t\t\t<dd>\r\n");
		strBuffer.append("\t\t\t\t<input type=\"text\" class=\"txt2 width420\" placeholder=\"列名\" ng-model=\"obj.col\"/>\r\n");
		strBuffer.append("\t\t\t\t<span class=\"error\"><b>{{currentError.mask}}</b></span>\r\n");
		strBuffer.append("\t\t\t</dd>\r\n");
		strBuffer.append("\t\t</dl>\r\n");
		strBuffer.append("\t\t<dl>\r\n");
		strBuffer.append("\t\t\t<dt>参数名2</dt>\r\n");
		strBuffer.append("\t\t\t<dd>\r\n");
		strBuffer.append("\t\t\t\t<input type=\"text\" class=\"txt2 width420\" placeholder=\"列名2\" ng-model=\"obj.col2\"/>\r\n");
		strBuffer.append("\t\t\t\t<span class=\"error\"><b>{{currentError.mask}}</b></span>\r\n");
		strBuffer.append("\t\t\t</dd>\r\n");
		strBuffer.append("\t\t</dl>\r\n");
		strBuffer.append("\t\t<dl>\r\n");
		strBuffer.append("\t\t<dt></dt>\r\n");
		strBuffer.append("\t\t<dd>\r\n");
		strBuffer.append("\t\t\t<input type=\"button\" ng-click=\"objSave()\" value=\"确&nbsp;&nbsp;定\" class=\"btnYellow c2 pdLR\">\r\n");
		strBuffer.append("\t\t\t<input type=\"button\" value=\"取&nbsp;消\" class=\"btnGray c1\" ng-click=\"save_cancel()\">\r\n");
		strBuffer.append("\t\t</dd>\r\n");
		strBuffer.append("\t\t</dl>\r\n");
		strBuffer.append("\t</div>\r\n");
		strBuffer.append("</div>");
		try {
			String path = System.getProperty("user.dir") + "/public/modules/"+folder+"/"+fileName;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String resPath = path + "/" + fileName + ".html";
			System.out.println("resPath=" + resPath);
			FileUtils.writeStringToFile(new File(resPath), strBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void GenJS(String folder,String fileName){
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("/**\r\n");
		strBuffer.append("* 创建时间："+DateUtil.getDateTime(new Date(), "yyyy-MM-dd hh:mm:ss")+"\r\n");
		strBuffer.append("*/\r\n");
		strBuffer.append("\"use strict\";\r\n");
		strBuffer.append("angular.module('"+fileName+"', ['ngRoute','selectAddress','ngSanitize','ngKeditor','ui.select'])\r\n");
		strBuffer.append("\t.config(['$routeProvider', function ($routeProvider) {\r\n");
		strBuffer.append("\t\t$routeProvider.when('/"+fileName+"', {\r\n");
		strBuffer.append("\t\t\ttemplateUrl: baseUrl + '/"+fileName+"/"+fileName+".html',\r\n");
		strBuffer.append("\t\t\t\tcontroller: '"+GenEntityUtil.initcap(fileName.concat("_ctrl"))+"'\r\n");
		strBuffer.append("\t\t});\r\n");
		strBuffer.append("\t}])\r\n");
		strBuffer.append("\t.factory('"+GenEntityUtil.getCamelStrForCol(fileName.concat("_remote"))+"', ['$http', function ($http) {\r\n");
		strBuffer.append("\t\treturn {\r\n");
		strBuffer.append("\t\t\tqryList: function (index, size,searchText) {\r\n");
		strBuffer.append("\t\t\t\treturn $http({\r\n");
		strBuffer.append("\t\t\t\t\tmethod:'post',\r\n");
		strBuffer.append("\t\t\t\t\turl: '/wechat/VnoService/query"+GenEntityUtil.initcap(fileName.concat("_ByPage"))+"List',\r\n");
		strBuffer.append("\t\t\t\t\tparams: {\r\n");
		strBuffer.append("\t\t\t\t\t\tPAGE_INDEX: index,\r\n");
		strBuffer.append("\t\t\t\t\t\tPAGE_SIZE: size,\r\n");
		strBuffer.append("\t\t\t\t\t\tsearchKeyWord: searchText || ''\r\n");
		strBuffer.append("\t\t\t\t\t}\r\n");
		strBuffer.append("\t\t\t\t});\r\n");
		strBuffer.append("\t\t\t},\r\n");
		
		strBuffer.append("\t\t\tobjSave: function (obj) {\r\n");
		strBuffer.append("\t\t\t\treturn $http({\r\n");
		strBuffer.append("\t\t\t\t\tmethod:'post',\r\n");
		strBuffer.append("\t\t\t\t\turl: '/wechat/VnoService/save"+GenEntityUtil.initcap(fileName)+"',\r\n");
		strBuffer.append("\t\t\t\t\tparams: obj\r\n");
		strBuffer.append("\t\t\t\t});\r\n");
		strBuffer.append("\t\t\t},\r\n");
		
		strBuffer.append("\t\t\tobjDelete: function (obj) {\r\n");
		strBuffer.append("\t\t\t\treturn $http({\r\n");
		strBuffer.append("\t\t\t\t\tmethod:'post',\r\n");
		strBuffer.append("\t\t\t\t\turl: '/wechat/VnoService/delete"+GenEntityUtil.initcap(fileName)+"',\r\n");
		strBuffer.append("\t\t\t\t\tparams: obj\r\n");
		strBuffer.append("\t\t\t\t});\r\n");
		strBuffer.append("\t\t\t}\r\n");
		
		strBuffer.append("\t\t}\r\n");
		strBuffer.append("\t}])\r\n");
		strBuffer.append("\t.controller('"+GenEntityUtil.initcap(fileName.concat("_ctrl")+"',['$scope', '$injector','$window', function ($scope, $injector,$window) {\r\n"));
		strBuffer.append("\t\trequire(['"+fileName+"/"+fileName+".ctrl'], function (ctrl) {\r\n");
		strBuffer.append("\t\t\t$injector.invoke(ctrl, this, {'$scope': $scope,'$window':$window});\r\n");
		strBuffer.append("\t\t});\r\n");
		strBuffer.append("\t}]);\r\n");
		try {
			String path = System.getProperty("user.dir") + "/public/modules/"+folder+"/"+fileName;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String resPath = path + "/" + fileName + ".js";
			System.out.println("resPath=" + resPath);
			FileUtils.writeStringToFile(new File(resPath), strBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void GenCtrlJS(String folder,String fileName){
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("/**\r\n");
		strBuffer.append("* 创建时间："+DateUtil.getDateTime(new Date(), "yyyy-MM-dd hh:mm:ss")+"\r\n");
		strBuffer.append("*/\r\n");
		strBuffer.append("\"use strict\";\r\n");
		strBuffer.append("define([], function () {\r\n");
		strBuffer.append("\treturn ['$scope', '"+GenEntityUtil.getCamelStrForCol(fileName.concat("_remote"))+"', 'Upload','$window', function ($scope, queryRemote,Upload,$window) {\r\n");
		strBuffer.append("\t\t//分页索引\r\n");
		strBuffer.append("\t\t$scope.pageIndex = 1;\r\n");
		strBuffer.append("\t\t//页面的大小\r\n");
		strBuffer.append("\t\t$scope.pageSize = 10;\r\n");
		strBuffer.append("\t\t//总页数\r\n");
		strBuffer.append("\t\t$scope.pages = 1;\r\n");
		strBuffer.append("\t\t\r\n");
		strBuffer.append("\t\t//显示页面：列表list与编辑edit\r\n");
		strBuffer.append("\t\t$scope.state = 'list';\r\n");
		strBuffer.append("\t\t\r\n");
		strBuffer.append("\t\t//搜索关键字\r\n");
		strBuffer.append("\t\t$scope.searchText = '';\r\n");
		strBuffer.append("\t\t//当前编辑的系统配置项\r\n");
		strBuffer.append("\t\t$scope.sysparam = {};\r\n");
		strBuffer.append("\t\t//总条数\r\n");
		strBuffer.append("\t\t$scope.total = 0;\r\n");
		strBuffer.append("\t\t//当前数据列表\r\n");
		strBuffer.append("\t\t$scope.objs = [];\r\n");
		strBuffer.append("\t\t\r\n");
		strBuffer.append("\t\t//查询列表函数\r\n");
		strBuffer.append("\t\tvar loadList = function (index, size, searchText) {\r\n");
		strBuffer.append("\t\t\tqueryRemote.qryList(index, size, searchText).success(function (res) {\r\n");
		strBuffer.append("\t\t\t\tif(res.ret==\"0\"){\r\n");
		strBuffer.append("\t\t\t\t\t$scope.objs = res.data.data || [];\r\n");
		strBuffer.append("\t\t\t\t\t$scope.total = res.data.total || 0;\r\n");
		strBuffer.append("\t\t\t\t\tif($scope.total!=0){\r\n");
		strBuffer.append("\t\t\t\t\t\t if($scope.total % $scope.pageSize>0){\r\n");
		strBuffer.append("\t\t\t\t\t\t\t $scope.pages = parseInt($scope.total / $scope.pageSize) + 1;\r\n");
		strBuffer.append("\t\t\t\t\t\t }else{\r\n");
		strBuffer.append("\t\t\t\t\t\t\t $scope.pages = parseInt($scope.total / $scope.pageSize);\r\n");
		strBuffer.append("\t\t\t\t\t\t }\r\n");
		strBuffer.append("\t\t\t\t\t }else{\r\n");
		strBuffer.append("\t\t\t\t\t\t $scope.pages = 1;\r\n");
		strBuffer.append("\t\t\t\t\t\t $scope.pageIndex = 1;\r\n");
		strBuffer.append("\t\t\t\t\t }\r\n");
		strBuffer.append("\t\t\t\t}else{\r\n");
		strBuffer.append("\t\t\t\t\tlayer.msg('请求失败，请重试');\r\n");
		strBuffer.append("\t\t\t\t}\r\n");
		strBuffer.append("\t\t\t});\r\n");
		strBuffer.append("\t\t};\r\n");
		strBuffer.append("\t\t//loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());\r\n");

		strBuffer.append("\r\n");
		strBuffer.append("\t\t//首页\r\n");
		strBuffer.append("\t\t$scope.home = function () {\r\n");
		strBuffer.append("\t\t\tif ($scope.pageIndex == 1) {\r\n");
		strBuffer.append("\t\t\t\tlayer.msg('已经是首页了');\r\n");
		strBuffer.append("\t\t\t}\r\n");
		strBuffer.append("\t\t\telse {\r\n");
		strBuffer.append("\t\t\t\t$scope.pageIndex = 1;\r\n");
		strBuffer.append("\t\t\t\tloadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());\r\n");
		strBuffer.append("\t\t\t}\r\n");
		strBuffer.append("\t\t};\r\n");
		
		strBuffer.append("\r\n");
		strBuffer.append("\t\t//前一页\r\n");
		strBuffer.append("\t\t$scope.prev = function () {\r\n");
		strBuffer.append("\t\t\tif ($scope.pageIndex == 1) {\r\n");
		strBuffer.append("\t\t\t\tlayer.msg('没有上一页了');\r\n");
		strBuffer.append("\t\t\t}\r\n");
		strBuffer.append("\t\t\telse {\r\n");
		strBuffer.append("\t\t\t\t$scope.pageIndex--;\r\n");
		strBuffer.append("\t\t\t\tloadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());\r\n");
		strBuffer.append("\t\t\t}\r\n");
		strBuffer.append("\t\t};\r\n");
		
		strBuffer.append("\r\n");
		strBuffer.append("\t\t//下一页\r\n");
		strBuffer.append("\t\t$scope.next = function () {\r\n");
		strBuffer.append("\t\t\tif ($scope.pageIndex == $scope.pages) {\r\n");
		strBuffer.append("\t\t\t\tlayer.msg('没有下一页了');\r\n");
		strBuffer.append("\t\t\t}\r\n");
		strBuffer.append("\t\t\telse {\r\n");
		strBuffer.append("\t\t\t\t$scope.pageIndex++;\r\n");
		strBuffer.append("\t\t\t\tloadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());\r\n");
		strBuffer.append("\t\t\t}\r\n");
		strBuffer.append("\t\t};\r\n");
		
		strBuffer.append("\r\n");
		strBuffer.append("\t\t//最后一页\r\n");
		strBuffer.append("\t\t$scope.last = function () {\r\n");
		strBuffer.append("\t\t\tif ($scope.pageIndex == $scope.pages) {\r\n");
		strBuffer.append("\t\t\t\tlayer.msg('已经是尾页了');\r\n");
		strBuffer.append("\t\t\t}\r\n");
		strBuffer.append("\t\t\telse {\r\n");
		strBuffer.append("\t\t\t\t$scope.pageIndex = $scope.pages;\r\n");
		strBuffer.append("\t\t\t\tloadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());\r\n");
		strBuffer.append("\t\t\t}\r\n");
		strBuffer.append("\t\t};\r\n");
		
		strBuffer.append("\t\t//搜索按钮\r\n");
		strBuffer.append("\t\t$scope.search = function(){\r\n");
		strBuffer.append("\t\t\t$scope.pageIndex = 1;\r\n");
		strBuffer.append("\t\t\tloadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());\r\n");
		strBuffer.append("\t\t}\r\n");
		
		strBuffer.append("\t\t//重置按钮\r\n");
		strBuffer.append("\t\t$scope.reset = function(){\r\n");
		strBuffer.append("\t\t\t$scope.searchText='';\r\n");
		strBuffer.append("\t\t\tloadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());\r\n");
		strBuffer.append("\t\t}\r\n");
		
		strBuffer.append("\t\t//添加\r\n");
		strBuffer.append("\t\t$scope.gotoAdd = function () {\r\n");
		strBuffer.append("\t\t\t$scope.stateInfo = \"添加\";\r\n");
		strBuffer.append("\t\t\t$scope.currentError={};\r\n");
		strBuffer.append("\t\t\t$scope.state = 'edit';\r\n");
		strBuffer.append("\t\t};\r\n");
		strBuffer.append("\r\n");
		
		strBuffer.append("\t\t//编辑\r\n");
		strBuffer.append("\t\t$scope.gotoEdit = function (obj) {\r\n");
		strBuffer.append("\t\t\t$scope.stateInfo = \"编辑\";\r\n");
		strBuffer.append("\t\t\t$scope.currentError={};\r\n");
		strBuffer.append("\t\t\t$scope.obj = obj;\r\n");
		strBuffer.append("\t\t\t$scope.state = 'edit';\r\n");
		strBuffer.append("\t\t};\r\n");
		strBuffer.append("\r\n");
		
		strBuffer.append("\t\t//添加、编辑->保存\r\n");
		strBuffer.append("\t\t$scope.objSave = function () {\r\n");
		strBuffer.append("\t\t\t//容错信息提示对象\r\n");
		strBuffer.append("\t\t\t$scope.currentError={};\r\n");
		strBuffer.append("\t\t\tif ($scope.obj.objTitle == \"\") {\r\n");
		strBuffer.append("\t\t\t\t$scope.currentError.objTitle='请输入标题';\r\n");
		strBuffer.append("\t\t\t\treturn;\r\n");
		strBuffer.append("\t\t\t}\r\n");
		strBuffer.append("\t\t\tqueryRemote.objSave($scope.obj).success(function (res) {\r\n");
		strBuffer.append("\t\t\t\tif (res.ret == \"0\") {\r\n");
		strBuffer.append("\t\t\t\t\tloadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());\r\n");
		strBuffer.append("\t\t\t\t\t$scope.state = 'list';\r\n");
		strBuffer.append("\t\t\t\t}else{\r\n");
		strBuffer.append("\t\t\t\t\tlayer.msg('保存失败，message：'+res.desc);\r\n");
		strBuffer.append("\t\t\t\t}\r\n");
		strBuffer.append("\t\t\t});\r\n");
		strBuffer.append("\t\t};\r\n");
		strBuffer.append("\r\n");

		strBuffer.append("\t\t//取消\r\n");
		strBuffer.append("\t\t$scope.save_cancel = function () {\r\n");
		strBuffer.append("\t\t\t$scope.state = 'list';\r\n");
		strBuffer.append("\t\t\t//loadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());\r\n");
		strBuffer.append("\t\t};\r\n");
		strBuffer.append("\r\n");
		
		strBuffer.append("\t\t//删除\r\n");
		strBuffer.append("\t\t$scope.objDelete = function (obj) {\r\n");
		strBuffer.append("\t\t\t$scope.obj = obj;\r\n");
		strBuffer.append("\t\t\tif($scope.obj.objId == \"\"){\r\n");
		strBuffer.append("\t\t\t\tlayer.msg('标识丢失，删除失败');\r\n");
		strBuffer.append("\t\t\t\treturn;\r\n");
		strBuffer.append("\t\t\t}\r\n");
		strBuffer.append("\t\t\tlayer.open({\r\n");
		strBuffer.append("\t\t\t\ttitle:'确认删除',\r\n");
		strBuffer.append("\t\t\t\tcontent: '确定要删除：'+$scope.obj.objTitle+'？',\r\n");
		strBuffer.append("\t\t\t\tbtn: ['确认', '取消'],\r\n");
		strBuffer.append("\t\t\t\tshadeClose: false,\r\n");
		strBuffer.append("\t\t\t\tyes: function(){\r\n");
		strBuffer.append("\t\t\t\t\t$scope.newError = {};\r\n");
		strBuffer.append("\t\t\t\t\tqueryRemote.objDelete($scope.obj).success(function (res) {\r\n");
		strBuffer.append("\t\t\t\t\t\tif (res.ret == \"0\") {\r\n");
		strBuffer.append("\t\t\t\t\t\t\tlayer.msg('成功删除'+$scope.obj.objTitle);\r\n");
		strBuffer.append("\t\t\t\t\t\t\tif($scope.objs.length==1&&$scope.pageIndex>1){\r\n");
		strBuffer.append("\t\t\t\t\t\t\t\t$scope.pageIndex = $scope.pageIndex-1;\r\n");
		strBuffer.append("\t\t\t\t\t\t\t}\r\n");
		strBuffer.append("\t\t\t\t\t\t\tloadList($scope.pageIndex, $scope.pageSize,$scope.searchText.trim());\r\n");
		strBuffer.append("\t\t\t\t\t\t\t$scope.state = 'list';\r\n");
		strBuffer.append("\t\t\t\t\t\t}else{\r\n");
		strBuffer.append("\t\t\t\t\t\t\tlayer.msg('删除失败，请重试或联系系统管理员');\r\n");
		strBuffer.append("\t\t\t\t\t\t}\r\n");
		strBuffer.append("\t\t\t\t\t});\r\n");
		strBuffer.append("\t\t\t\t}, no: function(){\r\n");
		strBuffer.append("\t\t\t\t\t//layer.open({content: '你选择了取消', time: 1});\r\n");
		strBuffer.append("\t\t\t\t}\r\n");
		strBuffer.append("\t\t\t});\r\n");
		strBuffer.append("\t\t};\r\n");
		strBuffer.append("\r\n");
		
		strBuffer.append("\t\t$scope.$apply();\r\n");
		strBuffer.append("\t}];\r\n");
		strBuffer.append("});");
		try {
			String path = System.getProperty("user.dir") + "/public/modules/"+folder+"/"+fileName;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String resPath = path + "/" + fileName + ".ctrl.js";
			System.out.println("resPath=" + resPath);
			FileUtils.writeStringToFile(new File(resPath), strBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
 