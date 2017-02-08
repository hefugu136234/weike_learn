<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css"
	rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript" src="/assets/js/admin/product/manufacturer_list.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/blueimp/jquery.blueimp-gallery.min.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>厂商管理</h2>
		<ol class="breadcrumb">
			<li class="active">厂商列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/group/manufacturer/add/page">新建厂商</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>厂商列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="manufacturer_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">厂商名称</th>
							<th rowspan="1" colspan="1" style="width: 15%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 8%;">厂商编号</th>
							<th rowspan="1" colspan="1" style="width: 20%;">厂商地址</th>
							<th rowspan="1" colspan="1" style="width: 10%;">图标</th>
							<th rowspan="1" colspan="1" style="width: 10%;">关联产品组</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 15%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>

		</div>
	</div>
</div>

<!-- The Gallery as lightbox dialog, should be a child element of the document body -->
<div id="blueimp-gallery" class="blueimp-gallery">
	<div class="slides"></div>
	<h3 class="title"></h3>
	<a class="prev">‹</a> <a class="next">›</a> <a class="close">×</a> <a
		class="play-pause"></a>
	<ol class="indicator"></ol>
</div>

<script type="text/javascript">
	var status = '${requestScope.error_info}';
	if (!!status) {
		alert(status);
	}
</script>
