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
<title>积分商场</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/users.css" rel="stylesheet">
<link href="/assets/css/app/shop.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<body>
	<div class="page">
		<div class="content">
			<div class="user-status-panel">
				<!-- 加载公共的头部个人信息 -->
				<jsp:include page="/wechat/common/top_userInfo.jsp"></jsp:include>
				<!-- 加载公共的头部个人信息 -->

				<div class="user-status">
					<div class="box points">
						<p class="num">${vo_user_data.score}</p>
						<p class="tt">积分</p>
					</div>
				</div>
			</div>

			<div class="icon-title bold clearfix">
				<h5 class="tt bfL">兑换商品列表</h5>
			</div>

			<div class="shelves-list clearfix">
				<c:if test="${not empty requestScope.goods_list}">
					<c:forEach var="item" items="${requestScope.goods_list}">
						<a href="/api/webchat/wx/jifen/shop/detail/${item.uuid}" class="box">
							<div class="img">
								<img src="${item.cover}">
							</div>
							<div class="intro">
								<h5 class="name">${item.name}</h5>
								<p class="price">
									所需积分：<span class="num">${item.integral}</span>
								</p>
							</div>
						</a>
					</c:forEach>
				</c:if>
			</div>

		</div>
	</div>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
