<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>

<script type="text/javascript"
	src="/assets/js/admin/common_datatable.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/collect/list.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>合集管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/collect/list/page">合集管理</a></li>
			<li class="active">合集列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/collect/course/add">创建课程</a>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/collect/compilation/add">创建合集</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>合集/课程&nbsp;列表</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="collect_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">所有人</th>
							<th rowspan="1" colspan="1" style="width: 10%;">类型</th>
							<th rowspan="1" colspan="1" style="width: 20%;">简介</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 10%;">操作</th>
						</tr>
					</thead>

				</table>
			</div>

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