<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/datetimepicker/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="/assets/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="/assets/ueditor/ueditor.all.min.js"></script>
<script type="text/javascript" charset="utf-8"
	src="/assets/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="/assets/js/admin/uploader_common.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/offline_activity/activity_common.js"></script>
<style>
.pre-view {
	border: 1px gray solid;
	max-width: 280px;
	max-height: 280px;
}

table td {
	vertical-align: top;
	width: 300px;
}
</style>
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>线下活动</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/offline/activity/list/page">返回列表</a></li>
			<li class="active">新建线下活动</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/admin/offline/activity/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>新建线下活动</h5>
		</div>
		<div class="ibox-content">

			<form id="activity_from" class="form-horizontal formBox valForm">

				<div class="form-group">
					<label class="col-sm-3 control-label">活动名称：</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" name="name"
							required="required" maxlength="50" placeholder="请填写活动名称(50字以内)" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>


				<div class="form-group">
					<label class="col-sm-3 control-label">活动地点：</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" name="address"
							required="required" maxlength="100" placeholder="请填写活动地址(100字以内)" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>


				<div class="form-group">
					<label class="col-sm-3 control-label">报名类型：</label>
					<div class="col-sm-2">
						<select id="enrollType" name="enrollType" class="form-control"
							required="required">
							<option value="0">公开</option>
							<option value="1" selected="selected">审核</option>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">人数限制：</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" name="limitNum"
							maxlength="8" value="0" required="required" />
					</div>
					<span style="color: red;">(默认 0 为无限制，限制人数请填具体数值)</span>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">价格(￥)：</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" name="price" maxlength="8"
							value="0" required="required" />
					</div>
					<span style="color: red;">(默认 0 为免费，请填写正整数)</span>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">消费积分：</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" name="integral"
							maxlength="8" value="0" required="required" />
					</div>
					<span style="color: red;">(默认 0 为无需消耗积分，请填写正整数)</span>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">封面：</label>
					<div class="col-sm-6">
						<table>
							<tr>
								<td><input type="file" data-type="cover_tv"
									value="TV/web封面" /></td>
								<td style="vertical-align: top;"><input type="file"
									data-type="cover_wx" value="微信封面" /></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">报名开始时间：</label>
					<div class="col-sm-3">
						<div class="input-group" id="bookStart">
							<input type="text" class="form-control" name="bookStartDate"
								id="bookStartDate" readonly="readonly" required="required" />
						</div>
					</div>

					<label class="col-sm-2 control-label">报名结束时间：</label>
					<div class="col-sm-3">
						<div class="input-group" id="bookEnd">
							<input type="text" class="form-control" name="bookEndDate"
								id="bookEndDate" readonly="readonly" required="required" />
						</div>
					</div>
				</div>
				<div class="hr-line-dashed"></div>



				<div class="form-group">
					<label class="col-sm-3 control-label">简介：</label>
					<div class="col-sm-6">
						<textarea class="form-control" rows="4" name="mark"
							required="required" maxlength="249" placeholder="简介(200字以内)"></textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>


				<div class="form-group">
					<label class="col-sm-3 control-label">详情：</label>
					<div class="col-md-8">
						<!-- 加载编辑器的容器 -->
						<script id="description_script" name="description"
							type="text/plain" style="height: 350px;"></script>
					</div>
				</div>

				<textarea class="hide" id="cover" name="cover"></textarea>
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/admin/offline/activity/list/page"
							class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>


<script src="/assets/js/moment.min.js"></script>
<script src="/assets/js/monment_zh-cn.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/datetimepicker/bootstrap-datetimepicker.min.js"
	charset="UTF-8"></script>
<script type="text/javascript">
	var description_script = UE.getEditor('description_script');
	$(function() {
		$('input[type="file"]').each(function(i, e) {
			var $e = $(e)
			uploaderInit(new Part($e, $e.data("type"), function(part) {
			}).init());
		});
	});

	function submitFrom(from) {
		var cover = getcover();
		if (cover.length < 2) {
			alert("请上传封面图片");
			return false;
		}
		cover = JSON.stringify(cover);
		$('#cover').text(cover);
		var check_description = description_script.hasContents();
		if (!check_description) {
			alert('请输入详情信息');
			return false;
		}
		$.post('/admin/offline/data/save', $(from).serialize(), function(data) {
			if (data.status == 'success') {
				location.href = '/admin/offline/activity/list/page';
			} else {
				alert(data.message);
			}
		});
	}
</script>

