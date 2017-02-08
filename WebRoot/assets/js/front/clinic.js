// Custom scripts
$(document)
		.ready(
				function() {
					$("#clinic_bar").addClass("active");
					$
							.getJSON(
									"/f/clinic/info",
									function(data) {
										$('#clinicName').text(data.clinicName);
										$("#clinicDesc").text(
												data.clinicDescription);
										var mediasList = data.mediasList;
										var parentUl = $('#clinic_image_show');
										parentUl.empty();
										$
												.each(
														mediasList,
														function(index, item) {
															var uuid = item.uuid;
															var taskId = item.taskId;
															var type = item.type;
															var description = item.description;
															var originHost = item.originHost;
															var uploadDate = item.uploadDate;
															if (type == 'image') {
																var bigUrl="http://cloud.lankr.cn/api/image/" +taskId + "?m/2/h/500/f/jpg";
																var childli = $('<tr data-id='
																		+ uuid
																		+ '><td width="20%"><a href="'+bigUrl
																		+'" title="诊所图片" data-gallery=""><img data-id='
																		+ taskId
																		+ ' class="image-thumb" src="http://cloud.lankr.cn/api/image/'
																		+ taskId
																		+ '?m/2/h/80"alt="/assets/img/front/clinic_image.png"/></a></td><td width="50%" class="des">'
																		+ description
																		+ '</td><td width="15%">'
																		+ uploadDate
																		+ '</td><td width="15%"><a class="edit" href="#">编辑</a>|<a class="del" href="#">删除</a></td></tr>');
																parentUl
																		.append(childli);
																childli
																		.find(
																				'.edit')
																		.click(
																				function(
																						event) {
																					event
																							.preventDefault();
																					$(
																							'#image-preview')
																							.attr(
																									'src',
																									childli
																											.find(
																													'.image-thumb')
																											.attr(
																													'src'));
																					$(
																							'#taskId')
																							.val(
																									childli
																											.find(
																													'.image-thumb')
																											.data(
																													"id"));
																					$(
																							'#image_description')
																							.val(
																									childli
																											.find(
																													'.des')
																											.text());
																					$(
																							'#imageEditor')
																							.modal(
																									'show')
																					// 加载上传图片对话框
																					$(
																							'#uploadImage')
																							.uploadify(
																									{
																										'swf' : '/assets/js/uploadify/uploadify.swf',
																										'uploader' : 'http://cloud.lankr.cn/api/image/upload',
																										'formData' : {
																											'appKey' : 'ff7a9de914595ec790dbf5b32ab46e9a'
																										},
																										'fileObjName' : 'file',
																										'fileTypeExts' : '*.bmp;*.jpg;*.jpeg;*.png',
																										'method' : 'post',
																										'fileSizeLimit' : 10000,
																										'auto' : true,
																										'cancelImg' : '/assets/js/uploadify/uploadify-cancel.png',
																										'onUploadSuccess' : function(
																												file,
																												data,
																												response) {
																											$(
																													'#image-preview')
																													.show();
																											var json_data = JSON
																													.parse(data);
																											$(
																													'#image-preview')
																													.attr(
																															'src',
																															json_data.url);
																											$(
																													'#taskId')
																													.val(
																															json_data.taskId);
																										}
																									});
																					// 提交更新
																					$(
																							'#finishImage')
																							.unbind(
																									'click')
																					$(
																							'#finishImage')
																							.click(
																									function() {
																										$
																												.post(
																														"/f/clinic/media/update",
																														{
																															'uuid' : uuid,
																															'taskId' : $(
																																	'#taskId')
																																	.val(),
																															'description' : $(
																																	'#image_description')
																																	.val()
																														},
																														function() {
																														})
																												.done(
																														function() {
																														})
																												.fail(
																														function() {
																															alert("更新失败");
																														})
																												.always(
																														function(
																																data) {
																															if (data.status == 'success') {
																																$(
																																		'#imageEditor')
																																		.modal(
																																				'hide')
																																renderMediaItem(
																																		data,
																																		childli)
																															} else {
																																if (data.message) {
																																	alert(data.message)
																																}
																															}
																														})
																									})
																				})
																childli
																		.find(
																				'.del')
																		.click(
																				function(
																						event) {
																					event
																							.preventDefault();
																					var con = confirm("确定删除?");
																					if (!con) {
																						return false;
																					}
																					$
																							.post(
																									'/f/clinic/media/del',
																									{
																										'uuid' : uuid
																									},
																									function() {
																									})
																							.always(
																									function(
																											data) {
																										if (data.status == 'success') {
																											childli
																													.remove();
																										} else {
																											if (data.message) {
																												alert(data.message)
																											}
																										}
																									})
																				})
															} else if (type == 'video') {
																// unimplements
															}

														});

										// var swiper = new
										// Swiper('.swiper-container', {
										// nextButton : '.swiper-button-next',
										// prevButton : '.swiper-button-prev',
										// slidesPerView : 5,
										// paginationClickable : true,
										// spaceBetween : 8
										// });
										$('.clinicGallery')
												.find('li')
												.hover(
														function() {
															$(this)
																	.find(
																			'.desc')
																	.stop()
																	.animate(
																			{
																				bottom : '0'
																			});
														},
														function() {
															$(this)
																	.find(
																			'.desc')
																	.stop()
																	.animate(
																			{
																				bottom : '-24px'
																			});
														});

									});

					$('#imageEditor').on('hidden.bs.modal', function() {
						$('#image-preview').attr('src', '');
						$('#taskId').val('');
						$('#image_description').val('');
					})

				});

function renderMediaItem(data, $ui) {
	console.log(data)
	if (data.isActive == 0) {
		$ui.remove();
		return;
	}
	$ui.find('.image-thumb').attr('src',
			data.originHost + "/api/image/" + data.taskId + '?m/2/h/150')
	$ui.find('.des').html(data.description)
	$ui.find('.image-thumb').data('id', data.taskId)
}