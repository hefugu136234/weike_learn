<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
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
<meta content="black-translucent"
  name="apple-mobile-web-app-status-bar-style" />
<title>问卷考试</title>
<link rel="stylesheet" href="/assets/css/app/font.css">
<link rel="stylesheet" href="/assets/css/app/default.css">
<link rel="stylesheet" href="/assets/css/app/package.css">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<body>
  <div class="page">
    <div class="content package-cover-bg">
      <div class="package-cover-title">${vo_data.name}</div>
      <div class="package-cover-intro">${vo_data.desc}</div>
      <div class="package-cover-status">
        <div class="box">
          <span class="icon icon-clock"></span>
          考试时间：
          <span class="num">${vo_data.examineTime}分钟</span>
        </div>
        <div class="box">
          <span class="icon icon-rank"></span>
          考题数量：
          <span class="num">${vo_data.examineNum}题</span>
        </div>
        <div class="box">
          <span class="icon icon-pass"></span>
          合格分数：
          <span class="num">≥${vo_data.qualifiedScore}分</span>
        </div>
      </div>

      <div class="package-cover-btns p10">
        <input type="hidden" id="examine_uuid" value="${vo_data.uuid}"/>
        <input type="hidden" id="course_uuid" value="${vo_data.courseUuid}"/>
        <button id="examine_start" class="button btn-full btn-radius btn-orange">开始考试</button>
      </div>
    </div>
  </div>
  <script type="text/javascript">
  $(function(){
	 var examine_uuid=$('#examine_uuid').val();
	 var courseUuid=$('#course_uuid').val();
	 var course_url = '/api/webchat/course/package/detail/' + courseUuid;
	 $('#examine_start').click(function(e){
			$.getJSON('/api/webchat/normalCollect/questionnaire', {
			uuid : examine_uuid,
			redirect_uri : course_url
		}, function(data) {
			if (isSuccess(data)) {
				location.href = data.message;
			} else {
				AlertClassTip(data.message);
			}
		});
	 });
  });
  </script>

  <!-- 取消分享 -->
  <%@ include file="/wechat/common/base_noshare.jsp"%>
</body>
</html>
