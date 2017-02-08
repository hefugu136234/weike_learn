<%@page import="com.lankr.tv_cloud.model.Category"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css"
	href="/assets/css/jquery.gridster.css">
<link rel="stylesheet" type="text/css" href="/assets/css/widget_ui.css">

<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/jstree/jstree.min.js"></script>

<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/jstree/tree_style.min.css">
<script src="/assets/js/jquery.gridster.js" type="text/javascript"
	charset="utf-8"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>

<script src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<script type="text/javascript" src="/assets/js/admin/uploader_common.js"></script>
<script type="text/javascript" src="/assets/js/admin/tv_setting_v2.js"></script>

<style type="text/css">

.gs_remove_handle {
	float: right;
	margin-right: 2px;
	cursor: pointer;
}

.gs_setting {
	margin-top: 20px;
	color: black;
}

.res_detail {
	margin-top: 20px;
	color: black;
}

.child-setting {
	position: absolute;
	top: 0;
	left: 0;
	height: 20px;
	line-height: 20px;
	color: black;
	background: white;
	display: none;
}

.loading {
	display: none;
}

#image-preview {
	max-height: 300px;
	display: none
}

.widget-size {
	position: absolute;
	left: 50%;
	margin-left:-40px;
	top:50%;
	margin-top:-10px;
	height: 20px;
	line-height: 20px;
	background-color: white;
	color: black;
	display: none;
}

.pre-view {
	border: 1px gray solid;
	max-width: 280px;
	max-height: 280px;
}

.gs_setting {
	position: absolute;
	bottom: 0;
	left: 0;
	height: 20px;
	line-height: 20px;
	color: black;
	background: white;
}


#selector_container {
	display: none;
}

.ref-controller {
	display: none;
}
.widget-item{
cursor: move;
}
</style>
<script type="text/javascript">
	var error = '${requestScope.error}';
	if (!!error) {
		alert(error);
	}
	Layout.prototype.pool = [];
	Layout.prototype.get = function(uuid){
		var g;
		for(var i = 0 ; i < Layout.prototype.pool.length ; i++){
			if(Layout.prototype.pool[i].uuid == uuid){
				g = Layout.prototype.pool[i];
				break;
			}
		}
		return g;
	}
</script>
<div class="wrapper wrapper-content animated fadeInRight">
	<div class="row">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title" >
					<%-- <h5 id="title" ><span>TV页面定制 &gt; <a href="/project/tv/home/settings/v2">首页</a> 
								${not empty requestScope.path?requestScope.path:''}</span></h5> --%>
					<h5 id="title" ><span>TV页面定制 &gt; <a href="/project/tv/home/settings/v2">首页</a></span></h5>
				</div>
				<div class="ibox-content">
					<ul class='nav nav-tabs' id="nav-tabs">
						<c:forEach var="layout" items="${requestScope.layouts }"
							varStatus="status">
							<li ${status.index == 0?"class='active'":""}><a
								href="#${layout.uuid }" data-toggle="tab">${layout.name }</a></li>
						</c:forEach>
					</ul>
					<div style="margin-top: 10px;">
						<input id="add-widget-btn" type="button" class="btn btn-primary"
							value="普通组件"> <input id="add-resource-btn" type="button"
							class="btn btn-primary" value="资源组件">
					</div>
					<div class="tab-content" style="min-height: 800px">
						<c:forEach var="layout" items="${requestScope.layouts }"
							varStatus="status">
							<div id="${layout.uuid }"
								class="ibox-content tab-pane fade in ${status.index == 0?"active":""}">
								<script type="text/javascript">
									var widgets = [];
									<c:choose>
										<c:when test="${not empty layout.widgets}">
											 widgets= ${layout.widgets}
							    	</c:when>
									</c:choose>
									new Layout('${layout.uuid}',widgets,'${layout.project.uuid}');
								</script>
								<div class="gridsterBox">
									<div class="gridster">
										<span
											class="glyphicon glyphicon-refresh glyphicon-refresh-animate loading">Loading...</span>
										<ul>
										</ul>
									</div>
								</div>
								
							</div>

						</c:forEach>
						<input id="save_widgets_btn" type="button"
									class="btn btn-primary" value="保存">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {					
		var g = $('.ibox-title h5');
		<%Category c = (Category) request.getAttribute("category");
		String projectUuid = (String)request.getAttribute("projectUuid");%>
		<%String html = "";
		while (c != null) {
			html = "> <a href=\"/project/tv/home/settings/v2Sub?categoryUuid=" 
						+ c.getUuid() + "&projectUuid=" + projectUuid + "\">"
						+ c.getName() + "</a>" + html;
			c = c.getParent();
		}%>
		g.append('<%=html%>');
	})
</script>

<div class="modal fade" id="tv_widget_setting_modal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static"
	data-id="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="setting_label">Widget设置</h4>
			</div>
			<div class="modal-body" style="min-height: 500px">
				<div class="form-horizontal" name="ref_type">
					<div class="form-group">
						<label class="control-label col-md-3" for="label_name">关联类型</label>
						<div class="col-md-6">
							<select class="form-control" id="ref_type_selector">
								<option selected="selected" value="CATEGORY">学科</option>
								<option value="RESOURCE">资源</option>
								<option value="ACTIVITY">活动</option>
								<option value="BROADCAST">直播</option>
							</select>
						</div>
					</div>

					<div class="form-group" id="selector_container">
						<label class="control-label col-md-3">选择资源</label>
						<div class="col-md-6 ref-controller">
							<select class="form-control" id="selector_resource"
								name="resourceUuid">
								<option>请输入"资源名称"检索</option>
							</select>
						</div>
						<div class="col-md-6 ref-controller">
							<select class="form-control" id="selector_activity"
								name="activityUuid">
								<option>请输入"活动名称"检索</option>
							</select>
						</div>
						
						<div class="col-md-6 ref-controller">
							<select class="form-control" id="selector_broadcast"
								name="broadcastUuid">
								<option>请输入"直播名称"检索</option>
							</select>
						</div>
						
						<div id="tree_container" class="col-md-6 ref-controller">
							<div id="tree"></div>
						</div>
					</div>
					<div class="hr-line-dashed"></div>

					<div class="form-group">
						<label class="col-sm-3 control-label">设置图片</label>
						<div class="col-md-6">
							<input class="uploader-component" type="file">
						</div>
					</div>
					<div class="hr-line-dashed"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label">推荐文案</label>
						<div class="col-md-8">
							<textarea class="form-control" rows="2" cols="30" id="mark" maxlength="100" ></textarea>
						</div>
					</div>
					<div class="hr-line-dashed"></div>
				</div>
			</div>
			<div class="modal-footer">
				<button id="confirm_btn" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="tv_home_mgr" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true" data-id="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="project_label">板块管理</h4>
			</div>
			<div class="modal-body" style="min-height: 300px">
				<div class="form-horizontal" name="commentform">
					<div class="form-group">
						<label class="control-label col-md-4" for="label_name">添加板块：</label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="label_name"
								 placeholder="请输入板块名称" />
						</div>
						<button id="save-btn" type="button" class="btn btn-primary">添加</button>
					</div>
					<div class="hr-line-dashed"></div>
				</div>
				<div id="data-container">				
				</div>
			</div>
			<div class="modal-footer">
				<!-- <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button> -->
				<button id="tvSet_next" type="button" class="btn btn-primary">下一步</button>
			</div>
		</div>
	</div>
</div>