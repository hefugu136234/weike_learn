<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/questionnaire/questionnaire_common.js"></script>
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>问卷管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/questionnaire/list/page">返回列表</a></li>
			<li class="active">修改问卷</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/questionnaire/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>修改问卷</h5>
		</div>
		<div class="ibox-content">
			<form id="questionnaire_form" class="form-horizontal formBox valForm">
			
			<input type="hidden" name="uuid" value="${requestScope.ques_data.uuid}"/>

				<div class="form-group">
					<label class="col-sm-3 control-label">问卷名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name"
							required="required" maxlength="100" value="${requestScope.ques_data.name}"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				
				<div class="form-group">
					<label class="col-sm-3 control-label">问卷id：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="urlLink"
						id="urlLink" readonly="readonly" value="${requestScope.ques_data.urlLink}"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">重复答题：</label>
					<div class="col-sm-4">
					<label class="control-label">${requestScope.ques_data.repeat eq "1"?"是":"否"}</label>
					</div>
				</div>
				<div class="hr-line-dashed"></div>


				<div class="form-group">
					<label class="col-sm-3 control-label">问卷简介：</label>
					<div class="col-md-6">
						<textarea rows="6" class="form-control" maxlength="250"
							name="mark" placeholder="问卷简介" required="required">${requestScope.ques_data.mark}</textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit" id="save_btn">保存</button>
						<a href="/project/questionnaire/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
function submitForm(from){
	$.post('/project/questionnaire/update/data',$(from).serialize(),
			function(data){
		if (data.status == 'success') {
			window.location.href = '/project/questionnaire/list/page';
		} else {
			if (!!data.message) {
				alert(data.message)
			} else {
				alert('添加失败');
			}
		}
	});
}

</script>

