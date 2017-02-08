$(document).ready(function() {
	var modal_confirm_btn = $('#confirm_btn');
	var category_trace = $('#category_trace');
	var select_trace_text = '';
	var current_node = '';
	//$('#medias').val(medias);
	
	//分类选择
	$('#tree').jstree({
		'core' : {
			'data' : {
				'url' : '/asset/category/node',
				'data' : function(node) {
					return {
						'uuid' : node.id,
						'timestamp' : new Date().getTime()
					}
				}
			},
			'check_callback' : true,
			'themes' : {
				'responsive' : false
			}
		},
		'plugins' : [ 'state' ]
	}).on('changed.jstree',function(e, data) {
		if (data && data.selected && data.selected.length) {
			select_trace_text = '';
			current_node = data.node;
			$(data.node.parents).each(function(i, e) {
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
		}
	});
	
	//分类回显
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
				}
			}
		}
	});

	//分类选择框关闭触发操作
	modal_confirm_btn.click(function() {
		category_trace.empty();
		category_trace.html(select_trace_text)
		$('#categorySelectorModal').modal('hide');
		$('#categoryUuid').val(current_node.id)
	});
	
	// 上限设置
	var pc = $('#plimit_controller')
	plimitui();
	pc.change(function(){
		plimitui()
	})
	function plimitui(){
		if(pc.val() == '1'){
			$('#plimit_number').show();
			$('#plimit').val($('#plimit').val());
		}else{
			$('#plimit_number').hide();
			$('#plimit').val('-1')
		}
	}
	
	//媒体图片回显
	$('input[type="file"]').each(function(i, e) {
		var $e = $(e)
		uploaderInit(new Part($e, $e.data("type"), function(part) {
			$.each(getMedias(), function(i, e) {
				if (part.type == e.type) {
					part.renderPreview(e.url)
				}
			})
		}).init())
	})
	
	//------------------------//
	//表单验证
	jQuery.validator.addMethod("val_empty", function(value, element) {
		return !!value;
	}, "请先赋一个值");
	
	jQuery.validator.addMethod("val_integer", function(value, element) {
		return  value%1 === 0 && parseInt(value) >= -1;
	}, "请输入一个大于0的整数");
	
	$('#activity_update_form').validate({
		ignore: ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
		rules : {
			categoryUuid: {
				val_empty: true		
			},
			plimit:{
				val_integer: true
			},
			joinType:{
				val_empty: true	
			},
			notification:{
				val_empty: true
			},
			description:{
				val_empty: true
			}
		},
		messages : {
			name:"请输入活动标题",
			mark:"请输入简介",
			categoryUuid:"请选择分类",
			joinType:"请选择参与类型"
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
			submitActivity(form);
		}
	});
})

function fromText(val_text, node) {
	var id = node.id;
	var parent = node.parent;
	var text = node.text;
	if (id != '#' && !!parent && !!text) {
		val_text = text + ">" + val_text;
	}
	return val_text;
}

function selectCate(val_text, node) {
	var uuid = node.id;
	val_text = val_text + '<b>' + node.text + '</b>';
	$('#category_trace').html(val_text);
	$('#categoryUuid').val(uuid);
}

function getMedias() {
	return medias;
}

//提交表单
function submitActivity(form){
	var activityUuid = $('#activityUuid').val();
	//test
	console.log(activityUuid);
	
	var medias = [];
	$('.pre-view').each(function(i, e) {
		medias.push({
			type : $(e).data("type"),
			url : $(e).attr("src")
		})
	})
	$('#medias').val(JSON.stringify(medias))
	$.post('/admin/activity/update/', $(form).serialize(), function(data){
		//test
		console.log(data);
		
		if (data.status == 'success') {
			alert('活动修改成功');
			location.href='/admin/activity/list';
		} else {
			if(!!data.message){
				alert(data.message)
				location.href='/admin/activity/list';
			}else{
				alert('活动修改失败');
				location.href='/admin/activity/list';
			}
			// 刷新token
			if(!!data.token){
				$('#token').val(data.token)
			}
		}
	});
}
