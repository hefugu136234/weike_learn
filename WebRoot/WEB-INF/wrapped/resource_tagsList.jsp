<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/css/plugins/jqcloud/jqcloud.css">
<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/css/plugins/dataTables/dataTables.responsive.css">

<script src="/assets/js/plugins/jqcloud/jqcloud-1.0.4.min.js" ></script>		
<script src="/assets/js/admin/resource_tagsCloud.js" ></script>	
	
<%-- <input type="hidden" id="tagsJson" value="${requestScope.tagsJson}" /> --%>
<input type="hidden" id="resUuid" value="${requestScope.resourceUuid}" />
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>资源标签管理</h2>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/project/resource/labeling/addPage/${requestScope.resourceUuid}">贴标签</a>
	</div>
</div>
<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<!-- <input class="btn btn-sm btn-primary bfR mt20" type="button" 
						onclick="javascript:history.back(-2);" value="返回"> -->
			<h5><font color='green'>"${requestScope.resource.name}"</font>&nbsp;的标签</h5>
		</div>
		<div class="ibox-content" id="resourceTagsList">
			<font color="red">TODO:</font><br/>
			<div id="tagsCloud" style="width:500px; margin-left:180px; height:500px;"></div>
			<%-- <font color="green">ResourceUuid:</font><span>${requestScope.resource.uuid}</span><br/> --%>
			<font color="green">TagsJson:</font><span>${requestScope.tagsJson}</span>
		</div>
	</div>
</div>
