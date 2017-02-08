<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/jstree/tree_style.min.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="/assets/js/formValidation.min.js">
	
</script>
<script type="text/javascript"
	src="/assets/js/plugins/jstree/jstree.min.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/activity/main.js?ver=1.0"></script>
<script type="text/javascript"
	src="/assets/js/admin/activity/create.js?ver=1.0"></script>
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>活动管理</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/activity/list">返回列表</a></li>
			<li class="active">新建活动</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/admin/activity/list">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>新建活动</h5>
		</div>
		<div class="ibox-content">
			<form id="activity_form" method="post" action="/admin/activity/save"
				class="form-horizontal formBox valForm">
				<input type="hidden" name="token" value="${requestScope.token}" />
				<div class="form-group">
					<label class="col-sm-3 control-label">活动名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name"
							required="required" maxlength="29"  placeholder="请填写活动名称(30字以内)"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">学科分类：</label>
					<div class="col-sm-6">
						<input type="button" class="btn btn-primary"
							id="video_category_button" value="选择分类" data-toggle="modal"
							data-target="#categorySelectorModal"> <span
							id="category_trace"></span>
					</div>
					<input type="hidden" id="categoryUuid" name="categoryUuid">
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">参与类型：</label>
					<div class="col-sm-2">
						<select name="joinType" id="joinType" class="form-control"
							required="required">
							<option value="">请选择</option>
							<option value="0" selected="selected">公开</option>
							<option value="1">需审批</option>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">人数限制：</label>
					<div class="col-sm-2">
						<select id="plimit_controller" class="form-control">
							<option value="0" selected="selected">无限制</option>
							<option value="1">设上限</option>
						</select>
					</div>
					<div class="col-sm-2" id="plimit_number" style="display: none;">
						<input class="form-control" maxlength="8" placeholder="请输入上限数量（单位：个）"
							name="plimit" id="plimit">
					</div>

				</div>

				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">开启征集作品：</label>
					<div class="col-sm-2">
						<select name="collected" id="collected" class="form-control"
							required="required">
							<option value="0">否</option>
							<option value="1" selected="selected">是</option>
						</select>
					</div>
				</div>

				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">是否征稿实名：</label>
					<div class="col-sm-2">
						<select name="authentic" id="authentic" class="form-control"
							required="required">
							<option value="0">否</option>
							<option value="1" selected="selected">是</option>
						</select>
					</div>
				</div>


				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">简介：</label>
					<div class="col-sm-6">
						<input type="mark" class="form-control" name="mark"
							required="required" maxlength="149" placeholder="活动简介(150字以内)"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">活动详情：</label>
					<div class="col-md-6">
						<textarea rows="6" class="form-control" id="description"
							maxlength="1000" placeholder="活动详情(1000字以内)" name="description" placeholder=""></textarea>
						<span style="color: red">此内容也用于弹窗显示</span>
					</div>

				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="#" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<div class="modal fade" id="categorySelectorModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">选择分类</h4>
			</div>
			<div class="modal-body">
				<div id="tree"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="confirm_btn" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
