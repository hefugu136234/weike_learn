var locationData;

jQuery.fn.dataTableExt.oApi.fnPagingInfo = function(oSettings) {
	return {
		"iStart" : oSettings._iDisplayStart,
		"iEnd" : oSettings.fnDisplayEnd(),
		"iLength" : oSettings._iDisplayLength,
		"iTotal" : oSettings.fnRecordsTotal(),
		"iFilteredTotal" : oSettings.fnRecordsDisplay(),
		"iPage" : oSettings._iDisplayLength === -1 ? 0 : Math
				.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength),
		"iTotalPages" : oSettings._iDisplayLength === -1 ? 0 : Math
				.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength)
	};
};
$(document).ready(function() {
	showActive([ 'assets-mgr', 'speaker_mgr_nav' ]);
	
	//头像上传
	var $e = $('#speakerHeadImg');
	uploaderInit(new Part($e, 1, function(part) {
	}, uploadFinished).init());
	function uploadFinished(src) {
		$('#imgTaskId').val(src);
	}

	$.get('/info/getLocation',function(data){
		locationData = jQuery.parseJSON(data) ;
	}) ;

	//初始化ueditor编辑器
	editor = UE.getEditor('speakerDescription');
	
	//回显编辑器里的内容
	editor.addListener("ready", function() {
		editor.setContent($('#speakerDescripion_hiden').text());
	});
	
	$('#speaker_form').submit(function(event){
		event.preventDefault();
		var province=$('#province').val();
		var city=$('#city').val();
		var hospital=$('#hospital').val();
		var departments=$('#departments').val();
		var img = $('#imgTaskId').val();
		
		if(province=='0'){
			alert('请选择省份');
			return false;
		}
		
		if(city=='0'){
			alert('请选择城市');
			return false;
		}
		
		if(hospital=='0'){
			alert('请选择医院');
			return false;
		}
		
		if(departments=='0'){
			alert('请选择科室');
			return false;
		}
		
		 $.post('/project/speaker/save', $('#speaker_form').serialize(), function(data) {
				if (data.status == 'success') { 
					alert("保存成功");
					location.href = '/project/speaker/mgr';
				} else {
					 if(!!data.message){
							alert(data.message);
						}else{
							alert("保存失败");
						}
				}

			});
	});
});

function clearContent(parent) {
	parent.empty();
	parent.append('<option value="0">请选择</option>');
}

function changeProvince(uuid) {
	clearContent($('#city'));
	if (uuid != 0) {
		
		var cityItem = $('#city');
		$.each(locationData,function(index,item) {
			if(item.uuid == uuid){
				var citys = item.citys ;
				if (citys != null) {
					$.each(citys,function(index,city) {
						var option = '<option value=' + city.uuid + '>' + city.name
								+ '</option>';
						cityItem.append(option) ;
					});
				} else {
					alert("城市数据加载错误") ;
				}
			}
		}) ;
	}
}

function changeCity(uuid) {
	clearContent($('#hospital'));
	if (uuid != 0) {
		$.getJSON('/api/webchat/hospital/' + uuid, function(data) {
			var hospital = $('#hospital');
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

//function submitFrom() {
//	 $.post('/project/speaker/save', $('#speaker_form').serialize(), function(data) {
//		if (data.status == 'success') { 
//			alert("添加成功");
//			location.href = '/project/speaker/mgr';
//		} else {
//			 if(!!data.message){
//					alert(data.message);
//				}else{
//					alert("添加失败");
//				}
//		}
//
//	});
//}