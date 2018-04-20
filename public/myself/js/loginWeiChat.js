function _showError(message, textStatus, errorThrown) {
//  $("#alerts").prepend(tmpl("template-alert", {
//    level: "danger",
//    title: (errorThrown != "" ? errorThrown : textStatus) + ": ",
//    description: message
//  }));
  var errorHtml = '<div class="alert alert-danger alert-dismissable"> <button type="button" class="close" data-dismiss="alert">&times;</button> <strong>"'+ (errorThrown != "" ? errorThrown : textStatus) + ": "+'"</strong><div id="description">"'+message+'"';
   $("#alerts").html(errorHtml);
}
//判断是否是pc
function IsPC(){  
     var userAgentInfo = navigator.userAgent;
     var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");  
     var flag = true;  
     for (var v = 0; v < Agents.length; v++) {  
         if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = false; break; }  
     }  
     return flag;  
  }
//判断是否是微信
function is_weixin() { 
    var ua = window.navigator.userAgent.toLowerCase(); 
    if (ua.match(/MicroMessenger/i) == 'micromessenger') { 
        return true;
    } else { 
       return false;
    } 
 }
//得到参数
function getQueryString(name) {  
	 var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");  
	 var r = window.location.search.substr(1).match(reg);  
	 if (r != null) return unescape(r[2]); return null;  
} 
//微信登录
function pc_WechatLogin(appId){
	window.location.href='https://open.weixin.qq.com/connect/qrconnect?appid='+appId+'&redirect_uri=http://dw201709.com/sp/mobile/Skip/toLoginFilter?flag=PC&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect';
}
function refresh_data(){
	accessory_Date();
};
//列表数据
function accessory_Date(){
	$.ajax({
		url:'/sp/mobile/Execute/uploadListPage?flag='+flag,
		type:'post',
		success:function(data){
			if(data.data){
				var ret = data.data.data;
				var html;
				for(var i = 0;i<ret.length;i++){
					html+= ' <tr class="row-file" id="'+ret[i].fileId+'">'+
					'<td class="column-icon"><span class="glyphicon glyphicon-cloud"></span></td>'+
			        '<td class="column-name"><p class="edit">'+ret[i].realName+'</p></td>'+
			        '<td class="column-size"><p>'+ret[i].fileSize+'</p></td>'+
			        '<td class="column-icon"><button  type="button" class="btn btn-default btn-xs button-print"onclick="toConfig('+ret[i].fileId+')" > <span class="glyphicon glyphicon-print"></span> </button> </td>'+
			        '<td class="column-icon"><button type="button" class="btn btn-info btn-xs button-eye" data-id="'+ret[i].fileId+'" data-toggle="modal" data-target="#previewModal"> <span class="glyphicon glyphicon-eye-open"></span> </button> </td>'+
			        '<td class="column-delete"> <button type="button" class="btn btn-danger btn-xs button-delete" onclick="del('+ret[i].fileId+')"> <span class="glyphicon glyphicon-trash"></span> </button> </td>'+
			        '</tr>';
				}
				$("#listing").html(html);
			}
		}
	});
}
function toConfig(id){
	window.location.href="/sp/mobile/Skip/toStampConfig?paramId="+id+"&flag="+flag;
}
function del(id){
	 $.ajax({
		 url:'/sp/mobile/Execute/delFile',
	 	 type:'post',
	 	 data:{fileId:id},
	 	 success:function(){
	 		 $("#"+id).remove();
	 	 }
	 });
}
function getSessionUser(){
	var flag = false;
	$.ajax({
		url:'/sp/mobile/Execute/getSessionForUser',
		type:'POST',
		async:false,
		success:function(data){
			if(data.desc == "success"){
				var res = data.data;
				$("#logBtn").css("display","none");
				$(".radiu").css("display","block");
				$(".radiu").css("background-image","url('"+res.headImgUrl+"')");
				flag = true;
			}
		}
	});
	return flag;
}
function isUploadButnShow(){
	return btnFlag;
}
var isPC = true;
var flag ="PC";
var btnFlag = false;
$(function(){
	btnFlag = getSessionUser();
	if(!btnFlag){
		$("#upload-file").attr('disabled', true);
		$("#userInfo").attr('disabled',true);
	}
	isPC = IsPC();
	if(isPC){
		flag = "PC";
	}else{
		flag = "weChat";
	}
	$("#userInfo").click(function(){
		window.location.href='/sp/mobile/Skip/toOrder?flag='+flag;
	})
	$('#previewModal').on('shown.bs.modal', function (e) {
		 var btn = $(e.relatedTarget),
	        id = btn.data("id"); 
			 if(""!=id){
				 $.ajax({
						url:'/sp/mobile/Execute/queryFileById?flag='+flag,
						type:'post',
						data:{fileId:id},
						success:function(data){
							var w;
							var h;
							if(data.data){
								if(isPC){
									 w = window.innerWidth;
									 h = window.innerHeight;
									 var loadUrl = data.data.fileUrl.substring(0,data.data.fileUrl.toString().lastIndexOf('/'));
									$("#previewDialog").css("width",w);
									$("#prevewContent").css("height",(h)+100);
									$("#prevewBody").css("height",h);
									$("#modalTitle").html(data.data.realName);
									if(data.data.fileType == ".pdf"){
										$("#prevewBody").html('<iframe id="modalIframe" src="../../../sp/'+loadUrl+'/'+data.data.fileName+'" style="width:100%;height:100%" frameborder="no" border="0" />');
									}else if (data.data.fileType ==".doc" || data.data.fileType ==".docx"){
										$("#prevewBody").html('<iframe id="modalIframe" src="../../../sp/'+loadUrl+'/html/'+data.data.htmlName+'" style="width:100%;height:100%" frameborder="no" border="0" />');
									}else if(data.data.fileType == ".xls" || data.data.fileType == ".xlsx"){
										$("#prevewBody").html('<iframe id="modalIframe" src="../../../sp/'+loadUrl+'/html/'+data.data.htmlName+'" style="width:100%;height:100%" frameborder="no" border="0" />');
									}
									
								}else{
									$("#modalTitle").html(data.data.realName);
									$("#modalIframe").attr("src","sp/public/tmp/upload/2018/1111111111/pdf.pdf");
								}	
							}
						}
					})
			 }
	})
})
