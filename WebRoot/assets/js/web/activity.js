$(document).ready(function(){
  activeNav('nav_activity');

  // 活动首页最新直播滚动
  if (typeof Swiper != 'undefined'){
    var activity_new_swiper = new Swiper('#activity_new_list', {
      paginationClickable: true,
      nextButton: '#activity_new_next',
      prevButton: '#activity_new_prev',
      pagination: '#activity_new_pagination',
      slidesPerView: 4,
      spaceBetween: 24
    });
  }

  var liveCount=$('#liveCount').val();
  liveCount=parseInt(liveCount);
  var pageObject=pageObj(liveCount,10,1);
  pageControllerInit(pageObject,pagecontainer,getRequest);
});

var wondeReviewList_div,wondeReviewList_page;

function pagecontainer(){
	if(!wondeReviewList_page){
		wondeReviewList_page=$('#wondeReviewList_page');
	  }
	return wondeReviewList_page;
}

function getRequest(obj){
	if(!wondeReviewList_div){
		wondeReviewList_div=$('#wondeReviewList_div');
	}
	  //请求数据
	  $.getJSON('/f/web/wonder/live/record', {
			'size' : obj.batchSize,
			'currentPage' : obj.currentPage
		}, function(data) {
			if (isSuccess(data)) {
				var itemList = data.wondeReviewList;
				obj.total=data.liveCount;
				buildUl(wondeReviewList_div,itemList);
				pageControllerInit(obj, pagecontainer,getRequest);
			}
		});
}

function buildUl(ul,itemList){
	ul.empty();
	var li_item='';
	if(isItemList(itemList)){
		$.each(itemList,function(index,item){
			var item_div=buildUlItem(item);
			li_item=li_item+item_div;
		});
	}
	ul.append(li_item);
}

function buildUlItem(item){
	var url = '/f/web/live/detail/'+item.uuid;
	var cover = item.cover;
  var name = item.name;
  name = cutStrForNum(name, 40);

	if(!cover){
		cover='/assets/img/web/placeholder.jpg';
	}
	var item_div='<div class="item"><div class="media"><div class="media-left img">'
                 +'<a href="'+url+'"><img class="media-object" src="'+item.cover+'" width="132" alt="">'
                 +'<div class="img-tag br"><span class="icon icon-users"></span>'+item.bookNum+'</div></a></div>'
                 +'<div class="media-body info"><h4 class="media-heading tt"><a href="'+url+'">'+name+'</a></h4>'
                 +'<p class="end-time">结束：'+item.endDate+'</p></div></div></div>';
	return item_div;
}
