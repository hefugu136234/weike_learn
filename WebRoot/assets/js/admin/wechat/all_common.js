var verify_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|17[4678]|18[0-9]|14[57])[0-9]{8}$/; // 验证手机号码
var verify_http = new RegExp("^(http).*");

$(document).ready(function() {
  // 限制字符串字数
  $('.limit-str-text').each(function() {
    var $that = $(this);
    var text = $that.html();
    var min_str = $that.data('minstr');
    var new_str = cutStrForNum(text, min_str);
    $that.html(new_str);
  });

  // 弹框关闭
  $('.feed-back-modal').find('.btn.cancel, .btn.sub, .cover').click(function() {
    $(this).parents('.feed-back-modal').fadeOut().removeClass('active');
  });

  // 单个滚动banner初始化
  var banners = $('.default-swiper');
  if (isItemList(banners)) {
    banners.each(function(index, item) {
      var $item = $(item);
      var swiper_pagination = $item.find('.swiper-pagination');
      new Swiper($item, {
        pagination : swiper_pagination,
        paginationClickable : true
      });
    });
  }

  // 组滚动banner初始化
  var default_group_swiper = $('.default-group-swiper');
  if (isItemList(default_group_swiper)) {
    default_group_swiper.each(function(index, item) {
      var $item = $(item);
      var li_length = $item.find('li').length;
      if (li_length < 3) {
        return true;// continue
      }
      new Swiper($item, {
        slidesPerView : 'auto',
        paginationClickable : true,
        loop : true,
        autoplay : 4000,
        autoplayDisableOnInteraction : false
      });
    });
  }

  // 标签切换
  $('.tab-control').find('.tab-control-tag').on('click', '.btn', function(){
    var $that = $(this);
    var i = $that.index();
    var $parent = $that.parents('.tab-control');
    var $tags = $parent.find('.tab-control-tag');
    var $content = $parent.find('.tab-content');

    $tags.find('li').removeClass('active');
    $that.addClass('active');
    $content.hide();
    $content.eq(i).show();
  });

  // 收藏的公共方法
  $('#my_collection').click(function(event) {
    event.preventDefault();
    var $this = $(this);
    var star = $this.find('.icon');
    var text = $this.find('.info');
    var num = $this.find('.num');
    var uuid = $this.data('uuid');
    $.post('/api/webchat/collection/status', {
      'uuid' : uuid
    }, function(data) {
      if (isSuccess(data)) {
        if (data.reStatus) {
          star.attr('class', 'icon icon-star');
          text.html('已收藏');
        } else {
          star.attr('class', 'icon icon-star-line');
          text.html('收藏');
        }
        num.html(data.reCount);
      } else {
        alert(data.message);
      }
    });
  });

  // 点赞的公共方法
  $('#my_zan').click(function(event) {
    event.preventDefault();
    var $this = $(this);
    var text = $this.find('.info');
    var num = $this.find('.num');
    var uuid = $this.data('uuid');
    $.post('/api/webchat/praise/status', {
      'uuid' : uuid
    }, function(data) {
      if (isSuccess(data)) {
        if (data.reStatus) {
          text.html('已赞');
        } else {
          text.html('赞');
        }
        num.html(data.reCount);
      } else {
        alert(data.message);
      }
    });
  });

  // 分享的公共方法
  $('#my_share').click(function(e) {
    e.preventDefault();
    $('#share_from_wechat').fadeIn();
  });

  // 分享方式隐藏
  $('#share_from_wechat').click(function() {
    $(this).fadeOut();
  });

});

// 字符串限制字数
function cutStrForNum(str, len) {
  var str_length = 0;
  var str_cut = new String();
  var result = str;

  for (var i = 0; i < str.length; i++) {
    a = str.charAt(i);
    str_length++;
    if (escape(a).length > 4) {
      // 中文字符的长度经编码之后大于4
      str_length++;
    }
    str_cut = str_cut.concat(a);

    if (str_length >= len) {
      str_cut = str_cut.concat('...');
      result = str_cut.toString();
      break;
    }
  }

  return result;
}

// 判断数据返回状态
function isSuccess(data) {
  if (data.status == 'success') {
    return true
  }
  return false;
}

// 判断list数据
function isItemList(list) {
  if (!!list && list.length > 0) {
    return true
  }
  return false;
}

// 图片过滤
function coverFilter(cover) {
  var img_src = cover;
  if (!verify_http.test(cover)) {
    img_src = "http://cloud.lankr.cn/api/image/" + cover
        + "?m/2/h/500/f/jpg";
  }
  return img_src;
}

// 5秒一次记录页面停留时间
function pageRemainInit(callUUidBack, duration) {
  var uuid = callUUidBack();
  if (!!uuid) {
    duration = duration * 1000;
    self.setInterval(function() {
      // 向服务器推送数据
      $.post('/api/webchat/record/page/remain', {
        'uuid' : uuid
      }, function(data) {
        // console.log(data);
      });
    }, duration);
  }
}

// 由秒得到天时分秒数据
var formatTime = function(longTime) {
  // 转化为 日+小时+分+秒
  var time = parseFloat(longTime);
  if (time != null && time != "") {
    if (time < 60) {
      var s = time;
      time = s + '秒';
    } else if (time > 60 && time < 3600) {
      var m = parseInt(time / 60);
      var s = parseInt(time % 60);
      time = m + "分钟" + s + "秒";
    } else if (time >= 3600 && time < 86400) {
      var h = parseInt(time / 3600);
      s
      var m = parseInt(time % 3600 / 60);
      var s = parseInt(time % 3600 % 60 % 60);
      time = h + "小时" + m + "分钟" + s + "秒";
    } else if (time >= 86400) {
      var d = parseInt(time / 86400);
      var h = parseInt(time % 86400 / 3600);
      var m = parseInt(time % 86400 % 3600 / 60)
      var s = parseInt(time % 86400 % 3600 % 60 % 60);
      time = d + '天' + h + "小时" + m + "分钟" + s + "秒";
    }
  }
  return time;
};

// 底部添加点击样式
function addActiveClass(id) {
  $('#' + id).addClass('active');
}

// 渲染资源列表
/**
 * 2016-06-24 ulCall 填充数据的dom ul的实体id list 资源数据 clear_data 是否要清空数据
 */
function drawResourceList($ul, list, clear_data) {
  if (!isItemList(list)) {
    return;
  }
  if (clear_data) {
    $ul.empty();
  }
  var ul_content = '';
  $.each(list, function(index, item) {
    var li = drawResourceItem(item,'');
    ul_content += li;
  });
  $ul.append(ul_content);
}

/**
 * 主要资源 video pdf 三分屏 新闻 绘制资源列表的每个item
 *
 * @param item
 */
function drawResourceItem(item,url) {
  var type = item.type;
  var uuid = item.uuid;
  var name = item.name;
  var cover = item.cover;
  var speakerName = item.speakerName;
  var hospitalName = item.hospitalName;
  var catgoryName = item.catgoryName;
  var viewCount = item.viewCount;
  if(url == ''){
    //资源
    url = '/api/webchat/resource/first/view/' + uuid;
  }else{
    //作品
    url = '/api/webchat/wx/works/detail/' + uuid;
  }
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
    return drawResNewsItem(item);
  } else if (type == 'VR') {
    fileClass += ' vr-type';
    filename = 'VR';
  }
  var item_li = '<a href="'+url+'" class="box">'
              + '<div class="img">'
              + '<img src="'+cover+'">'
              + '<div class="img-tag tr '+fileClass+'">'+filename+'</div>'
              + '<div class="img-tag br view-num">'
              + '<span class="icon icon-eye"></span>'+viewCount+'</div></div>'
              + '<div class="info">'
              + '<h5 class="tt">'+name+'</h5>'
              + '<div class="desc">'
              + '<p><span>'+catgoryName+'</span></p>'
              + '<p><span>'+speakerName+'</span>|<span>'+hospitalName+'</span></p></div></div>'
              + '<div class="icon icon-arr-r"></div></a>';
  return item_li;
}

/**
 * 构建资源新闻类型
 * @param item
 * @returns {String}
 */
function drawResNewsItem(item) {
  var catgoryName = item.catgoryName;
  var uuid = item.uuid;
  var name = item.name;
  var dateTime = item.dateTime;
  var shareCount = item.shareCount;
  var cover = item.cover;
  var desc = item.desc;
  var url = '/api/webchat/resource/first/view/' + uuid;
  var li = '';
  if (catgoryName == '分享有礼') {
    // 分享有礼
    name = cutStrForNum(name, 20);
    desc = cutStrForNum(desc, 20);
    li = '<a href="'+url+'" class="box with-share">'
       + '<div class="img"><img src="'+cover+'"></div>'
       + '<div class="info">'
       + '<h5 class="tt">'+name+'</h5>'
       + '<div class="desc">'+desc+'</div></div>'
       + '<div class="share-item">'
       + '<div class="icon icon-share"></div>'
       + '<div>分享 '+shareCount+'</div></div>'
       + '<div class="icon icon-arr-r"></div></a>';
  } else {
    // 一般新闻
    desc = cutStrForNum(desc, 25);
    li = '<a href="'+url+'" class="box">'
       + '<div class="img">'
       + '<img src="'+cover+'">'
       + '<div class="img-tag tr file-type">资讯</div></div>'
       + '<div class="info">'
       + '<h5 class="tt">'+name+'</h5>'
       + '<div class="desc">'
       + '<p>'+desc+'</p>'
       + '<p>'+dateTime+'</p></div></div>'
       + '<div class="icon icon-arr-r"></div></a>';
  }
  return li;
}

/**
 * 获取当前时间(按固定格式)
 *
 * @returns {String}
 */
function getNowFormatDate() {
  // 是当前时间比现实时间多1s,便于取到最新数据
  var date = new Date();
  var seperator1 = "-";
  var seperator2 = ":";
  var month = date.getMonth() + 1;
  month = repairZero(month);
  var strDate = date.getDate();
  strDate = repairZero(strDate);
  var hours = date.getHours();
  hours = repairZero(hours);
  var minutes = date.getMinutes();
  minutes = repairZero(minutes);
  var seconds = date.getSeconds() + 1;
  seconds = repairZero(seconds);
  var currentdate = date.getFullYear() + seperator1 + month + seperator1
      + strDate + " " + hours + seperator2 + minutes + seperator2
      + seconds;
  return currentdate;
}

/**
 * 补0
 *
 * @param num
 * @returns
 */
function repairZero(num) {
  if (num < 10) {
    return num = "0" + num;
  }
  return num;
}

/**
 * buildListCallBack 生成对应的list
 * buildUlCallBack 生成ul的callback； 每个调用都要实现的方法
 *
 * 在外部调用刷新的方法 dropload.opts.loadUpFn(dropload); 调用加载更多的方法
 * dropload.opts.loadDownFn(dropload);
 * lock=='up' 向上锁
 */
function dataDropload($div, $ul, request_url, buildListCallBack,
    buildUlCallBack,lock) {
  var time = getNowFormatDate();
  var dropload = $div.dropload({
        scrollArea : window,
        domUp : {
          domClass : 'dropload-up',
          domRefresh : '<div class="dropload-refresh">↓下拉刷新</div>',
          domUpdate : '<div class="dropload-update">↑释放更新</div>',
          domLoad : '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
        },
        domDown : {
          domClass : 'dropload-down',
          domRefresh : '<div class="dropload-refresh">↑上拉加载更多</div>',
          domLoad : '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
          domNoData : '<div class="dropload-noData">暂无更多数据</div>'
        },
        loadUpFn : function(me) {
          // 向上刷新
          var startTime = getNowFormatDate();
          var dateTimeSmpt = new Date().getTime();
          $.getJSON(request_url, {
            'startTime' : startTime,
            'size' : 10,
            'action' : 'refresh',
            'dateTimeSmpt' : dateTimeSmpt
          }, function(data) {
            if (isSuccess(data)) {
              // 回调1
              var list = buildListCallBack(data);
              $ul.empty();
              if(isItemList(list)){
                me.isFirst = false;
                // 组合li 回调2
                buildUlCallBack($ul, list);
              }
              // 每次数据加载完，必须重置
              me.resetload();
              // 解锁
              me.unlock();
              me.noData(false);
            } else {
              // 每次数据加载完，必须重置,无更多数据
              me.resetload();
            }
          });
        },
        loadDownFn : function(me) {
          // 往下滑动加载更多
          if (!me.listStartTime) {
            // 多数不用
            me.listStartTime = getNowFormatDate();
          }
          if (me.isFirst == undefined) {
            me.isFirst = true;
          }
          var dateTimeSmpt = new Date().getTime();
          $.getJSON(request_url, {
            'startTime' : me.listStartTime,
            'size' : 10,
            'action' : 'downMore',
            'isFirst' : me.isFirst,
            'dateTimeSmpt' : dateTimeSmpt
          }, function(data) {
            if (isSuccess(data)) {
              var list = buildListCallBack(data);
              if(isItemList(list)){
                me.isFirst = false;
                // 组合li 回调2
                buildUlCallBack($ul, list);
              } else {
                if (!me.isFirst) {
                  // 当不是首次加载到数据，隐藏无数据
                  me.$domDown.addClass('hide');
                }
                // 锁定
                me.lock();
                // 无数据
                me.noData();
              }
              // 每次数据加载完，必须重置
              me.resetload();
            } else {
              if (!me.isFirst) {
                // 当不是首次加载到数据，隐藏无数据
                me.$domDown.addClass('hide');
              }
              // 锁定
              me.lock();
              // 无数据
              me.noData();
              me.resetload();
            }
          });
        },
        threshold : 40
      });
  if(lock=='up'||lock=='down'){
    dropload.lock(lock);//锁住上方
  }
  return dropload;
}


/**
 * 绘制资源的下拉公共方法
 * @param $ul
 * @param list
 */
function commonDropUl($ul, list) {
  drawResourceList($ul, list, false);
}

/**
 * 活动列表的数据绘制
 * @param $ul
 * @param list
 */
function buildActivityListUl($ul,list){
  var li_item = '';
  $.each(list,function(index,item){
    var li_a = '/api/webchat/activity/total/page/' + item.uuid;
    var li_title = item.name;
    var li_kv = item.kv;
    var li_mark = item.mark;
    var li = '<a href="'+li_a+'" class="box">'
           + '<div class="img"><img src="'+li_kv+'"></div>'
           + '<div class="info-tag">'
           + '<h5 class="tt">'+li_title+'</h5>'
           + '<p>'+li_mark+'</p><span class="icon icon-arr-r"></span></div></a>';
    li_item += li;
  });
  $ul.append(li_item);
}
