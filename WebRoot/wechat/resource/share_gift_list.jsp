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
<title>邀请好友</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/dropload.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript"
	src="/assets/js/wechat/resource/common_news_list.js"></script>
</head>

<body>
	<div class="page">
		<!-- <header class="top-bar">
      <div class="crumb-nav">
        <a href="/api/webchat/my/center" class="logo icon-logo"></a>
        邀请好友
      </div>
    </header> -->

		<c:choose>
			<c:when test="${vo_data.resCount gt 0}">
				<div id="dropload_resource_div">
					<div id="dropload_resource_ul" class="list-with-img with-arr p10"></div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="empty-tips-area show">
					<p>
						<img src="/assets/img/app/icon_empty.png" width="18%">
					</p>
					<p>内容暂无</p>
					<p>去其他栏目看看吧</p>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	<input type="hidden" id="category_uuid" value="${vo_data.uuid}" />
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
