<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
<script type="text/javascript"
	src="/assets/js/wechat/broadcast/live_first.js"></script>
</head>

<body>
	<div class="blur-background" id="broadcast_bg"
		style="background-image:url('${vo_data.cover}')"></div>
	<div class="broadcast-cover" id="broadcast_guide">
		<div class="free-tag">
			<img src="/assets/img/app/live/tag_free.png" width="100%">
		</div>

		<div class="broadcast-summary">
			<h5 class="tt">${vo_data.name}</h5>
			<p>${vo_data.date}${vo_data.dateWeek}</p>
			<p>${vo_data.sTime}-${vo_data.eTime}</p>
		</div>

		<c:if test="${vo_data.buttonType == 1}">
			<div class="broadcast-status-tt">报名未开始</div>
			<div class="broadcast-enter-btn begin" id="enter_live"></div>
		</c:if>
		<c:if test="${vo_data.buttonType == 2}">
			<div class="broadcast-status-tt">报名开放中</div>
			<div class="broadcast-enter-btn active" id="enter_live"></div>
		</c:if>
		<c:if test="${vo_data.buttonType == 3}">
			<div class="broadcast-status-tt">报名已结束</div>
			<div class="broadcast-enter-btn ing" id="enter_live"></div>
		</c:if>
		<c:if test="${vo_data.buttonType == 4}">
			<div class="broadcast-status-tt">直播进行中</div>
			<div class="broadcast-enter-btn ing" id="enter_live"></div>
		</c:if>
		<c:if test="${vo_data.buttonType == 5}">
			<div class="broadcast-status-tt">直播已结束</div>
			<!-- 无录播 -->
			<div class="broadcast-enter-btn end" id="enter_live"></div>
		</c:if>
		<c:if test="${vo_data.buttonType == 6}">
			<div class="broadcast-status-tt">直播已结束</div>
			<!-- 有录播 -->
			<div class="broadcast-enter-btn recorded" id="enter_live"></div>
		</c:if>

		<div class="broadcast-status">
			<div class="users">
				<span class="icon icon-users"></span>已参加：<span class="num">${vo_data.bookNum}</span>
				<span class="group"> <c:choose>
						<c:when test="${vo_data.castLimit == 0}">/无限制</c:when>
						<c:otherwise>/${vo_data.castLimit}人</c:otherwise>
					</c:choose>
				</span>
			</div>
			<c:if test="${vo_data.countDownStart != 0}">
				<div class="countdown">
					<span class="icon icon-countdown"></span> <span
						class="num day_show"></span> 天 <span class="num hour_show"></span>
					小时 <span class="num minute_show"></span> 分钟 <span
						class="num second_show"></span> 秒
				</div>
			</c:if>

			<div class="broadcast-intro">
				<c:choose>
					<c:when test="${fn:length(vo_data.desc)>60}">${fn:substring(vo_data.desc,0,60)}...</c:when>
					<c:otherwise>${vo_data.desc}</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
	
	
	<input type="hidden" id="uuid" value="${vo_data.uuid}" />
	<input type="hidden" id="isPinCode" value="${vo_data.needPinCode}" />
	<input type="hidden" id="countDownStart"
		value="${vo_data.countDownStart}" />
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
