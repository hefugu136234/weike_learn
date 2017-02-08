<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" href="/assets/js/uploadify/uploadify.css">

<script src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/admin/uploader_common.js"></script>
<script src="/assets/ueditor/ueditor.config.js"></script>
<script src="/assets/ueditor/ueditor.all.min.js"></script>
<script src="/assets/js/admin/speaker_new.js"></script>

<style>
	.pre-view {
		border: 1px gray solid;
		max-width: 280px;
		max-height: 280px;
	}
</style>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>讲者管理</h2>
		<ol class="breadcrumb">
			<li class="active">新建讲者</li>
		</ol>
	</div>

</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>新建讲者</h5>
		</div>
		<div class="ibox-content">
			<form id="speaker_form" class="form-horizontal formBox valForm">
			<input type="hidden" name="token" value="${requestScope.token }">
			<input type="hidden" name="speaker_uuid" value="${requestScope.speaker.uuid }">
			<input type="hidden" id="province_uuid" value="${not empty requestScope.speaker.province?requestScope.speaker.province:''}"/>
			<input type="hidden" id="city_uuid" value="${not empty requestScope.speaker.city?requestScope.speaker.city:''}"/>
			<input type="hidden" id="hospital_uuid" value="${not empty requestScope.speaker.hospital?requestScope.speaker.hospital:''}"/>
			<input type="hidden" id="department_uuid" value="${not empty requestScope.speaker.department?requestScope.speaker.department:''}"/>
			<input type="hidden" id="sex_uuid" value="${not empty requestScope.speaker.sex?requestScope.speaker.sex:''}"/>
			<textarea class="hide" id="speakerDescripion_hiden">${requestScope.speaker.resume }</textarea>
			
				<div class="form-group">
					<label class="col-sm-3 control-label">姓名：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="name" maxlength="29" placeholder="请填写讲者名称(30字以内)"
							required="required" value="${requestScope.speaker.name }"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">性别：</label>
					<div class="col-sm-2">
						<select id="sex" class="form-control" name="sex">
							<option value="2">请选择</option>
							<option value="1">男</option>
							<option value="0">女</option>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">头像：</label>
					<div class="col-sm-4">
						<input id="speakerHeadImg" type="file"> 
						<span id="headImgUploadStatus" style="display: none; color: red; margin-left: 10px;"></span>
						<input type="hidden" name="avatar" id="imgTaskId" value="${requestScope.speaker.avatar }">
						<img class="pre-view" alt="" src="${requestScope.speaker.avatar }"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">手机号：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="mobile"
							placeholder="手机号" maxlength="12" value="${requestScope.speaker.mobile}"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">所在省份：</label>
					<div class="col-sm-4">
						<select class="form-control" id="province" name="province"
							required="required" onchange="changeProvince(this.value);">
							<option value="0">请选择</option>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">所在城市：</label>
					<div class="col-sm-4">
						<select class="form-control" id="city" name="city"
							required="required" onchange="changeCity(this.value);">
							<option value="0">请选择</option>
							<c:if test="${not empty requestScope.speaker.cityList}">
							 <c:forEach var="item" items="${requestScope.speaker.cityList}">
								<option value="${item.uuid}">${item.name}</option>
							</c:forEach>
							</c:if>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">所在医院：</label>
					<div class="col-sm-4">
						<select class="form-control" id="hospital" name="hospital_uuid"
							required="required">
							<option value="0">请选择</option>
							<c:if test="${not empty requestScope.speaker.hosList}">
							 <c:forEach var="item" items="${requestScope.speaker.hosList}">
								<option value="${item.uuid}">${item.name}</option>
							</c:forEach>
							</c:if>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">所在科室：</label>
					<div class="col-sm-4">
						<select class="form-control" id="departments" name="department_uuid"
							required="required">
							<option value="0">请选择</option>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">职称：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="position"
							maxlength="59" placeholder="请填写职称(60字以内)" value="${requestScope.speaker.position}"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<%-- <div class="form-group">
					<label class="col-sm-3 control-label">个人简介：</label>
					<div class="col-sm-6">
						<textarea name="resume" cols="60" rows="6" maxlength="1500"
							class="form-control" placeholder="讲者简介">${requestScope.speaker.resume }</textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div> --%>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">个人简介：</label>
					<div class="col-sm-8">
						<script id="speakerDescription" name="resume" required="required"
									type="text/plain" style="height: 500px;" ></script>				
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<input class="btn btn-primary mr20" type="submit" value="保存">
						<a href="/project/speaker/mgr" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script>
$(function(){
		buildOption(${requestScope.province_list},$('#province'),'省份数据加载出错');
		buildOption(${requestScope.department_list},$('#departments'),'科室数据加载出错');
		
		var province_uuid=$('#province_uuid').val();
		if(!!province_uuid){
			$('#province').val(province_uuid);
		}
		var city_uuid=$('#city_uuid').val();
		if(!!city_uuid){
			$('#city').val(city_uuid);
		}
		var hospital_uuid=$('#hospital_uuid').val();
		if(!!hospital_uuid){
			$('#hospital').val(hospital_uuid);
		}
		var department_uuid=$('#department_uuid').val();
		if(!!department_uuid){
			$('#departments').val(department_uuid);
		}
		
		var sex_uuid=$('#sex_uuid').val();
		if(!!sex_uuid){
			$('#sex').val(sex_uuid);
		}
		
})
</script>

