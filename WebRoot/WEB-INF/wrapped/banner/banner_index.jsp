<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" type="text/css" href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet" href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css" >
	
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script src="/assets/js/plugins/blueimp/jquery.blueimp-gallery.min.js"></script>
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>Banner管理</h2>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/project/banner/addPage">新建Banner</a>
	</div>
</div>
<div class="wrapper wrapper-content animated fadeInRight">
	<ul id="myTab" class="nav nav-tabs">
		<li class="active">
			<a href="#list_all" data-toggle="tab" id="all" >首页</a>
		</li>
		<li>
			<a href="#list_wechat" data-toggle="tab" id="wechat" >微信平台Banner列表</a>
		</li>
		<li>
			<a href="#list_cloud" data-toggle="tab" id="cloud" >盒子平台Banner列表</a>
		</li>
		<li>
			<a href="#list_app" data-toggle="tab" id="app" >APP Banner列表</a>
		</li>
		<li>
			<a href="#list_web" data-toggle="tab" id="web" >WEB Banner列表</a>
		</li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="list_all">
			<jsp:include page="/WEB-INF/wrapped/banner/banner_listData_all.jsp" />
		</div>
		
		<div class="tab-pane fade" id="list_wechat">
			<jsp:include page="/WEB-INF/wrapped/banner/banner_listData_wechat.jsp" />
		</div>
		
		<div class="tab-pane fade" id="list_cloud">
			<jsp:include page="/WEB-INF/wrapped/banner/banner_listData_cloud.jsp" />
		</div>
		
		<div class="tab-pane fade" id="list_app">
			<jsp:include page="/WEB-INF/wrapped/banner/banner_listData_app.jsp" />
		</div>
		
		<div class="tab-pane fade" id="list_web">
			<jsp:include page="/WEB-INF/wrapped/banner/banner_listData_web.jsp" />
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


