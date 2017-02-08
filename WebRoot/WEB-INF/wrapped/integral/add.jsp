<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="/assets/js/admin/uploader_common.js"></script>
<script type="text/javascript" src="/assets/js/admin/integral/consume_add_valid.js"></script>

<style type="text/css">
#image-preview {
	max-height: 300px;
	display: none
}
.pre-view {
	border: 1px gray solid;
	max-width: 280px;
	max-height: 280px;
}
</style>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>积分商城管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/integral/goods/list/page">返回列表</a></li>
			<li class="active">新建商品</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/project/integral/goods/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>新建商品</h5>
		</div>
		<div class="ibox-content">
			<form id="consume_form" class="form-horizontal formBox valForm">
				
				<input type="hidden" id="uploadTag" value="" />
					
				<div class="form-group">
					<label class="col-sm-3 control-label">商品名称：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="name" id="name"
							required="required" maxlength="59" placeholder="60字以内"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">商品价格：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="price" id="price"
							required="required" maxlength="80" placeholder="如：3.27"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">兑换积分：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="integral" id="integral"
							required="required" maxlength="80" placeholder="请输入整数"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">商品库存：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="number" id="number"
							required="required" maxlength="80" placeholder="请输入整数"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">商品类型：</label>
					<div class="col-sm-4">
					<select class="form-control" name="goodType" id="goodType">
					    <option value="">请选择</option>
					     <option value="1">虚拟商品</option>
					     <option value="3">实体商品</option>
					</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">是否实名：</label>
					<div class="col-sm-4">
					<select class="form-control" name="sign" id="sign">
					    <option value="">请选择</option>
					     <option value="0">无需实名</option>
					     <option value="1">实名用户</option>
					</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">兑换次数限制：</label>
					<div class="col-sm-2">
						<select id="ulimit_selector" class="form-control">
							<option value="0" selected="selected">无限制</option>
							<option value="1">设上限</option>
						</select>
					</div>
					<div class="col-sm-2" id="ulimit_number" style="display: none;">
						<input class="form-control" placeholder="请输入上限数量（单位：次）"
							name="userLimited" id="userLimited">
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">商品封面：</label>
					<div class="col-sm-6">
						<input id="cover" type="file" class="form-control" value="商品封面" />
						<input type="hidden" name="cover" id="cover_hidden">
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">商品简介：</label>
					<div class="col-md-6">
						<textarea rows="6" class="form-control" maxlength="999"
							name="description" id="description" placeholder="1000字以内"></textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit" id="save_btn">保存</button>
						<a href="/project/integral/goods/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
showActive([ 'goods-list', 'integral-mgr' ]);
	var $e = $('#cover')
	uploaderInit(new Part($e, 1, function(part) {
	}, uploadFinished).init())

	function uploadFinished(src) {
		$('#cover_hidden').val(src)
		$('#uploadTag').val('uploadSuccess');
	}
	
	var ulimit = $('#ulimit_selector')
	ulimitui();
	ulimit.change(function(){
		ulimitui()
	})
	function ulimitui(){
		if(ulimit.val() == '1'){
			$('#ulimit_number').show();
			$('#userLimited').val('')
		}else{
			$('#ulimit_number').hide();
			$('#userLimited').val('0')
		}
	}

	function submitForm(form){$.post('/admin/consume/save',$(form).serialize()).always(
		function(data) {
			if (data.status == 'success') {
				window.location.href = '/project/integral/goods/list/page';
			} else {
				if (!!data.message) {
					alert(data.message)
				} else {
					alert('添加失败');
				}
			}
		})
	}
</script>

