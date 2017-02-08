<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/admin/hospital_mgr/hospital_mgr_list.js"></script>
<script src="/assets/js/admin/hospital_mgr/hospital_location.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>医院管理</h2>
		<ol class="breadcrumb">
			<li class="active">医院列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/hospital/mgr/add/page">添加医院</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>医院列表</h5>
		</div>
		<div class="ibox-content">
			<div id="hospital_mgr" class="dataTables_wrapper">
				<div id="filterSearch">
					<div id="province" style="display:inline">
						省份过滤：
						<select id="selector_province" onchange="changeSelectorProvince(this.value);">
							<option value="0">所有省份</option>
						</select>
					</div>&nbsp;&nbsp;
					<div id="city" style="display:inline">
						城市过滤：
						<select id="selector_city" onchange="changeSelectorCity(this.value);">
							<option value="0">该省份下所有城市</option>
						</select>
					</div>
				</div><br/>
				<table id="hospital_list" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 20%;">名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">等级</th>
							<th rowspan="1" colspan="1" style="width: 10%;">所在地</th>
							<th rowspan="1" colspan="1" style="width: 10%;">电话</th>
							<th rowspan="1" colspan="1" style="width: 15%;">详细地址</th>
							<th rowspan="1" colspan="1" style="width: 10%;">更新时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>

<script>
	$(function(){
		buildOption(${requestScope.province_list},$('#selector_province'),'省份数据加载出错');
	})
	
	/*function changeSelectorProvince(uuid) {
		clearContent($('#selector_city'));
		if (uuid != 0) {
			//加载城市数据
			$.getJSON('/api/webchat/city/' + uuid, function(data) {
				var city = $('#selector_city');
				if (data.itemList) {
					$.each(data.itemList, function(index, item) {
						var option = '<option value=' + item.uuid + '>' + item.name
								+ '</option>';
						city.append(option);
					});
				} else {
					alert("城市数据加载错误");
				}
			});
		}
		//根据选中省份，执行过滤查询
		var provinceSelectedValue = $('#selector_province option:selected').val();
		table_hospital.fnReloadAjax('/hospital/mgr/datatable?provinceUuid=' + provinceSelectedValue);
	}*/
	
	function changeSelectorCity(uuid){
		var provinceSelectedValue = $('#selector_province option:selected').val();
		var citySelectedValue = $('#selector_city option:selected').val();
		if(0 == citySelectedValue){
			table_hospital.fnReloadAjax('/hospital/mgr/datatable?provinceUuid=' + provinceSelectedValue);
		}else{
			table_hospital.fnReloadAjax('/hospital/mgr/datatable?provinceUuid=' + provinceSelectedValue + '&cityUuid=' + citySelectedValue);
		}
	}
	
	function clearContent(parent) {
		parent.empty();
		parent.append('<option value="0">该省份下所有城市</option>');
	}
	
	function buildOption(vo,elem,msg){
		var itemList=vo.itemList;
		if(!!itemList){
			$.each(itemList, function(index, item) {
				var option = '<option value='+item.uuid+'>' + item.name
						+ '</option>';
				elem.append(option);
			});
		}else{
			alert(msg);
		}
	}
</script>
