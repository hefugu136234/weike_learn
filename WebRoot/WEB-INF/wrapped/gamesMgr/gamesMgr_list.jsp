<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css" href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" type="text/css" href="/assets/css/plugins/dataTables/dataTables.responsive.css">

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/admin/gamesMgr/gamesMgr_list.js"></script>
<script src="/assets/js/plugins/ZeroClipboard/ZeroClipboard.js"></script>
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>游戏管理</h2>
		<ol class="breadcrumb">
			<li class="active">游戏列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/games/add/page">新建游戏</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>游戏列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="games_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 15%;">游戏名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">游戏开始时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">游戏结束时间</th>
							<th rowspan="1" colspan="1" style="width: 20%;">备注</th>
							<th rowspan="1" colspan="1" style="width: 10%;">当前状态</th>
							<th rowspan="1" colspan="1" style="width: 20%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
			
		</div>
	</div>
</div>

<div class="modal fade" id="lotteryUrlModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="urlModalLabel">游戏链接</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-3 control-label">链接：</label>
						<div class="col-md-8">
							<textarea class="form-control" id="fe_text" rows="4" readonly="readonly" ></textarea>
						</div>
					</div>
				</div>	
			</div>
			<div class="modal-footer">
				<button id="getLotteryUrl" class="btn btn-primary" data-clipboard-target="fe_text">复制链接</button>
			</div>
		</div>
	</div>
</div>
