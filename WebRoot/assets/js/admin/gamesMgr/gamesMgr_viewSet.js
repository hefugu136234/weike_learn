$(document).ready(function() {
	showActive([ 'games-list-nav', 'games-mgr' ]);
	
	var pageEditor = UE.getEditor('page_script');
	var uuid = $('#lotteryUuid').val();
	
	pageEditor.addListener("ready", function() {
		// editor准备好之后才可以使用 
		pageEditor.setContent($('#description_hide').text());
	});
	
	
	$('#btn_submit').click(function(){
		
		var check_description = pageEditor.hasContents();
		if (!check_description) {
			alert("请填写内容");
			return ;
		}else{
			var pageInfo = pageEditor.getContent();
			console.log(pageInfo);
			$.post('/project/games/pageSet', {
				uuid : uuid,
				page : pageInfo
			}, function(data) {
				if (data['status'] == 'success') {
					window.location.href="/project/games/list/page";
				} else {
					if (!!data.message) {
						alert(data.message);
					} else {
						alert('状态修改失败');
					}
				}
			});
		}
	})
});
