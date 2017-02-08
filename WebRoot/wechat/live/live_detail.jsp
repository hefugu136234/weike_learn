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
<title>${vo_data.name}</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/broadcast.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<body>
	<div class="broadcast-page"
		style="background-image:url('${vo_data.bg}')">
		<div class="title">直播地址</div>
		<div class="broadcast-link">
			<a id="plat_redirect" href="${vo_data.liveUrl}" class="btn"><span
				class="icon icon-play2"></span>开始直播</a>
		</div>
		<div class="line-tag clearfix">
			<h5 class="tt">简 介</h5>
		</div>
		<div class="broadcast-desc">${vo_data.description}</div>
	</div>

	<input type="hidden" id="uuid" value="${vo_data.uuid}" />
	<script type="text/javascript">
	var url="${vo_data.liveUrl}";
		$(function() {
			var uuid = $('#uuid').val();
			$('#plat_redirect').click(
					function() {
						$.post('/api/webchat/broadcast/redirect/thrid/record'
								{
							uuid:uuid,
							url:url
								}, function(data) {
							console.log(data)
						});
					});
		});
	</script>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
