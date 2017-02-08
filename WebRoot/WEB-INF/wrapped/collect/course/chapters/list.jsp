<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script src="/assets/js/admin/uploader_common.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/common_datatable.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/collect/course/chapters/list.js"></script>




<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>课程管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/collect/list/page">课程管理</a></li>
			<li class="active">章节列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/collect/course/chapters/add/page/${requestScope.collect.uuid }">创建章节</a>
	</div>
</div>

<input type="hidden" id="courseUuid" value="${requestScope.collect.uuid }" />

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5><font color="green">${requestScope.collect.name }&nbsp;/&nbsp;</font>章节列表</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="collect_chapters_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">通过分数</th>
							<th rowspan="1" colspan="1" style="width: 20%;">简介</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 10%;">操作</th>
						</tr>
					</thead>

				</table>
			</div>

		</div>
	</div>
</div>

<!-- 试卷 -->
<div class="modal fade" id="questionnaire_modal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">章节试题选择：</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-md-3">章节名称：</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="collect_name" readonly="readonly" />
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label">试卷名称：</label>
						<div class="col-sm-9 controls">
							<input type="text" class="form-control" name="name" id="questionnaire_name" maxlength="29"
								value="" placeholder="请输入试卷名(30字以内)"
								autocomplete="off" required="required">
						</div>
					</div>
					
					
					<div class="form-group">
						<label class="col-sm-3 control-label">试题数目：</label>
						<div class="col-sm-3 controls">
							<input type="text" class="form-control" id="collect_num" maxlength="3"/>
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-md-3">答题时间：</label>
						<div class="col-sm-3 controls">
						<input type="text" class="form-control" id="collect_time"/>
						</div>
						<label class="control-label">分钟</label>
					</div>
					
					<div class="form-group">
						<label class="control-label col-md-3">请选择试卷：</label>
						<div id="select_div" class="col-sm-9"></div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label">封面：</label>
						<div class="col-sm-3">
							<input id="cover-view" type="file" value="选择封面"/> <img class="pre-view"
								alt="" src="" />
						</div>
						<input type="hidden" name="cover" id="cover" value="" >
					</div>
					
					
					<div class="form-group">
						<label class="col-sm-3 control-label">简介：</label>
						<div class="col-sm-9">
							<textarea class="form-control" rows="5" maxlength="199" id="mark"
								placeholder="请输入简介(200字以内)" name="mark"></textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="collect_questionnaire" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- 试卷 -->

<script>
	
</script>