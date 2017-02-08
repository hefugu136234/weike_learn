$(document).ready(function() {
	showActive([ 'holder_project', 'nav_banner_setting' ]);
	$('.layout-item').each(function(i, e) {
		var $e = $(e);
		var uuid = $e.data('id');
		var status = $e.data('status');
		initOpration($e, uuid, status);
		initStatus($e, status)
	})
});

function initStatus(container, status) {
	var cell = container.find('.item-status')
	cell.empty();
	var class_val = 'underline', val = '已下线'
	if (status == 1) {
		class_val = 'approved';
		val = "已上线"
	}
	cell.append('<span class="' + class_val + '">' + val + '<span>')
}

function initOpration(container, uuid, status) {
	var cell = container.find('.item-op');
	cell.empty();
	var status_op = $('<a href="#">' + (status == 1 ? '下线' : '上线') + '</a>')
	cell.append(status_op)
	var edit_op = $('<a href="/tv/layout/' + uuid + '/edit">编辑</a>')
	cell.append(edit_op)
	if (status != 1) {
		var del_op = $('<a href="#">删除</a>')
		cell.append(del_op)
		del_op.click(function(e) {
			e.preventDefault();
			var con = confirm('删除之后将不可恢复');
			if (!con) {
				return;
			}
			$.post('/tv/layout/' + uuid + '/del').always(function(data) {
				if (data.status == 'success') {
					container.remove();
				}
			})
		})
	}
	status_op.click(function(e) {
		e.preventDefault();
		$.post('/tv/layout/' + uuid + '/status', {
			"status" : status
		}).always(function(data) {
			if (data.status == 'success') {
				initStatus(container, data.layout_status)
				initOpration(container, data.uuid, data.layout_status)
			}
		})
	})

}