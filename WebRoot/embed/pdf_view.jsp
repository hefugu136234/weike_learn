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
<title>${requestScope.title}</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
</head>

<body>
	<div class="page">
		<div id="page" class="content">
			<c:if test="${not empty requestScope.taskId}">
				<c:forEach var="index" begin="1" end="${requestScope.pageNum}">
					<img src="/assets/img/grey.gif" width="100%"
						data-original="http://7xo6el.com2.z0.glb.qiniucdn.com/${requestScope.taskId}?odconv/jpg/page/${index}/density/150/quality/80/resize/800" />
				</c:forEach>
			</c:if>
		</div>
		<textarea class="hide" id="data_json">${requestScope.data_json}</textarea>
	</div>

	<script type="text/javascript" src="/assets/js/jquery.js"></script>
	<script type="text/javascript"
		src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript"
		src="/assets/js/wechat/common_res_share.js"></script>
	<script type="text/javascript"
		src="//cdn.bootcss.com/jquery.lazyload/1.9.1/jquery.lazyload.min.js"></script>
	<script type="text/javascript">
		var data_error = '${requestScope.error_data}';
		$(function() {
			if (!!data_error) {
				alert(data_error);
			} else {
				$('img').lazyload({
					placeholder : "/assets/img/grey.gif",
					effect : "fadeIn",
					container : $("#page")
				});

				var imgsObj = $('img');
				var imgs = new Array();
				for (var i = 0; i < imgsObj.size(); i++) {
					imgs.push(imgsObj.eq(i).attr('data-original'));
				}

				$('img').click(function(event) {
					event.preventDefault();
					WeixinJSBridge.invoke('imagePreview', {
						'current' : $(this).attr('src'),
						'urls' : imgs
					});
				});

				//分享资源使用
				var data = $('#data_json').text();
				if (!!data_json) {
					try {
						var data = JSON.parse(data_json);
						getResConfig(data);
					} catch (e) {
						console.log(e);
					}
				}
			}
		});
	</script>
</body>
</html>
