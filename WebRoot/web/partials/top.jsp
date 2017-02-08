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
		<div class="pull-right nav-user hide">
			<a id="no_login_a" href="javascript:void(0);"
				class="btn btn-default btn-lg btn-login btn-bd-green hide"><span
				class="icon icon-wechat"></span>微信登录</a> 
				
				<a id="login_ing_a" href="javascript:;" class="top-ucenter hide"> 
				<img src="/assets/img/app/default_img.jpg" alt="" class="img-circle photo">
				<div class="info">
					<p class="name"></p>
					<p class="icons">
							<img id="vip_login_img" src="/assets/img/web/icon_vip.png"
								class="icon" />
							<img id="real_login_img" src="/assets/img/web/icon_real.png"
								class="icon" />
					</p>
				</div>
			</a>
		</div>
	</div>

	<div class="sub-nav-con" id="sub_nav_con">
		<div class="container">
			<div class="sub-nav-list clearfix" id="top_menu_list"></div>
		</div>
	</div>
</nav>
