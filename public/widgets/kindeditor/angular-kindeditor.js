/**
 * @license AngularJS v1.2.9
 * (c) 2010-2014 Google, Inc. http://angularjs.org
 * License: MIT
 * Created by Zed on 19-11-2014.
 */
(function (window, angular) {
    'use strict';

    angular
        .module('ngKeditor', [])
        .directive('keditor', function () {

            var linkFn = function (scope, elm, attr, ctrl) {

                if (typeof KindEditor === 'undefined') {
                    console.error('Please import the local resources of kindeditor!');
                    return;
                }

                var editor;
                var _config = {
                    width: '100%',
                    autoHeightMode: false,
                    uploadJson: '/Application/upload4Kindeditor',
                    filePostName: 'fileToUpload',
                    afterCreate: function () {
                        this.loadPlugin('autoheight');
                    }
                };

                var editorId = elm[0],
                    editorConfig = scope.config || _config;

                editorConfig.afterChange = function () {
                    console.log('change');
                    if (!scope.$$phase) {
                        ctrl.$setViewValue(this.html());
                        //exception happens here when angular is 1.2.28
                    }
                };

                if (KindEditor) {
                    editor = KindEditor.create(editorId, editorConfig);
                }

                ctrl.$parsers.push(function (viewValue) {
                    ctrl.$setValidity('keditor', viewValue);
                    return viewValue;
                });
                ctrl.$render = function () {
                    var _initContent = ctrl.$isEmpty(ctrl.$viewValue) ? '' : ctrl.$viewValue;
                    if (editor)
                        editor.html(_initContent);
                };
            };

            return {
                require: 'ngModel',
                scope: {config: '=config'},
                link: linkFn
            };
        });
})(window, window.angular);