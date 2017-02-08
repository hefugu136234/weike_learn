<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/jstree/tree_style.min.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/jstree/jstree.min.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/activity/activity_subject.js"></script>
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>活动管理</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/activity/list">返回列表</a></li>
			<li class="active">活动学科配置</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/admin/activity/list">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>
				<span style="color: red;">${requestScope.name}</span>-学科配置
			</h5>
		</div>
		<div class="ibox-content">

			<div class="form-horizontal">
				<input type="hidden" id="uuid" value="${requestScope.uuid}" />
				<div class="form-group">
					<label class="col-sm-2 control-label">新增学科：</label>
					<div class="col-sm-3">
						<input type="button" class="btn btn-primary" id="addSubject"
							value="新增学科" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-2 control-label">学科列表：</label>
					<div class="col-sm-9">
						<table id="subject_list_table"
							class="display nowrap dataTable dtr-inline" style="width: 100%"
							role="grid">
							<thead>
								<tr>
									<th rowspan="1" colspan="1" style="width: 40%;">学科的原始名称</th>
									<th rowspan="1" colspan="1" style="width: 40%;">活动学科名称</th>
									<th style="width: 20%;">操作</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>

			</div>

		</div>
	</div>
</div>

<!-- 增加学科 -->
<div class="modal fade" id="subject_modal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">活动学科设置</h4>
			</div>
			<div class="modal-body">

				<div class="form-horizontal">

					<div class="form-group">
						<label class="control-label col-md-3">学科：</label>
						<div class="col-md-8">
							<div id="subject_tree"></div>
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-md-3">活动学科名称：</label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="activity_sub_name" maxlength="30"/>
						</div>
					</div>
					
					
					<div class="form-group">
						<label class="control-label col-md-3"></label>
						<div class="col-md-8">
							<span style="color: red">注：活动学科名称是显示在微信作品提交的学科选择，默认与原学科一致。</span>
						</div>
					</div>


				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="confirm_subject" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- 增加学科 -->

<script type="text/javascript">
	$(function() {
		showActive([ 'activity-list-nav', 'activity-mgr' ]);
		var error = '${requestScope.error}';
		if (!!error) {
			alert(error);
			$('#addSubject').click(function() {
				alert('活动不存在');
			});
		} else {
			initDataSubject();
		}
	});
</script>
