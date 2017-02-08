<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<link rel="stylesheet" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" href="/assets/css/site.css">

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/admin/activity/resource_list.js"></script>
<script src="/assets/js/admin/activity/main.js?ver=1.0"></script>
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>活动管理</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/activity/list">返回活动列表</a></li>
			<li class="active">活动资源列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/admin/activity/${requestScope.activity.uuid}/resmgr">添加资源</a>
	</div>
</div>
<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5><font color="green">${requestScope.activity.name}</font>&nbsp;&nbsp;的资源列表</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="activity_reslist" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">名称</th>
							<th rowspan="1" colspan="1" style="width: 8%;">讲者</th>
							<th rowspan="1" colspan="1" style="width: 8%;">类型</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">修改时间</th>
							<th rowspan="1" colspan="1" style="width: 8%;">浏览次数</th>
							<th rowspan="1" colspan="1" style="width: 20%;">备注</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th rowspan="1" colspan="1" style="width: 15%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="activityUuid" value="${requestScope.activity.uuid}" />
<div class="modal fade" id="markModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">完整信息如下</h4>
			</div>
			<div class="modal-body">
				<div id="markDetail" ></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>