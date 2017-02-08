$(document).ready(
		function() {
			var item_new = $('#games_seed').find('.vote-item-clean');
			var container = $('#games_container .list-group');
			var addButton = $('#new_games_controller');

			// 回显奖品信息
			if (awards.length > 0) {
				$.each(awards, function(i, e) {
					games_add_award_init(new Award(e.uuid, e.name, e.number,
							e.maxWinTimes, e.conditional));
				})
			}

			// 添加商品
			addButton.click(function() {
				games_add_award_init(emptyAward);
			})

			function games_add_award_init(award) {
				var item_container = $('<li data-id="' + award.uuid
						+ '" class="list-group-item"></li>');
				var item = item_new.clone();

				// 奖品信息回填
				item.find('input[name=awardName]').val(award.awardName);
				item.find('input[name=awardNumber]').val(award.awardNumber);
				item.find('input[name=awardMaxWinTimes]').val(
						award.awardMaxWinTimes);
				item.find('input[name=awardConditional]').val(
						award.awardConditional);

				item_container.append(item);
				container.append(item_container);

				// 删除奖品
				award_del_init(item_container);
			}

			function award_del_init(games) {
				games.find('.vote-del').click(function() {
					var con = confirm('是否确认删除该奖品');
					if (!con) {
						return;
					}
					var uuid = games.data('id');
					// 如果uuid为空，为当前新增，还未保存到DB,可以直接删除
					if (!uuid.trim()) {
						games.remove();
						// uuid不为空，需要从DB中将记录删除
					} else {
						$.post('/project/games/removeAward', {
							uuid : uuid
						}, function() {

						}).always(function(data) {
							if (data.status == 'success') {
								games.remove();
							} else {
								alert(data.status)
							}
						})
					}
				})
			}
		})

function Award(uuid, awardName, awardNumber, awardMaxWinTimes, awardConditional) {
	this.uuid = uuid;
	this.awardName = awardName;
	this.awardNumber = awardNumber;
	this.awardMaxWinTimes = awardMaxWinTimes;
	this.awardConditional = awardConditional
}

var emptyAward = new Award('', '', '', '', '');
