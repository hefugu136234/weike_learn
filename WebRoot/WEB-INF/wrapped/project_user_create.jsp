<%@page import="org.springframework.web.util.HtmlUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js">
	
	
</script>
<script type="text/javascript" src="/assets/js/formValidation.min.js">
</script>
<script type="text/javascript" src="/assets/js/formvalidation.js">
<!--

//-->
</script>

<style>
<!--
-->
</style>
<%
	String token = (String) request.getAttribute("token");
	String status = (String) request.getAttribute("status");
%>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>新建用户</h2>
		<ol class="breadcrumb">
			<li>用户管理</li>
            <li class="active">
                <strong>新建用户</strong>
            </li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/userReference/list">返回</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>新建用户</h5>
		</div>
		<div class="ibox-content" style="display: block;">
			<form id="new_project_user_form" method="post"
				action="/project/new/user/save" class="form-horizontal">
				<input type="hidden" name="token" value="<%=token%>">
				<div class="form-group">
					<label class="col-sm-2 control-label">用户名</label>
					<div class="col-sm-5 controls">
						<input type="text" class="form-control" name="username"
							autocomplete="off" required="required" maxlength="29" placeholder="用户名(登陆必要字段,30字以内)">
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-2 control-label">昵称</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" name="nickname" 
							maxlength="29" placeholder="昵称(30字以内)">
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
					<label class="col-sm-2 control-label">角色分配</label>
					<div class="col-sm-5">
						<select class="form-control role-select" id="selectRole"
							name="roleName">
						</select>
						
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-2 control-label">简介</label>
					<div class="col-sm-5">
						<textarea class="form-control" rows="3" 
						maxlength="99" placeholder="用户简介(100字以内)"
							name="mark"></textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<!-- <div class="form-group">
					<div class="col-sm-4 col-sm-offset-2">
						<button class="btn btn-white" type="button">取消</button>
						<button class="btn btn-primary" type="submit">保存</button>
					</div>
				</div> -->
				
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/userReference/list" class="btn btn-warning">取消</a>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
			</form>
		</div>
	</div>
</div>


<script>
	$(document).ready(function() {		
		showActive([ 'holder_project', 'pro_new_user_nav' ]);
		$.post('/project/user/getRole',{},function(data){
			var parent = $("#selectRole");
			parent.empty();
			var item = '';
			for(var i=0;i<data.length;i++){
				item = $('<option value='+data[i].roleName+'>'+data[i].roleDesc+'</option>');
				parent.append(item);
			}});
		
		$('#new_project_user_form').formValidation({
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
		<%if (status != null) {%>
		alert('<%=status%>');
<%}%>
	})
</script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<link href="/assets/css/plugins/chosen/chosen.css" rel="stylesheet">