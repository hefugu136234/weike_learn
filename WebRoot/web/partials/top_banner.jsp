<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty banner_list.bannerList}">
<div class="index-banner">
  <div class="carousel" id="top_banner">
    <c:forEach items="${banner_list.bannerList}" var="item">
      <div class="carousel-cell">
        <a href="${item.url}">
          <img src="${not empty item.cover ? item.cover : '/assets/img/web/placeholder_2x1.jpg'}" />
          <div class="cover"></div>
        </a>
      </div>
    </c:forEach>
  </div>
</div>
<script src="/assets/js/web/top_banner.js"></script>
</c:if>
