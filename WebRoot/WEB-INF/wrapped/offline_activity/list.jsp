<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/offline_activity/list.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>线下活动</h2>
		<ol class="breadcrumb">
			<li class="active">线下活动列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/admin/offline/activity/add/page">新建线下活动</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>线下活动列表</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="activity_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;" >
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">活动名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建日期</th>
							<th rowspan="1" colspan="1" style="width: 10%;">报名类型</th>
							<th rowspan="1" colspan="1" style="width: 10%;">报名上限</th>
							<th rowspan="1" colspan="1" style="width: 10%;">活动地点</th>
							<th rowspan="1" colspan="1" style="width: 15%;">价格</th>
							<th rowspan="1" colspan="1" style="width: 10%;">发起人</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 15%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>

		</div>
	</div>
</div>

<!-- 绑定发起人 -->
<div class="modal fade" id="bind_user_modal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title"><span id="activity_name" style="color: green;"></span> 绑定发起人</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-md-2">关联用户</label>
						<div id="select_div" class="col-sm-8"></div>
					</div>
					<div class="form-group">
						<label class="control-label col-md-2">解除绑定</label>
						<div class="col-md-3">
							<input type="button" class="btn btn-primary" id="unbind_user" value="解除绑定"/>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="bind_confirm" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- 绑定发起人 -->


<script type="text/javascript">
	var error = '${requestScope.error}';
	if (!!error) {
		alert(error);
	}
</script>
