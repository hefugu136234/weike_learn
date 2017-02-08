$(document).ready(function() {
	showActive([ 'threescreen_mgr_nav', 'assets-mgr' ]);
	
	jQuery.validator.addMethod("select", function(value, element) {
		return $(element).prop('selectedIndex') != 0;
	}, "请选中一个有效选项.");

	jQuery.validator.addMethod("val_empty", function(value, element) {
		return !!value;
	}, "请先赋一个值");
	var validator=$('#threescreen_form').validate({
		ignore: ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : false,
		messages : {
			name : "请输入三分屏名称",
			fileId : "请上传视频文件",
			pdfTaskId:"请上传pdf文件",
			categoryUuid : "请先选择一个分类"
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
		},
		submitHandler : function(form) {
			submitFrom(form);
		}
	});
	
	$('#spaker_selector').ajaxChosen({
        dataType: 'json',
        type: 'GET',
        url:'/project/threescreen/search/speaker'
	},{
        loadingImg: '/assets/img/loading.gif'
	});
	


	
	$('#pdf_upload').uploadify({
		'swf' : '/assets/js/uploadify/uploadify.swf',
		'uploader' : 'http://upload.qiniu.com/',
		'formData' : {
			'token' : $('#signature').val()
		},
		'fileObjName' : 'file',

		'fileTypeExts' : '*.pdf',
		'method' : 'post',
		'fileSizeLimit' : 500000,
		'buttonText' : '选择PDF',
		'auto' : true,
		'cancelImg' : '/assets/js/uploadify/uploadify-cancel.png',
		'onUploadSuccess' : function(file, data, response) {
			$('#pdf_status').show();
			var json_data = JSON.parse(data);
			if (json_data.key) {
				$('#pdf_status').html('pdf上传成功');
				$('#pdfTaskId').val(json_data.key);
				//整个表单验证
				//validator.valid();
				validator.element($('#pdfTaskId'));
			} 
		} ,'onUploadError': function (file, errorCode, errorMsg, errorString) {  
//			 $("#pdf_upload").uploadify("settings", "formData", { 'token': data });  
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
	
	//pdf封面上传改为七牛
	var $e = $('#cover_uploadify');
	uploaderInit(new Part($e, 1, function(part) {
	}, uploadFinished).init())
	function uploadFinished(src) {
		$('#cover').val(src)
		//$('#uploadTag').val('uploadSuccess');
	}
	
	/*
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
		'buttonText':'选择封面',
		'auto' : true,
		'cancelImg' : '/assets/js/uploadify/uploadify-cancel.png',
		'onUploadSuccess' : function(file, data, response) {
			$('#cover_preview').show();
			var json_data = JSON.parse(data);
			$('#cover_preview').attr('src', "http://cloud.lankr.cn/api/image/" +json_data.taskId + "?m/2/h/180/f/jpg");
			$('#cover').val(json_data.taskId)
		},
	});*/
	
	$('#tree').jstree({
		'core' : {
			'multiple' : false,
			'data' : {
				'url' : '/asset/category/node',
				'data' : function(node) {
					return {
						'uuid' : node.id,
						'timestamp' : new Date().getTime()
					};
				}
			},
			'check_callback' : true,
			'themes' : {
				'responsive' : false
			}
		},
		'plugins' : [ 'state' ]
	}).on({
		'after_open.jstree' : function(e, data) {
			// 清空所有的选中
			$('#tree').jstree().deselect_all();
			// 打开选中
			var categoryUuid = $('#categoryUuid').val();
			if (!!categoryUuid) {
				var targetNode = $('#tree').jstree('get_node', categoryUuid);
				if (targetNode) {
					//如果有数据时，初始化
					nodeChage(targetNode);
					if (!targetNode.state.selected) {
						$('#tree').jstree('select_node', categoryUuid);
					}
				}
			}
		}
	});

	// 对话框每次出现时，初始化其中的数据
	$('#categorySelectorModal').on('show.bs.modal', function() {
		$('#tree').bind('changed.jstree', function(e, data) {
			var node = data.node;
			if (node) {
				var parents = node.parents;
				if (parents) {
					var val_text = '';
					$(parents).each(function(index, item) {
						var parent_node = $('#tree').jstree('get_node', item);
						val_text = fromText(val_text, parent_node);
					});
					$('#category_trace').empty();
					$('#categoryUuid').val('');
					if (!!val_text) {
						selectCate(val_text, node);
						//单个元素的验证
						validator.element($('#categoryUuid'));
					}
				}
			}
		});
	});

	$('#categorySelectorModal').on('hide.bs.modal', function() {
		$('#tree').unbind('changed.jstree');
	});
});

//修改初始化的数据
function nodeChage(node){
	if (node) {
		var parents = node.parents;
		if (parents) {
			var val_text = '';
			$(parents).each(function(index, item) {
				var parent_node = $('#tree').jstree('get_node', item);
				val_text = fromText(val_text, parent_node);
			});
			if (!!val_text) {
				selectCate(val_text, node);
			}
		}
	}
}

// 页面显示
function fromText(val_text, node) {
	var id = node.id;
	var parent = node.parent;
	var text = node.text;
	if (id != '#' && !!parent && !!text) {
		val_text = text + ">" + val_text;
	}
	return val_text;
}

// 页面赋值
function selectCate(val_text, node) {
	var uuid = node.id;
	val_text = val_text + '<b>' + node.text + '</b>';
	$('#category_trace').html(val_text);
	$('#categoryUuid').val(uuid);
}
