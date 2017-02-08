$(document)
		.ready(
				function() {
					showActive([ 'video_mrg_nav', 'assets-mgr' ]);
					var item_new = $('#vote_seed').find('.vote-item-clean');
					var container = $('#vote_container .list-group')
					var resource_uuid = $('#res_uuid').val();
					var vote_data = $('#votes_data').val();
					$('#new_vote_controller').click(function() {
						vote_add_subject_init(empty_subject);
					})
					if (votes.length > 0) {
						$.each(votes, function(i, e) {
							vote_add_subject_init(new Subject(e.uuid, e.title,
									e.type, e.options));
						})
					}
					function vote_add_subject_init(subject) {
						var item_container = $('<li data-id="' + subject.uuid
								+ '" class="list-group-item"></li>');
						var item = item_new.clone();
						item_container.append(item);
						container.append(item_container)
						item.find('.vote-title').val(subject.title)
						var vote_options_container = item_container
								.find('.vote-options-container')
						if (subject.options.length == 0) {
							vote_add_option_init(vote_options_container,
									empty_option)
						} else {
							$.each(subject.options, function(i, e) {
								vote_add_option_init(vote_options_container,
										new Option(e.uuid, e.option, e.count))
							})
						}

						// 删除投票
						vote_del_init(item_container);
						item_container.find('.save-vote-btn').click(function() {
							save_vote(item_container);
						})
						var selector = item_container.find('.vote-type');
						selector.val(subject.type)
						if (!!subject.uuid.trim()) {
							selector.attr('disabled', 'disabled');
						}
					}

					function vote_del_init(vote) {
						vote.find('.vote-del').click(function() {
							var con = confirm('删除之后将不可恢复');
							if (!con) {
								return;
							}
							var uuid = vote.data('id');
							if (!uuid.trim()) {
								vote.remove();
							} else {
								$.post('/project/resource/vote/del', {
									uuid : uuid
								}, function() {

								}).always(function(data) {
									if (data.status == 'success') {
										vote.remove();
									} else {
										alert(data.status)
									}
								})
							}
						})
					}

					function vote_add_option_init(opntion_container, option) {
						var item = $('<div data-id="'
								+ option.uuid
								+ '" class="form-group option-item-container"><label class="col-sm-2 control-label"></label><div class="col-sm-4"><input type="text" value="'
								+ option.option
								+ '" class="form-control"  placeholder="请输入投票选项"></div></div>')
						var _del = $('<a style="margin-right: 10px;" class="option-del-btn">删除</a>')
						var _add = $('<a class="option-add-btn">增加</a>')
						if (!editable && option.result >= 0) {
							item.append($('<span class="option-item-result">'
									+ option.result + '票</span>'));
						}

						_add.click(function(e) {
							e.preventDefault();
							vote_add_option_init(opntion_container,
									empty_option);
						})
						_del.click(function(e) {
							e.preventDefault();

							if (!option.uuid.trim()) {
								vote_option_remove(opntion_container, item)
								return;
							}
							var con = confirm('确认删除？')
							if (!con)
								return;
							$.post('/project/resource/vote/option/del', {
								uuid : option.uuid
							}, function() {

							}).always(
									function(data) {
										if (data.status == 'success') {
											vote_option_remove(
													opntion_container, item);
										} else {
											if (data.message) {
												alert(data.message);
											}
										}
									})

						})
						opntion_container.find('.option-add-btn').hide();
						item.append(_del)
						item.append(_add);
						opntion_container.append(item)
						var del_all = opntion_container.find('.option-del-btn')
						if (del_all.length == 1) {
							del_all.hide();
						} else {
							del_all.show();
						}
					}

					function vote_option_remove(opntion_container, item) {
						item.remove()
						opntion_container.find('.option-add-btn').last().show()
						var del_all = opntion_container.find('.option-del-btn');
						if (del_all.length == 1) {
							del_all.hide();
						}
					}
					function save_vote(item) {
						var options = [];
						var title = item.find('.vote-title').val();
						var type = item.find('.vote-type').val();
						var uuid = item.data('id');
						var item_empty = false;
						item.find('.option-item-container').each(
								function(i, e) {
									var $e = $(e);
									var value = $e.find('input[type=text]')
											.val()
									if (!value.trim()) {
										item_empty = true;
									}
									var id = $e.data("id");
									options.push({
										"uuid" : id,
										"option" : value
									})
								})
						var data = {
							"uuid" : uuid,
							"title" : title,
							"type" : parseInt(type),
							"options" : options
						}
						if (!title.trim()) {
							alert("标题不能为空");
							return;
						}
						if (item_empty) {
							alert("选项值不能为空");
							return;
						}

						if (!uuid.trim()) {
							var c = confirm("提交之后，题型将不可更改!");
							if (!c) {
								return;
							}
						}
						$.post('/project/resource/vote/save', {
							res_uuid : resource_uuid,
							vote_data : JSON.stringify(data)
						}, function() {

						}).always(function(data) {
							if (data.status == 'success') {
								refresh_subject(item, data)
								alert('保存成功')
							} else {
								if (data.message) {
									alert(data.message);
								} else {
									alert(data.status);
								}
							}
						})
					}

					function refresh_subject(item, subject) {
						item.find('.vote-title').val(subject.title);
						var selector = item.find('.vote-type');
						selector.val(subject.type)
						if (!!subject.uuid.trim()) {
							selector.attr('disabled', 'disabled');
						}
						item.data('id', subject.uuid);
						// 删除原来的options
						item.find('.option-item-container').remove();
						var options_container = item
								.find('.vote-options-container')
						$.each(subject.options, function(i, e) {
							vote_add_option_init(options_container, e);
						})
					}
					if (!editable) {
						disableEdit();
					}
				})
var empty_subject = new Subject('', '', 1, [])
var empty_option = new Option('', '', -1);
function Option(uuid, option, result) {
	this.uuid = uuid;
	this.option = option;
	this.result = result;
}

function Subject(uuid, title, type, options) {
	this.uuid = uuid;
	this.title = title;
	this.type = type;
	this.options = options;
}

function disableEdit() {
	var effect_area = $('#vote_container')
	effect_area.find('input').attr('readonly', true);
	effect_area.find('button').remove();
	effect_area.find('.vote-del').remove();
	effect_area.find('.option-del-btn').remove();
	effect_area.find('.option-add-btn').remove();
	$('#edit_forbidden_tips').show();
}
