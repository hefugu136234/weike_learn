$(function(){
	showActive([ 'offline_activity_list', 'activity-mgr' ]);
	
	$('#bookStartDate').datetimepicker({
		format : 'YYYY-MM-DD',
		ignoreReadonly: true
	});
	
	$('#bookEndDate').datetimepicker({
		format : 'YYYY-MM-DD',
		useCurrent : false,
		ignoreReadonly: true
	});
	
	$("#bookStartDate").on("dp.change", function(e) {
		$('#bookEndDate').data("DateTimePicker").minDate(e.date);
	});
	$("#bookEndDate").on("dp.change", function(e) {
		$('#bookStartDate').data("DateTimePicker").maxDate(e.date);
	});
	
	jQuery.validator.addMethod("select", function(value, element) {
		return $(element).prop('selectedIndex') != 0;
	}, "请选中一个有效选项.");

	jQuery.validator.addMethod("val_empty", function(value, element) {
		return !!value;
	}, "请先赋一个值");

	var validator = $('#activity_from').validate({
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
			price : {
				digits : true
			}
		},
		messages : {
			name : "请输入活动名称",
			address : "请输入活动地址",
			limitNum:"请填入正整数",
			price:"请填入正整数",
			integral:"请填入正整数",
			bookStartDate:"请输入报名开始时间",
			bookEndDate:"请输入报名结束时间",
			mark:"请输入简介",
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

//获取图片
function getcover(){
	var cover=[];
	$('.pre-view').each(function(i, e) {
		cover.push({
			type : $(e).data("type"),
			url : $(e).attr("src")
		});
	});
	return cover;
}