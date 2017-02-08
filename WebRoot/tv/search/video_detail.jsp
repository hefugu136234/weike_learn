<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="page">
	<div class="content">
		<header class="top-bar with-status">
			<div class="crumb-nav">
				${res_vo.name}
			</div>
			<div class="top-status play-times">
				<p>当前播放</p>
				<p class="num">${res_vo.viewCount}</p>
			</div>
		</header>

		<div id="id_video_container"></div>

		<div class="resource-intro">
			<div class="icon-title mb-1 white bold clearfix">
				<h5 class="tt bfL">
					<span class="icon icon-detail"></span>简 介
				</h5>
			</div>

			<div class="intro">
				<div class="clearfix">
					<div class="col-2">
						<span class="tt">讲者：</span>${res_vo.speakerName}
					</div>
					<div class="col-2">
						<span class="tt">时长：</span>${res_vo.video.videoTime}
					</div>
				</div>
				<p>
					<span class="tt">名称：</span>${res_vo.name}</p>
				<p>
					<span class="tt">日期：</span>${res_vo.dateTime}</p>
				<p>
					<span class="tt">简介：</span>${res_vo.desc}</p>
				<p>
					<span class="tt">医院：</span>${res_vo.hospitalName}</p>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="page_reamin_uuid" value="${page_reamin_uuid}" />
<input type="hidden" id="resource_uuid" value="${res_vo.uuid}" />
<input type="hidden" id="resource_fileId" value="${res_vo.video.fileId}" />
<script type="text/javascript">
//首次播放增加积分操作
//新js原 video_view.js
var firstShow=true;
var resource_uuid,player;
$(function(){
	// 定义全局变量
	resource_uuid=$('#resource_uuid').val();
	var fileId=$('#resource_fileId').val();


	// 绘制视频信息
	var option={
			'auto_play' : '0',
			'file_id' : fileId,
			'app_id' : '1251442335',
			'width' : 640,
			'height' : 480,
			'disable_full_screen' : 0,
			'stretch_full' : 1,
			'WMode' : 'opaque',
			'remember' : 0
	};
	player=new qcVideo.Player('id_video_container',option,{
		'playStatus' : function(status) {
			// console.log(status);
			if (status == 'ready') {
				// ready，什么都不做
			} else if (status == 'seeking') {
				// 搜索时，什么都不做
			} else if (status == 'playing') {
				// 第一次点击播放增加积分
				//viewResAddJifen(resource_uuid);
			} else if (status == 'suspended' || status == 'playEnd') {
				// 什么都不做
			}else if(status=='stop'){
				// 试看结束
			}
		}
	});
	setTimeout(function(){
		player.play(1);
	},1000);
});
</script>

