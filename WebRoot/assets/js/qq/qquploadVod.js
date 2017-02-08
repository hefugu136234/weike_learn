var secretId = '';
var secretKey = '';
$(function() {
	/**
	 * 初始化加载secretId secretKey
	 */
	// var $ = qcVideo.get('$');
	var video_type=$("#videotype").val();
	if(video_type=='0'){
		$.getJSON('/project/qq/secret', function(orData) {
			secretId = orData.secretId;
			if (!!secretId) {
				accountDone('pickfiles', true, true);
			}
		});
	}
	
});

/**
 * 
 * @param upBtnId
 *            上传按钮ID
 * @param secretId
 *            云api secretId
 * @param isTranscode
 *            是否转码
 * @param isWatermark
 *            是否设置水印
 */
var accountDone = function(upBtnId, isTranscode, isWatermark) {
	var $ = qcVideo.get('$');
	ErrorCode = qcVideo.get('ErrorCode');
	Log = qcVideo.get('Log');
	JSON = qcVideo.get('JSON');
	util = qcVideo.get('util');
	Code = qcVideo.get('Code');
	if (ErrorCode.UN_SUPPORT_BROWSE !== qcVideo_Init(upBtnId, isTranscode,
			isWatermark)) {
		$('#start_upload').on('click', function() {
			// @api 上传
			qcVideo.uploader.startUpload();
			$('#start_upload').html('正在上传...')
			
		});

		$('#stop_upload').on('click', function() {
			// @api 暂停上传
			qcVideo.uploader.stopUpload();
		});

		$('#re_upload').on('click', function() {
			// @api 恢复上传（错误文件重新）
			qcVideo.uploader.reUpload();
		});

		$('#result').on('click', '[data-act="del"]', function(e) {
			var $line = $(this).parent();
			var fileId = $line.get(0).id;

			Log.debug('delete', fileId);

			$line.remove();
			// @api 删除文件
			qcVideo.uploader.deleteFile(fileId);
			$('#fileId').val('')
			$('#start_upload').html('开始上传')

		});
	} else {
		alert("浏览器不支持上传视频");
	}
};

/**
 * 视频上传的初始设置 qcVideo.uploader.init(var1,var2) var1=上传基础条件 var2=上传的回调
 */
var qcVideo_Init = function(upBtnId, isTranscode, isWatermark) {
	qcVideo.uploader.init({
		/**
		 * 1: 上传基础条件
		 */
		// 上传的url，必填
		web_upload_url : 'http://vod.qcloud.com/v2/index.php',
		// 必填
		secretId : secretId,
		// 选填
		// secretKey：secret_key,
		// 必填=上传按钮的id
		upBtnId : upBtnId,
		// 转码
		isTranscode : isTranscode,
		// 水印
		isWatermark : isWatermark,
		// 签名
		getSignature : function(argStr, cb) {
			console.log("isWatermark")
			$.ajax({
				'dataType' : 'text',
				'url' : '/project/qq/signature?args=' + encodeURIComponent(argStr),
				'success' : function(data) {
					console.log(data);
					cb(data);
				}
			});
		}
	}, {
		// 此参数为回调函数 3个参数 onFileUpdate=更新文件状态和进度
		// onFileStatus=文件状态发生变化 onFilterError=上传时错误文件过滤提示
		/**
		 * 更新文件状态和进度 id: 文件ID, size: 文件大小, name: 文件名称, status: 状态, percent: 进度
		 * speed: 速度, errorCode: 错误码,serverFileId: 后端文件ID
		 */
		onFileUpdate : function(args) {
			if(args.status == Code.BATCH_FILE_SET){
				$('#start_upload').show();
			}
			$('#file_select_status').show();
			var $line = $('#' + args.id);
			if (!$line.get(0)) {
				// 一次只能上传一个文件------------
				$('#result').find('.line').each(function(i, e) {
					qcVideo.uploader.deleteFile(e.id)
				})
				$('#result').empty();
				//---------------
				$('#result').append(
						'<div class="line" id="' + args.id + '"></div>');
				$line = $('#' + args.id);
				$('#videoName').val(args.name)
			}
			var finalFileId = '';
			if (args.code == Code.UPLOAD_DONE) {
				finalFileId = '文件ID>>' + args.serverFileId
				$('#fileId').val(args.serverFileId)
				$('#start_upload').html('开始上传');
				$('#start_upload').hide();
			}
			$line.html('' + '文件名：' + args.name + ' >> 大小：'
					+ util.getHStorage(args.size) + ' >> 状态：'
					+ util.getFileStatusName(args.status) + ''
					+ (args.percent ? ' >> 进度：' + args.percent + '%' : '')
					+ (args.speed ? ' >> 速度：' + args.speed + '' : '')
					+ '<span data-act="del" class="delete">删除</span>'
					+ finalFileId);
		},
		/**
		 * 文件状态发生变化 done: 完成数量 , fail: 失败数量 , sha: 计算SHA或者等待计算SHA中的数量 , wait:
		 * 待上传数量 , uploading: 上传中的数量
		 */
		onFileStatus : function(info) {
			$('#count').text('各状态总数-->' + JSON.stringify(info));
		},
		/**
		 * 上传时错误文件过滤提示 code:{-1: 文件类型异常,-2: 文件名异常} , message: 错误原因 ， solution:
		 * 解决方法}
		 */
		onFilterError : function(args) {
			var msg = 'message:' + args.message
					+ (args.solution ? (';solution==' + args.solution) : '');
			$('#error').html(msg);
		}

	});
};
