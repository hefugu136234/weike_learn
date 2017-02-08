$(document).ready(function() {

	
	
	
	showActive([ 'news_list_nav', 'assets-mgr' ]);

	jQuery.validator.addMethod("val_empty", function(value, element) {
		return !!value;
	}, "请先赋一个值");
	$('#news_form').validate({
		ignore: ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : false,
		rules : {
			qrTaskId: {
				val_empty: true
			}
		},
		messages : {
			title:"请输入新闻标题",
			categoryUuid:"请选择新闻分类",
			summary : "请输入新闻摘要",
			qrTaskId:"请上传新闻简介图"
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

var modal_confirm_btn = $('#confirm_btn');
var category_trace = $('#category_trace');
var select_trace_text = '';
var current_node_uuid = '';
$('#tree').jstree({
	'core':{
		'data':{
			'url':'/asset/category/node',
			'data':function(node){
				return {
					'uuid':node.id,
					'timestamp':new Date().getTime()
				}
			}
		},
		'check_callback':true,
		'themes' : {
			'responsive' : false
		}
	},
	'plugins' : [ 'state' ]
}).on('changed.jstree',function(e,data){
	if (data && data.selected&& data.selected.length){
		var current_node = data.node;
		var parent=current_node.parent;
		if(parent!='#'){
			//选的不是根节点
			current_node_uuid=current_node.id;
			select_trace_text = '';
			$(data.node.parents).each(function(i,e){
				var node = getNodeById(e);
				if (e != '#') {
					select_trace_text = node.text
							+ " > "
							+ select_trace_text;
				}
			});
			select_trace_text = select_trace_text
			+ '<b>'
			+ current_node.text
			+ '</b>'
		}else{
			select_trace_text = '';
			current_node_uuid='';
		}
	}
});


	modal_confirm_btn.click(function() {
		category_trace.empty();
		category_trace.html(select_trace_text)
		$('#categorySelectorModal').modal('hide');
		$('#categoryUuid').val(current_node_uuid);
	});
	
	//修改上传地址七牛
	var $e = $('#qrcode_uploadify')
	uploaderInit(new Part($e, 1, function(part) {
	}, uploadFinished).init())
	function uploadFinished(src) {
		$('#qrTaskId').val(src)
		//$('#uploadTag').val('uploadSuccess');
	}
	
	/*$('#qrcode_uploadify').uploadify({
		'swf' : '/assets/js/uploadify/uploadify.swf',
		'uploader' : 'http://cloud.lankr.cn/api/image/upload',
		'formData' : {
			'appKey' : 'ff7a9de914595ec790dbf5b32ab46e9a'
		},
		'fileObjName' : 'file',

		'fileTypeExts' : '*.bmp;*.jpg;*.jpeg;*.png',
		'method' : 'post',
		'fileSizeLimit' : 10000,
		'buttonText':'简介图',
		'auto' : true,
		'cancelImg' : '/assets/js/uploadify/uploadify-cancel.png',
		'onUploadSuccess' : function(file, data, response) {
			$('#qrcode_preview').show();
			var json_data = JSON.parse(data);
			$('#qrcode_preview').attr('src', "http://cloud.lankr.cn/api/image/" +json_data.taskId + "?m/2/h/180/f/jpg");
			$('#qrTaskId').val(json_data.taskId)
		},
	});*/

});

function getNodeById(id) {
	var all = $('#tree').jstree();
	return all._model.data[id];
}
