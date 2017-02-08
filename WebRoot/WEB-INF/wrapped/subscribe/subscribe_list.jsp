<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript" src="/assets/js/admin/subscribe_list.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>预约管理</h2>
		<ol class="breadcrumb">
			<li class="active">预约列表</li>
		</ol>
	</div>
	<!-- <div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/pdf/add/page">新建PDF</a>
	</div> -->
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>预约列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="subscribe_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">姓名</th>
							<th rowspan="1" colspan="1" style="width: 10%;">手机</th>
							<th rowspan="1" colspan="1" style="width: 20%;">企业名称</th>
							<th rowspan="1" colspan="1" style="width: 20%;">品牌名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">企业邮箱</th>
							<th rowspan="1" colspan="1" style="width: 20%;">办公地址</th>
							<th rowspan="1" colspan="1" style="width: 10%;">座 机</th>
							<!-- <th style="width: 15%;">操作</th> -->
						</tr>
					</thead>
				</table>
			</div>

		</div>
	</div>
</div>

