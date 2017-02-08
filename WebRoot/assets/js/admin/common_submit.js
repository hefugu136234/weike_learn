/**
 * @author kalean.xiang
 * @version 1.0
 * 
 * required: // 必填 email: // 邮箱地址 url: // url地址 date: // 日期 dateISO: //
 * ISO格式的日期(2014/08/27 或 2014-08-27) number: // 数字(负数，正数，小数，整数) digits: // 正整数
 * creditcard: // 信用卡 minlength: // 输入字符最小长度(中文算一个字符) maxlength: //
 * 输入字符最大长度(中文算一个字符) rangelength: // 输入字符最小，最大长度(中文算一个字符) min: // 数值最小值 max: //
 * 数值最大值 range: // 数值最小，最大值 equalTo: // 再次输入相同的值 remote: //
 * 发送ajax请求验证(常用案例就是在注册时，验证用户名是否存在)， // 注：请求返回的 response === true || response
 * === 'true'，才算验证通过，这需要后端的配合
 * 
 */
+function($) {
	if (typeof $.validator != 'function') {
		throw new Error('jQuery validator required')
	}
	// $.validator.addMethod("emptyInvalid", function(value, element) {
	// return isBlank(value);
	// }, "请键入值");
}(jQuery);

function Param(k, v, msg) {

}

function formInit(form, handler) {
	form.validate({
		ignore : ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
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
			if (typeof handler == 'function') {
				handler(form)
			} else {
				form.submit();
			}
		}
	});
}
