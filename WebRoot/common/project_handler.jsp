<%@page import="org.springframework.web.util.HtmlUtils"%>
<%@page import="com.lankr.tv_cloud.model.Project"%>
<%@page import="com.lankr.tv_cloud.model.UserReference"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>后台管理</title>
<link href="/assets/css/bootstrap.min.css" rel="stylesheet">
<link href="/assets/font-awesome/css/font-awesome.css" rel="stylesheet">
<link href="/assets/css/docs.min.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<%
	List<UserReference> refs = (List<UserReference>) request.getAttribute("references");
%>
</head>
<span style="margin-left: 50px;">选择操作的项目：</span>
<p>
<div class="bs-glyphicons" style="margin-left: 50px; margin-right: 50px">
	<ul class="bs-glyphicons-list">
		<%
			if (refs != null && !refs.isEmpty()) {
				for (int i = 0; i < refs.size(); i++) {
					UserReference ref = refs.get(i);
					Project project = ref.getProject();
		%>
		<li style="cursor: pointer;" data-id="<%=project.getUuid()%>"><span
			class="glyphicon glyphicon-asterisk" aria-hidden="true"></span> <span
			class="glyphicon-class"><%=HtmlUtils.htmlEscape(project.getProjectName())%></span></li>
		<%
			}
			}
		%>
	</ul>
</div>
<body>
<script src="/assets/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$.each($('ul li'),function(index,element){
			$(element).click(function(){
				$.post(
						'/user/hold/project',
						{
							uuid : $(element).data('id')
						},
						function() {
						})
				.always(
						function(
								data) {
							console
									.log(data)
							if ('success' == data.status) {
								window.location.href = '/tv/home/settings?timestamp='
										+ new Date()
												.getTime()
							} else if(data.status){
								alert(data.status)
							}
						})
			})
	});
})
</script>
</body>
</html>

