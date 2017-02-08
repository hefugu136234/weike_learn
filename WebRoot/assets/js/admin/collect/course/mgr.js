var editor;
$(document).ready(
		function() {
			activeStub('collect_mgr_nav');

			// 讲者下拉框异步加载
			$('#speaker_selector').ajaxChosen({
				dataType : 'json',
				type : 'GET',
				url : '/project/threescreen/search/speaker'
			}, {
				loadingImg : '/assets/img/loading.gif'
			});

			// 封面上传(cover)
			var $e = $('#cover-view');
			uploaderInit(new Part($e, 1, function(part) {
			}, function(src) {
				$('#webCover').val(src);
			}).init());
			
			// 背景(wechat)
			var $wechat_bg = $('#wechat-bg');
			uploaderInit(new Part($wechat_bg, 2, function(part) {
			}, function(src) {
				$('#wechatBg').val(src);
			}).init());

			// UEditor
			editor = UE.getEditor('description');
			editor.addListener("ready", function() {
				editor.setContent($('#descripion_hiden').text());
			});
			

			// 表单提交
			formInit($('#new_course_form'), function(form) {
				if(! $('#webCover').val()){
					alert('请上传封面图');
					return;
				}
				if(! $('#wechatBg').val()){
					alert('请上传背景图');
					return;
				}
				if(! $('#speaker_selector').val()){
					alert('请选择讲者');
					return;
				}
				$.post('/project/collect/course/save', $(form).serialize()).always(function(data) {
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