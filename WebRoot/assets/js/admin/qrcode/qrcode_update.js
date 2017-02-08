$(function() {
	showActive([ 'qrcode_mgr_nav', 'assets-mgr' ]);
	// 上传图片的初始化
	uploaderInit(new Part($('#cover'), 1, function(part) {
		var cover_hidden = $('#cover_hidden').val();
		if (!!cover_hidden) {
			part.renderPreview(cover_hidden);
		}
	}, function(src) {
		$('#cover_hidden').val(src);
	}).init());


	$('#submit_button').click(function(){
		var uuid=$('#uuid').val();
		var name = $('#name').val();
		if(name==''){
			alert('请输入名称');
			return false;
		}
		var redictUrl = $('#redictUrl').val();
		if(redictUrl==''){
			alert('请输入跳转链接');
			return false;
		}
		$('#submit_button').attr('disabled','disabled');
		var auth = $('#auth').prop('checked');
		if(auth){
			auth=1;
		}else{
			auth=0;
		}
		var title = $('#title').val();
		var cover = $('#cover_hidden').val();
		var mark = $('#mark').val();
		$.post('/project/qrcode/update/data',
				{
			'uuid':uuid,
			'name':name,
			'redictUrl':redictUrl,
			'auth':auth,
			'title':title,
			'cover':cover,
			'mark':mark
				},function(data){
					$('#submit_button').removeAttr('disabled');
					if(data.status=='success'){
						window.location.href = '/project/qrcode/list/page';
					}else{
						alert(data.message);
					}
				});
	});


});

