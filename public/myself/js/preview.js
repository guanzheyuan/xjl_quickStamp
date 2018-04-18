var h;
var isPC = true;
$(document).ready(function(){  
	isPC = IsPC();
	if(isPC){
		h = document.documentElement.clientHeight || document.body.clientHeight;
	}else{
		h = $(".container").height()
	}
	console.log(h);
	previewFile();
});
function IsPC(){  
    var userAgentInfo = navigator.userAgent;
    var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");  
    var flag = true;  
    for (var v = 0; v < Agents.length; v++) {  
        if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = false; break; }  
    }  
    return flag;  
 }
//地址栏得到参数
function getUrlParms(name){
   var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
   var r = window.location.search.substr(1).match(reg);
   if(r!=null)
   return unescape(r[2]);
   return null;
}
function previewFile(){
	$.ajax({
		url:'/sp/mobile/Execute/queryFileById',
		type:'post',
		data:{fileId:getUrlParms("paramId")},
		success:function(data){
			if(data.data){
				$("#panelTitle").html(data.data.realName);
				if(isPC){
					var html='<embed src="../../public/tmp/upload/2018/1111111111/html/'+data.data.htmlName+'" style="width:9%;height:'+h+'" />';
					$("#panel-body").html(html);
				}else{
					//var pymParent = new pym.Parent('panel-body', "../../public/tmp/upload/2018/1111111111/html/"+data.data.htmlName, {});
					//$("#panel-body").load("../../public/tmp/upload/2018/1111111111/html/"+data.data.htmlName);
					var html='<iframe src="../../public/tmp/upload/2018/1111111111/html/'+data.data.htmlName+'" style="width:95%;height:100%;" frameborder="no" border="0" />';
					$("#panel-body").html(html);
				}
			}
		}
	})
}