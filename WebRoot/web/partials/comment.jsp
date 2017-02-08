<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 搜索 -->
<link href="/assets/css/app/search.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/qqFace/jquery.qqFace.js"></script>
<script type="text/javascript" src="/assets/js/web/comment.js"></script>
<div class="title-tag mt16 clearfix">
	<h5 class="tt pull-left">
		<span class="icon icon-message"></span>评 论 <span class="num" id="comment_num"></span>
	</h5>
</div>

<div class="panel no-radius">
	<div class="panel-body">
		<form class="form-horizontal comments-form mt32" id="comments_form">
			<div class="form-group">
				<div class="col-sm-offset-1 col-sm-10">
					<textarea class="form-control" rows="4" id="comment_text_area"></textarea>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-1 col-sm-10">
					<div id="resource_emotion" class="resource_emotion emoji-btn pull-left">
						<span class="icon icon-emoji"></span>插入表情
					</div>
					<button type="button" class="btn btn-blue pull-right" onclick="addComment()">提交评论</button>
				</div>
			</div>
		</form>

		<div class="comments-list" id="comments_list">
			<!-- 一条评论 -->
			<!-- <div class="media comments-item">
				<div class="media-left photo">
					<img class="media-object img-circle"
						src="/assets/img/web/default_photo.jpg" alt="用户头像">
				</div>
				<div class="media-body">
					<h4 class="media-heading clearfix">
						<div class="name pull-left">
							<span class="name-html">某某某</span> 评论了：
						</div>
						<div class="date pull-right">
							<span class="icon icon-clock"></span>2016-01-01 12:12:12
						</div>
					</h4>
					<div class="comment-detail">评论内容</div>
					<div class="comment-operation text-right">
						<span class="item reply-btn"><i class="icon icon-message"></i>回复</span>
						<span class="item dialog-btn"><i class="icon icon-detail"></i>查看对话</span>
						<span class="item delete-btn"><i class="icon icon-trash"></i>删除评论</span>
					</div>
					<div class="form-horizontal reply-comments">
						<div class="form-group">
							<div class="col-sm-12">
								<textarea class="form-control reply-comments-text" rows="2"></textarea>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-12 text-right">
								<button type="button" class="btn btn-sm btn-default cancel-btn">取消</button>
								<button type="button" class="btn btn-sm btn-success">提交回复</button>
							</div>
						</div>
					</div>
				</div>
			</div> -->
			<!-- 一条评论 -->
		</div>
		<div class="text-center">
			<ul id="pagination_ul" class="pagination"></ul>
		</div>

	</div>
</div>

<!-- 查看对话 -->
<div class="modal fade no-radius" id="dialog_comments" tabindex="-1"
	role="dialog" aria-labelledby="dialog_commentsLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="dialog_commentsLabel">查看对话</h4>
			</div>
			<div class="modal-body">
				<ul class="dialog-comments-list" id="dialog_comments_list">
					<!-- <li class="item clearfix">
						<div class="time">2分钟前：</div>
						<div class="photo">
							<img class="img-circle" src="/assets/img/web/default_photo.jpg"
								alt="用户头像">
						</div>
						<div class="detail">
							<div>
								<span class="name">李木子</span>：你瞅啥？
							</div>
							<div class="operation text-right">
								<span class="item reply-dialog-btn"><i
									class="icon icon-message"></i>回复</span>
							</div>
							<div class="form-horizontal reply-dialog">
								<div class="form-group">
									<div class="col-sm-12">
										<textarea class="form-control reply-dialog-text" rows="2"></textarea>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-12 text-right">
										<button type="button"
											class="btn btn-xs btn-default cancel-btn">取消</button>
										<button type="button" class="btn btn-xs btn-success">提交回复</button>
									</div>
								</div>
							</div>
						</div>
					</li> -->
					<!-- <li class="item"><span class="time">1分钟前：</span>
						<div class="photo">
							<img class="img-circle" src="/assets/img/web/default_photo.jpg"
								alt="用户头像">
						</div>
						<div class="detail">
							<div>
								<span class="name">某某某</span>回复<span class="name">李木子</span>：瞅你咋地！
							</div>
							<div class="operation text-right">
								<span class="item reply-dialog-btn"><i
									class="icon icon-message"></i>回复</span>
							</div>
							<div class="form-horizontal reply-dialog">
								<div class="form-group">
									<div class="col-sm-12">
										<textarea class="form-control reply-dialog-text" rows="2"></textarea>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-12 text-right">
										<button type="button"
											class="btn btn-xs btn-default cancel-btn">取消</button>
										<button type="button" class="btn btn-xs btn-success">提交回复</button>
									</div>
								</div>
							</div>
						</div></li> -->
				</ul>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
