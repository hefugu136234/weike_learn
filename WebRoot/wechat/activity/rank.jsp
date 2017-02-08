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
<title>最热排行榜</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/activity.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<body>
	<div class="page">
		<div class="activity-contanier" style="background-image:url('${vo_data.background}');">
			<a href="/api/webchat/activity/wonder/page" class="icons icon-logo page-absolute-logo"></a>

			<div class="activity-rank">
				<div class="icon-title bold clearfix">
					<h5 class="tt bfL"><span class="icon icon-fire"></span>最热排行榜</h5>
				</div>
				<c:choose>
					<c:when test="${not empty vo_data.ranks}">
						<div class="activity-rank-list">
							<c:forEach var="item" items="${vo_data.ranks}">
								<a href="/api/webchat/resource/first/view/${item.uuid}" class="items">
									<div class="box clearfix">
										<div class="num icon-cup"></div>
										<div class="info">
											<h5 class="tt">${item.name}</h5>
											<p class="desc">
												<span>编号：${item.code}</span><span>${item.catgoryName}</span>
											</p>
										</div>
									</div>
									<div class="user">
										<div class="photo">
											<img src="${item.speakerPhoto}">
										</div>
										<div class="name">${item.speakerName}</div>
										<!-- <div class="icon_vip"></div> -->
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
	</div>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
