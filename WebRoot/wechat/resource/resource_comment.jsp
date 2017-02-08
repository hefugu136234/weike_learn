<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript" src="/assets/js/qqFace/jquery.qqFace.js"></script>
<script type="text/javascript" src="/assets/js/wechat/resource/resource_comment.js"></script>

<div class="resource-comments mt10">
  <div class="icon-title mb-1 white bold clearfix">
    <h5 class="tt bfL">
      <span class="icon icon-message"></span>评 论
    </h5>
    <a href="javascript:;" class="button btn-cyan pull-right" id="sub_comments" onclick="addComment()">发 送</a>
  </div>

  <textarea name="comments" class="comments-content-text" id="resource_comments_text" rows="3" placeholder="请输入评论内容"></textarea>
  <!-- 	<p>
		<span class="resource_emotion">表情</span>
	</p>  -->

  <div class="comments-content" id="comments_content">
    <ul class="list" id="comments_list">
      <!-- <li class="clearfix box">
        <div class="photo">
          <img src="" alt="">
        </div>
        <div class="detail">
          <h5 class="name">李木子</h5>
          <div class="con">评论内容</div>
          <div class="time">2016-01-01 12:12:12</div>
        </div>
        <div class="operation">
          <div class="btn reply-btn"><span class="icon icon-message"></span>回复</div>
          <div class="btn del-btn">删除</div>
        </div>
      </li>
      <li class="clearfix box">
        <div class="photo">
          <img src="" alt="">
        </div>
        <div class="detail">
          <h5 class="name">李木子</h5>
          <div class="con">评论内容</div>
          <div class="time">2016-01-01 12:12:12</div>
        </div>
        <div class="operation">
          <div class="btn reply-btn"><span class="icon icon-message"></span>回复</div>
          <div class="btn del-btn">删除</div>
        </div>
      </li> -->
    </ul>
  </div>
</div>

<div class="feed-back-modal" id="comments_reply_modal">
  <div class="cover"></div>
  <div class="alert">
    <div class="tt">回复评论</div>
    <div class="con p10">
      <textarea name="comments" class="reply-content-text" id="reply_comments_text" rows="3" placeholder="请输入回复内容" maxlength="230"></textarea>
     <!--  <p>
		<span class="comment_emotion">表情</span>
	</p> -->
    </div>

    <div class="btns">
      <div class="btn cancel" onclick="cancelModel()">取消</div>
      <div class="btn sub sub-reply" onclick="replyComment()">提交回复</div>
    </div>
  </div>
</div>

