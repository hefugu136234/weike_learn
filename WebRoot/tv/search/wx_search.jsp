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
<title>搜索结果</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/search.css" rel="stylesheet">
<script src="/assets/js/jquery.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/assets/js/wechat/all_common.js"></script>
</head>
<body>
	<div class="page search-page" style="display:block;">
		<div class="search-bar">
			<div class="cancel-btn disable"><span class="icon icon-logo"></span></div>
			<form id="common_serach">
				<div class="input-box">
					<input type="search" class="input" id="search_input" maxlength="50"
						placeholder="请输入资源的名称、作者" />
				</div>
				<button type="submit" class="sub-btn icon-search" id="search_button"></button>
			</form>
		</div>

		<div class="content">
			<div id="search_total_div">
				<div id="search_has_result" class="hide">
					<div class="search-status-tag">
						共有<span class="num"></span>条记录
					</div>
					<div id="search_has_result_ul" class="list-with-img with-tv p10"></div>
				</div>

				<div id="search_no_reslut" class="search-nothing hide">
					<div class="icon">
						<img src="/assets/img/app/search_noting.png" width="11%">
					</div>
					<div class="info">
						<p>对不起</p>
						<p>没有搜索到相对应的信息</p>
					</div>
				</div>

			</div>

			<div class="search-status-tag">热门关键字</div>
			<div class="search-hot clearfix">
				<a href="javascript:void(0);" data-search="MDT"
					onclick="aClickSearch(this);">1. MDT</a> <a
					href="javascript:void(0);" data-search="腹腔镜"
					onclick="aClickSearch(this);">2. 腹腔镜</a> <a
					href="javascript:void(0);" data-search="肿瘤"
					onclick="aClickSearch(this);">3. 肿瘤</a> <a
					href="javascript:void(0);" data-search="胃癌"
					onclick="aClickSearch(this);">4. 胃癌</a>
			</div>

			<div class="search-others">
				<h5 class="tt">别的同道也在搜：</h5>
				<div class="list clearfix">
					<a href="javascript:void(0);" data-search="指南"
						onclick="aClickSearch(this);" class="items">指南</a> <a
						href="javascript:void(0);" data-search="胰腺炎"
						onclick="aClickSearch(this);" class="items">胰腺炎</a> <a
						href="javascript:void(0);" data-search="直播"
						onclick="aClickSearch(this);" class="items">直播</a>
				</div>
			</div>

			<div class="feed-back-modal" id="search_alert">
				<div class="cover"></div>
				<div class="alert">
					<h5 class="tt"></h5>
					<div class="con">

					</div>
					<div class="btns">
						 <a href="javascript:wx.closeWindow();" class="btn sub">关闭</a>
					</div>
					<div class="close-btn cancel">
						<span class="icon icon-close"></span>
					</div>
				</div>
			</div>

			<input type="hidden" id="uuid" value="${uuid}" /> <input
				type="hidden" id="user_uuid" value="${user_uuid}" />
		</div>
	</div>

	<div class="hidden-full-page with-bg loading-page" id="loading_alert">
    <div class="tips">资源信息正在投影中，请稍后...</div>
    <div class="la-ball-clip-rotate">
      <div></div>
    </div>
  </div>
	<script type="text/javascript">
		$(function() {
			search_input = $('#search_input');
			search_has_result_ul = $('#search_has_result_ul');
			qr_uuid = $('#uuid').val();
			user_uuid = $('#user_uuid').val();

			$('#common_serach').submit(function() {
				var search_input_val = search_input.val();
				if (search_input_val == '') {
					alert('请输入搜索内容！');
					return false;
				}
				searchAction(search_input_val);
				return false;
			});

			var listener=function(uuid){
				$.post('/api/webchat/tv/search/page/alive',{
					uuid:uuid,
					ticket:ticket
				},function(data){
					if(isSuccess(data)){
						setTimeout(function(){
							listener(uuid);
						},5000);
					}else{
						cosnole.log(data);
					}
				});
			};
			listener(qr_uuid);
		});

		var search_input, search_has_result_ul, qr_uuid, user_uuid;
		var ticket='';

		// 执行搜索的输入
		function searchAction(search) {
			var dataTime = new Date().getTime();
			//api/webchat/search/action/result
			// /api/webchat/search/thrid/action/result
			$.getJSON('/api/webchat/search/action/result', {
				'search' : search,
				'dateTime' : dataTime
			}, function(data) {
				if (isSuccess(data)) {
					searchResultShow(data);
				} else {
					if (!!data.message) {
						alert(data.message);
					} else {
						alert('网络异常');
					}
				}
			});
		}
		// 其他搜索的点击操作
		function aClickSearch(obj) {
			var search = $(obj).data('search');
			searchAction(search);
		}

		// 结果显示操作
		function searchResultShow(data) {
			var search = data.query;
			search_input.val(search);
			var hits = data.hits;
			var list = data.items;
			if (isItemList(list)) {
				$('#search_has_result').find('span.num').html(hits);
				$('#search_has_result_ul').empty();
				// 处理li数据
				searchResourceList(search_has_result_ul, list);
				$('#search_has_result').show();
				$('#search_no_reslut').hide();
			} else {
				$('#search_has_result').hide();
				$('#search_no_reslut').show();
			}
		}

		function searchResourceList($ul, list) {
			if (!isItemList(list)) {
				return;
			}
			$ul.empty();
			var ul_content = '';
			$.each(list, function(index, item) {
				var li = searchResourceItem(item);
				ul_content += li;
			});
			$ul.append(ul_content);
		}

		function searchResourceItem(item) {
			var type = item.type;
			var uuid = item.uuid;
			var name = item.name;
			var cover = item.cover;
			var speakerName = item.speakerName;
			var hospitalName = item.hospitalName;
			var catgoryName = item.catgoryName;
			var viewCount = item.viewCount;
			name = dealSpanStr(name, 34);
			var url = "javascript:tvShowRes('"+uuid+"')";;
			var fileClass = '', filename = '';
			if (type == 'VIDEO') {
				fileClass = 'video-type';
				filename = '视频';
			} else if (type == 'PDF') {
				fileClass = 'pdf-type';
				filename = 'PDF';
			} else if (type == 'THREESCREEN') {
				fileClass = 'ppt-type';
				filename = '课件';
			} else if (type == 'NEWS') {
				fileClass = 'file-type';
				filename = '资讯';
			} else if (type == 'VR') {
	      fileClass += ' vr-type';
	      filename = 'VR';
	    }
			var item_li = '<a href="' + url + '" class="box">'
					+ '<div class="img">' + '<img src="' + cover + '">'
					+ '<div class="img-tag tr '
					+ fileClass + '">'
					+ filename + '</div>' + '<div class="img-tag br view-num">'
					+ '<span class="icon icon-eye"></span>' + viewCount
					+ '</div></div>' + '<div class="info">' + '<h5 class="tt">'
					+ name + '</h5>' + '<div class="desc">' + '<p><span>'
					+ catgoryName + '</span></p>' + '<p><span>' + speakerName
					+ '</span> | <span>' + hospitalName
					+ '</span></p></div></div>'
					+ '<div class="tv-screen-icon"><p class="icon icon-tv"></p><p>TV投屏</p></div></a>';
			return item_li;
		}


		function tvShowRes(uuid){
			$('#loading_alert').show();
			$.post('/api/webchat/tv/search/shadow/action',{
				uuid:qr_uuid,
				user_uuid:user_uuid,
				res_uuid:uuid,
				ticket:ticket
			},function(data){
				$('#loading_alert').hide();
				if (isSuccess(data)) {
					//alert('投屏成功');
					if(data.code==210){
						ticket=data.ticket;
						$('#loading_alert').find('div.tips').html('资源信息正在tv上播放，请稍后...');
						$('#loading_alert').show();
						setTimeout(function(){
							$('#loading_alert').hide();
						},1000);
					}
				}else{
					if(data.code == 401||data.code==406){
						$('#search_alert').find('div.con').html("二维码已过期，关闭页面");
						$('#search_alert').fadeIn().addClass('active');
					}else{
						alert(data.message);
					}
				}
			});
		}
	</script>
</body>
</html>
