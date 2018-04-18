var openId = '';
var vnoId = '1';
var url = window.location.href.split('#')[0];
var ajaxReturn = $.ajax({url:"/sp/Weixin/getWxSdkInfo", data: {url: url,openId:openId,vnoId:vnoId}});
$(function(){
	queryStampSet();
	getSessionUser();
	$("#save").click(function(){
		doPay();
	});
	ajaxReturn.done(function (data) {
		var appId = data.appId,
	    signature = data.signature,
	    timestamp = data.timestamp,
	    nonce = data.nonce;
		 wx.config({
	         debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	         appId: appId, // 必填，公众号的唯一标识
	         timestamp: timestamp, // 必填，生成签名的时间戳
	         nonceStr: nonce, // 必填，生成签名的随机串
	         signature: signature,// 必填，签名，见附录1
	         jsApiList: ['chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	     });
	});
});

function doPay(){
	if(!checkPayType()){return;}
	var choicePay = $("input[type='radio']:checked").val();
	if('2' == choicePay){
		//微信支付
		if(is_weixin()){
			//微信浏览器
			var wxPay_ajaxReturn = $.ajax({url:"/sp/Weixin/getWXPay", data: {openId:openId,paramId:getUrlParms("paramId")}});
			wxPay_ajaxReturn.done(function (data) {
				hideLoading();
				wx.chooseWXPay({
					  timestamp:data.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
					  nonceStr: data.nonceStr, // 支付签名随机串，不长于 32 位
					  package: data.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
					  signType: 'MD5', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
					  paySign: data.paySign, // 支付签名
					  success:function(res){
						  window.location.href="http://dw201709.com/sp";
					  },
					  cencel:function(res){
						  alert('取消');
					  },
					  fail:function(res){
						 alert(JSON.stringify(res));
					  }
					});
			});
		}else{
			//PC支付
			var wxPay_ajaxReturn = $.ajax({url:"/sp/Weixin/wxQrCode", data: {openId:openId,paramId:getUrlParms("paramId")}});
			wxPay_ajaxReturn.done(function (data) {
				 var setTime = setInterval(function(){
					 var wxQueryOrderReturn  = $.ajax({url:"/sp/Weixin/queryOrder", data: {openId:openId,paramId:getUrlParms("paramId")}});
					 wxQueryOrderReturn.done(function(data){
						 if(data.data.trade_state == "SUCCESS"){
							 console.log('clear');
							 clearInterval(setTime);
							 $("#modalTitle").html('支付成功');
							 $("#payBody").html('支付成功即将跳转！');
							 var returnPay = $.ajax({url:"/sp/mobile/Execute/modifyPayStatus", data: {setId:getUrlParms("paramId")}});
							 returnPay.done(function(data){
								 console.log(data);
								 setTimeout(function(){
									 //支付跳转
									 $('#payModal').modal('hide');
									 window.location.replace('../../../sp/mobile/Skip/toOrder?flag=PC');
								 },2000);
							 })
							
						 }
					 })
				 },5000) 
				if(data.data.result_code == "true"){
					$('#payModal').modal('show');
					$("#modalTitle").html('<img alt="" src="../../../sp/public/images/pc/WePayLogo.png"  width="300" height="90"/>');
					$("#payBody").html(' <img alt="" src="../../../sp/'+data.data.url.substring(data.data.url.indexOf('public'),data.data.url.length)+'" width="300" height="300"><img alt="" src="../../../sp/public/images/pc/WeCon.png"/>');
				}
			});
		}
	}else{
		//支付宝
		if(is_weixin()){
	     //微信浏览器
		}else{
	     //PC支付
			console.log('支付宝PC端支付');
			var AliPay_ajaxReturn = $.ajax({url:"/sp/AliPay/doUnifiedOrder", data: {paramId:getUrlParms("paramId")}});
			AliPay_ajaxReturn.done(function(data){
				if(data.data != 'FAIL'){
					$('#alipayModal').modal('show');
					$('#alipayTitle').html('支付宝支付跳转请等待');
					$('#alipayBody').html(data.data);
				}
			});
		}
	}
}
function checkPayType(){
	var choicePay = $("input[type='radio']:checked");
	var flag = false;
	if(choicePay.length == 0){
		$(".alert").css("display","block");
		$(".alert").html("请选择支付方式！");
		flag = false;
	}else{
		$(".alert").css("display","none");
		flag = true;
	}
	return flag;
}
function showLoading(text){
    $("#loadingText").html(text);
    $("#loading").modal("show");
}
function hideLoading(){
    $("#loading").modal("hide");
}
//地址栏得到参数
function getUrlParms(name){
   var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
   var r = window.location.search.substr(1).match(reg);
   if(r!=null)
   return unescape(r[2]);
   return null;
}

function queryStampSet(){
	$.ajax({
		url:'/sp/mobile/Execute/queryStampSetById',
		type:'post',
		data:{setId:getUrlParms("paramId")},
		success:function(data){
			if(data.data){
				$("#copies").html(data.data.copies+'张'+'&nbsp;&nbsp;'+'¥'+data.data.totalPrice);
				getFile(data.data.fileId);
			}
		}
	})
}

function getFile(id){
	$.ajax({
		url:'/sp/mobile/Execute/queryFileById',
		type:'post',
		data:{fileId:id},
		success:function(data){
			 if(data.data){
				 $("#fileName").html(data.data.realName);
			 }
		}
	})
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
function getSessionUser(){
	var flag = false;
	$.ajax({
		url:'/sp/mobile/Execute/getSessionForUser',
		type:'POST',
		async:false,
		success:function(data){
			if(data.desc == "success"){
				var res = data.data;
				openId = res.wxOpenId;
				$("#login").css("display","block");
				$(".radiu").css("display","block");
				$(".radiu").css("background-image","url('"+res.headImgUrl+"')");
				flag = true;
			}
		}
	});
	return flag;
}