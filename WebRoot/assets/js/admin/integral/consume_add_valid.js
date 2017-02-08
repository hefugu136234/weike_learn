$(document).ready(function(){
	jQuery.validator.addMethod("val_empty", function(value, element) {
		return !!value;
	}, "该项不能为空");
	jQuery.validator.addMethod("val_integer", function(value, element) {
		return  parseInt(value) >= 0;
	}, "非法数据，请重新输入合适的数据");
	jQuery.validator.addMethod("select", function(value, element) {
		return $(element).prop('selectedIndex') != 0;
	}, "请选中一个有效选项.");
	jQuery.validator.addMethod("val_integer_", function(value, element) {
		return  value%1 === 0 && parseInt(value) >= -1;
	}, "请输入一个大于0的整数");
	
	$('#consume_form').validate({
		ignore: ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : false,
		rules : {
			price:{
				val_integer: true
			},
			name:{
				val_empty: true	
			},
			integral:{
				val_integer: true	
			},
			description:{
				val_empty: true	
			},
			cover:{
				val_empty:true
			},
			goodType:{
				select:true
			},
			sign:{
				select:true
			},
			userLimited:{
				val_integer_:true
			}
		},
		messages : {
			price:"请输入正确的价格",
			name:"请输入名称",
			integral:"请输入正确的积分",
			description:"请添加描述",
			cover:"请上传商品封面",
			goodType:"请选择商品类型",
			number:"请输入商品库存",
			sign:"请选择兑换该商品的用户类型"
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
			submitForm(form);
		}
	});
})
