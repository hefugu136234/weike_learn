
$(function(){
	var droploadList,comment_body_uuid,comment_body_type,body_url,common_request,
	$comments_content,$comments_list,$reply_text,$comments_text,$comments_reply_modal;
	body_url=location.href.split('#')[0];
	comment_body_uuid=$('#comment_body_uuid').val();
	comment_body_type=$('#comment_body_type').val();
	comment_body_type=parseInt(comment_body_type);
	
	//获取
	var requestObjComment=function(type){
		if(type==5){
			//线下活动
			return {
				type:5,
				pull_list:'/api/webchat/offline/activity/comment/list',
				add_data:'/api/webchat/offline/activity/add/comment'};
		}
	};
	
	//要请求的数据
	common_request=requestObjComment(comment_body_type);
	$comments_content=$('#comments_content');
	$comments_list=$('#comments_list');
	$reply_text=$('#reply_comments_text');
	$comments_text=$('#resource_comments_text');
	$comments_reply_modal=$('#comments_reply_modal');
	
	var checkContent=function(content) {
		if (!content) {
			AlertClassTip('请输入内容再提交') ;
			return false;
		}
		if (content.length > 240) {
			AlertClassTip("您输入的内容过多，请重新输入");
			return false ;
		}
		return true ;
	};
	
	var buildData=function(content, type,parentId) {
		$.post(common_request.add_data,{
			uuid:comment_body_uuid,
			parentId:parentId,
			content:content,
			type:type
		},function(data) {
			if (isSuccess(data)) {
				$comments_text.val("") ;
				var commentVo = data.data ;
				var item = buildCommentItem(commentVo) ;
				$comments_list.prepend(item);
				//隐藏‘暂无更多数据’
				var nodata=$comments_content.find('div.dropload-noData');
				if(isItemList(nodata)){
					$(nodata).addClass('hide');
				}
				var score = commentVo.integral;
				if (score > 0) {
					//显示增加积分
					scoreTipObj().show(score);
				}
			} else {
				if(data.code==511){
					loginAlert(body_url);
					return false;
			   }
			   if(!!data.message){
				   AlertClassTip(data.message);
				   return false;
			   }else{
				   AlertClassTip('数据出错');
				   return false;
			  }
			}
		});
	};
	

	var replace_em=function(str){
	    str = str.replace(/\</g,'<；');
	    str = str.replace(/\>/g,'>；');
	    str = str.replace(/\n/g,'<；br/>；');
	    str = str.replace(/\[em_([0-9]*)\]/g,'<img src="/assets/img/qqFace/$1.gif" border="0" />');
	    return str;
	};

	var dateFormat=function(time) {
		var date = new Date(time) ;
		return customFormat(date,"#YYYY#-#MM#-#DD# #hhh#:#mm#") ;
	};

	var dateToString=function(time) {
		var date = new Date().getTime();
		var temp = (date - time)/1000 ;
		if (temp >=0 && temp< 5) {
			return "刚刚";
		} else if (temp>=5 && temp < 60) {
			return Math.floor(temp)+"秒前";
		} else if (temp >= 60 && temp < 60*60) {
			return  Math.floor(temp/60)+"分钟前";
		} else if (temp >= 60*60 && temp < 60*60*24) {
			return  Math.floor(temp/(60*60)) + "小时前";
		} else {
			return dateFormat(time);
		}
	};
	
	// 必须实现方法
	var dropListDeal=function(data){
		var list = data.commentVos;
		if (!!list && list.length > 0) {
			var time = list[list.length - 1].createTime;
			droploadList.listStartTime = dateFormat(time);
		}
		return list;
	};
	
	var buildCommentItem=function(item) {
		var li = $("<li class='clearfix box' data-id='"+item.id+"'></div>") ;
		var photo = $("<div class='photo'><img src='"+ item.userView.photo + "' alt=''></div>");
		li.append(photo);
		var detail = $( "<div class='detail'></div>");
		if (item.parent == null) {
			var h5 = $( "<h5 class='name'>" + item.userView.showName + "</h5>");
			detail.append(h5);
		} else {
			var h5 = $("<h5 class='name'>" + item.userView.showName + "回复了"+ item.parent.userView.showName + "</h5>");
			detail.append(h5);
		}
		var content = $("<div class='con'>"+ replace_em(item.content) +"</div>")
		detail.append(content);
		var time = $( "<div class='time'>"+ dateToString(item.createTime)+ "</div>");
		detail.append(time);
		li.append(detail);
		var operation = $("<div class='operation'></div>");
		var reply = $("<div class='btn reply-btn'><span class='icon icon-reply'></span>回复</div>");
		reply.click(function(){
			var reply_id =li.data('id');
			$comments_reply_modal.data('id',reply_id);
			$comments_reply_modal.fadeIn().addClass('active');
			$reply_text.val('');
			$reply_text.focus();
		});
		operation.append(reply);
		if (item.isPraise) {
			var zan = $("<div class='btn zan-btn'><span class='icon icon-like-solid'></span><span class='num'>"+item.praiseNum+"</span></div>");
		} else {
			var zan = $("<div class='btn zan-btn'><span class='icon icon-like'></span><span class='num'>"+item.praiseNum+"</span></div>");
		}
		zan.on('click', function(e) {
			e.preventDefault();
			var $icon = $(this).find('.icon');
			var $num = $(this).find('.num');
			var target_id = li.data('id');
			var num = parseInt($num.text());
			var flag = $(this).find('.icon-like');

			$.post('/api/webchat/res/praise/comment', {
				id: target_id
			}, function(data) {
				if (isSuccess(data)) {
					if (flag.length>0) {
						$icon.removeClass('icon-like');
						$icon.addClass('icon-like-solid');//点过
						$num.text('');
						$num.text(num+1)
					} else {
						$icon.removeClass('icon-like-solid');
						$icon.addClass('icon-like');
						$num.text('');
						$num.text(num-1)
					}
				} else {
					if(data.code==511){
						loginAlert(body_url);
						return false;
				   }
				   if(!!data.message){
					   AlertClassTip(data.message);
					   return false;
				   }else{
					   AlertClassTip('数据出错');
					   return false;
				  }
				}
			});
		});
		operation.append(zan);

		if (item.isDelete) {
			var deleteComment = $("<div class='btn del-btn'>删除</div>");
			deleteComment.click(function(){
				if (confirm('您确定要删除这条评论？')) {
					var target_id = li.data('id');
					$.post('/api/webchat/res/delete/comment', {
						id:target_id
					}, function(data) {
						if (isSuccess(data)) {
							li.fadeOut(function(){
								li.remove();
							});
						} else {
							if(data.code==511){
								loginAlert(body_url);
								return false;
						   }
						   if(!!data.message){
							   AlertClassTip(data.message);
							   return false;
						   }else{
							   AlertClassTip('数据出错');
							   return false;
						  }
						}
					})
				} else {
					return false;
				}
			});
			operation.append(deleteComment);
		}
		li.append(operation);
		return li;
	};
	
	var buildUl=function(ul, itemList){
		$.each(itemList, function(index, item) {
			var li = buildCommentItem(item);
			ul.append(li);
		});
	};
	
	var dropload_request=common_request.pull_list+'?uuid='+comment_body_uuid;
	//请求数据
	droploadList = dataDropload($comments_content,$comments_list,
			dropload_request,dropListDeal,buildUl,'up');
	
	$('#sub_comments').click(function(){
		var content = $comments_text.val() ;
		var flag = checkContent(content) ;
		if (!flag) {
			return false ;
		}
		buildData(content,1) ;
	});
	
	$comments_reply_modal.find('div.btn.sub.sub-reply').click(function(e){
		var reply_id=$comments_reply_modal.data('id');
		var content = $reply_text.val();
		var flag = checkContent(content);
		if (!flag) {
			return false ;
		}
		buildData(content,2,reply_id);
		$reply_text.val('') ;
		$comments_reply_modal.data('id','');
	});
	
	
});







