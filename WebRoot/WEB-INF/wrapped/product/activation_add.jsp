<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link href="/assets/css/plugins/chosen/chosen.css" rel="stylesheet">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/product/activite_common.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>流量卡管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/group/active/list/page">返回列表</a></li>
			<li class="active">新建流量卡</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/group/active/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>新建流量卡</h5>
		</div>
		<div class="ibox-content">
			<form id="active_form" class="form-horizontal formBox valForm">

				<div class="form-group">
					<label class="col-sm-3 control-label">厂商：</label>
					<div class="col-sm-6">
						<select class="form-control" id="manufacturer_selector"
							name="manufacturer_uuid" onchange="changeOption(this.value);">
							<option value="0">请选择</option>
							 <c:forEach var="item" items="${requestScope.manufacturer_list}">
								<option value="${item.id}">${item.text}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">产品组：</label>
					<div class="col-sm-6">
						<select class="form-control" id="group_selector"
							name="group_uuid">
							<option value="0">请选择</option>
						</select>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">流量卡时长(天)：</label>
					<div class="col-sm-6">
						<select class="form-control" id="time_selector"
							name="time">
							<option value="0">请选择</option>
							<option value="30">30</option>
							<option value="90">90</option>
							<option value="180">180</option>
							<option value="360">360</option>
							<option value="720">720</option>
						</select>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">生产数量：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="num" id="num" />
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button  id="acvtive_submit" class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/group/active/list/page"
							class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
	function submitFrom(from) {
		var num=$('#num').val();
		if(!num){
			num=1;
		}
		var value="确定要批量成产"+num+"条激活码吗";
		if(confirm(value)){
		$('#acvtive_submit').attr('disabled','disabled');
		$.post('/project/group/active/save',$(from).serialize(),
		function(data) {
			$('#acvtive_submit').attr('disabled',false);
			if (data.status == 'success') {
				location.href = '/project/group/active/list/page';
			} else {
				alert(data.message);
			}
		});
		}
	}
</script>
