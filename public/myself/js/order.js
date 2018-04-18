function queryOrder(){
	$.ajax({
		url:'/sp/mobile/Execute/queryStampSet',
		type:'post',
		success:function(data){
			if(data.data.data){
				var dataObj = data.data.data;
				var html = '<ol>';
				for(var i = 0;i<dataObj.length;i++){
					$.ajax({
						url:"/sp/mobile/Execute/queryFileById",
						data: {fileId:dataObj[i].fileId},
						async:false,
						success:function(data){
							//是否支付
							if(dataObj[i].payState == '1'){
								html+='<li><div class="page-header"><h4>'+data.data.realName+'<small><span class="label label-success">已支付</span>&nbsp;&nbsp;&nbsp;订单号:'+dataObj[i].outTradeNo.substring(0,10)+'&nbsp;¥'+dataObj[i].totalPrice+'</small></h4></div><p><img src='+dataObj[i].filePath+'/></p></li>';
							}else if(dataObj[i].payState == '2'){
								html+='<li><div class="page-header"><h4>'+data.data.realName+'<small><span class="label label-default">未支付</span>&nbsp;&nbsp;&nbsp;订单号:'+dataObj[i].outTradeNo.substring(0,10)+'&nbsp;¥'+dataObj[i].totalPrice+'</small></h4></div><p><img src='+dataObj[i].filePath+'/></p></p></li>';
							}
						}
					});
				}
				html +='</ol>';
				console.log(html);
				$(".widget-content").html(html);
			}
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
				$(".account-name").html(res.nickName);
				$("#headShot").attr("src",res.headImgUrl);
				flag = true;
			}
		}
	});
	return flag;
}
$(function(){
	queryOrder();
	getSessionUser();
})