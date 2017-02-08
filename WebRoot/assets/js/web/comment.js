var curUuid ;
$(document).ready(function() {
	var replyResurce = $('#comment_text_area');
	$('.resource_emotion').click(function(e) {
		emotion(this,replyResurce,e);
	})
	var rest_count = 0 ;
	curUuid = $("#res_uuid").val() ;
	var content = $("#comments_list") ;
	$.post("/f/web/res/page/comment", {
		uuid:curUuid,
		size:20,
		currentPage:1,
		first:"sign"
	}, function(data){
		if (isSuccess(data)) {
			console.log(data);
			rest_count = data.resCommentCount;
			var itemList = data.commentVos;
			buildUl(content,itemList);
			$("#comment_num").html("("+rest_count+")");
			var pageObject=pageObj(rest_count,20,1);
			pageControllerInit(pageObject,pagecontainer,getRequest);
		} else {
			var item='<div class="content-error"><div class="icon icon-empty"></div>'
				 +'<div class="tips"><p>评论加载出错</p></div></div>';
		}
	});
})


var pagination_ul,res_list_div;
function pagecontainer(){
	if(!pagination_ul){
		  pagination_ul=$('#pagination_ul');
	  }
	return pagination_ul;
}

function getRequest(obj){
	if(!curUuid){
		curUuid=$('#res_uuid').val();
	}
	if(!res_list_div){
		res_list_div=$('#comments_list');
	}
	  //请求数据
	  $.post('/f/web/res/page/comment', {
			'size' : obj.batchSize,
			'currentPage' : obj.currentPage,
			'uuid' : curUuid
		}, function(data) {
			if (isSuccess(data)) {
				var itemList = data.commentVos;
				buildUl(res_list_div,itemList);
				pageControllerInit(obj, pagecontainer,getRequest);
			}
		});
}

function buildUl(ul,itemList){
	ul.empty();
	var li_item='';
	if(isItemList(itemList)){
		$.each(itemList,function(index,item){
			var item_div=buildHasItem(item);
			if (index == 2) {
				console.log(item_div);
			}
			ul.append(item_div);
		});
	}else{
//		li_item=buildNoiItem();
	}
}

function buildDetail(container,itemList) {
	container.empty();
	var li_item = "";
	$.each(itemList,function(index,item) {
		var item_div = buildDetailItem(item) ;
		console.log(item_div);
		container.append(item_div);
	});
}

//每一个资源div
function buildHasItem(item){

	var item_div = $("<div class='media comments-item' data-id='"+item.id+"'></div>") ;
	var left = $("<div class='media-left photo'><img class='media-object img-circle' src='"+item.userView.photo+"' alt='用户头像'></div>") ;
	item_div.append(left);
	var body = $("<div class='media-body'></div>");
	var h4 = $("<h4 class='media-heading clearfix'></h4>");
	var pull_left= $("<div class='name pull-left'></div>");
	if (item.parent) {
		pull_left.append("<span class='name-html'>"+item.userView.showName+"</span> 回复 <span class='name-html'>"+item.parent.userView.showName+"</span>：");
	} else {
		pull_left.append("<span class='name-html'>"+item.userView.showName+"</span> 评论了：");
	}
	h4.append(pull_left);
	var pull_right= $("<div class='date pull-right'><span class='icon icon-clock'></span>"+dateToString(item.createTime)+"</div>");
	h4.append(pull_right);
	body.append(h4);
	body.append("<div class='comment-detail'>"+replace_em(item.content)+"</div>");
	var text_right = $("<div class='comment-operation text-right'></div>");
	var reply = $("<span class='item reply-btn'><i class='icon icon-reply'></i>回复</span>");
	reply.click(function(){
		reply_comment.show();
		reply_comment_text.focus();
	});
	text_right.append(reply);
	if (item.parent != null) {
		var dialog=$("<span class='item dialog-btn><i class='icon icon-detail'></i>查看对话</span>");
		dialog.click(function(){
			var id = item_div.data('id') ;
			$.post("/f/web/res/detail/comment", {
				id:id
			}, function(data) {
				if (isSuccess(data)) {
					var itemList = data.commentVos;
					var res_list_div = $("#dialog_comments_list") ;
					buildDetail(res_list_div,itemList);
					$('#dialog_comments').modal('show');
				} else {
					alert(data.message);
				}
			})
		});
		text_right.append(dialog);
	}

	if (item.isPraise) {
		var zan = $("<span class='item zan-btn'><i class='icon icon-like-solid'></i><i class='num'>"+item.praiseNum+"</i></span>");
	} else {
		var zan = $("<span class='item zan-btn'><i class='icon icon-like'></i><i class='num'>"+item.praiseNum+"</i></span>");
	}
	zan.on('click', function(e) {
		e.preventDefault();
		var $icon = $(this).find('.icon');
		var $num = $(this).find('.num');
		var id = item_div.data('id');
		var num = parseInt($num.text());
		var flag = $(this).find('.icon-like');
		$.post('/f/web/res/praise/comment', {
			id: id
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
					alert("请先在右上方微信登录后再点赞!");
		            return false;
				  }
				  if(!!data.message){
					 alert(data.message);
				  }else{
					 alert('数据出错');
				  }
			}
		});
	});
	text_right.append(zan);

	if (item.isDelete) {
		var delete_text = $("<span class='item delete-btn'><i class='icon icon-trash'></i>删除</span>");
		delete_text.click(function(){
			var id = item_div.data('id');
			if (confirm('您确定要删除这条评论？')){
				$.post('/f/web/res/delete/comment', {
					id:id
				}, function(data) {
					if (isSuccess(data)) {
						// alert('删除成功') ;
						item_div.fadeOut(function(){
							item_div.remove();
						});
					} else {
					   if(data.code==511){
						   alert("请先在右上方微信登录后再删除!");
						   return false;
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
		text_right.append(delete_text);
	}
	body.append(text_right);
	var reply_comment = $("<div class='form-horizontal reply-comments'></div>");
	var form_1 = $("<div class='form-group'></div>");
	var div_1 = $("<div class='col-sm-12'></div>");
	var reply_comment_text = $("<textarea class='form-control reply-comments-text' id='reply-comments-text' rows='2'></textarea>");
	var emotion_show = $('<div class="resource_emotion emoji-btn pull-left"><span class="icon icon-emoji"></span>插入表情</div>');
	emotion_show.click(function(e){
		emotion(this, reply_comment_text,e);
	});
	div_1.append(reply_comment_text);
	// div_1.append(emotion_show);
	form_1.append(div_1);
	reply_comment.append(form_1);
	var form_2 = $("<div class='form-group'></div>");
	var div_2 = $("<div class='col-sm-12'></div>");
	var cancel = $("<button type='button' class='btn btn-sm btn-link pull-right mr8 cancel-btn'>取消</button>");
	cancel.click(function(){
		reply_comment_text.val('');
		reply_comment.hide();
	});
	var submit = $("<button type='button' class='btn btn-sm btn-success pull-right'>提交回复</button>");
	submit.click(function(){
		var id = item_div.data('id') ;
		var content = reply_comment_text.val() ;
		var flag = checkContent(content) ;
		if (!flag) {
			return false ;
		}
		$.post('/f/web/res/add/comment',{
			uuid:curUuid,
			parentId:id,
			content:content,
			type:2
		}, function(data) {
			if (isSuccess(data)) {
				// alert("评论成功");
				reply_comment_text.val('');
				var item = data.data ;
				var li_item = buildHasItem(item);
				reply_comment.hide();
				$("#comments_list").prepend(li_item) ;

				var score = item.integral;
				if (score > 0) {
					addScoreTips(score);
				}
			} else {
				if(data.code==511){
					   alert("请先在右上方微信登录后再评论!");
					   return false;
				   }
				   if(!!data.message){
					   alert(data.message);
				   }else{
					   alert('数据出错');
				  }
			}
		});
	});
	div_2.append(emotion_show);
	div_2.append(submit);
	div_2.append(cancel);
	form_2.append(div_2);
	reply_comment.append(form_2);
	body.append(reply_comment);
	item_div.append(body);
	return item_div;

}

function buildDetailItem(item) {
	if (item.parent == null) {
		var li = $( "<li class='item clearfix' data-id='"+item.id+"'>");
	} else {
		var li = $("<li class='item' data-id='"+item.id+"'>");
	}
	var time = $("<div class='time'>"+dateToString(item.createTime)+"</div>");
	li.append(time);
	var photo = $("<div class='photo'><img class='img-circle' src='"+item.userView.photo+"'alt='用户头像'></div>") ;
	li.append(photo);
	var detail = $("<div class='detail'></div>");
	var div_1 = $("<div class='comment-detail'></div>");
	if (item.parent == null) {
		var name = $("<span class='name'>"+item.userView.showName+"</span>");
		div_1.append(name);
		div_1.append(" : "+replace_em(item.content));
	} else {
		console.log(item.parent)
		var name = $("<span class='name'>"+item.userView.showName+"</span> 回复 <span class='name'>"+item.parent.userView.showName+"</span>");
		div_1.append(name);
		div_1.append(" : "+replace_em(item.content));
	}
	console.log(replace_em(item.content));
	detail.append(div_1);
	var text_right = $("<div class='operation text-right'></div>");
	var reply_dialog_btn = $("<span class='item reply-dialog-btn'><i class='icon icon-reply'></i>回复</span>");
	reply_dialog_btn.click(function(){
		reply_dialog.show();
		reply_dialog_txt.focus();
	});
	text_right.append(reply_dialog_btn);
	detail.append(text_right);
	var reply_dialog = $("<div class='form-horizontal reply-dialog'></div>");
	var form1 = $("<div class='form-group'></div>");
	var div_2 = $("<div class='col-sm-12'></div>");
	var reply_dialog_txt = $("<textarea class='form-control reply-dialog-text' id='dialog-comments-text' rows='2'></textarea>");
	var dialog_emotion = $('<div class="resource_emotion emoji-btn emoji-sm pull-left"><span class="icon icon-emoji"></span>插入表情</div>');
	dialog_emotion.click(function(e){
		emotion(this,reply_dialog_txt,e);
	});
	div_2.append(reply_dialog_txt);
	// div_2.append(dialog_emotion);
	form1.append(div_2);
	reply_dialog.append(form1);
	var form2 = $("<div class='form-group'></div>");
	var div_3 = $("<div class='col-sm-12'></div>");
	var cancel = $("<button type='button' class='btn btn-xs btn-link mr8 pull-right cancel-btn'>取消</button>");
	cancel.click(function(){
		reply_dialog_txt.val('');
		reply_dialog.hide();
	});
	var submit = $("<button type='button' class='btn btn-xs btn-success pull-right'>提交回复</button>");
	submit.click(function(){
		var d_id = li.data('id') ;
		var d_content = reply_dialog_txt.val() ;
		var flag = checkContent(d_content) ;
		if (!flag) {
			return false ;
		}
		$.post('/f/web/res/add/comment',{
			uuid:curUuid,
			parentId:d_id,
			content:d_content,
			type:2
		}, function(data) {
			if (isSuccess(data)) {
				// alert("评论成功");
				reply_dialog_txt.val('');
				var item = data.data ;
				var li_item = buildHasItem(item);
				var d_li_item = buildDetailItem(item) ;
				reply_dialog.hide();
				$("#comments_list").prepend(li_item) ;
				$("#dialog_comments_list").prepend(d_li_item);
				 var score = item.integral;
					if (score > 0) {
						addScoreTips(score);
					}

			} else {
				if(data.code==511){
					   alert("请先在右上方微信登录后再评论!");
					   return false;
				   }
				   if(!!data.message){
					   alert(data.message);
				   }else{
					   alert('数据出错');
				  }
			}
		})
	});
	div_3.append(dialog_emotion);
	div_3.append(submit);
	div_3.append(cancel);
	form2.append(div_3);
	reply_dialog.append(form2);
	detail.append(reply_dialog);
	li.append(detail);
	return li ;

}

//当没有内容的显示
//function buildNoiItem(){
//	var item='<div class="content-empty"><div class="icon icon-empty"></div>'
//			 +'<div class="tips"><p>暂无评论</p></div></div>';
//	return item;
//}

function addComment() {
    var $this = $("#comments_list");
    var content = $("#comment_text_area").val() ;
    var flag = checkContent(content) ;
    if (!flag) {
        return false ;
    }
    $.post("/f/web/res/add/comment",{
        uuid:curUuid,
        parentId:0,
        content:content,
        type:1
    },function(data) {
    	console.log(data);
        if (isSuccess(data)) {
            // alert("评论成功");
            $("#comment_text_area").val("") ;
            var commentVo = data.data ;
            var item = buildHasItem(commentVo) ;
            if (!!$this.find(".content-empty")) {
                $this.find(".content-empty").remove();
            }
            $this.prepend(item) ;

            var score = commentVo.integral;
			if (score > 0) {
				addScoreTips(score);
			}
        } else {
        	if(data.code==511){
				   alert("请先在右上方微信登录后再评论!");
				   return false;
			   }
			   if(!!data.message){
				   alert(data.message);
			   }else{
				   alert('数据出错');
			  }
        }
    })
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
	return customFormat(date,"#YYYY#-#MM#-#DD# #hhh#:#mm# ") ;
}

function dateToString(time) {
	var date = new Date().getTime();
	var temp = (date - time)/1000 ;
	if (temp >=0 && temp< 5) {
		return "刚刚";
	}
	if (temp < 60) {
		return Math.floor(temp)+"秒前";
	} else if (temp >= 60 && temp < 60*60) {
		return  Math.floor(temp/60)+"分钟前";
	} else if (temp >= 60*60 && temp < 60*60*24) {
		return  Math.floor(temp/(60*60)) + "小时前";
	} else {
		return dateFormat(time);
	}
}


