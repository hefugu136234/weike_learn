<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript" src="/assets/js/admin/product/group_list.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>“${requestScope.manufacturer_name}”的产品组</h2>
		<ol class="breadcrumb">
		  <li><a href="/project/group/manufacturer/list/page">返回厂商列表</a></li>
			<li class="active">产品组列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/group/product/add/page/${requestScope.uuid}">新建产品组</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>产品组列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="group_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 20%;">产品组名称</th>
							<th rowspan="1" colspan="1" style="width: 15%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">产品组编号</th>
							<th rowspan="1" colspan="1" style="width: 25%;">所属厂商</th>
							<th rowspan="1" colspan="1" style="width: 15%;">状态</th>
							<th style="width: 15%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>

		</div>
	</div>
	<input type="hidden" id="pageUuid" value="${requestScope.uuid}"/>
</div>



<script type="text/javascript">
	var status = '${requestScope.error_info}';
	if (!!status) {
		alert(status);
	}
	
	$(function(){
		showActive([ 'group_mgr_nav', 'holder_project' ]);
		// 加载table数据
		$('#group_list_table').dataTable({
			"bProcessing" : true,
			"bServerSide" : true,
			"bStateSave" : false,
			"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
			"fnDrawCallback" : function(oSettings) {
			},
			"iDisplayLength" : 10,
			"iDisplayStart" : 0,
			"sAjaxSource" : '/project/group/product/list/data/'+'${requestScope.uuid}',
			"aoColumns" : [ {
				"mData" : "name",
				"orderable" : false
			}, {
				"mData" : "createDate",
				"orderable" : false
			}, {
				"mData" : "serialNum",
				"orderable" : false
			},{
				"mData" : "manufacturer",
				"orderable" : false,
				"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
					cutOutData(nTd, sData, 60);
				}
			}, {
				"mData" : "isStatus",
				"orderable" : false,
				"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
					statusCell(nTd, sData);
				}
			}, {
				"mData" : "uuid",
				"orderable" : false,
				"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
					loadAddLink(nTd, sData, oData['isStatus']);
				}
			} ]
		});
	});
</script>
