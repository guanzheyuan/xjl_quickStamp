// JavaScript Document
$(document).on("pagecreate","#ch_ad",function(){
	$(".list_address li").on("swipeleft",function(){
		$(this).animate({marginLeft:'-6.4rem'});
		$(this).find('a.edit').animate({right:'7.6rem'});
		$(this).find('.delete').animate({right:'0rem'});
	});
	$(".list_address li").on("swiperight",function(){
		$(this).animate({marginLeft:'0rem'});
		$(this).find('a.edit').animate({right:'1.2rem'});
		$(this).find('.delete').animate({right:'-6.4rem'});
	});
});

