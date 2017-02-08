<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link href="/assets/css/plugins/chosen/chosen.css" rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/jstree/tree_style.min.css">
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/jstree/jstree.min.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/threescreen/threescreen_common.js?ver=1.0"></script>
<script src="/assets/js/admin/uploader_common.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>三分屏管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/threescreen/list/page">三分屏列表</a></li>
			<li class="active">修改三分屏</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="javascript:;" onClick="javascript :history.back(-1);">返回</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>修改三分屏</h5>
		</div>
		<div class="ibox-content">
			<form id="threescreen_form" class="form-horizontal formBox valForm">
				<input type="hidden" name="token" value="${requestScope.token}" />
				<input type="hidden" name="uuid" value="${requestScope.threeScreen_info.uuid}" />
				<input type="hidden" id="signature" name="signature"
					value="${requestScope.signature}" />

				<div class="form-group">
					<label class="col-sm-3 control-label">三分屏名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name" id="name"
							required="required" maxlength="29" placeholder="请填写三分屏名称(30字以内)" value="${requestScope.threeScreen_info.name}"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">选择分类：</label>
					<div class="col-sm-6">
						<input type="button" class="btn btn-primary" id="pdf_category"
							value="选择分类" data-toggle="modal"
							data-target="#categorySelectorModal" name="pdf_category" /> <span
							id="category_trace"></span> <input type="hidden"
							id="categoryUuid" name="categoryUuid" required="required" value="${requestScope.threeScreen_info.categoryUuid}"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">讲者：</label>
					<div class="col-sm-4">
						<select class="form-control" id="spaker_selector"
							name="spaker_selector">
							<option value="0">请选择</option>
							<c:if test="${not empty requestScope.spaker_list}">
							 <c:forEach var="item" items="${requestScope.spaker_list}">
								<option value="${item.id}">${item.text}</option>
							</c:forEach>
							</c:if>
						</select>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">封面：</label>
					<div class="col-sm-6">
						<input id="cover_uploadify" type="file" /> <img
							id="cover_preview" alt="" src="" style="display: none;" width="480" height="320" class="pre-view"/> <input
							type="hidden" id="cover" name="cover" />
						<hr>
					</div>
				</div>

				<div id="sanfen_div" class="form-group">
					<label class="col-sm-3 control-label">三分屏对应关系：</label>
					<div id="sanfen_div_contain" class="col-sm-6">
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-3 control-label">三分屏简介：</label>
					<div class="col-sm-6">
						<textarea name="mark" class="form-control" cols="60" rows="4" maxlength="280"
							placeholder="三分屏简介(300字以内)">${requestScope.threeScreen_info.mark}</textarea>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/threescreen/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<!-- Modal -->
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
				<button id="confirm_btn" type="button" class="btn btn-primary"
					data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- Modal -->
<script type="text/javascript">
$(function(){
	var patt = new RegExp("^(http).*");
	var cover='${requestScope.threeScreen_info.cover}';
	if(!!cover){
		var cover_img=cover;
		if (!patt.test(cover)) {
			cover_img="http://cloud.lankr.cn/api/image/" + cover
			+ "?m/2/h/180/f/jpg"
		}
		$('#cover_preview').show();
		$('#cover_preview').attr('src',cover_img);
		$('#cover').val(cover);
	}

	var speakerUUid='${requestScope.threeScreen_info.speakerUuid}';
	if(!!speakerUUid){
		$("#spaker_selector").val(speakerUUid)
		$("#spaker_selector").trigger("chosen:updated");
		//$('#spaker_selector').val(speakerUUid);
	}
	var division='${requestScope.threeScreen_info.division}';
	if(!!division){
		try{
		var data_division=JSON.parse(division);
		if(!!data_division&&data_division.length>0){
			$('#sanfen_div').show();
			//排序
			data_division.sort(function(a,b){
			var a_video=a.video;
			var b_video=b.video;
            if(a_video<b_video){
            	return -1;
            }else if(a_video>b_video){
            	return 1;
            }else{
            	return 0;
            }
            });
			var sanfen_div_contain=$('#sanfen_div_contain');
			sanfen_div_contain.empty();
			$.each(data_division,function(i,item){
				var video=item.video;
				video=converSends(video);
				var pdf=item.pdf;
				sanfen_div_contain.append(buildDataUl(video,pdf));
			});
		}
		}catch(e){
			$('#sanfen_div').hide();
			console.log(e.message);
		}
	}else{
		$('#sanfen_div').hide();
	}
});

function submitFrom(from){
	$.post('/project/threescreen/update/data'
			,$(from).serialize(),
			function(data){
		if(data.status=='success'){
			location.href = '/project/threescreen/list/page';
		}else{
			alert(data.message);
		}
	});
}

//秒钟化为时分秒
function converSends(sens){
	sens=parseInt(sens);
	var hour= Math.floor(sens/3600);
	var other=sens % 3600;
	var min= Math.floor(sens/60);
	var msend = other%60;
	if(hour<10){
		hour='0'+hour;
	}
	if(min<10){
		min='0'+min;
	}
	if(msend<10){
		msend='0'+msend;
	}
	return hour+':'+min+':'+msend;
}

function buildDataUl(time,page){
	var ui=$('<div class="row">'+
	'<div class="col-sm-4">'+
		'<div class="input-group m-b"><span class="input-group-addon">时间点</span><input type="text" class="form-control" value="'+time+'" readonly="readonly"/></div>'+
	'</div>'+
	'<div class="col-sm-1">'+
		'<div class="fa fa-exchange"></div>'+
	'</div>'+
	'<div class="col-sm-4">'+
		'<div class="input-group m-b"><input type="text" class="form-control" value="'+page+'" readonly="readonly"/> <span class="input-group-addon">页数</span></div>'+
	'</div>'+
'</div>');
return ui;
}


</script>
