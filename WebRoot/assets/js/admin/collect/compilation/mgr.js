var editor;
$(document).ready(
	function() {
		activeStub('collect_mgr_nav');

		// 封面上传
		var $e = $('#cover-view');
		uploaderInit(new Part($e, 1, function(part) {
		}, function(src) {
			$('#webCover').val(src);
		}).init());

		// UEditor
		editor = UE.getEditor('description');
		editor.addListener("ready", function() {
			editor.setContent($('#descripion_hiden').text());
		});

		// 表单提交
		formInit($('#compilation_form'), function(form) {
			if(! $('#webCover').val()){
				alert('请上传封面图');
				return;
			}
			$.post('/project/collect/compilation/save', $(form).serialize()).always(function(data) {
				if (data.status == 'success') {
					alert('操作成功');
					window.location.href="/project/collect/list/page";
				} else if (!isBlank(data.message)) {
					alert(data.message)
				} else {
					alert('操作失败')
				}
			})
		});
	})