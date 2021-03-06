<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/jstree/tree_style.min.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="/assets/js/formValidation.min.js">
</script>
<!-- 配置文件 -->
<script type="text/javascript" src="/assets/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="/assets/ueditor/ueditor.all.min.js"></script>
<script type="text/javascript" charset="utf-8"
	src="/assets/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>

<script type="text/javascript"
	src="/assets/js/admin/activity/main.js?ver=1.0"></script>
	<script type="text/javascript"
	src="/assets/js/admin/uploader_common.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/activity/config.js?ver=1.0"></script>
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



<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>活动管理</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/activity/list">返回列表</a></li>
			<li class="active">活动配置</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/admin/activity/list">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>活动配置</h5>
		</div>
		<div class="ibox-content">
			<form id="activity_config_form" method="post" action=""
				class="form-horizontal formBox valForm">
				<input type="hidden" name="token" value="${requestScope.token}" />
				<div class="form-group">
					<label class="col-sm-3 control-label">基本配置：</label>
					<div style="background-color: #e0e0e0" class="col-sm-6">
						<%-- <input type="text" class="form-control" value="${requestScope.activity.name}"
							required="required" maxlength="80" readonly="readonly"/> --%>

						<span>名称：${requestScope.activity.name}</span><br /> <span>学科：${requestScope.activity.category.name}</span><br />

						<span>参与类型：${requestScope.activity.joinType == -1 ?"公开":"需审批"}</span><br />

						<span>人数限制：${requestScope.activity.plimit == -1?"无限制":(requestScope.activity.plimit) }</span><br />
						
						<span>是否开启征稿：${requestScope.activity.collected == 0?"否":"是" }</span><br />
						
						<span>征稿是否需要实名：${requestScope.activity.authentic == 0?"不需要":"需要" }</span>
					</div>
				</div>
				<%-- 	<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">活动代码：</label>
					<div class="col-sm-6">	
							<span>${requestScope.activity.code}</span>
					</div>
					<input type="hidden" id="categoryUuid" name="categoryUuid">
				</div> --%>


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
					<label class="col-sm-3 control-label">提交作品须知：</label>
					<div class="col-md-8">
					<!-- 加载编辑器的容器 -->
						<script id="editor" name="notification" type="text/plain"
							style="height: 350px;"></script>
						<%-- <textarea rows="6" class="form-control" id="notification"
							maxlength="1000" name="notification" placeholder="">${requestScope.activity.config.notification }</textarea> --%>
					</div>
				</div>
				<input type="hidden" name="medias" id="medias">
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="button" id="btn_submit">保存</button>
						<a href="#" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
	<textarea id="hide_content" class="hide">${requestScope.activity.config.notification }</textarea>
</div>


<script type="text/javascript">
//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
var ue = UE.getEditor('editor');
ue.addListener("ready", function () { 
    // editor准备好之后才可以使用 
    ue.setContent($('#hide_content').text()); 
   });

	$('#btn_submit')
			.click(
					function(e) {
						var medias = [];
						$('.pre-view').each(function(i, e) {
							medias.push({
								type : $(e).data("type"),
								url : $(e).attr("src")
							})
						})
						$('#medias').val(JSON.stringify(medias))
						$.post(
										'/admin/activity/${requestScope.activity.uuid}/config/save',
										$('#activity_config_form').serialize(),
										function(data) {
											if (data.status == 'success') {
												alert('配置保存成功');
												location.href = '/admin/activity/list'
											} else {
												if (!!data.message) {
													alert(data.message)
												} else {
													alert('配置保存失败');
												}
												// 刷新token
												if (!!data.token) {
													$('#token').val(data.token)
												}
											}
										});
					})
					
	var medias = ${empty requestScope.activity.config.medias ? "[]": requestScope.activity.config.medias};
</script>
