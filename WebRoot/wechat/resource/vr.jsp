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
    <title>VR demo</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/resource.css" rel="stylesheet">
    <script type="text/javascript" src="/assets/js/wechat/base64.js"></script>
    <script language="javascript" type="text/javascript" src="/assets/player/UtoVRPlayerGuide.js"></script>
  </head>

  <body>
    <div id="panoDesk" style="position:absolute;width: 100%;height: 100%; overflow: hidden; padding: 0px; margin: 0px;"></div>
    <script language="javascript" type="text/javascript">
      /*播放器参数配置*/
      
      var params = {
        container: document.getElementById("panoDesk"), //播放器容器Dom对象
        name: "SceneViewer", //播放器名称
        dragDirectionMode: true, //播放器拖动模式
        dragMode: true,
        isGyro: true,  //默认开启陀螺仪功能  移动端支持陀螺仪设备有效
        fullScreenMode:true,
        scenesArr: [
          {
            sceneId: "test1k",
            sceneName: "测试",
            // sceneFilePath: "http://vrassets.lankr.net/960p.mp4",
            sceneFilePath: "http://vrassets.lankr.net/960p.mp4",
            sceneType: "Video"
          }
        ],
        //播放器不支持全景播放回调
        errorCallBack: function (e) {
         // console.log("错误状态：" + e);
          alert("错误状态:"+e);
        },
        //浏览器不支持全屏回调
        fsCallBack: function (status, playObj) {
          alert("浏览器不支持全屏！");
        }
      };
      window.onload = function () {
        initLoad(params);
      };
    </script>
  </body>
</html>
