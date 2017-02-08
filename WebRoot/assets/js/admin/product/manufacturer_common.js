$(document).ready(function() {
	showActive([ 'group_mgr_nav', 'holder_project' ]);

	jQuery.validator.addMethod("val_empty", function(value, element) {
		return !!value;
	}, "请先赋一个值");
	jQuery.validator.addMethod("val_length", function(value, element) {
		return value.length==3;
	}, "请先赋一个值");
	var validator=$('#manufacturer_form').validate({
		ignore: ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : false,
		rules : {
			serialNum:{
				digits:true,
				val_length:true
			},
			taskId : {
				val_empty: false
			}
		},
		messages : {
			name : "请输入厂商名称",
			serialNum:{
				required:"请输入厂商编号",
				digits:"请输入3位数字",
				val_length:"请输入3位数字"
			},
			taskId : "请上传厂商logo",
			address : "请输入厂商地址",
		},

		highlight : function(element) {
			$(element).closest('.form-group').addClass('has-error');
		},

		success : function(label) {
			label.closest('.form-group').removeClass('has-error');
			label.remove();
		},

		errorPlacement : function(error, element) {
			element.parent('div').append(error);
		}
		,

		submitHandler : function(form) {
			submitFrom(form);
		}
	});

	
	$('#cover_uploadify').uploadify({
		'swf' : '/assets/js/uploadify/uploadify.swf',
		'uploader' : 'http://cloud.lankr.cn/api/image/upload',
		'formData' : {
			'appKey' : 'ff7a9de914595ec790dbf5b32ab46e9a'
		},
		'fileObjName' : 'file',

		'fileTypeExts' : '*.bmp;*.jpg;*.jpeg;*.png',
		'method' : 'post',
		'fileSizeLimit' : 10000,
		'buttonText':'选择logo',
		'auto' : true,
		'cancelImg' : '/assets/js/uploadify/uploadify-cancel.png',
		'onUploadSuccess' : function(file, data, response) {
			$('#cover_preview').show();
			var json_data = JSON.parse(data);
			$('#cover_preview').attr('src', "http://cloud.lankr.cn/api/image/" +json_data.taskId + "?m/2/h/180/f/jpg");
			$('#coverTaskId').val(json_data.taskId)
		},
	});
	
});

