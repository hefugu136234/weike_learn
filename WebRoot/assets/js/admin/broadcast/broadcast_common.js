$(document).ready(function() {
	showActive([ 'live-list', 'live-mgr' ]);

	$('#bookStartDate').datetimepicker({
		format : 'YYYY-MM-DD HH:mm',
		ignoreReadonly: true
	});
	$('#bookEndDate').datetimepicker({
		format : 'YYYY-MM-DD HH:mm',
		useCurrent : false,
		ignoreReadonly: true
	});
	$('#startDate').datetimepicker({
		format : 'YYYY-MM-DD HH:mm',
		ignoreReadonly: true
	});
	$('#endDate').datetimepicker({
		format : 'YYYY-MM-DD HH:mm',
		useCurrent : false,
		ignoreReadonly: true
	});
	
	$("#bookStartDate").on("dp.change", function(e) {
		$('#bookEndDate').data("DateTimePicker").minDate(e.date);
	});
	$("#bookEndDate").on("dp.change", function(e) {
		$('#bookStartDate').data("DateTimePicker").maxDate(e.date);
	});

	$("#startDate").on("dp.change", function(e) {
		$('#endDate').data("DateTimePicker").minDate(e.date);
	});
	$("#endDate").on("dp.change", function(e) {
		$('#startDate').data("DateTimePicker").maxDate(e.date);
	});
	
	// 讲者下拉框异步加载
	$('#spaker_selector').ajaxChosen({
		dataType : 'json',
		type : 'GET',
		url : '/project/threescreen/search/speaker'
	}, {
		loadingImg : '/assets/img/loading.gif'
	});
	
	/**
	 * 初始化时间插件
	 */

	jQuery.validator.addMethod("select", function(value, element) {
		return $(element).prop('selectedIndex') != 0;
	}, "请选中一个有效选项.");

	jQuery.validator.addMethod("val_empty", function(value, element) {
		return !!value;
	}, "请先赋一个值");

	var validator = $('#broadcast_form').validate({
		ignore : ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : false,
		rules : {
			limitNum : {
				digits : true
			},
			integral:{
				digits : true
			},
			platFormType : {
				select : true
			}
		},
		messages : {
			name : "请输入直播名称",
			limitNum : "请输入人数限制的整数",
			integral:"请填入正整数",
			bookStartDate:"请输入报名开始时间",
			bookEndDate:"请输入报名结束时间",
			startDate : "请输入直播开始时间",
			endDate : "请输入直播结束时间",
			platFormType : "请输入直播结束时间",
			mark:"请输入简介",
			castAction :"请输入直播接口地址"
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

});

function changePlat(){
	var option=$('#platFormType').find("option:selected");
	var id=option.val();
	if(id==1){
		var url=option.attr('data-requestUrl');
		$('#castAction').val(url);
		$('div.live-interface-model').removeClass('hide')
	}else if(id==3){
		var url=option.attr('data-requestUrl');
		$('#castAction').val(url);
		$('div.live-interface-model').removeClass('hide')
	}else{
		$('div.live-interface-model').addClass('hide');
	}
	
}
