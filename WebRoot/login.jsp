<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
#valid_controller{
text-align: left;
display: none;
}

</style>
<title>USER | LOGIN</title>
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<link href="/assets/css/bootstrap.min.css" rel="stylesheet">
<link href="/assets/font-awesome/css/font-awesome.css" rel="stylesheet">

<link href="/assets/css/loading/style.css" rel="stylesheet">

<link href="/assets/css/animate.css" rel="stylesheet">
<link href="/assets/css/style.css" rel="stylesheet">
<style type="text/css">
.loginscreen.middle-box { top:0; height: 100%; margin-top:0;}
#login_form { position: absolute; top: 30%; left: 50%; width:300px; height: 180px; margin:-90px 0 0 -150px;}
.footCopyright { position: absolute; bottom: 24px; left: 0; width:100%; height: 32px; line-height:32px; text-align: center;}
</style>
</head>
<body class="gray-bg">
	<div class="middle-box text-center loginscreen  animated fadeInDown">
		<form id="login_form" class="m-t" role="form" action=""
			method="post">
			<div class="form-group">
				<input type="text" class="form-control" placeholder="用户名"
					name="username" required="" id="username">
			</div>
			<div class="form-group">
				<input type="password" class="form-control" placeholder="密 码"
					name="password" required="" id="password">
			</div>
			<div class="form-group">
			<div id="valid_controller">
				<input type="text" id="validateCode" class="form-control" style="width: 120px;display: inline-block;" placeholder="验证码"
					name="validateCode">
					<img id="vcode" alt="" src="" style="display: inline-block;cursor: pointer;">							
					</div>
			</div>
			<div><button type="button" id="login_sub"
				class="btn btn-primary block full-width m-b">登 录</button></div>
			<!-- loading -->
			<div id="loading" class="loader hide mt30">
				<div class="dot"></div>
				<div class="dot"></div>
				<div class="dot"></div>
				<div class="dot"></div>
				<div class="dot"></div>
			</div>
		</form>
		<p class="footCopyright">
			<small>Lankr &copy; 2015</small>
		</p>
	</div>
	<!-- Mainly scripts -->
	<script src="/assets/js/bootstrap.min.js"></script>
</body>

<script type="text/javascript">
	$(document).ready(function(){
		var need_validcode = ${requestScope.need_validcode}
		if(need_validcode){
			showValidCode();
		}
		$('#login_sub').click(function(e) {
			var username = $('#username').val()
			var password = $('#password').val()
			if(username == ''){
				alert('请输入用户名')
				return;
			}
			if(password == '' || password.length < 6){
				alert('密码至少为6位字符')
				return;
			}
			if($('#valid_controller').is(':visible')){
				var validateCode = $('#validateCode').val();
				if(validateCode == ''){
					alert('请输入正确的验证码')
					return;
				}
			}
			
			$('#login_sub').addClass('hide');
			$('#loading').removeClass('hide');
			
			$.post('/admin/login', $('#login_form').serialize()).always(function(data) {
				if(data.status == "success"){
					location.href = '/user/project/multipart';
				}else{
					$('#login_sub').removeClass('hide');
					$('#loading').addClass('hide');
					alert(data.message)
					if(data.code == 3){
						showValidCode();
					}
				}
			})
		})
		
		function showValidCode(){
			$('#valid_controller').show();
			var img = $('#vcode');
			freshCode(img);
			img.unbind('click')
			img.click(function(){
				freshCode(img);
			})
		}
		
		function freshCode(img){
			img.attr('src','/user/1/validate/code?timestamp' + new Date().getTime())
		}
	})
</script>
</html>
