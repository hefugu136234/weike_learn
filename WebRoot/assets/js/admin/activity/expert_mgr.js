$(document).ready(function(){
	
	//讲者选择框
	$('#speaker_selector').ajaxChosen({
	    dataType: 'json',
	    type: 'GET',
	    url:'/project/threescreen/search/speaker'
	},{
	    loadingImg: '/assets/img/loading.gif'
	})
	
	//微信专家背景
	var $e = $('#speakerWechatBgImg');
	uploaderInit(new Part($e, 1, function(part) {
	}, uploadFinished).init());
	function uploadFinished(src) {
		$('#webchatBgImgTaskId').val(src);
	}
	
	$('#expertMgr_btn_submit').click(function(e) {
		var activityUuid = $('#activityUuid').val();
		var speakerUuid = $('#speaker_selector').val();
		var webchatBgImage = $('#webchatBgImgTaskId').val();
		if('0' == speakerUuid){
			alert('请选择专家');
			return;
		}
		if(! webchatBgImage){
			alert('请上传专家微信背景图');
			return;
		}
		$.post('/admin/activity/expertMgr/save', $('#activity_expertMgr_form').serialize(), function(data) {
			if (data.status == 'success') {
				alert('操作成功');
				location.href = '/admin/activity/expert/page/' + activityUuid;
			} else {
				if (!!data.message) {
					alert(data.message)
					window.location.reload();
				} else {
					alert('操作失败');
				}
			}
		});
	})
});