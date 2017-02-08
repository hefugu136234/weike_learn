<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <title>登录</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
  </head>

  <body>
    <div class="page form-bg">
      <div class="content">
        <div class="form-logo"><img src="/assets/img/app/logo.png"></div>
        <div class="form-title">请您登录</div>

        <form name="loginForm" id="login_form">
          <div class="default-form transparent p10 mt10">
            <input type="hidden" name=openId value="${requestScope.web_chat_openid}">
            <label class="form-group">
              <div class="form-label"><i class="icon icon-phone"></i></div>
              <div class="form-input">
                <input type="tel" id="mobile" name="username" class="form-control" placeholder="请输入手机号码">
              </div>
            </label>
            <label class="form-group">
              <div class="form-label"><i class="icon icon-lock"></i></div>
              <div class="form-input">
                <input type="password" name="password" id="password" class="form-control" placeholder="请输入账户密码">
              </div>
            </label>

            <div class="item-link">
              <a href="/api/webchat/forget/password">忘记密码?</a>
            </div>

            <div class="btn-area mt10 clearfix">
              <a href="/api/webchat/register" class="button btn-blur btn-radius">
                <span class="icon icon-plus"></span>
                注册新用户
              </a>
              <button type="submit" class="button btn-cyan btn-radius">
                <span class="icon icon-next"></span>
                登 录
              </button>
            </div>

            <div class="default-form-tips mt10">
              <p>感谢您的关注！知了微课平台是移动专业医学教育平台，提供您需要的医学专业资讯。</p>
              <p>根据有关规定，医学专业资讯仅供医学专业人士参阅。</p>
              <p>如您尚未注册，请您先点击“新用户注册”，并填写真实信息。</p>
            </div>
          </div>
        </form>
      </div>
    </div>

    <script type="text/javascript">
    $(document).ready(function(){
      $('#login_form').submit(function(event){
      	event.preventDefault();
      	var $form = $(this);
        var regPhone = /^(0|86|17951)?(13[0-9]|15[012356789]|17[4678]|18[0-9]|14[57])[0-9]{8}$/; //验证手机号码
        var mobileInput = $('#mobile');

        if (mobileInput.val() == ''){
          AlertClassTip('请填写您的手机号码！');
          return false;
        }
        else if (mobileInput.val() != '' && !regPhone.test(mobileInput.val())){
          AlertClassTip('请填写正确的手机号码！');
          mobileInput.val('');
          mobileInput.focus();
          return false;
        }
        if ($('#password').val() == ''){
          AlertClassTip('请填写您的密码！');
          return false;
        }

        $.post('/api/webchat/login',$form.serialize(),function(data){
        	if(data.status=='success'){
        		window.location.href = '/api/webchat/logined/page';
        	}else{
        		AlertClassTip(data.message);
        	}
        });

      });
    });
    </script>
  </body>
</html>
