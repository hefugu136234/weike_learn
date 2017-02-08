<%@page import="com.lankr.tv_cloud.utils.Tools"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/js/uploadify/uploadify.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<script type="text/javascript" src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="/assets/js/formValidation.min.js"></script>
<script type="text/javascript" src="/assets/js/formvalidation.js"></script>
<script type="text/javascript" src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<%
	Object obj = request.getAttribute("adver_status");
	String adver_status = (obj == null ? "" : (String) obj);
%>


<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-lg-10">
    <h2>
      广告管理
    </h2>
    <ol class="breadcrumb">
      <li>
        <a href="/project/adver/list/page">返回列表</a>
      </li>
      <li class="active">
        广告创建
      </li>
    </ol>
  </div>
  <div class="col-lg-2">
    <a class="btn btn-sm btn-primary bfR mt20" href="/project/adver/list/page">返回列表</a>
  </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>创建广告</h5>
		</div>
		<div class="ibox-content">
			<form id="new_adver_form" method="post" action="/project/adver/add/save" class="form-horizontal">
				<input type="hidden" name="token" value="<%=request.getAttribute("token")%>">

				<div class="form-group">
					<label class="col-sm-3 control-label">广告名称</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name" id="adverName" required="required">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">广告位置</label>
					<div class="col-sm-6">
						<select class="form-control" id="selectpositon" name="positon"></select>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">广告时长（秒）</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="adtime" id="adverTime" required="required">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">广告图片</label>
					<div class="col-sm-6">
						<input id="uploadify" type="file"/>
						<img style="display: none; max-height: 240px;" id="image-preview" alt="" class="img-thumbnail" src="" />
						<input type="hidden" id="img_url" name="img_url" value=""/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">广告简介</label>
					<div class="col-sm-6">
						<textarea class="form-control" rows="3" placeholder="请输入广告简介" name="description"></textarea>
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

<script>
$(document).ready(function() {
	showActive([ 'ad_create_nav', 'ad-mgr' ]);

	$('#uploadify').uploadify({
		'swf' : '/assets/js/uploadify/uploadify.swf',
		'uploader' : 'http://cloud.lankr.cn/api/image/upload',
		'formData' : {
			'appKey' : 'ff7a9de914595ec790dbf5b32ab46e9a'
		},
		'fileObjName' : 'file',

		'fileTypeExts' : '*.bmp;*.jpg;*.jpeg;*.png',
		'method' : 'post',
		'fileSizeLimit' : 10000,
		'auto' : true,
		'cancelImg' : '/assets/js/uploadify/uploadify-cancel.png',
		'onUploadSuccess' : function(file, data, response) {
			//console.log(data);
			$('#image-preview').show();
			var json_data = JSON.parse(data);
			$('#image-preview').attr('src', json_data.url);
			$('#img_url').val(json_data.url);
		}
	});

	//获取广告位置
	$.get("/project/adver/postion/data",function(){

	}).done(function() {

	}).fail(function() {

	}).always(function(data) {
		//console.log(data);
		if (data.status) {
			alert(data.status)
		}else{
			$("#selectpositon").empty();
			$.each(data,function(){
				var item = $('<option value='+this.id+'>'+this.name+'</option>');
				$("#selectpositon").append(item);
			});
			$("#selectpositon").find("option").eq(0).attr("selected","selected");
		}
	});
});


$('#new_adver_form').formValidation({
    framework: 'bootstrap',
    fields: {
    	adtime: {
             validators: {
            		 digits: {
                     message: 'Please enter integer.'
            	 }
             }
         }
    }
});

	var status ='<%=adver_status%>';

	if (status != '') {
		alert(status);
		status = ''
	}
</script>
