<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css"
	rel="stylesheet">
<script src="/assets/js/plugins/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/blueimp/jquery.blueimp-gallery.min.js"></script>
<script type="text/javascript" src="/assets/js/admin/news_list.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>文章管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/news/list/page">文章管理</a></li>
			<li class="active">文章列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/news/add/page">创建文章</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>文章列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="news_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 25%;">文章标题</th>
							<th rowspan="1" colspan="1" style="width: 10%;">文章分类</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 15%;">标签</th>
							<th rowspan="1" colspan="1" style="width: 20%;">简介图</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 10%;">操作</th>
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
var news_info="${requestScope.news_info}";
	if (!!news_info) {
		alert(news_info);
		news_info = '';
	}
</script>
