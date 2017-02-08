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
<title>tv搜索</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
</head>
<body>
	<div class="page">
		<div class="content">
			<div style="margin-top: 200px;">
				<img id="qr" src="" alt="" width="180" style="margin-left: 100px">
				<p style="margin-top: 10px;text-align: center;"><span id="qr_mark"></span></p>
			</div>
		</div>
	</div>
	
	<div class="hidden-full-page with-bg loading-page" id="loading_alert">
    <div class="tips">检测到投影资源，请稍后...</div>
    <div class="la-ball-clip-rotate">
      <div></div>
    </div>
  </div>
	<script type="text/javascript">
	(function(){
		var $span=$('#qr_mark');
		
		var polling=function(uuid){
			$.post('/api/webchat/tv/search/qr/connect',{
				uuid:uuid,
				device:'qwertyui'
			},function(data){
				if(data.code==200||data.code==209){
					qrshow(data.message);
					setTimeout(polling(uuid),1000);
				}else if(data.code==207){
					qrshow(data.message);
					$('#loading_alert').show();
					setTimeout(function(){
						$('#loading_alert').hide();
						location.href='/api/webchat/tv/search/result?qr_uuid='+uuid+'&referId='+data.referId+'&device=qwertyui';
					},2000);
				}else{
					qrshow(data.message);
				}
			});
		};
		
		var qrshow=function(message){
			$span.html(message);
		};
		
		$.getJSON('/api/webchat/tv/search/qr',{
			device:'qwertyui',
			datatime:new Date().getTime()
		},function(data){
			if(data.status=='success'){
				$('#qr').attr('src',data.qrUrl);
				qrshow(data.message);
				polling(data.uuid);
			}else{
				console.log(data.message);
			}
		});
	}())
	</script>
</body>
</html>
