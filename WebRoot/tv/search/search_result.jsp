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
<title>${res_vo.name}</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/resource.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript" src="http://qzonestyle.gtimg.cn/open/qcloud/video/h5/h5connect.js"></script>
<script src="//cdn.bootcss.com/jquery.lazyload/1.9.1/jquery.lazyload.min.js"></script>
</head>
<body>
	<jsp:include page="${sub_page}"></jsp:include>
	<div class="hidden-full-page with-bg loading-page" id="loading_alert">
    <div class="tips">检测到投影资源，请稍后...</div>
    <div class="la-ball-clip-rotate">
      <div></div>
    </div>
  </div>
	<input type="hidden" id="qr_search_uuid" value="${qr_search_uuid}"/>
	<input type="hidden" id="qr_search_res_id" value="${qr_search_res_id}"/>
	<input type="hidden" id="qr_device" value="${qr_device}"/>
	<script type="text/javascript">
	(function(){
		var listener=function(uuid,res_id,device){
			$.post('/api/webchat/tv/search/qr/connect',{
				uuid:uuid,
				device:device
			},function(data){
				if(data.code==207){
					if(data.referId!=res_id){
						$('#loading_alert').show();
						setTimeout(function(){
							$('#loading_alert').hide();
							location.href='/api/webchat/tv/search/result?qr_uuid='+uuid+'&referId='+data.referId+'&device='+device;
						},2000);
					}else{
						setTimeout(listener(uuid,res_id,device),1000);
					}
				}else if(data.code==401){
					alert('二维码已过期');
				}else{
					console.log(data);
				}
			});
		};
		
		var uuid=$('#qr_search_uuid').val();
		var qr_search_res_id=$('#qr_search_res_id').val();
		var qr_device=$('#qr_device').val();
		listener(uuid,qr_search_res_id,qr_device);
	}());
	</script>
</body>
</html>
