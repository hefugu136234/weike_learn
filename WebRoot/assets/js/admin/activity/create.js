$(document).ready(function() {
	var modal_confirm_btn = $('#confirm_btn');
	var category_trace = $('#category_trace');
	var select_trace_text = '';
	var current_node = '';
	$('#tree')
			.jstree(
					{
						'core' : {
							'data' : {
								'url' : '/asset/category/node',
								'data' : function(node) {
									return {
										'uuid' : node.id,
										'timestamp' : new Date()
												.getTime()
									}
								}
							},
							'check_callback' : true,
							'themes' : {
								'responsive' : false
							}
						},
						'plugins' : [ 'state' ]
					})
			.on(
					'changed.jstree',
					function(e, data) {
						if (data && data.selected
								&& data.selected.length) {
							select_trace_text = '';
							current_node = data.node;
							$(data.node.parents)
									.each(
											function(i, e) {
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
			$('#plimit').val('')
		}else{
			$('#plimit_number').hide();
			$('#plimit').val('-1')
		}
	}
	
	// 提交表单
	jQuery.validator.addMethod("val_empty", function(value, element) {
		return !!value;
	}, "请先赋一个值");
	
	jQuery.validator.addMethod("val_integer", function(value, element) {
		return  value%1 === 0 && parseInt(value) >= -1;
	}, "请输入一个大于0的整数");
	
	$('#activity_form').validate({
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
			}
			
		},
		messages : {
			name:"请输入活动标题",
			categoryUuid:"请选择活动分类",
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
		}
		,
		submitHandler : function(form) {
			submitActivity(form);
		}
	});
})
function submitActivity(form){
	$.post('/admin/activity/save',
			$(form).serialize(),
			function(data){
		if (data.status == 'success') {
			alert('活动创建成功');
			location.href='/admin/activity/list'
		} else {
			if(!!data.message){
				alert(data.message)
			}else{
				alert('活动创建失败');
			}
			// 刷新token
			if(!!data.token){
				$('#token').val(data.token)
			}
		}
	});
}



