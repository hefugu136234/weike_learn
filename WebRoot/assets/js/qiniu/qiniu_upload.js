$(function() {
	var videotype_val = $('#videotype').val();
	var $qiniu_file=$('#qiniu_file');
	var uploader;
	var $qiniu_status = $('#qiniu_status');
	var $qiniu_start = $('#qiniu_start');
	var $qiniu_result=$('#qiniu_result');
	var $qiuniu_key=$('#qiuniu_key');
	$qiniu_start.click(function() {
		uploader.start();
		$qiniu_start.html('正在上传...');
	});
	var $qiniu_stop = $('#qiniu_stop');
	$qiniu_stop.click(function(){
		 uploader.stop();
		 $qiniu_start.html('开始上传');
		 $qiniu_start.prop('disabled',false);
		 $qiniu_file.prop('disabled',false);
		 $qiniu_start.show();
		 $qiniu_stop.hide();
	});

	if (videotype_val == '1') {
		uploader = Qiniu.uploader({
			runtimes : 'html5,flash,html4',// 上传模式,依次退化
			browse_button : "qiniu_file",// 上传选择的点选按钮，**必需**
			container : "qiniu_container",// 上传区域DOM ID，默认是browser_button的父元素
			dragdrop : false,// 开启可拖曳上传
			max_file_size : '4096mb',// 最大文件体积限制
			flash_swf_url : '/assets/js/qiniu/plupload/js/Moxie.swf',// 引入flash,相对路径
			chunk_size : '4mb',// 分块上传时，每片的体积
			multi_selection : false,// 设置一次只能选择一个文件
			domain : 'http://vrassets.lankr.net',// bucket 域名，下载资源时用到，**必需**
			uptoken_url : '/video/qiniu/uploader/signature',// Ajax请求upToken的Url，**强烈建议设置**（服务端提供）
			//uptoken:'asdasdasdasd',
			save_key:true,
			get_new_uptoken : false,// 设置上传文件的时候是否每次都重新获取新的token
			auto_start : false, // 选择文件后自动上传，若关闭需要自己绑定事件触发上传
			log_level : 5,
			init : {
				'FilesAdded' : function(up, files) {
					// 文件添加进队列后,处理相关的事情
					//console.log('FilesAdded');
					preFile(up,files);
				},
				'BeforeUpload' : function(up, file) {
					// 每个文件上传前,处理相关的事情
					//console.log('BeforeUpload');
					beforeLoad(up, file);
				},
				'UploadProgress' : function(up, file) {
					// 每个文件上传时,处理相关的事情
					uploadprogress(up, file);
				},
				'onCancel':function(id,fileName){
					console.log('onCancel: '+id+' '+fileName);
				},
				'UploadComplete' : function() {
					// 队列文件处理完毕后,处理相关的事情
				},
				'FileUploaded' : function(up, file, info) {
					// 每个文件上传成功后,处理相关的事情
					// 其中 info 是文件上传成功后，服务端返回的json，形式如
					fileuploaded(up, file, info);
				},
				'Error' : function(up, err, errTip) {
					// 上传出错时,处理相关的事情
					errorMsg(up, err, errTip);
				}
			}
		});
	}
	
	function preFile(up,files){
		//队里有数据先清空
		var file_currnet=files[0];
		console.log(file_currnet);
		var file_quenu=up.files;
		if(file_quenu.length>1){
			if(file_quenu[0].id!=file_currnet.id){
				up.removeFile(file_quenu[0]);
			}
		}
		console.log(up);
		$qiniu_result.empty();
		$qiniu_result.append(
				'<div class="line" id="' + file_currnet.id + '"></div>');
		var $line=$('#' + file_currnet.id);
		var filename=file_currnet.name;
		var fileSize = plupload.formatSize(file_currnet.size).toUpperCase();
		$line.html('' + '文件名：' + filename + ' >> 大小：'
				+ fileSize + ' >> 状态：等待上传');
		$qiniu_start.prop('disabled',false);
		$qiniu_status.show();
		$qiniu_start.show();
		$qiniu_stop.hide();
		$('#videoName').val(filename)
	}
	
	function beforeLoad(up,file){
		var $line=$('#' + file.id);
		var filename=file.name;
		var fileSize = plupload.formatSize(file.size).toUpperCase();
		$line.html('' + '文件名：' + filename + ' >> 大小：'
				+ fileSize + ' >> 状态：正在上传...');
		//$qiniu_start.hide();
		$qiniu_start.prop('disabled',true);
		$qiniu_file.prop('disabled',true);
		$qiniu_stop.show();
		
	}
	
	function uploadprogress(up, file){
		var $line=$('#' + file.id);
		var filename=file.name;
		var fileSize = plupload.formatSize(file.size).toUpperCase();
		var uploaded = file.loaded;
		var uploaded_size = plupload.formatSize(uploaded).toUpperCase();
		var formatSpeed = plupload.formatSize(file.speed).toUpperCase();
		$line.html('' + '文件名：' + filename + ' >> 大小：'
				+ fileSize + ' >> 状态：正在上传...'
				+' '
				+'>>已上传：'+uploaded_size+' '+'>>上传进度：'+file.percent+'%'
				+' '
				+'>>上传速度：'+formatSpeed+'/s');
	}
	
	function fileuploaded(up, file, info){
		var $line=$('#' + file.id);
		var filename=file.name;
		var fileSize = plupload.formatSize(file.size).toUpperCase();
		var uploaded = file.loaded;
		var uploaded_size = plupload.formatSize(uploaded).toUpperCase();
		$line.html('' + '文件名：' + filename + ' >> 大小：'
				+ fileSize + ' >> 状态：上传完成'
				+' '
				+'>>已上传：'+uploaded_size+' '+'>>上传进度：'+file.percent+'%');
		$qiniu_start.hide();
		$qiniu_stop.hide();
		$qiniu_file.prop('disabled',false);
		var qiniu_key=JSON.parse(info);
		$qiuniu_key.val(qiniu_key.key);
		console.log('info:'+info);
	}
	
	function errorMsg(up, err, errTip){
		console.log('Error');
		console.log(err);
		console.log(errTip);
	}

});