$(document).ready(function(){
	$('#res_selector').ajaxChosen({
	    dataType: 'json',
	    type: 'GET',
	    url:'/admin/activity/resmgr/search'
	},{
	    loadingImg: '/assets/img/loading.gif'
	})
	
	$('#resmgr_btn_submit').click(function(e) {
		var tmp = $('#activityUuid').val();
		$.post('/admin/activity/resmgr/save/', $('#activity_resmgr_form').serialize(), function(data) {
			if (data.status == 'success') {
				alert('配置保存成功');
				location.href = '/admin/activity/' + tmp + '/reslist';
				//location.href = '/admin/activity/list'
			} else {
				if (!!data.message) {
					alert(data.message)
				} else {
					alert('配置保存失败');
				}
				// 刷新token
				if (!!data.token) {
					$('#token').val(data.token)
				}
			}
		});
	})
	
	//showSpeakerSelector();
});

/*function showSpeakerSelector() {
	// speaker
	$.get('/user/speakers/json', function() {
	}).always(function(data) {
		if (data.status == 'success') {
			$.each(data.data, function(i, e) {
				$('#resmgr_btn_submit').append('<option value="' + e.uuid
							+ '">' + e.name
							+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
							+ (typeof(e.hospitalName) == 'undefined' ? '无' : e.hospitalName) + '</option>')
			
			})
		}
		$('#resmgr_btn_submit').chosen({
			placeholder_text_single : "请选择该资源的讲者",
			no_results_text : "没有匹配到结果"
		});
	})
}*/
