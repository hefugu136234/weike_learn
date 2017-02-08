$(document).ready(function(){
  // activeNav('nav_index');

  // 头部筛选图标
  $('#category_tabs').find('li').each(function(){
    var $that = $(this);
    var $icon = $that.find('.icon');
    itemAddIcons($that, $icon);
  });

  /**
   * 分页操作
   */
  var rest_count=$('#rest_count').val();
  rest_count=parseInt(rest_count);
  var pageObject=pageObj(rest_count,10,1);
  pageControllerInit(pageObject,pagecontainer,getRequest);
});

var pagination_ul,selectUuid,res_list_div;
function pagecontainer(){
	if(!pagination_ul){
		  pagination_ul=$('#pagination_ul');
	  }
	return pagination_ul;
}

function getRequest(obj){
	if(!selectUuid){
		selectUuid=$('#selectUuid').val();
	}
	if(!res_list_div){
		res_list_div=$('#res_list_div');
	}
	  //请求数据
	  $.getJSON('/f/web/sub/subject/page', {
			'size' : obj.batchSize,
			'currentPage' : obj.currentPage,
			'uuid' : selectUuid
		}, function(data) {
			if (isSuccess(data)) {
				var itemList = data.subResList;
				obj.total=data.subResCount;
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
			li_item=li_item+item_div;
		});
	}else{
		li_item=buildNoiItem();
	}
	ul.append(li_item);
}

//每一个资源div
function buildHasItem(item){
	var url='/f/web/resource/detail/'+item.uuid;
  var type = item.type;
  var name = item.name;
  var bloody = item.bloody;
  name = cutStrForNum(name, 90);

  var fileClass = '', filename = '';
  if (type == 'VIDEO') {
    fileClass = 'video-type';
    filename = '视频';
  } else if (type == 'PDF') {
    fileClass = 'pdf-type';
    filename = 'PDF';
  } else if (type == 'THREESCREEN') {
    fileClass = 'ppt-type';
    filename = '课件';
  } else if (type == 'NEWS') {
    //return drawResNewsItem(item);
    fileClass = 'file-type';
    filename = '资讯';
  } else if (type == 'VR') {
    fileClass += ' vr-type';
    filename = 'VR';
  }

	var item_div = '<div class="item"><div class="media"><div class="media-left img">'
  		         + '<a href="'+url+'"><img class="media-object" src="'+item.cover+'" alt="">';
  if (bloody){
    item_div += '<div class="bloody-icon"><img alt="" src="/assets/img/app/icon_bloody.png"></div>';
  }
  item_div += '<div class="img-tag tr ' + fileClass + '">' + filename + '</div>'
	         + '<div class="img-tag br"><span class="icon icon-eye"></span>'+item.viewCount
	         + '</div></a></div><div class="media-body info"><h4 class="media-heading tt">'
	         + '<a href="'+url+'">'+name+'</a>'
	         + '</h4><p><span>'+item.code+'</span> | <span>'+item.speakerName+'</span>'
	         + '</p><p><span>'+item.catgoryName+'</span> | <span>'+item.hospitalName+'</span>'
	         + '</p></div></div></div>';

  return item_div;
}

//当没有内容的显示
function buildNoiItem(){
	var item='<div class="content-empty"><div class="icon icon-empty"></div>'
			 +'<div class="tips"><p>内容暂无</p><p>去其他栏目看看吧</p></div></div>';
	return item;
}
