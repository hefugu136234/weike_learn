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
<title>课程包列表</title>

<link rel="stylesheet" href="/assets/css/app/font.css">
<link rel="stylesheet" href="/assets/css/app/default.css">
<link href="http://cdn.bootcss.com/Swiper/3.1.7/css/swiper.min.css"
	rel="stylesheet">
<link href="/assets/css/app/dropload.css" rel="stylesheet">
<link rel="stylesheet" href="/assets/css/app/package.css">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript"
	src="http://cdn.bootcss.com/Swiper/3.1.7/js/swiper.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript"
	src="/assets/js/wechat/course/course_package_list.js"></script>
</head>

<body>
	<div class="page">
		<div class="content pb50">
			<!-- <div class="package-banner">
          <img src="/assets/img/app/package/banner.jpg" alt="课程包banner">
          <a href="/api/webchat/index" class="logo"><img src="/assets/img/app/logo_font.png"></a>
        </div> -->
			<!-- banner -->
			<c:if test="${not empty vo_data.bannerList}">
				<div class="swiper-container default-swiper">
					<ul class="swiper-wrapper">
						<c:forEach var="item" items="${vo_data.bannerList}">
							<li class="swiper-slide"><a
								href="${not empty item.url?item.url:'javascript:void(0);'}">
									<img src="${item.cover}">
							</a></li>
						</c:forEach>
					</ul>
					<div class="swiper-pagination"></div>
				</div>
				<a href="/api/webchat/index"
					class="icons icon-logo page-absolute-logo"></a>
			</c:if>
			<!-- banner -->

			<div id="course_package_div">
				<div id="course_package_ul" class="grid-item-group">
					<!-- <div class="box">
					<a href="#">
						<div class="img">
							<img src="" alt="课程包封面">
							<div class="package-process">
								<div class="process-circle">
									<p class="process">
										<span class="num">10</span>%
									</p>
									<p>进度</p>
								</div>
							</div>
							<div class="package-done">
								<div class="info">
									<div class="icon icon-checked"></div>
									<p>已完成</p>
								</div>
							</div>
						</div>
						<div class="desc">
							<h5 class="tt">内科基本知识课堂</h5>
							<div class="package-list-status">
								<span class="box"><i class="icon icon-edit"></i>999</span> <span
									class="box txtC"><i class="icon icon-like"></i>999</span> <span
									class="box txtR"><i class="icon icon-message"></i>999</span>
							</div>
						</div>
					</a>
				</div> -->

				</div>
			</div>
		</div>
	</div>

	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
