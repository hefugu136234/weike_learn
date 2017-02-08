<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
		
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>Banner管理</h2>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/banner/mgr">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>更新Banner</h5>
		</div>
		<div class="ibox-content">
			<form id="update_form" class="form-horizontal formBox valForm">
				<!-- 防止重复提交token -->
				<input type="hidden" name="token" value="${requestScope.token}" />
				<input type="hidden" name="uuid" value="${requestScope.bannerVo.uuid}" />
				
				<div class="form-group">
					<label class="col-sm-3 control-label">图片标题 ：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="title" id="bannerTitle" maxlength="80" 
						value="${requestScope.bannerVo.title}" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">触发链接 ：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="refUrl" id="refUrl" maxlength="150" 
						value="${requestScope.bannerVo.refUrl}" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">选择平台 ：</label>
					<div class="col-sm-6">
						<select class="form-control" id="type_selector" required="required" name="type">
							<option value="0" ${requestScope.bannerVo.type eq 0 ? 'selected': ''}>缺省</option>
							<option value="1" ${requestScope.bannerVo.type eq 1 ? 'selected': ''}>TV平台</option>
							<option value="2" ${requestScope.bannerVo.type eq 2 ? 'selected': ''}>App</option>
							<option value="3" ${requestScope.bannerVo.type eq 3 ? 'selected': ''}>微信平台</option>
							<option value="4" ${requestScope.bannerVo.type eq 4 ? 'selected': ''}>WEB平台</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">展示区域 ：</label>
					<div class="col-sm-6">
						<select class="form-control" required="required" name="position">
							<option value="0" ${requestScope.bannerVo.position eq 0 ? 'selected': ''}>全部</option>
							<option value="1" ${requestScope.bannerVo.position eq 1 ? 'selected': ''}>首页</option>
							<option value="2" ${requestScope.bannerVo.position eq 2 ? 'selected': ''}>直播</option>
							<option value="3" ${requestScope.bannerVo.position eq 3 ? 'selected': ''}>活动</option>
							<option value="4" ${requestScope.bannerVo.position eq 4 ? 'selected': ''}>课程</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">有效期（天）：</label>
					<div class="col-sm-6">
						<select class="form-control" id="time_selector" required="required" name="validDate">
							<option value="3" <c:if test="${'3' eq requestScope.bannerVo.validDate}">selected</c:if> >3</option>
							<option value="5" <c:if test="${'5' eq requestScope.bannerVo.validDate}">selected</c:if> >5</option>
							<option value="10" <c:if test="${'10' eq requestScope.bannerVo.validDate}">selected</c:if> >10</option>
							<option value="15" <c:if test="${'15' eq requestScope.bannerVo.validDate}">selected</c:if> >15</option>
							<option value="90" <c:if test="${'90' eq requestScope.bannerVo.validDate}">selected</c:if> >90</option>
							<option value="180" <c:if test="${'180' eq requestScope.bannerVo.validDate}">selected</c:if> >180</option>
							<option value="360" <c:if test="${'360' eq requestScope.bannerVo.validDate}">selected</c:if> >360</option>
							<!-- <option value="user-defined">自定义</option> -->
						</select>
					</div>
				</div>
		
				<div class="form-group">
					<label class="col-sm-3 control-label">备注信息：</label>
					<div class="col-sm-6">
						<textarea class="form-control" name="mark" cols="60" rows="4" maxlength="149" placeholder="Banner简介(150字以内)" >
							${requestScope.bannerVo.mark}
						</textarea>
					</div>
				</div>
		
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">提交修改</button>
						<a href="/project/banner/mgr" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
$(function() {
	showActive([ 'assets-mgr', 'banner_mgr_nav' ]);
	
	//更新Banner请求
	$('#update_form').submit(function(event) {
		event.preventDefault();
		var $form = $(this);
		$.post('/project/banner/updateBanner', $form.serialize(), function() {
		}).always(function(data) {
			if ('success' == data['status']) {
				alert("更新成功");
				window.location.href = '/project/banner/mgr';
			} else {
				alert(data['message']);
			}
		});
	});
})
</script>
