$(document).ready(function() {
	$('input[type="file"]').each(function(i, e) {
		var $e = $(e)
		uploaderInit(new Part($e, $e.data("type"), function(part) {
			$.each(getMedias(), function(i, e) {
				if (part.type == e.type) {
					part.renderPreview(e.url)
				}
			})
		}).init())
	})

})

function getMedias() {
	return medias;
}
