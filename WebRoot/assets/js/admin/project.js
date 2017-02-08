// Custom scripts
$(document).ready(function() {
	showActive([ 'project_create_nav', 'project_mgr' ]);
	var loader = $('#user_selector');
	var user_value = $('#user_value');
	$('#user_select_close').click(function() {
		loader.hide();
	});
	var timer = null;
	user_value.bind('input', function() {
		if (timer != null) {
			clearTimeout(timer)
		}
		timer = setTimeout('fetchUserInfo("' + $(this).val() + '")', 400);
	});

	user_value.focus(function() {
		if (loader.is(':hidden')) {
			loader.show();
			fetchUserInfo(user_value.val());
		}
	});

	// $('#new_project_form').validate({
	// rules : {
	// project_name : {
	// required : true,
	// minlength : 2
	// },
	// username : {
	// required : true
	// }
	// }
	// })

});

var last_request;
var request_interval = 200;

function fetchUserInfo(query) {
	return $.get("/admin/user/fetch", {
		query : query
	}, function() {

	}).done(
			function(data) {
				try {
					var loader = $('#user_loader_table');
					var body = loader.find('tbody');
					body.empty();
					var _d = $(data)
					if (_d.length == 0) {
						body.append($('<tr><td colspan="2">未查到用户</td></tr>'));
					} else {
						_d.each(function(i, e) {
							if (e.username) {
								var item = $('<tr><td>' + e.username
										+ '</td><td>' + e.nickname
										+ '</td></tr>');
								body.append(item);
								item.click(function() {
									$('#user_value').val(e.username);
									$('#user_selector').hide();
								})
							}
						})
					}
				} catch (e) {

				}
			}).fail(function() {
	}).always(function() {
	});

}