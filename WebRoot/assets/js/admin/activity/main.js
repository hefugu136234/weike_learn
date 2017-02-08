$(document).ready(function() {
	activeStub('activity-list-nav')
})

function getNodeById(id) {
	var all = $('#tree').jstree();
	return all._model.data[id];
}