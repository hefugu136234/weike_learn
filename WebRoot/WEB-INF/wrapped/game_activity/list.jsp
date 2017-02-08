<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link href="/assets/css/site.css" rel="stylesheet" >
<link href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css" rel="stylesheet">
<link href="/assets/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/admin/game_activity/list.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>临时活动管理</h2>
		<ol class="breadcrumb">
			<li class="active">摇一摇活动</li>
		</ol>
	</div>
	<!-- <div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/admin/activity/new">新建活动</a>
	</div> -->
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>参与者列表</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<div id="filterSearch">
					<div id="winer" style="display:inline">
						是否中奖：<select name="resourceType" id="searchButton_isWinner" >
									<option value="">所有</option>
									<option value="yes">中奖用户</option>
									<option value="no">未中奖用户</option>
								 </select>&emsp;&emsp;
					</div>
					<div id="handle" class="hide" style="display:inline">
						处理状态: <select name="resourceState" id="searchButton_isHandle" >
									<option value="">所有</option>
									<option value="1">未处理</option>
									<option value="2">已处理</option>
								 </select>&emsp;&emsp;
					</div>
				</div><br/>
				<table id="shake_record_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 15%;">用户名</th>
							<th rowspan="1" colspan="1" style="width: 15%;">用户昵称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">参与日期</th>
							<th rowspan="1" colspan="1" style="width: 15%;">是否中奖</th>
							<th rowspan="1" colspan="1" style="width: 10%;">处理日期</th>
							<th rowspan="1" colspan="1" style="width: 15%;">备注</th>
							<th rowspan="1" colspan="1" style="width: 10%;">兑换状态</th>
							<th rowspan="1" colspan="1" style="width: 15%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
			
		</div>
	</div>
</div>
