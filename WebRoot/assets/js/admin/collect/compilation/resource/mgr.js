$(document).ready( function() {
	activeStub('collect_mgr_nav');

	var compilationUuid = $('#compilationUuid').val();
	
	$('#res_selector').ajaxChosen({
	    dataType: 'json',
	    type: 'GET',
	    url:'/admin/activity/resmgr/search'
	},{
	    loadingImg: '/assets/img/loading.gif'
	})

	// 表单提交
	formInit($('#new_compilation_res_form'), function(form) {
		$.post('/project/collect/compilation/resource/save', $(form).serialize()).always(function(data) {
			if (data.status == 'success') {
				alert('操作成功');
				window.location.href='/project/collect/compilation/resource/list/page/' + compilationUuid;
			} else if (!isBlank(data.message)) {
				alert(data.message)
			} else {
				alert('操作失败')
			}
		})
	});
})