<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" 
	href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
	
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js" ></script>	
<script src="/assets/js/admin/tag/tagChild_list.js" ></script>
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>标签管理</h2>
		<ol class="breadcrumb">
			<li class="active">标签列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/tag/addChildTagPage/${requestScope.parent_id}/${requestScope.parent_uuid}">新建子标签</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<a class="btn btn-sm btn-primary bfR mt20" href="/tag/showParentTagPag">返回上层</a>
			<h5><font color="green">"${requestScope.parent_name}"</font>&nbsp;的子标签</h5>
		</div>
		<div class="ibox-content">
			<div id="tagList_second" class="dataTables_wrapper">
				<table id="tagTable_second" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 20%;">标签名</th>
							<th rowspan="1" colspan="1" style="width: 15%;">创建时间</th>	
							<th rowspan="1" colspan="1" style="width: 15%;">更新时间</th>
							<th rowspan="1" colspan="1" style="width: 20%;">备注</th>
							<th style="width: 15%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>

<input type="hidden" id="parent_uuid" value="${requestScope.parent_uuid}" />
<!-- 信息完整显示模态框 -->
<div class="modal fade" id="dataModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">该单元格完整信息如下：</h4>
			</div>
			<div class="modal-body">
				<div id="dataDetail" ></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
