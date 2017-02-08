<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript" src="/assets/js/admin/product/activation_list.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>流量卡管理</h2>
		<ol class="breadcrumb">
			<li class="active">流量卡列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/group/active/add/page">新建流量卡</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>流量卡列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="active_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">卡号</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 15%;">厂商</th>
							<th rowspan="1" colspan="1" style="width: 10%;">产品组</th>
							<th rowspan="1" colspan="1" style="width: 10%;">时长（天）</th>
							<th rowspan="1" colspan="1" style="width: 10%;">激活者</th>
							<th rowspan="1" colspan="1" style="width: 10%;">激活时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">激活状态</th>
							<th style="width: 15%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>

		</div>
	</div>
</div>


<!-- <script type="text/javascript">
	var status = '${requestScope.error_info}';
	if (!!status) {
		alert(status);
	}
</script> -->
