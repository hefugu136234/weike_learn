// Custom scripts
$(document).ready(function() {
	$("#clinic_bar").addClass("active");
	$.getJSON("/f/clinic/info", function(data) {
		// console.log(data);
		console.log(data.clinicName);		
		$('#clinicName').html(data.clinicName);
		// console.log(data.clinicName);
		$("#clinicDesc").val(data.clinicDescription);

		// var mediasList=data.mediasList;
		// console.log(mediasList);
		// console.log(mediasList.length);
		// $.each(mediasList,function(index,item){
		// var uuid=item.uuid;
		// var taskId=item.taskId;
		// var type=item.type;
		// var description=item.description;
		// var originHost=item.originHost;
		// });
	});

	// 对话框每次出现时，初始化其中的数据
	$('#imageModal').on('show.bs.modal', function(e) {
		$('#image-preview').hide();
		$('#image-preview').attr('src', '');
		$('#taskId').val('');
		$('#image_description').val('');
	});

	$('#finshImage').click(function() {
		// 提交前校验
		var taskId = $('#taskId').val();
		if ($('#image-preview').attr('src') == '' || taskId == '') {
			alert("请先上传图片");
			return;
		}
		$.post('/f/clinic/load/image', {
			'taskId' : taskId,
			'description' : $('#image_description').val()
		}, function(data) {
			if(data.status=='success'){
				alert('图片保存成功');
				$('#imageModal').modal('hide');
			}else{
				alert('图片保存失败');
			}
		});

	});

	// 加载上传图片对话框
	$('#uploadImage').uploadify({
		'swf' : '/assets/js/uploadify/uploadify.swf',
		'uploader' : 'http://cloud.lankr.cn/api/image/upload',
		'formData' : {
			'appKey' : 'ff7a9de914595ec790dbf5b32ab46e9a'
		},
		'fileObjName' : 'file',
		'fileTypeExts' : '*.bmp;*.jpg;*.jpeg;*.png',
		'method' : 'post',
		'fileSizeLimit' : 10000,
		'auto' : true,
		'cancelImg' : '/assets/js/uploadify/uploadify-cancel.png',
		'onUploadSuccess' : function(file, data, response) {
			$('#image-preview').show();
			var json_data = JSON.parse(data);
			
			$('#image-preview').attr('src', json_data.url);
			$('#taskId').val(json_data.taskId);
			console.log($('#taskId').val());
			
		}
	});

});
