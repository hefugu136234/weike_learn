<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/css/plugins/dataTables/dataTables.responsive.css">

<script src="/assets/js/admin/resource_tags.js" ></script>
<style>
<!--
#parentTagsTable tbody tr:hover td {
	background: #E2E4E5;
	cursor: pointer;
}
#childTagsTable tbody tr:hover td {
	background: #E2E4E5;
	cursor: pointer;
}
#parentTagsTable td,th {
	text-align: center;
}
#childTagsTable td,th {
	text-align: center;
}
-->
</style>

<input type="hidden" id="resourceUuid" value="${requestScope.resourceUuid}" />

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>资源标签管理</h2>
	</div>
	<div class="col-lg-2">
		<input type="button" class="btn btn-sm btn-primary bfR mt20" onclick="javascript:history.back(-1);" value="返回" >
	</div>
</div>
<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>资源添加标签</h5>
		</div>
		<div class="ibox-content" id="resourceTagsAdd">
			<div id="parentTags" style="width:300px; float:left; height:500px; overflow:scroll; border: 1px gray solid;" >
				<table id="parentTagsTable" class="table">
					<thead>
						<tr>
							<th>名称</th>
							<th>修改日期</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div id="childTags" style="width:500px; margin-left:400px; height:500px; overflow:scroll; border: 1px gray solid;">
				<table id="childTagsTable" class="table">
					<thead>
						<tr>
							<th>选择</th>
							<th>名称</th>
							<th>修改日期</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				<!-- <input type="checkbox" id="all">  -->
				<!-- <input type="button" value="全选" class="btn btn-sm btn-primary bfR mt20" id="selectAll">   
				<input type="button" value="全不选" class="btn btn-sm btn-primary bfR mt20" id="unSelect">   
				<input type="button" value="反选" class="btn btn-sm btn-primary bfR mt20" id="reverse">    -->
				<!-- <input type="button" value="获得选中的所有值" class="btn" id="getValue"> -->
			</div>
			<input class="btn btn-sm btn-primary mt20" type="button" id="checkboxSubmitButton"value="提交">
		</div>
	</div>
</div>