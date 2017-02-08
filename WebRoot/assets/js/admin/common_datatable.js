/**
 * @author kalean.xiang
 * @version 1.0
 * @param unqkey
 *            always uuid
 */

function datatable(element, url, columns, unqkey) {
	if (typeof (columns) == 'undefined' || columns.length <= 0) {
		throw new Error('columns required');
	}
	var dt = new DT();
	var aoColumns = [];
	$.each(columns, function(i, col) {
		aoColumns.push({
			"defaultContent" : col.defaultContent,
			"mData" : col.mData,
			"orderable" : col.orderable,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol,oSettings) {				
				dt.put(oData, col, $(nTd), iRow)
				if (typeof (col.reDraw) == 'function') {
					var rows = 0;
					if(oSettings.json){
						rows = oSettings.json.iTotalDisplayRecords
					}
					col.reDraw($(nTd), sData, oData, iRow, iCol,rows);
				}
			}
		})
	})
	dt.table = element.dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
			dt.rows = Math.min(Math.abs(oSettings._iRecordsDisplay
					- oSettings._iDisplayStart), oSettings._iDisplayLength)
		},
		"fnPreDrawCallback" : function(oSettings) {

		},
		"fnInitComplete" : function(oSettings, json) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : url,
		"aoColumns" : aoColumns
	});
	
	return dt;
}

function DT(table, uq) {
	this.uniqkey = uq;
	if (isBlank(this.uniqkey)) {
		this.uniqkey = 'uuid';
	}
	this.table = table;
	var cc = new Map();
	this.put = function(data, column, td, iRow) {
		var k = data[this.uniqkey];
		if (!cc.get(iRow)) {
			cc.set(iRow, {})
		}
		if (k != cc.get(iRow)['unique']) {
			cc.set(iRow, {})
			cc.get(iRow)['columns'] = [];
			cc.get(iRow)['unique'] = k;
		}
		cc.get(iRow)['columns'].push({
			fn : column,
			ui : td
		})
	}
	this.refreshItem = function(item) {
		var key = item[this.uniqkey]
		if (key == 'undefined') {
			alert('返回值需要包含主键 ' + this.uniqkey);
			return;
		}
		var obj = {};
		for (var i = 0; i < this.rows; i++) {
			if (cc.get(i)['unique'] == key) {
				obj = cc.get(i);
				break;
			}
		}
		var cols = obj['columns']
		$.each(cols, function(i, e) {
			
		//	var d = item[e.fn.mData]
			//see common.js
			var d = traceValue(item,e.fn.mData,e.fn.defaultContent)
//			if (isBlank(d)) {
//				d = e.fn.defaultContent;
//			}
			if (typeof (e.fn.reDraw) == 'function') {
				e.fn.reDraw(e.ui, d, item);
			} else {
				e.ui.empty();
				e.ui.append(d)
			}
		})
	}
//	this.cache = function() {
//		return cc;
//	}
}

function Column(trace, fnReDraw, defValue, orderable) {
	this.defaultContent = isBlank(defValue) ? "" : defValue;
	this.mData = trace;
	this.reDraw = fnReDraw;
	this.orderable = orderable ? orderable : false;	
}
