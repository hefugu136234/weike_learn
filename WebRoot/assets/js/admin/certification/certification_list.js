$.fn.dataTableExt.oApi.fnReloadAjax = function ( oSettings, sNewSource, fnCallback, bStandingRedraw ){
    if ( sNewSource != undefined && sNewSource != null ) {
        oSettings.sAjaxSource = sNewSource;
    }
    // Server-side processing should just call fnDraw
    if ( oSettings.oFeatures.bServerSide ) {
        this.fnDraw();
        return;
    }
    this.oApi._fnProcessingDisplay( oSettings, true );
    var that = this;
    var iStart = oSettings._iDisplayStart;
    var aData = [];
    this.oApi._fnServerParams( oSettings, aData );
    oSettings.fnServerData.call( oSettings.oInstance, oSettings.sAjaxSource, aData, function(json) {
        /* Clear the old information from the table */
        that.oApi._fnClearTable( oSettings );
        /* Got the data - add it to the table */
        var aData =  (oSettings.sAjaxDataProp !== "") ?
            that.oApi._fnGetObjectDataFn( oSettings.sAjaxDataProp )( json ) : json;
        for ( var i=0 ; i<aData.length ; i++ ){
            that.oApi._fnAddData( oSettings, aData[i] );
        }
        oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
        that.fnDraw();
        if ( bStandingRedraw === true ){
            oSettings._iDisplayStart = iStart;
            that.oApi._fnCalculateEnd( oSettings );
            that.fnDraw( false );
        }
        that.oApi._fnProcessingDisplay( oSettings, false );
        /* Callback user function - for event handlers etc */
        if ( typeof fnCallback == 'function' && fnCallback !== null )
        {
            fnCallback( oSettings );
        }
    }, oSettings );
};

var oTable;

$(document).ready(function() {
	showActive([ 'holder_project', 'certification_mgr_nav' ]);
	oTable = $('#certification_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) { },
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/certification/datatable",
		"aoColumns" : [ {
			"mData" : "imageUrl",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				imageShow(nTd, sData, '身份证预览');
			}
		}, {
			"mData" : "realName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				//console.log(oData)
				renderUserCell(nTd, sData,oData['userUuid']);
			}
		}, {
			"mData" : "userName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				//console.log(oData)
				renderUserCell(nTd, sData,oData['userUuid']);
			}
		}, {
			"mData" : "nickName",
			"orderable" : false
		}, {
			"mData" : "claimantDate",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				claimantDateOption(nTd, sData);
			}
		}, {
			"mData" : "checkDate",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				checkDataOption(nTd, sData);
			}
		}, {
			"mData" : "descrption",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData,20);
			}
		}, {
			"mData" : "status",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				statusCell(nTd, sData);
			}
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				operation(nTd, sData, oData);
			}
		}]
	});
	
	showSpeakerSelector();
	onSearchSpeaker();
	onFormSubmit();
	onCerModalShow();
	
	$('#searchButton_state').change(function(){
		loadData();
	});
	
})

function renderUserCell(cell,username,uuid){
	var c = $(cell);
	c.empty();
	var item = $('<a href="#">' + username + '</a>')
	item.click(function(e){
		e.preventDefault();
		userInfo(uuid)
	})
	c.append(item);
}

var patt = new RegExp("^(http).*");

//当前审核状态
function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if (isStatus == 0) {
		value = '<span style="color: red;">未审核</span>';
	} else if (isStatus == 1) {
		value = '<span style="color: green;">审核通过</span>';
	} else if (isStatus == 2) {
		value = '<span style="color: red;">审核未通过</span>'
	}
	_p.append(value);
}

var edit_row;

//审核操作
function operation(cell, _uuid, data) {
	var _status = data['status'];
	var pass;
	var reject;
	var undo;
	var _p = $(cell);
	_p.empty();
	
	//if(_status == 0 || _status== 2){ //20160307
	if(_status == 0){
		pass = $('<a href="javascript:void(0);">通过</a>');
		pass.click(function(e) {
			e.preventDefault();
			//if(confirm("审核前请核对该申请者信息,点击“确认”以继续.")){
				edit_row = _p.parent();
				$('#certificationModal').attr("data-id", data['uuid']);
				$('#realname').val(data['realName']);
				$('#speaker_city').val(data['cityName']);
				initCertificationModal(data);
				$('#certificationModal').modal('show');
				/*
				$.post('/certification/status', {
					uuid : _uuid,
					status : 1
				}).always(function(data) {
					if (data.status == 'success') {
						refreshItem(_p.parent().children(), data.data)
					} else {
						if (!!data.message) {
							alert(data.message)
						} else {
							alert(data.status)
						}
					}
				})*/
			//}
		})
		_p.append(pass).append(" | ");
		
		reject = $('<a href="javascript:void(0);">拒绝</a>');
		reject.click(function(e) {
			e.preventDefault();
				$('#refuse_uuid').val(_uuid);
				$('#resufe_confirm_btn').unbind('click');
				$('#resufe_confirm_btn').click(function(e){
					e.preventDefault();
					var refuse_case=$('#refuse_case').val();
					var uuid=$('#refuse_uuid').val();
					if(refuse_case==''){
						alert('请填写拒绝的理由');
						return false;
					}
					$.post('/certification/status',
							{'uuid':uuid,'status':2,'refuse':refuse_case},
							function(data){
								if (data.status == 'success') {
									$('#dataModal_refuse').modal('hide');
									refreshItem(_p.parent().children(), data.data)
								} else {
									if (!!data.message) {
										alert(data.message)
									} else {
										alert(data.status)
									}
								}
							});
				});
				$('#dataModal_refuse').modal('show');
		});
		_p.append(reject);
		
	//} else if(_status == 1){ //20160307
	} else if(_status == 1 || _status== 2){
		undo=$('<a href="javascript:void(0);">撤销</a>');
		undo.click(function(e) {
			e.preventDefault();
			if(confirm("你确定要 撤销 该条审核记录吗？")){
				$.post('/certification/status', {
					uuid : _uuid,
					status : 0
				}).always(function(data) {
					if (data.status == 'success') {
						refreshItem(_p.parent().children(), data.data)
					} else {
						if (!!data.message) {
							alert(data.message)
						} else {
							alert(data.status)
						}
					}
				})			
			}
		})
		_p.append(undo);
	}
}

function refreshItem(tds, data) {
	renderUserCell(tds[1], data['realName'],data['userUuid']);
	renderUserCell(tds[2], data['userName'],data['userUuid']);
	checkDataOption(tds[3], data['nickName']);
	checkDataOption(tds[6], data['descrption']);
	
	claimantDateOption(tds[4], data['claimantDate']);
	checkDataOption(tds[5], data['checkDate']);
	cutOutData(tds[6],data['descrption'],20);
	statusCell(tds[7], data['status']);
	operation(tds[8], data['uuid'], data);
}

//js截取字符串
function cutOutData(cell, val, num) {
	var c = $(cell);
	c.empty();
	val = val.length > num ? val.substr(0, num) + '...' : val;
	c.append(val);
}

function checkDataOption(nTd, sData){
	var _td = $(nTd);
	_td.empty();
	_td.append('' + sData);
}

function claimantDateOption(nTd, sData){
	var _td = $(nTd);
	_td.empty();
	_td.append('' + sData);
}

function imageShow(cell, taskId, title) {
	var parent = $(cell);
	parent.empty();
	if (!!taskId) {
		var img = '<a href=' + taskId + ' title="' + title
			+ '" data-gallery=""><img style="width:50px;" src="'
			+ taskId + '"/></a>';
		parent.append(img);
	}else{
		parent.append('无');
	}
}

function initCertificationModal(data){
	var _id = data['userId'];
	var image = data['imageUrl'];
	var cerData;
	var isAssociationData;
	var relName = data['realName'];
	
	if('' == image){
		$('#image-preview').hide();
	}
	$('#image-preview').attr('src', image);
	$('#image-preview').show();
	
	//加载地区列表
	$.get('/project/getProvinceList', function() {}).always(function(data) {
		buildOption(data, $('#province'), '');
	})
	//加载科室列表
	$.get('/project/getDepartList', function() {}).always(function(_data) {
		buildOption(_data, $('#departments'), '');
	})
	//加载厂商列表
	$.get('/project/getManufacturerList', function() {}).always(function(result) {
		buildOption(result, $('#company'), '');
	})
	
	//获取申请者详情
	$.get("/certification/detail", {userId: _id}).always(function(data) {
		if (data.status == 'success') {
			cerData = data['data'];
			//查询该申请者是否已关联讲者
			$.get("/admin/speaker/speakerAssociationUserOrNot", {userId: _id}).always(function(_data) {
				if (_data.status == 'success') {
					isAssociationData = _data['data'];
					certificationInfoSetUp(cerData, isAssociationData, relName, _id);
				} else {
					if (!!_data.message) {
						alert(_data.message);
						return;
					} else {
						alert(_data.status);
						return;
					}
				}
			})
		} else {
			if (!!data.message) {
				alert(data.message);
				return;
			} else {
				alert(data.status);
				return;
			}
		}
	})
	
	
}

function certificationInfoSetUp(cerData, isAssociationData, relName, userId){
	//test
	console.log(cerData);
	
	var type = cerData['type'];	//1,企业用户	0,医生
	var isAssociation = isAssociationData['bool']; //'true'，已关联讲者	'false'，未关联讲者
	
	$('#username').val(cerData['userName']);
	
	var cityList = cerData['cityList'];
	var hospList = cerData['hosList'];
	
	buildOptionCityAndHospital(cityList,$('#city'));
	buildOptionCityAndHospital(hospList,$('#hospital'));
	
	showSelected(cerData);
	
	$('#professor_doctor').val(cerData['professor']);
	$('#professor_company').val(cerData['professor']);
	
	if(0 == type){
		$('#select_type').find("option[value='0']").prop("selected",true);
		$('#company_detail').hide();
		$('#doctor_detail').show();
		if('true' == isAssociation){
			speakerUuid='';
			$('#no_speaker').hide();
			$('#hasAssociationSpeaker').hide();
			$('#noAssociationSpeaker').hide();
			$('#noAssociationSpeakerNew').hide();
			$('#select_sex').hide();
			$('#submitButton').text('审核');
			$('#creae_speaker').hide();
		}else if('false' == isAssociation){
			$('#hasAssociationSpeaker').hide();
			//讲者推荐
			//recommendSpeaker(relName);
			//$('#noAssociationSpeaker').show();
			
			$('#noAssociationSpeakerNew').show();
			$('#submitButton').text('关联并审核');
			$('#select_sex').show();
			$('#creae_speaker').show();
		}
	}else if(1 == type){
		$('#select_type').find("option[value='1']").prop("selected",true);
		$('#company_detail').show();
		$('#doctor_detail').hide();
		$('#hasAssociationSpeaker').hide();
		$('#noAssociationSpeaker').hide();
		$('#no_speaker').hide();
		$('#noAssociationSpeakerNew').hide();
		$('#submitButton').text('审核');
		$('#select_sex').hide();
		$('#creae_speaker').hide();
	}
	
	$('#select_type').change(function(){
//		showSelected(cerData);
//		buildOptionCityAndHospital(cityList,$('#city'));
//		buildOptionCityAndHospital(hospList,$('#hospital'));
		
		if(0 == $('#select_type').val()){
			$('#company_detail').hide();
			$('#doctor_detail').show();
			if('true' == isAssociation){
				speakerUuid='';
				$('#no_speaker').hide();
				$('#hasAssociationSpeaker').hide();
				$('#noAssociationSpeaker').hide();
				$('#noAssociationSpeakerNew').hide();
				$('#select_sex').hide();
				$('#submitButton').text('审核');
				$('#creae_speaker').hide();
			}else if('false' == isAssociation){
				$('#hasAssociationSpeaker').hide();
				//讲者推荐
				//recommendSpeaker(relName);
				//$('#noAssociationSpeaker').show();
				
				$('#noAssociationSpeakerNew').show();
				$('#submitButton').text('关联并审核');
				$('#select_sex').show();
				$('#creae_speaker').show();
			}
		}else if(1 == $('#select_type').val()){
			$('#company_detail').show();
			$('#doctor_detail').hide();
			$('#hasAssociationSpeaker').hide();
			$('#noAssociationSpeaker').hide();
			$('#submitButton').text('审核');
			$('#no_speaker').hide();
			$('#noAssociationSpeakerNew').hide();
			$('#select_sex').hide();
			$('#creae_speaker').hide();
		}
	})
}

function showSpeakerSelector(){
	/*
	var speaker_selector = $('#speaker_selector');
	$.get('/user/speakers/json', function() {}).always(function(data) {
		if (data.status == 'success') {
			$.each(data.data,function(i, e) {
				speaker_selector.append('<option value="'
										+ e.uuid
										+ '">'
										+ e.name
										+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
										+ (typeof(e.hospitalName) == 'undefined' ? '无' : e.hospitalName)
										+ '</option>')
			})
		}
		speaker_selector.chosen({
			placeholder_text_single : "请选择该资源的讲者",
			no_results_text : "没有匹配到结果"
		});
	})*/
	
	$('#speaker_selector').ajaxChosen({
	  dataType: 'json',
	  type: 'GET',
	  url:'/project/threescreen/search/speaker'},
	  {loadingImg: '/assets/img/loading.gif'
	});

}

function onCerModalShow(){
	$('#certificationModal').on('show.bs.modal', function() {
		$('#speaker_selector_chosen').width(255);
		$('#speaker_detail_search').hide();
	})
}

function buildOption(vo,elem,msg){
	//elem.empty();
	clearContent(elem);
	var itemList=vo.itemList;
	if(!!itemList){
		if(0 == itemList.length){
			clearContent(elem);
			return ;
		}
		$.each(itemList, function(index, item) {
			var option = '<option value='+item.uuid+'>' + item.name
					+ '</option>';
			elem.append(option);
		});
	}else{
		alert(msg);
	}
}

function buildOptionCityAndHospital(vo,elem){
	clearContent(elem);
	if(!!vo){
		if(0 == vo.length){
			clearContent(elem);
			return ;
		}
		$.each(vo, function(index, item) {
			var option = '<option value='+item.uuid+'>' + item.name
					+ '</option>';
			elem.append(option);
		});
	}
}

function clearContent(parent) {
	parent.empty();
	parent.append('<option value="0">请选择</option>');
}

function changeProvince(uuid) {
	clearContent($('#city'));
	clearContent($('#hospital'));
	if (uuid != 0) {
		$.getJSON('/api/webchat/city/' + uuid, function(data) {
			var city = $('#city');
			if (data.itemList) {
				$.each(data.itemList, function(index, item) {
					var option = '<option value=' + item.uuid + '>' + item.name
							+ '</option>';
					city.append(option);
				});
			} else {
				alert("城市数据加载错误");
			}
		});
	}
}

function changeCity(uuid) {
	clearContent($('#hospital'));
	if (uuid != 0) {
		$.getJSON('/api/webchat/hospital/' + uuid, function(data) {
			var hospital = $('#hospital');
			if (data.itemList) {
				$.each(data.itemList, function(index, item) {
					var option = '<option value=' + item.uuid + '>' + item.name
							+ '</option>';
					hospital.append(option);
				});
			} 
		});
	}
}

var speakerUuid;

function onSearchSpeaker(){
	$('#speaker_selector').change(function(){
		$('#no_speaker').hide();
		$('#speaker_detail_search').show();
		speakerUuid = $('#speaker_selector').val();
		$.get("/admin/speaker/json", {speakerUuid: speakerUuid}, function(data){
			if('success' == data.status){
				showSelectedSpeakerDetail(data.data);
			}
		})
	})
}

function showSelectedSpeakerDetail(data){
	$('#speaker_name_search').val(data['speakerName']);
	$('#speaker_sex_search').val(data['sex']);
	$('#speaker_mobile_search').val(data['phoneNum']);
	$('#speaker_city_search').val(data['cityName']);
	$('#speaker_hospital_search').val(data['hospitalName']);
	$('#speaker_department_search').val(data['deparmentName']);
}

function createInfo(){
	var textAre = $('#certificationInfo');
	textAre.empty();
	if(0 == $('#select_type').val()){
//		if('' != $('#realname').val()){
//			textAre.append($('#realname').val()).append(' 认证成功，就职于 ');
//		}
//		if('' != $('#city').val()){
//			textAre.append($('#city').find("option:selected").text()).append(',');
//		}
//		if('' != $('#hospital').val()){
//			textAre.append($('#hospital').find("option:selected").text());
//		}
		if('' != $('#city').val()){
			textAre.append($('#city').find("option:selected").text());
		}
		if('' != $('#hospital').val()){
			textAre.append($('#hospital').find("option:selected").text());
		}
		if('' != $('#departments').val()){
			textAre.append($('#departments').find("option:selected").text());
		}
		if('' != $('#professor_doctor').val()){
			textAre.append($('#professor_doctor').val());
		}
	}
	if(1 == $('#select_type').val()){
		if('' != $('#company').text()){
			textAre.append("企业 ").append($('#company').find("option:selected").text()).append(' 认证成功');
		}
	}
}

function recommendSpeaker(relName){
	$.get('/project/threescreen/search/speaker', {q: relName}).always(function(_data) {
		if(0 != _data.items.length){
			//获取讲者uuid，查询详细信息展示
			speakerUuid = _data['items'][0]['id'];
			$.get("/admin/speaker/json", {speakerUuid: speakerUuid}, function(data){
				if('success' == data.status){
					showSelectedSpeakerDetail(data.data);
					$('#speaker_log').text('推荐关联以下讲者,或者点击上方按钮重新检索');
					$('#no_speaker').hide();
					$('#speaker_detail_search').show();
				}
			})
		}else{
			//$('#no_speaker').show();
		}
	})
}

function showSelected(cerData){
	if(!!cerData['provinceUuid']){
		$('#province option[value=' + cerData.provinceUuid + ']').attr("selected","selected");
	}
	if(!!cerData['cityUuid']){
		$('#city option[value=' + cerData.cityUuid + ']').attr("selected","selected");
	}
	if(!!cerData['hospitalUuid']){
		$('#hospital option[value=' + cerData.hospitalUuid + ']').attr("selected","selected");
	}
	if(!!cerData['departmentUuid']){
		$('#departments option[value=' + cerData.departmentUuid + ']').attr("selected","selected");
	}
	if(!!cerData['companyUuid']){
		$('#company option[value=' + cerData.companyUuid + ']').attr("selected","selected");
	}
}

function onFormSubmit(){
	$('#submitButton').unbind('click');
	$('#submitButton').click(function(){
		$.post('/certification/status/before', {
			userRealName : $('#realname').val(),
			attestationType : $('#select_type').val(),
			provinceUuid : $('#province').val(),
			cityUuid : $('#city').val(),
			hospitalUuid : $('#hospital').val(),
			departmentsUuid : $('#departments').val(),
			professorDoctor : $('#professor_doctor').val(),
			companyUuid : $('#company').val(),
			professorCompany : $('#professor_company').val(),
			certificationInfo : $('#certificationInfo').val(),
			uuid : $('#certificationModal').attr("data-id"),
			status : 1,
			speakerPhone : $('#username').val(),
			speakerSex : $('#sex').val()
		}).always(function(data) {
			if (data.status == 'success') {
				$('#certificationModal').modal('hide');
				refreshItem(edit_row.children(), data.data);
			} else {
				if (!!data.message) {
					alert(data.message)
					//$('#certificationModal').modal('hide');
				} else {
					alert(data.status)
					//$('#certificationModal').modal('hide');
				}
			}
		})
	})
}

function loadData(){
	var state=$('#searchButton_state option:selected').val();
	oTable.fnReloadAjax("/certification/datatable?state=" + state );
}
