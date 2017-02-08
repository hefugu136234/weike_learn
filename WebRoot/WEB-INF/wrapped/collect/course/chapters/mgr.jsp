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
<script src="/assets/js/admin/collect/course/chapters/mgr.js"></script>
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
		<h2>课程管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/collect/list/page">课程管理</a></li>
			<li class="active">${not empty requestScope.chapter ? '编辑章节' : '新建章节' }</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href='/project/collect/list/page'>返回</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>${not empty requestScope.chapter ? '编辑章节' : '新建章节' }</h5>
		</div>
		<div class="ibox-content" style="display: block;">
			<form id="new_course_chapters_form" method="post" action=""
				class="form-horizontal">
	
				<input type="hidden" name="courseUuid" id="courseUuid" value="${requestScope.course.uuid }" >
				<input type="hidden" name="chapterUuid" id="chapterUuid" value="${requestScope.chapter.uuid }" >
				<textarea class="hide" id="descripion_hiden">${requestScope.chapter.description }</textarea>
				<input type="hidden" name="token" value="${requestScope.token }" > 
	
				<div class="form-group">
					<label class="col-sm-2 control-label">章节名：</label>
					<div class="col-sm-4 controls">
						<input type="text" class="form-control" name="name" maxlength="29"
							value="${requestScope.chapter.name }" placeholder="请输入章节名称(30字以内)"
							autocomplete="off" required="required">
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">通过分数：</label>
					<div class="col-sm-4 controls">
						<input type="text" class="form-control" name="passScore" id="passScore" maxlength="2"
							value="${requestScope.chapter.passScore }" placeholder="请设置通过该章节的分数"
							autocomplete="off" required="required">
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">条件：</label>
					<div class="col-sm-4">
						<select name="type" id="type" class="form-control"
							required="required">
							<option value="0" ${requestScope.type eq '0' ? 'selected': ''}>不需要上一章节通过</option>
							<option value="1" ${requestScope.type eq '1' ? 'selected': ''}>需要上一章节通过</option>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
	
				<div class="form-group">
					<label class="col-sm-2 control-label">简介：</label>
					<div class="col-sm-4">
						<textarea class="form-control" rows="5" maxlength="199"
							placeholder="请输入简介(200字以内)" name="mark">${requestScope.chapter.mark }</textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
	
				<div class="form-group">
					<label class="col-sm-2 control-label">描述：</label>
					<div class="col-sm-8">
						<script id="description" name="description" required="required"
							type="text/plain" style="height: 350px;"></script>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
	
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">提交</button>
						<a href="/project/collect/list/page" class="btn btn-warning">取消</a>
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
