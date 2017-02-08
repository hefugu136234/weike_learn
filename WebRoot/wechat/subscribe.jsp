<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta content="template language" name="keywords" />
    <meta content="author" name="author" />
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" name="viewport" />
    <meta content="yes" name="mobile-web-app-capable" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
    <title>知了云盒公测预约</title>
    <link rel="stylesheet" media="all" href="/assets/css/jquery.fullPage.css" />
    <link rel="stylesheet" media="all" href="/assets/css/app/default.css" />
    <link rel="stylesheet" media="all" href="/assets/css/app/subscribe.css" />
    <jsp:include page="wx_wrapped.jsp"></jsp:include>
  </head>

  <body>

    <div id="fullpage_subscribe">
      <div class="section subscribe_index">
        <div class="tag1">
          <img alt="" width="76%" src="/assets/img/app/subscribe/banner_info1.png" />
        </div>
        <div class="tag2">
          <img alt="" width="30%" src="/assets/img/app/subscribe/banner_info2.png" />
        </div>
        <div id="tvBox" class="subscribe-tv-box">
          <div id="id_video_container"></div>
        </div>
        <!-- <a class="subscribeBtn" href="javascript:;">公测申请</a> -->
        <div class="arr_next">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_next_w.png" />
        </div>
      </div>
      <div class="section subscribe">
        <div class="arr_back">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_back.png" />
        </div>
        <div class="title">
          <img alt="" width="47%" src="/assets/img/app/subscribe/tt1.png" />
        </div>
        <div class="btnGroups">
          <a class="btn share" href="javascript:;">
            <span>分享</span>
          </a>
        </div>
        <div class="arr_next">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_next.png" />
        </div>
      </div>
      <div class="section subscribe_product">
        <div class="arr_back">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_back.png" />
        </div>
        <div class="title">
          <img alt="" width="62%" src="/assets/img/app/subscribe/tt2.png" />
        </div>
        <div class="img">
          <img alt="" width="79%" src="/assets/img/app/subscribe/img2.jpg" />
        </div>
        <div class="btnGroups">
          <a class="btn share" href="javascript:;">
            <span>分享</span>
          </a>
        </div>
        <div class="arr_next">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_next.png" />
        </div>
      </div>
      <div class="section subscribe_product2">
        <div class="arr_back">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_back.png" />
        </div>
        <div class="title">
          <img alt="" width="58%" src="/assets/img/app/subscribe/tt3.png" />
        </div>
        <div class="img">
          <img alt="" width="75%" src="/assets/img/app/subscribe/img3.png" />
        </div>
        <div class="btnGroups">
          <a class="btn share" href="javascript:;">
            <span>分享</span>
          </a>
        </div>
        <div class="arr_next">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_next.png" />
        </div>
      </div>
      <div class="section subscribe_product3">
        <div class="arr_back">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_back.png" />
        </div>
        <div class="title">
          <img alt="" width="37%" src="/assets/img/app/subscribe/tt4.png" />
        </div>
        <div class="img">
          <img alt="" width="100%" src="/assets/img/app/subscribe/img4.jpg" />
        </div>
        <div class="btnGroups">
          <a class="btn share" href="javascript:;">
            <span>分享</span>
          </a>
        </div>
        <div class="arr_next">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_next.png" />
        </div>
      </div>
      <div class="section subscribe_about">
        <div class="arr_back">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_back.png" />
        </div>
        <div class="title">
          <img alt="" width="35%" src="/assets/img/app/subscribe/tt5.png" />
        </div>
        <div class="img">
          <img alt="" width="20%" src="/assets/img/app/subscribe/img5.png" />
        </div>
        <div class="info">
          <p>“知了云盒”是由上海翼得营销策划有限公司以及上海郞客信息技术有限公司共同开发，“知了云盒”以网络平台为主，搭建中国医学领域多媒体教育学习互动平台，“知了云盒”涵盖电视盒子，手机，ipad等多媒体数字终端，构建网络一体化的学术交流、沟通平台。</p>
          <p>“知了云盒”内容丰富，涵盖麻醉、内科、外科等各大领域。旨在普及学术知识，促进学术沟通，通过学术知识推送、核心知识的讲授、临床应用技能的培训，全面提升医生临床思维能力和诊断技能。</p>
        </div>
        <div class="arr_next">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_next.png" />
        </div>
      </div>
      <div class="section subscribe_contact">
        <div class="arr_back">
          <img alt="" width="5%" src="/assets/img/app/subscribe/arr_back.png" />
        </div>
        <div class="title">
          <img alt="" width="35%" src="/assets/img/app/subscribe/tt6.png" />
        </div>
        <div class="img">
          <img alt="" width="20%" src="/assets/img/app/subscribe/img5.png" />
        </div>
        <div class="info">
          <p><strong>电子信箱：</strong>bd@z-box.com.cn</p>
          <p><strong>服务热线：</strong>400 806 2533</p>
        </div>
      </div>
    </div>

    <div id="share_from_wechat" class="share-wechat-tips">
      <p>请从微信中进行分享！</p>
    </div>

    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="/assets/js/jquery.fullPage.min.js"></script>
    <script type="text/javascript" src="http://qzonestyle.gtimg.cn/open/qcloud/video/h5/h5connect.js"></script>
    <script type="text/javascript" src="/assets/js/common.js"></script>
    <script>
      $(document).ready(function() {
        var option = {
          "auto_play": "0",
          "file_id": "16092504232103694599",
          "app_id": "1251442335",
          "width": 312,
          "height": 182
        };
        new qcVideo.Player("id_video_container", option);

        $('#fullpage_subscribe').fullpage({
          navigation: true,
          navigationPosition: 'left',
          verticalCentered: false
        });

        setTimeout(function(){
          $('.subscribe_index').find('.tag1').fadeIn();
        }, 1000);

        setTimeout(function(){
          $('.subscribe_index').find('.tag2').fadeIn();
        }, 3000);

        setTimeout(function(){
          $('.subscribe_index').find('.tag2').fadeOut();
          $('#tvBox').fadeIn();
        }, 5000);

        $('.subscribeBtn, .btn.subscribe').click(function(){
          var starttime = new Date('2015/09/22 11:30');
          var endtime = new Date('2015/09/25 11:30');
          var nowtime = new Date();
          var leftsecond = parseInt((starttime.getTime()-nowtime.getTime())/1000);
          var endsecond = parseInt((endtime.getTime()-nowtime.getTime())/1000);
          if (leftsecond <= 0){
            $(this).attr('href', '/f/wx/subscribe/select');
          }
          else if (endsecond <= 0) {
        	  alert('第一批公测申请已结束,请继续关注"知了云盒"微信平台，"知了云盒"即将正式发布,Coming Soon …');
          }
          else {
        	  alert('公测申请将于9月23日12点正式开始，请您耐心等待。');
          }

        });

        $('.btnGroups').find('.btn.share').click(function(){
          $('#share_from_wechat').fadeIn();
        });

        $('#share_from_wechat').click(function(){
          $(this).fadeOut();
        });

      });

    </script>
  </body>
</html>
