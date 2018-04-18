/**
 * Created by nalantianyi on 15/10/16.
 */
'use strict';
//require.config({
//    baseUrl: baseUrl,
//    waitSeconds: 0,
//    paths: {
//        angularRoute: '/public/angularjs/angular-route.min',
//        ngSanitize: '/public/angularjs/angular-sanitize.min',
//        'layer':'/public/layer/layer.min',
//        'ng-file-upload': '/public/ng-upload/ng-file-upload-all.min',
//        'kindeditor': '/public/kindeditor/kindeditor-all-min',
//        'ng-kindeditor': '/public/kindeditor/angular-kindeditor',
//        'kindeditor-lang':'/public/kindeditor/lang/zh_CN',
//        'select': '/public/select/select.min',
//        'selectCity': '/public/select/selectAddress2'
//
//    },
//    shim: {
//        'layer': {'exports': 'layer'},
//        'ng-kindeditor': {deps: ['kindeditor','kindeditor-lang']},
//        'kindeditor-lang':{deps:['kindeditor']}
//    }
//});


//require([
//    'ng-file-upload'
//], function () {

$.ajax({
    url: '/dw/mobile/ActivityService/qryMeunByUserId',
    data: {
    	openId: openId
    }
}).success(function (res) {
        var role = res.data;
        var aa = ['home/home'];
        var bb = ['ngRoute', 'home', 'ngFileUpload'];
        role.menuArrayList.forEach(function (item) {
            if (item.listZzbMenu.length == 0) {
                aa.push(item.menuUrl + '/' + item.menuUrl);
                bb.push(item.menuUrl);
            }
            else {
                item.listZzbMenu.forEach(function (_item) {
                    if (_item.listZzbMenu.length == 0) {
                        aa.push(_item.menuUrl + '/' + _item.menuUrl);
                        bb.push(_item.menuUrl);
                    }
                    else {
                        _item.listZzbMenu.forEach(function (__item) {
                            aa.push(__item.menuUrl + '/' + __item.menuUrl);
                            bb.push(__item.menuUrl);
                        });
                    }
                });
            }
        });
        require(aa, function () {

            angular.module('myApp', bb)

                .config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
                    $routeProvider.otherwise({redirectTo: '/home'});
                    $httpProvider.interceptors.push('loading');
                }])
                .
                run(['$rootScope', function ($rootScope) {

                    $rootScope.$on('$locationChangeSuccess', function (ex, newUrl, oldUrl, newState, oldState) {
                        $rootScope.isShowSubMenu = true;
                        var urls = newUrl.split('#');
                        var bUrl = urls[urls.length - 1].replace('/', '');

                        var findMenuId = function (bUrl) {
                            var menuId = '';
                            $.each(role.menuArrayList, function (key, value) {
                                if (value.listZzbMenu.length > 0) {
                                    $.each(value.listZzbMenu, function (_key, _value) {
                                        if (_value.menuUrl == bUrl) {
                                            menuId = value.menuId;
                                        }
                                        $.each(_value.listZzbMenu, function (__key, __value) {
                                            if (__value.menuUrl == bUrl) {
                                                menuId = value.menuId;
                                            }
                                        });
                                    });

                                }

                            });
                            return menuId;

                        };
                        var menuId = findMenuId(bUrl);

                    	$('#nav_list').find('li.home').click(function(){
                    		$('#nav_list').find('li.home').removeClass('active');
                    		$(this).addClass('active');
                    	});
                       
                    	$('.subnav').find('a').click(function(){
                    		$('.subnav').removeClass('active');
                    		$(this).parent().addClass('active');
                    	});
                        
                        if (menuId !== '') {
                            var $lis = $('.tabs').find('li');
                            $lis.removeClass('on');
                            $lis.each(function () {
                                if ($(this).attr('menu-id') == menuId) {
                                    $(this).addClass('on');
                                }
                            });
                            if (bUrl != 'home') {
                                $.each(role.menuArrayList, function (key, value) {
                                    if (menuId == value.menuId && value.listZzbMenu.length > 0) {
                                        $rootScope.subMenus = value.listZzbMenu;

                                    }

                                });
                            }
                        }
                        else{
                            var $lis = $('.tabs').find('li');
                            $lis.removeClass('on');
                            $('ul.tabs').find('a').each(function(){
                                var href=$(this).attr('href');
                                if(href&&href.indexOf(bUrl)!=-1)
                                {
                                    $(this).parent().addClass('on');
                                }

                            });
                            $rootScope.isShowSubMenu = false;
                        }

                        setTimeout(
                            function(){
                                var $a = $('.con').find('a');
                                $a.removeClass('cur').each(function () {

                                    var aUrl = $(this).attr('href').split('/');
                                    var targerUrl = aUrl[aUrl.length - 1].replace('#', '');
                                    if (targerUrl == bUrl) {
                                        $(this).addClass('cur');
                                    }
                                });

                            }
                        );




                    });
                    $rootScope.$on('$routeChangeStart', function () {
                        $rootScope.layerIndex = layer.load(0, {shade: [0.9, '#fff']});
                    });
                    $rootScope.$on('routeChangeSuccess', function () {
                        layer.closeAll();
                    });
                    $(window).resize(function(){
                        $('.right').height($(window).height()-121);
                     	$(".nav-list").height($(window).height()-106);
                        $('.tabs').height($(window).height()-161);
                    }).trigger('resize');
                }])
                .factory('loading', ['$rootScope', function ($rootScope) {
                    return {
                        request: function (config) {
                            $rootScope.layerIndex = layer.load(0, {shade: false});

                            return config;
                        },
                        response: function (res) {
                            layer.close($rootScope.layerIndex);
                            return res;
                        }
                    };
                }])
                .controller('RootController', ['$scope', '$window', '$rootScope', '$location', function ($scope, $window, $rootScope, $location) {


                    $scope.logout = function () {
                        layer.confirm('确认注销吗？', {icon: 3}, function (index) {
                            $window.location = '/LoginService/logout';
                        });
                    };
                    $scope.toggleAccountSetting = function () {
                        $scope.showAccountSetting = !$scope.showAccountSetting;
                    };

                    //choose menu
                    $rootScope.menus = role.menuArrayList;
                    $rootScope.subMenus = [];
                    $rootScope.displaySubMenus = function (menu) {
                        if (menu.listZzbMenu.length == 0) {
                        }
                        else {
                            $rootScope.subMenus = menu.listZzbMenu;
                            var url=menu.listZzbMenu[0].menuUrl;
                            $rootScope.isShowSubMenu = true;
                            $location.url(url);

                        }
                    };


                }]);
            angular.element().ready(function () {
                // bootstrap the app manually
                angular.bootstrap(document, ['myApp']);

            });
        });

    }
);
//});

