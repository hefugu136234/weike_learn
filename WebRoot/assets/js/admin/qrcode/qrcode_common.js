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

	// 提交表单
	jQuery.validator.addMethod("val_empty", function(value, element) {
		return !!value;
	}, "该项不能为空");

	jQuery.validator.addMethod("val_integer", function(value, element) {
		return parseInt(value) >= 0;
	}, "非法数据，请重新输入合适的数据");
	jQuery.validator.addMethod("select", function(value, element) {
		return $(element).prop('selectedIndex') != 0;
	}, "请选中一个有效选项.");

	$('#qrcode_form').validate({
		ignore : ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : false,
		rules : {
			type : {
				select : true
			}
		},
		messages : {
			name : "请输入二维码的名称",
			type : "请选择二维码类型"
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
			if (!is_submit) {
				checkFrom(form);
			}else{
				return false;
			}
		}
	});

	// select change 事件
	$('#qrcode_type').change(function() {
		var type = $(this).val();
		if (!!type) {
			$('#contrl').removeClass('hide');
			if (type == 4) {
				$('#select_obj_div').hide();
				$('#auth').removeAttr('disabled');
				$('#redictUrl').removeAttr('disabled');
			} else {
				buildSearchSelect(type);
				$('#select_obj_div').show();
				$('#auth').attr('disabled', 'disabled');
				$('#redictUrl').attr('disabled', 'disabled');
			}
		} else {
			$('#contrl').addClass('hide');
		}
	});

});

// 还没提交
var is_submit = false;

function buildSearchSelect(type) {
	$('#select_obj').empty();
	var select_val = '<select class="form-control" id="select_obj_item"></select>';
	$('#select_obj').append(select_val);

	// 获取服务器对应的10条数据
	$.getJSON('/project/qrcode/get/obj/resource', {
		'type' : type,
		'q' : ''
	}, function(data) {
		if (data.status == 'success') {
			initModalData(data, type);
		} else {
			alert(data.message);
		}
	});

}

function checkFrom(from) {
	var type = $('#qrcode_type').val();
	if (type != 4) {
		var obj = $('#select_obj_item').val();
		if (obj == 0) {
			alert('请选择对应的二维码载体');
			return false;
		}
	}
	var redictUrl = $('#redictUrl').val();
	if (redictUrl == '') {
		alert('请填写二维码的跳转链接');
		return false;
	}
	if (type != 6) {
		var title = $('#title').val();
		if (title == '') {
			alert('请填写图文消息的标题');
			return false;
		}
		var cover = $('#cover_hidden').val();
		if (cover == '') {
			alert('请上传图文消息的封面');
			return false;
		}
		var mark = $('#mark').val();
		if (cover == '') {
			alert('请上传图文消息的简介');
			return false;
		}
	}
	submitForm(from);
}

function submitForm(from) {
	is_submit=true;
	var name = $('#name').val();
	var type = $('#qrcode_type').val();
	var obj = $('#select_obj_item').val();
	var redictUrl = $('#redictUrl').val();
	var auth = $('#auth').prop('checked');
	if (auth) {
		auth = 1;
	} else {
		auth = 0;
	}
	var title = $('#title').val();
	var cover = $('#cover_hidden').val();
	var mark = $('#mark').val();
	$.post('/project/qrcode/add/data', {
		'name' : name,
		'type' : type,
		'obj' : obj,
		'redictUrl' : redictUrl,
		'auth' : auth,
		'title' : title,
		'cover' : cover,
		'mark' : mark
	}, function(data) {
		is_submit=false;
		if (data.status == 'SUCCESS') {
			window.location.href = '/project/qrcode/list/page';
		} else {
			alert(data.message);
		}
	});
}

function initModalData(data, type) {
	buildSelect(data.items, type);
	var select_obj_item = $('#select_obj_item');
	select_obj_item.get(0).selectedIndex = 0;
	// 绑定change事件
	select_obj_item.change(function() {
		var uuid = $(this).val();
		if (!!uuid) {
			var redictUrl = $('#redictUrl');
			var auth = $('#auth');
			if (type == 1) {
				redictUrl.val('/api/webchat/activity/total/page/' + uuid);
				auth.prop("checked", true);
			} else if (type == 3) {
				redictUrl.val('/api/webchat/broadcast/first/page/' + uuid);
				auth.prop("checked", true);
			} else if (type == 6) {
				redictUrl.val('/api/webchat/resource/first/page/' + uuid);
				auth.prop("checked", true);
			} else if (type == 7) {
				redictUrl.val('/api/webchat/game/first/page/' + uuid);
				auth.prop("checked", true);
			}
		}
	});
	select_obj_item.ajaxChosen({
		dataType : 'json',
		type : 'GET',
		url : '/project/qrcode/get/obj/resource?type=' + type
	}, {
		loadingImg : '/assets/img/loading.gif'
	});

	$('#select_obj_item_chosen').css('width', '100%');
}

function buildSelect(list, type) {
	var obj_label = $('#obj_label');
	var select_obj_item = $('#select_obj_item');
	if (type == 1) {
		obj_label.html('活动选择：');
		select_obj_item.append('<option value="">请输入"活动名称"检索活动</option>');
	} else if (type == 3) {
		obj_label.html('直播选择：');
		select_obj_item.append('<option value="">请输入"直播名称"检索直播</option>');
	} else if (type == 6) {
		obj_label.html('资源选择：');
		select_obj_item.append('<option value="">请输入"资源名称"检索资源</option>');
	} else if (type == 7) {
		obj_label.html('资源选择：');
		select_obj_item.append('<option value="">请输入"游戏名称"检索游戏</option>');
	}
	if (!!list && list.length > 0) {
		$.each(list, function(index, item) {
			var option = buildOption(item);
			select_obj_item.append(option);
		});
	}
}

function buildOption(addition) {
	var option = '<option value="' + addition.id + '">' + addition.text
			+ '</option>';
	return option;
}