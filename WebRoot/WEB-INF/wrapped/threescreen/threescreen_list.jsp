<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css" >
<link rel="stylesheet" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
	
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/admin/threescreen/threescree_list.js"></script>
<script src="/assets/js/plugins/blueimp/jquery.blueimp-gallery.min.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>三分屏管理</h2>
		<ol class="breadcrumb">
			<li class="active">三分屏列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/threescreen/page/add">新建三分屏信息</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>三分屏列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="three_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 15%;">三分屏名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">分类</th>
							<th rowspan="1" colspan="1" style="width: 20%;">封面</th>
							<th rowspan="1" colspan="1" style="width: 15%;">讲者</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 20%;">操作</th>
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
	var status = '${requestScope.threeScreen_error}';
	if (!!status) {
		alert(status);
	}
</script>
