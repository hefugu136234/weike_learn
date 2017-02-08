<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript" src="/assets/js/admin/apply_list.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>vip申请管理</h2>
		<ol class="breadcrumb">
			<li class="active">vip申请列表</li>
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
			<h5>申请记录列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="apply_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 15%;">姓名</th>
							<th rowspan="1" colspan="1" style="width: 12%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 13%;">手机</th>
							<th rowspan="1" colspan="1" style="width: 20%;">医院</th>
							<th rowspan="1" colspan="1" style="width: 20%;">科室</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 10%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>

		</div>
	</div>
</div>

<!-- 状态更改 -->
<div class="modal fade" id="dataModal_vip" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">状态更改</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
						<input type="radio" name="status" value="1"/> 审核
						<input type="radio" name="status" value="2"/> 发货
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="vip_button" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- 状态更改 -->


