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
    <title>注册</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/reg.css" rel="stylesheet">
  </head>

  <body>
    <div class="page">
      <div class="content">
        <form name="regForm" id="regForm">
          <div id="step1">
            <div class="default-form">
              <label class="form-group with-btn">
                <div class="form-input">
                  <input type="tel" id="mobile" name="mobile" class="form-control" placeholder="请输入手机号码">
                  <button type="button" name="code_btn" class="button btn-green" id="valid_code_but">获取验证码</button>
                </div>
              </label>
              <label class="form-group">
                <div class="form-input">
                  <input type="text" name="valid_code" id="valid_code" class="form-control" placeholder="请输入验证码" required>
                </div>
              </label>
            </div>
            <div class="p10">
              <button type="button" id="next_step1" class="button btn-full btn-cyan btn-radius">下一步</button>
            </div>
          </div>

          <div id="step2" class="hide">
            <div class="reg-top-tag clearfix">
              <div class="bfL">注册 > 企业通道</div>
            </div>

            <div class="default-form">
              <label class="form-group">
                <div class="form-label">用户姓名：</div>
                <div class="form-input">
                  <input type="text" name="nickname" id="nickname" class="form-control" placeholder="用户姓名不得为空" maxlength="25"/>
                </div>
              </label>
              <label class="form-group">
                <div class="form-label">账户密码：</div>
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

            <div id="companyForm">
              <div class="default-form mt10">
               <label class="form-group form-select">
                  <div class="form-label">企业名称：</div>
                  <div class="form-input">
                    <select id="company" name="company" class="form-control">
        							<option value="">请选择</option>
        							 <c:forEach var="item" items="${requestScope.manufacturerVo}">
        								<option value="${item.id}">${item.text}</option>
        							</c:forEach>
        					  </select>
                  </div>
                </label>
                <label class="form-group">
                  <div class="form-label">职位名称：</div>
                  <div class="form-input">
                    <input type="text" name="professor_com" id="professor_com" class="form-control" maxlength="25"/>
                  </div>
                </label>
              </div>
             <!-- <div class="default-form mt10">
                <label class="form-group">
                  <div class="form-label">VIP 激 活：</div>
                  <div class="form-input">
                    <input type="text" name="card_code_com" id="card_code_com" class="form-control" placeholder="非必填，填写后即为VIP用户">
                  </div>
                </label>
              </div>  -->
              <div class="default-form mt10">
                <div class="form-group protocol">
                  <input type="checkbox" checked="checked" name="protocol" id="protocol_com" required>
                  <a href="javascript:void(0);" class="link">用户协议</a>
                </div>
              </div>
              <div class="p10">
                <button type="button" id="sub_Company" class="button btn-full btn-cyan btn-radius">提交申请</button>
              </div>
            </div>
          </div>

          <input type="hidden" id="hidden_vaild_code" value=""/>
          <input type="hidden" id="token" name="token" value="${requestScope.token}"/>
        </form>
      </div>
    </div>
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/region.js"></script>
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
          var regPhone = /^(0|86|17951)?(13[0-9]|15[012356789]|17[4678]|18[0-9]|14[57])[0-9]{8}$/; //验证手机号码

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


        $.post('/api/webchat/register/code/send', { 'mobile': mobileInput.val() }, function(data){
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
          if(mobileInput.val()==''){
          	alert('请填写手机号码');
          	return false;
          }
          if(valid_code.val()==''){
          	alert('请填写验证码');
          	return false;
          }
          $.post(
            '/api/webchat/register/code/valid',
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

      //企业用户
      $('#sub_Company').click(function(event){
    	  event.preventDefault();
    	  var nickname=$('#nickname').val();
    	  var password=$('#password').val();
    	  var re_password=$('#re_password').val();
    	  var valid_code=$('#hidden_vaild_code').val();
    	  var activeCode='';//$('#card_code_com').val();
    	  var professor=$('#professor_com').val();
    	  var company=$('#company').val();
          var token=$('#token').val();
          if (nickname == ''){
              alert('请填写您的姓名！');
              return false;
            }
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
            if (company == ''){
              alert('请选择您所在的公司');
              return false;
            }
            var protocol_check = $('#protocol_com').is(':checked');
            if (protocol_check == false){
              alert('请选择用户协议！');
              return false;
            }
            $.post('/api/webchat/company/reg/save',
                 	{
                 	'valid_code':valid_code,
                 	'password':password,
                 	'nickname':nickname,
                 	'token':token,
                 	'professor':professor,
                 	'activeCode':activeCode,
                 	'company':company
                 	},
                 	function(data){
                 	if(data.status=='success'){
                 		if(!!activeCode){
                 			//2=企业
                 			localpage(2);
                 		}else{
                 			localpage(3);
                 		}
                 	}else if(data.status=='error'){
                      	alert(data.message);
                      	//绑定失败，跳转登录页面
                      	window.location.href = '/api/webchat/page/login';
                 	}else if(data.status='param invalid'){
                 		alert('用户注册成功，云卡激活失败，请到个人中心重新激活');
                 		window.location.href = '/api/webchat/my/center';
                 	}else{
                 		if(!!data.message){
                    		  alert(data.message);
                    	  }else{
                        	alert("注册失败");
                    	  }
                 	}
                 });
      });


    });

    function localpage(num){
    	window.location.href = '/api/webchat/reg/success/'+num;
    }

    </script>
  </body>
</html>
