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
<title>${res_vo.name}</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/resource.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript"
	src="http://qzonestyle.gtimg.cn/open/qcloud/video/h5/h5connect.js"></script>
<script type="text/javascript"
	src="/assets/js/wechat/resource/video_detail.js"></script>
</head>

<!-- 新页面 原 resource_preview.jsp-->
<body>
	<div class="page">
		<header class="top-bar with-status">
			<div class="crumb-nav">
				<a href="/api/webchat/index" class="logo icon-logo"></a>
				<%-- <a href="javascript:window.location.reload();">${res_vo.name}</a> --%>
				${res_vo.name}
			</div>
			<div class="top-status play-times">
				<p>当前播放</p>
				<p class="num">${res_vo.viewCount}</p>
			</div>
		</header>

		<c:if test="${res_vo.type eq 'VIDEO'}">
			<div id="id_video_container"></div>
			<div id="resource_pre" class="resource-preview hide">
				<img src="${res_vo.cover}" alt="">
				<div class="cover"></div>
				<div class="play-btn icon-play" id="vr_play_btn"></div>
			</div>
		</c:if>

    <c:if test="${res_vo.type eq 'VR'}">
      <!-- vr播放的前置页面 -->
			<div class="resource-preview">
				<img src="${res_vo.cover}" alt="">
				<div class="cover"></div>
				<div class="play-btn icon-play" id="vr_play_btn"></div>
			</div>
		</c:if>

		<!-- 收藏点赞分享公共部分 -->
		<jsp:include page="/wechat/resource/resource_common_part.jsp"></jsp:include>

		<div class="resource-intro">
			<div class="icon-title mb-1 white bold clearfix">
				<h5 class="tt bfL">
					<span class="icon icon-detail"></span>简 介
				</h5>
			</div>

			<div class="intro">
				<div class="clearfix">
					<div class="col-2">
						<span class="tt">讲者：</span>${res_vo.speakerName}
					</div>
					<div class="col-2">
						<span class="tt">时长：</span>${res_vo.video.videoTime}
					</div>
				</div>
				<p>
					<span class="tt">名称：</span>${res_vo.name}</p>
				<p>
					<span class="tt">日期：</span>${res_vo.dateTime}</p>
				<p>
					<span class="tt">简介：</span>${res_vo.desc}</p>
				<p>
					<span class="tt">医院：</span>${res_vo.hospitalName}</p>
			</div>
		</div>

		<jsp:include page="/wechat/resource/resource_vote.jsp"></jsp:include>

		<jsp:include page="/wechat/resource/resource_comment.jsp"></jsp:include>
	</div>

	<input type="hidden" id="page_reamin_uuid" value="${page_reamin_uuid}" />
	<input type="hidden" id="resource_uuid" value="${res_vo.uuid}" />
	<input type="hidden" id="resource_bloody" value="${res_vo.bloody?1:0}" />
	<input type="hidden" id="resource_type" value="${res_vo.type}" />
	<input type="hidden" id="resource_fileId"
		value="${res_vo.video.fileId}" />
		<input type="hidden" id="res_auth_view" value="${res_auth_view}" />
		<input type="hidden" id="res_auth_view_val" value="${res_auth_view_val}" />
		<input type="hidden" id="res_auth_view_login" value="${res_auth_view_login}" />
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
