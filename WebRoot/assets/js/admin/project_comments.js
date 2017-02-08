var pagination_ul, comment_list_div, target, content, range_start, range_end;

$(document).ready(function() {
	activeStub('comment-list-nav');
	
	// TimeRange Init
	$('#timeRange .input-daterange').datepicker({
		format: 'yyyy-mm-dd',
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true
    });

	// 加载数据
	var pageSize = 20;
	target = $('#target');
	content = $('#content');
	range_start = $('#range_start');
	range_end = $('#range_end');
	pullContnet(pageSize);
	
	target.keyup(function(){
		pullContnet(pageSize);
	})
	content.keyup(function(){
		pullContnet(pageSize);
	})
	range_start.change(function(){
		pullContnet(pageSize);
	})
	range_end.change(function(){
		pullContnet(pageSize);
	})

	// 清空过滤条件
	$('#clean').click(function(){
		target.val('');
		content.val('');
		range_start.val('');
		range_end.val('');
		pullContnet(pageSize);
	})
})

function pullContnet(pageSize){
	$.post('/project/resource/comments/data', {
		'pageSize' : pageSize,
		'currentPage' : 1,
		'target': target.val(),
		'content': content.val(),
		'rangeStart': range_start.val(),
		'rangeEnd': range_end.val()
	}, function(data){
		if (isSuccess(data)) {
			console.log(data);
			var comment_block = $('#comment_list') ;
			var rest_count = data.resCommentCount;
			var itemList = data.commentVos;
			$('#comment_num').html(rest_count+" Messages");
			buildUl(comment_block, itemList);
			var pageObject=pageObj(rest_count, pageSize, 1);
			pageControllerInit(pageObject,pagecontainer,getRequest);
		} else {
			var item='<div class="content-error"><div class="icon icon-empty"></div>'
				 +'<div class="tips"><p>评论加载出错</p></div></div>';
			comment_block.empty();
			comment_block.append(item);
		}
	});
}

function pagecontainer(){
	if(!pagination_ul){
		  pagination_ul=$('#pagination_ul');
	  }
	return pagination_ul;
}

function getRequest(obj){
	if(!comment_list_div){
		comment_list_div=$('#comment_list');
	}
    $.post('/project/resource/comments/data', {
		'pageSize' : obj.batchSize,
		'currentPage' : obj.currentPage,
		'target': target.val(),
		'content': content.val(),
		'rangeStart': range_start.val(),
		'rangeEnd': range_end.val()
	}, function(data) {
		if (isSuccess(data)) {
			var itemList = data.commentVos;
			buildUl(comment_list_div, itemList);
			pageControllerInit(obj, pagecontainer,getRequest);
		}
	});
}

function buildUl(ul,itemList){
	ul.empty();
	if(!!itemList && itemList.length > 0){
		$.each(itemList, function(index, item){
			var item_div=buildItem(item);
			ul.append(item_div);
		});
		gotoTop();
	}else{
		var item='<div class="content-error"><div class="icon icon-empty"></div>'
			 +'<div class="tips"><p>暂无评论内容或无匹配项</p></div></div>';
		ul.append(item);
	}
}

function buildItem(item){
	var item_div = $('<div class="feed-element" data-id="' + item['id'] + '"></div>');
	var head_img = $('<a href="javascript:void(0)" class="pull-left"> <img alt="用户头像" class="img-circle" src="' + 
							item['userView']['photo']+ '"></a>');
	head_img.click(function(){
		userInfo(item['userView']['uuid']);
	});
	item_div.append(head_img);
	
	var message_body = $('<div class="media-body"></div>');
	if(!!item['parent']){
		message_body.append('<small class="pull-right">' + dateFormat(item['createTime']) + '</small>' + 
								'<strong>' + item['userView']['showName'] + '</strong> 回复了 <strong>' + 
								item['parent']['userView']['showName'] + '</strong><br>' + 
							'<small class="text-muted">' + dateToString(item['createTime']) + '</small>');
	}else if(!!item['resource']){
		message_body.append('<small class="pull-right">' + dateFormat(item['createTime']) + '</small>' + 
								'<strong>' + item['userView']['showName'] + '</strong> 评论了资源: <strong><font color="green">' + 
									item['resource']['name'] + '</font></strong><br>' + 
							'<small class="text-muted">' + dateToString(item['createTime']) + '</small>');
	}else{
		message_body.append('<small class="pull-right">' + dateFormat(item['createTime']) + '</small>' + 
								'<strong>' + item['userView']['showName'] + '</strong> 参与了评论 <br>' + 
							'<small class="text-muted">' + dateToString(item['createTime']) + '</small>');
	}
	var body_content = $('<div class="well"><div class="comment-detail">' + replace_em(item['content']) + '</div></div>');
	message_body.append(body_content);

	var option = $('<div class="pull-right"></div>');
	var check;
	if(1 == item['_status']){
		check = $('<a style="margin-left:10px" class="label label-primary">已审核</a>');
	}else{
		check = $('<a style="margin-left:10px" class="label label-warning">审核</a>');
		check.click(function(){
			$.post('/project/resource/comments/update', {
				commentUuid : item['uuid']
			}, function() {
			}).done(function() {
			}).fail(function() {
			}).always(function(data) {
				if(!! data['message']){
					check.removeClass('label-warning');
					check.addClass('label-primary');
					check.html('已审核');
					//alert(data['message']);
				}else{
					alert(data['status'])
				}
			})
		});
	}
	option.append(check);
	
	var detail = $('<a style="margin-left:10px" class="label label-primary">详情</a>');
	detail.click(function(){
		var id = item_div.data('id') ;
		$.post("/f/web/res/detail/comment", {
			id:id
		}, function(data) {
			if (isSuccess(data)) {
				var itemList = data.commentVos;
				var res_list_div = $("#dialog_comments_list") ;
				buildDetail(res_list_div,itemList);
				$('#commonsDetailModal').modal('show');
			} else {
				alert(data.message);
			}
		})
	})
	option.append(detail);
	
	var remove = $('<a style="margin-left:10px" class="label label-danger">删除</a>');
	remove.click(function(){
		if(confirm("该评论下的所有子评论也会被删除，是否确认?")){
			$.post('/project/commentUser/remove', {
				commentUuid : item['uuid']
			}, function() {
			}).done(function() {
			}).fail(function() {
			}).always(function(data) {
				if(!! data['message']){
					//window.location.reload();
					//alert(data['message']);
					pullContnet(20);
				}else{
					alert(data['status'])
				}
			})
		}
	});
	option.append(remove);
	
	message_body.append(option);
	item_div.append(message_body);
	return item_div;
}

function buildDetail(container,itemList) {
	container.empty();
	var li_item = "";
	$.each(itemList,function(index,item) {
		var item_div = buildDetailItem(item) ;
		container.append(item_div);
	});
}

function buildDetailItem(item) {
	if (item.parent == null) {
		var li = $( "<li class='item clearfix' data-id='"+item.id+"'>");
	} else {
		var li = $("<li class='item' data-id='"+item.id+"'>");
	}
	var time = $("<div class='time'>"+ dateFormat(item.createTime) +"</div>");
	li.append(time);
	var photo = $("<div class='photo'><img class='img-circle' src='"+item.userView.photo+"'alt='用户头像'></div>") ;
	li.append(photo);
	var detail = $("<div class='detail'></div>");
	var div_1 = $("<div class='comment-detail'></div>");
	
	if(!!item.parent){
		var name = $("<span class='name'>"+item.userView.showName+"</span> 回复了 <span class='name'>"+item.parent.userView.showName+"</span>");
		div_1.append(name);
		div_1.append(" : "+replace_em(item.content));
	}else if(!!item.resource){
		var name = $("<span class='name'>"+item.userView.showName+"</span> 评论资源 <span class='name'>"+item.resource.name+"</span>");
		div_1.append(name);
		div_1.append(" : "+replace_em(item.content));
	}else{
		var name = $("<span class='name'>"+item.userView.showName+"</span> 参与了评论");
		div_1.append(name);
		div_1.append(" : "+replace_em(item.content));
	}
	detail.append(div_1);
	li.append(detail);
	return li ;
}


/* ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝ */
function isSuccess(data) {
	if (data.status == 'success') {
		return true
	}
	return false;
}

var pageObj = function(total, batchSize, currentPage) {
	var pageObject = new Object();
	pageObject.total = total;
	pageObject.batchSize = batchSize;
	pageObject.currentPage = currentPage;
	return pageObject;
}

function pageControllerInit(pageObject, containerCallback, requestCallback) {
	var total = pageObject.total;
	var batchSize = pageObject.batchSize;
	var currentPage = pageObject.currentPage;
	var container = containerCallback();
	container.empty();
	if (total == 0) {
		return;
	}
	var show_interval = 3;
	var pages = Math.ceil(total / batchSize);
	var pre = $('<li data-page="' + Math.max(1, currentPage - 1)
			+ '"><a href="javascript:void(0);">上一页</a></li>');
	var next = $('<li data-page="' + Math.min(pages, currentPage + 1)
			+ '"><a href="javascript:void(0);" >下一页</a></li>');
	var first = $('<li data-page="1"><a href="javascript:void(0);" >1</a></li>');
	var tail = $('<li data-page="' + pages + '"><a href="javascript:void(0);">'
			+ pages + '</a></li>');
	var others = $('<li><span>…</span></li>')
	container.append(pre);
	container.append(first);
	if (currentPage - show_interval > 2) {
		container.append(others.clone());
	}
	var max_show = 1;
	for (var i = currentPage - show_interval; i <= currentPage + show_interval; i++) {
		if (i > 1 && i < pages) {
			var item = $('<li data-page="' + i
					+ '"><a href="javascript:void(0);">' + i + '</a></li>');
			container.append(item);
			if (currentPage == i) {
				item.addClass('active')
			}
			max_show = i;
		}
	}
	if (max_show < pages - 1) {
		container.append(others.clone());
	}
	if (pages > 1) {
		container.append(tail);
	}
	container.append(next);
	if (currentPage == 1) {
		pre.addClass('disabled');
		first.addClass('active');
	}
	if (currentPage == pages) {
		next.addClass('disabled');
		tail.addClass('active');
	}
	container.find('li').each(function(i, e) {
		var $e = $(e);
		if ($e.hasClass('disabled') || $e.hasClass('active')) {
			return;
		}
		var page = $e.data("page")
		if (page > 0) {
			$e.click(function(e) {
				console.log('当前点击：' + page);
				pageObject.currentPage = page;
				requestCallback(pageObject);
			})
		}
	});
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

var customFormat = function(date, formatString) {
	var YYYY, YY, MMMM, MMM, MM, M, DDDD, DDD, DD, D, hhhh, hhh, hh, h, mm, m, ss, s, ampm, AMPM, dMod, th;
	var dateObject = date;
	YY = ((YYYY = dateObject.getFullYear()) + "").slice(-2);
	MM = (M = dateObject.getMonth() + 1) < 10 ? ('0' + M) : M;
	MMM = (MMMM = [ "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" ][M - 1])
			.substring(0, 3);
	DD = (D = dateObject.getDate()) < 10 ? ('0' + D) : D;
	DDD = (DDDD = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
			"Friday", "Saturday" ][dateObject.getDay()]).substring(0, 3);
	th = (D >= 10 && D <= 20) ? 'th' : ((dMod = D % 10) == 1) ? 'st'
			: (dMod == 2) ? 'nd' : (dMod == 3) ? 'rd' : 'th';
	formatString = formatString.replace("#YYYY#", YYYY).replace("#YY#", YY)
			.replace("#MMMM#", MMMM).replace("#MMM#", MMM).replace("#MM#", MM)
			.replace("#M#", M).replace("#DDDD#", DDDD).replace("#DDD#", DDD)
			.replace("#DD#", DD).replace("#D#", D).replace("#th#", th);

	h = (hhh = dateObject.getHours());
	if (h == 0)
		h = 24;
	if (h > 12)
		h -= 12;
	hh = h < 10 ? ('0' + h) : h;
	hhhh = hhh < 10 ? ('0' + hhh) : hhh;
	AMPM = (ampm = hhh < 12 ? 'am' : 'pm').toUpperCase();
	mm = (m = dateObject.getMinutes()) < 10 ? ('0' + m) : m;
	ss = (s = dateObject.getSeconds()) < 10 ? ('0' + s) : s;
	return formatString.replace("#hhhh#", hhhh).replace("#hhh#", hhh).replace(
			"#hh#", hh).replace("#h#", h).replace("#mm#", mm).replace("#m#", m)
			.replace("#ss#", ss).replace("#s#", s).replace("#ampm#", ampm)
			.replace("#AMPM#", AMPM);
}