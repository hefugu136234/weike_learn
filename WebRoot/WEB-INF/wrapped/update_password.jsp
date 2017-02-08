<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js">	
</script>
<script type="text/javascript" src="/assets/js/formValidation.min.js">
</script>
<script type="text/javascript" src="/assets/js/formvalidation.js">
</script>
<%
	String status = (String) request.getAttribute("status");
%>
<div class="ibox float-e-margins">
	<div class="ibox-title">
		<h5>修改密码</h5>
	</div>
	<div id="update_password_form" class="ibox-content" style="display: block;">
	  	<form id="update_password_form"  class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label">原始密码</label>
				<div class="col-sm-5 controls">
					<input type="password" class="form-control" id="oldPassword" name="oldPassword"
						autocomplete="off" required="required" />
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">新密码</label>
				<div class="col-sm-5">
					<input type="password" class="form-control" name="password"
						id="password" autocomplete="off" required="required"/>
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			<div class="form-group">
				<label class="col-sm-2 control-label">重复密码</label>
				<div class="col-sm-5">
					<input type="password" class="form-control" id="confirmPassword"
						name="confirmPassword" autocomplete="off" required="required"/>
				</div>
			</div>
			<div class="hr-line-dashed"></div>
			
			<div class="form-group">
				<div class="col-sm-4 col-sm-offset-2">
					<button class="btn btn-white" type="button">取消</button>
					<button class="btn btn-primary" type=submit onclick="onsummit()">保存</button>
				</div>
			</div>
		  </form>
	</div>
</div>
<script>
	$(document).ready(function() {
		//showActive([ 'admin_user_mgr', 'admin_user_create_nav' ]);
		
		$('#update_password_form').formValidation({
		    framework: 'bootstrap',
		    fields: {
/**		    	oldPassword:{
		    		validators:{
/**		    			callback:{
		    				 message: 'The original password is wrong',
	                            callback: function (value, validator, $field) {
	                            	var mark=0;
	                            	console.log(value);
	                            	$.get("user/validat/oldpassword",{'oldPassword':value},function(data){
	                            		console.log(data);
	                            		if(data.status=='success')
	                            			mark=1;
	                    				  });
	                            	console.log(mark);
	            		    	if(mark==1)return true;
	            		    	return false;
	                            }
		    				
		    			}
		    			ajax:{
		    				url:'user/validat/oldpassword',
		    				type: 'GET',
		    				data: {"oldPassword":$('oldPassword').val()},
		    				success:function(result){
		    					console.log(result);
		    				}
		    			}
		    			
		    		}
		    	},*/
		        confirmPassword: {
		            validators: {
		                identical: {
		                    field: 'password',
		                    message: 'The password and its confirm are not the same'
		                }
		            }
		        }
		    }
		});
		
	});
	<%if (status != null) {%>
		alert('<%=status%>');
<%}%>

 function onsummit(){
	 var password=$('#password').val();
	 var oldPassword=$('#oldPassword').val();
	 var confirmPassword=$('#confirmPassword').val();
	 if(oldPassword==''){
		 return false;
	 }else if(password==''){
		 return false;
	 }else if(confirmPassword==''){
		 return false;
	 }else if(password!=confirmPassword){
		 return false;
	 }
	$.post('/user/update/password',{'oldPassword':oldPassword,'password':password},function(data){
		console.log(data);
		var obj = eval( "(" + data + ")" );
		alert(obj.status);
			if(obj.status=='success'){
				window.location.href='/admin/user/logout';
		}
		  });
} 
	
</script>