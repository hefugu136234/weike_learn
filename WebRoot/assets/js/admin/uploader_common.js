var pattern = new RegExp("^(http).*");
function Part(element, type, initcb, finishcb) {
	this.element = element;
	this.type = type
	this.fn = function(d) {
		var data = JSON.parse(d)
		var src = qiniu_cdn_host + '/' + data.key;
		// 创建预览的视图
		this.renderPreview(src);
		if (finishcb != undefined) {
			finishcb(src);
		}
	}
	this.renderPreview = function(image) {
		var container = $('#' + this.element.attr('id')).parent();
		var pre = container.find('.pre-view');
		if (pre.length == 0) {
			pre = $('<img class="pre-view" alt="" data-type="' + this.type
					+ '" src=""/>');
			container.append(pre);
		}
		pre = $(pre);
		if (pattern.test(image)) {
			pre.show()
			pre.attr('src', image)
		} else {
			pre.hide();
		}
		this.url = image;
	}
	this.getShowName = function() {
		var v = element.attr("value")
		if (!!v) {
			return v;
		} else {
			return "选择文件";
		}
	}
	this.init = function() {
		this.element.attr("id", "uploader_component_" + this.type)
		if (initcb != undefined) {
			initcb(this);
		}
		return this;
	}
}

function uploaderInit(part) {
	part.element.hide();
	$.get('/admin/qiniu/uploader/sign?timestamp=' + new Date().getTime())
			.always(function(data) {
				if (data.status == 'success') {
					imageUploaderComponent(part, data.signature)
				} else {
					alert(data.status);
				}
			})
}

function imageUploaderComponent(part, token) {
	part.element.show();
	part.element.uploadify({
		'swf' : '/assets/js/uploadify/uploadify.swf',
		'uploader' : 'http://upload.qiniu.com/',
		'formData' : {
			'token' : token
		},
		'fileObjName' : 'file',
		'fileTypeExts' : '*.bmp;*.jpg;*.jpeg;*.png',
		'method' : 'post',
		'fileSizeLimit' : '10000KB',
		'buttonText' : part.getShowName(),
		'auto' : true,
		'cancelImg' : '/assets/js/uploadify/uploadify-cancel.png',
		'onUploadSuccess' : function(file, data, response) {
			//test
			/*console.log(file);
			console.log(data);
			console.log(response);*/
			//console.log(data);
			part.fn(data)
		},
		'onUploadError' : function(file, errorCode, errorMsg, errorString) {
			alert('The file ' + file.name + ' could not be uploaded: '
					+ errorString);
		}
	});
}
