$(document).ready(function() {
	showActive([ 'pro_user_list_nav', 'holder_project' ]);
	
	var depart_list;
	var province_list;
	var company_list;
	var _data;
	
	var cell={
		doctor_detail_div : $('#doctor_detail'),
		company_detail_div : $('#company_detail'),
		user_type : $('#user_type'),
		user_name : $('#user_name'),
		user_nickname : $('#user_nickname'),
		user_realName : $('#user_realName'),
		user_professor : $('#user_professor'),
		user_sex : $('#user_sex'),
		user_role : $('#user_role'),
		user_password : $('#user_password'),
		user_province : $('#user_province'),
		user_city : $('#user_city'),
		user_hospital : $('#user_hospital'),
		user_departments : $('#user_departments'),
		user_company : $('#user_company'),
		user_mark : $('#user_mark')
	}
	
	$.get('/project/getProvinceList', function() {}).always(function(data_province) {
		province_list = data_province;
	})
	$.get('/project/getDepartList', function() {}).always(function(data_depart) {
		depart_list = data_depart;
	})
	$.get('/project/getManufacturerList', function() {}).always(function(result) {
		company_list = result;
	})
	
	
	//获取用户信息
	$.get('/project/userReference/user/getUpdateUserData', {
		userUuid: $('#userUuid').val()
	}).always(function(data){
		_data = data;
		if('success' == data['status']){
			initPage(data, cell, depart_list, province_list, company_list);
		}else{
			alert('获取数据失败，请重试')
		}
	})
	
	cell.user_type.change(function(){
		dataInit($('#user_type').val(), cell, _data, depart_list, province_list, company_list);
	})
	
	submitFrom(cell);
})

function initPage(data, cell, depart_list, province_list, company_list){
	//test
	console.log(data);
	
	if('success' !== data['status']){
		alert(data['message']);
		return false;
	}else{
		//用户类型回显
		cell.user_type.val(data['data']['type']);
		
		dataInit(data['data']['type'], cell, data, depart_list, province_list, company_list);
		
		//基础信息回显
		cell.user_name.val(data['data']['userName']);
		cell.user_nickname.val(data['data']['userNickName']);
		if('NO_CER' !== data['data']['userRealName']){
			cell.user_realName.val(data['data']['userRealName']);
		}else{
			cell.user_realName.val('');
			$('#realname').hide();
		}
		cell.user_professor.val(data['data']['professor']);
		cell.user_mark.val(data['data']['mark']);
		$.post('/project/user/getRole',{},function(data_role){
			cell.user_role.empty();
			var item = '';
			for(var i=0;i<data_role.length;i++){
				item = $('<option value='+data_role[i].roleName+'>'+data_role[i].roleDesc+'</option>');
				cell.user_role.append(item);
			}
			if('' !== data['data']['userRole'] && 0 !== data['data']['userRole']){
				cell.user_role.val(data['data']['userRole']);
			}
		});
		
		if('' !== data['data']['sex'] && 0 !== data['data']['sex']){
			cell.user_sex.val(data['data']['sex']);
		}
		
	}
}

function dataInit(type, cell, data, depart_list, province_list, company_list){
	if(0 == type){
		cell.doctor_detail_div.removeClass('hide');
		cell.company_detail_div.addClass('hide');
		
		//省份回显
		buildOption(province_list, cell.user_province, '省份列表加载失败');
		if('' !== data['data']['provinceUuid'] && 0 !== data['data']['provinceUuid']){
			cell.user_province.val(data['data']['provinceUuid']);
		}
		
		//科室回显
		buildOption(depart_list, cell.user_departments, '科室列表加载失败');
		if('' !== data['data']['departmentUuid'] && 0 !== data['data']['departmentUuid']){
			cell.user_departments.val(data['data']['departmentUuid']);
		}
		
		//城市回显
		buildOptionCity(data['data'], cell.user_city, '城市列表加载失败');
		if('' !== data['data']['cityUuid'] && 0 !== data['data']['cityUuid']){
			cell.user_city.val(data['data']['cityUuid']);
		}
		
		//医院回显
		buildOptionHospital(data['data'], cell.user_hospital, '医院列表加载失败');
		if('' !== data['data']['hospitalUuid'] && 0 !== data['data']['hospitalUuid']){
			cell.user_hospital.val(data['data']['hospitalUuid']);
		}
	}else if(1 == type){
		cell.company_detail_div.removeClass('hide');
		cell.doctor_detail_div.addClass('hide');
		
		//厂商回显
		buildOption(company_list, cell.user_company, '厂商列表加载失败');
		if('' !== data['data']['companyUuid'] && 0 !== data['data']['companyUuid']){
			cell.user_company.val(data['data']['companyUuid']);
		}
	}else{
		cell.doctor_detail_div.addClass('hide');
		cell.company_detail_div.addClass('hide');
	}
}

function buildOption(vo,elem,msg){
	clearContent(elem);
	var itemList=vo.itemList;
	
	if(!!itemList){
		if(0 == itemList.length){
			clearContent(elem);
			return ;
		}
		$.each(itemList, function(index, item) {
			var option = '<option value='+item.uuid+'>' + item.name
					+ '</option>';
			elem.append(option);
		});
	}else{
		alert(msg);
	}
}

function buildOptionCity(vo,elem,msg){
	clearContent(elem);
	var itemList=vo.cityList;
	
	if(!!itemList){
		if(0 == itemList.length){
			clearContent(elem);
			return ;
		}
		$.each(itemList, function(index, item) {
			var option = '<option value='+item.uuid+'>' + item.name
					+ '</option>';
			elem.append(option);
		});
	}
}

function buildOptionHospital(vo,elem,msg){
	clearContent(elem);
	var itemList=vo.hosList;
	
	if(!!itemList){
		if(0 == itemList.length){
			clearContent(elem);
			return ;
		}
		$.each(itemList, function(index, item) {
			var option = '<option value='+item.uuid+'>' + item.name
					+ '</option>';
			elem.append(option);
		});
	}
}

function clearContent(parent) {
	parent.empty();
	parent.append('<option value="0">请选择</option>');
}

function changeProvince(uuid) {
	clearContent($('#user_city'));
	if (uuid != 0) {
		$.getJSON('/api/webchat/city/' + uuid, function(data) {
			var city = $('#user_city');
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
}

function changeCity(uuid) {
	clearContent($('#user_hospital'));
	if (uuid != 0) {
		$.getJSON('/api/webchat/hospital/' + uuid, function(data) {
			var hospital = $('#user_hospital');
			if (data.itemList) {
				$.each(data.itemList, function(index, item) {
					var option = '<option value=' + item.uuid + '>' + item.name
							+ '</option>';
					hospital.append(option);
				});
			} 
		});
	}
}

function submitFrom(cell){
	$('#project_user_update').submit(function(event){
		event.preventDefault();
		
		var type_val = cell.user_type.val();
		
		var province_val = cell.user_province.val();
		var city_val = cell.user_city.val();
		var hospital_val = cell.user_hospital.val();
		var department_val = cell.user_departments.val();
		
		var company_val = cell.user_company.val();
		
		var name_val = cell.user_name.val();
		var nickName_val = cell.user_nickname.val();
		var realName_val = cell.user_realName.val();
		var professor_val = cell.user_professor.val();
		var sex_val = cell.user_sex.val();
		var role_val = cell.user_role.val();
		var mark_val = cell.user_mark.val();
		
		if(type_val == '0'){
			if(province_val=='0'){
				alert('请选择省份');
				return false;
			}
			if(city_val=='0'){
				alert('请选择城市');
				return false;
			}
			if(hospital_val=='0'){
				alert('请选择医院');
				return false;
			}
			if(department_val=='0'){
				alert('请选择科室');
				return false;
			}
		}else if(type_val == '1'){
			if(company_val=='0'){
				alert('请选择企业');
				return false;
			}
		}
		if('' == name_val){
			alert('请填写用户名');
			return false;
		}
		if('' == nickName_val){
			alert('请填写用户昵称');
			return false;
		}
//		if(! $('#realname').is(":hide")){
//			if('' == realName_val){
//				alert('请填写用户真实姓名');
//				return false;
//			}
//		}
		if('' == professor_val){
			alert('请填写职称');
			return false;
		}
		if('' == sex_val){
			alert('请选择性别');
			return false;
		}
		if('0' == role_val){
			alert('请选择用户角色');
			return false;
		}
		
		$.post('/project/userReference/update/save/' + $('#userUuid').val(), $('#project_user_update').serialize(), function(data) {
			if (data.status == 'success') { 
				alert("修改成功");
				location.href = '/project/userReference/list';
			} else {
				if(!!data.message){
					alert(data.message);
				}else{
					alert("修改成功");
				}
			}
		});
	});
}
