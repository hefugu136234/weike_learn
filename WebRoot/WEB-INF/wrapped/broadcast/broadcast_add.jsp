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
			<li class="active">新建直播</li>
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
			<h5>新建直播</h5>
		</div>
		<div class="ibox-content">

			<form id="broadcast_form" class="form-horizontal formBox valForm">

				<input type="hidden" name="token" value="${requestScope.token}" />
				<div class="form-group">
					<label class="col-sm-3 control-label">直播名称：</label>

					<div class="col-sm-6">
						<input type="text" class="form-control" name="name"
							required="required" maxlength="29" placeholder="请填写直播名称(30字以内)"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">直播类型：</label>
					<div class="col-sm-2">
						<select id="castType" name="castType" class="form-control" required="required">
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
						<input type="text" class="form-control" name="limitNum" maxlength="8" value="0" />
					</div>
					<span style="color: red;">(默认 0 为无限制，限制人数请填具体数值)</span>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">观看消耗积分：</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" name="integral" maxlength="8" value="0" />
					</div>
					<span style="color: red;">(默认 0 为无需消耗积分，请填写正整数)</span>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">报名开始时间：</label>
					<div class="col-sm-3">
						<div class="input-group" id="bookStart">
							<input type="text" class="form-control" name="bookStartDate"
								id="bookStartDate" readonly="readonly" required="required"/> 
						</div>
					</div>

					<label class="col-sm-2 control-label">报名结束时间：</label>
					<div class="col-sm-3">
						<div class="input-group" id="bookEnd">
							<input type="text" class="form-control" name="bookEndDate"
								id="bookEndDate" readonly="readonly" required="required"/> 
						</div>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">直播开始时间：</label>
					<div class="col-sm-3">
						<div class="input-group" id="startDay">
							<input type="text" class="form-control" name="startDate"
								id="startDate" required="required" readonly="readonly" />
						</div>
					</div>
					<label class="col-sm-2 control-label">直播结束时间：</label>
					<div class="col-sm-3">
						<div class="input-group" id="endDay">
							<input type="text" class="form-control" name="endDate"
								id="endDate" required="required" readonly="readonly" /> 
						</div>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">直播演讲者：</label>
					<div class="col-sm-5">
						<select name="speakerUuid" id="spaker_selector" class="form-control">
							<option value="">请选择</option>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">直播平台：</label>
					<div class="col-sm-3">
						<select name="platFormType" id="platFormType" class="form-control"
							required="required" onchange="changePlat();">
							<option value="0" data-requestUrl="">请选择</option>
							<c:if test="${not empty requestScope.data_vo.livePlatformList}">
								<c:forEach var="item"
									items="${requestScope.data_vo.livePlatformList}">
									<option value="${item.platfromId}" data-requestUrl="${item.requestUrl}">${item.platfromName}</option>
								</c:forEach>
							</c:if>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="live-interface-model hide">
				<div class="form-group">
					<label class="col-sm-3 control-label">直播接口地址：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="castAction" name="castAction"
							 maxlength="250" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				</div>

				<!-- <div class="form-group">
					<label class="col-sm-3 control-label">pincode：</label>
					<div class="col-sm-6">
						<input type="text" id="pincode" class="form-control" name="pincode"
							maxlength="150" />
					</div>
				</div>
				<div class="hr-line-dashed"></div> -->

				<div class="form-group">
					<label class="col-sm-3 control-label">简介：</label>
					<div class="col-sm-6">
						<textarea class="form-control" rows="4" name="mark"
							required="required" maxlength="249" placeholder="直播简介(250字以内)"></textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>


				<div class="form-group">
					<label class="col-sm-3 control-label">微信直播详情：</label>
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
		</div>
	</div>
</div>

<!-- <script type="text/javascript"
	src="//cdn.bootcss.com/moment.js/2.11.2/moment.min.js"></script> -->
<!-- <script type="text/javascript"
	src="//cdn.bootcss.com/moment.js/2.11.2/locale/zh-cn.js"></script> -->
<script src="/assets/js/moment.min.js"></script>
<script src="/assets/js/monment_zh-cn.js"></script>	
	
<script type="text/javascript"
	src="/assets/js/plugins/datetimepicker/bootstrap-datetimepicker.min.js"
	charset="UTF-8"></script>
<script type="text/javascript">
	var description_script = UE.getEditor('description_script');
	var tv_description_script=UE.getEditor('tv_description_script');
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
		$.post('/project/broadcast/data/save', $(from).serialize(), function(
				data) {
			if (data.status == 'success') {
				location.href = '/project/broadcast/list/page';
			} else {
				alert(data.message);
			}
		});
	}
</script>

