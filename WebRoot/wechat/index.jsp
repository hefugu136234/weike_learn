<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta content="template language" name="keywords" />
		<meta content="author" name="author" />
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" name="viewport" />
		<meta content="yes" name="mobile-web-app-capable" />
		<meta content="yes" name="apple-mobile-web-app-capable" />
		<meta content="telephone=no" name="format-detection" />
		<meta content="email=no" name="format-detection" />
		<meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
		<title>知了微课 知我所学，了我所需</title>

		<link rel="stylesheet" href="http://cdn.bootcss.com/Swiper/3.1.7/css/swiper.min.css">
		<link rel="stylesheet" href="/assets/css/app/font.css">
		<link rel="stylesheet" href="/assets/css/app/default.css">
		<link rel="stylesheet" href="/assets/css/app/home.css">

		<script type="text/javascript" src="/assets/js/jquery.js"></script>
		<script type="text/javascript" src="http://cdn.bootcss.com/Swiper/3.1.7/js/swiper.min.js"></script>
		<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
		<script type="text/javascript" src="/assets/js/wechat/index.js"></script>
	</head>

	<body>
		<div class="page">
			<div class="content pb50">
				<!-- banner -->
				<c:if test="${not empty vo_data.bannerList}">
					<div class="swiper-container default-swiper">
						<ul class="swiper-wrapper">
							<c:forEach var="item" items="${vo_data.bannerList}">
								<li class="swiper-slide">
									<a href="${not empty item.url?item.url:'javascript:void(0);'}">
										<img src="${item.cover}">
									</a>
								</li>
							</c:forEach>
						</ul>
						<div class="swiper-pagination"></div>
					</div>
				</c:if>
				<!-- banner -->

				<a href="/api/webchat/search/page" class="search-link-icon icon-search"></a>

				<!-- 活动 -->
				<c:if test="${not empty vo_data.activityList}">
					<div class="swiper-container default-group-swiper preview-3-2">
						<ul class="swiper-wrapper">
							<c:forEach var="item" items="${vo_data.activityList}">
								<li class="swiper-slide">
									<a href="/api/webchat/activity/total/page/${item.uuid}">
										<img src="${item.cover}">
									</a>
								</li>
							</c:forEach>
						</ul>
					</div>
				</c:if>
				<!-- 活动 -->

				<!-- 学科 -->
				<c:if test="${not empty vo_data.menuList}">
					<div class="grid-icon-group category-plate">
						<ul class="list clearfix">
							<c:forEach var="item" items="${vo_data.menuList}">
								<li>
									<a href="${item.url}" class="btn">
										<div data-name="${item.name}" class="icon"></div>
										<h5 class="tt">${item.name}</h5>
									</a>
								</li>
							</c:forEach>
							<c:if test="${fn:length(vo_data.menuList) lt 8}">
								<li>
									<a href="/api/webchat/first/level/subject" class="btn">
										<div class="icon">
											<img src="/assets/img/app/home/category_icon_more.png">
										</div>
										<h5 class="tt">更多分类</h5>
									</a>
								</li>
							</c:if>
						</ul>
					</div>
				</c:if>
				<!-- 学科 -->

				<!-- 最新视频 -->
				<c:if test="${not empty vo_data.items}">
					<div class="icon-title mt8 clearfix">
						<h5 class="tt bfL">
							<span class="icon icon-broadcast"></span>最新视频
						</h5>
					</div>

					<div class="list-with-img with-arr p10">
    				<c:forEach var="item" items="${vo_data.items}">
    					<c:if test="${item.type ne 'NEWS'}">
		    				<a href="/api/webchat/resource/first/view/${item.uuid}" class="box">
		    					<div class="img">
		    						<img src="${item.cover}">
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
										<div class="img-tag br view-num">
											<span class="icon icon-eye"></span>${item.viewCount}
										</div>
		    					</div>
		    					<div class="info">
		    						<h5 class="tt limit-str-text" data-minstr="34">${item.name}</h5>
		    						<div class="desc">
											<p><span>${item.catgoryName}</span></p>
											<p><span>${item.speakerName}</span> | <span>${item.hospitalName}</span></p>
										</div>
		    					</div>
		    					<div class="icon icon-arr-r"></div>
		    				</a>
		    			</c:if>
    				</c:forEach>
    			</div>
				</c:if>
				<!-- 最新视频 -->
			</div>
			<jsp:include page="/wechat/common/foot.jsp"></jsp:include>
		</div>
		<!-- 加入分享 -->
		<%@ include file="/wechat/common/base_share.jsp"%>
	</body>
</html>
