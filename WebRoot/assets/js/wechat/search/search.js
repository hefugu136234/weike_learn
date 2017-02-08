$(function() {
	search_input = $('#search_input');
	search_has_result_ul=$('#search_has_result_ul');

	$('#common_serach').submit(function(){
		var search_input_val = search_input.val();
		if (search_input_val == '') {
			alert('请输入搜索内容！');
			return false;
		}
		searchAction(search_input_val);
		return false;
	});
});

var search_input,search_has_result_ul;

// 执行搜索的输入
function searchAction(search) {
	var dataTime = new Date().getTime();
	//api/webchat/search/action/result
	// /api/webchat/search/thrid/action/result
	$.getJSON('/api/webchat/search/action/result', {
		'search' : search,
		'dateTime' : dataTime
	}, function(data) {
		if (isSuccess(data)) {
			searchResultShow(data);
		} else {
			if(!!data.message){
				alert(data.message);
			}else{
				alert('网络异常');
			}
		}
	});
}
// 其他搜索的点击操作
function aClickSearch(obj) {
	var search = $(obj).data('search');
	searchAction(search);
}

// 结果显示操作
function searchResultShow(data) {
	var search = data.query;
	search_input.val(search);
	var hits = data.hits;
	var list = data.items;
	if(isItemList(list)){
		$('#search_has_result').find('span.num').html(hits);
		$('#search_has_result_ul').empty();
		// 处理li数据
		drawResourceList(search_has_result_ul,list);
		$('#search_has_result').show();
		$('#search_no_reslut').hide();
	}else{
		$('#search_has_result').hide();
		$('#search_no_reslut').show();
	}
}
