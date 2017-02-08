<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link href="/assets/jcarouselcontrol/styles/style.css" rel="stylesheet">
<link href="/assets/css/site.css" rel="stylesheet">
<link href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css" rel="stylesheet">

<script src="/assets/ueditor/ueditor.config.js"></script>
<script src="/assets/ueditor/ueditor.all.min.js"></script>
<script src="/assets/ueditor/lang/zh-cn/zh-cn.js" charset="utf-8" ></script>
<script src="http://qzonestyle.gtimg.cn/open/qcloud/video/h5/h5connect.js"></script>
<script src="http://cdn.bootcss.com/jcarousel/0.3.4/jquery.jcarousel.min.js"></script>
<script src="/assets/js/plugins/blueimp/jquery.blueimp-gallery.min.js"></script>
<script src="/assets/js/admin/threescreen/threescreen_relate.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>三分屏管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/threescreen/list/page">返回列表</a></li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/threescreen/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>名称：${requestScope.threeScreen_info.name}</h5>
		</div>
		<div class="ibox-content">
			<form id="threescreen_form" class="form-horizontal formBox valForm">
				<input type="hidden" id="token" name="token" value="${requestScope.token}" />
				<input type="hidden" name="uuid" id="uuid" value="${requestScope.threeScreen_info.uuid}" />

				<div class="form-group">
					<div class="col-sm-6">
						<div id="id_video_container"></div>
					</div>
					<div class="col-sm-6">
						<div class="connected-carousels sfpGallery">
							<div class="stage">
								<div class="carousel carousel-stage">
									<ul>
										<c:if test="${not empty requestScope.threeScreen_info.pdfTaskId}">
											<c:forEach var="index" begin="1" end="${requestScope.threeScreen_info.pdfNum}">
												<li>
													<a href="http://7xo6el.com2.z0.glb.qiniucdn.com/${requestScope.threeScreen_info.pdfTaskId}?odconv/jpg/page/${index}/density/150/quality/80/resize/800" data-gallery=""><img src="http://7xo6el.com2.z0.glb.qiniucdn.com/${requestScope.threeScreen_info.pdfTaskId}?odconv/jpg/page/${index}/density/150/quality/80/resize/400" height="400"></a>
												</li>
											</c:forEach>
										</c:if>
									</ul>
								</div>
								<a href="javascript:" class="prev prev-stage fa fa-chevron-left"></a>
								<a href="javascript:" class="next next-stage fa fa-chevron-right"></a>
								<div id="pdf_pages"><span class="cur"></span>/<span class="all"></span></div>
							</div>

							<div class="navigation">
								<a href="javascript:" class="prev prev-navigation">&lsaquo;</a>
								<a href="javascript:" class="next next-navigation">&rsaquo;</a>
								<div class="carousel carousel-navigation">
									<ul>
										<c:if
											test="${not empty requestScope.threeScreen_info.pdfTaskId}">
											<c:forEach var="index" begin="1"
												end="${requestScope.threeScreen_info.pdfNum}">
												<li><img src="http://7xo6el.com2.z0.glb.qiniucdn.com/${requestScope.threeScreen_info.pdfTaskId}?odconv/jpg/page/${index}/density/100/quality/60/resize/200" width="50" height="50" /></li>
											</c:forEach>
										</c:if>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">三分屏对应关系：</label>
					<div class="col-sm-8">
						<div class="row">
							<div class="col-sm-5">
								<div class="input-group m-b sfTimeInput">
									<input id="hour_text" type="text" class="form-control" value="" />
									<span class="input-group-addon">时</span>
								</div>
								<div class="input-group m-b sfTimeInput">
									<input id="min_text" type="text" class="form-control" value="" />
									<span class="input-group-addon">分</span>
								</div>
								<div class="input-group m-b sfTimeInput">
									<input id="sends_text" type="text" class="form-control"
										value="" /> <span class="input-group-addon">秒</span>
								</div>
							</div>
							<div class="col-sm-1 txtC">
								<div class="fa fa-exchange"></div>
							</div>
							<div class="col-sm-2">
								<div class="input-group m-b">
									<span class="input-group-addon">第</span>
									<input id="page_text" type="text" class="form-control nopd txtC" value="" />
									<span class="input-group-addon">页</span>
								</div>
							</div>
							<input id="sanfen_button" type="button" class="btn btn-primary"
								value="新增对应关系" />
						</div>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">三分屏持续时间：</label>
					<div class="col-sm-8">
						<div class="row">
							<div class="col-sm-5">
								<div class="input-group m-b">
									<span class="input-group-addon">持续时间</span>
									<input id="sustain_time_input" type="text" class="form-control" value="" />
									<span class="input-group-addon">秒</span>
								</div>
							</div>
							<div class="col-sm-1 txtC">
								<div class="fa fa-exchange"></div>
							</div>
							<div class="col-sm-2">
								<div class="input-group m-b">
									<span class="input-group-addon">第</span>
									<input id="sustain_page_input" type="text" class="form-control nopd txtC" value="" />
									<span class="input-group-addon">页</span>
								</div>
							</div>
							<input id="sustain_button" type="button" class="btn btn-primary"
								value="新增对应关系" />
						</div>
					</div>
				</div>

				<div id="sanfen_div" class="form-group">
					<label class="col-sm-2 control-label"></label>
					<div id="sanfen_div_contain" class="col-sm-8">
					</div>
				</div>



				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-2">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/threescreen/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<!-- The Gallery as lightbox dialog, should be a child element of the document body -->
<div id="blueimp-gallery" class="blueimp-gallery">
	<div class="slides"></div>
	<h3 class="title"></h3>
	<a class="prev">‹</a> <a class="next">›</a> <a class="close">×</a> <a
		class="play-pause"></a>
	<ol class="indicator"></ol>
</div>

<!-- 对应关系描述 -->
<div class="modal fade" id="threescreenRelateModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-id="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="urlModalLabel">三分屏对应关系描述</h4>
			</div>
			<div class="modal-body">
				<!-- <script id="threescreenRelateDescription" name="description" required="required"
						type="text/plain" style="height: 250px;" ></script> -->
				<textarea id="threescreenRelateDescription" class="form-control" name="description" rows="6" required="required"></textarea>
			</div>
			<div class="modal-footer">
				<button id="confirm_btn" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function() {
	    showActive(['threescreen_mgr_nav', 'assets-mgr']);

	    var fileId = '${requestScope.threeScreen_info.fileId}';
	    var division = '${requestScope.threeScreen_info.division}';
	    var videoTime = '${requestScope.threeScreen_info.videoTime}';
	    var pdfNum = '${requestScope.threeScreen_info.pdfNum}';

	    initData(division, fileId, videoTime, pdfNum);

	    $('#threescreen_form').submit(function(event) {
	        event.preventDefault();
	        var re_late = judySumitBefor();
	        if ( !! re_late) {
	            re_late = JSON.stringify(re_late);
	            console.log(re_late);
	            $.post('/project/threescreen/relation/update/data', {
	                'uuid': $('#uuid').val(),
	                'token': $('#token').val(),
	                'jsonData': re_late
	            },
	            function(data) {
	                if (data.status == 'success') {
	                    location.href = '/project/threescreen/list/page';
	                } else {
	                    alert(data.message);
	                }
	            });
	        }
	    });
	});
</script>
