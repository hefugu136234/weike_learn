$(document).ready(function(){
	var isHasAvatar = $('#isHasAvatar').val();
	if('' == isHasAvatar){
		$('#head_image').hide();
	}else{
		$('#headImage').attr("src", isHasAvatar);
	}
	
	var isCertification = $('#isCertification').val();
	if('' == isCertification){
		$('#certification_info').hide();
		$('#certification_noInfo').show();
	}else{
		$('#certification_info').show();
		$('#certification_noInfo').hide();
	}
	
	var isHasAddress = $('#isHasAddress').val();
	if('' == isHasAddress){
		$('#address').hide();
		$('#noAddress').show();
	}else{
		$('#address').show();
		$('#noAddress').hide();
	}
})
