<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="template language" name="keywords" />
    <meta content="author" name="author" />
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" name="viewport" />
    <meta content="yes" name="mobile-web-app-capable" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/disposable.css" rel="stylesheet">
    <title>知了闹元宵</title>
  </head>

  <body>
    <div class="page envelopes">
      <div class="content result">
        <div class="logo"><img src="/assets/img/app/envelopes/logo.png" width="100%"></div>
        <div class="title"><img src="/assets/img/app/envelopes/title.png" width="74%"></div>
        <div class="result_tag">恭喜您，获得<span class="num">10</span>元话费</div>
        <div class="float_intro">
          <h5 class="tt">权威麻醉专家，坐镇知了微课！</h5>
          <div class="info">
            <p>>课题：靶控输注丙泊酚静脉麻醉的快速指南</p>
            <p>>课题：Marsh VS Schnider</p>
            <p>>更多精彩内容即将上线，敬请期待。</p>
          </div>
        </div>
        <div class="btn_group clearfix">
          <div class="btn" id="btn_rule"><img src="/assets/img/app/envelopes/btn_rule.png" width="100%"></div>
          <div class="btn" id="btn_record"><img src="/assets/img/app/envelopes/btn_record.png" width="100%"></div>
        </div>

        <div class="float_info" id="info_rule">
          <h5 class="tt">活动规则</h5>
          <div class="info">
            <p>活动时间：</p>
            <p>元宵节期间（2月22日-2月26日）</p>
            <p>参与方式：</p>
            <p>每日进入知了云盒，参与抽奖，即有机会获得10-50元话费</p>
            <p>每日3次抽奖机会</p>
          </div>
          <div class="close">╳</div>
        </div>
        <div class="float_info" id="info_record">
          <h5 class="tt">抽奖纪录</h5>
          <ul class="record_list">
            <li><span class="date">2月22日</span>话费10元</li>
            <li><span class="date">2月23日</span>话费5元</li>
          </ul>
          <div class="close">╳</div>
        </div>
      </div>
    </div>

    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript">
      $(document).ready(function(){
        $('#btn_rule').click(function(){
          $('.float_info').fadeOut();
          $('#info_rule').fadeIn();
        });

        $('#btn_record').click(function(){
          $('.float_info').fadeOut();
          $('#info_record').fadeIn();
        });

        $('.float_info').find('.close').click(function(){
          $(this).parents('.float_info').fadeOut();
        });
      });
    </script>
  </body>
</html>
