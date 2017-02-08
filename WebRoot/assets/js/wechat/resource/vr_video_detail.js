$(function(){
	var videoTogglePlay;
	var resource_vrurl=$('#resource_vrurl').val();
	var resource_uuid=$('#resource_uuid').val();
	var page_reamin_uuid = $('#page_reamin_uuid').val();
	// 页面记录时间对象
	var page_obj = creatPageReamin(page_reamin_uuid, 5);
	// 增加积分对象
	var jifen_obj = creatResourseJifen(resource_uuid);
	/*播放器参数配置*/
    var params = {
      container:document.getElementById("panoDesk"), //播放器容器Dom对象
      name:"SceneViewer", //播放器名称
      dragDirectionMode:true, //播放器拖动模式
      dragMode:false,
      isGyro:true,        //默认开启陀螺仪功能  移动端支持陀螺仪设备有效
      scenesArr:[
        {
          sceneId:"VR_SHOW",
          // sceneFilePath:"http://7xo6el.com2.z0.glb.qiniucdn.com/video.mp4",
          //sceneFilePath:"http://4607.vod.myqcloud.com/4607_53e4350cb54811e6bc811bda67685817.f0.mp4",
          sceneFilePath:resource_vrurl,
          // sceneFilePath: "/assets/videos/960p.mp4",
          sceneType:"Video",
          initPan:0,
          initFov:100
        }
      ],
      initOverCallBack:function(player){
      	videoTogglePlay=document.getElementById("videoTogglePlay");
      },
      videoPlayerCallBack:function(){
    	  //点击播放，启动回调记录
    	// 页面监测
			page_obj.controller(function() {
				return isVrPlay(videoTogglePlay.className);
			});
			// 第一次点击播放增加积分
			jifen_obj.request();
      }
//      videoTogglePlayCallBack:function(player){
//      	//console.log(player.videoTogglePlay);
//      	console.log(player);
//      }
    };
    initLoad(params);
});

function isVrPlay(className){
	if(className.indexOf('videoPlay')>0){
		return true;
	}
	return false;
}
