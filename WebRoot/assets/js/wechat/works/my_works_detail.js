// 必须实现方法
function buildDataList(data) {
	var list = data.itemList;
	if (!!list && list.length > 0) {
		droploadList.listStartTime = list[list.length - 1].dateTime;
	}
	return list;
}

function buildUl(ul, itemList) {
	$.each(itemList,function(i,item){
		var uuid=item.uuid;
		var commonUser=item.commonUser;
		var commonUserImg=item.commonUserImg;
		var dateTime=item.dateTime;
		var common=item.common;
		var deltel=item.deltel;
		var manage=item.manage;
		if(!commonUserImg){
			if(manage){
				//管理员头像
				commonUserImg='/assets/img/app/manaer.jpg';
			}else{
				//普通头像
				commonUserImg='/assets/img/app/default_img.jpg';
			}
		}
		if(manage){
			if(!!commonUser){
				commonUser=commonUser+'(管理员)'
			}else{
				commonUser='管理员'
			}
		}else{
			if(!commonUser){
				commonUser='未知';
			}
		}
		var comments_li = '<li class="clearfix"><div class="photo">'
			              +'<img src="'+commonUserImg+'" alt=""></div><div class="detail">'
			              +'<h5 class="name">'+commonUser+'</h5><div class="con">'
			              + common+'</div><div class="time">'
			              + dateTime+'</div>';
		if(deltel){
			comments_li=comments_li+'<a href="javascript:;" class="del-btn" data-uuid="'+uuid+'" onclick="delCommon(this);">删除评论</a>';
		}
		comments_li=comments_li+'</div></li>';
		ul.append(comments_li);
	});
}

var droploadList;
function dealDataInit(json_data) {
	// 填充数据
	var oupsCode = json_data.oupsCode;
	$('#p_oups_code').html(oupsCode);

	// 作品状态
	/**
	 * 1=为原始的老状态 0=初始 1,3=收到作品 4=初审 5=作品转码 6=专业审核 8=上线 10=不合格
	 */
	var oupsStatus = json_data.oupsStatus;
	var activeIndex;
	if (oupsStatus == 0) {
		// 初始
		$('#id_p1').html('您的作品正在发送中');
		$('#id_p2').html('请耐心等候');
		activeIndex = 0;
	} else if (oupsStatus == 1 || oupsStatus == 3) {
		// 收到作品
		$('#id_p1').html('您的作品已被接收');
		$('#id_p2').html('请耐心等候');
		activeIndex = 1;
	} else if (oupsStatus == 4) {
		// 初审
		$('#id_p1').html('您的作品正在初审中');
		$('#id_p2').html('请耐心等候');
		activeIndex = 2;
	} else if (oupsStatus == 5) {
		// 转码
		$('#id_p1').html('您的作品正在转码中');
		$('#id_p2').html('请耐心等候');
		activeIndex = 3;
	} else if (oupsStatus == 6) {
		// 专业审核
		$('#id_p1').html('您的作品正在专业审核中');
		$('#id_p2').html('请耐心等候');
		activeIndex = 4;
	} else if (oupsStatus == 8) {
		// 上线
		var resStatus=json_data.resStatus;
		if(resStatus==1){
			dealVideo(json_data.fileId);
		}else{
			$('#id_p1').html('作品关联视频下线');
		}
		activeIndex = 5;
	} else {
		// 不合格
		$('div.video-upload-ing').find('div.icon').removeClass(
				'icon-upload-thin').addClass('icon-cross');
		$('#id_p1').html('您的作品审核不合格');
		$('#id_p2').html('请关注管理员评论');
	}

	// 处理进度条
	if (!!activeIndex) {
		dealProcessUl(activeIndex);
	}

	// 处理简介信息
	$('.page_title').html(json_data.name);
	$('#oups_name').html(json_data.name);
	$('#video_speaker').html(json_data.speakerName);
	$('#video_time').html(json_data.oupsTime);
	$('#video_name_s').html(json_data.name);
	$('#video_date').html(json_data.dateTime);
	$('#video_intro').html(json_data.desc);

	// 绑定发送评论事件
	var video_comments_text = $('#video_comments_text')
	$('#sub_comments').click(function() {
		var value = video_comments_text.val();
		if (value == '') {
			video_comments_text.focus();
			video_comments_text.addClass('alert');
			return false;
		}
		$.post('/api/webchat/wx/oups/add/comments',{
			'uuid':json_data.uuid,
			'common':value
		},function(data){
			if(data.status=='success'){
				//刷新
				video_comments_text.val('');
				dropload.opts.loadUpFn(dropload);
//				setTimeout(function(){
//				},1000);
			}else{
				alert(data.message);
			}
		});
	});

	video_comments_text.on('keyup', function() {
		$(this).removeClass('alert');
	});

	// 获取评论
	// 实例化下拉插件
	droploadList=dataDropload($('#oups_comments_div'), $('#oups_comments_ul'),
			'/api/webchat/wx/oups/comments/list/'+json_data.uuid, buildDataList, buildUl,'up');
	setTimeout(function(){
		$(droploadList.$domDown).find('div.dropload-noData').html('暂无更多反馈');
	},1000);


}

// 处理状态进度条
function dealProcessUl(tagerIndex) {
	$('ul.upload-process').find('li').each(function(index, item) {
		if (tagerIndex == index&&tagerIndex!=5) {
			$(item).addClass("active");
			return false;
		}
		$(item).addClass("done");
	});

}

// 处理视频封面
function dealVideo(fileId) {
	$('#id_video_container').empty();
	var option = {
		"auto_play" : "0",
		"file_id" : fileId,
		"app_id" : "1251442335",
		"width" : 640,
		"height" : 480
	}; /* 调用播放器进行播放 */
	new qcVideo.Player("id_video_container", option);
}

function delCommon(obj){
	var uuid=$(obj).data('uuid');
	$.post('/api/webchat/wx/oups/del/comments',{
		'uuid':uuid
	},function(data){
		if(data.status=='success'){
			//刷新
			dropload.opts.loadUpFn(dropload);
//			setTimeout(function(){
//			},1000);
		}else{
			alert(data.message);
		}
	});
}
