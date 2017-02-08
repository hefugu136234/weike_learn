var editor;
$(document).ready(function() {
	activeStub('collect_mgr_nav');

	var courseUuid = $('#courseUuid').val();
	
	// UEditor
	editor = UE.getEditor('description');
	editor.addListener("ready", function() {
		editor.setContent($('#descripion_hiden').text());
	});

	// 表单提交
	formInit($('#new_course_chapters_form'), function(form) {
		if(!/^[0-9]*$/.test($('#passScore').val())){
			$('#passScore').val('');
	        alert("通过分数请输入数字!");
	        return;
	    }
		
		$.post('/project/collect/course/chapters/save', $(form).serialize()).always(function(data) {
			if (data.status == 'success') {
				alert('操作成功');
				window.location.href='/project/collect/course/chapters/list/page/' + courseUuid;
			} else if (!isBlank(data.message)) {
				alert(data.message)
			} else {
				alert('操作失败');
			}
		})
	});
})