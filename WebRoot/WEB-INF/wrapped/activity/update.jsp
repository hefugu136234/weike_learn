<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet" href="/assets/css/plugins/jstree/tree_style.min.css">
<link rel="stylesheet" href="/assets/css/plugins/jstree/tree_style.min.css">
<script src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script src="/assets/js/formValidation.min.js"></script>
<script src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script src="/assets/js/admin/activity/main.js?ver=1.0"></script>
<script src="/assets/js/admin/uploader_common.js"></script>
<script src="/assets/js/admin/activity/update.js"></script>
<script src="/assets/js/plugins/jstree/jstree.min.js"></script>
<!-- 配置文件 -->
<script type="text/javascript" src="/assets/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="/assets/ueditor/ueditor.all.min.js"></script>
<script type="text/javascript" charset="utf-8"
	src="/assets/ueditor/lang/zh-cn/zh-cn.js"></script>

<style>
<!--
.pre-view {
	border: 1px gray solid;
	max-width: 280px;
	max-height: 280px;
}
table td{
	vertical-align:top;
	width: 300px;
}
-->
</style>

<script type="text/javascript">
	var medias = ${empty requestScope.activity.config.medias ? "[]": requestScope.activity.config.medias};
</script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>活动管理</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/activity/list">返回列表</a></li>
			<li class="active">活动编辑</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/admin/activity/list">返回列表</a>
	</div>
</div>
<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>活动编辑</h5>
		</div>
		<div class="ibox-content">
			<form id="activity_update_form" method="post" action=""
				class="form-horizontal formBox valForm">
				<input type="hidden" name="token" id="token" value="${requestScope.token}" />
				<input type="hidden" name="activityUuid" id="activityUuid" value="${requestScope.activity.uuid}" />
				<input type="hidden" name="medias" id="medias">
				
				<div class="form-group">
					<label class="col-sm-3 control-label">活动名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name" id="name" value="${requestScope.activity.name}"
							required="required" maxlength="29"  placeholder="请填写活动名称(30字以内)" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">活动分类：</label>
					<div class="col-sm-6">
						<input type="button" class="btn btn-primary"
							id="video_category_button" value="选择分类" data-toggle="modal"
							data-target="#categorySelectorModal"> <span id="category_trace"></span>
					</div>
					<input type="hidden" id="categoryUuid" name="categoryUuid" value="${requestScope.activity.category.uuid }">
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">参与类型：</label>
					<div class="col-sm-2">
						<select name="joinType" id="joinType" class="form-control" required="required">
							<option value="">请选择</option>
							<option value="0" ${requestScope.activity.joinType == 0?"selected=\"selected\"":""}>公开</option>
							<option value="1" ${requestScope.activity.joinType == 1?"selected=\"selected\"":""}>需审批</option>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">人数限制：</label>
					<div class="col-sm-2">
						<select id="plimit_controller" class="form-control">
							<option value="0" ${requestScope.activity.plimit == -1?"selected=\"selected\"":""}>无限制</option>
							<option value="1" ${requestScope.activity.plimit != -1?"selected=\"selected\"":""}>设上限</option>
						</select>
					</div>					
					<div class="col-sm-2" id="plimit_number" style="display: none;">
						<input class="form-control" name="plimit" id="plimit" maxlength="8" value="${requestScope.activity.plimit }">
					</div>
				</div>
				
				
					<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">开启征集作品：</label>
					<div class="col-sm-2">
						<select name="collected" id="collected" class="form-control"
							required="required">
							<option value="0" ${requestScope.activity.collected == 0?"selected=\"selected\"":""}>否</option>
							<option value="1" ${requestScope.activity.collected == 1?"selected=\"selected\"":""}>是</option>
						</select>
					</div>
				</div>

				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">是否征稿实名：</label>
					<div class="col-sm-2">
						<select name="authentic" id="authentic" class="form-control"
							required="required">
							<option value="0" ${requestScope.activity.authentic == 0?"selected=\"selected\"":""}>否</option>
							<option value="1" ${requestScope.activity.authentic == 1?"selected=\"selected\"":""}>是</option>
						</select>
					</div>
				</div>
				
				
				
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">自动关联资源：</label>
					<div class="col-sm-1">
						<select name="auto" id="auto" class="form-control"
							required="required">
							<option value="1"
								${requestScope.activity.config.auto == 1?"selected=\"selected\"":""}>是</option>
							<option value="0"
								${requestScope.activity.config.auto == 0?"selected=\"selected\"":""}>否</option>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">背景：</label>
					<div class="col-sm-6">
						<table>
							<tr>
								<td><input type="file" data-type="0" value="TV背景" /></td>
								<td style="vertical-align: top;"><input type="file"
									data-type="1" value="微信背景" /></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">KV：</label>
					<div class="col-sm-6">
						<table>
							<tr>
								<td><input type="file" data-type="2" value="TV KV" /></td>
								<td style="vertical-align: top;"><input type="file"
									data-type="3" value="微信KV" /></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">封面：</label>
					<div class="col-sm-6">
						<table>
							<tr>
								<td><input type="file" data-type="4" name="tv_bg"
									value="TV封面" /></td>
								<td style="vertical-align: top;"><input type="file"
									data-type="5" value="微信封面" /></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">简介：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="mark" id="mark"
							required="required" maxlength="149" placeholder="活动简介(150字以内)" value="${requestScope.activity.mark }"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">活动详情：</label>
					<div class="col-md-6">
							<textarea rows="6" class="form-control" id="description" maxlength="1000"
								name="description" placeholder="活动详情(1000字以内)">${requestScope.activity.description}</textarea>
								<span style="color: red">此内容也用于弹窗显示</span>
						</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">提交作品须知：</label>
					<div class="col-md-8">
					<!-- 加载编辑器的容器 -->
						<script id="editor" name="notification" type="text/plain"
							style="height: 350px;"></script>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit" id="btn_submit">保存</button>
						<a href="/admin/activity/list" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
	<textarea id="hide_content" class="hide">${requestScope.activity.config.notification }</textarea>
</div>

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
//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
var ue = UE.getEditor('editor');
ue.addListener("ready", function () { 
    // editor准备好之后才可以使用 
    ue.setContent($('#hide_content').text()); 
   });
</script>