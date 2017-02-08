<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/datetimepicker/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="/assets/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="/assets/ueditor/ueditor.all.min.js"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script type="text/javascript" charset="utf-8"
	src="/assets/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/broadcast/broadcast_common.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>直播管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/broadcast/list/page">返回列表</a></li>
			<li class="active">修改直播</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/broadcast/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>修改直播</h5>
		</div>
		<div class="ibox-content">

			<form id="broadcast_form" class="form-horizontal formBox valForm">

				<input type="hidden" name="uuid"
					value="${requestScope.data_vo.uuid}" />
				<div class="form-group">
					<label class="col-sm-3 control-label">直播名称：</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" name="name"
							required="required" maxlength="29" placeholder="请填写直播名称(30字以内)"
							value="${requestScope.data_vo.name}" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">直播类型：</label>
					<div class="col-sm-2">
						<select id="castType" name="castType" class="form-control"
							required="required">
							<option value="1" selected="selected">公开</option>
							<option value="2">审核</option>
							<option value="3">pincode邀请</option>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">人数限制：</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" maxlength="8" name="limitNum"
							value="${requestScope.data_vo.limitNum}" />
					</div>
					<span style="color: red;">(默认 0 为无限制，限制人数请填具体数值)</span>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">观看消耗积分：</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" name="integral" maxlength="8" value="${requestScope.data_vo.integral}" />
					</div>
					<span style="color: red;">(默认 0 为无需消耗积分，请填写正整数)</span>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">报名开始时间：</label>
					<div class="col-sm-3">
						<div class="input-group" id="bookStart">
							<input type="text" class="form-control" name="bookStartDate"
								id="bookStartDate" readonly="readonly"
								value="${requestScope.data_vo.bookStartDate}" required="required"/> 
						</div>
					</div>

					<label class="col-sm-2 control-label">报名结束时间：</label>
					<div class="col-sm-3">
						<div class="input-group" id="bookEnd">
							<input type="text" class="form-control" name="bookEndDate"
								id="bookEndDate" readonly="readonly"
								value="${requestScope.data_vo.bookEndDate}" required="required"/> 
						</div>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">直播开始时间：</label>
					<div class="col-sm-3">
						<div class="input-group" id="startDay">
							<input type="text" class="form-control" name="startDate"
								id="startDate" required="required" readonly="readonly"
								value="${requestScope.data_vo.startDate}" /> 
						</div>
					</div>
					<label class="col-sm-2 control-label">直播结束时间：</label>
					<div class="col-sm-3">
						<div class="input-group" id="endDay">
							<input type="text" class="form-control" name="endDate"
								id="endDate" required="required" readonly="readonly"
								value="${requestScope.data_vo.endDate}" /> 
						</div>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">直播演讲者：</label>
					<div class="col-sm-5">
						<select name="speakerUuid" id="spaker_selector" class="form-control">
							<option value="">请选择</option>
							<c:if test="${not empty requestScope.chosenItem}">
								<option selected="selected" value="${requestScope.chosenItem.id}">${requestScope.chosenItem.text}</option>
							</c:if>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">直播平台：</label>
					<div class="col-sm-3">
						<select name="platFormType" id="platFormType" class="form-control" disabled="disabled">
							<option value="0">请选择</option>
							<c:if test="${not empty requestScope.data_vo.livePlatformList}">
								<c:forEach var="item" items="${data_vo.livePlatformList}">
								 <c:if test="${data_vo.platFormType eq item.platfromId}"> 
									<option value="${item.platfromId}" selected="selected">${item.platfromName}</option>
								</c:if> 
								</c:forEach>
							</c:if>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<c:if test="${data_vo.platFormType eq 1 ||data_vo.platFormType eq 3}">
				<div class="form-group">
					<label class="col-sm-3 control-label">直播接口地址：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="castAction" name="castAction"
							required="required" maxlength="250" value="${requestScope.data_vo.castAction}"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				</c:if>
				
				<c:if test="${data_vo.platFormType eq 2}">
				<div class="form-group">
					<label class="col-sm-3 control-label">直播接口地址：</label>
					<div class="col-sm-2">
					<label class="form-control control-label">用户参与链接:</label>
					<label class="form-control control-label">用户口令:</label>
					<label class="form-control control-label">嘉宾参与链接:</label>
					<label class="form-control control-label">嘉宾口令:</label>
					</div>
					<div class="col-sm-6">
						<input type="text" class="form-control" value="${data_vo.liveActionShowJs.attendeeJoinUrl}"/>
						<input type="text" class="form-control" value="${data_vo.liveActionShowJs.attendeeToken}"/>
						<input type="text" class="form-control" value="${data_vo.liveActionShowJs.panelistJoinUrl}"/>
						<input type="text" class="form-control" value="${data_vo.liveActionShowJs.panelistToken}"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				</c:if>
				
				
				
				<%-- <div class="form-group">
					<label class="col-sm-3 control-label">pincode：</label>
					<div class="col-sm-6">
						<input type="text" id="pincode" class="form-control" name="pincode"
							maxlength="150" value="${requestScope.data_vo.pincode}"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div> --%>

				<div class="form-group">
					<label class="col-sm-3 control-label">简介：</label>
					<div class="col-sm-6">
					<textarea class="form-control" rows="4" name="mark"
							required="required" maxlength="249" placeholder="直播简介(250字以内)">${requestScope.data_vo.mark}</textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>


				<div class="form-group">
					<label class="col-sm-3 control-label">直播详情：</label>
					<div class="col-md-8">
						<!-- 加载编辑器的容器 -->
						<script id="description_script" name="description"
							type="text/plain" style="height: 350px;"></script>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">TV和Web详情：</label>
					<div class="col-md-8">
						<!-- 加载编辑器的容器 -->
						<script id="tv_description_script" name="tvDescription"
							type="text/plain" style="height: 350px;"></script>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/broadcast/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
			<input type="hidden" id="castType_hide"
				value="${requestScope.data_vo.castType}" /> <input type="hidden"
				id="platFormType_hide" value="${requestScope.data_vo.platFormType}" />
			<textarea class="hide" id="description_hide">${requestScope.data_vo.description}</textarea>
			<textarea class="hide" id="tv_description_hide">${requestScope.data_vo.tvDescription}</textarea>
		</div>
	</div>
</div>

<!-- <script type="text/javascript"
	src="//cdn.bootcss.com/moment.js/2.11.2/moment.min.js"></script>
<script type="text/javascript"
	src="//cdn.bootcss.com/moment.js/2.11.2/locale/zh-cn.js"></script> -->
<script src="/assets/js/moment.min.js"></script>
<script src="/assets/js/monment_zh-cn.js"></script>		
<script type="text/javascript"
	src="/assets/js/plugins/datetimepicker/bootstrap-datetimepicker.min.js"
	charset="UTF-8"></script>
<script type="text/javascript">
	var castType_hide = $('#castType_hide').val();
	if (!!castType_hide) {
		$('#castType').val(castType_hide);
	}
	/* var platFormType_hide = $('#platFormType_hide').val();
	if (!!platFormType_hide) {
		$('#platFormType').val(platFormType_hide);
	} */

	var description_script = UE.getEditor('description_script');
	description_script.addListener("ready", function() {
		// editor准备好之后才可以使用 
		description_script.setContent($('#description_hide').text());
	});
	
	var tv_description_script = UE.getEditor('tv_description_script');
	tv_description_script.addListener("ready", function() {
		// editor准备好之后才可以使用 
		tv_description_script.setContent($('#tv_description_hide').text());
	});

	function submitFrom(from) {
		var castType=$('#castType').val();
		if(castType==3){
			var pincode=$('#pincode').val();
			if(pincode==''){
				alert('请输入pincode');
				return false;
			}
		}
		var check_description = description_script.hasContents();
		if (!check_description) {
			alert('请输入微信详情');
			return false;
		}
		var check_description=tv_description_script.hasContents();
		if (!check_description) {
			alert('请输入tv和web详情');
			return false;
		}
		$.post('/project/broadcast/data/update', $(from).serialize(), function(
				data) {
			if (data.status == 'success') {
				location.href = '/project/broadcast/list/page';
			} else {
				alert(data.message);
			}
		});
	}
</script>

