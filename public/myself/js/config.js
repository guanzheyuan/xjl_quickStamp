var unitPrice = 0.01;
var flag ="PC";
var isPC = true;
$(function(){
	isPC = IsPC();
	if(isPC){
		flag = "PC";
	}else{
		flag = "weChat";
	}
	getSessionUser();
	//默认一份
	var copies = $("#copies");
	var copies_val = $("#copies_val");
	var price = $("#price");
	var isWeChat = is_weixin();
	if(isWeChat){
		copies.css("width","20");
	}
	$("#minus").click(function(){
		var val = copies_val.val();
		if(parseInt(val) > 1){
			if(parseInt(val)<=10 && !isWeChat){
				copies.css("width","10");
			}
			if(parseInt(val)<=10 && isWeChat){
				copies.css("width","20");
			}
			if(parseInt(val)>10 && parseInt(val)<=100 && !isWeChat){
				copies.css("width","20");
			}
			if(parseInt(val)>10 && parseInt(val)<=100 && isWeChat){
				copies.css("width","25");
			}
			copies_val.attr("value",parseInt(val)-1);
			copies.attr("placeholder",parseInt(val)-1);
			price.html((unitPrice*(parseInt(val)-1)).toFixed(2));
		} 
	});
	$("#plus").click(function(){
		var val = copies_val.val();
		if(parseInt(val)>=9 && !isWeChat){
			copies.css("width","20");
		}
		if(parseInt(val)>=9 && isWeChat){
			copies.css("width","25");
		}
		if(parseInt(val)>=99  && !isWeChat){
			copies.css("width","30");
		}
		if(parseInt(val)>=99  && isWeChat){
			copies.css("width","35");
		}
		copies_val.attr("value",parseInt(val)+1);
		copies.attr("placeholder",parseInt(val)+1);
		price.html((unitPrice*(parseInt(val)+1)).toFixed(2));
	});
	$("#save").click(function(){
		saveStampConfig();
	})
	getFile();
});
//判断是否是微信
function is_weixin() { 
    var ua = window.navigator.userAgent.toLowerCase(); 
    if (ua.match(/MicroMessenger/i) == 'micromessenger') { 
        return true;
    } else { 
       return false;
    } 
 }
//地址栏得到参数
function getUrlParms(name){
   var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
   var r = window.location.search.substr(1).match(reg);
   if(r!=null)
   return unescape(r[2]);
   return null;
}
//展示
function getFile(){
	$.ajax({
		url:'/sp/mobile/Execute/queryFileById?flag='+flag,
		type:'post',
		data:{fileId:getUrlParms("paramId")},
		success:function(data){
			 $("#fileName").html(data.data.realName);
			 $("#price").html(unitPrice);
		}
	})
}
//存储
function saveStampConfig(){
	var l = Ladda.create(document.getElementById("save"));
	l.start();
	l.setProgress( 0.5 );
	var date = {fileId:getUrlParms("paramId"),copies:$("#copies_val").val(),scope:'1',paperType:'1',colour:'1',face:'1',totalPrice:$("#price").html()};
	$.ajax({
		url:'/sp/mobile/Execute/saveStampSet?flag='+flag,
		type:'post',
		data:date,
		success:function(data){
			if(data.data){
				l.stop();
				window.location.href="/sp/mobile/Skip/toStampPayMent?paramId="+data.data.setId+"&?flag="+flag;
			}
		}
	});
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
function getSessionUser(){
	var flag = false;
	$.ajax({
		url:'/sp/mobile/Execute/getSessionForUser',
		type:'POST',
		async:false,
		success:function(data){
			if(data.desc == "success"){
				var res = data.data;
				$("#login").css("display","block");
				$(".radiu").css("display","block");
				$(".radiu").css("background-image","url('"+res.headImgUrl+"')");
				flag = true;
			}
		}
	});
	return flag;
}