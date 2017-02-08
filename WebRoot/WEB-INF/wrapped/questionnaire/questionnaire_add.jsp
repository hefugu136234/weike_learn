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
			<li class="active">新建问卷</li>
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
			<h5>新建问卷</h5>
		</div>
		<div class="ibox-content">
			<form id="questionnaire_form" class="form-horizontal formBox valForm">
			
			<!-- <div class="form-group">
					<label class="col-sm-3 control-label">测试问卷：</label>
					<div class="col-sm-6">
						<input type="button" class="btn btn-primary mr20" value="答题" onclick="clickWenJuanQues(event);"/>
					</div>
				</div> -->
			
			<div class="form-group">
					<label class="col-sm-3 control-label">访问问卷网：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="wenjuan_wang" placeholder="访问问卷网的链接"/>
						<span style="color:red;margin-top: 5px;margin-bottom: 10px;" >注意:当点击后未打开问卷网页面,可将以上链接复制在浏览器访问(过期后，请重新点击获取)</span>
					</div>
					<input type="button" class="btn btn-primary mr20" value="点击进入问卷网" onclick="clickWenJuan(event);"/>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">问卷名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name"
							required="required" maxlength="100" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">问卷链接：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="que_link"
							 maxlength="80" placeholder="复制问卷网问卷发布后的(收集数据)测评链接" oninput="buildId(this.value);"/>
							 <span id="que_link_error" style="color:#a94442;margin-top: 5px;margin-bottom: 10px;display:none;" >请输发布后问卷的正确链接</span>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				
				<div class="form-group">
					<label class="col-sm-3 control-label">问卷id：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="urlLink"
						id="urlLink" required="required" readonly="readonly" placeholder="当问卷链接键入正确时，自动填充"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">是否可以重复答题：</label>
					<div class="col-sm-4">
					<input id="repeat" type="checkbox" value="1" checked="checked" name="repeat"/>是
					</div>
				</div>
				<div class="hr-line-dashed"></div>


				<div class="form-group">
					<label class="col-sm-3 control-label">问卷简介：</label>
					<div class="col-md-6">
						<textarea rows="6" class="form-control" maxlength="250"
							name="mark" placeholder="问卷简介" required="required"></textarea>
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
	$.post('/project/questionnaire/add/data',$(from).serialize(),
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

function clickWenJuan(e){
	e.preventDefault();
	 $.getJSON('/project/questionnaire/wenjuan/link',{
		 timestamp:new Date().getTime() 
	 },function(data){
		if (data.status == 'success') {
			$('#wenjuan_wang').val(data.message);
			window.open(data.message);
		} else {
			alert('获取数据失败');
		}
	});
}

function clickWenJuanQues(e){
	e.preventDefault();
	$.getJSON('/project/questionnaire/wenjuan/action/test',{
		 timestamp:new Date().getTime() 
	 },function(data){
		if (data.status == 'success') {
			window.location.href=data.message;
		} else {
			alert('获取数据失败');
		}
	});
}
</script>

