$(document).ready(function(){
  activeNav('nav_activity');

  activity_uuid = $('#activity_uuid').val();
  var allOupsCount=$('#allOupsCount').val();
  allOupsCount=parseInt(allOupsCount);
  var pageObject=pageObj(allOupsCount,10,1);
  pageControllerInit(pageObject,pagecontainer,getRequest);
});

var allOupsList_con,allOupsList_page, activity_uuid;

function pagecontainer(){
  if(!allOupsList_page){
    allOupsList_page=$('#allOupsList_page');
    }
  return allOupsList_page;
}

function getRequest(obj){
  if(!allOupsList_con){
    allOupsList_con=$('#allOupsList_con');
  }
    //请求数据
    $.getJSON('/f/web/activity/resource/page', {
      'size' : obj.batchSize,
      'uuid' : activity_uuid,
      'currentPage' : obj.currentPage
    }, function(data) {
      if (isSuccess(data)) {
        var itemList = data.subResList;
        obj.total=data.subResCount;
        buildUl(allOupsList_con,itemList);
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
             +'<a href="'+url+'"><img class="media-object" src="'+item.cover+'" alt="">'
             +'<div class="img-tag tr ' + fileClass + '">' + filename + '</div>';
  if (bloody){
    item_div += '<div class="bloody-icon"><img alt="" src="/assets/img/app/icon_bloody.png"></div>';
  }
  item_div += '<div class="img-tag br"><span class="icon icon-eye"></span>'+item.viewCount
           + '</div></a></div><div class="media-body info"><h4 class="media-heading tt">'
           + '<a href="'+url+'">'+name+'</a>'
           + '</h4><p><span>'+item.code+'</span> | <span>'+item.speakerName+'</span>'
           + '</p><p><span>'+item.catgoryName+'</span> | <span>'+item.hospitalName+'</span>'
           + '</p></div></div></div>';
  return item_div;
}
