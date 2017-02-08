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
<link href="http://cdn.bootcss.com/Swiper/3.1.7/css/swiper.min.css"
	rel="stylesheet">
<link href="/assets/css/app/dropload.css" rel="stylesheet">
<link href="/assets/css/app/activity.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript"
	src="http://cdn.bootcss.com/Swiper/3.1.7/js/swiper.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript"
	src="/assets/js/wechat/activity/activiy_detail.js"></script>
</head>

<body>
	<div class="page">
		<div class="activity-contanier" style="background-image:url('${vo_data.background}');">
			<a href="/api/webchat/activity/wonder/page" class="icons icon-logo page-absolute-logo"></a>

			<div class="activity-intro-tag mb16 limit-str-text" data-minstr="48">${vo_data.description}</div>

			<c:if test="${not empty vo_data.expertList}">
				<div class="swiper-container default-group-swiper no-radius preview-2-3 specialist-list">
					<ul class="swiper-wrapper">
						<c:forEach var="item" items="${vo_data.expertList}">
							<li class="swiper-slide">
								<a href="/api/webchat/activity/expert/detail/${item.uuid}">
									<img src="${item.cover}">
									<div class="info">
										<h5 class="name">${item.name}</h5>
										<p class="desc">${item.professor}</p>
									</div>
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:if>

			<c:if test="${not empty vo_data.recommends}">
				<div class="icon-title bold mt8 clearfix">
					<h5 class="tt bfL"><span class="icon icon-hot"></span>热门推荐</h5>
				</div>
				<div class="swiper-container default-group-swiper no-radius preview-4-3">
					<ul class="swiper-wrapper">
						<c:forEach var="item" items="${vo_data.recommends}">
							<c:if test="${item.type ne 'NEWS'}">
								<li class="swiper-slide">
									<a href="/api/webchat/resource/first/view/${item.uuid}">
										<img src="${item.cover}">
										<div class="tags">${item.name}</div>
										<c:if test="${item.type eq 'VIDEO'}">
											<div class="img-tag tr video-type">视频</div>
										</c:if>
										<c:if test="${item.type eq 'PDF'}">
											<div class="img-tag tr pdf-type">PDF</div>
										</c:if>
										<c:if test="${item.type eq 'THREESCREEN'}">
											<div class="img-tag tr ppt-type">课件</div>
										</c:if>
										<c:if test="${item.type eq 'VR'}">
											<div class="img-tag tr vr-type">VR</div>
										</c:if>
										<c:if test="${item.bloody}">
											<div class="bloody-icon">
												<img alt="" src="/assets/img/app/icon_bloody.png">
											</div>
										</c:if>
									</a>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</c:if>

			<div id="activity_wrapper">
				<div id="activity_oups_list" class="list-with-img with-arr p10"></div>
			</div>

			<div class="activity-operation-btns">
				<a href="/api/webchat/activity/res/ranking/${vo_data.uuid}" class="btn rank">
				<span class="icon icon-statistics"></span>人气</a>
				<c:if test="${requestScope.vo_data.collected}">
					<a href="javascript:void(0);" class="btn sub" onclick="isneedName();"><span class="icon icon-cloud-up"></span>提交作品</a>
				</c:if>
			</div>
		</div>
	</div>
  <!-- 登录模块 -->
  <%-- <jsp:include page="/wechat/common/login_part.jsp"></jsp:include> --%>

	<input type="hidden" id="activity_uuid" value="${vo_data.uuid}" />
	<input type="hidden" id="activity_description" value="${vo_data.description}" />
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
