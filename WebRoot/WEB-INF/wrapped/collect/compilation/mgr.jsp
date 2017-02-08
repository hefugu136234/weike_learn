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
<script src="/assets/js/admin/collect/compilation/mgr.js"></script>
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
		<h2>合集管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/collect/list/page">合集管理</a></li>
			<li class="active">${not empty requestScope.compilation ? '编辑合集' : '新建合集' }</li>
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
			<h5>${not empty requestScope.compilation ? '编辑合集' : '新建合集' }</h5>
		</div>
		<div class="ibox-content" style="display: block;">
			<form id="compilation_form" method="post" action=""
				class="form-horizontal">
	
				<input type="hidden" name="token" value="${requestScope.token }" > 
				<input type="hidden" name="webCover" id="webCover" value="${requestScope.media.url }" >
				<input type="hidden" name="uuid" value="${requestScope.compilation.uuid }" >
				<textarea class="hide" id="descripion_hiden">${requestScope.compilation.description }</textarea>
	
				<div class="form-group">
					<label class="col-sm-2 control-label">名称：</label>
					<div class="col-sm-4 controls">
						<input type="text" class="form-control" name="name" maxlength="29"
							value="${requestScope.compilation.name }" placeholder="请输入合集名称(30字以内)"
							autocomplete="off" required="required">
					</div>
				</div>
				<div class="hr-line-dashed"></div>
	
				<div class="form-group">
					<label class="col-sm-2 control-label">封面(WEB)：</label>
					<div class="col-sm-3">
						<input id="cover-view" type="file"> <img class="pre-view"
							alt="" src="${requestScope.media.url }" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>
	
				<div class="form-group">
					<label class="col-sm-2 control-label">简介：</label>
					<div class="col-sm-4">
						<textarea class="form-control" rows="5" maxlength="199"
							placeholder="请输入简介(200字以内)" name="mark">${requestScope.compilation.mark }</textarea>
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
