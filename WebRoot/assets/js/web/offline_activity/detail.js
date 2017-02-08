$(function() {
	var offline_uuid = $('#comment_body_uuid').val();
	$('#go_book').click(function(e) {
		var $this = $(this);
		$.post('/f/web/offline/activity/book', {
			uuid : offline_uuid
		}, function(data) {
			if (isSuccess(data)) {
				$this.hide();
				alert('报名申请已提交');
			} else {
				if (data.code == 511) {
					alert('请先登录');
					return false;
				} else {
					alert(data.message);
				}
			}
		});
	});

	var tbody_ul = $('#book_detail_table').find('tbody');

	$('#book_detail').click(function(e) {
		// 展示报名详情
		$('#offline_apply_detail').modal('show');
	});
	var $table_pagination_ul = $('#table_pagination_ul');


	var buildUl = function(itemList) {
		tbody_ul.empty();
		if (isItemList(itemList)) {
			var li_item = '';
			$.each(itemList, function(index, item) {
				var item_div = buildItem(item);
				li_item = li_item + item_div;
			});
			tbody_ul.append(li_item);
		}
	};

	var buildItem = function(item) {
		var name = item.name;
		var bookTime = item.bookTime;
		var checkStatus = item.checkStatus;
		var checkStatusVal = '';
		if (checkStatus == 0) {
			checkStatusVal = '未审核';
		} else if (checkStatus == 1) {
			checkStatusVal = '已通过';
		} else if (checkStatus == 2) {
			checkStatusVal = '未通过';
		}
		var tr = '<tr>' + '<td>' + name + '</td>' + '<td>' + bookTime + '</td>'
				+ '<td class="status">' + checkStatusVal + '</td>';
		return tr;
	};

	var tableRequest = function(obj) {
		// 请求数据
		$.getJSON('/f/web/offline/book/list', {
			'size' : obj.batchSize,
			'currentPage' : obj.currentPage,
			'uuid' : offline_uuid
		}, function(data) {
			if (isSuccess(data)) {
				var itemList = data.items;
				obj.total = data.itemTotalSize;
				$('#booked_person_num').html(data.itemTotalSize);
				buildUl(itemList);
				pageControllerInit(obj, function(){
					return $table_pagination_ul;
				}, tableRequest);
			}
		});
	};
	
	tableRequest(pageObj(0,10,1));
});