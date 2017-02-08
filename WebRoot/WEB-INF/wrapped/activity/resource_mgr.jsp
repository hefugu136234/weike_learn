<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">

<script src="/assets/js/admin/activity/main.js?ver=1.0"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script src="/assets/js/admin/activity/resource_mgr.js?ver=1.0"></script>
	
	
<style>
<!--
.pre-view {
	border: 1px gray solid;
	max-width: 280px;
	max-height: 280px;
}
table td{
vertical-align:top;
width: 300px;
}
-->
</style>



<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>活动管理</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/activity/list">返回活动列表</a></li>
			<li class="active">活动资源配置</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/admin/activity/${requestScope.activity.uuid}/reslist">返回资源列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>给&nbsp;&nbsp;<font color="green">${requestScope.activity.name}</font>&nbsp;&nbsp;活动添加资源</h5>
		</div>
		<div class="ibox-content">
			<form id="activity_resmgr_form" method="post" action=""
				class="form-horizontal formBox valForm">
				<input type="hidden" name="token" value="${requestScope.token}" />
				<input type="hidden" name="activityUuid" id="activityUuid" value="${requestScope.activity.uuid}" />
				
				<div class="form-group">
					<label class="col-sm-3 control-label" >选择资源：</label>
					<div class="col-md-6">
						<select class="form-control" id="res_selector"
							name="resourceUuid">
							<option>请输入"资源名称"或者"讲者"检索资源</option>
							<!-- <option>请输入"资源名称"或者"讲者"检索资源，多个条件用"空格"分开</option>  -->
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">资源简介：</label>
					<div class="col-md-6">
						<textarea rows="6" class="form-control" id="mark" maxlength="59"
								name="mark" placeholder="该简介在60字以内"></textarea>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="button" id="resmgr_btn_submit">保存</button>
						<a href="/admin/activity/list" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
