<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="template language" name="keywords" />
    <meta content="author" name="author" />
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" name="viewport" />
    <meta content="yes" name="mobile-web-app-capable" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
    <title>签收</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/sign_in.css" rel="stylesheet">
  </head>

  <body onload="init();">
    <header class="top-bar">
      <div class="crumb-nav">
        <a href="/api/webchat/my/center" class="logo icon-logo"></a>
        签收
      </div>
    </header>

    <div class="sign-in-container">
      <canvas id="canvas" width="100%" height="200"></canvas>
    </div>

    <div id="imgPrev">
      <div id="imageitem"><img src="" id="signImg"></div>
      <div class="btn-group">
        <button type="button" id="signInBtn" class="btn">签 收</button>
        <button type="button" id="cancelBtn" class="btn">重新生成</button>
      </div>
    </div>

    <div id="loadingBox" class="hidden-full-page with-bg">
      <div class="la-ball-clip-rotate">
        <div></div>
      </div>
    </div>

    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <!-- 画图js -->
    <script type="text/javascript" src="/assets/js/wechat/createjs.min.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/movieclip-0.8.1.min.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/Graphics.js"></script>

    <script type="text/javascript">
    var canvas, stage, exportRoot;

    function init() {
      canvas = document.getElementById("canvas");
      lib.properties.width = canvas.width = window.innerWidth;
     // console.log(lib.properties.width);
      lib.properties.height = canvas.height = window.innerHeight-80;
      //console.log(lib.properties.height);

      exportRoot = new lib.Graphics();

      stage = new createjs.Stage(canvas);
      stage.addChild(exportRoot);
      stage.update();
      createjs.Ticker.setFPS(lib.properties.fps);
      createjs.Ticker.addEventListener("tick", stage);

      document.addEventListener("touchmove", function(e){e.preventDefault();}, false);
    }


    $(function(){
    	$('#signInBtn').click(function(e){
    		e.preventDefault();
    		$('#signInBtn').attr('disabled','disabled');
    		$('#loadingBox').show();
    		var imgData=$('#signImg').attr('src');
    		console.log('imgData:'+imgData);
    		var formData = new FormData();//无参数的form
    		formData.append("imgData",convertBase64UrlToBlob(imgData));
    		$.ajax({
    			url:'/api/webchat/affirm/sign',
    			type:'POST',
    			dataType:"json",
    			data : formData,
    		    processData : false,         // 告诉jQuery不要去处理发送的数据
    		    contentType : false,        // 告诉jQuery不要去设置Content-Type请求头
    		    success:function(data){
    	            //window.location.href="${ctx}"+data;
    	            console.log(data);
    	            $('#loadingBox').fadeOut();
    	            $('#signInBtn').removeAttr('disabled');
    	            if(data.status=='success'){
    	            	location.href='/api/webchat/my/center';
    	            }else{
    	            	alert(data.message);
    	            }
    	        },
    	        xhr:function(){            //在jquery函数中直接使用ajax的XMLHttpRequest对象
    	            var xhr = new XMLHttpRequest();
    	            xhr.upload.addEventListener("progress", function(evt){
    	                if (evt.lengthComputable) {
    	                    var percentComplete = Math.round(evt.loaded * 100 / evt.total);
    	                    console.log("正在提交."+percentComplete.toString() + '%');        //在控制台打印上传进度
    	                }
    	            }, false);
    	            return xhr;
    	        }
    		});
    	});

    	 $('#cancelBtn').click(function(e){
    	    	e.preventDefault();
    	      $('#imgPrev').hide();
    	    });
    });


    /**
     * 将以base64的图片url数据转换为Blob
     * @param urlData
     *            用url方式表示的base64图片数据
     */
    function convertBase64UrlToBlob(urlData){

        var bytes=window.atob(urlData.split(',')[1]);        //去掉url的头，并转换为byte

        //处理异常,将ascii码小于0的转换为大于0
        var ab = new ArrayBuffer(bytes.length);
        var ia = new Uint8Array(ab);
        for (var i = 0; i < bytes.length; i++) {
            ia[i] = bytes.charCodeAt(i);
        }

        return new Blob( [ab] , {type : 'image/png'});
    }


    </script>
  </body>
</html>
