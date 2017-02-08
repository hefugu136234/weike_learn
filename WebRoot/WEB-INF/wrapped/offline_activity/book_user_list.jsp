<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/offline_activity/user_list.js"></script>
	
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>线下活动</h2>
		<ol class="breadcrumb">
			<li class="active">线下活动报名人员列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/admin/offline/activity/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5><span style="color: green;">${name}</span>报名人员</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="list_user_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;" >

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">用户名</th>
							<th rowspan="1" colspan="1" style="width: 10%;">报名时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">修改时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">手机号码</th>
							<th rowspan="1" colspan="1" style="width: 10%;">真实姓名</th>
							<th rowspan="1" colspan="1" style="width: 10%;">微信昵称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">用户类型</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 20%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
			<input type="hidden" id="uuid" value="${uuid}"/>
		</div>
	</div>
</div>

<!-- 拒绝审核-->
<div class="modal fade" id="dataModal_refuse" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel_pdf">拒绝审核通过的理由：</h4>
			</div>
			<div class="modal-body">
			<div class="form-horizontal">
				<div class="form-group">
						<label class="control-label col-md-2">拒绝理由</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="refuse_case" maxlength="200" />
						</div>
					</div>
			</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="resufe_confirm_btn" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- 拒绝审核-->
