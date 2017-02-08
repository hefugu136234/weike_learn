<%@page import="org.springframework.web.util.HtmlUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" href="/assets/js/uploadify/uploadify.css">

<script src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script src="/assets/js/formValidation.min.js"></script>
<script src="/assets/js/formvalidation.js"></script>
<script src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script src="/assets/js/admin/uploader_common.js"></script>
<script src="/assets/ueditor/ueditor.config.js"></script>
<script src="/assets/ueditor/ueditor.all.min.js"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script src="/assets/js/admin/common_submit.js"></script>
<script src="/assets/js/admin/collect/course/chapters/resource/mgr.js"></script>
<style>
<!--
.pre-view {
	border: 1px gray solid;
	max-width: 280px;
	max-height: 280px;
}
-->
</style>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>章节资源管理</h2>
		<ol class="breadcrumb">
			<li><a
				href="/project/collect/course/chapters/list/page/${requestScope.chapter.uuid }">章节管理</a></li>
			<li class="active">${not empty requestScope.resourceGroup ? '编辑资源' : '新建资源' }</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/collect/course/chapters/resource/list/page/${requestScope.chapter.uuid }">返回</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>${not empty requestScope.resourceGroup ? '编辑资源' : '新建资源' }</h5>
		</div>
		<div class="ibox-content" style="display: block;">
			<form id="new_course_chapters_res_form" method="post" action=""
				class="form-horizontal">

				<input type="hidden" name="resourceGroupUuid" id="resourceGroupUuid"
					value="${requestScope.resourceGroup.uuid }">
				<input type="hidden" name="chapterUuid" id="chapterUuid"
					value="${requestScope.chapter.uuid }"> 
				<input type="hidden" name="token" value="${requestScope.token }">

				<div class="form-group">
					<label class="col-sm-2 control-label">选择资源：</label>
					<div class="col-sm-4">
						<select class="form-control" id="res_selector" name="resourceUuid">
							<option>请输入"资源名称"或者"讲者"检索资源</option>
							<c:if test="${not empty requestScope.resourceItem}">
								<option selected="selected"
									value="${requestScope.resourceItem.id}">${requestScope.resourceItem.text}</option>
							</c:if>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-2 control-label">名称：</label>
					<div class="col-sm-4 controls">
						<input type="text" class="form-control" name="name" maxlength="59"
							value="${requestScope.resourceGroup.name }"
							placeholder="资源名称(60字以内)，缺省值为选中资源的名称" autocomplete="off" >
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-2 control-label">简介：</label>
					<div class="col-sm-4">
						<textarea class="form-control" rows="5" maxlength="299"
							placeholder="请输入简介(300字以内)，缺省值为选中资源的简介" name="mark">${requestScope.resourceGroup.mark }</textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit" id="commit">提交</button>
						<a href="/project/collect/course/chapters/resource/list/page/${requestScope.chapter.uuid }" class="btn btn-warning">取消</a>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
			</form>
		</div>
	</div>
</div>
<script>
	$(document).ready(function() {
		var error = '${requestScope.error}';
		if (!!error) {
			alert(error);
		}
	})
</script>
