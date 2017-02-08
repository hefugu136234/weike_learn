$(document).ready(function(){
  // 获取验证码
  $('#reg_code_btn').on('click', function(){
    var $that = $(this);
    var $phone = $('#user_phone');
    if ($phone.val() == ''){
      alert('请输入您的手机号码！');
      return false;
    } else if ($phone.val() != '' && !verify_phone.test($phone.val())){
      alert('请输入正确的手机号码！');
      $phone.val('');
      $phone.focus();
      return false;
    }

    $.post(
      '/f/web/reg/code/send',
      {
        'mobile': $phone.val()
      },
      function(data){
        if (isSuccess(data)){
          // 如果已关注公众账号，发送验证码消息：
        	 alert('验证码已经发送至您的手机，请注意查收!');
             countdownCode($that);
        } else {
          // 如果未关注，弹出消息框：
        	var message=data.message;
        	if('no subscribe'==message){
        		$('#attention_accounts_modal').modal('show');
                return false;
        	}else{
        		alert(message);
        	}

        }

      }
    );
  });

   jQuery.validator.addMethod("checkPhone", function(value, element) {
		return verify_phone.test(value);
	}, "请输入正确的手机号.");
	jQuery.validator.addMethod("select", function(value, element) {
		return $(element).prop('selectedIndex') != 0;
	}, "请选中一个有效选项.");

	jQuery.validator.addMethod("checkboxSelect", function(value, element) {
		return $(element).prop('checked');
	}, "请选中复选框");

	var validator = $('#reg_from').validate({
		ignore : ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : false,
		rules : {
			user_phone:{
				checkPhone:true
			},
	        user_pw_confirm:{
	        	equalTo : '#user_pw'
			},
			user_province:{
				select : true
			},
			city : {
				select : true
			},
			hosipital:{
				select : true
			},
			user_department:{
				select : true
			},
			checkbox_sel:{
				checkboxSelect:true
			}
		},
		messages : {
			user_phone : {
				required:"请输入手机号",
				checkPhone:"输入的手机号码不合法"
			},
			code : "请输入验证码",
			user_name:"请输入用户姓名",
			user_pw:"请输入密码",
			user_pw_confirm:{
				required: "请输入确认密码",
				equalTo: "输入的密码不一致，请确认"
			},
			user_province : "请选中省",
			city : "请选中市",
			hosipital : "请选中医院",
			user_department:"请选中医院",
			user_title :"请输入职称",
			checkbox_sel:"请同意协议"
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

function submitFrom(from){
	var submit_button=$('#submit_button');
	var mobile=$('#user_phone').val();
	var code=$('#code').val();
	var nickname=$('#user_name').val();
	var password=$('#user_pw').val();
	var city=$('#city').val();
	var hosipital=$('#hosipital').val();
	var department=$('#user_department').val();
	var professor=$('#user_title').val();
	var activeCode=$('#user_vip').val();

	submit_button.prop('disabled',true);

	$.post('/f/web/user/reg/save', {
		'mobile':mobile,
		'code':code,
		'nickname':nickname,
		'password':password,
		'city':city,
		'hosipital':hosipital,
		'department':department,
		'professor':professor,
		'activeCode':activeCode

	}, function(data) {
		submit_button.prop('disabled',false);
		if (isSuccess(data)) {
			location.href = '/f/web/logined/redirect/url';
		} else {
			if(data.status=='param invalid'){
				alert(data.message);
				location.href = '/f/web/logined/redirect/url';
			}else{
				alert(data.message);
			}
		}
	});
}
