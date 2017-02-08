<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet"
	href="/assets/css/plugins/jstree/tree_style.min.css">
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">

<script src="/assets/js/qq/uploaderh5.js?ver=2.0" charset="utf-8"></script>
<script src="/assets/js/jquery.js"></script>
<script src="/assets/js/plugins/jstree/jstree.min.js"></script>
<script src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script src="/assets/js/formValidation.min.js"></script>
<script src="/assets/js/formvalidation.js"></script>
<script src="/assets/js/qq/qquploadVod.js?ver=2.0"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/qiniu/plupload/js/plupload.full.min.js"></script>
<script src="/assets/js/qiniu/plupload/js/i18n/zh_CN.js"></script>
<script src="/assets/js/qiniu/qiniu.min.js"></script>
<script src="/assets/js/qiniu/qiniu_upload.js"></script>
<script src="/assets/js/admin/assets_mgr.js?ver=1.2" charset="utf-8"></script>

<style>
.out {
	width: 100px;
	height: 30px;
	text-align: center;
	margin: 0px auto;
	margin-top: 5px;
}

.result {
	width: auto;
	height: 60px;
	border: solid 1px green;
}

.count {
	width: 300px;
	height: 50px;
	border: solid 1px green;
	font-size: 13px;
}

.delete {
	cursor: pointer;
	padding: 0px 5px;
	text-decoration: underline;
	color: red;
}

#secret .head {
	padding: 5px;
	margin-bottom: 20px;
}

#secret .input_area {
	margin-bottom: 10px;
	text-align: left;
}

#secret .input_area span {
	display: inline-block;
	min-width: 83px;
}

#secret .head a {
	font-weight: bold;
}

#secret input[type=text] {
	width: 300px;
	height: 28px;
}

#secret button {
	margin: 10px;
	padding: 5px;
	width: 98px;
}
</style>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>资源管理</h2>
		<ol class="breadcrumb">
			<li><a href="/asset/video/mgr">返回列表</a></li>
			<li class="active">新建视频</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/asset/video/mgr">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<c:if test="${video_type==0}">
				<h5>新建腾讯云视频</h5>
			</c:if>
			<c:if test="${video_type ==1}">
				<h5>新建七牛云视频</h5>
			</c:if>
		</div>

		<div class="ibox-content" style="display: block;">
			<form id="video_new_form" method="post" action="/asset/video/save"
				enctype="application/x-www-form-urlencoded;charset=UTF-8"
				class="form-horizontal">
				<input type="hidden" name="videotype" id="videotype"
					value="${video_type}">

				<c:if test="${video_type==0}">
					<div class="form-group">
						<label class="col-sm-2 control-label">腾讯文件选择</label>
						<div class="col-sm-5">
							<button class="btn btn-primary" id="pickfiles" type="button">添加文件</button>
							<div id="file_select_status" style="display: none;">
								<div class="result" id="result"></div>
								<!-- <div class="count" id="count" style="width: 400px;"></div> -->

								<button id="start_upload" class="btn btn-primary"
									style="margin-top: 5px; display: none;" type="button">开始上传</button>
								<!-- <button id="stop_upload" class="btn btn-primary" type="button">取消上传</button> -->
								<button id="re_upload" class="btn btn-primary"
									style="display: none;" type="button">重新上传</button>
								<div class="out" id="error"
									style="color: red; width: 400px; text-align: left;"></div>
							</div>
							<input type="text" class="form-control" id="fileId" name="fileId"
								readonly="readonly" placeholder="腾讯云的视频ID">
						</div>
					</div>
				</c:if>

				<c:if test="${video_type==1}">
					<div class="form-group">
						<label class="col-sm-2 control-label">七牛文件选择</label>
						<div class="col-sm-5">
							<div id="qiniu_container"><button class="btn btn-primary" id="qiniu_file" type="button">添加文件</button></div>
							<div id="qiniu_status" style="display: none;">
								<div class="result" id="qiniu_result"></div>
								<button id="qiniu_start" class="btn btn-primary"
									style="margin-top: 5px;display: none;" type="button">开始上传</button>
								 <button id="qiniu_stop" class="btn btn-primary" style="margin-top: 5px;display: none;" type="button">停止上传</button>
							</div>
							<input type="text" class="form-control" id="qiuniu_key" name="qiuniu_key"
								readonly="readonly" placeholder="七牛云视频ID">
						</div>
					</div>
				</c:if>

				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-2 control-label">视频名称</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" name="title"
							id="videoName" required="required">
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-2 control-label">视频分类</label>
					<div class="col-sm-8">
						<input type="button" class="btn btn-primary"
							id="video_category_button" value="选择分类" data-toggle="modal"
							data-target="#categorySelectorModal"> <span
							id="category_trace"></span>
					</div>
					<input type="hidden" id="categoryUuid" name="categoryUuid">
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-2 control-label">视频讲者</label>
					<div class="col-sm-5">
						<select class="form-control" id="speaker_selector"
							name="speaker_uuid" required="required">
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-2 control-label">是否收费</label>
					<div class="col-sm-5">
						<div class="checkbox">
							<label> <input id="price_control" name="need_price"
								type="checkbox"></label> <input id="price" class="form-control"
								type="text" style="display: none;" placeholder="输入价格"
								name="price"> <span id="chinese_tips"
								style="color: green;"> </span>
						</div>

					</div>
				</div>

				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-2 control-label">视频简介</label>
					<div class="col-sm-5">
						<textarea class="form-control" rows="3" maxlength="499"
							placeholder="请输入视频简介(500字以内)" name="description"></textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<div class="col-sm-4 col-sm-offset-2">

						<button class="btn btn-primary mr20" type="submit">确定</button>
						<a href="/asset/video/mgr" class="btn btn-warning">取消</a>
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
<script>
	var status = '${requestScope.status}';
	if (status != '') {
		alert(status);
		status = ''
	}
</script>