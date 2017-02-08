<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.lankr.tv_cloud.vo.ProductGroupVo"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link href="/assets/css/plugins/chosen/chosen.css" rel="stylesheet">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/product/group_common.js"></script>
	<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
	

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>产品组管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/group/product/list/page/${requestScope.pageUuid}">返回列表</a></li>
			<li class="active">修改产品组</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/group/product/list/page/${requestScope.pageUuid}">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>修改产品组</h5>
		</div>
		<div class="ibox-content">
			<form id="group_form" class="form-horizontal formBox valForm">
				<input type="hidden" name="token" value="${requestScope.token}" />
				<input type="hidden" name="uuid" value="${requestScope.uuid}" />

				<div class="form-group">
					<label class="col-sm-3 control-label">产品组名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name"
							required="required" maxlength="80" value="${requestScope.productGroup.name}" placeholder="产品组名称(80字以内)"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">产品组编号：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="serialNum"
							required="required" maxlength="4" value="${requestScope.productGroup.serialNum}" placeholder="产品组编号(长度不超过4位数)"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">厂商关联：</label>
					<div class="col-sm-6">
						<select class="form-control" id="manufacturer_selector"
								name="manufacturer_uuid">
								<!-- <option value="0">请选择</option> -->
								<c:if test="${not empty requestScope.productGroup.manufacturerUuid}">
								<option value="${requestScope.productGroup.manufacturerUuid}">${requestScope.productGroup.manufacturer}</option>
								</c:if>
							</select>
					</div>
				</div>

		

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/group/product/list/page/${requestScope.pageUuid}"
							class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
	<input type="hidden" id="pageUuid" value="${requestScope.pageUuid}"/>
</div>

<script type="text/javascript">
/* $(function(){
	var manufacturerUuid='${requestScope.productGroup.manufacturerUuid}';
	if(!!manufacturerUuid){
		$('#manufacturer_selector').select(manufacturerUuid);
	}
}); */
function submitFrom(from) {
	$.post('/project/group/product/update/data',$(from).serialize(),
	function(data) {
		if (data.status == 'success') {
			location.href = '/project/group/product/list/page/'+$('#pageUuid').val();
		} else {
			alert(data.message);
		}
	});
}
</script>
