	(function() {

		(function() {
			var $ = qcVideo.get('$');
			$('#start').on(
					'click',
					function(e) {
						var secretId = $('#secret_id').val();
						if (!!secretId) {

							$('#result').show();
							$('#count').show();
							$('#pickfiles_area').show();
							$('#btnarea').show();
							$('#secret').hide();
							$('#error').show();

							accountDone('pickfiles', secretId, $(
									'input[name="transcode"][value="1"]').is(
									':checked'), $(
									'input[name="watermark"][value="1"]').is(
									':checked'));
						}
					});

		})();

		/**
		 *
		 * @param upBtnId 上传按钮ID
		 * @param secretId 云api secretId
		 * @param isTranscode 是否转码
		 * @param isWatermark 是否设置水印
		 */
		var accountDone = function(upBtnId, secretId, isTranscode, isWatermark) {

			var $ = qcVideo.get('$'), ErrorCode = qcVideo.get('ErrorCode'), Log = qcVideo
					.get('Log'), JSON = qcVideo.get('JSON'), util = qcVideo
					.get('util'), Code = qcVideo.get('Code');

			//您的secretKey
			var secret_key = $('#secret_key').val() || '';

			//@api 当前浏览器是否支持上传
			if (ErrorCode.UN_SUPPORT_BROWSE !== qcVideo.uploader
					.init(
							//1: 上传基础条件
							{
								web_upload_url : 'http://vod.qcloud.com/v2/index.php',
								secretId : secretId, // 云api secretId

								//云api secretKey : 选填参数 （secretKey不能暴露给外部用户，建议只在内部系统使用该参数）
								secretKey : secret_key,

								/*
								 @desc 获取签名的方法

								 //server端实现逻辑
								 // 1:首先 secretKey 做sha1 加密 argStr 得到结果 result
								 // 2:最后 将result做base64后返回
								 //附nodejs实例实现
								 // crypto.createHmac('sha1', '你的secretKey').update(argStr).digest().toString('base64');

								getSignature: function(argStr,cb){

								    $.ajax({
								        'dataType': 'jsonp',
								        'url': 'http://signature.qcloud.com/getSignature.php?args='+encodeURIComponent(argStr),
								        'success': function(d){
								            cb(d['result']);
								        }
								    });
								},
								 */
								upBtnId : upBtnId, //上传按钮ID（任意页面元素ID）
								isTranscode : isTranscode,//是否转码
								isWatermark : isWatermark
							//是否设置水印
							}
							//2: 回调
							,
							{

								/**
								 * 更新文件状态和进度
								 * @param args { id: 文件ID, size: 文件大小, name: 文件名称, status: 状态, percent: 进度 speed: 速度, errorCode: 错误码,serverFileId: 后端文件ID }
								 */
								onFileUpdate : function(args) {
									var $line = $('#' + args.id);
									if (!$line.get(0)) {
										$('#result')
												.append(
														'<div class="line" id="' + args.id + '"></div>');
										$line = $('#' + args.id);
									}

									var finalFileId = '';

									if (args.code == Code.UPLOAD_DONE) {
										finalFileId = '文件ID>>'
												+ args.serverFileId
									}

									$line
											.html(''
													+ '文件名：'
													+ args.name
													+ ' >> 大小：'
													+ util
															.getHStorage(args.size)
													+ ' >> 状态：'
													+ util
															.getFileStatusName(args.status)
													+ ''
													+ (args.percent ? ' >> 进度：'
															+ args.percent
															+ '%' : '')
													+ (args.speed ? ' >> 速度：'
															+ args.speed + ''
															: '')
													+ '<span data-act="del" class="delete">删除</span>'
													+ finalFileId);

								},

								/**
								 * 文件状态发生变化
								 * @param info  { done: 完成数量 , fail: 失败数量 , sha: 计算SHA或者等待计算SHA中的数量 , wait: 等待上传数量 , uploading: 上传中的数量 }
								 */
								onFileStatus : function(info) {
									$('#count').text(
											'各状态总数-->' + JSON.stringify(info));

								},

								/**
								 *  上传时错误文件过滤提示
								 * @param args {code:{-1: 文件类型异常,-2: 文件名异常} , message: 错误原因 ， solution: 解决方法}
								 */
								onFilterError : function(args) {
									var msg = 'message:'
											+ args.message
											+ (args.solution ? (';solution==' + args.solution)
													: '');
									$('#error').html(msg);
								}

							})) {

				//事件绑定
				(function() {

					$('#start_upload').on('click', function() {
						//@api 上传
						qcVideo.uploader.startUpload();
					});

					$('#stop_upload').on('click', function() {
						//@api 暂停上传
						qcVideo.uploader.stopUpload();
					});

					$('#re_upload').on('click', function() {
						//@api 恢复上传（错误文件重新）
						qcVideo.uploader.reUpload();
					});

					$('#result')

					.on('click', '[data-act="del"]', function(e) {
						var $line = $(this).parent();
						var fileId = $line.get(0).id;

						Log.debug('delete', fileId);

						$line.remove();
						//@api 删除文件
						qcVideo.uploader.deleteFile(fileId);
					});
				})();

			}

		};
	})();