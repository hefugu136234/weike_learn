var locationData;
$(document).ready(function(){

	$.get('/info/getLocation',function(data){
		locationData = jQuery.parseJSON(data) ;
	}) ;
})

function getCity(uuid){
	clearContent($('#hospital_city')) ;
	if (uuid != 0) {
		$.each(locationData,function(index,item) {
			if (item.uuid == uuid){
				var citys = item.citys ;
				if( citys != null) {
					$.each(citys,function(index,city) {
						var option = '<option value=' + city.uuid + '>' + city.name
								+ '</option>';
						$('#hospital_city').append(option) ;
					});
				} else {
					alert("城市数据加载错误") ;
				}
			}
		}) ;
	}
}

function changeSelectorProvince(uuid) {
	clearContent($('#selector_city')) ;
	if (uuid != 0) {
		$.each(locationData,function(index,item) {
			if(item.uuid == uuid){
				var citys = item.citys ;
				if (citys != null) {
					$.each(citys,function(index,city) {
						var option = '<option value=' + city.uuid + '>' + city.name
								+ '</option>';
						$('#selector_city').append(option) ;
					});
				} else {
					alert("城市数据加载错误") ;
				}
			}
		}) ;
	}
	//根据选中省份，执行过滤查询
	var provinceSelectedValue = $('#selector_province option:selected').val() ;
	table_hospital.fnReloadAjax('/hospital/mgr/datatable?provinceUuid=' + provinceSelectedValue) ;
}