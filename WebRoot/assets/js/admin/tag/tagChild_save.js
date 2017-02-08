$(document).ready(function() {
	showActive([ 'assets-mgr', 'tag-list-nav' ]);
	var p_uuid = $('#parent_uuid').val();
	
	//标签名验证
//	$('#tag_name').blur(function(event){
//		event.preventDefault();
//		var tag_name = $('#tag_name').val()
//		$.get('/tag/tagNameValid', {tagName:tag_name}, function(){
//		}).always(function(data) {
//			if ('success' == data['status']) {
//				$('#tag_name').val('');
//				$('#error_info').append('<font color="red">已有相似的标签名存在！</font>');
//				$('#tag_name').focus(function(){
//					$('#error_info').empty();
//				})
//			}else{
//				$('#error_info').empty();
//			}
//		});
//	})
	
	//新增标签请求
	$('#childTag_save').submit(function(event) {
		event.preventDefault();
		var $form = $(this);
		$.post('/tag/childTagSave', $form.serialize(), function() {
		}).always(function(data) {
			if ('success' == data['status']) {
				alert("保存成功");
				
				window.location.href = '/tag/showChildTagPag/' + p_uuid;
				//location.reload(true);
			} else {
				alert(data['message']);
			}
		});
	});
})
