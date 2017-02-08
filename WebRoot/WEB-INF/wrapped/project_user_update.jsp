<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<script src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script src="/assets/js/formValidation.min.js"></script>
<script src="/assets/js/formvalidation.js">
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/admin/project_user_update.js"></script>

<link href="/assets/css/plugins/chosen/chosen.css" rel="stylesheet">


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>编辑用户</h2>
		<ol class="breadcrumb">
			<li>用户管理</li>
            <li class="active">
                <strong>编辑用户</strong>
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
			<h5>更新用户信息</h5>
		</div>
		<div class="ibox-content" style="display: block;">
			
			<input type="hidden" id="userUuid" value="${requestScope.userUuid}" />
		
			<form id="project_user_update" method="post" action="/project/new/user/save" class="form-horizontal">
				<h3><font color="green">用户类型信息</font></h3>
				<div id="oups_type_div" class="form-group">
					<label class="col-sm-2 control-label">用户类型：</label>
					<div class="col-sm-5">
						<select id="user_type" name="userType" class="form-control">
							<option value=0 >医生用户</option>
							<option value=1 >企业用户</option>
						</select>
					</div>
				</div>
				
				<div id="doctor_detail" class="hide">
					<div class="form-group">
						<label class="col-sm-2 control-label">所在省份：</label>
						<div class="col-sm-5">
							<select class="form-control" id="user_province" name="provinceUuid"
								required="required" onchange="changeProvince(this.value);">
								<option value="0">请选择</option>
							</select>
						</div>
					</div>
				
					<div class="form-group">
						<label class="col-sm-2 control-label">所在城市：</label>
						<div class="col-sm-5">
							<select class="form-control" id="user_city" name="cityUuid"
								required="required" onchange="changeCity(this.value);">
								<option value="0">请选择</option>
							</select>
						</div>
					</div>
	
					<div class="form-group">
						<label class="col-sm-2 control-label">所在医院：</label>
						<div class="col-sm-5">
							<select class="form-control" id="user_hospital" name="hospitalUuid"
								required="required">
								<option value="0">请选择</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label">所属科室：</label>
						<div class="col-sm-5">
							<select class="form-control" id="user_departments" name="departmentUuid"
								required="required">
								<option value="0">请选择</option>
							</select>
						</div>
					</div>
				</div>
				
				<div id="company_detail" class="hide" >
					<div class="form-group">
						<label class="col-sm-2 control-label">企业名称：</label>
						<div class="col-sm-5">
							<select class="form-control" id="user_company" name="companyUuid"
								required="required" >
								<option value="0">请选择</option>
							</select>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">职称：</label>
					<div class="col-sm-5">
						<input type="text" id="user_professor" name="professor" class="form-control" required="required"/>
					</div>
				</div>
				
				<div class="hr-line-dashed"></div>
				
				<h3><font color="green">用户基本信息</font></h3>
				<div class="form-group">
					<label class="col-sm-2 control-label">用户名：</label>
					<div class="col-sm-5 controls">
						<input type="text" class="form-control" id="user_name" name="userName"
							autocomplete="off" required="required">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">昵称：</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="user_nickname" name="nickName"
						required="required">
					</div>
				</div>
				
				<div class="form-group" id="realname">
					<label class="col-sm-2 control-label">真实姓名：</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="user_realName" name="realName" >
					</div>
				</div>
				
				<div class="form-group" id="select_sex">
					<label class="col-sm-2 control-label">性别：</label>
					<div class="col-sm-5">
						<select id="user_sex" name="sex" class="form-control" >
							<option value="">请选择</option>
							<option value="1">男</option>
							<option value="0">女</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">角色：</label>
					<div class="col-sm-5">
						<select class="form-control role-select" id="user_role" name="userRole">
							<option value="0">请选择</option>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<!-- <div class="form-group">
					<label class="col-sm-2 control-label">重置密码：</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" name="user_password"
							id="password" autocomplete="off" required="">
					</div>
				</div>
				<div class="hr-line-dashed"></div> -->
				
				<div class="form-group">
					<label class="col-sm-2 control-label">简介：</label>
					<div class="col-sm-5">
						<textarea class="form-control" rows="4" placeholder="请输入简介" id="user_mark"
							name="mark"></textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
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
