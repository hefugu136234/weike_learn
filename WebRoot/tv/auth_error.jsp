<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<title>授权结果</title>
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
				<a href="javascript:;" id="go_index" class="button btn-full btn-blue btn-radius mt32 hide">进入首页</a>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="/assets/js/jquery.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			var result_info = $('#result_info');
			if (result_info.html() == '授权成功'){
				$('#go_index').css('display', 'block');
			}

			$('#go_index').click(function(){
	    	$.post('/api/webchat/tv/auth/go/vip',function(data){
	    		if(data.status=='success'){
	    			 location.href = "/api/webchat/index";
	    		}else{
	    			alert("用户不存在，请重新扫码");
	    		}
	    	});
	    });
		});
	</script>
</body>
</html>
