$(document).ready(function() {
	showActive([ 'group_mgr_nav', 'holder_project' ]);

	jQuery.validator.addMethod("select", function(value, element) {
		//console.log($(element).prop('selectedIndex'));
		return $(element).prop('selectedIndex') != 0;
	}, "请选中一个有效选项.");
	jQuery.validator.addMethod("val_length", function(value, element) {
		return value.length==3;
	}, "请先赋一个值");
	$('#group_form').validate({
		ignore: ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : false,
		rules : {
			serialNum:{
				digits:true,
				val_length:true
			},
			manufacturer_uuid : {
				select: true
			}
		},
		messages : {
			name : "请输入产品组名称",
			serialNum:{
				required:"请输入产品组编号",
				digits:"请输入3位数字",
				val_length:"请输入3位数字"
			},
			manufacturer_uuid : "请关联厂商",
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
	
	//$('#manufacturer_selector_chosen').width(320);
	
//	$('#manufacturer_selector').chosen({
//		search_contains: true,
//		placeholder_text_single : "请选择该资源的讲者",
//		no_results_text : "没有匹配到结果"
//	});
	
	$('#manufacturer_selector').ajaxChosen({
        dataType: 'json',
        type: 'GET',
        url:'/project/group/product/search/Manufacturer'
},{
        loadingImg: '/assets/img/loading.gif'
});
	

//	speaker_selector.val(edit_speaker.uuid)
//	speaker_selector.trigger("chosen:updated");

	
});

