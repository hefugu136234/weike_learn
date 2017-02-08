<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
#valid_controller {
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
.loginscreen.middle-box {
	top: 0;
	height: 100%;
	margin-top: 0;
}

#login_form {
	position: absolute;
	top: 30%;
	left: 50%;
	width: 300px;
	height: 180px;
	margin: -90px 0 0 -150px;
}

.footCopyright {
	position: absolute;
	bottom: 24px;
	left: 0;
	width: 100%;
	height: 32px;
	line-height: 32px;
	text-align: center;
}
</style>
</head>
<body class="gray-bg">
	<div class="middle-box text-center loginscreen  animated fadeInDown">
		<div style="margin-top: 240px;">
			<img id="qr_container" alt="" style="width: 350px" height="350px"
				src="">
			<span id="tips"></span>
		</div>
	
	</div>
		<p class="footCopyright">
			<small>Lankr &copy; 2015</small>
		</p>
	<!-- Mainly scripts -->
	<script src="/assets/js/bootstrap.min.js"></script>
</body>

<script type="text/javascript">
	$(function() {
		$.get(
				'/api/tv/login/qr/product?device=asdfasdfasdasdfd&t='
						+ new Date().getTime()).always(function(data) {
			if (data.status == 'success') {
				$('#qr_container').attr('src', data.qrUrl);
				if(data.message){
					showTips(data.message)
				}
				c(data.uuid);
			}
		})

		function c(uuid) {
			$.post(
					'/api/tv/login/qr/connect?device=asdfasdfasdasdfd&t='
							+ new Date().getTime() + "&uuid=" + uuid).always(
					function(data) {
						if(data.message){
							showTips(data.message)
						}
						if (data.code == 200 || data.code == 201) {
							setTimeout(function() {
								c(uuid);
							}, 1000);
						} else {
							console.log(data.message);
						}
					})
		}
	})
	function showTips(msg){
		$('#tips').empty();
		$('#tips').append(msg)
	}
</script>
</html>
