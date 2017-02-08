<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="template language" name="keywords" />
<meta content="author" name="author" />
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"
	name="viewport" />
<meta content="yes" name="mobile-web-app-capable" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="telephone=no" name="format-detection" />
<meta content="email=no" name="format-detection" />
<meta content="black-translucent"
	name="apple-mobile-web-app-status-bar-style" />
<title>二维码</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<script type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>
<body>
	<div class="page">
		<div class="content error-page">
			<div class="p10">
				<div class="icon">
					<img src="/assets/img/web/mascot.png" width="30%">
				</div>
				<p id="result_info">${error}</p>
				<!-- <a href="javascript:wx.closeWindow();"
					class="button btn-full btn-blue btn-radius">关闭</a> -->
				<a href="javascript:wx.closeWindow();" class="button btn-full btn-cyan btn-radius mt32">关闭</a>
			</div>
		</div>
	</div>
</body>
</html>
