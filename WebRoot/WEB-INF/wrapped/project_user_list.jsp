<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css"
	rel="stylesheet">

<div id="blueimp-gallery" class="blueimp-gallery">
	<div class="slides"></div>
	<h3 class="title"></h3>
	<a class="prev">‹</a> <a class="next">›</a> <a class="close">×</a> <a
		class="play-pause"></a>
	<ol class="indicator"></ol>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>用户列表</h2>
		<ol class="breadcrumb">
			<li>用户管理</li>
            <li class="active">
                <strong>用户列表</strong>
            </li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/user/new">新建用户</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<ul id="myTab" class="nav nav-tabs">
		<li class="active">
			<a href="#list_hasReg" data-toggle="tab" id="hasReg" >已注册</a>
		</li>
		<li>
			<a href="#list_noReg" data-toggle="tab" id="noReg" >未注册</a>
		</li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<input type="hidden" id="isSuperAdmin" value="${requestScope.isSuperAdmin }" />
		
		<div class="tab-pane fade in active" id="list_hasReg">
			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>已注册用户列表</h5>
					</div>
					<div class="ibox-content">
						<div id="list_wrapper" class="dataTables_wrapper">
							<table id="project_hasreg_user_list_table" class="display dataTable dtr-inline"
											 role="grid" style="width: 100%;">
								<thead>
									<tr>
										<th rowspan="1" colspan="1" style="width: 10%;">用户名</th>
										<th rowspan="1" colspan="1" style="width: 8%;">真实姓名</th>
										<th rowspan="1" colspan="1" style="width: 8%;">微信昵称</th>
										<th rowspan="1" colspan="1" style="width: 10%;">用户类型</th>
										<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
										<th rowspan="1" colspan="1" style="width: 10%;">手机号码</th>
										<th rowspan="1" colspan="1" style="width: 10%;">到期时间</th>
										<th rowspan="1" colspan="1" style="width: 10%;">角色</th>
										<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
										<th style="width: 10%;">操作</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>	
		</div>
		<div class="tab-pane fade" id="list_noReg">
			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>未注册用户列表</h5>
					</div>
					<div class="ibox-content">
						<div id="list_wrapper" class="dataTables_wrapper">
							<table id="project_noreg_user_list_table" class="display dataTable dtr-inline"
											 role="grid" style="width: 100%;">
								<thead>
									<tr>
										<th rowspan="1" colspan="1" style="width: 15%;">昵称</th>
										<th rowspan="1" colspan="1" style="width: 15%;">创建时间</th>
										<th rowspan="1" colspan="1" style="width: 15%;">头像</th>
										<th rowspan="1" colspan="1" style="width: 15%;">OpenID</th>
										<th rowspan="1" colspan="1" style="width: 15%;">UnionID</th>
										<th rowspan="1" colspan="1" style="width: 15%;">状态</th>
										<th style="width: 10%;">操作</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
	


<!-- 用户流量卡使用记录 -->
<div class="modal fade" id="userCardListModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">流量卡记录</h4>
			</div>
			<div class="modal-body">
				<div id="userCardTable"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="select_confirm_btn_search" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/blueimp/jquery.blueimp-gallery.min.js"></script>
<script type="text/javascript" src="/assets/js/admin/project_user_list.js"></script>