<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet"
	href="/assets/css/plugins/datetimepicker/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="/assets/css/site.css">

<script src="/assets/ueditor/ueditor.config.js"></script>
<script src="/assets/ueditor/ueditor.all.min.js"></script>
<script src="/assets/ueditor/lang/zh-cn/zh-cn.js" charset="utf-8"></script>
<script src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script src="/assets/js/moment.min.js"></script>
<script src="/assets/js/monment_zh-cn.js"></script>
<script
	src="/assets/js/plugins/datetimepicker/bootstrap-datetimepicker.min.js"
	charset="UTF-8"></script>
<script src="/assets/js/admin/gamesMgr/gamesMgr_common.js"></script>
<script src="/assets/js/admin/gamesMgr/gamesMgr_award_operation.js"></script>

<style>
#games_container li {
	margin-top: 10px;
}

#games_container li:HOVER {
	background-color: #f3f3f4;
}

.input-group-addon-my {
	border: 1px solid #E5E6E7;
	font-size: 16px;
	padding: 6px 10px;
	color: green;
}
</style>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>游戏管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/games/list/page">返回列表</a></li>
			<li class="active">${not empty requestScope.lottery.uuid ? '编辑游戏' : '新建游戏' }</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/games/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>${not empty requestScope.lottery.uuid ? '编辑游戏' : '新建游戏' }</h5>
		</div>
		<div class="ibox-content">
			<form id="games_form" class="form-horizontal formBox valForm">
				<input type="hidden" name="token" id="submitToken"
					value="${requestScope.token}" /> <input type="hidden" name="uuid"
					id="uuid" value="${requestScope.lottery.uuid }" />
				<textarea class="hide" id="rules_hiden">${requestScope.lottery.rules }</textarea>
				<input type="hidden" name="templateId" id="templateId"
					value="${requestScope.lottery.templateId }" /> <input
					type="hidden" name="game_id" id="game_id"
					value="${not empty requestScope.lottery.id ? requestScope.lottery.id : '0' }" />
				<input type="hidden" name="status" id="status"
					value="${not empty requestScope.lottery.status ? requestScope.lottery.status : '0' }" />
				<input type="hidden" name="isActive" id="isActive"
					value="${not empty requestScope.lottery.isActive ? requestScope.lottery.isActive : '0' }" />
				<div class="form-group">
					<label class="col-sm-2 control-label">游戏名称：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" name="name" id="gameName"
							value="${requestScope.lottery.name }" required="required"
							maxlength="19" placeholder="请输入游戏名称(20字以内)" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-2 control-label">用户参与次数：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" name="joinTimes"
							id="gameJoinTimes" value="${requestScope.lottery.joinTimes }"
							id="bookStartDate" required="required" maxlength="8"
							placeholder="请设置该游戏用户可参与次数" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-2 control-label">游戏开始时间：</label>
					<div class="col-sm-3">
						<div class="input-group">
							<input type="text" class="form-control" name="beginDate"
								id="beginDate" readonly="readonly"
								value="${requestScope.lottery.beginDate }" required="required" />
						</div>
					</div>
					<label class="col-sm-2 control-label">游戏结束时间：</label>
					<div class="col-sm-3">
						<div class="input-group">
							<input type="text" class="form-control" name="endDate"
								id="endDate" readonly="readonly"
								value="${requestScope.lottery.endDate }" required="required" />
						</div>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<!-- <div class="form-group">
					<label class="col-sm-2 control-label">参与用户：</label>
					<div class="col-sm-6" id="joinType">
						<input type="checkbox" id="all" />所有用户&nbsp;&nbsp;
						<input type="checkbox" id="certification" />实名用户&nbsp;&nbsp;
						<input type="checkbox" id="certification_no" />未实名用户
					</div>
				</div>
				<div class="hr-line-dashed"></div> -->

				<div class="form-group">
					<label class="col-sm-2 control-label"></label>
					<div class="col-sm-8">
						<div id="games_container">
							<ul class="list-group"></ul>
						</div>
						<input type="button" class="btn btn-primary"
							id="new_games_controller" value="添加奖项" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-2 control-label">游戏模版：</label>
					<div class="col-sm-4">
						<select class="form-control" id="gameTemplateId" name="templateId">
							<option value="0">请选择</option>
							<option value="1">摇一摇</option>
							<!-- <option value="2">大转盘</option>
							<option value="3">刮刮卡</option> -->
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-2 control-label">游戏规则：</label>
					<div class="col-sm-8">
						<%-- <textarea class="form-control" rows="7" name="rules" id="gameRules" maxlength="99" >
										${requestScope.lottery.rules }</textarea> --%>
						<script id="gameRules" name="rules" required="required"
							type="text/plain" style="height: 250px;"></script>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-8">
						<textarea class="form-control" rows="4" name="mark" id="gameMark"
							maxlength="99" placeholder="游戏简介(100字以内)">${requestScope.lottery.mark }</textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit"
							id="saveOrUpdateGames">保存</button>
						<a href="/project/games/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<div style="display: none;" id="games_seed">
	<div class="vote-item-clean">
		<div class="vote-wrapped">
			<div class="form-group">
				<label class="col-sm-2 control-label">奖项名称：</label>
				<div class="col-sm-4">
					<input type="text" class="form-control vote-title" name="awardName"
						value="" maxlength="19" placeholder="请输入奖项名称(20字以内)">
				</div>
				<label class="col-sm-2 control-label">奖项数量：</label>
				<div class="col-sm-4">
					<input type="text" class="form-control vote-title"
						name="awardNumber" maxlength="8" value="" placeholder="请输入奖项数量(正整数)">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">中奖次数：</label>
				<div class="col-sm-4">
					<input type="text" class="form-control vote-title"
						name="awardMaxWinTimes" maxlength="8" value="" placeholder="每个用户中奖次数(正整数)">
				</div>
				<label class="col-sm-2 control-label">中奖概率：</label>
				<div>
					<div class="col-sm-3">
						<input type="text" class="form-control vote-title"
							name="awardConditional" value="" placeholder="整数或小数">
					</div>
					<span class="input-group-addon-my">%</span>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-6 col-sm-offset-3">
				<button class="btn btn-warning vote-del bfL mt20" type="button"
					id="delButton">删除</button>
			</div>
		</div>
	</div>
</div>
<script>
	var awards = ${requestScope.awards}
</script>