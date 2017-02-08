<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/questionnaire/questionnaire_list.js"></script>
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>问卷管理</h2>
		<ol class="breadcrumb">
			<li class="active">问卷列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/project/questionnaire/add/page">新建问卷</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>
				问卷列表
			</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="display nowrap dataTable dtr-inline">
				<table id="questionnaire_list_table" 
					class="display nowrap dataTable dtr-inline" style="width: 100%"
					role="grid">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 40%;">名称</th>
							<th rowspan="1" colspan="1" style="width: 20%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 30%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>

		</div>
	</div>
</div>

<script type="text/javascript">
var error = '${requestScope.error}';
if (!!error) {
	alert(error);
}
</script>
