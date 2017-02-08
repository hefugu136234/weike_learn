<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta name="renderer" content="webkit">
<title>知了微课 - 绑定帐号</title>
<link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
<link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<!-- <script
	src="//cdn.bootcss.com/1000hz-bootstrap-validator/0.10.2/validator.min.js"></script> -->
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script src="/assets/js/web/common.js"></script>
<script type="text/javascript" src="/assets/js/wechat/region.js"></script>
<script src="/assets/js/web/reg.js"></script>

<!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<jsp:include page="partials/top.jsp"></jsp:include>

	<div class="container">
		<ol class="breadcrumb crumb-nav">
			<li><a href="index.jsp">首页</a></li>
			<li class="active">绑定帐号</li>
		</ol>

		<div class="panel no-radius reg-panel">
			<div class="panel-body">
				<form id="reg_from">
					<div class="title-tag clearfix">
						<h5 class="tt pull-left">
							<span class="icon icon-user"></span>基础信息
						</h5>
					</div>

					<div class="form-horizontal default-form">
						<div class="form-group">
							<label class="col-sm-2 control-label">手机号码</label>
							<div class="col-sm-4">
								<input type="tel" class="form-control" id="user_phone" name="user_phone"
									placeholder="请输入" maxlength="15" required="required"/>
							</div>
							<div class="col-sm-6">
								<button type="button" class="btn btn-blue" id="reg_code_btn">获取验证码</button>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">验证码</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="code" name="code"
									placeholder="请输入" required="required"/>
							</div>
						</div>
						<div class="separate-blank"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">用户姓名</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="user_name" name="user_name"
									placeholder="请输入" maxlength="20" required="required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">帐号密码</label>
							<div class="col-sm-4">
								<input type="password" class="form-control" id="user_pw" name="user_pw"
									placeholder="请输入" data-minlength="6" maxlength="20" required="required"/>
							</div>
							<!-- <div class="col-sm-6 input-tips">
								<div class="help-block">帐号密码至少6个字符！</div>
							</div> -->
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">重复密码</label>
							<div class="col-sm-4">
								<input type="password" class="form-control" id="user_pw_confirm" name="user_pw_confirm"
									placeholder="请输入" data-match="#user_pw"
									data-match-error="两次输入密码不一致！" maxlength="20" required="required"/>
							</div>
							<div class="col-sm-6 input-tips">
								<div class="help-block with-errors"></div>
							</div>
						</div>

						<div class="qr-code">
							<div class="img">
								<img src="/assets/img/qr.jpg" alt="知了云盒二维码" />
							</div>
							<h5 class="tt">扫一扫 关注知了云盒</h5>
						</div>
					</div>

					<div class="separate-line"></div>

					<div class="title-tag clearfix">
						<h5 class="tt pull-left">
							<span class="icon icon-hospital"></span>医院信息
						</h5>
					</div>

					<div class="form-horizontal default-form">
						<div class="form-group">
							<label class="col-sm-2 control-label">所在省</label>
							<div class="col-sm-4">
								<select class="form-control" id="user_province" name="user_province"
									onchange="changeProvince(this.value);" required>
									<option value="">请选择</option>
									<c:if test="${not empty requestScope.province_list}">
										<c:forEach var="item" items="${requestScope.province_list}">
											<option value="${item.uuid}">${item.name}</option>
										</c:forEach>
									</c:if>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">所在市</label>
							<div class="col-sm-4">
								<select class="form-control" id="city" name="city"
									onchange="changeCity(this.value);" required>
									<option value="">请选择</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">所在医院</label>
							<div class="col-sm-4">
								<select class="form-control" id="hosipital" name="hosipital" required>
									<option value="">请选择</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">所在科室</label>
							<div class="col-sm-4">
								<select id="user_department" class="form-control" name="user_department" required>
									<option value="">请选择</option>
									<c:if test="${not empty requestScope.depart_list}">
										<c:forEach var="item" items="${requestScope.depart_list}">
											<option value="${item.uuid}">${item.name}</option>
										</c:forEach>
									</c:if>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">职位名称</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="user_title" name="user_title"
									placeholder="请输入" maxlength="30" required="required"/>
							</div>
						</div>

					</div>

					<div class="separate-line"></div>

					<div class="form-horizontal default-form mt32">
						<div class="form-group">
							<label class="col-sm-2 control-label">VIP激活</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="user_vip"
									placeholder="请输入">
							</div>
							<div class="col-sm-6 input-tips">(非必填)您可以在您收到的知了云卡背面图层找到VIP激活码。</div>
						</div>

						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10 checkbox protocol-check">
								<input id="checkbox_sel" name="checkbox_sel" type="checkbox" checked="checked"/> <a href="/f/web/reg/protocol" class="tag">我同意用户协议</a>
							</div>
						</div>

						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10 input-tips">
								<p>感谢您的关注！知了微课平台是移动专业医学教育平台，提供您需要的医学专业资讯。</p>
								<p>根据有关规定，医学专业资讯仅供医学专业人士参阅。</p>
								<p>如您尚未绑定帐号，请您先进行帐号绑定，并填写真实信息。</p>
							</div>
						</div>

						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<input type="submit" id="submit_button" class="btn btn-orange long-btn"
									value="提交绑定" />
							</div>
						</div>

					</div>
				</form>
			</div>
		</div>
	</div>

	<jsp:include page="partials/footer.jsp"></jsp:include>

	<div class="modal fade no-radius share-process-modal"
		id="attention_accounts_modal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">关注公众帐号，进行帐号绑定！</h4>
				</div>
				<div class="modal-body">
					<div class="con clearfix">
						<div class="code">
							<img src="/assets/img/qr.jpg" />
						</div>
						<div class="info">请先用微信“扫一扫”左侧的二维码, 关注我们的公众帐号，再进行帐号绑定。</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
