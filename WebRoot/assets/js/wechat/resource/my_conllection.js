//新js原 无
var droploadList;
$(function() {
	var emptyArea = $('.empty-tips-area');
	if (!isItemList(emptyArea)) {
		// 初始化下拉
		droploadList = dataDropload($('#dropload_resource_div'),
				$('#dropload_resource_ul'),
				'/api/webchat/resource/my/collect/list', commonDropUpdateTime,
				commonDropUl, '');
	}
});

/**
 * 下拉列表 获取公共更新时间方法
 * 注：此处通的为 updateTime的更新时间
 * @param data
 * @returns
 */
function commonDropUpdateTime(data) {
	var list = data.items;
	if (isItemList(list)) {
		droploadList.listStartTime = list[list.length - 1].updateTime;
	}
	return list;
}
