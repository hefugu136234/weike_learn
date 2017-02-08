<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<!-- <script src="/assets/js/admin/pdf_common.js?ver=1.0"></script> -->
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>作品资源解除绑定</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/oups/list/page">返回列表</a></li>
			<li class="active">资源解除绑定</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/admin/oups/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>资源解除绑定</h5>
		</div>
		<div class="ibox-content">
			<form id="relate_form" class="form-horizontal formBox valForm">
				<input type="hidden" id="uuid" value="${requestScope.oupsCodeVo.uuid}" />

				<div class="form-group">
					<label class="col-sm-3 control-label">作品 编号：</label>
					<div class="col-sm-6">
						<label>${requestScope.oupsCodeVo.codeNum}</label>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">作品名称：</label>
					<div class="col-sm-6">
						<label>${requestScope.oupsCodeVo.name}</label>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">申请者名称：</label>
					<div class="col-sm-6">
						<label>${requestScope.oupsCodeVo.applyUserName}</label>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">资源名称：</label>
					<div class="col-sm-6">
						<label>${requestScope.oupsCodeVo.resName}</label>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">资源分类：</label>
					<div class="col-sm-6">
						<label>${requestScope.oupsCodeVo.cateName}</label>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">资源类型：</label>
					<div class="col-sm-6">
						<label>${requestScope.oupsCodeVo.resType}</label>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">资源简介：</label>
					<div class="col-sm-6">
						<label>${requestScope.oupsCodeVo.resMark}</label>
					</div>
				</div>


				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/admin/oups/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
$(function(){
	activeStub('activity_oups_code_list-nav');
	$('#relate_form').submit(function(event) {
		event.preventDefault();
			$.post('/admin/oups/remove/binding', {'uuid':$('#uuid').val()}, function(data) {
				if (data.status == 'success') {
					alert("作品解除绑定成功");
					window.location.href = '/admin/oups/list/page';
				} else {
					alert(data.message);
				}
			});
	});
});
</script>
