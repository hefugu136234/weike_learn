<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="user-photo">
	<div class="photo">
		<img src="${vo_user_data.photo}" alt="">
	</div>
	<c:if test="${vo_user_data.vipStatus eq 'in_use'}">
		<div class="icon vip"></div>
	</c:if>
	<c:if test="${vo_user_data.vipStatus eq 'late_use'}">
		<div class="icon not-vip"></div>
	</c:if>
</div>

<div class="user-info">
	<div class="box">
		<h5 class="name">
			${vo_user_data.showName}
			<c:if test="${vo_user_data.realFlag}">
				<span class="icon real"></span>
			</c:if>
		</h5>
		<div class="desc">
			<c:if test="${vo_user_data.realFlag}">
				<p>${vo_user_data.realNameInfo}</p>
			</c:if>
			<c:if test="${not empty vo_user_data.vipDeadTime}">
				<p>VIP期至：${vo_user_data.vipDeadTime}</p>
			</c:if>
		</div>
	</div>
</div>
