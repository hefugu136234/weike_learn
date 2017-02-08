<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="/assets/js/formValidation.min.js"></script>
<script type="text/javascript" src="/assets/js/formvalidation.js"></script>
<%
	String token = (String) request.getAttribute("token");
	String status = (String) request.getAttribute("status");
%>
<div class="ibox float-e-margins">
	<div class="ibox-title">
		<h5>创建用户</h5>
	</div>
	<div class="ibox-content" style="display: block;">
		<form id="new_global_user_form" method="post"
			action="/admin/user/save" class="form-horizontal">
			<input type="hidden" name="token" value="<%=token%>">
			<div class="form-group">
				<label class="col-sm-2 control-label">用户名</label>
				<div class="col-sm-5 controls">
					<input type="text" class="form-control" name="username"
						autocomplete="off" required="">
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			<div class="form-group">
				<label class="col-sm-2 control-label">昵称</label>
				<div class="col-sm-5">
					<input type="text" class="form-control" name="nickname">
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			<div class="form-group">
				<label class="col-sm-2 control-label">密码</label>
				<div class="col-sm-5">
					<input type="password" class="form-control" name="password"
						id="password" autocomplete="off" required="">
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			<div class="form-group">
				<label class="col-sm-2 control-label">重复密码</label>
				<div class="col-sm-5">
					<input type="password" class="form-control" id="confirmPassword"
						name="confirmPassword" autocomplete="off" required="">
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			<div class="form-group">
				<label class="col-sm-2 control-label">简介</label>
				<div class="col-sm-5">
					<textarea class="form-control" rows="3" placeholder="请输入简介"
						name="mark"></textarea>
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
	$(document).ready(function() {
		showActive([ 'admin_user_mgr', 'admin_user_create_nav' ]);

		$('#new_global_user_form').formValidation({
		    framework: 'bootstrap',
		    fields: {
		        confirmPassword: {
		            validators: {
		                identical: {
		                    field: 'password',
		                    message: 'The password and its confirm are not the same'
		                }
		            }
		        }
		    }
		});

	})
	<%if (status != null) {%>
		alert('<%=status%>');
<%}%>

</script>
