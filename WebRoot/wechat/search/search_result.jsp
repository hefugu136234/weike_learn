<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    <title>搜索结果</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/search.css" rel="stylesheet">
  </head>

  <body>
    <div class="page searchResultPage">
      <div class="searchbar">
        <a href="/api/webchat/index" class="cancelBtn">取消</a>
        <form>
          <div class="inputBox">
            <input type="search" class="input">
          </div>
          <button type="submit" class="subBtn icon-search"></button>
        </form>
      </div>
      <div class="content">
        <div id="search_total_div">
          <div id="search_has_result">
            <div class="searchStatus">共有<span class="num">20</span>条记录</div>
            <ul class="list newsList searchResultList">
              <li class="item clearfix item-icon-right">
                <a href="/api/webchat/video/local/134539bd-afe9-4ec4-96e1-c74068f23e26">
                  <div class="img">
                    <img src="http://assets.lankr.cn/tv_cloud/2015/09/25/120/39596026/5f631657df32ef4adaf0435097c591fa.jpg">
                    <div class="fileStyle video">视频</div>
                  </div>
                  <div class="info">
                    <h2 class="tt">外科视频1</h2>
                    <div class="desc">
                      <p><span>编号：0008</span><span>外科</span></p>
                      <p>讲者1 | 芜湖市第二人民医院</p>
                    </div>
                  </div>
                  <i class="icon icon-arr-r"></i>
                </a>
              </li>
              <li class="item clearfix item-icon-right">
                <a href="/api/webchat/video/local/134539bd-afe9-4ec4-96e1-c74068f23e26">
                  <div class="img">
                    <img src="http://assets.lankr.cn/tv_cloud/2015/09/25/120/39596026/5f631657df32ef4adaf0435097c591fa.jpg">
                    <div class="fileStyle video">视频</div>
                  </div>
                  <div class="info">
                    <h2 class="tt">外科视频1</h2>
                    <div class="desc">
                      <p><span>编号：0008</span><span>外科</span></p>
                      <p>讲者1 | 芜湖市第二人民医院</p>
                    </div>
                  </div>
                  <i class="icon icon-arr-r"></i>
                </a>
              </li>
            </ul>
          </div>

          <div id="search_no_reslut" class="searchNothing hide">
            <div class="icon"><img src="/assets/img/app/search_noting.png" width="11%"></div>
            <div class="info">
              <p>对不起</p>
              <p>没有搜索到相对应的信息</p>
            </div>
          </div>

        </div>

        <div class="searchStatus">热门关键字</div>
        <div class="searchHot clearfix">
          <a href="#">1. 内镜</a>
          <a href="#">2. 胃肠肿瘤</a>
          <a href="#">3. 陈潇院士</a>
          <a href="#">4. 知了播客秀</a>
          <a href="#">5. 阑尾手术</a>
          <a href="#">6. 肝</a>
        </div>

        <div class="searchOthers">
          <h5 class="tt">别的同道也在搜：</h5>
          <div class="list clearfix">
            <a href="#" class="items">神经重症</a>
            <a href="#" class="items">十二指肠</a>
            <a href="#" class="items">ICU镇静</a>
            <a href="#" class="items">中山医院肿瘤科 陈潇院士</a>
            <a href="#" class="items">胃</a>
          </div>
        </div>

      </div>
    </div>

    <script type="text/javascript" src="/assets/js/jquery.js"></script>
  </body>
</html>
