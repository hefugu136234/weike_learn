<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="/assets/js/admin/uploader_common.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/qrcode/qrcode_update.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>二维码管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/qrcode/list/page">返回列表</a></li>
			<li class="active">编辑二维码</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/qrcode/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>编辑二维码</h5>
		</div>
		<div class="ibox-content">
			<div id="qrcode_form" class="form-horizontal formBox valForm">
			
			<input type="hidden" id="uuid" value="${requestScope.data_vo.uuid}"/>

				<div class="form-group">
					<label class="col-sm-3 control-label">二维码名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="name" name="name"
							required="required" maxlength="80" placeholder="如 ：直播：信视角,便于搜索" value="${requestScope.data_vo.name}"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">二维码类型及实体：</label>
					<div class="col-sm-8">
						<label class="control-label">${requestScope.data_vo.qrType}</label>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">跳转链接：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="redictUrl"
							name="redictUrl" required="required"  value="${requestScope.data_vo.redictUrl}"/>
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-md-3">是否授权：</label>
					<div class="col-sm-6">
						<input type="checkbox" id="auth" name="auth"  <c:if test="${requestScope.data_vo.auth eq 1}">checked="checked"</c:if>/>
						
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">图文消息标题：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="title" name="title"
							 maxlength="80" value="${requestScope.data_vo.qrTitle}"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">图文消息封面：</label>
					<div class="col-sm-6">
						<input id="cover" type="file" value="消息封面" /> <input
							type="hidden" name="cover" id="cover_hidden" value="${requestScope.data_vo.qrCover}"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">图文消息简介：</label>
					<div class="col-sm-6">
						<textarea id="mark" name="mark" cols="60" rows="4" maxlength="150"
							placeholder="图文消息简介">${requestScope.data_vo.qrDesc}</textarea>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button id="submit_button" class="btn btn-primary mr20"
							type="button">保存</button>
						<a href="/project/qrcode/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
