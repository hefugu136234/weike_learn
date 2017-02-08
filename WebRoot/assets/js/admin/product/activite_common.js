$(document).ready(function() {
	showActive([ 'active_mgr_nav', 'holder_project' ]);

	jQuery.validator.addMethod("select", function(value, element) {
		// console.log($(element).prop('selectedIndex'));
		return $(element).prop('selectedIndex') != 0;
	}, "请选中一个有效选项.");
	jQuery.validator.addMethod("val_largh", function(value, element) {
		return value <= 1000;
	}, "请先赋一个值");
	$('#active_form').validate({
		ignore : ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : false,
		rules : {
			num : {
				digits : true,
				val_largh : true
			},
			manufacturer_uuid : {
				select : true
			},
			group_uuid : {
				select : true
			},
			time : {
				select : true
			}
		},
		messages : {
			time : "请选择时间时限",
			num : {
				digits : "请输入1000以内的数字",
				val_largh : "请输入1000以内的数字"
			},
			manufacturer_uuid : "请选择厂商",
			group_uuid : "请选择产品组"
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

	// $('#manufacturer_selector_chosen').width(320);

	// $('#manufacturer_selector').chosen({
	// search_contains: true,
	// placeholder_text_single : "请选择该资源的讲者",
	// no_results_text : "没有匹配到结果"
	// });

	$('#manufacturer_selector').ajaxChosen({
		dataType : 'json',
		type : 'GET',
		url : '/project/group/product/search/Manufacturer'
	}, {
		loadingImg : '/assets/img/loading.gif'
	});

	// speaker_selector.val(edit_speaker.uuid)
	// speaker_selector.trigger("chosen:updated");

});

function changeOption(value) {
	if (value != 0) {
		// 请求数据
		var group_selector=$('#group_selector');
		clearContent(group_selector);
		$.getJSON('/project/group/active/group/' + value, function(data) {
			if(data.status=='success'){
				var items=data.items;
				$.each(items, function(index, item) {
					var option = '<option value=' + item.id + '>' + item.text
							+ '</option>';
					group_selector.append(option);
				});
			}
		});

	}
}

function clearContent(parent) {
	parent.empty();
	parent.append('<option value="0">请选择</option>');
}

