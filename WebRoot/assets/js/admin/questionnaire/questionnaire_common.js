$(function(){
	showActive([ 'questionnaire_list_nav', 'questionnaire_mgr' ]);
	
	$('#questionnaire_form').validate({
		ignore: ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : false,
		messages : {
			name:"请输入问卷名称",
			urlLink:"请输入正确的问卷链接",
			mark:"请输入问卷简介"
		},

		highlight : function(element) {
			$(element).closest('.form-group').addClass('has-error');
		},

		success : function(label) {
			label.closest('.form-group').removeClass('has-error');
			label.remove();
		},

		errorPlacement : function(error, element) {
			element.parent('div').append(error);
		},
		
		submitHandler : function(form) {
			submitForm(form);
		}
	});
});

function buildId(val){
	if(val==''){
		$('#que_link_error').show();
		return ;
	}
	val=val.split('/');
	//console.log(val);
	if(val.length==0){
		$('#que_link_error').show();
		return ;
	}
	if(!val[0].startsWith('http')){
		$('#que_link_error').show();
		return ;
	}
	var has_s=false;
	var m_i=0;
	$.each(val,function(i,t){
		if(t=='s'){
			m_i=i;
			has_s=true;
			return false;
		}
	});
	if(!has_s){
		$('#que_link_error').show();
		return ;
	}
	m_i+=1;
	if(m_i>=val.length){
		$('#que_link_error').show();
		return ;
	}
	val=val[m_i];
	if(val==''){
		$('#que_link_error').show();
		return ;
	}
	$('#urlLink').val(val);
	$('#que_link_error').hide();
};