<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>

<script type="text/javascript"
	src="/assets/js/admin/common_datatable.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/collect/compilation/resource/list.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>普通合集</h2>
		<ol class="breadcrumb">
			<li><a
				href="/project/collect/list/page">合集管理</a></li>
			<li class="active">资源列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/collect/compilation/resource/add/page/${requestScope.compilation.uuid }">添加资源</a>
	</div>
</div>

<input type="hidden" id="compilationUuid"
	value="${requestScope.compilation.uuid }" />

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5><font color="green">${requestScope.compilation.name }&nbsp;/&nbsp;</font>资源列表</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="collect_compilation__res_list_table"
					class="display dataTable dtr-inline" cellspacing="0" width="100%"
					role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">名称</th>
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
	
</script>