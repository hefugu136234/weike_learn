<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css" href="/assets/js/uploadify/uploadify.css">

<script src="/assets/ueditor/ueditor.config.js"></script>
<script src="/assets/ueditor/ueditor.all.min.js"></script>
<script src="/assets/ueditor/lang/zh-cn/zh-cn.js" charset="utf-8" ></script>
<script src="/assets/js/admin/gamesMgr/gamesMgr_viewSet.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>游戏管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/games/list/page">返回列表</a></li>
			<li class="active">展示页面配置</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/games/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>展示页面配置</h5>
		</div>
		<div class="ibox-content">
			<form id="cast_config_form" class="form-horizontal formBox valForm">
				<input type="hidden" id="lotteryUuid" value=${requestScope.uuid } />
				<textarea class="hide" id="description_hide">${requestScope.page}</textarea>
				<!-- <div class="form-group">
					<label class="col-sm-2 control-label"></label>
					<div class="col-md-8">
						<script id="page_script" name="page" required="required"
							type="text/plain" style="height: 450px;"></script>
					</div>
				</div> -->
				<script id="page_script" name="page" required="required"
							type="text/plain" style="height: 600px;" ></script>

				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="button" id="btn_submit">保存</button>
						<a href="/project/games/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>