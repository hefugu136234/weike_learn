<%@page import="com.lankr.tv_cloud.utils.Tools"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
	<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/jstree/tree_style.min.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/jstree/jstree.min.js"></script>
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<!-- 配置文件 -->
<script type="text/javascript" src="/assets/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="/assets/ueditor/ueditor.all.js"></script>
<script type="text/javascript" charset="utf-8"
	src="/assets/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="/assets/js/admin/news_common.js?ver=1.0"></script>
<script type="text/javascript" src="/assets/js/admin/uploader_common.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>文章管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/news/list/page">返回列表</a></li>
			<li class="active">文章创建</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/news/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>新建文章</h5>
		</div>
		<div class="ibox-content">
			<form id="news_form" class="form-horizontal">
				<input type="hidden" name="token"
					value="<%=request.getAttribute("token")%>">

				<div class="form-group">
					<label class="col-sm-3 control-label">文章标题</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="title"
							id="news_name" required="required"
							maxlength="79" placeholder="请输入文章标题(80字以内)" />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">文章分类</label>
					<div class="col-sm-6">
						<input type="button" class="btn btn-primary"
							id="video_category_button" value="选择分类" data-toggle="modal"
							data-target="#categorySelectorModal"> <span
							id="category_trace"></span>
							<input type="hidden" id="categoryUuid" name="categoryUuid" required="required"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">文章作者</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="author"
							id="news_author" placeholder="请输入文章作者(30字以内)" maxlength="29" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">文章简介图：</label>
					<div class="col-sm-6">
						<input id="qrcode_uploadify" type="file" /> <img
							id="qrcode_preview" alt="" src="" style="display: none;" class="pre-view"/> <input
							type="hidden" id="qrTaskId" name="qrTaskId" />
						<hr>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">广告标签</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="news_label"
							name="label" placeholder="多个标签请用'/'隔开(300字以内)" maxlength="299" />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">文章摘要</label>
					<div class="col-sm-6">
						<textarea class="form-control" name="summary" id="news_sunmmary"
							required="required" placeholder="请输入文章摘要(400字以内)" maxlength="399"
							rows="4"></textarea>
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-3 control-label">文章内容</label>
					<div class="col-sm-9">
						<!-- 加载编辑器的容器 -->
						<script id="editor" name="content" type="text/plain"
							style="height: 350px;"></script>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/adver/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<!-- Modal -->
<div class="modal fade" id="categorySelectorModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">选择分类</h4>
			</div>
			<div class="modal-body">
				<div id="tree"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="confirm_btn" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
//实例化编辑器
//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
var ue = UE.getEditor('editor');

function submitFrom(from){
	var check = UE.getEditor('editor').hasContents();
	if (check){
		$.post('/project/news/add/save',
				$(from).serialize(),
				function(data){
			if (data.status == 'success') {
				alert('文章添加成功');
				location.href = '/project/news/list/page';
			} else {
				alert(data.status);
			}
		});
	}else{
		alert('请先编辑文章内容');
	}
}	
</script>