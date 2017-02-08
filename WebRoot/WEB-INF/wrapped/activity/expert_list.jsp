<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<link rel="stylesheet" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css" >
<link rel="stylesheet" href="/assets/css/site.css">

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/plugins/blueimp/jquery.blueimp-gallery.min.js"></script>
<script src="/assets/js/admin/activity/expert_list.js"></script>
<script src="/assets/js/admin/activity/main.js?ver=1.0"></script>
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>活动管理</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/activity/list">返回活动列表</a></li>
			<li class="active">活动专家列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/admin/activity/expertMgr/${requestScope.activity.uuid}">添加专家</a>
	</div>
</div>
<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5><font color="green">${requestScope.activity.name}</font>&nbsp;&nbsp;的专家列表</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="activity_expertList" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">头像</th>
							<th rowspan="1" colspan="1" style="width: 10%;">姓名</th>
							<th rowspan="1" colspan="1" style="width: 15%;">医院</th>
							<th rowspan="1" colspan="1" style="width: 10%;">职称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">备注</th>
							<th rowspan="1" colspan="1" style="width: 10%;">添加时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">更新时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th rowspan="1" colspan="1" style="width: 15%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>

<!-- The Gallery as lightbox dialog, should be a child element of the document body -->
<div id="blueimp-gallery" class="blueimp-gallery">
	<div class="slides"></div>
	<h3 class="title"></h3>
	<a class="prev">‹</a> <a class="next">›</a> <a class="close">×</a> <a
		class="play-pause"></a>
	<ol class="indicator"></ol>
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