<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="page">
	<div id="page" class="content">
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
<script type="text/javascript">
$(function(){
	$('img').lazyload({
		placeholder : "/assets/img/grey.gif",
		effect : "fadeIn",
		container : $("#page")
	});

	var imgsObj = $('img');
	var imgs = new Array();
	for (var i = 0; i < imgsObj.size(); i++) {
		imgs.push(imgsObj.eq(i).attr('data-original'));
	}

	$('img').click(function(event) {
		event.preventDefault();
		WeixinJSBridge.invoke('imagePreview', {
			'current' : $(this).attr('src'),
			'urls' : imgs
		});
	});
});
</script>
