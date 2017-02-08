<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="/assets/js/admin/uploader_common.js"></script>
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
		<h2>直播管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/broadcast/list/page">返回列表</a></li>
			<li class="active">直播配置</li>
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
			<h5>直播配置</h5>
		</div>
		<div class="ibox-content">
			<form id="cast_config_form" class="form-horizontal formBox valForm">
				<div class="form-group">
					<label class="col-sm-3 control-label">直播名称：</label> <input
						type="hidden" id="uuid" value="${requestScope.data_vo.uuid}" />
					<div class="col-sm-6">
						<input type="text" class="form-control" required="required"
							maxlength="80" value="${requestScope.data_vo.name}"
							readonly="readonly" />
					</div>
				</div>

				<div class="hr-line-dashed"></div>


				<div class="form-group">
					<label class="col-sm-3 control-label">背景：</label>
					<div class="col-sm-6">
						<table>
							<tr>
								<td><input type="file" data-type="bg_tv" value="TV背景" /></td>
								<td style="vertical-align: top;"><input type="file"
									data-type="bg_wx" value="微信背景" /></td>
							</tr>
						</table>

					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">banner：</label>
					<div class="col-sm-6">
						<table>
							<tr>
								<td><input type="file" data-type="banner_tv"
									value="TV banner" /></td>
								<td style="vertical-align: top;"><input type="file"
									data-type="banner_wx" value="微信banner" /></td>
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
								<td><input type="file" data-type="cover_tv" value="TV封面" /></td>
								<td style="vertical-align: top;"><input type="file"
									data-type="cover_wx" value="微信封面" /></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				<textarea class="hide" id="banner"
					>${requestScope.data_vo.banner}</textarea>
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="button" id="btn_submit">保存</button>
						<a href="/project/broadcast/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>


<script type="text/javascript">
showActive([ 'live-list', 'live-mgr' ]);
$(function(){
	$('input[type="file"]').each(function(i, e) {
		var $e = $(e)
		uploaderInit(new Part($e, $e.data("type"), function(part) {
			$.each(getbanner(), function(i, e) {
				if (part.type == e.type) {
					part.renderPreview(e.url)
				}
			});
		}).init());
	});
});
function getbanner(){
	var banner=$('#banner').text();
	if(!!banner){
		banner=JSON.parse(banner);
		return banner;
	}
	return [];
}

$('#btn_submit').click(function(e){
	e.preventDefault();
	var banner=[];
	$('.pre-view').each(function(i, e) {
		banner.push({
			type : $(e).data("type"),
			url : $(e).attr("src")
		});
	});
	checkPost(banner);
	if(!wxbg){
		alert('请上传微信背景图');
		return false;
	}
	if(!wxbanner){
		alert('请上传微信banner');
		return false;
	}
	if(!wxcover){
		alert('请上传微信封面');
		return false;
	}
	banner=JSON.stringify(banner);
	$.post('/project/broadcast/update/config',{'uuid':$('#uuid').val(),'banner':banner},function(data){
		if (data.status == 'success') {
			location.href = '/project/broadcast/list/page';
		} else {
			alert(data.message);
		}
	});
});

var wxbg,wxbanner,wxcover;
wxbg=wxbanner=wxcover=false;
function checkPost(banner){
	wxbg=wxbanner=wxcover=false;
	$.each(banner,function(index,item){
		if(item.type=='bg_wx'){
			wxbg=true;
		}else if(item.type=='banner_wx'){
			wxbanner=true;
		}else if(item.type=='cover_wx'){
			wxcover=true;
		}
	});
}					
</script>
