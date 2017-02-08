<%@page import="org.springframework.web.util.HtmlUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js">
	
</script>
<script type="text/javascript" src="/assets/js/formValidation.min.js">
	
</script>
<script type="text/javascript" src="/assets/js/formvalidation.js">
<!--
	
//-->
</script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script src="/assets/js/admin/common_submit.js"></script>
<style>
<!--
-->
</style>
<%
	String token = (String) request.getAttribute("token");
%>
<div class="ibox float-e-margins">
	<div class="ibox-title">
		<h5>创建课程</h5>
	</div>
	<div class="ibox-content" style="display: block;">
		<form id="new_course_form" method="post" action=""
			class="form-horizontal">
			<input type="hidden" name="token" value="<%=token%>">
			<div class="form-group">
				<label class="col-sm-2 control-label">课程名</label>
				<div class="col-sm-5 controls">
					<input type="text" class="form-control" name="name"
						autocomplete="off" required>
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			<div class="form-group">
				<label class="col-sm-2 control-label">讲者：</label>
				<div class="col-sm-4">
					<select class="form-control" id="speaker_selector"
						name="speaker_selector">
						<option value="0">请选择</option>
					</select>
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			<div class="form-group">
				<label class="col-sm-2 control-label">简介</label>
				<div class="col-sm-5">
					<textarea class="form-control" rows="3" placeholder="请输入简介"
						name="mark"></textarea>
				</div>
			</div>
			<div class="hr-line-dashed"></div>

			<!-- <div class="form-group">
				<div class="col-sm-4 col-sm-offset-2">
					<button class="btn btn-white" type="button">取消</button>
					<button class="btn btn-primary" type="submit">保存</button>
				</div>
			</div> -->

			<div class="form-group">
				<div class="col-sm-6 col-sm-offset-3">
					<button class="btn btn-primary mr20" type="submit">保存</button>
					<a href="#" class="btn btn-warning">取消</a>
				</div>
			</div>
			<div class="hr-line-dashed"></div>
		</form>
	</div>
</div>
<script>
	$(document).ready(
			function() {
				activeStub('collect_mgr_nav')
				//讲者下拉框异步加载
				$('#speaker_selector').ajaxChosen({
					dataType : 'json',
					type : 'GET',
					url : '/project/threescreen/search/speaker'
				}, {
					loadingImg : '/assets/img/loading.gif'
				});
				formInit($('#new_course_form'), function(form) {
					$.post('/project/collect/course/save', $(form).serialize())
							.always(function(data) {
								if (data.status == 'success') {
									alert('保存成功');
								} else if (!isBlank(data.message)) {
									alert(data.message)
								} else {
									alert('保存失败')
								}
							})
				});
			})
</script>
