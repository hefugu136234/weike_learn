<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>

<script type="text/javascript" src="/assets/js/admin/project_list.js"></script>
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">

<style>
<!--
#data-container{
width: 100%;
text-align: center;
}

#data-container table{
width: 100%
} 

#data-container table thead{
font-weight: bold;
} 

#data-container table tbody tr{
height: 32px;
} 

-->
</style>

<div id="list_wrapper" class="dataTables_wrapper">
	<table id="project_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
		<thead>
			<tr>
				<th rowspan="1" colspan="1" style="width: 114px;">项目名</th>
				<th rowspan="1" colspan="1" style="width: 130px;">创建时间</th>
				<th rowspan="1" colspan="1" style="width: 120px;">用途</th>
				<th rowspan="1" colspan="1" style="width: 180px;">备注</th>
				<th style="width: 98px;">操作</th>
			</tr>
		</thead>

	</table>
</div>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">

<div class="modal fade" id="tv_home_mgr" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true" data-id="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="project_label">TV首页板块管理</h4>
			</div>
			<div class="modal-body" style="min-height: 300px">
				<div class="form-horizontal" name="commentform">
					<div class="form-group">
						<label class="control-label col-md-4" for="label_name">板块名称：</label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="label_name"
								 placeholder="" />
						</div>
						<button id="save-btn" type="button" class="btn btn-primary">保存</button>
					</div>
					<div class="hr-line-dashed"></div>
				</div>
				<div id="data-container">				
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<!-- 	<button id="confirm_btn" type="button" class="btn btn-primary">确定</button> -->
			</div>
		</div>
	</div>
</div>