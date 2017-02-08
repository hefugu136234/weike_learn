<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 新页面 原 无-->
<!-- 搜索 -->
<link href="/assets/css/app/search.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/wechat/search/search.js"></script>
<div class="page search-page">
  <div class="search-bar">
    <div class="cancel-btn">取消</div>
    <form id="common_serach">
      <div class="input-box">
        <input type="search" class="input" id="search_input" maxlength="50" placeholder="多个关键字空格隔开查询"/>
      </div>
      <button type="submit" class="sub-btn icon-search" id="search_button"></button>
    </form>
  </div>

  <div class="content">
    <div id="search_total_div">
      <div id="search_has_result" class="hide">
        <div class="search-status-tag">共有<span class="num"></span>条记录</div>
        <div id="search_has_result_ul" class="list-with-img with-arr p10"></div>
      </div>

      <div id="search_no_reslut" class="search-nothing hide">
        <div class="icon">
          <img src="/assets/img/app/search_noting.png" width="11%">
        </div>
        <div class="info">
          <p>对不起</p>
          <p>没有搜索到相对应的信息</p>
        </div>
      </div>

    </div>

    <div class="search-status-tag">热门关键字</div>
    <div class="search-hot clearfix">
      <a href="javascript:void(0);" data-search="MDT" onclick="aClickSearch(this);">1. MDT</a>
      <a href="javascript:void(0);" data-search="腹腔镜" onclick="aClickSearch(this);">2. 腹腔镜</a>
      <a href="javascript:void(0);" data-search="肿瘤" onclick="aClickSearch(this);">3. 肿瘤</a>
      <a href="javascript:void(0);" data-search="胃癌" onclick="aClickSearch(this);">4. 胃癌</a>
    </div>

    <div class="search-others">
      <h5 class="tt">别的同道也在搜：</h5>
      <div class="list clearfix">
        <a href="javascript:void(0);" data-search="指南" onclick="aClickSearch(this);" class="items">指南</a>
        <a href="javascript:void(0);" data-search="胰腺炎" onclick="aClickSearch(this);" class="items">胰腺炎</a>
        <a href="javascript:void(0);" data-search="直播" onclick="aClickSearch(this);" class="items">直播</a>
      </div>
    </div>

  </div>
</div>
<!-- 搜索 -->
