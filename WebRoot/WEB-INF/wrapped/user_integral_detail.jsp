<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/admin/user_integral_detail.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>
			用户名：<font color="blue">${requestScope.user.username}</font>&nbsp;&nbsp;
			昵称：<font color="blue">${requestScope.user.nickname}</font>&nbsp;&nbsp;
			当前积分：［&nbsp;<font color="red">${requestScope.totalIntegral }</font>&nbsp;］
		</h2>
	</div>
	<div class="col-lg-2">
		<!-- <a class="btn btn-sm btn-primary bfR mt20" href="/admin/integral/superAdminChangeIntegral/">手动改分</a> -->
		<input type="button" class="btn btn-sm btn-primary bfR mt20"
			id="changeIntegral" value="积分变更">
	</div>
</div>

<input type="hidden" id="userUuid" value="${requestScope.user.uuid }">

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>用户积分明细</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="user_integral_detail_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 15%;">积分类型</th>
							<th rowspan="1" colspan="1" style="width: 10%;">日期</th>
							<th rowspan="1" colspan="1" style="width: 15%;">行为</th>
							<th rowspan="1" colspan="1" style="width: 15%;">资源</th>
							<th rowspan="1" colspan="1" style="width: 15%;">积分来源</th>
							<th rowspan="1" colspan="1" style="width: 15%;">分值</th>
							<th rowspan="1" colspan="1" style="width: 15%;">物品</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="chageIntegralModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="changeIntegralModalLabel">积分修改</h4>
			</div>

			<div class="modal-body">
				<div id="integral_input" class="form-horizontal">

					<div class="form-group">
						<label class="col-sm-2 control-label">变更方式:</label>
						<div class="col-sm-3">
							<select id="type" name="type" class="form-control">
								<option value="1">新增</option>
								<option value="-1">扣除</option>
							</select>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">输入积分:</label>
						<div class="col-sm-5">
							<input type="text" class="form-control" name="title"
								id="input_integral" required="required">
						</div>
						<span><font color="red" id="error_info"></font></span>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">备注:</label>
						<div class="col-sm-8">
							<input type="text" class="form-control" id="mark" name="mark" value="管理员修改"
								required="required">
						</div>
					</div>
					
					<div class="form-group">
					<label class="col-sm-2 control-label">验证码:</label>
					<div id="valid_controller" class="col-sm-8">
						<input type="text" id="validateCode" class="form-control" style="width: 120px;display: inline-block;" placeholder="验证码"
							name="code" required="required">
							<img id="vcode" alt="" src="" style="display: inline-block;cursor: pointer;">							
							</div>
					</div>
					
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="changIntegralCommit" type="button"
					class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
