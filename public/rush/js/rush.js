function queryToileList(){
	var dataParams = {toiletName:'',pageNumber:1,pageSize:10};
	$.ajax({type: 'GET', url: "http://47.98.200.5:8080/watersaving/api/toilet/list",
        async: false,
        dataType: "jsonp",
        jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
        jsonpCallback:"getParam",
        data:dataParams,  
        success: function (response, status, xhr) {
        	console.log('状态为：' + status + ',状态是：' + xhr.statusText);
            console.log(response);
         },
        error: function(jqXHR, textStatus, errorThrown){
        	 console.log('status:'+jqXHR.status);
        	 console.log('readyState:'+jqXHR.readyState);
        	 console.log('textStatus:'+textStatus);
        	 console.log('error_errorThrown:'+errorThrown);
             console.log('error_textStatus:'+textStatus);
        }
}); 
}
function getParam(data){
	console.log('data:'+data.rows);
}
$(function(){
});