<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<nav class="navbar top-bar" id="top_bar">
	<div class="container">
		<ul class="nav navbar-nav nav-menu">
			<li class="logo"><a href="/f/web/index"><img
					src="/assets/img/web/logo.png" class="icon" /></a></li>
			<li id="nav_index" class="with-arr"><a href="/f/web/index">首
					页</a></li>
			<li id="nav_activity"><a href="/f/web/activity/list/page">活
					动</a></li>
			<li id="nav_yunhe"><a href="javascript:;">云 盒</a></li>
		</ul>
		<div class="pull-right nav-user">
			<c:choose>
				<c:when test="${empty sessionScope.user_view_key}">
					<a id="no_login_a" href="/f/web/login"
						class="btn btn-default btn-lg btn-login btn-bd-green"><span
						class="icon icon-wechat"></span>微信登录</a>
				</c:when>
				<c:otherwise>
					<a id="login_ing_a" href="javascript:;" class="top-ucenter">
						<img src="${not empty sessionScope.user_view_key.photo?sessionScope.user_view_key.photo:'/assets/img/app/default_img.jpg'}" alt=""
						class="img-circle photo">
						<div class="info">
							<p class="name">${sessionScope.user_view_key.showName}</p>
							<p class="icons">
							<c:if test="${sessionScope.user_view_key.vip eq 'in_use'}">
								<img id="vip_login_img" src="/assets/img/web/icon_vip.png"
									class="icon" /> </c:if><c:if test="${sessionScope.user_view_key.realName}"><img id="real_login_img"
									src="/assets/img/web/icon_real.png" class="icon" /></c:if>
							</p>
						</div>
					</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="sub-nav-con" id="sub_nav_con">
		<div class="container">
			<div class="sub-nav-list clearfix" id="top_menu_list"></div>
		</div>
	</div>
</nav>
