<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="template language" name="keywords" />
    <meta content="author" name="author" />
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" name="viewport" />
    <meta content="yes" name="mobile-web-app-capable" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
    <title>忘记密码</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
  </head>

  <body>
    <div class="page">
      <div class="content">
        <form name="forgetForm" id="forgetForm">
          <div id="step1">
            <div class="default-form">
              <div class="form-group with-btn">
                <div class="form-input">
                  <input type="tel" id="mobile" name="mobile" class="form-control" placeholder="请输入手机号码">
                  <button type="button" name="code_btn" class="button btn-green" id="valid_code_but">获取验证码</button>
                </div>
              </div>
              <label class="form-group">
                <div class="form-input">
                  <input type="text" name="valid_code" id="valid_code" class="form-control" placeholder="请输入验证码">
                </div>
              </label>
            </div>
            <div class="p10">
              <button type="button" id="next_step1" class="button btn-full btn-cyan btn-radius">下一步</button>
            </div>
          </div>

          <div id="step2" class="hide">
            <div class="default-form">
              <input type="hidden" id="hidden_vaild_code" value=""/>
              <label class="form-group">
                <div class="form-label">重置密码：</div>
                <div class="form-input">
                  <input type="password" name="password" id="password" class="form-control" placeholder="密码不得为空" maxlength="20"/>
                </div>
              </label>
              <label class="form-group">
                <div class="form-label">确认密码：</div>
                <div class="form-input">
                  <input type="password" name="re_password" id="re_password" class="form-control" placeholder="请再次输入密码" maxlength="20"/>
                </div>
              </label>
            </div>
            <div class="p10">
              <button type="submit" id="next_step2" class="button btn-full btn-cyan btn-radius">提交</button>
            </div>
          </div>
        </form>
      </div>
    </div>
    <div id="hexagon"></div>
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript">

    $(document).ready(function(){
      // 用户获取手机验证码
      //获取验证码按钮
      var validCodeBut = $('#valid_code_but');
      //验证码
      var valid_code = $('#valid_code');
      var mobileInput = $('#mobile');
      var nextStep = $('#next_step1');
      var countWait = 60;

      validCodeBut.click(function(event){
        event.preventDefault();
        var objThis = $(this);
        var regPhone = /^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/; //验证手机号码

        if (mobileInput.val() == ''){
          alert('请填写您的手机号码！');
          return false;
        }
        else if (mobileInput.val() != '' && !regPhone.test(mobileInput.val())){
          alert('请填写正确的手机号码！');
          mobileInput.val('');
          mobileInput.focus();
          return false;
        }

      $.post('/api/webchat/forget/code/send', { 'mobile': mobileInput.val() }, function(data){
          if(data.status=='success'){
            countTime(objThis);
            alert('验证码已经发送成功');
          }else{
        	  if(!!data.message){
        		  alert(data.message)
        	  }else{
            	alert("验证码发送失败，请稍后再试");
        	  }
          }
        });
      });


      nextStep.on('click', function(event){
        event.preventDefault();
        $.post(
          '/api/webchat/froget/code/valid',
          {
            'code': valid_code.val(),
            'mobile': mobileInput.val()
          },
        	function(data){
          	if(data.status=='success'){
              $('#step1').hide();
              $('#step2').show();
              $('#hidden_vaild_code').val(data.valid_code);
          	}
            else{
            	 if(!!data.message){
           		  alert(data.message)
           	  }else{
               	alert("验证码错误");
           	  }
          	}
          }
        );
      });

      function countTime(obj) {
    	  var my_countTime = window.setInterval(function(){
    		  $(obj).attr("disabled",true);
 	          $(obj).addClass("disabled");
    		  $(obj).html(countWait + "秒后重新获取");
    		  countWait--;
    		  if (countWait == 0) {
	   	            $(obj).attr("disabled",false);
	   	            $(obj).removeClass("disabled");
	   	            $(obj).html("获取验证码");
	   	            window.clearInterval(my_countTime);
	   	            countWait = 60;
   	          }
    	  },1000);
      }

      $('#forgetForm').submit(function(event){
    	  event.preventDefault();
    	  var password=$('#password').val();
    	  var re_password=$('#re_password').val();
    	  var valid_code=$('#hidden_vaild_code').val();

        if (password == ''){
          alert('请设置您的密码！');
          return false;
        }
        if (re_password == ''){
          alert('请再次填写密码！');
          return false;
        }
        else if (password != re_password){
          alert('两次填写的密码不一致！');
          return false;
        }
        $.post('/api/webchat/forget/init/psw',
        	{
        	'valid_code':valid_code,
        	'psw':password
        	},
        	function(data){
        	if(data.status=='success'){
        		window.location.href = '/api/webchat/qr/auth/changeUser';
        	}else{
        		if(!!data.message){
           		  alert(data.message);
           	  }else{
               	alert("密码重置失败");
           	  }
        	}
        });

      });
    });

    function clearNoNum(obj){
      obj.value = obj.value.replace(/[^\d]/g,""); //先把非数字的都替换掉，除了数字
    }
    </script>
  </body>
</html>
