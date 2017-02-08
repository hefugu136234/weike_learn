<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css" >
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/plugins/blueimp/jquery.blueimp-gallery.min.js" type="text/javascript"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script src="/assets/js/admin/speaker_list.js?ver=1.0"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>讲者管理</h2>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/speaker/new">新建讲者</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>讲者列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="speakers_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">头像</th>
							<th rowspan="1" colspan="1" style="width: 5%;">名字</th>
							<th rowspan="1" colspan="1" style="width: 5%;">性别</th>
							<th rowspan="1" colspan="1" style="width: 10%;">编辑时间</th>	
							<th rowspan="1" colspan="1" style="width: 10%;">已关联用户信息</th>
							<th rowspan="1" colspan="1" style="width: 10%;">医院</th>
							<th rowspan="1" colspan="1" style="width: 7%;">科室</th>
							<th rowspan="1" colspan="1" style="width: 10%;">职称</th>
							<th rowspan="1" colspan="1" style="width: 7%;">状态</th>
							<th rowspan="1" colspan="1" style="width: 10%;">操作</th>
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

<div class="modal fade" id="userModel" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-id="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="ModalLabel">讲者关联用户</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal" >
					<form id="activity_resmgr_form" method="post" action="" class="form-horizontal formBox valForm">
						<%-- <input type="hidden" name="token" value="${requestScope.token}" />
						<input type="hidden" name="activityUuid" id="activityUuid" value="${requestScope.activity.uuid}" /> --%>
						
						<div class="form-group">
							<label class="col-sm-3 control-label" >选择用户：</label>
							<div class="col-md-6">
								<select class="form-control" id="user_selector" name="userUuid">
									<!-- <option>请输入用户名或昵称或手机号码检索信息</option> -->
									<option value="null">请输入关键字检索信息</option>
								</select>
							</div>
						</div>
						<div id="user_detail_div" class="form-group">
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-3 control-label">用户昵称：</label>
								<div class="col-sm-6">
									<label id="user_nickName"></label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">用户名：</label>
								<div class="col-sm-6">
									<label id="user_name"></label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">手机号码：</label>
								<div class="col-sm-6">
									<label id="user_phoneNum"></label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">用户简介：</label>
								<div class="col-sm-6">
									<label id="user_desciption"></label>
								</div>
							</div>
						</div>
					</form>					
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id="user_association_submit" class="btn btn-sm btn-primary bfR mt20">提交</button>
			</div>
		</div>
	</div>
</div>


