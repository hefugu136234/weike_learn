<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">

<script src="/assets/js/admin/tag/tagParent_save.js"></script>	
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>标签管理</h2>
		<ol class="breadcrumb">
			<li><a href="/tag/showParentTagPag">返回列表</a></li>
			<li class="active">编辑标签类别</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/tag/showParentTagPag">返回标签类别列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>编辑标签类别</h5>
		</div>
		<div class="ibox-content">
			<form id="parentTag_update" class="form-horizontal formBox valForm">
				<!-- 防止重复提交token -->
				<input type="hidden" name="token" value="${requestScope.token}" />
				<input type="hidden" name="uuid" value="${requestScope.parentVo.uuid}" />
				
				<div class="form-group">
					<label class="col-sm-3 control-label">类别名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name" id="tag_name" required="required" maxlength="49" 
									placeholder="类别名称(50字以内)" value="${requestScope.parentVo.name}" />
					</div>
				</div>
					
				<div class="form-group">
					<label class="col-sm-3 control-label">备注信息：</label>
					<div class="col-sm-6">
						<textarea class="form-control" name="mark" cols="60" rows="4" maxlength="99" placeholder="该分类的简介(非必须,100字以内)" >${requestScope.parentVo.mark}</textarea>
					</div>
				</div>
		
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/tag/showParentTagPag" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function() {
	//更新Banner请求
	$('#parentTag_update').submit(function(event) {
		event.preventDefault();
		var $form = $(this);
		$.post('/tag/updateParentTag', $form.serialize(), function() {
		}).always(function(data) {
			if ('success' == data['status']) {
				alert("更新成功");
				window.location.href = '/tag/showParentTagPag';
			} else {
				alert(data['message']);
			}
		});
	});
})
</script>
