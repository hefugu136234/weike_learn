<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="template language" name="keywords" />
<meta content="author" name="author" />
<meta
  content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"
  name="viewport" />
<meta content="yes" name="mobile-web-app-capable" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="telephone=no" name="format-detection" />
<meta content="email=no" name="format-detection" />
<meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
<title>申领VIP卡</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/users.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<body>
  <div id="page_vip_bg" class="page vip-bg">
    <div class="content">
      <!-- <header class="top-bar transparent">
        <div class="crumb-nav">
          <a href="/api/webchat/my/center" class="logo icon-logo"></a>
          申领VIP卡
        </div>
      </header> -->

      <div class="user-status-panel">
      	<!-- 加载公共的头部个人信息 -->
    		<jsp:include page="/wechat/common/top_userInfo.jsp"></jsp:include>
    		<!-- 加载公共的头部个人信息 -->

        <div class="vip-status">
          <div id="vip_status_icon" class="icon"></div>
          <div class="info">
            <p class="tt">VIP状态:</p>
            <p id="vip_status_p"></p>
          </div>
        </div>
      </div>

      <ul class="upload-process transparent clearfix">
        <li class="active">
          <div class="tt">申请VIP</div>
          <div class="tag">
            <div class="arr"></div>
          </div>
        </li>
        <li>
          <div class="tt">发货</div>
          <div class="tag">
            <div class="arr"></div>
          </div>
        </li>
        <li>
          <div class="tt">激活</div>
          <div class="tag">
            <div class="arr"></div>
          </div>
        </li>
      </ul>

      <div class="vip-status-area hide">
        <div class="icon"><img src="/assets/img/app/vip/status_icon_sq.png" width="22%" /></div>
        <div class="info">
          <p>您的VIP已申请</p>
          <p>我们会在最短时间内处理您的信息</p>
          <p>并且会通过您的手机告知申请结果</p>
          <p>请耐心等待</p>
        </div>
      </div>

      <div class="vip-status-area hide">
        <div class="icon"><img src="/assets/img/app/vip/status_icon_sh.png" width="22%" /></div>
        <div class="info">
          <p>您的云卡已经发货</p>
          <p>预计一周内送达</p>
        </div>
        <div class="mt32 txtC">
          <button type="button" class="button btn-radius btn-blur" id="order_apply">我要催单</button>
        </div>
      </div>

      <div class="vip-status-area hide">
        <div class="vip-code-input">
          <input type="text" id="card_code" name="card_code" placeholder="请输入知了云卡背面密码" class="input">
        </div>
        <input type="hidden" id="token" value="${requestScope.token}"/>
        <button type="button" class="button btn-full btn-radius btn-yellow" id="active_button">激活VIP</button>
      </div>

      <div class="vip-status-area hide">
        <div class="icon"><img src="/assets/img/app/vip/status_icon_done.png" width="22%" /></div>
        <div class="info">
          <p>恭喜您成功激活VIP</p>
        </div>
        <div class="deadline">
          <p>您的VIP期限至</p>
          <p id="deadline_p">${requestScope.deadTime}</p>
        </div>
      </div>

    </div>
  </div>

  <div id="page_vip_home" class="page vip-home">
    <div class="content">
      <!-- <header class="top-bar transparent">
        <div class="crumb-nav">
          <a href="/api/webchat/my/center" class="logo icon-logo"></a>
          申领VIP卡
        </div>
      </header> -->

      <div class="con">
        <div class="tips">
          <h5 class="tt"><img src="/assets/img/app/vip/apply_tt.png"></h5>
          <div class="info">
            <p>1.“知了云盒”医学智能电视盒一台</p>
            <p>2.“知了云盒教学资源”年卡一张</p>
            <p>3. 年费仅需¥<strong>499</strong></p>
          </div>
        </div>
        <div class="apply-vip-btns">
          <div class="btn" id="applic_vip_a">
            <div class="icon"><img src="/assets/img/app/vip/apply_icon1.png"></div>
            <div class="tt">
              <p>暂无云卡</p>
              <p>立即申领</p>
            </div>
          </div>

          <div class="btn" id="applic_vip_b">
            <div class="icon"><img src="/assets/img/app/vip/apply_icon2.png"></div>
            <div class="tt">
              <p>已有云卡</p>
              <p>立即兑换</p>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>

  <div class="feed-back-modal" id="vip-intro-alert">
    <div class="cover"></div>
    <div class="alert">
      <h5 class="tt">VIP说明</h5>
      <div class="con">您必须通过实名认证，才能申领VIP</div>
      <div class="btns">
        <div class="btn cancel">取消</div>
        <a href="/api/webchat/activity/real/name/page" class="btn sub">实名认证</a>
      </div>
    </div>
  </div>

  <textarea class="hide" id="vip_page_show">${requestScope.vip_page_show}</textarea>
  <script type="text/javascript">
    $(document).ready(function() {
      // 0=未实名  1=未申请 2=已申请
      var vip_page_data=JSON.parse($('#vip_page_show').text());
      var vip_page_show=vip_page_data.showPage;
      if(vip_page_show==0){
        $('#page_vip_bg').hide();
        $('#page_vip_home').show();
        $('#vip-intro-alert').fadeIn().addClass('active');
      }else if(vip_page_show==1){
        $('#page_vip_bg').hide();
        $('#page_vip_home').show();
        $('#vip-intro-alert').fadeOut().removeClass('active');
      }else if(vip_page_show==2){
        //显示其他的信息
        // 显示进度条 0=申请vip 1=已审核 2=已发货 3=已激活
        var showProcess=vip_page_data.showProcess;
        dealProcessUl(showProcess);
        $('#page_vip_bg').show();
        $('#page_vip_home').hide();
        $('#vip-intro-alert').fadeOut().removeClass('active');
      }

      $('#applic_vip_a').click(function(){
        //申请vip的操作
        $.post('/api/webchat/application/vip',{
        	type:0
        },function(data){
          if(data.status=='success'){
          // 显示进度条 0=申请vip 1=已审核 2=已发货 3=已激活
          var showProcess=data.showProcess;
          var deadTime=data.deadTime;
          if(!!deadTime){
            $('#deadline_p').html(data.message);
          }
          dealProcessUl(showProcess);
          $('#page_vip_bg').show();
          $('#page_vip_home').hide();
          $('#vip-intro-alert').fadeOut().removeClass('active');
          }else{
            var showPage=data.showPage;
            if(showPage==0){
              $('#vip-intro-alert').fadeIn().addClass('active');
            }else if(showPage==1){
              alert('申请失败');
            }
          }
        });
      });

      $('#applic_vip_b').click(function(){
          //申请vip的操作
          $.post('/api/webchat/application/vip',{
          	type:2
          },function(data){
            if(data.status=='success'){
            // 显示进度条 0=申请vip 1=已审核 2=已发货 3=已激活
            var showProcess=data.showProcess;
            var deadTime=data.deadTime;
            if(!!deadTime){
              $('#deadline_p').html(data.message);
            }
            dealProcessUl(showProcess);
            $('#page_vip_bg').show();
            $('#page_vip_home').hide();
            $('#vip-intro-alert').fadeOut().removeClass('active');
            }else{
              var showPage=data.showPage;
              if(showPage==0){
                $('#vip-intro-alert').fadeIn().addClass('active');
              }else if(showPage==1){
                alert('失败');
              }
            }
          });
        });

      $('#order_apply').click(function(){
        alert('催单信息发送成功，请耐心等候');
      });

      /**
        **自动请求图像数据
        **/
       /*  var nowDate=new Date().getTime();
        $.getJSON('/api/webchat/wx/get/photo?timestamp=' + nowDate,
          function(data) {
          if(data.status=='success'){
            var message=data.message;
            if(!!message){
              $('#photo_img').attr('src',message);
            }
          }
        }); */


      $('#active_button').click(function(event) {
        event.preventDefault();
        var card_code = $('#card_code').val();

        if (card_code == '') {
          alert('请输入VIP号码！');
          return false;
        }
        $(this).attr('disabled', 'disabled');
        $.post('/api/webchat/active/code', {
          'activeCode' : card_code,
          'token':$('#token').val()
        }, function(data) {
          $('#active_button').removeAttr("disabled");
          if (data.status == 'success') {
            $('#deadline_p').html(data.message);
            dealProcessUl(3);
          } else {
            if (!!data.message) {
              alert(data.message);
            } else {
              alert("激活失败");
            }
          }
        });

      });
    });

    // 处理状态进度条
    function dealProcessUl(tagerIndex) {
      // 显示进度条 0=申请vip 1=已审核 2=已发货 3=已激活
      var vip_status_p=$('#vip_status_p');
      var vip_status_icon=$('#vip_status_icon');
      if(tagerIndex==0){
        vip_status_icon.addClass('icon-order-done');
        vip_status_p.html('已申请');
      }else if(tagerIndex==1){
        vip_status_icon.addClass('icon-wait');
        vip_status_p.html('已审核');
      }else if(tagerIndex==2){
        vip_status_icon.addClass('icon-truck-o');
        vip_status_p.html('已发货');
      }else if(tagerIndex==3){
        vip_status_icon.addClass('icon-round-check');
        vip_status_p.html('已激活');
      }
      $('div.vip-status-area').each(function(index,item){
        if(tagerIndex==index){
          $(item).show();
        }else{
          $(item).hide();
        }
      });
      $('ul.upload-process').find('li').each(function(index, item) {
        if (tagerIndex == index) {
          if(!$(item).hasClass("active")){
          $(item).addClass("active");
          }
          return false;
        }else{
          if($(item).hasClass("active")){
            $(item).removeClass("active");
            }
        }
        $(item).addClass("done");
      });

    }
  </script>
  <!-- 加入分享 -->
<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
