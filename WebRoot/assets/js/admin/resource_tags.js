$(document).ready(function(){
	var parentTags = $('#parentTags');
	var childTags = $('#childTags');
	var checkedArr = new Array; 
	var checkedUuids;
	var resUuid = $('#resourceUuid').val();
	
	showActive([ 'assets-mgr', 'category_mgr_nav' ]);
	loadParentData();
	$('#checkboxSubmitButton').on("click",function(){
		$('#childTagsTable input:checked').each(function(i){ 
			checkedArr[i] = $(this).val(); 
        }); 
		checkedUuids = checkedArr.join(','); 
		if(null != checkedUuids && checkedUuids.length != 0 && null != resUuid && resUuid.length != 0){
			$.post("/project/resource/labeling/resAddLabel/", {checkedTagsUuids: checkedUuids, resourceUuid: resUuid}, function(data){
				if ('success' == data.status){
					alert('关联标签成功');
					location.href='/project/resource/labeling/list/' + resUuid;
				} else {
					if(!!data.message){
						alert(data.message)
					}else{
						alert('关联标签失败');
					}
				}
			})
		}
	})
})

function loadParentData(){
	$.get("/tag/showParentTagListWithoutPageOption", function(){}).done(
		function(data) {
			try {
				var loader = $('#parentTagsTable');
				var body = loader.find('tbody');
				body.empty();
				var _d = $(data['aaData']);
				if (_d.length == 0) {
					body.append($('<tr><td colspan="2">你还没有标签，请在标签管理页面添加标签</td></tr>'));
				} else {
					_d.each(function(i, e) {
						if (e.name) {
							var item = $('<tr><td>' + e.name
									+ '</td><td>' + e.modifyDate
									+ '</td></tr>');
							body.append(item);
							item.click(function() {
								loadChildData(e);
							})
						}
					})
				}
			} catch (e) {}
		}).fail(function() {}
	).always(function() {});
}

function loadChildData(e){
	var parentUuid = e.uuid_1;
	$.get("/tag/showChildTagListWithoutPageOption/" + parentUuid, function(){}).done(
		function(data) {
			//test
			console.log(data);
			try {
				var loader = $('#childTagsTable');
				var body = loader.find('tbody');
				body.empty();
				var _d = $(data['aaData']);
				if (_d.length == 0) {
					body.append($('<tr><td colspan="3"><font color="red" size=4>该标签类下没有子标签! 请在标签管理页面添加</font></td></tr>'));
				} else {
					_d.each(function(i, e) {
						if (e.name) {
							var item = $('<tr><td><input type="checkbox" value="' + e.uuid + '" >'
										+ '</td><td>' + e.name
										+ '</td><td>' + e.modifyDate
										+ '</td></tr>');
							body.append(item);
						}
					})
				}
			} catch (e) {}
		}).fail(function() {}
	).always(function() {});
}