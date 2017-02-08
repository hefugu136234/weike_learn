<%@page import="com.lankr.tv_cloud.model.Category"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/layout_list.js?ver=1.0"></script>
	<style>
<!--
.item-op a {
	margin-left: 10px;
}
-->
</style>
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>布局管理</h2>
		<ol class="breadcrumb">
			<li class="active">布局列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a id="layout_new_btn" class="btn btn-sm btn-primary bfR mt20"
			href="#">新建布局</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>
				TV页面定制 > <a href="/tv/home/settings">首页</a>
			</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="layout_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;" >
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 20%;">版块</th>
							<th rowspan="1" colspan="1" style="width: 15%;">编辑时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建人</th>
							<th rowspan="1" colspan="1" style="width: 25%;">备注</th>
							<th rowspan="1" colspan="1" style="width: 15%;">状态</th>
							<th style="width: 15%;">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${requestScope.layouts }" var="layout">
							<tr class="layout-item" data-id="${layout.uuid }" data-status="${layout.status }">
								<td>${fn:escapeXml(layout.name)}</td>
								<td><fmt:formatDate value="${layout.modifyDate }"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${layout.user.username }</td>
								<td>${fn:escapeXml(layout.mark) }</td>
								<td class="item-status"></td>
								<td class="item-op"></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<script type="text/javascript">
				$(document).ready(function() {					
					var g = $('.ibox-title h5');
					var n = $('#layout_new_btn')
					<%Category c = (Category) request.getAttribute("category");
			if (c == null) {%>
							alert('布局无效');
							window.location.href = "/tv/home/settings";					
					<%}
			String html = "";
			if (c != null) {
				html = "> <span>" + c.getName() + "</span>";%>
						n.click(function(e){
							e.preventDefault();
							window.location.href = '/tv/layout/<%=c.getUuid()%>/create';
						})
						<%c = c.getParent();
			}
			while (c != null) {
				html = "> <a href=\"/tv/layout/" + c.getUuid() + "/list\">"
						+ c.getName() + "</a>" + html;
				c = c.getParent();
			}%>
					g.append('<%=html%>')
			<%%>
				})
					
			</script>
		</div>
	</div>
</div>


