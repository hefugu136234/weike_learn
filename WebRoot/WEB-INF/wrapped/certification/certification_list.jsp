<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css" >
<link rel="stylesheet" href="/assets/css/plugins/dataTables/dataTables.responsive.css" >
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css" >
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/admin/certification/certification_list.js"></script>
<script src="/assets/js/plugins/blueimp/jquery.blueimp-gallery.min.js"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>实名认证</h2>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>认证列表</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<div id="filterSearch">
					审核状态：<select name="resourceType" id="searchButton_state" >
								<option value=''>所有</option>
								<option value='0'>未审核</option>
								<option value='1'>已审核</option>
								<option value='2'>审核未通过</option>
							 </select>&emsp;&emsp;
				</div><br/>
				<table id="certification_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;" >
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">预览</th>
							<th rowspan="1" colspan="1" style="width: 10%;">真实姓名</th>
							<th rowspan="1" colspan="1" style="width: 10%;">申请人(登录名)</th>
							<th rowspan="1" colspan="1" style="width: 10%;">申请人昵称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">提交时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">审核时间</th>
							<th rowspan="1" colspan="1" style="width: 15%;">备注</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 10%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>


<div id="blueimp-gallery" class="blueimp-gallery">
	<div class="slides"></div>
	<h3 class="title"></h3>
	<a class="prev">‹</a> <a class="next">›</a> <a class="close">×</a> <a
		class="play-pause"></a>
	<ol class="indicator"></ol>
</div>
<!-- 拒绝审核-->
<div class="modal fade" id="dataModal_refuse" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel_pdf">拒绝审核通过的理由：</h4>
			</div>
			<div class="modal-body">
			<div class="form-horizontal">
				<div class="form-group">
						<label class="control-label col-md-2">拒绝理由</label>
						<div class="col-md-9">
						    <input type="hidden" id="refuse_uuid" value=""/>
							<input type="text" class="form-control" id="refuse_case" maxlength="200" />
						</div>
					</div>
			</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="resufe_confirm_btn" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- 拒绝审核-->
<!-- 申请者信息核对更新 -->
<div class="modal fade" id="certificationModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true" onmousemove="createInfo()" data-id="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">实名信息认证</h4>
			</div>
			<div class="modal-body">
				<div id="petitionerDetail" class="form-horizontal">
					<div class="form-group">
						<center>
							<div >
								<img style="display: none; max-height: 400px;" id="image-preview"
									alt="" class="img-thumbnail" src="">
							</div>
						</center>
					</div>
					<div class="hr-line-dashed"></div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label">申请人：</label>
						<div class="col-sm-6">
							<input type="text" id="username" class="form-control"
								readonly="readonly" />
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label">真实姓名：</label>
						<div class="col-sm-6">
							<input type="text" id="realname" class="form-control" />
						</div>
					</div>

					<div id="oups_code_div" class="form-group">
						<label class="col-sm-3 control-label">认证类型：</label>
						<div class="col-sm-6">
							<select id="select_type"  class="form-control">
								<option value=0 >医生用户</option>
								<option value=1 >企业用户</option>
							</select>
						</div>
					</div>
					
					<div class="hr-line-dashed"></div>
					
					<h3><font color="green">申请者当前信息</font></h3>
					<!-- 医生信息 -->
					<div id="doctor_detail" >
						<div class="form-group">
							<label class="col-sm-3 control-label">所在省份：</label>
							<div class="col-sm-6">
								<select class="form-control" id="province" name="province"
									required="required" onchange="changeProvince(this.value);">
									<option value="0">请选择</option>
								</select>
							</div>
						</div>
					
						<div class="form-group">
							<label class="col-sm-3 control-label">所在城市：</label>
							<div class="col-sm-6">
								<select class="form-control" id="city" name="city"
									required="required" onchange="changeCity(this.value);">
									<option value="0">请选择</option>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">所在医院：</label>
							<div class="col-sm-6">
								<select class="form-control" id="hospital" name="hospital_uuid"
									required="required">
									<option value="0">请选择</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-3 control-label">所属科室：</label>
							<div class="col-sm-6">
								<select class="form-control" id="departments" name="department_uuid"
									required="required">
									<option value="0">请选择</option>
								</select>
							</div>
						</div>
						
						<div class="form-group" id="select_sex">
							<label class="col-sm-3 control-label">性别：</label>
							<div class="col-sm-6">
								<select id="sex" class="form-control" name="sex">
									<!-- <option value="2">请选择</option> -->
									<option value="1">男</option>
									<option value="0">女</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-3 control-label">职称：</label>
							<div class="col-sm-6">
								<input type="text" id="professor_doctor" class="form-control" />
							</div>
						</div>
						
					</div>
					
					<!-- 企业用户信息 -->
					<div id="company_detail" >
						<div class="form-group">
							<label class="col-sm-3 control-label">企业名称：</label>
							<div class="col-sm-6">
								<select class="form-control" id="company" name="companyName"
									required="required">
									<option value="0">请选择</option>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">职称：</label>
							<div class="col-sm-6">
								<input type="text" id="professor_company" class="form-control" />
							</div>
						</div>
					</div>
					
					<!-- 认证推荐 -->
					<div id="result_info">
						<h3><font color="green">认证推荐</font></h3>
						<div class="form-group">
							<label class="col-sm-3 control-label"></label>
							<div class="col-sm-6">
								<textarea id="certificationInfo" class="form-control" name="certificationInfo" 
								cols="60" rows="4" maxlength="240" ></textarea>
							</div>
						</div>
					</div>
					
					<!-- 用户讲者信息 -->
					<!-- <div id="user_speaker_info" >
						<!-- 未关联讲者，列出推荐讲者，并提示关联讲者 -->
						<!-- <div id="noAssociationSpeaker">
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-3 dtr-inline" ><font color="red">该用户还没有关联讲者，请在右侧选择讲者以关联</font></label>
								<div class="col-md-6">
									<select class="form-control" id="speaker_selector" name="speaker_uuid">
										<option>请输入讲者姓名检索讲者信息</option>
									</select>
								</div>
							</div>
							搜索讲者，显示讲者详情
							<div id="speaker_detail_search">
								<h3><font color="green" id="speaker_log">欲关联讲者信息</font></h3>
								<div class="form-group">
									<label class="col-sm-3 control-label">讲者姓名：</label>
									<div class="col-sm-6">
										<input type="text" id="speaker_name_search" class="form-control"
									readonly="readonly" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">性别：</label>
									<div class="col-sm-6">
										<input type="text" id="speaker_sex_search" class="form-control"
									readonly="readonly" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">手机：</label>
									<div class="col-sm-6">
										<input type="text" id="speaker_mobile_search" class="form-control"
									readonly="readonly" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">所在城市：</label>
									<div class="col-sm-6">
										<input type="text" id="speaker_city_search" class="form-control"
									readonly="readonly" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">所属医院：</label>
									<div class="col-sm-6">
										<input type="text" id="speaker_hospital_search" class="form-control"
									readonly="readonly" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">所属科室：</label>
									<div class="col-sm-6">
										<input type="text" id="speaker_department_search" class="form-control"
									readonly="readonly" />
									</div>
								</div>
							</div>
						</div> -->
					<!-- <h3><font color="green" id="no_speaker">暂无讲者推荐</font></h3> -->
					<div class="hr-line-dashed"></div>
					<h4><font color="red" id="creae_speaker">该用户还没关联讲者，将以当前信息创建讲者</font></h4>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id="submitButton" class="btn btn-primary mr20" >审核</button>
			</div>
		</div>
	</div>
</div>
