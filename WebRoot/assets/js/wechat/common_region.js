function clearContent(parent) {
	parent.empty();
	parent.append('<option value="">请选择</option>');
}

var locationData ;

$(document).ready(function(){
	$.get('/info/getLocation',function(data){
		locationData = jQuery.parseJSON(data) ;
		console.log(locationData);
	}) ;
})

function changeProvince(uuid) {
	clearContent($('#city')) ;
	clearContent($('#district')) ;
	if (!!uuid) {
		$.each(locationData,function(index,item) {
			if (uuid == item.uuid) {
				var citys = item.citys ;
				if (citys != null) {
					$.each(citys,function(index,city) {
						var option = '<option value=' + city.uuid + '>' + city.name
								+ '</option>';
						$('#city').append(option) ;
					}) ; 
				} 
			}
		}) ;
	}
}

function changeCity(uuid) {
	var puuid = $("#province").val() ;
	clearContent($('#district'));
	if (!!uuid) {
		$.each(locationData,function(index,item) {
			if (item.uuid == puuid) {
				var citys = item.citys ;
				var districts ;
				if (citys != null) {
					$.each(citys,function(index,city) {
						if (city.uuid == uuid) {
							districts = city.districts ;
						}
					});
				}
				if (districts != null) {
					$.each(districts,function(index,district) {
						var option = '<option value=' + district.uuid + '>' + district.name
								+ '</option>';
						$('#district').append(option) ;
					});
				}
			}
		});
	}
}