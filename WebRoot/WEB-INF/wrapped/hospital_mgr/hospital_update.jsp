<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/js/uploadify/uploadify.css">
<script src="/assets/js/admin/hospital_mgr/hospital_mgr_update.js"></script>	
<script src="/assets/js/admin/hospital_mgr/hospital_location.js"></script>	
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>医院管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/hospital/mgr">返回列表</a></li>
			<li class="active">编辑医院</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/hospital/mgr">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>编辑医院</h5>
		</div>
		<div class="ibox-content">
			<form id="hospital_update_form" class="form-horizontal formBox valForm">
				<input type="hidden" name="token" value="${requestScope.token }" >
				<input type="hidden" name="hospitalUuid" value="${requestScope.hospital.uuid }" >
				
				<div class="form-group">
					<label class="col-sm-3 control-label">所在省份：</label>
					<div class="col-sm-4">
						<select class="form-control" id="hospital_province" name="province"
							required="required" onchange="getCity(this.value);">
							<option value="0">请选择</option>
						</select>
					</div>
				</div>
				
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">所在城市：</label>
					<div class="col-sm-4">
						<select class="form-control" id="hospital_city" name="city" required="required" >
							<option value="0">请选择</option>
							<c:if test="${not empty requestScope.hospital.cityList}">
							 <c:forEach var="item" items="${requestScope.hospital.cityList}">
								<option value="${item.uuid}">${item.name}</option>
							</c:forEach>
							</c:if>
						</select>
					</div>
				</div>
				
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">医院等级：</label>
					<div class="col-sm-4">
						<select class="form-control" id="hospital_grade" name="grade" required="required" >
							<option value="0">请选择</option>
							<option value="1">一级医院</option>
							<option value="2">二级医院</option>
							<option value="3">三级医院</option>
							<option value="5">民营医院</option>
							<option value="6">医学院校</option>
							<option value="4">未评级别医院</option>
						</select>
					</div>
				</div>
				
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">医院名称：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="hospital_name" name="name" maxlength="59"
							 placeholder="名称(60字以内)" value="${requestScope.hospital.name }" />
					</div>
				</div>
				
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">联系方式：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="hospital_mobile" name="mobile" maxlength="29"
						placeholder="联系方式(30字以内)"
							 value="${requestScope.hospital.mobile }"/>
					</div>
				</div>
				
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">详细地址：</label>
					<div class="col-sm-4">
						<textarea class="form-control" name="address" cols="60" rows="4" id="hospital_address"
											maxlength="199" placeholder="地址(200字以内)" >${requestScope.hospital.address }</textarea>
					</div>
				</div>
				
				<div class="hr-line-dashed"></div>
				<div class="form-group">
					<div class="col-sm-3 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/hospital/mgr" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script>
	$(function(){
		buildOption(${requestScope.province_list},$('#hospital_province'),'省份数据加载出错');
		
		var province_uuid = "${requestScope.hospital.provinceUuid}";
		if(!!province_uuid){
			$('#hospital_province').val(province_uuid);
		}
		var city_uuid = "${requestScope.hospital.cityUuid}";
		console.log(city_uuid);
		if(!!city_uuid){
			$('#hospital_city').val(city_uuid);
		}
		
		var grade_value = "${requestScope.hospital.gradeValue}";
		if(!!grade_value){
			$('#hospital_grade').val(grade_value);
		}
	})
</script>
