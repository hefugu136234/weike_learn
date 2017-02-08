<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>

<!-- <script type="text/javascript" src="/assets/js/admin/vote.min.js"></script> -->
<script type="text/javascript" src="/assets/js/admin/vote_developer.js"></script>
<style>
.radio input[type=radio] {
	margin-top: 10px;
}

.answer_table {
	border-collapse: separate;
	border-spacing: 0.5em;
}

#vote_container {
	width: 70%;
	margin-left: auto;
	margin-right: auto;
}

#vote_container li {
	margin-top: 10px;
}

#vote_container li:HOVER {
	background-color: #f3f3f4;
}
}
</style>
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>资源的投票管理</h2>
		<ol class="breadcrumb">
			<li><a href="/asset/video/mgr">返回列表</a></li>
		</ol>
	</div>
	<div class="col-lg-2">
		<!-- <a class="btn btn-sm btn-primary bfR mt20" href="/asset/video/mgr">返回列表</a> -->
		<a class="btn btn-sm btn-primary bfR mt20" href="/asset/category/mgr">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>投票管理</h5>
		</div>
		<span id="edit_forbidden_tips" style="color: red; display: none;">已经上线的资源不能修改投票，如需修改请先将资源下线。</span>
		<div class="ibox-content">
			<form id="answer_form" class="form-horizontal formBox valForm">
				<input type="hidden" name="res_uuid" id="res_uuid"
					value="${requestScope.resource.uuid}" />
				<div class="form-group">
					<label class="col-sm-3 control-label">视频名称：</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="video_name"
							value="${requestScope.resource.name}" readonly="readonly" />
					</div>
				</div>
				<div id="vote_container">
					<ul class="list-group">
					</ul>
					<button class="btn btn-primary mr20" id="new_vote_controller"
						style="min-width: 80px;" type="button">新增一题</button>
				</div>
			</form>
		</div>
	</div>
</div>
<div style="display: none;" id="vote_seed">
	<div class="vote-item-clean">
		<div class="vote-wrapped">
			<div class="form-group">
				<label class="col-sm-2 control-label">标题：</label>
				<div class="col-sm-6">
					<input type="text" class="form-control vote-title"
						placeholder="请输入投票标题">
				</div>
				<a class="vote-del">删除</a>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">题型：</label>
				<div class="col-sm-2">
					<select class="form-control vote-type">
						<option value="1">单选</option>
						<option value="2">最多选2项</option>
						<option value="3">最多选3项</option>
						<option value="0">不限制</option>
					</select>
				</div>
			</div>
			<div class="vote-options-container" style="margin-left: 20px">
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-6 col-sm-offset-3">
				<button class="btn btn-primary mr20 save-vote-btn" type="button">保存</button>
			</div>
		</div>
	</div>
</div>
<script>
var votes = ${requestScope.votes}
var editable = ${requestScope.resource.status} != 1
</script>