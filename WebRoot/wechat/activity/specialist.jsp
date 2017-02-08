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
<meta name="full-screen" content="yes">
<meta name="x5-fullscreen" content="true">
<meta content="black-translucent"
	name="apple-mobile-web-app-status-bar-style" />
<title>${vo_data.name}</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/activity.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<body>
	<div class="page">
		<header class="top-bar transparent absolute">
      <div class="crumb-nav">
        <a href="/api/webchat/activity/wonder/page" class="logo icon-logo"></a>
        ${vo_data.name}
      </div>
    </header>

		<div class="specialist-container" style="background-image:url('${vo_data.wxbg}');">
			<div class="specialist-info-tag">
				<div class="photo">
					<img src="${vo_data.cover}">
				</div>
				<div class="name">${vo_data.name}</div>
				<div class="hospital">${vo_data.joinProfessor}</div>
				<div class="intro">${vo_data.desc}</div>
				<!-- <div class="code">
          <div class="icon icon-code"></div>
          <div class="tt">我的二维码</div>
        </div> -->
			</div>

			<c:choose>
				<c:when test="${not empty vo_data.items}">
					<div class="specialist-works-list">
						<c:forEach var="item" items="${vo_data.items}">
							<a href="/api/webchat/resource/first/view/${item.uuid}" class="specialist-card"> <img
								src="${item.cover}">
								<div class="intro-tag">
									<h5 class="tt">${item.name}</h5>
									<div class="clearfix">
										<div class="date bfL">${item.dateTime}</div>
										<div class="status bfR">
											<span class="icon icon-eye"></span>${item.viewCount}<span
												class="icon icon-star-line"></span>${item.collectCount}<span
												class="icon icon-like"></span>${item.praiseCount}<!--  <span
												class="icon icon-share"></span> -->
										</div>
									</div>
								</div>
							</a>
						</c:forEach>
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
	</div>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
