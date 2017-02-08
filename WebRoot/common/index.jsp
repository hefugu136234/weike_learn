<%@page import="org.springframework.web.util.HtmlUtils"%>
<%@page import="com.lankr.tv_cloud.model.Project"%>
<%@page import="com.lankr.tv_cloud.model.UserReference"%>
<%@page import="com.lankr.tv_cloud.model.User"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title>后台管理</title>
<link href="/assets/css/bootstrap.min.css" rel="stylesheet">
<link href="/assets/font-awesome/css/font-awesome.css" rel="stylesheet">
<link href="/assets/css/animate.css" rel="stylesheet">
<link href="/assets/css/style.css" rel="stylesheet">
<link href="/assets/css/default.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/common.js"></script>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<style type="text/css">
.approved {
	color: green;
}

.unapproved {
	color: red;
}

.underline {
	color: #FF9933;
}
</style>

<%
	String content_page = (String) request.getAttribute("page_include");
	User user = (User) request.getAttribute("currentUser");
%>
<script type="text/javascript">
	$.ajaxSetup({
		cache : false
	});
	function activeInit() {
		$('#side-menu').find('li').each(function(i, e) {
			$(e).removeClass('active');
		});
	}

	function showActive(ids) {
		activeInit();
		$(ids).each(function(i, e) {
			var element = $('#' + e);
			if (element != 'undefind') {
				element.toggleClass('active');
			}
		});
	}

	function activeStub(id) {
		activeInit();
		var element = $('#' + id);
		element.toggleClass('active');
		//get root
		var root = element.parents('li');
		if (root.length > 0) {
			root.toggleClass('active');
		}
	}

	function userInfo(uuid) {
		$('#iframe').attr("src", '/project/userOverView/' + uuid);
		$('#user_info_global').modal('show');
	}

	function resourceInfo(uuid) {
		$('#iframe_resource').attr("src", '/project/resourceOverView/' + uuid);
		$('#resource_info_global').modal('show');
	}

	var qiniu_cdn_host = '${requestScope.qiniu_cdn_host}';
</script>
</head>

<body>
	<div id="wrapper">
		<nav class="navbar-default navbar-static-side" role="navigation">
			<div class="sidebar-collapse">
				<ul class="nav" id="side-menu">
					<li class="nav-header">
						<div class="dropdown profile-element">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#"> <span
								class="clear"> <span class="block m-t-xs"> <strong
										class="font-bold">您好，<%=user.getNickname() == null ? user.getUsername() : user
					.getNickname()%></strong>
								</span> <span class="text-muted text-xs block"> <%
 	if (user.getMainRole().isSuperAdmin()) {
 %> <%=user.getMainRole().getRoleDesc()%> <%
 	} else {
 		UserReference ur = user.getHandlerReference();
 		if (ur != null) {
 %> <%=ur.getRole().getRoleDesc()%> <%
 	}
 	}
 %> <b class="caret"></b>
								</span>
							</span>
							</a>
							<ul class="dropdown-menu animated fadeInRight m-t-xs">
								<%
									if (user.getMainRole().isProUser()) {
								%>
								<li><a href="/user/project/multipart">我的项目</a></li>
								<%
									}
								%>
								<li><a href="/user/load/password/page">设置</a></li>
								<li><a href="/admin/user/logout">注销</a></li>
							</ul>
						</div>
						<div class="logo-element">Lankr</div>
					</li>
					<%
						if (user.getMainRole().isSuperAdmin()) {
					%>
					<li id="project_mgr"><a href="#" class="tt"> <i
							class="fa fa-th-large"></i> <span class="nav-label">项目总览</span> <span
							class="fa arrow"></span>
					</a>
						<ul class="nav nav-second-level">
							<li id="project_list_nav"><a class="action_controller"
								href="/admin/project/list">项目列表</a></li>
							<li id="project_create_nav"><a class="action_controller"
								href="/admin/project/new">项目创建</a></li>
						</ul></li>
					<li id="admin_user_mgr"><a href="#"
						class="action_controller tt"> <i class="fa fa-th-large"></i> <span
							class="nav-label">用户总览</span> <span class="fa arrow"></span>
					</a>
						<ul class="nav nav-second-level">
							<li id="user_list_nav"><a class="action_controller"
								href="/admin/user/list">用户列表</a></li>
							<li id="admin_user_create_nav"><a class="action_controller"
								href="/admin/user/new">用户创建</a></li>
						</ul></li>
					<li><hr></li>
					<%
						}
					%>

					<%
						if (user.getHandlerReference() != null) {
							Project pro = user.getStubProject();
					%>
					<%
						if (pro != null && user.getProUserRole().isProAdmin()) {
					%>
					<li id="holder_project"><a href="#" class="tt"> <i
							class="fa fa-plus-square"></i> <span class="nav-label"><%=HtmlUtils.htmlEscape(pro.getProjectName())%></span>
							<span class="fa arrow"></span>
					</a>
						<ul class="nav nav-second-level">
							<li id="nav_banner_setting"><a href="/tv/home/settings">TV订制</a></li>
							<li id="nav_tv_settings"><a
								href="/project/tv/home/settings/v2">TV订制(新)</a></li>
							<li id="wx_subject"><a href="/project/wx/subject/list/page">学科管理</a></li>
							<!-- li id="pro_detail_nav"><a href="#">项目详情</a></li-->
							<li id="pro_user_list_nav"><a
								href="/project/userReference/list">用户列表</a></li>
							<!-- <li id="pro_new_user_nav"><a href="/project/user/new">新建用户</a></li> -->
							<li id="apply_mgr_nav"><a href="/project/apply/list/page">vip申请管理</a></li>
							<!-- <li id="subscribe_mgr_nav"><a href="/project/subscribe/list/page">预约记录管理</a></li> -->
							<li id="group_mgr_nav"><a
								href="/project/group/manufacturer/list/page">厂商管理</a></li>
							<li id="active_mgr_nav"><a
								href="/project/group/active/list/page">流量卡管理</a></li>
							<li id="share_mgr_nav"><a
								href="/project/group/share/list/page">分享有礼</a></li>
							<li id="certification_mgr_nav"><a href="/certification/page">实名审核</a></li>
						</ul></li>
					<%
						}
					%>
					<li id="assets-mgr"><a href="#" class="tt"> <i
							class="fa fa-play-circle-o"></i> <span class="nav-label">资源</span>
							<span class="fa arrow"></span>
					</a>
						<ul class="nav nav-second-level">
							<li id="category_mgr_nav"><a href="/asset/category/mgr">分类管理</a></li>
							<li id="video_mrg_nav"><a href="/asset/video/mgr">视频管理</a></li>
							<li id="pdf_mrg_nav"><a href="/project/pdf/list/page">PDF管理</a></li>
							<li id="threescreen_mgr_nav"><a
								href="/project/threescreen/list/page">三分屏管理</a></li>
							<li id="news_list_nav"><a href="/project/news/list/page">文章管理</a></li>
							<li id="speaker_mgr_nav"><a href="/project/speaker/mgr">讲者管理</a></li>
							<li id="banner_mgr_nav"><a href="/project/banner/mgr">Banner管理</a></li>
							<li id="tag-list-nav"><a href="/tag/showParentTagPag">标签管理</a></li>
							<li id="hospital_mgr_nav"><a href="/project/hospital/mgr">医院管理</a></li>
							<li id="qrcode_mgr_nav"><a href="/project/qrcode/list/page">二维码管理</a></li>
							<li id="collect_mgr_nav"><a
								href="/project/collect/list/page">合集管理</a></li>
						</ul></li>
					<!-- 				<li id="ad-mgr">
						<a href="#" class="tt">
							<i class="fa fa-delicious"></i>
							<span class="nav-label">广告管理</span>
							<span class="fa arrow"></span>
						</a>
						<ul class="nav nav-second-level">
							<li id="ad_list_nav"><a href="/project/adver/list/page">广告列表</a></li>
							<li id="ad_create_nav"><a href="/project/adver/add/page">广告创建</a></li>
						</ul>
					</li>-->
					<!-- 				<li id="news-mgr">
						<a href="#" class="tt">
							<i class="fa fa-delicious"></i>
							<span class="nav-label">管理</span>
							<span class="fa arrow"></span>
						</a>
						<ul class="nav nav-second-level">
							<li id="news_list_nav"><a href="/project/news/list/page">文章管理</a></li>
							<li id="news_create_nav"><a href="/project/news/add/page">新闻创建</a></li>
						</ul>
					</li>	 		 -->

					<li id="activity-mgr"><a href="#" class="tt"> <i
							class="fa fa-th-large"></i> <span class="nav-label">活动</span> <span
							class="fa arrow"></span>
					</a>
						<ul class="nav nav-second-level">
							<li id="activity-list-nav"><a href="/admin/activity/list">活动管理</a></li>
							<li id="offline_activity_list"><a href="/admin/offline/activity/list/page">线下活动</a></li>
							<li id="activity_oups_code_list-nav"><a
								href="/admin/oups/list/page">作品编号</a></li>
							<!-- <li id="game-shake-nav"><a href="/game/exchange/record/page">摇一摇兑换</a></li> -->
						</ul></li>

					<li id="integral-mgr"><a href="#" class="tt"> <i
							class="fa fa-th-large"></i> <span class="nav-label">积分</span> <span
							class="fa arrow"></span>
					</a>
						<ul class="nav nav-second-level">
							<li id="goods-list"><a
								href="/project/integral/goods/list/page">兑换品</a></li>
							<!-- <li id="goods-new"><a href="/admin/consume/new">新建商品</a></li> -->
							<li id="exchange-list-nav"><a href="/admin/exchange/list">兑换记录</a></li>
						</ul></li>

					<li id="live-mgr"><a href="javascript:" class="tt"> <i
							class="fa fa-th-large"></i> <span class="nav-label">直播</span> <span
							class="fa arrow"></span>
					</a>
						<ul class="nav nav-second-level">
							<li id="live-list"><a href="/project/broadcast/list/page">直播管理</a></li>
						</ul></li>

					<li id="games-mgr"><a href="javascript:" class="tt"> <i
							class="fa fa-th-large"></i> <span class="nav-label">游戏</span> <span
							class="fa arrow"></span>
					</a>
						<ul class="nav nav-second-level">
							<li id="games-list-nav"><a href="/project/games/list/page">游戏管理</a></li>
							<li id="game-record-nav"><a
								href="/project/games/record/page">抽奖记录(新)</a></li>
						</ul></li>

					 <li id="questionnaire_mgr">
						<a href="javascript:" class="tt">
							<i class="fa fa-th-large"></i>
							<span class="nav-label">问卷</span>
							<span class="fa arrow"></span>
						</a>
						<ul class="nav nav-second-level">
							<li id="questionnaire_list_nav"><a href="/project/questionnaire/list/page">问卷管理</a></li>
						</ul>
					</li> 
					
					<li id="comment-mgr"><a href="javascript:" class="tt"> <i
							class="fa fa-th-large"></i> <span class="nav-label">评论</span> <span
							class="fa arrow"></span>
					</a>
						<ul class="nav nav-second-level">
							<li id="comment-list-nav"><a href="/project/resource/comments/page">评论管理</a></li>
						</ul></li>

					<!--
					<li>
						<a href="#" class="tt">
							<i class="fa fa-th-large"></i>
							<span class="nav-label">报表</span>
						</a>
					</li>
					-->
					<%
						}
					%>
				</ul>
			</div>
		</nav>

		<div id="page-wrapper" class="gray-bg">
			<div class="row border-bottom">
				<nav class="navbar navbar-static-top" role="navigation"
					style="margin-bottom: 0">
					<div class="navbar-header">
						<a class="navbar-minimalize minimalize-styl-2 btn btn-primary"
							href="#"><i class="fa fa-bars"></i></a>
					</div>
					<ul class="nav navbar-top-links navbar-right">
						<li><a href="/admin/user/logout"> <i
								class="fa fa-sign-out"></i> 安全退出当前帐号
						</a></li>
					</ul>
				</nav>
			</div>
			<%
				if (content_page != null) {
			%>
			<jsp:include page="<%=content_page%>" />
			<%
				}
			%>
			<div class="footer">
				<div class="pull-right"></div>
				<div>
					<strong>Copyright</strong> Lankr &copy; 2015-2016
				</div>
			</div>

		</div>
	</div>

	<!-- Mainly scripts -->
	<script src="/assets/js/bootstrap.min.js"></script>
	<script src="/assets/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="/assets/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<!-- Custom and plugin javascript -->
	<script src="/assets/js/main.js"></script>
	<script src="/assets/js/plugins/pace/pace.min.js"></script>

	<div class="modal fade" id="user_info_global" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
		data-id="">
		<div class="modal-dialog" style="width: 75%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="user_label">用户详情</h4>
				</div>
				<div class="modal-body" style="min-height: 600px;">
					<iframe height=560 width=100% id="iframe" src="" frameborder=0
						allowfullscreen></iframe>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="resource_info_global" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
		data-id="">
		<div class="modal-dialog" style="width: 75%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="user_label">资源信息概览</h4>
				</div>
				<div class="modal-body" style="min-height: 600px;">
					<iframe height=560 width=100% id="iframe_resource" src=""
						frameborder=0 allowfullscreen></iframe>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
