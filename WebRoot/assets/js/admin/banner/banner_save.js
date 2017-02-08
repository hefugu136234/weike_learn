$(document).ready(function() {
	showActive([ 'assets-mgr', 'banner_mgr_nav' ]);
	
	//banner图片上传操作
	$('#bannerImg_uploadify').uploadify({
		'swf' : '/assets/js/uploadify/uploadify.swf',
		'uploader' : 'http://upload.qiniu.com/',
		'formData' : {
			'token' : $('#signature').val()
		},
		'fileObjName' : 'file',
		'fileTypeExts' : '*.bmp;*.jpg;*.jpeg;*.png',
		'method' : 'post',
		'fileSizeLimit' : 10000,
		'buttonText':'选择图片',
		'auto' : true,
		'cancelImg' : '/assets/js/uploadify/uploadify-cancel.png',
		'onUploadSuccess' : function(file, data, response) {
			//上传成功后自动填写图片名称,提示上传成功
			$('#bannerUploadStatus').show();
			var json_data = JSON.parse(data);
			if (json_data.key) {
				$('#bannerUploadStatus').html('上传成功');
				$('#bannerSize').val(0);
				$('#bannerNum').val(0);
				$('#bannerTaskId').val(json_data.key);
				$('#bannerName').val(file.name);
				$('#uploadTage').val("success");
			} 
			//test
			console.log($('#uploadTage').val());
		} ,'onUploadError': function (file, errorCode, errorMsg, errorString) {  
			if(errorMsg==401){
				alert('token数据无效，请重新加载页面');
			}else if(errorMsg==413){
				alert('文件的太大，无法上传成功');
			}else if(errorMsg==614){
				alert('文件已存在');
			}else{
				alert('页面数据过期，请重新刷新');
			}
        } 
	});
	
	//新增Banner请求
	$('#submit_form').submit(function(event) {
		event.preventDefault();
		var $form = $(this);
		$.post('/project/banner/', $form.serialize(), function() {
		}).always(function(data) {
			if ('success' == data['status']) {
				alert("保存成功");
				window.location.href = '/project/banner/mgr';
			} else {
				alert(data['message']);
			}
		});
	});
})