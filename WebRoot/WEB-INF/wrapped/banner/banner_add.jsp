<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/js/uploadify/uploadify.css">
<script src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script src="/assets/js/admin/banner/banner_save.js"></script>	
	
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
			<h5>新建Banner</h5>
		</div>
		<div class="ibox-content">
			<form id="submit_form" class="form-horizontal formBox valForm">
				<!-- 防止重复提交token -->
				<input type="hidden" name="token" value="${requestScope.token}" />
				<!-- 七牛上传token -->
				<input type="hidden" id="signature" name="signature" value="${requestScope.signature}" />
				
				<input type="hidden" id="uploadTage" name="uploadTage" value="" />
				
				<div class="form-group">
					<label class="col-sm-3 control-label">图片标题 ：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="title" id="bannerTitle" required="required" maxlength="80" />
					</div>
				</div>
		
				<div class="form-group">
					<label class="col-sm-3 control-label">添加图片 ：</label>
					<div class="col-sm-6">
						<input id="bannerImg_uploadify" type="file"> 
						<span id="bannerUploadStatus" style="display: none; color: red; margin-left: 10px;"></span>
						<!-- <img id="bannerImgPreview" alt="" src="" style="display: none;"> -->
						<input type="hidden" name="bannerSize" id="bannerSize" /> 
						<input type="hidden" name="bannerNum" id="bannerNum" /> 
						<input type="hidden" name="bannerTaskId" id="bannerTaskId">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">图片名称 ：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name" id="bannerName" required="required" maxlength="80" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">触发链接 ：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="refUrl" id="refUrl"  maxlength="150" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">选择平台 ：</label>
					<div class="col-sm-6">
						<select class="form-control" id="type_selector" required="required" name="type">
							<option value="0">缺省</option>
							<option value="1">TV平台</option>
							<option value="2">App</option>
							<option value="3">微信平台</option>
							<option value="4">WEB平台</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">展示区域 ：</label>
					<div class="col-sm-6">
						<select class="form-control" required="required" name="position">
							<option value="0">全部</option>
							<option value="1">首页</option>
							<option value="2">直播</option>
							<option value="3">活动</option>
							<option value="4">课程</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">有效期（天）：</label>
					<div class="col-sm-6">
						<select class="form-control" id="time_selector" required="required" name="validDate">
							<option value="3">3</option>
							<option value="5">5</option>
							<option value="10">10</option>
							<option value="15">15</option>
							<option value="90">90</option>
							<option value="180">180</option>
							<option value="360">360</option>
							<!-- <option value="user-defined">自定义</option> -->
						</select>
					</div>
				</div>
		
				<div class="form-group">
					<label class="col-sm-3 control-label">备注信息：</label>
					<div class="col-sm-6">
						<textarea class="form-control" name="mark" cols="60" rows="4" maxlength="149" placeholder="Banner简介(150字以内)"></textarea>
					</div>
				</div>
		
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/banner/mgr" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
