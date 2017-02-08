<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="page">
	<div class="content">
		<header class="top-bar">
			<div class="crumb-nav">
				<a href="/api/webchat/my/center" class="logo icon-logo"></a>
				${res_vo.news.title}
			</div>
		</header>

		<div class="article-detail">
			<div class="summary">
				<h5 class="tt">${res_vo.news.title}</h5>
				<div class="date">${res_vo.dateTime}</div>
			</div>

			<div class="details">${res_vo.news.content}</div>
		</div>
	</div>
</div>
<input type="hidden" id="resource_uuid" value="${res_vo.uuid}" />

