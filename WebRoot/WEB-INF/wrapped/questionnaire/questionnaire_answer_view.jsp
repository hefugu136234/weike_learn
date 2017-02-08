<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>问卷管理</h2>
		<ol class="breadcrumb">
			<li><a
				href="/project/questionnaire/answer/list/page/${requestScope.pro_uuid}">返回上一级</a></li>
			<li class="active">答题详情</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/questionnaire/answer/list/page/${requestScope.pro_uuid}">返回上一级</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">

	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>答题详情</h5>
		</div>
		<div class="ibox-content">
			<c:if test="${not empty requestScope.answer}">
				<input type="hidden" id="uuid" value="${requestScope.answer.uuid}" />
				
				<div class="row" style="margin-bottom:32px;">
					<div class="col-sm-2">答题人：<span style="color: red;">${requestScope.answer.username}</span></div>
					<div class="col-sm-2">答题序号：<span style="color: red;">${requestScope.answer.answerSeq}</span></div>
					<div class="col-sm-2">答题状态：<span style="color: red;">${requestScope.answer.answerStatus}</span></div>
					<div class="col-sm-2">答题开始时间：<span style="color: red;">${requestScope.answer.answerStatus}</span></div>
					<div class="col-sm-2">答题用时：<span style="color: red;">${requestScope.answer.answerTime}</span></div>
					<div class="col-sm-2">分值：<span style="color: red;">${answer.score}</span></div>

				</div>
				
				<c:if test="${not empty requestScope.answer.list}">
					<c:forEach var="item" items="${requestScope.answer.list}">
						<div class="well">
							<h3>${item.title}</h3>
							<p>${item.answer}</p>
						</div>
					</c:forEach>
				</c:if>

			</c:if>
		</div>
	</div>
</div>
<script type="text/javascript">
	showActive([ 'questionnaire_list_nav', 'questionnaire_mgr' ]);
	var error = '${requestScope.error}';
	if (!!error) {
		alert(error);
	}
</script>

