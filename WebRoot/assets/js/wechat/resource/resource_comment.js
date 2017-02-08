var droploadList;
var curUuid;
var curCommentId;
$(document).ready(function() {
//	var replyResurce = $('#resource_comments_text');
//	$('.resource_emotion').click(function(e) {
//		emotion(this,replyResurce,e);
//	})
//	var replyComment = $("#reply_comments_text");
//	$('.comment_emotion').click(function(e) {
//		emotion(this,replyComment,e);
//	})
	var createDate = getNowFormatDate();
	curUuid = $("#resource_uuid").val();
	// 实例化下拉插件
	droploadList = dataDropload($('#comments_content'),$('#comments_list'),'/api/webchat/res/page/comment?uuid='+curUuid, buildDataList,buildUl, 'up');
	// droploadList.lock('up');锁住上方
});

// 必须实现方法
function buildDataList(data) {
	var list = data.commentVos;
	if (!!list && list.length > 0) {
		var time = list[list.length - 1].createTime;
		droploadList.listStartTime = dateFormat(time);
	}
	return list;
}

function buildUl(scoreHisList_ul, itemList) {
	var item_li = '';
	$.each(itemList, function(index, item) {
		var li = buildCommentItem(item);
		scoreHisList_ul.append(li);
		//item_li = item_li+li ;
	});
	//scoreHisList_ul.append(item_li);
}

function buildCommentItem(item) {
	//console.log(item);
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
		curCommentId =li.data('id');
		$('#comments_reply_modal').fadeIn().addClass('active');
		$('#reply_comments_text').focus();
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
		curCommentId = li.data('id');
		var num = parseInt($num.text());
		var flag = $(this).find('.icon-like');

		$.post('/api/webchat/res/praise/comment', {
			id: curCommentId
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
					var url='/api/webchat/resource/first/view/'+curUuid;
					loginAlert(url);
			   }
			   if(!!data.message){
				   alert(data.message);
			   }else{
				   alert('数据出错');
			  }
			}
		});
	});
	operation.append(zan);

	if (item.isDelete) {
		var deleteComment = $("<div class='btn del-btn'>删除</div>");
		deleteComment.click(function(){
			if (confirm('您确定要删除这条评论？')) {
				curCommentId = li.data('id');
				$.post('/api/webchat/res/delete/comment', {
					id:curCommentId
				}, function(data) {
					if (isSuccess(data)) {
						li.fadeOut(function(){
							li.remove();
						});
					} else {
						if(data.code==511){
							var url='/api/webchat/resource/first/view/'+curUuid;
							loginAlert(url);
					   }
					   if(!!data.message){
						   alert(data.message);
					   }else{
						   alert('数据出错');
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

}

function addComment() {
	var $this = $("#comments_list");
	var content = $("#resource_comments_text").val() ;
	var flag = checkContent(content) ;
	if (!flag) {
		return false ;
	}
	buildData(content,1,$this) ;
}


function replyComment() {
	var $this = $("#comments_list");
	var content = $('#reply_comments_text').val() ;
	var flag = checkContent(content) ;
	if (!flag) {
		return false ;
	}
	buildData(content,2,$this);
	$('#reply_comments_text').val('') ;
}

function cancelModel() {
	$('#reply_comments_text').val('');
	$('#comments_reply_modal').fadeOut()
			.removeClass('active');
}


function buildData(content, type, $this) {
	if (type ==1) {
		curCommentId = 0 ;
	}
	$.post("/api/webchat/res/add/comment",{
		uuid:curUuid,
		parentId:curCommentId,
		content:content,
		type:type
	},function(data) {
		if (isSuccess(data)) {
			console.log(data);
			$("#resource_comments_text").val("") ;
			var commentVo = data.data ;
			var item = buildCommentItem(commentVo) ;
			$this.prepend(item) ;

			var score = commentVo.integral;
			if (score > 0) {
				//显示增加积分
				scoreTipObj().show(score);
			}
		} else {
			if(data.code==511){
				var url='/api/webchat/resource/first/view/'+curUuid;
				loginAlert(url);
		   }
		   if(!!data.message){
			   alert(data.message);
		   }else{
			   alert('数据出错');
		  }
		}
	});
}

function checkContent(content) {
	if (content==null || content=='') {
		alert('请输入内容再提交') ;
		return false;
	}
	if (content.length > 240) {
		alert("您输入的内容过多，请重新输入");
		return false ;
	}
	return true ;
}

function replace_em(str){
    str = str.replace(/\</g,'<；');
    str = str.replace(/\>/g,'>；');
    str = str.replace(/\n/g,'<；br/>；');
    str = str.replace(/\[em_([0-9]*)\]/g,'<img src="/assets/img/qqFace/$1.gif" border="0" />');
    return str;
}

function dateFormat(time) {
	var date = new Date(time) ;
	return customFormat(date,"#YYYY#-#MM#-#DD# #hhh#:#mm#") ;
}

function dateToString(time) {
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
}
