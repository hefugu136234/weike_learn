<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="template language" name="keywords" />
<meta content="author" name="author" />
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"
	name="viewport" />
<meta content="yes" name="mobile-web-app-capable" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="telephone=no" name="format-detection" />
<meta content="email=no" name="format-detection" />
<meta content="black-translucent"
	name="apple-mobile-web-app-status-bar-style" />
<title>我的作品</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<body>
	<div class="page">
		<div class="content">
			<!-- <header class="top-bar">
        <div class="crumb-nav">
          <a href="/api/webchat/my/center" class="logo icon-logo"></a>
          我的作品
        </div>
      </header> -->

			<div class="tab-control">
				<ul class="tab-control-tag select-tag with-bg">
	        <li class="btn active">待审核</li>
	        <li class="btn">已发布</li>
	      </ul>

	      <div class="tab-ctrl-content">
	      	<div class="tab-content active">
	      		<div id="my_oups_no_list" class="list-with-img with-arr p10"></div>
						<div class="empty-tips-area">
							<p>
								<img src="/assets/img/app/icon_empty.png" width="18%">
							</p>
							<p>内容暂无</p>
							<p>去其他栏目看看吧</p>
						</div>
	      	</div>
	      	<div class="tab-content">
	      		<div id="my_oups_has_list" class="list-with-img with-arr p10"></div>
						<div class="empty-tips-area">
							<p>
								<img src="/assets/img/app/icon_empty.png" width="18%">
							</p>
							<p>内容暂无</p>
							<p>去其他栏目看看吧</p>
						</div>
	      	</div>
	      </div>
	    </div>

		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
      var startTime=getNowFormatDate();
			//获取我的待上传作品
			$.getJSON('/api/webchat/activity/my/center/oups/list', {
				'type' : 0,
				'size' : 10,
				'startTime':startTime
			}, function(data) {
				if (isSuccess(data)) {
					buildNoOups(data.items);
				}
			});

			//获取我已上传作品
			$.getJSON('/api/webchat/activity/my/center/oups/list', {
				'type' : 1,
				'size' : 10,
				'startTime':startTime
			}, function(data) {
				if (data.status == 'success') {
					buildOupsList(data.items);
				}
			});
		});

		//建立为上传作品
		function buildNoOups(itemVo) {
			var ul = $('#my_oups_no_list');
			ul.empty();
			if (isItemList(itemVo)) {
				$.each(itemVo,
					function(index, item) {
					var name = item.name;
					var cover = '/assets/img/app/pre_check.jpg';
					var code = item.code;
					var typeName = '视频';
					var catgoryName = item.catgoryName;
					var dateTime = item.dateTime;
					var url = '/api/webchat/wx/works/detail/' + item.uuid;

					var li = '<a href="'+url+'" class="box">'
								 + '<div class="img">'
								 + '<img src="'+cover+'">'
								 + '<div class="img-tag tr video-type">'+typeName+'</div></div>'
								 + '<div class="info">'
								 + '<h5 class="tt">'+name+'</h5>'
								 + '<div class="desc">'
								 + '<p><span>编号：'+code+'</span></p>'
								 + '<p><span>'+catgoryName+'</span>　<span>'+dateTime+'</span></p></div></div>'
								 + '<div class="icon icon-arr-r"></div></a>';
					ul.append(li);
				});
			} else {
				$('#my_oups_no_list').next('.empty-tips-area').show();
			}
		}

		//建立已发布作品列表
		function buildOupsList(itemVo) {
			var ul = $('#my_oups_has_list');
			ul.empty();
			if (isItemList(itemVo)) {
				var ul_content = '';
				$.each(itemVo,
				function(index, item) {
					var li = drawResourceItem(item,'oups');
					ul_content += li;
				});
				ul.append(ul_content);
			} else {
				$('#my_oups_has_list').next('.empty-tips-area').show();
			}
		}

	</script>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
