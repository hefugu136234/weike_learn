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
    <title>修改密码</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
  </head>

  <body>
    <div class="page">
      <div class="content">
        <form id="changeForm">
          <div class="default-form">
            <label class="form-group">
              <div class="form-label">原始密码：</div>
              <div class="form-input">
                <input type="password" name="old_password" id="old_password" class="form-control" placeholder="密码不得为空" maxlength="20"/>
              </div>
            </label>
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
            <button type="submit" class="button btn-full btn-cyan btn-radius">提 交</button>
          </div>
        </form>
      </div>
    </div>
    <div id="hexagon"></div>
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/region.js"></script>
    <script type="text/javascript">
    $(document).ready(function(){

      $('#changeForm').submit(function(event){
    	  event.preventDefault();
    	  var old_password=$('#old_password').val();
    	  var password=$('#password').val();
    	  var re_password=$('#re_password').val();

        if (old_password == ''){
          alert('请填写原始密码！');
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

        $.post('/api/webchat/change/psw',
        	{
        	'oldPsw':old_password,
        	'psw':password
        	},
        	function(data){
        	if(data.status=='success'){
        		window.location.href = '/api/webchat/my/center';
        	}else{
        		if(!!data.message){
           		  alert(data.message);
           	  }else{
               	alert("密码修改失败");
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
