<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/product/manufacturer_common.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>厂商管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/group/manufacturer/list/page">返回列表</a></li>
			<li class="active">新建厂商</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/group/manufacturer/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>新建厂商</h5>
		</div>
		<div class="ibox-content">
			<form id="manufacturer_form" class="form-horizontal formBox valForm">
				<input type="hidden" name="token" value="${requestScope.token}" />

				<div class="form-group">
					<label class="col-sm-3 control-label">厂商名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name"
							required="required" maxlength="80" placeholder="厂商名称(60字以内)" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">厂商编号：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="serialNum"
							required="required" maxlength="4" placeholder="厂商编号(4字以内)"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">厂商logo：</label>
					<div class="col-sm-6">
						<input id="cover_uploadify" type="file"> <img
							id="cover_preview" alt="" src="" style="display: none;"> <input
							type="hidden" name="taskId" id="coverTaskId">
						<hr>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">厂商地址：</label>
					<div class="col-sm-6">
						<textarea name="address" cols="60" rows="4" maxlength="180"
							placeholder="厂商地址(200字以内)"></textarea>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/group/manufacturer/list/page"
							class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
	function submitFrom(from) {
		$.post('/project/group/manufacturer/save',$(from).serialize(),
		function(data) {
			if (data.status == 'success') {
				location.href = '/project/group/manufacturer/list/page';
			} else {
				alert(data.message);
			}
		});
	}
</script>
