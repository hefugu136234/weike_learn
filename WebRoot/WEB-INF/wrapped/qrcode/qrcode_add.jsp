<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="/assets/js/admin/uploader_common.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/qrcode/qrcode_common.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>二维码管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/qrcode/list/page">返回列表</a></li>
			<li class="active">新建二维码</li>
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
			<h5>新建二维码</h5>
		</div>
		<div class="ibox-content">
			<form id="qrcode_form" class="form-horizontal formBox valForm">

				<div class="form-group">
					<label class="col-sm-3 control-label">二维码名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="name" name="name"
							required="required" maxlength="80" placeholder="如 ：直播：信视角,便于搜索"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">二维码类型：</label>
					<div class="col-sm-3">
						<select class="form-control" id="qrcode_type" name="type">
							<option value="">请选择</option>
							<option value="1">活动</option>
							<option value="3">直播</option>
							<option value="6">资源</option>
							<option value="7">游戏</option>
							<option value="4">URL</option>
						</select>
					</div>
				</div>

				<div id="contrl" class="hide">

					<div id="select_obj_div" class="form-group">
						<!-- 资源活动选择 -->
						<label id="obj_label" class="control-label col-md-3"></label>
						<div id="select_obj" class="col-sm-6"></div>
					</div>

					<div class="form-group">
						<!-- 说明-->
						<label class="control-label col-md-3"></label>
						<div id="select_obj" class="col-sm-9">
							<span style="color: red;">注：当选中一个对应资源实体（活动、直播、资源），跳转链接和授权为系统默认，不要更改。</span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label">跳转链接：</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="redictUrl" name="redictUrl"
								required="required" />
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-md-3">是否授权：</label>
						<div class="col-sm-6">
							<input type="checkbox" id="auth" name="auth" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label">图文消息标题：</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="title" name="title"
							 maxlength="80" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label">图文消息封面：</label>
						<div class="col-sm-6">
							<input id="cover" type="file" value="消息封面" /> <input
								type="hidden" name="cover" id="cover_hidden" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label">图文消息简介：</label>
						<div class="col-sm-6">
							<textarea id="mark" name="mark" cols="60" rows="4" maxlength="150"
								placeholder="图文消息简介"></textarea>
						</div>
					</div>

				</div>



				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button id="submit_button" class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/qrcode/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
