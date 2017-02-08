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
<meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
<title>学科分类</title>
<link href="http://cdn.bootcss.com/Swiper/3.1.7/css/swiper.min.css" rel="stylesheet">
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script src="http://cdn.bootcss.com/Swiper/3.1.7/js/swiper.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>
<!-- onclick="javascript:window.location.reload();" -->
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

			<!-- 学科 -->
			<c:if test="${not empty vo_data.menuList}">
				<ul class="grid-img-group clearfix">
					<c:forEach var="item" items="${vo_data.menuList}">
						<li>
							<a href="${item.url}" class="box">
								<img src="${item.cover}" alt="${item.name}" class="cover">
								<div class="tt">${item.name}</div>
								<div class="num">${item.resCount}</div>
							</a>
						</li>
					</c:forEach>
				</ul>
			</c:if>
			<!-- 学科 -->

		</div>
		<jsp:include page="/wechat/common/foot.jsp"></jsp:include>
	</div>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
	<script type="text/javascript">
	$(function(){
		addActiveClass('xkfl');
	});
	</script>
</body>
</html>
