function queryToiletList(){
	var dataParams = {toiletName:'',pageNumber:1,pageSize:10};
	$.ajax({
		url:'/sp/mobile/Execute/queryTolietList',
		type:'post',
		datType:'application/json',
		data:dataParams,
		success:function(data){
			 console.log(data);
		}
	});
}

function test(){
	var dataParams = {toiletName:'',pageNumber:1,pageSize:10};
	 $.ajax({  
         type : "POST",  
         url : "http://47.98.200.5:8080/watersaving/api/toilet/list",  
         dataType : "application/x-www-form-urlencoded;charset=UTF-8",  
         data:dataParams,  
         success : function(data){  
              console.log(data);
         }  
      });  
}
function jsonpCallback(){
	console.log(12);
}
$(function(){
	queryToiletList();
	test();
});