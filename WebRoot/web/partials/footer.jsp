<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<footer class="footer">
  <div class="container">
    <div class="copyright">© 2016 <b>Lankr.</b> All Rights Reserved. <a href="http://www.miitbeian.gov.cn">沪ICP备14039441号-10</a> 联系电话：400 806 2533</div>
  </div>
</footer>

<div class="side-operation-group">
  <div class="item">
    <a href="#code_modal" data-toggle="modal" data-target="#code_modal">
      <div class="icon icon-code"></div>
      <h5 class="tt">扫一扫</h5>
    </a>
  </div>
  <div class="item">
    <a href="javascript:;" id="back_top">
      <div class="icon icon-top"></div>
      <h5 class="tt">回到顶部</h5>
    </a>
  </div>
</div>

<!-- 点击扫一扫弹出 -->
<div class="modal fade no-radius code-modal" id="code_modal">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">扫一扫，关注我们</h4>
      </div>
      <div class="modal-body">
        <div class="code-img"><img src="/assets/img/qr.jpg" /></div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="baidu_tj.jsp"></jsp:include>
