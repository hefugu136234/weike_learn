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
<meta content="black-translucent"
	name="apple-mobile-web-app-status-bar-style" />
<title>${res_vo.name}</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript"
	src="//cdn.bootcss.com/jquery.lazyload/1.9.1/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/resource/pdf_detail.js"></script>
</head>
<!-- 新页面 原 pdf_view.jsp-->
<body>
	<div class="page">
		<div id="page">
			<c:if test="${not empty res_vo.pdf.pdfTaskId}">
				<c:forEach var="index" begin="1" end="${res_vo.pdf.pdfnum}">
					<img src="/assets/img/grey.gif" width="100%"
						data-original="http://7xo6el.com2.z0.glb.qiniucdn.com/${res_vo.pdf.pdfTaskId}?odconv/jpg/page/${index}/density/150/quality/80/resize/800" />
				</c:forEach>
			</c:if>
		</div>
	</div>
	<input type="hidden" id="page_reamin_uuid" value="${page_reamin_uuid}" />
	<input type="hidden" id="resource_uuid" value="${res_vo.uuid}" />
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
