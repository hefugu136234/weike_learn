<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<title>客户端接口</title>
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<link href="/assets/css/bootstrap.min.css" rel="stylesheet">
<link href="/assets/font-awesome/css/font-awesome.css" rel="stylesheet">
<style type="text/css">
.api-container {
	list-style-type: decimal;
}

.api-container li {
	margin-top: 10px;
	background-color: #FFFFF6;
}

.api-container li:HOVER {
	background-color: #CCCCCC;
}

.api-item {
	min-height: 80px;
}

.api-item span {
	font-size: 16px;
	font-weight: bold;
}

.api-item table {
	margin-top: 10px;
	width: 80%;
}

.api-item table .head {
	width: 80px;
	text-align: right;
	font-size: 14px;
}
</style>
</head>
<body>

	<div class='container-fluid' style="margin-top: 20px">
		<!--
		选项卡：通过BS的类来控制选项卡的样式
　　　　 ?处可以换成以下几个类
　　　　 .nav-tabs:如图1
　　　　 .nav-pills:如图2
　　　　 .nav-tabs nav-stacked:如图3
	-->
		<ul class='nav nav-tabs'>
			<li class='active'><a href='#'>盒子接口</a></li>
			<li><a href='#'>App接口</a></li>
		</ul>
	</div>

	<ul id="tv_container" class="api-container">
		<li class="api-item"><span>盒子登录</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/tv/authorize</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">POST</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">username、password、device(设备的唯一识别码，如mac_addr)</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">{ "token":
						"MTg3ZmI1ZDItY2RiOC00NDE2LWFjNTctMWMwOGIyMWYzOTVkNg==",
						"username": "fang", "nickname": "方", "createdDate": "Jun 16, 2015
						6:11:08 PM", "status": "success" }</td>
				</tr>
			</table></li>

		<li class="api-item"><span>盒子登录token刷新</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/tv/authorize/renew</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">POST</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">原token、device(设备的唯一识别码，如mac_addr)</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">{ "token":
						"MTg3ZmI1ZDItY2RiOC00NDE2LWFjNTctMWMwOGIyMWYzOTVkNg==",
						"username": "fang", "nickname": "方", "createdDate": "Jun 16, 2015
						6:11:08 PM", "status": "success" }</td>
				</tr>
			</table></li>

		<li class="api-item"><span>预约二维码</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/tv/yy/qr</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">token</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">{ items: [ { type: "cz", qr_url:
						"http://xxx.png", description: "初诊" }, { type: "xy", qr_url:
						"http://xxx.png", description: "上门洗牙" }, { type: "fz", qr_url:
						"http://xxx.png", description: "复诊" } ], status: "success" }</td>
				</tr>
			</table></li>


		<li class="api-item"><span>获取预约的数据</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/tv/yy/qr</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">token</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">{ items: [ { type: "cz", qr_url:
						"http://xxx.png", description: "初诊" }, { type: "xy", qr_url:
						"http://xxx.png", description: "上门洗牙" }, { type: "fz", qr_url:
						"http://xxx.png", description: "复诊" } ], status: "success" }</td>
				</tr>
			</table></li>

		<li class="api-item"><span>新闻列表</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/tv/news/list</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">token（必须），latestTime(查询的起始时间，必须)，pageSize（字符串类型，每次查询的条数，必须），categoryUuid（种类的uuid，必须）</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">{ "newsList": [ { "uuid":
						"38b561a2-f301-4181-879e-68a98539dc34", "title": "阿斯达",
						"createDate": "2015-08-04 16:48:18", "author": "按时大大", "label":
						"大撒旦", "summary": "阿斯达", "categoryName": "慈善", "categoryUuid":
						"1aaa30d5-68be-4fdd-9859-bbcea3bfb351" } ], "status": "success" }</td>
				</tr>
			</table></li>

		<li class="api-item"><span>新闻详情</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/tv/news/detail/{uuid}</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">token（必须），uuid（新闻的uuid，必须）</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">{ "item": { "uuid":
						"38b561a2-f301-4181-879e-68a98539dc34", "title": "阿斯达",
						"createDate": "2015-08-04 16:48:18", "content": "
						<p>阿萨达大厦大 阿打算打</p>", "author": "按时大大", "label": "大撒旦", "summary":
						"阿斯达", "categoryName": "慈善", "categoryUuid":
						"1aaa30d5-68be-4fdd-9859-bbcea3bfb351" }, "status": "success" }
					</td>
				</tr>
			</table></li>
	</ul>


	<ul class="api-container" style="display: none;">
		<li class="api-item"><span style="color: red;">注：所有返回的json,除‘status’是必须返回外，其他的
				字段当其值为null时，不在返回。<br /> 如当返回值中有一个‘items’为list类型，正常值为--{ items: [ {
				uuid: "0adcf0f6-1030-11e5-be70-a9e47cbf4a34", createDate: "Jun 11,
				2015 7:52:30 PM"} ], status:"success" }<br />
				但是当items为null时，返回值可能为：{items[],status:"success"}或{status;"success"}<br />
				同理，当items为一个对象为null时，将不返回

		</span></li>
		<li class="api-item"><span>APP登录</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/authorize</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">POST</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">username password</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">{ "token":
						"ZDEzYzBiZGQtNzc3OC00OWJlLThiNWYtMzM5ZTMyNmI0YmEwNA==", "user": {
						"id": "8c2bb695-7e1a-490a-a9d0-ed3ea3ded769", "username": "asdf",
						"nickname": "asdfasdf", "email": "asdfa@163.com", "address":
						"asdfadsf", "avatar": "http://asd.png" }, "status": "success" }</td>
				</tr>
			</table></li>

		<li class="api-item"><span>发送注册验证码</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/user/register/code/send</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">POST</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">mobile(必须) device(可选,设备的mac_addr)</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json</td>
				</tr>
			</table></li>




		<li class="api-item"><span>验证码的验证</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/user/register/code/valid</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">POST</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">code（必须，验证码）， mobile(必须，手机号码)</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json 如 { "status": "success", "valid_code":
						"7a185855-0be9-4963-ba15-d93b089462ba" }</td>
				</tr>

				<tr>
					<td class="head" style="color: red">注*：</td>
					<td class="body">为配合测试，验证码值为 123456</td>
				</tr>

			</table></li>

		<li class="api-item"><span>保存注册用户</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/user/register/save</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">POST</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">valid_code(必须，验证成功返回的值),password(必须，密码)
						，nickname（可选， 姓名）</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">同用户登录成功的返回值</td>
				</tr>
			</table></li>

		<li class="api-item"><span>预约类型<span style="color: red;">(过期)</span></span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/subscribe/type</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">token(必须)</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json</td>
				</tr>
			</table></li>



		<li class="api-item"><span>保存预约信息<span
				style="color: orange;">(修改15-7-28)</span></span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/subscribe/save</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">POST</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">token(不是必须，当用户登录后，添加此参数),clinicUuid（必须，关联的诊所uuid）,nickname（必须，患者的姓名）,phone（必须，患者的手机号码）,mark(可选),effectDate（必须，预约的时间，格式为：yyyy-MM-dd
						HH:mm:ss）,typeUuid（必须--预约类型）</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json</td>
				</tr>
			</table></li>

		<li class="api-item"><span>新闻列表</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/news/list</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">无</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json：{ "items": [ { "uuid":
						"ed671f8f-1467-4e1c-9eb6-8ddf12146f17", "title": "测试标题",
						"createDate": "2015-07-27 17:54:59", "author": "测试作者", "label":
						"标签1/标签2", "summary": "测试摘要" }, { "uuid":
						"39cc3a1c-347d-42b8-bc6a-f4182d7decfb", "title": "7月25的正文",
						"createDate": "2015-07-27 18:01:55", "author": "7.25", "label":
						"7、25", "summary": "摘要25" } ], "status": "success" }</td>
				</tr>
			</table></li>

		<li class="api-item"><span>单条新闻的详情</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/news/detail/{uuid}</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">uuid(必须，新闻的uuid)</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json：{ "item": { "uuid":
						"ed671f8f-1467-4e1c-9eb6-8ddf12146f17", "title": "测试标题",
						"createDate": "2015-07-27 17:54:59", "content": "
						<p>
							测试内容<br />
						</p>", "author": "测试作者", "label": "标签1/标签2", "summary": "测试摘要" },
						"status": "success" }
					</td>
				</tr>
			</table></li>

		<li class="api-item"><span>获取预约中诊所和省的数据</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/subscribe/clinic/type</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">无</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json：{ "typeList": [ { "uuid":
						"73f4cb3d-6119-42ac-8c71-d189605df1d6", "type": "涂氟" }, { "uuid":
						"bfdebd92-5cad-4571-8065-f7771555935d", "type": "美白修复" } ],
						"provinceList": [ { "uuid":
						"49aaaab7-c235-4601-96f2-68d90ac1d624", "name": "北京市" }, { "uuid":
						"bb79c6c7-13b4-4d33-8602-6226cf4ee6d9", "name": "天津市" } ],
						"status": "success" }</td>
				</tr>
			</table></li>

		<li class="api-item"><span>获取省的列表</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/province/list</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">无</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json：{"provinceList": [ { "uuid":
						"49aaaab7-c235-4601-96f2-68d90ac1d624", "name": "北京市" }, { "uuid":
						"bb79c6c7-13b4-4d33-8602-6226cf4ee6d9", "name": "天津市" }],
						"status": "success" }</td>
				</tr>
			</table></li>

		<li class="api-item"><span>获取市的列表</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/city/list/{uuid}</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">uuid(省的uuid，必选)</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json：{ "cityList": [ { "uuid":
						"2753caa7-59c1-4673-953a-fd1e2232b5a5", "name": "北京市" } ],
						"status": "success" }</td>
				</tr>
			</table></li>

		<li class="api-item"><span>根据市选诊所数据</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/clinic/list/{uuid}</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">uuid(省的uuid，必选)</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json：{ "clinicList": [ { "uuid":
						"0adcf0f6-1030-11e5-be70-a9e47cbf4a34", "name": "翔宇齿科修改22",
						"namePinyin": "xiangyuchikexiugai22", "contactPhone":
						"13564851455", "description": "我们改222" } ], "status": "success" }</td>
				</tr>
			</table></li>


		<li class="api-item"><span>个人预约列表</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/subscribe/list</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">GET</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">token(必须，登录的token)</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json：{ "items": [ { "uuid":
						"2f84cbc9-d963-477c-bf97-be5286a0bc2a", "name": "无名", "typeName":
						"牙痛", "effectDate": "2015-07-29 16:00:00", "mark": "", "status":
						"预约成功" }, { "uuid": "d1f3a0da-9281-4148-a3c6-eaf13d194227",
						"name": "无名", "typeName": "烤瓷牙", "effectDate": "2015-07-29
						16:00:00", "mark": "", "status": "预约成功" } ], "status": "success" }</td>
				</tr>
			</table></li>

		<li class="api-item"><span>修改密码</span>
			<table>
				<tr>
					<td class="head">请求URI：</td>
					<td class="body">/api/app/user/password/update</td>
				</tr>
				<tr>
					<td class="head">请求类型：</td>
					<td class="body">POST</td>
				</tr>
				<tr>
					<td class="head">请求参数：</td>
					<td class="body">token(必须，登录的token),oriPassword=原始密码,newPassword=要修改的新密码</td>
				</tr>
				<tr>
					<td class="head">返回数据：</td>
					<td class="body">json：{"status": "success"}</td>
				</tr>
			</table></li>

	</ul>

</body>
<script type="text/javascript">
	$('ul.nav-tabs li a').click(function(e) {
		$('.api-container').hide()
		$('ul.nav-tabs li.active').removeClass('active')
		$(this).parent('li').addClass('active')
		$('ul.api-container').eq($(this).parent().index()).show()
	})
</script>
</html>