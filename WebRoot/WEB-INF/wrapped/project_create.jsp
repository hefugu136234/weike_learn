<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>




<script src="/assets/js/admin/project.js" type="text/javascript"
	charset="utf-8"></script>
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js">
</script>
<style>
<!--
#user_selector {
	border: 1px gray solid;
	height: 280px;
	display: none;
	position: absolute; z-index : 999;
	background: #FAFAFA;
	z-index: 999;
}

#user_loader_table {
	margin-top: 18px;
	width: 100%;
}

#user_loader_table td,th {
	text-align: center;
}

#user_loader_table tbody tr:hover td {
	background: #E2E4E5;
	cursor: pointer;
}

-->
</style>
<%
	String message = (String) request.getAttribute("message");
	String token = (String) request.getAttribute("token");
%>
<div class="ibox float-e-margins">
	<div class="ibox-title">
		<h5>创建新项目</h5>
	</div>
	<div class="ibox-content" style="display: block;">
		<form id="new_project_form" method="post" action="/admin/project/save"
			class="form-horizontal">
			<input type="hidden" name="token" value="<%=token%>">
			<div class="form-group">
				<label class="col-sm-2 control-label">项目名称</label>
				<div class="col-sm-5 controls">
					<input type="text" class="form-control" name="project_name"
						autocomplete="off" required="">
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			<div class="form-group">
				<label class="col-sm-2 control-label">项目管理员</label>
				<div class="col-sm-5">
					<input type="text" class="form-control" name="username"
						id="user_value" autocomplete="off" required="">
					<div id="user_selector" class="col-sm-5">
						<a href="javascript:void(0);" style="float: right"
							id="user_select_close">关闭</a>
						<table id="user_loader_table" class="table">
							<thead>
								<tr>
									<th>用户名</th>
									<th>昵称</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>

			</div>
			<div class="hr-line-dashed"></div>
			<div class="form-group">
				<label class="col-sm-2 control-label">使用领域</label>
				<div class="col-sm-5">
					<input type="text" class="form-control" name="apply">
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			<div class="form-group">
				<label class="col-sm-2 control-label">项目简介</label>
				<div class="col-sm-5">
					<textarea class="form-control" rows="3" placeholder="请输入项目简介"
						name="project_desc"></textarea>
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			<div class="form-group">
				<div class="col-sm-4 col-sm-offset-2">
					<button class="btn btn-white" type="button">取消</button>
					<button class="btn btn-primary" type="submit">保存</button>
				</div>
			</div>
		</form>
	</div>
</div>
<script>
<%if (message != null) {%>alert('<%=message%>' + "");
<%}%>
</script>