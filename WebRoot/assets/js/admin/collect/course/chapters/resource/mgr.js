$(document).ready( function() {
	activeStub('collect_mgr_nav');

	var chapterUuid = $('#chapterUuid').val();
	
	$('#res_selector').ajaxChosen({
	    dataType: 'json',
	    type: 'GET',
	    url:'/admin/activity/resmgr/search'
	},{
	    loadingImg: '/assets/img/loading.gif'
	})

	// 表单提交
	formInit($('#new_course_chapters_res_form'), function(form) {
		$.post('/project/collect/course/chapters/resource/save', $(form).serialize()).always(function(data) {
			if (data.status == 'success') {
				alert('操作成功');
				window.location.href='/project/collect/course/chapters/resource/list/page/' + chapterUuid;
			} else if (!isBlank(data.message)) {
				alert(data.message)
			} else {
				alert('操作失败')
			}
		})
	});
})