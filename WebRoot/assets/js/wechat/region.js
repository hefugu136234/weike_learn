function clearContent(parent) {
	parent.empty();
	parent.append('<option value="">请选择</option>');
}

function changeProvince(uuid) {
	clearContent($('#city'));
	clearContent($('#hosipital'));
	if (!!uuid) {
		$.getJSON('/api/webchat/city/' + uuid, function(data) {
			var city = $('#city');
			console.log(data)
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
	clearContent($('#hosipital'));
	if (!!uuid) {
		$.getJSON('/api/webchat/hospital/' + uuid, function(data) {
			var hosipital = $('#hosipital');
			if (data.itemList) {
				$.each(data.itemList, function(index, item) {
					var option = '<option value=' + item.uuid + '>' + item.name
							+ '</option>';
					hosipital.append(option);
				});
			} 
		});
	}
}