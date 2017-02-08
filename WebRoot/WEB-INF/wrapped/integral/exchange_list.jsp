<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css" >
<link rel="stylesheet" href="/assets/css/plugins/dataTables/dataTables.responsive.css">

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/admin/integral/exchange_list.js"></script>
<script src="/assets/js/common.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>积分管理</h2>
		<ol class="breadcrumb">
			<li class="active">兑换记录</li>
		</ol>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>兑换记录</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="exchange_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 15%;">兑换者</th>
							<th rowspan="1" colspan="1" style="width: 15%;">申请日期</th>
							<th rowspan="1" colspan="1" style="width: 15%;">处理日期</th>
							<th rowspan="1" colspan="1" style="width: 10%;">消耗积分</th>
							<th rowspan="1" colspan="1" style="width: 10%;">物品名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">当前状态</th>
							<th rowspan="1" colspan="1" style="width: 25%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
			
		</div>
	</div>
</div>

<!-- 收货地址 -->
<div class="modal fade" id="address_modal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">兑换记录的收货地址：</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					
					<div class="form-group">
						<label class="control-label col-md-3">收货人：</label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="ex_name"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-md-3">手机号码：</label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="ex_phone"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-md-3">收货地址：</label>
						<div class="col-md-8">
							<textarea id="ex_address" class="form-control"  rows="3" maxlength="250"
							placeholder="收货地址"></textarea>
						</div>
					</div>
				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="address_but" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- 收货地址-->
